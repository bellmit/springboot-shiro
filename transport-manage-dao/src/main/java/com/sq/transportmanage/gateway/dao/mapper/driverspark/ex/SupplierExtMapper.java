package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierExtMapper {

    List<Integer> selectListByMerchantId(@Param("merchantId") Integer merchantId);
}