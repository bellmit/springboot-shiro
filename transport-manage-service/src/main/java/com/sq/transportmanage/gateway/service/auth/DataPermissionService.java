package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.mapper.driverspark.base.BaseMerchantMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseMerchantCityConfigExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SupplierExtMapper;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.common.enums.DataLevelEnum;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description: 数据权限
 * @author: admin
 * @create: 2020-04-29 17:58
 **/
@Service
public class DataPermissionService {


    private static Logger logger = LoggerFactory.getLogger(DataPermissionService.class);

    @Autowired
    private BaseMerchantMapper baseMerchantMapper;

    @Autowired
    private SupplierExtMapper supplierExtMapper;

    @Autowired
    private BaseMerchantCityConfigExMapper baseMerchantCityConfigExMapper;

    @Autowired
    private BaseDriverTeamService baseDriverTeamService;


    public SSOLoginUser populateLoginUser(SSOLoginUser loginUser){
        /**运力商级别  则城市为运力商集合下所有  车队为运力商集合下所有  班组为运力商集合下所有**/
        if(DataLevelEnum.SUPPLIER_LEVEL.getCode().equals(loginUser.getLevel())){
            List<Integer> cityIds = baseMerchantCityConfigExMapper.queryServiceCityId(loginUser.getMerchantId());
            if(!CollectionUtils.isEmpty(cityIds)){
                loginUser.setCityIds(StringUtils.join(cityIds.toArray(), ","));
            }
            List<Integer> teamIds = baseDriverTeamService.queryServiceTeamIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null);
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }
            List<Integer> groupIds = baseDriverTeamService.queryServiceGreoupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null,null);
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }
            return loginUser;
        }
        /**城市级别  则车队为运力商城市集合下所有  班组为运力商城市集合下所有**/
        if(DataLevelEnum.CITY_LEVEL.getCode().equals(loginUser.getLevel()) && StringUtils.isEmpty(loginUser.getCityIds())){
            List<Integer> teamIds = baseDriverTeamService.queryServiceTeamIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds());
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }
            List<Integer> groupIds = baseDriverTeamService.queryServiceGreoupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds(),null);
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }
        }
        /**车队级别  则班组为车队集合下所有**/
        if(DataLevelEnum.TEAM_LEVEL.getCode().equals(loginUser.getLevel()) && StringUtils.isEmpty(loginUser.getTeamIds())){
            List<Integer> groupIds = baseDriverTeamService.queryServiceGreoupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds(),loginUser.getTeamIds());
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }
        }
        return loginUser;
    }
}