package com.sq.transportmanage.gateway.service.common.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author (yangbo)
 * @Date: 2019/5/27 21:51
 * @Description:(hikari多数据源配置)
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DBProperties {
    /***
     * mp_driver_master 主库
     */
    private HikariDataSource mpdrivermaster;
    /***
     * mp_driver_slave 从库
     */
    private HikariDataSource mpdriverslave;
    /***
     * mdbcarmanage_master 主库
     */
    private HikariDataSource mdbcarmanagemaster;
    /***
     * mdbcarmanage_slave 从库
     */
    private HikariDataSource mdbcarmanageslave;

    /**
     * driversparkmaster 主库
     */
    private HikariDataSource driversparkmaster;

    /**
     * driversparkslave从库
     */
    private HikariDataSource driversparkslave;

    public HikariDataSource getMpdrivermaster() {
        return mpdrivermaster;
    }

    public void setMpdrivermaster(HikariDataSource mpdrivermaster) {
        this.mpdrivermaster = mpdrivermaster;
    }

    public HikariDataSource getMpdriverslave() {
        return mpdriverslave;
    }

    public void setMpdriverslave(HikariDataSource mpdriverslave) {
        this.mpdriverslave = mpdriverslave;
    }

    public HikariDataSource getMdbcarmanagemaster() {
        return mdbcarmanagemaster;
    }

    public void setMdbcarmanagemaster(HikariDataSource mdbcarmanagemaster) {
        this.mdbcarmanagemaster = mdbcarmanagemaster;
    }

    public HikariDataSource getMdbcarmanageslave() {
        return mdbcarmanageslave;
    }

    public void setMdbcarmanageslave(HikariDataSource mdbcarmanageslave) {
        this.mdbcarmanageslave = mdbcarmanageslave;
    }

    public HikariDataSource getDriversparkmaster() {
        return driversparkmaster;
    }

    public void setDriversparkmaster(HikariDataSource driversparkmaster) {
        this.driversparkmaster = driversparkmaster;
    }

    public HikariDataSource getDriversparkslave() {
        return driversparkslave;
    }

    public void setDriversparkslave(HikariDataSource driversparkslave) {
        this.driversparkslave = driversparkslave;
    }
}
