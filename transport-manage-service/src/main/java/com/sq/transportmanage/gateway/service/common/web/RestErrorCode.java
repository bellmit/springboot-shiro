package com.sq.transportmanage.gateway.service.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义错误码与错误提示
 * @author zhaoyali
 */
public final class RestErrorCode{

	private static final Logger log = LoggerFactory.getLogger(RestErrorCode.class);
	/**错误码与错误文字的映射关系*/
	private static Map<Integer,String> codeMsgMappings  = new HashMap<Integer,String>();

	/**-----------------------------------------------系统参数*/
	@ResultMessage("成功")
	public static final int SUCCESS                                   = 0;
	@ResultMessage("获得互斥锁超时")
	public static final int GET_LOCK_TIMEOUT                = 1;
	@ResultMessage("参数错误")
	public static final int PARAMS_ERROR                 =400;
	@ResultMessage("缺少授权")
	public static final int HTTP_UNAUTHORIZED             = 401;
	@ResultMessage("禁止访问")
	public static final int HTTP_FORBIDDEN                     = 403;
	@ResultMessage("资源不存在")
	public static final int HTTP_NOT_FOUND                   = 404;
	@ResultMessage("系统内部发生错误")
	public static final int HTTP_SYSTEM_ERROR              = 500;
	@ResultMessage("服务错误：{0}")
	public static final int CAR_API_ERROR           = 996;
	@ResultMessage("need login!")
	public static final int HTTP_INVALID_SESSION           = 997;
	@ResultMessage("请求参数校验不通过{0}")
	public static final int HTTP_PARAM_INVALID              = 998;
	@ResultMessage("未知错误")
	public static final int UNKNOWN_ERROR                   = 999;
	@ResultMessage("记录操作失败")
	public static final int RECORD_DEAL_FAILURE                   = 501;



	/**-----------------------------------------------用户*/
	@ResultMessage("获取验证码太频繁,请{0}分钟后重试")
	public static final int GET_MSGCODE_EXCEED           = 1000;
	@ResultMessage("用户不存在")
	public static final int USER_NOT_EXIST                      = 1001;
	@ResultMessage("用户名或密码错误")
	public static final int USER_PASSWORD_WRONG      = 1002;
	@ResultMessage("用户已被禁用")
	public static final int USER_INVALID                          = 1003;
	@ResultMessage("登录失败")
	public static final int USER_LOGIN_FAILED                = 1004;
	@ResultMessage("短信验证码已经失效")
	public static final int MSG_CODE_INVALID                = 1005;
	@ResultMessage("短信验证码不正确")
	public static final int MSG_CODE_WRONG                = 1006;
	@ResultMessage("登录账号不能重复")
	public static final int ACCOUNT_EXIST                      = 1007;
	@ResultMessage("邮箱不能重复")
	public static final int EMAIL_EXIST                      = 1009;
	@ResultMessage("短信验证码发送失败")
	public static final int MSG_CODE_FAIL                      = 1011;
	@ResultMessage("短信验证码{0}秒内不能重复发送")
	public static final int MSG_CODE_REPEAT_SEND                      = 1012;
	@ResultMessage("登录太频繁，请{0}分钟后重新登录")
	public static final int DO_LOGIN_FREQUENTLY           = 1013;
	@ResultMessage("手机号不能重复")
	public static final int PHONE_EXIST                          = 1014;
	@ResultMessage("用户名称不能重复")
	public static final int USER_EXIT                          = 1015;

	@ResultMessage("当前用户不是系统管理员，不能创建商户")
	public static final int IS_NOT_SYS_ROLE                          = 1200;
	@ResultMessage("当前用户不是系统管理员，不能移动权限菜单")
	public static final int CAN_NOT_CHANGE_MENU                          = 1201;


	@ResultMessage("原密码不正确")
	public static final int OLD_PASSWORD_WRONG      = 1210;

	@ResultMessage("用户未登录")
	public static final int USER_NOT_LOGIN                      = 1301;

	/**----------------------------------------------权限管理*/
	@ResultMessage("父权限不存在")
	public static final int PARENT_PERMISSION_NOT_EXIST           = 10001;
	@ResultMessage("权限代码已经存在")
	public static final int PERMISSION_CODE_EXIST                        = 10002;
	@ResultMessage("权限类型不合法")
	public static final int PERMISSION_TYPE_WRONG                     = 10003;
	@ResultMessage("权限不存在")
	public static final int PERMISSION_NOT_EXIST                          = 10004;
	@ResultMessage("存在已经生效的子权限，请先禁用子权限")
	public static final int PERMISSION_DISABLE_CANT                    = 10005;
	@ResultMessage("父权限已经被禁用，请先启用父权限")
	public static final int PERMISSION_ENABLE_CANT                     = 10006;
	@ResultMessage("{0}为系统预置权限，不能禁用、修改")
	public static final int SYSTEM_PERMISSION_CANOT_CHANGE  = 10007;



	/**---------------------------邮件发送异常参数---------------------*/
	@ResultMessage("邮箱不存在")
	public static final int EMAIL_UNEXIST                    = 2000;

	@ResultMessage("邮箱验证码已过期")
	public static final int EMAIL_VERIFY_EXPIRED                  = 2001;

	@ResultMessage("邮箱验证码不匹配")
	public static final int EMAIL_VERIFY_ERROR                    = 2002;

	@ResultMessage("该手机号不存在")
	public static final int PHONE_NOT_EXIST                    = 2005;

	@ResultMessage("手机验证码已发送")
	public static final int PHONE_CODE_EXIST                    = 2006;

	@ResultMessage("手机验证码已过期")
	public static final int PHONE_CODE_EXPIRE                    = 2007;

	@ResultMessage("手机验证码不正确")
	public static final int PHONE_CODE_UNSPECIAL                    = 2008;


	@ResultMessage("通过密码找回类型不匹配")
	public static final int RESET_TYPE_UNEXIST                    = 2020;


	/**----------------------------------------------角色管理*/
	@ResultMessage("角色不存在")
	public static final int ROLE_NOT_EXIST                                      = 10100;
	@ResultMessage("角色代码已经存在")
	public static final int ROLE_CODE_EXIST                                    = 10101;
	@ResultMessage("{0}为系统预置角色，不能禁用、修改")
	public static final int SYSTEM_ROLE_CANOT_CHANGE              = 10102;

	static{
		try {
			Field[] fields = RestErrorCode.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				ResultMessage annotation = field.getAnnotation(ResultMessage.class);
				if(annotation==null) {
					continue;
				}
				int resultCode = field.getInt(null);
				if (codeMsgMappings.containsKey(resultCode)) {
					String text = "["+ RestErrorCode.class.getName()+"]错误码定义发生冲突，应用进程已经退出，请解决冲突并重启服务！";
					log.error(text);
				}
				String resultMsg = annotation.value();
				if (null != resultMsg && !"".equals(resultMsg.trim())) {
					codeMsgMappings.put(resultCode, resultMsg.trim());
				}
			}
		} catch (Exception e) {
			log.error("初始化错误码异常！", e);
		}
	}
	/**生成错误信息的字符串**/
	public static String renderMsg( int errorCode , Object...  args) {
		String rawErrorMsg = codeMsgMappings.get(errorCode);
		if(rawErrorMsg==null) {
			return "未知错误";
		}
		return MessageFormat.format(rawErrorMsg, args);
	}

}