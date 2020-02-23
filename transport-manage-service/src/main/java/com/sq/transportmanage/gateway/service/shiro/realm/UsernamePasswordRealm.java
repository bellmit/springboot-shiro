package com.sq.transportmanage.gateway.service.shiro.realm;

import com.sq.transportmanage.gateway.dao.entity.mdbcarmanage.CarAdmUser;
import com.sq.transportmanage.gateway.service.service.authc.MyDataSourceService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**认证  与  权限  **/
@Component
public class UsernamePasswordRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordRealm.class);

	@Autowired
	private MyDataSourceService myDataSourceService;
	
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
			loginUser.setType( null );   //
			loginUser.setStatus( adMUser.getStatus() );           //状态
			loginUser.setAccountType( adMUser.getAccountType() );   //自有的帐号类型：[100 普通用户]、[900 管理员]
			loginUser.setLevel(adMUser.getLevel());
			//---------------------------------------------------------------------------------------------------------数据权限BEGIN

			logger.info( "[获取用户的身份认证信息]="+loginUser);
			return new SimpleAuthenticationInfo(loginUser, authenticationToken.getCredentials()  ,  this.getName() );
		} catch (Exception e) {
			logger.error("获取用户的身份认证信息异常",e);
			return null;
		}
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
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