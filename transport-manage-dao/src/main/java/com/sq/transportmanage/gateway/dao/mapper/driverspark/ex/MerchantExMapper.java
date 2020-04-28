package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/27 下午4:43
 * @Version 1.0
 */
public interface MerchantExMapper {




    /**查询商户名称**/
    List<Merchant> queryMerchantNames(@Param("merchantIds") Set<Integer> merchantIds,
                                      @Param("merchantName") String merchantName,
                                      @Param("status") Integer status);
}
