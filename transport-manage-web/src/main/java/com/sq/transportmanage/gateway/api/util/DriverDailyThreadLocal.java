package com.sq.transportmanage.gateway.api.util;


import java.util.Set;

/**
 * @author (yangbo)
 * @Date: 2019/5/28 16:00
 * @Description:(线程共享资源)
 */
public class DriverDailyThreadLocal {

    private final static ThreadLocal<Set<Integer>> local = new ThreadLocal<>();

    /**
     * 数据源名称必须和local中的key保持一致
     *
     * @param integerSet
     */
    public static void putSetData(Set<Integer> integerSet) {
        local.set(integerSet);
    }

    public static Set<Integer> getSetData() {
        return local.get();
    }

    public static void cleanSetData() {
        local.remove();
    }
}
