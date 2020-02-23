package com.sq.transportmanage.gateway.service.common.datasource;

/**
 * @author (yangbo)
 * @Date: 2019/1/4 11:42
 * @Description:(动态数据源的上下文 threadlocal)
 */
public class JdbcContextHolder {

    private final static ThreadLocal<String> local = new ThreadLocal<>();

    /**
     * 数据源名称必须和local中的key保持一致
     * @param name
     */
    public static void putDataSource(String name) {
        local.set(name);
    }

    public static String getDataSource() {
        return local.get();
    }

    public static void cleanDataSource(){
        local.remove();
    }
}
