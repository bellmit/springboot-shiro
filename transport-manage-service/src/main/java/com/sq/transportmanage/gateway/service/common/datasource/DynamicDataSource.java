package com.sq.transportmanage.gateway.service.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author (yangbo)
 * @Date: 2019/1/4 11:45
 * @Description:(动态获取数据源)
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return JdbcContextHolder.getDataSource();
    }
}
