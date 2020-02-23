package com.sq.transportmanage.gateway.service.shiro.session;


import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.service.authc.MyDataSourceService;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**当前登录用户 工具类**/
@Component
public final class WebSessionUtil{

	public static MyDataSourceService myDataSourceService;

	@Autowired
	MyDataSourceService myDataSourceServiceTemp;

	@PostConstruct
	public void init(){
		myDataSourceService = myDataSourceServiceTemp;
	}

	/**获取当前的登录用户**/
	public static SSOLoginUser getCurrentLoginUser() {

		Subject subject = SecurityUtils.getSubject();
		return (SSOLoginUser) subject.getPrincipal();

	}
	/**是否为：车管后台-超级管理员**/
	public static boolean isSupperAdmin() {
		Subject subject = SecurityUtils.getSubject();
		return subject.hasRole(SaasConst.SYSTEM_ROLE);
//		return subject.hasRole("car_manage_admin");
	}
	
	/**设置会话属性**/
	public static void setAttribute(String key, Object value) {
		Subject subject = SecurityUtils.getSubject();
		if(subject!=null) {
			subject.getSession().setAttribute(key, value);
		}
	}
	/**移除会话属性**/
	public static void removeAttribute(String key) {
		Subject subject = SecurityUtils.getSubject();
		if(subject!=null) {
			subject.getSession().removeAttribute(key);
		}
	}
	/**查询会话属性**/
	public static Object getAttribute(String key) {
		Subject subject = SecurityUtils.getSubject();
		if(subject!=null) {
			return subject.getSession().getAttribute(key);
		}
		return null;
	}

}