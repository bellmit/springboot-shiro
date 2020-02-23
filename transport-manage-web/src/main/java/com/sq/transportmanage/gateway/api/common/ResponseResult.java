package com.sq.transportmanage.gateway.api.common;


import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import lombok.Data;

/**
 * 接口返回数据统一模板
 * 
 * @author wangxin5
 *
 * @param <T>
 */
@Data
public class ResponseResult<T> {

	/**
	 * 0表示正常
	 */
	public static final int SUCCESS_CODE = 0;

	public static final int ERROR_CODE = 1;

	public static final String SUCCESS_MSG = "SUCCESS";

	public static final String ERROR_MSG = "ERROR";

	public static final String SIGN_ERROR_MSG = "SIGN_ERROR";

	/**
	 * 成功
	 */
	public static final ResponseResult SUCCESS = new ResponseResult(SUCCESS_CODE, SUCCESS_MSG);
	/**
	 * 失败
	 */
	public static final ResponseResult ERROR = new ResponseResult(ERROR_CODE, ERROR_MSG);

	/**
	 * 返回状态码
	 */
	private int code;

	/**
	 * 提示信息
	 */
	private String msg;

	/**
	 * 数据类型
	 */
	private T data;

	public ResponseResult() {
		super();
	}

	public ResponseResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 构造方法
	 *
	 * @param code
	 * @param msg
	 * @param data
	 */
	public ResponseResult(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> ResponseResult build(int code, String msg, T data) {
		return new ResponseResult(code, msg, data);
	}

	public static ResponseResult build(int code, String msg) {
		return new ResponseResult(code, msg);
	}

	public static ResponseResult success(Object data) {
		ResponseResult resp = new ResponseResult();
		resp.setCode(RestErrorCode.SUCCESS);
		resp.setMsg("成功");
		resp.setData(data);
		return resp;
	}

	public static ResponseResult fail(int errorCode, Object... errArgs) {
		ResponseResult resp = new ResponseResult();
		int code = errorCode == RestErrorCode.SUCCESS ? RestErrorCode.UNKNOWN_ERROR : errorCode;
		resp.setCode(code);
		resp.setMsg(RestErrorCode.renderMsg(code, errArgs));
		return resp;
	}

	public static ResponseResult fail(int errorCode, String  msg) {
		ResponseResult resp = new ResponseResult();
		int code = errorCode == RestErrorCode.SUCCESS ? RestErrorCode.UNKNOWN_ERROR : errorCode;
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}
}