package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseMerchantCityConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMerchantCityConfigExMapper {

    List<BaseMerchantCityConfig> queryServiceCity(@Param("merchantId") Integer merchantId);

    List<Integer> queryServiceCityId(@Param("merchantId") Integer merchantId);
}