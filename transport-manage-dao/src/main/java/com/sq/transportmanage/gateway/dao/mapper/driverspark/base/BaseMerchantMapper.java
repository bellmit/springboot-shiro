package com.sq.transportmanage.gateway.dao.mapper.driverspark.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseMerchant;

public interface BaseMerchantMapper {
    int insert(BaseMerchant record);

    int insertSelective(BaseMerchant record);

    BaseMerchant selectByPrimaryKey(Integer merchantId);

    int updateByPrimaryKeySelective(BaseMerchant record);

    int updateByPrimaryKey(BaseMerchant record);
}