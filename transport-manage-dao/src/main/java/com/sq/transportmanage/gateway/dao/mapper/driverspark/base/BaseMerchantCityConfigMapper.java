package com.sq.transportmanage.gateway.dao.mapper.driverspark.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseMerchantCityConfig;

public interface BaseMerchantCityConfigMapper {
    int insert(BaseMerchantCityConfig record);

    int insertSelective(BaseMerchantCityConfig record);

    BaseMerchantCityConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseMerchantCityConfig record);

    int updateByPrimaryKey(BaseMerchantCityConfig record);
}