package com.sq.transportmanage.gateway.service.common.datasource;

/**
 * @author (yangbo)
 * @Date: 2019/1/4 09:59
 * @Description:(Mysql数据源配置枚举,相关枚举类都可以放在这个包下面进行维护)
 */
public enum DataSourceType {

    // driver_spark主库
    DRIVERSPARK_MASTER("driver_spark_master"),
    // driver_spark从库
    DRIVERSPARK_SLAVE("driver_spark_slave");

    private String name;

    private DataSourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
