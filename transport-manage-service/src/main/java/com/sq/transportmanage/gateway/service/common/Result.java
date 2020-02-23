package com.sq.transportmanage.gateway.service.common;

import com.sq.transportmanage.gateway.service.common.constants.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口统一返回对象
 *
 * @author answer
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String msg = "操作成功";

    /**
     * 错误消息码
     */
    private String msgCode = "200";

    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result<T> error500(String message) {
        this.msg = message;
        this.code = Constants.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }

    public Result<T> success(String message) {
        this.msg = message;
        this.code = Constants.SUCCESS;
        this.success = true;
        return this;
    }

    public static Result<Object> ok() {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(Constants.SC_OK_200);
        r.setMsg("成功");
        return r;
    }

    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(Constants.SUCCESS);
        r.setMsg(msg);
        return r;
    }

    public static Result<Object> ok(Object data) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(Constants.SUCCESS);
        r.setResult(data);
        return r;
    }

    public static Result<Object> error(String msg) {
        return error(Constants.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static Result<Object> error(String msg, String msgCode) {
        Result<Object> r = new Result<>();
        r.setCode(Constants.ERROR);
        r.setMsgCode(msgCode);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }


    /**
     * 无权限访问返回结果
     */
    public static Result<Object> noauth(String msg) {
        return error(Constants.SC_JEECG_NO_AUTHZ, msg);
    }
}