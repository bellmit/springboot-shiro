package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseSupplierExMapper {
    List<BaseSupplier> queryALlSupplerByMerchantId(Integer merchantId);

    List<BaseSupplier> querySupplierNames(@Param("supplierIds") String supplierIds);
}