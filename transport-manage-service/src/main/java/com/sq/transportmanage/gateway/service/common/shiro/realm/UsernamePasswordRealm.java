package com.sq.transportmanage.gateway.service.common.shiro.realm;

import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRoleExMapper;
import com.sq.transportmanage.gateway.service.auth.MyDataSourceService;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.util.MD5Utils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**认证  与  权限  **/

/**
 * 这个就是shiro SSOLogin 的用户获取的属性配置
 */
@Component
public class UsernamePasswordRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordRealm.class);

	@Autowired
	private MyDataSourceService myDataSourceService;

	@Autowired
	private SaasPermissionExMapper saasPermissionExMapper;

	@Autowired
	private SaasRoleExMapper saasRoleExMapper;
	
    /**重写：获取用户的身份认证信息**/
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException{
		logger.info( "[获取用户的身份认证信息开始]authenticationToken="+authenticationToken);
		try {
			UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
			CarAdmUser adMUser = myDataSourceService.queryByAccount(token.getUsername());
			SSOLoginUser loginUser = new SSOLoginUser();  //当前登录的用户
			loginUser.setId( adMUser.getUserId() );                //用户ID
			loginUser.setLoginName( adMUser.getAccount() );//登录名
			loginUser.setMobile( adMUser.getPhone() );         //手机号码
			loginUser.setName( adMUser.getUserName() );    //真实姓名
			loginUser.setEmail(adMUser.getEmail()); //邮箱地址
			loginUser.setType(null);   //
			loginUser.setStatus( adMUser.getStatus() );           //状态
			loginUser.setAccountType( adMUser.getAccountType() );   //自有的帐号类型：[100 普通用户]、[900 管理员]
			loginUser.setLevel(adMUser.getLevel());
			loginUser.setMerchantId(adMUser.getMerchantId());
			loginUser.setSupplierIds(adMUser.getSuppliers());
			String md5= null;
			try {
				md5 = MD5Utils.getMD5DigestBase64(loginUser.getMerchantId());
			} catch (NoSuchAlgorithmException e) {
				logger.info("sign error" + e);
			}
			if(Constants.MANAGE_MD5.equals(md5)){
				loginUser.setSuper(true);
			}else {
				loginUser.setSuper(false);
			}
			List<String> menuUrlList = saasPermissionExMapper.queryPermissionMenussOfUser(adMUser.getUserId());
			loginUser.setMenuUrlList(menuUrlList);
			/**当前用户所拥有的菜单权限**/
			List<Integer> permissionIds = saasPermissionExMapper.queryPermissionIdsOfUser(adMUser.getUserId());
            /**查询所有的一级菜单**/
			if(!CollectionUtils.isEmpty(permissionIds)){
				List<SaasPermission> permissionList = saasPermissionExMapper.queryModularPermissions(permissionIds);
				Map<Integer,String> map = Maps.newHashMap();
				permissionList.forEach(list ->{
					map.put(list.getPermissionId(),list.getPermissionName());
				});
				loginUser.setMenuPermissionMap(map);
			}

			//---------------------------------------------------------------------------------------------------------数据权限BEGIN

			logger.info( "[获取用户的身份认证信息]="+loginUser);
			return new SimpleAuthenticationInfo(loginUser, authenticationToken.getCredentials()  ,  this.getName() );
		} catch (Exception e) {
			logger.error("获取用户的身份认证信息异常",e);
			return null;
		}
	}


	/**
	 * 查询角色登录进来所拥有的菜单时候shiro实现
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SSOLoginUser loginUser = (SSOLoginUser) principalCollection.getPrimaryPrincipal();
		String account = loginUser.getLoginName(); //登录名

		List<String> perms_string = saasPermissionExMapper.queryPermissionCodesOfUser(  loginUser.getId() );
		List<String> roles_string   = saasRoleExMapper.queryRoleCodesOfUser( loginUser.getId() );

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = new HashSet<String>( roles_string );
		authorizationInfo.setRoles( roles );
		logger.info( "[获取用户授权信息(角色)] "+account+"="+roles);

		Set<String> perms = new HashSet<String>( perms_string );
		authorizationInfo.setStringPermissions(perms);
		logger.info( "[获取用户授权信息(权限)] "+account+"="+perms);
		return authorizationInfo;
	}

	@Override
    public Object getAuthorizationCacheKey(PrincipalCollection principals) {
		SSOLoginUser loginUser = (SSOLoginUser) principals.getPrimaryPrincipal();
		String account = loginUser.getLoginName(); //登录名
        return "-AuthInfo-"+account;
    }
	

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}