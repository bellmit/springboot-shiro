package com.sq.transportmanage.gateway.service.common.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * /***
 * 根据自己的相关业务,后续需要增加其它数据源的时候直接在此类中读取配置文件进行初始化
 * 所有的切换数据源放在Service层进行切换
 *
 * @author yangbo
 * @Date: 2019/1/4 11:46
 * @Description:(容器初始化数据源配置类)
 */
@Configuration
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private DBProperties dbProperties;

    /**
     * 优先使用，多数据源
     * @return DataSource
     */
    @Bean(name="dynamicDataSource")
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        DataSource driver_spark_master = dbProperties.getDriversparkmaster();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>driver_spark_master,pool-name:{}",dbProperties.getDriversparkmaster().getPoolName());

        DataSource driver_spark_slave = dbProperties.getDriversparkslave();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>driver_spark_slave,pool-name:{}",dbProperties.getDriversparkslave().getPoolName());



        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(driver_spark_master);
        //配置多数据源
        Map<Object,Object> map = new HashMap<>();
        //key需要跟ThreadLocal中的值对应
        //driverspark库
        map.put(DataSourceType.MPDRIVER_MASTER,driver_spark_master);
        map.put(DataSourceType.MPDRIVER_SLAVE,driver_spark_slave);

        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }
}
