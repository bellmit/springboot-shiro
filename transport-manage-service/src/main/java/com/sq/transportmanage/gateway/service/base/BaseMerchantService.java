package com.sq.transportmanage.gateway.service.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.Merchant;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.MerchantExMapper;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/27 下午4:47
 * @Version 1.0
 */
@Service
public class BaseMerchantService {

    @Autowired
    private MerchantExMapper exMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Merchant> queryMerchantNames(String merchantIds,
                                             String merchantName,
                                             Integer status){

        if(StringUtils.isEmpty(merchantIds)){
            return null;
        }



        String[] str = merchantIds.split(Constants.SPLIT);

        Set<Integer> merchantIdSet = new HashSet<>();
        for(String merId : str){
            try {
                merchantIdSet.add(Integer.valueOf(merId));
            } catch (NumberFormatException e) {
                logger.error("抓换异常" + e);
            }
        }
        return  exMapper.queryMerchantNames(merchantIdSet,merchantName,status);
    }
}
