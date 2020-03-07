package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseSupplier;

import java.util.List;

public interface BaseSupplierExMapper {
    List<BaseSupplier> queryALlSupplerByMerchantId(Integer merchantId);
}