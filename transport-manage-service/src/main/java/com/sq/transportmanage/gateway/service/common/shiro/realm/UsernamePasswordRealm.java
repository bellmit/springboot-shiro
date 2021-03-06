package com.sq.transportmanage.gateway.service.common.shiro.realm;

import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRoleExMapper;
import com.sq.transportmanage.gateway.service.auth.DataPermissionService;
import com.sq.transportmanage.gateway.service.auth.MyDataSourceService;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import com.sq.transportmanage.gateway.service.util.MD5Utils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**认证  与  权限  **/

/**@Author fanht
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

	@Autowired
	@Qualifier("sessionDAO")
	private RedisSessionDAO redisSessionDAO;

	@Autowired
	private DataPermissionService dataPermissionService;
	
    /**重写：获取用户的身份认证信息**/
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException{
		logger.info( "[获取用户的身份认证信息开始]authenticationToken="+authenticationToken);
		try {
			UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
			CarAdmUser carAdmUser = myDataSourceService.queryByAccount(token.getUsername());


			//处理session 防止一个账号多处登录
			try {
				redisSessionDAO.clearRelativeSession(null,null,carAdmUser.getUserId());
			} catch (Exception e) {
				logger.info("=========清除session异常============");
			}

			/***当前登录的用户*/
			SSOLoginUser loginUser = new SSOLoginUser();

			//如果是超级管理员
			if(Constants.SUPER_MANAGE.equals(carAdmUser.getAccountType()) ){
				logger.info( "[获取用户的身份认证信息]="+loginUser);
				loginUser = this.ssoLoginUser(loginUser,carAdmUser);
				loginUser.setMerchantArea(carAdmUser.getMerchantArea());
				Integer minUserId = myDataSourceService.queryMinUserId(carAdmUser.getMerchantId());
				loginUser.setId(minUserId);
				loginUser.setSuper(false);
				dataPermissionService.populateLoginUser(loginUser);
				return new SimpleAuthenticationInfo(loginUser, authenticationToken.getCredentials()  ,  this.getName() );
			}
			loginUser = this.ssoLoginUser(loginUser,carAdmUser);
			dataPermissionService.populateLoginUser(loginUser);
			String md5= null;
			try {
				md5 = MD5Utils.getMD5DigestBase64(loginUser.getMerchantId().toString());
			} catch (NoSuchAlgorithmException e) {
				logger.info("sign error" + e);
			}
			if(Constants.MANAGE_MD5.equals(md5)){
				loginUser.setSuper(true);
			}else {
				loginUser.setSuper(false);
			}
			List<String> menuUrlList = saasPermissionExMapper.queryPermissionMenussOfUser(carAdmUser.getUserId());
			loginUser.setMenuUrlList(menuUrlList);
			/**当前用户所拥有的菜单权限**/
			List<Integer> permissionIds = saasPermissionExMapper.queryPermissionIdsOfUser(carAdmUser.getUserId());

			List<Byte> permissionTypes =  Arrays.asList( new Byte[] { SaasConst.PermissionType.MENU });

			Map<Integer,List<SaasPermissionDTO>> mapPermission = Maps.newHashMap();

			/**查询所有的一级菜单**/
			if(!CollectionUtils.isEmpty(permissionIds)){
				List<SaasPermission> permissionList = saasPermissionExMapper.queryModularPermissions(permissionIds);
				Map<Integer,String> map = Maps.newHashMap();
				permissionList.forEach(list ->{
					map.put(list.getPermissionId(),list.getPermissionName());
					//查询所有一级菜单下的子菜单 以树形结果返回
					List<SaasPermissionDTO> menuPerms = this.getChildren( permissionIds , list.getPermissionId(), permissionTypes);
					mapPermission.put(list.getPermissionId(),menuPerms);
				});

				loginUser.setMenuPermissionMap(map);
				loginUser.setMapPermission(mapPermission);
			}
			//




			//---------------------------------------------------------------------------------------------------------数据权限BEGIN

			logger.info( "[获取用户的身份认证信息]="+loginUser);
			return new SimpleAuthenticationInfo(loginUser, authenticationToken.getCredentials()  ,  this.getName() );
		} catch (Exception e) {
			logger.error("获取用户的身份认证信息异常",e);
			return null;
		}
	}


	/**
	 * 查询每个一级菜单下的子菜单
	 * @param permissionIds
	 * @param parentPermissionId
	 * @param permissionTypes  tree 树形，list 列表
	 * @return
	 */
	private List<SaasPermissionDTO> getChildren( List<Integer> permissionIds,  Integer parentPermissionId,  List<Byte> permissionTypes ){
		List<SaasPermission> childrenPos = saasPermissionExMapper.queryPermissions(permissionIds, parentPermissionId, null, permissionTypes, null, null);
		if(childrenPos==null || childrenPos.size()==0) {
			return null;
		}
		//递归
		List<SaasPermissionDTO> childrenDtos = BeanUtil.copyList(childrenPos, SaasPermissionDTO.class);
		Iterator<SaasPermissionDTO> iterator = childrenDtos.iterator();
		while (iterator.hasNext()) {
			SaasPermissionDTO childrenDto = iterator.next();
			List<SaasPermissionDTO> childs = this.getChildren( permissionIds, childrenDto.getPermissionId() ,  permissionTypes );
			childrenDto.setChildPermissions(childs);
		}
		return childrenDtos;
	}


	/**
	 * 查询角色登录进来所拥有的菜单时候shiro实现
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SSOLoginUser loginUser = (SSOLoginUser) principalCollection.getPrimaryPrincipal();
		String account = loginUser.getLoginName();



		List<String> permString = saasPermissionExMapper.queryPermissionCodesOfUser(  loginUser.getId() );
		List<String> rolesString   = saasRoleExMapper.queryRoleCodesOfUser( loginUser.getId() );

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = new HashSet<String>( rolesString );
		authorizationInfo.setRoles( roles );
		logger.info( "[获取用户授权信息(角色)] "+account+"="+roles);

		Set<String> perms = new HashSet<String>( permString );
		authorizationInfo.setStringPermissions(perms);
		logger.info( "[获取用户授权信息(权限)] "+account+"="+perms);
		return authorizationInfo;
	}

	@Override
    public Object getAuthorizationCacheKey(PrincipalCollection principals) {
		SSOLoginUser loginUser = (SSOLoginUser) principals.getPrimaryPrincipal();
		String account = loginUser.getLoginName();
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


    private SSOLoginUser ssoLoginUser (SSOLoginUser loginUser ,CarAdmUser admUser){
		/** 用户ID*/
		loginUser.setId( admUser.getUserId() );
		loginUser.setLoginName( admUser.getAccount() );
		loginUser.setMobile( admUser.getPhone() );
		loginUser.setName( admUser.getUserName() );
		loginUser.setEmail(admUser.getEmail());
		loginUser.setType(null);
		loginUser.setStatus( admUser.getStatus() );
		/**自有的帐号类型：[100 普通用户]、[900 管理员]*/
		loginUser.setAccountType( admUser.getAccountType() );
		loginUser.setLevel(admUser.getLevel());
		loginUser.setMerchantId(admUser.getMerchantId());
		loginUser.setSupplierIds(admUser.getSuppliers());
		loginUser.setCityIds(admUser.getCities());
		loginUser.setTeamIds(admUser.getTeamId());
		loginUser.setGroupIds(admUser.getGroupIds());
		loginUser.setDataLevel(admUser.getDataLevel());
		return loginUser;
	}
}