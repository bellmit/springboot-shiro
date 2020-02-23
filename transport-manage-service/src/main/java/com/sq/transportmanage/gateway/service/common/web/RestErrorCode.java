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

	private static Map<Integer,String> codeMsgMappings  = new HashMap<Integer,String>();//错误码与错误文字的映射关系

	//-----------------------------------------------系统参数
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
				if (codeMsgMappings.containsKey(resultCode)) {//错误码定义发生冲突
					String text = "["+ RestErrorCode.class.getName()+"]错误码定义发生冲突，应用进程已经退出，请解决冲突并重启服务！";
					log.error(text);
					//System.exit(-1);
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