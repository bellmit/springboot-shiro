package com.sq.transportmanage.gateway.service.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseMerchantCityConfig;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseMerchantCityConfigExMapper;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import com.sq.transportmanage.gateway.service.vo.BaseMerchantCityConfigVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/13 下午5:34
 * @Version 1.0
 */
@Service
public class BaseMerchantCityConfigService {

    @Autowired
    private BaseMerchantCityConfigExMapper exMapper;


    public List<BaseMerchantCityConfigVo> queryServiceCity(Integer merchantId){
        List<BaseMerchantCityConfig> configList = exMapper.queryServiceCity(merchantId);

        if(CollectionUtils.isEmpty(configList)){
            return null;
        }

        List<BaseMerchantCityConfigVo> voList = new ArrayList<>();
        configList.forEach(list ->{
            BaseMerchantCityConfigVo vo = new BaseMerchantCityConfigVo();
            vo.setId(list.getId());
            vo.setCityName(list.getCityName());
            vo.setCityId(list.getCityId());
            voList.add(vo);
        });
        return  voList;

    }

    public List<BaseMerchantCityConfigVo> queryServiceCityIdAndNames(String cityIds,Integer merchantId){

        List<Integer> cityIdList = new ArrayList<>();

        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityArr = cityIds.split(Constants.SPLIT);
            for(int i = 0;i<cityArr.length;i++){
                cityIdList.add(Integer.valueOf(cityArr[i].trim()));
            }
        }

        List<BaseMerchantCityConfig> configList = exMapper.queryServiceCityIdAndNames(cityIdList,merchantId);

        if(CollectionUtils.isEmpty(configList)){
            return null;
        }

        List<BaseMerchantCityConfigVo> voList = new ArrayList<>();
        configList.forEach(list ->{
            BaseMerchantCityConfigVo vo = new BaseMerchantCityConfigVo();
            vo.setId(list.getId());
            vo.setCityName(list.getCityName());
            vo.setCityId(list.getCityId());
            voList.add(vo);
        });
        return  voList;

    }
}
