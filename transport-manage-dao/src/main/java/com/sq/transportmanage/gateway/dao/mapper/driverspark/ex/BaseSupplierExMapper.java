package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author  fanht
 */
public interface BaseSupplierExMapper {
    /**
     * 根据商户获取商户下的运力商
     * @param merchantId
     * @return
     */
    List<BaseSupplier> queryAllSupplerByMerchantId(Integer merchantId);

    /**
     * 查询运力商名称
     * @param merchantId
     * @param supplierIdList
     * @return
     */
    List<BaseSupplier> querySupplierNames(@Param("merchantId")Integer merchantId,
                                          @Param("supplierIdList") List<Integer> supplierIdList);
}