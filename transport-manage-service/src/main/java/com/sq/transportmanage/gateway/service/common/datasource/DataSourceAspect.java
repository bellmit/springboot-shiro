package com.sq.transportmanage.gateway.service.common.datasource;

import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yangbo
 * @Date: 2019/1/4 11:32
 * @Description:(动态切换数据使用切面方式切换)
 */
@Aspect
@Order(1)	//设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中--前提是支持事物过程)
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    /**
     * 凡是使用annotation中的MyDataSource注解进行切点
     */
    @Pointcut("@annotation(com.sq.transportmanage.gateway.service.common.annotation.MyDataSource)")
    public void aspect(){}

    /**
     * 进入方法前进行切面动态切换数据源
     * @param point
     */
    @Before("aspect()")
    private void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();
        Class<?> classz = target.getClass();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = classz.getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(MyDataSource.class)) {
                MyDataSource data = m.getAnnotation(MyDataSource.class);
                JdbcContextHolder.putDataSource(data.value().getName());
            }
        } catch (Exception e) {
            logger.error("~~动态切换数据源异常DataSourceAspect.before:{}",e);
        }
    }

    /**
     * 每个服务使用完数据源之后进行清除操作
     */
    @After("aspect()")
    private void after(){
        JdbcContextHolder.cleanDataSource();
    }
}
