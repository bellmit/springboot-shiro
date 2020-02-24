package com.sq.transportmanage.gateway.service.common.datasource;

/**
 * @author (yangbo)
 * @Date: 2019/1/4 09:59
 * @Description:(Mysql数据源配置枚举,相关枚举类都可以放在这个包下面进行维护)
 */
public enum DataSourceType {

    //mp_driver主库
    MPDRIVER_MASTER("mpdriver_master"),

    //mp_driver从库
    MPDRIVER_SLAVE("mpdriver_slave"),

    // mdbcarmanage主库
    MDBCARMANAGER_MASTER("mdbcarmanage_master"),
    // mdbcarmanage从库
    MDBCARMANAGER_SLAVE("mdbcarmanage_slave"),


    // driver_spark主库
    DRIVERSPARK_MASTER("mdbcarmanage_master"),
    // driver_spark从库
    DRIVERSPARK_SLAVE("mdbcarmanage_slave");

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
