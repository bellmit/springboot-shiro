package com.sq.transportmanage.gateway.api.auth;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.base.BaseMerchantCityConfigService;
import com.sq.transportmanage.gateway.service.base.BaseMerchantService;
import com.sq.transportmanage.gateway.service.base.BaseSupplierService;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.enums.DataLevelEnum;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.vo.BaseDriverGroupVo;
import com.sq.transportmanage.gateway.service.vo.BaseDriverTeamVo;
import com.sq.transportmanage.gateway.service.vo.BaseMerchantCityConfigVo;
import com.sq.transportmanage.gateway.service.vo.BaseSupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BaseSupplierService baseSupplierService;

    @Autowired
    private CarAdmUserMapper carAdmUserMapper;

    @Autowired
    private BaseMerchantCityConfigService configService;

    @Autowired
    private BaseDriverTeamService driverTeamService;

    @Autowired
    private BaseMerchantService baseMerchantService;


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
     * 查询当前用户所有的权限
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




    /**
     * 联动获取当前用户的运力商权限
     * @return
     */
    @RequestMapping("/querySuppliersPermission")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse querySuppliersPermission(){
        logger.info("=======联动获取当前用户的运力商权限start======");
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser == null){
            logger.info("用户未登录");
            return AjaxResponse.fail(RestErrorCode.USER_NOT_LOGIN);
        }

        List<BaseSupplierVo> supplierVoList = null;

        if(StringUtils.isEmpty(ssoLoginUser.getSupplierIds())){
            supplierVoList    = baseSupplierService.querySupplierNames(ssoLoginUser.getMerchantId(),ssoLoginUser.getSupplierIds());
        }else {
            supplierVoList = baseSupplierService.listAllBaseSupplier(ssoLoginUser.getMerchantId());
        }

        logger.info("=======联动获取当前用户的运力商权限end======" + JSONObject.toJSONString(supplierVoList));
        return AjaxResponse.success(supplierVoList);
    }


    /**
     * 联动获取当前用户的城市权限
     * @return
     */
    @RequestMapping("/queryCitiesPermission")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryCitiesPermission(){
        logger.info("=======联动获取当前用户的城市权限start======" );
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser == null){
            logger.info("用户未登录");
            return AjaxResponse.fail(RestErrorCode.USER_NOT_LOGIN);
        }

        List<BaseMerchantCityConfigVo> cityVoList = null;

        if(StringUtils.isNotEmpty(ssoLoginUser.getCityIds())){
            cityVoList    = configService.queryServiceCityIdAndNames(ssoLoginUser.getCityIds(),ssoLoginUser.getMerchantId());
        }else {
            cityVoList = configService.queryServiceCity(ssoLoginUser.getMerchantId());
        }

        logger.info("=======联动获取当前用户的城市权限end======" + JSONObject.toJSONString(cityVoList));
        return AjaxResponse.success(cityVoList);
    }



    /**
     * 联动获取当前用户的车队权限
     * @return
     */
    @RequestMapping("/queryTeamsPermission")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryTeamsPermission(@Verify(param = "supplierId",rule = "required")Integer supplierId,
                                             @Verify(param = "cityId",rule = "required")Integer cityId){
        logger.info("=======联动获取当前用户的车队权限start======supplierId:{},cityId:{}",supplierId,cityId);
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser == null){
            logger.info("用户未登录");
            return AjaxResponse.fail(RestErrorCode.USER_NOT_LOGIN);
        }

        List<BaseDriverTeamVo> teamVoList = null;

        if(StringUtils.isNotEmpty(ssoLoginUser.getTeamIds())){
            teamVoList = driverTeamService.queryTeamIdAndNames(ssoLoginUser.getMerchantId(),ssoLoginUser.getTeamIds(),supplierId,cityId);
        }else {
            teamVoList = driverTeamService.queryServiceTeamIdsForVo(ssoLoginUser.getMerchantId(),supplierId.toString(),cityId.toString());
        }
        logger.info("=======联动获取当前用户的车队权限end======" + JSONObject.toJSONString(teamVoList));
        return AjaxResponse.success(teamVoList);
    }


    /**
     * 联动获取当前用户的班组权限
     * @return
     */
    @RequestMapping("/queryGroupsPermission")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryGroupsPermission(@Verify(param = "supplierId",rule = "required")Integer supplierId,
                                              @Verify(param = "cityId",rule = "required")Integer cityId,
                                              @Verify(param = "teamId",rule = "required")Integer teamId){
        logger.info("=======联动获取当前用户的班组权限start======supplierId,{},cityId:{},teamId:{}",supplierId,cityId,teamId);
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser == null){
            logger.info("用户未登录");
            return AjaxResponse.fail(RestErrorCode.USER_NOT_LOGIN);
        }

        List<BaseDriverGroupVo> groupVoList = null;

        if(StringUtils.isNotEmpty(ssoLoginUser.getGroupIds())){
            groupVoList = driverTeamService.queryGroupIdAndNames(ssoLoginUser.getMerchantId(),supplierId,cityId,teamId,ssoLoginUser.getGroupIds());
        }else {
            List<BaseDriverTeamVo> teamVoList = driverTeamService.queryServiceGroupIdsForVo(ssoLoginUser.getMerchantId(),ssoLoginUser.getSupplierIds(),ssoLoginUser.getCityIds(),ssoLoginUser.getTeamIds());
            if(!CollectionUtils.isEmpty(teamVoList)){
                teamVoList.forEach(vo ->{
                    BaseDriverGroupVo  groupVo = new BaseDriverGroupVo();
                    groupVo.setId(vo.getId());
                    groupVo.setGroupName(vo.getTeamName());
                });
            }
        }
        logger.info("=======联动获取当前用户的班组权限end======" + JSONObject.toJSONString(groupVoList));
        return AjaxResponse.success(groupVoList);
    }

}
