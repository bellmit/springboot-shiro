/*
package com.sq.transportmanage.gateway.api.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Random;

*/
/**
 * 统一异常处理
 * @author zhaoyali
 *//*

public  class CustomExceptionResolver implements HandlerExceptionResolver {
	private static final Logger   log        = LoggerFactory.getLogger(CustomExceptionResolver.class);
	private static final Random random = new SecureRandom();

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception ex){
		int ExceptionId = random.nextInt(2100000000);
		String exceptionMessage = ex.getMessage() + " (ExceptionId: "+ExceptionId+")";
		log.info(exceptionMessage, ex );
		
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		String exceptionDetail = sw.toString();
		
		//1.判断是否为AJAX请求
		Boolean isAjax = (Boolean) request.getAttribute("X_IS_AJAX");
		//2.根据不同的请求类型，分别返回不同的结果
		if(isAjax){
			AjaxResponse ajaxResponse = AjaxResponse.fail(RestErrorCode.HTTP_SYSTEM_ERROR);
			ajaxResponse.setMsg("系统发生异常，异常ID："+ExceptionId+"，请联系管理员。");
			this.outJson(response, ajaxResponse);
			return null;
		}else{
			String envName = request.getServletContext().getInitParameter("env.name");
			request.setAttribute("envName", envName );
			
			request.setAttribute("exceptionMessage", exceptionMessage);
			request.setAttribute("exceptionDetail",  exceptionDetail);
			return new ModelAndView("exception" );
		}
	}

	@SuppressWarnings("deprecation")
	private void outJson(HttpServletResponse response, AjaxResponse ajaxResponse){
		PrintWriter out = null;
		try{
			response.setStatus(HttpStatus.SC_OK, ajaxResponse.getMsg() );
			out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			response.setHeader("pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			out.write( JSON.toJSONString(ajaxResponse, true) );
			return;
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}*/