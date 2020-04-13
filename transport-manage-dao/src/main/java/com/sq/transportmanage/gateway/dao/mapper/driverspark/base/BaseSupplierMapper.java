package com.sq.transportmanage.gateway.dao.mapper.driverspark.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseSupplier;

public interface BaseSupplierMapper {
    int insert(BaseSupplier record);

    int insertSelective(BaseSupplier record);

    BaseSupplier selectByPrimaryKey(Integer supplierId);

    int updateByPrimaryKeySelective(BaseSupplier record);

    int updateByPrimaryKey(BaseSupplier record);
}