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
        //mp-driver库
        DataSource mp_driver_master = dbProperties.getMpdrivermaster();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>mp-driver主库,pool-name:{}",dbProperties.getMpdrivermaster().getPoolName());

        DataSource mp_driver_slave = dbProperties.getMpdriverslave();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>mp-driver从库,pool-name:{}",dbProperties.getMpdriverslave().getPoolName());
        //mdbcarmanage库
        DataSource mdbcarmanage_master = dbProperties.getMdbcarmanagemaster();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>mdbcarmanage主库,pool-name:{}",dbProperties.getMdbcarmanagemaster().getPoolName());

        DataSource mdbcarmanage_slave = dbProperties.getMdbcarmanageslave();
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>mdbcarmanage_master,pool-name:{}",dbProperties.getMdbcarmanageslave().getPoolName());

        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(mdbcarmanage_master);
        //配置多数据源
        Map<Object,Object> map = new HashMap<>();
        //key需要跟ThreadLocal中的值对应
        //mp_driver库
        map.put(DataSourceType.MPDRIVER_MASTER.getName(), mp_driver_master);
        map.put(DataSourceType.MPDRIVER_SLAVE.getName(), mp_driver_slave);
        //mdbcarmanage库
        map.put(DataSourceType.MDBCARMANAGER_MASTER.getName(), mdbcarmanage_master);
        map.put(DataSourceType.MDBCARMANAGER_SLAVE.getName(), mdbcarmanage_slave);

        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }
}
