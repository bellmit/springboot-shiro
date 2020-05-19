package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseMerchantCityConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author fanht
 */
public interface BaseMerchantCityConfigExMapper {

    /**
     * 查询详情
     * @param merchantId
     * @return
     */
    List<BaseMerchantCityConfig> queryServiceCity(@Param("merchantId") Integer merchantId);

    /**
     * 查询服务城市
     * @param merchantId
     * @return
     */
    List<Integer> queryServiceCityId(@Param("merchantId") Integer merchantId);

    /**
     * 查询名称和id
     * @param cityIdList
     * @param merchantId
     * @return
     */
    List<BaseMerchantCityConfig> queryServiceCityIdAndNames(@Param("cityIdList") List<Integer> cityIdList,
                                                            @Param("merchantId") Integer merchantId);
}