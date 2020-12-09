package com.sq.transportmanage.gateway.service.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @Author fanht
 * @Description
 * @Date 2020/12/9 上午11:45
 * @Version 1.0
 */
public class Lazy<T> implements Supplier<T>{

    private Supplier<T> supplier;
    @Override
    public T get() {
        return null;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier){
        Objects.requireNonNull(supplier,"supplier is null");
        if(supplier instanceof Lazy){
            return (Lazy<T>) supplier;
        }else {
            return new Lazy<>(supplier);
        }
    }

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Lazy{" +
                "supplier=" + supplier +
                '}';
    }
}
