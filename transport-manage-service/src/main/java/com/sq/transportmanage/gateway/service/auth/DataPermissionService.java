package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.mapper.driverspark.base.BaseMerchantMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseMerchantCityConfigExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SupplierExtMapper;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.common.enums.AccountTypeEnum;
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
        /**商户管理员或者超级管理员  则运力商为商户下所有运力商   城市为运力商集合下所有  车队为运力商集合下所有  班组为运力商集合下所有**/
        if(AccountTypeEnum.MERCHANT_ADM.getCode().equals(loginUser.getAccountType()) || AccountTypeEnum.SUPER_ADM.getCode().equals(loginUser.getAccountType())){
            List<Integer> supplierIds = supplierExtMapper.selectListByMerchantId(loginUser.getMerchantId());
            if(!CollectionUtils.isEmpty(supplierIds)){
                loginUser.setSupplierIds(StringUtils.join(supplierIds.toArray(), ","));
            }else{
                loginUser.setSupplierIds("");
            }
            List<Integer> cityIds = baseMerchantCityConfigExMapper.queryServiceCityId(loginUser.getMerchantId());
            if(!CollectionUtils.isEmpty(cityIds)){
                loginUser.setCityIds(StringUtils.join(cityIds.toArray(), ","));
            }else{
                loginUser.setCityIds("");
            }
            List<Integer> teamIds = baseDriverTeamService.queryServiceTeamIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null);
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }else {
                loginUser.setTeamIds("");
            }
            List<Integer> groupIds = baseDriverTeamService.queryServiceGroupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null,null);
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }else{
                loginUser.setGroupIds("");
            }
            return loginUser;
        }
        /**运力商级别  则城市为运力商集合下所有  车队为运力商集合下所有  班组为运力商集合下所有**/
        if(DataLevelEnum.SUPPLIER_LEVEL.getCode().equals(loginUser.getDataLevel())){
            List<Integer> cityIds = baseMerchantCityConfigExMapper.queryServiceCityId(loginUser.getMerchantId());
            if(!CollectionUtils.isEmpty(cityIds)){
                loginUser.setCityIds(StringUtils.join(cityIds.toArray(), ","));
            }else {
                loginUser.setCityIds("");
            }
            List<Integer> teamIds = baseDriverTeamService.queryServiceTeamIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null);
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }else{
                loginUser.setTeamIds("");
            }
            List<Integer> groupIds = baseDriverTeamService.queryServiceGroupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),null,null);
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }else{
                loginUser.setGroupIds("");
            }
            return loginUser;
        }
        /**城市级别  则车队为运力商城市集合下所有  班组为运力商城市集合下所有**/
        if(DataLevelEnum.CITY_LEVEL.getCode().equals(loginUser.getDataLevel())){
            List<Integer> teamIds = baseDriverTeamService.queryServiceTeamIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds());
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }else{
                loginUser.setTeamIds("");
            }
            List<Integer> groupIds = baseDriverTeamService.queryServiceGroupIds(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds(),null);
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }else{
                loginUser.setGroupIds("");
            }
            return loginUser;
        }

        /**车队级别  则车队为车队集合下所有**/
        if(DataLevelEnum.GROUP_LEVEL.getCode().equals(loginUser.getDataLevel())){
            List<Integer> teamIds = baseDriverTeamService.queryTeamsLevel(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds(),loginUser.getTeamIds());
            if(!CollectionUtils.isEmpty(teamIds)){
                loginUser.setTeamIds(StringUtils.join(teamIds.toArray(), ","));
            }else{
                loginUser.setTeamIds("");
            }
            return loginUser;
        }

        /**班组级别  则班组为车队下所有**/
        if(DataLevelEnum.GROUP_LEVEL.getCode().equals(loginUser.getDataLevel())){
            List<Integer> groupIds = baseDriverTeamService.queryGroupLevel(loginUser.getMerchantId(),loginUser.getSupplierIds(),loginUser.getCityIds(),loginUser.getTeamIds(),
                    loginUser.getGroupIds());
            if(!CollectionUtils.isEmpty(groupIds)){
                loginUser.setGroupIds(StringUtils.join(groupIds.toArray(), ","));
            }else{
                loginUser.setGroupIds("");
            }
            return loginUser;
        }
        return loginUser;
    }




}