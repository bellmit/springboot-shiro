package com.sq.transportmanage.gateway.api.auth;

import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.base.BaseMerchantCityConfigService;
import com.sq.transportmanage.gateway.service.base.BaseSupplierService;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.enums.DataLevelEnum;
import com.sq.transportmanage.gateway.service.common.enums.TeamType;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.vo.BaseDriverTeamVo;
import com.sq.transportmanage.gateway.service.vo.BaseMerchantCityConfigVo;
import com.sq.transportmanage.gateway.service.vo.BaseSupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/13 下午6:27
 * @Version 1.0
 */
@Controller
public class BaseSupplierController {


    @Autowired
    private BaseSupplierService baseSupplierService;

    @Autowired
    private CarAdmUserMapper carAdmUserMapper;

    @Autowired
    private BaseMerchantCityConfigService configService;

    @Autowired
    private BaseDriverTeamService driverTeamService;

    /**
     * 查询当前商户所拥有的数据权限-运力商
     * @return
     */
    @RequestMapping("/queryAllSuppliers")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllSuppliers(){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){
            List<BaseSupplierVo> voList = baseSupplierService.listAllBaseSupplier(Integer.valueOf(ssoLoginUser.getMerchantId()));
            return AjaxResponse.success(voList);
        }else {
            return AjaxResponse.success(null);
        }

    }


    /**
     * 查询当前商户下的所有的城市
     * @return
     */
    @RequestMapping("/queryAllCities")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllCities(){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){
            List<BaseMerchantCityConfigVo> voList = configService.queryServiceCity(Integer.valueOf(ssoLoginUser.getMerchantId()));
            return AjaxResponse.success(voList);
        }else {
            return AjaxResponse.success(null);
        }

    }


    /**
     * 查询当前商户下的所有的车队
     * @return
     */
    @RequestMapping("/queryAllTeams")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllTeams(@Verify(param = "supplierIds",rule = "required")String supplierIds,
                                      @Verify(param = "cityIds",rule = "required")String cityIds){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){

            List<BaseDriverTeamVo> voList = driverTeamService.queryServiceTeamIdsForVo(Integer.valueOf(ssoLoginUser.getMerchantId()),
                    supplierIds,cityIds);
            return AjaxResponse.success(voList);
        }else {
            return AjaxResponse.success(null);
        }

    }


    /**
     * 查询当前商户下的所有的班组
     * @return
     */
    @RequestMapping("/queryAllGroups")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllGroups(@Verify(param = "supplierIds",rule = "required")String supplierIds,
                                       @Verify(param = "cityIds",rule = "required")String cityIds,
                                       @Verify(param = "teamIds",rule = "required")String teamIds){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){
            List<BaseDriverTeamVo> voList = driverTeamService.queryServiceGroupIdsForVo(Integer.valueOf(ssoLoginUser.getMerchantId()),
                    supplierIds,cityIds,teamIds);
            return AjaxResponse.success(voList);
        }else {
            return AjaxResponse.success(null);
        }

    }



    /**
     * 查询当前用户所有的运力商
     * @return
     */
    @RequestMapping("/queryDataPermissions")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryDataPermissions(Integer userId){

        CarAdmUser carAdmUser = carAdmUserMapper.selectByPrimaryKey(userId);
        if(carAdmUser != null){
            Map<String,Object> map = Maps.newHashMap();
            map.put("suppliers",carAdmUser.getSuppliers());
            map.put("dataLevel",carAdmUser.getDataLevel());
            map.put("cities",carAdmUser.getCities());
            map.put("teamIds",carAdmUser.getTeamId());
            map.put("groupIds",carAdmUser.getGroupIds());
            return AjaxResponse.success(map);
        }else {
            return AjaxResponse.success(null);
        }

    }


    /**
     * 查询当前商户下的所有的城市
     * @return
     */
    @RequestMapping("/queryCurrentUserCities")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryCurrentUserCities(){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){
            List<BaseMerchantCityConfigVo> voList = configService.queryServiceCity(Integer.valueOf(ssoLoginUser.getMerchantId()));

            if(DataLevelEnum.SUPPLIER_LEVEL.getCode().equals(ssoLoginUser.getDataLevel())){
                return AjaxResponse.success(voList);
            }else if(DataLevelEnum.CITY_LEVEL.getCode().equals(ssoLoginUser.getDataLevel())
                    || DataLevelEnum.TEAM_LEVEL.getCode().equals(ssoLoginUser.getDataLevel())
                ||DataLevelEnum.GROUP_LEVEL.getCode().equals(ssoLoginUser.getDataLevel())){
              String citys =  ssoLoginUser.getCityIds();
              if(StringUtils.isNotEmpty(citys)){
                  String[] cityStr = citys.split(",");
                  List<Integer> cityList = new ArrayList<>();
                  for(int k = 0;k<cityStr.length;k++){
                      cityList.add(Integer.valueOf(cityStr[k].trim()));
                  }

                  List<BaseMerchantCityConfigVo> newVoList =  new ArrayList<>();
                  voList.forEach(vo ->{
                              if(cityList.contains(vo.getCityId())){
                                  newVoList.add(vo);
                              }
                          }
                  );
                  return AjaxResponse.success(newVoList);
              }
            }else {
                //如果是0  判断是否是管理员
                if(Constants.SUPER_MANAGE.equals(ssoLoginUser.getAccountType())
                        || Constants.MANAGE.equals(ssoLoginUser.getAccountType())){
                    return AjaxResponse.success(voList);
                }
            }
        }
        return AjaxResponse.success(null);
    }

}
