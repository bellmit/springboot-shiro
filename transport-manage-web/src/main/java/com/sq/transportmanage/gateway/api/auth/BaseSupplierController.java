package com.sq.transportmanage.gateway.api.auth;

import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.base.BaseMerchantCityConfigService;
import com.sq.transportmanage.gateway.service.base.BaseSupplierService;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.enums.TeamType;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.vo.BaseDriverTeamVo;
import com.sq.transportmanage.gateway.service.vo.BaseMerchantCityConfigVo;
import com.sq.transportmanage.gateway.service.vo.BaseSupplierVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * 查询当前商户下的所有的城市
     * @return
     */
    @RequestMapping("/queryAllTeams")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllTeams(String supplierIds,
                                      String teamIds){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){

            List<BaseDriverTeamVo> voList = driverTeamService.queryServiceTeam(Integer.valueOf(ssoLoginUser.getMerchantId()),
                    supplierIds,teamIds, TeamType.TEAM.getCode());
            return AjaxResponse.success(voList);
        }else {
            return AjaxResponse.success(null);
        }

    }


    /**
     * 查询当前商户下的所有的城市
     * @return
     */
    @RequestMapping("/queryAllGroups")
    @ResponseBody
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse queryAllGroups(String supplierIds,
                                      String teamIds){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();
        if(ssoLoginUser != null){
            List<BaseDriverTeamVo> voList = driverTeamService.queryServiceTeam(Integer.valueOf(ssoLoginUser.getMerchantId()),
                    supplierIds,teamIds, TeamType.GROUP.getCode());
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
            map.put("level",carAdmUser.getLevel());
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
}
