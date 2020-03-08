package com.sq.transportmanage.gateway.service.common.annotation;


import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author (yangbo)
 * @Date: 2019/1/4 09:56
 * @Description:(自定义动态数据源注解,所有相关自定义注解可以现在找个annotation的包路劲下面)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyDataSource {

    DataSourceType value() default DataSourceType.DRIVERSPARK_MASTER;//默认主表
}
