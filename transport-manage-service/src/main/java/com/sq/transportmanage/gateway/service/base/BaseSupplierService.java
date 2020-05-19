package com.sq.transportmanage.gateway.service.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseSupplier;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseSupplierExMapper;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import com.sq.transportmanage.gateway.service.vo.BaseSupplierVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/3/7 上午11:50
 * @Version 1.0
 */
@Slf4j
@Service
public class BaseSupplierService {

    @Autowired
    private BaseSupplierExMapper baseSupplierExMapper;

    /**
     * 查询商户下所有的运力商
     * @param merchantId
     * @return
     */
    public List<BaseSupplierVo> listAllBaseSupplier(Integer merchantId){

        List<BaseSupplier> baseSupplierList  = baseSupplierExMapper.queryAllSupplerByMerchantId(merchantId);
        if(CollectionUtils.isEmpty(baseSupplierList)){
            return null;
        }
        List<BaseSupplierVo> voList = BeanUtil.copyList(baseSupplierList,BaseSupplierVo.class);
        return voList;
    }

    public List<BaseSupplierVo> querySupplierNames(Integer merchantId,String supplierIds){
        List<Integer> supplierIdList = new ArrayList<>();

        if(StringUtils.isNotEmpty(supplierIds)){
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(int i = 0;i<supplierArr.length;i++){
                supplierIdList.add(Integer.valueOf(supplierArr[i].trim()));
            }
        }

        List<BaseSupplier> baseSupplierList  = baseSupplierExMapper.querySupplierNames(merchantId,supplierIdList);
        if(CollectionUtils.isEmpty(baseSupplierList)){
            return null;
        }

        List<BaseSupplierVo> voList = BeanUtil.copyList(baseSupplierList,BaseSupplierVo.class);
        return voList;
    }
}
