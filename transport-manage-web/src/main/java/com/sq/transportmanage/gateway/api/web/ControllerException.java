package com.sq.transportmanage.gateway.api.web;

import com.alibaba.fastjson.JSON;
import com.sq.transportmanage.gateway.api.common.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 */
@ControllerAdvice
public class ControllerException {

    private static final Logger logger = LoggerFactory.getLogger(ControllerException.class);


    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Object bindEx(BindException e) {
        logger.warn(e.getObjectName() + "参数校验失败" + " :{}", ExceptionUtils.getMessage(e));
        return processBindException(e.getBindingResult(), false);
    }

    private Object processBindException(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        boolean first = true;
        StringBuilder sb = new StringBuilder("[");
        for (FieldError fieldError : errors) {
            if (first) {
                sb.append(fieldError.getField()).append("=").append(fieldError.getDefaultMessage().trim());
                first = false;
            } else {
                sb.append(",").append(fieldError.getField()).append("=").append(fieldError.getDefaultMessage().trim());
            }
        }
        sb.append("]");
        return ResponseResult.build(1, sb.toString(), Collections.EMPTY_MAP);
    }

    private Object processBindException(BindingResult bindingResult, boolean showFirst) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        if (!showFirst) {
            return processBindException(bindingResult);
        }
        return ResponseResult.build(1, errors.get(0).getDefaultMessage(), Collections.EMPTY_MAP);
    }

    /**
     * 请求方法错误
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Object methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.warn("请求方法错误:{}", ExceptionUtils.getMessage(e));
        return ResponseResult.build(1, e.getMessage(), Collections.EMPTY_MAP);
    }

    /**
     * 参数校验错误
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Object illegalArgument(IllegalArgumentException e) {
        logger.warn("参数校验失败:{}", ExceptionUtils.getMessage(e));
        return ResponseResult.build(1, e.getMessage(), Collections.EMPTY_MAP);
    }

    /**
     * 参数校验
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Object ServletRequestBinding(MissingServletRequestParameterException e) {
        logger.warn("缺少参数: {}", ExceptionUtils.getMessage(e));
        return ResponseResult.build(1, e.getMessage(), Collections.EMPTY_MAP);
    }

    /**
     * 参数类型错误
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public Object typeMismatchException(TypeMismatchException e) {
        logger.warn("参数类型错误: {}", ExceptionUtils.getMessage(e));
        return ResponseResult.build(1, e.getMessage(), Collections.EMPTY_MAP);
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object constraintViolationException(ConstraintViolationException e) {
        String s = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).findFirst().get();
        logger.warn("传参异常:{}", s);
        return ResponseResult.build(1, s, Collections.EMPTY_MAP);
    }

    /**
     * Exception 权限异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)
    public Object UnauthenticatedException(UnauthorizedException e) {

        logger.info("权限不足:{}", e.getMessage());

        return ResponseResult.build(99, "权限不足", Collections.EMPTY_MAP);
    }

    /**
     * Exception 默认异常返回
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object defaulted(Exception e) {

        logger.error("捕获全局异常:{}", ExceptionUtils.getStackTrace(e));

        return ResponseResult.build(1, e.getMessage(), Collections.EMPTY_MAP);
    }



}
