package com.sq.transportmanage.gateway.api.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.api.common.AuthEnum;
import com.sq.transportmanage.gateway.api.common.Constants;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasUserRoleRalation;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.SaasPermissionMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.service.auth.*;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author fanht
 * @Description 创建商户、移动菜单
 * @Date 2020/2/27 下午2:22
 * @Version 1.0
 */
@RestController
public class OperateManageController {

    private static final Logger logger = LoggerFactory.getLogger(OperateManageController.class);



    @Autowired
    private UserManagementService userManagementService;


    @Autowired
    private RoleManagementService roleManagementService;


    @Autowired
    private SaasRoleService saasRoleService;

    @Autowired
    private SaasRolePermissionRalationService saasRolePermissionRalationService;

    @Autowired
    private PermissionManagementService permissionManagementService;

    @Autowired
    private SaasUserRoleRalationService saasUserRoleRalationService;

    @Autowired
    private SaasPermissionMapper saasPermissionMapper;

    @Autowired
    private SaasPermissionExMapper saasPermissionExMapper;

    /**
     * 创建商户
     * @param account
     * @param userName
     * @param phone
     * @param email
     * @return
     */
    @RequestMapping("/addMerchant")
    @MyDataSource(value = DataSourceType.DRIVERSPARK_SLAVE)
    public AjaxResponse addMerchant(
            @Verify(param="account",rule="required|RegExp(^[a-zA-Z0-9_\\-]{3,50}$)") String account,
            @Verify(param="userName",rule="required") String userName,
            @Verify(param="phone",rule="required|mobile") String phone,
            @Verify(param = "email",rule = "required|email")String email) throws NoSuchAlgorithmException {
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser != null &&  AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            String md5=MD5Utils.getMD5DigestBase64(loginUser.getMerchantId().toString());
            if(!Constants.MANAGE_MD5.equals(md5)){
                logger.info("当前用户不是系统管理员，不能创建商户");
                return AjaxResponse.fail(RestErrorCode.IS_NOT_SYS_ROLE);
            }
        }
        try {
            CarAdmUser user  = new CarAdmUser();
            user.setAccount(account.trim());
            user.setUserName(userName.trim());
            user.setPhone(phone);
            user.setEmail(email);
            user.setRoleId(1);
            ////管理员
            user.setAccountType(AuthEnum.MANAGE.getAuthId());
            //String merchantId = System.currentTimeMillis()+ UUID.randomUUID().toString().replaceAll("-","");
            Random random = new Random();
            Integer merchantId = Integer.valueOf((int) (Math.random()*1000));
            user.setMerchantId(merchantId);
            //创建商户后获取uuid并创建商户的系统用户，赋予默认权限
            AjaxResponse ajaxResponse = userManagementService.addUser(user);
            if(ajaxResponse == null || ajaxResponse.getCode() != 0){
                return ajaxResponse;
            }
            logger.info("========创建商户成功=========" + ajaxResponse);
            if(ajaxResponse.getCode() == 0 && ajaxResponse.getData() != null ){
                CarAdmUser admUser = (CarAdmUser) ajaxResponse.getData();
                SaasRole role = new SaasRole();
                role.setRoleCode("manage_"+System.currentTimeMillis());
                role.setRoleName("系统管理员");
                role.setValid(true);
                role.setMerchantId(merchantId);
                role.setCreaterId(user.getUserId());


                List<SaasRole> roles = saasRoleService.queryRoles(loginUser.getMerchantId(),null, role.getRoleCode(), null, null);
                if(roles!=null && roles.size()>0) {
                    return AjaxResponse.fail(RestErrorCode.ROLE_CODE_EXIST );
                }
                //保存
                role.setValid(true);
                int code = saasRoleService.insert(role);
                if(code > 0){
                    logger.info(">============给用户赋权============start");
                    //默认为树形,获取的是父菜单 此处为全部 添加的是list类型
                    Integer roleId = saasRoleService.getRoleId(merchantId);
                    List<SaasPermissionDTO> allDtos = permissionManagementService.getAllPermissions(SaasConst.PermissionDataFormat.LIST);
                    //初始化时候不能拥有菜单权限
                    allDtos = this.removeMenuPermission(allDtos);
                    List<Integer> permissions = new ArrayList<>();
                    if(!CollectionUtils.isEmpty(allDtos)){
                        allDtos.forEach(all ->{
                            permissions.add(all.getPermissionId());
                        });
                    }
                    //角色和账号添加关系
                    List<SaasUserRoleRalation> records = new ArrayList<>();
                    SaasUserRoleRalation saasUserRoleRalation = new SaasUserRoleRalation();
                    saasUserRoleRalation.setRoleId(roleId);
                    saasUserRoleRalation.setUserId(admUser.getUserId());
                    records.add(saasUserRoleRalation);
                    saasUserRoleRalationService.insertBatch(records);
                    logger.info(">===================赋权限成功==============" + JSONObject.toJSONString(permissions));
                    return  roleManagementService.savePermissionIds(roleId, permissions);
                }

            }
            return ajaxResponse;
        } catch (Exception e) {
            logger.error("创建商户异常" + e);
            return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
        }
    }




    /**
     * 移动菜单权限
     * @param permissionId
     * @param newParentPermessionId
     * @return
     */
    @RequestMapping(value = "/changeMenu",method = RequestMethod.POST)
    public AjaxResponse changeMenu(
            @Verify(param="permissionId",rule="required") Integer permissionId,
            @Verify(param="newParentPermessionId",rule="required") Integer newParentPermessionId) {
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser != null &&  AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            String md5= null;
            try {
                md5 = MD5Utils.getMD5DigestBase64(loginUser.getMerchantId().toString());
            } catch (NoSuchAlgorithmException e) {
                logger.error("获取md5加密异常" + e);
            }
            if(!Constants.MANAGE_MD5.equals(md5)){
                logger.info("当前用户不是系统管理员，不能移动菜单权限");
                return AjaxResponse.fail(RestErrorCode.CAN_NOT_CHANGE_MENU);
            }
        }
        try {
            //查询有哪些角色有当前的菜单权限，之后找出哪些用户是这些角色。找出该菜单以及上级上菜，
            //删除关联关系，并在新的菜单上建立关联关系，让之前的受影响的角色和用户拥有该菜单权限
            List<Integer> roleIds = saasRolePermissionRalationService.queryRoleIdsOfPermission(permissionId);
            //修改父菜单为新的菜单
            SaasPermission permission = new SaasPermission();
            permission.setPermissionId(permissionId);
            permission.setParentPermissionId(newParentPermessionId);
            //给指定角色配置上菜单
            AjaxResponse ajaxResponse =permissionManagementService.changeSaasPermissionContainParent(permission);
            List<Integer> roleList = new ArrayList<>();
            roleList.add(permissionId);
            roleIds.forEach(roleId ->{
                roleManagementService.savePermissionIds(roleId,roleList);
            });
            return ajaxResponse;
        } catch (Exception e) {
            logger.error("修改菜单异常" + e);
            return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 去掉添加、修改、禁用菜单的权限功能
     * @param saasPermissionDTOList
     * @return
     */
    private List<SaasPermissionDTO> removeMenuPermission(List<SaasPermissionDTO> saasPermissionDTOList){
        Map<String,SaasPermissionDTO> map = Maps.newHashMap();
        saasPermissionDTOList.forEach(list ->{
            map.put(list.getPermissionCode(),list);
        });
        Map<String,SaasPermissionDTO> mapContain = Maps.newHashMap();
        for(String permissionCode : map.keySet()){
            if(!SaasConst.MENU_PERMISSION.contains(permissionCode)){
                mapContain.put(permissionCode,map.get(permissionCode));
            }
        }

        List<SaasPermissionDTO> list = new ArrayList<>();
        for(String permissionCode : mapContain.keySet()){
            list.add(mapContain.get(permissionCode));
        }
        return list;
    }



    @RequestMapping("/batchAddMenu")
    @ResponseBody
    public AjaxResponse batchAddMenu(@Verify(param = "jsonMenu",rule = "required") String jsonMenu){
        SSOLoginUser ssoLoginUser = WebSessionUtil.getCurrentLoginUser();

        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser != null &&  AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            String md5= null;
            try {
                md5 = MD5Utils.getMD5DigestBase64(loginUser.getMerchantId().toString());
            } catch (NoSuchAlgorithmException e) {
                logger.error("获取md5加密异常" + e);
            }
            if(!Constants.MANAGE_MD5.equals(md5)){
                logger.info("当前用户不是系统管理员，不能添加菜单");
                return AjaxResponse.fail(RestErrorCode.CAN_NOT_CHANGE_MENU);
            }
        }

        JSONArray jsonArray = JSONArray.parseArray(jsonMenu);
        if(jsonArray != null && jsonArray.size() > 0){
            jsonArray.forEach(jsonObject ->{
                JSONObject jsonData = (JSONObject) jsonObject;
                String permissionName = jsonData.get("name") == null ? null : jsonData.getString("name");
                String permissionCode = "PAR" + UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
                String url = jsonData.get("url") == null ? null: jsonData.getString("url");
                //添加模块菜单
                SaasPermission pemission =  new SaasPermission();
                pemission.setParentPermissionId(0);
                pemission.setPermissionName(permissionName);
                pemission.setPermissionCode(permissionCode.trim());
                pemission.setPermissionType(SaasConst.PermissionType.MENU);
                pemission.setMenuUrl(url);
                pemission.setMenuOpenMode(SaasConst.MenuOpenMode.CURRENT_WINDOW);
                pemission.setMerchantId(ssoLoginUser.getMerchantId());
                pemission.setCreateTime(new Date());
                pemission.setUpdateTime(new Date());
                pemission = this.addSaasPermission(pemission);
                if(pemission != null &&  pemission.getPermissionId() > 0){
                    JSONArray arraySun = jsonData.getJSONArray("children");
                    if(arraySun != null && arraySun.size() > 0){
                        SaasPermission finalPemission = pemission;
                        arraySun.forEach(sun ->{
                            JSONObject jsonSun = (JSONObject) sun;
                            String sunName = jsonSun.get("name") == null ? null : jsonSun.getString("name");
                            String sunUrl = jsonSun.get("url") == null ? null : jsonSun.getString("url");
                            String permissionSunCode = "SUN" + UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
                            SaasPermission sunPermission = new SaasPermission();
                            sunPermission.setParentPermissionId(finalPemission.getPermissionId());
                            sunPermission.setPermissionName(sunName);
                            sunPermission.setPermissionCode(permissionSunCode);
                            sunPermission.setPermissionType(SaasConst.PermissionType.MENU);
                            sunPermission.setMenuUrl(sunUrl);
                            sunPermission.setMenuOpenMode(SaasConst.MenuOpenMode.CURRENT_WINDOW);
                            sunPermission.setMerchantId(ssoLoginUser.getMerchantId());
                            sunPermission.setCreateTime(new Date());
                            sunPermission.setUpdateTime(new Date());
                            sunPermission = this.addSaasPermission(sunPermission);

                            if(sunPermission != null && sunPermission.getPermissionId() > 0){
                                JSONArray jsonThird = jsonSun.getJSONArray("children");
                                if(jsonThird != null && jsonThird.size() > 0){
                                    SaasPermission thirdSaas = sunPermission;
                                    jsonThird.forEach(third ->{
                                        JSONObject thirdJson = (JSONObject) third;
                                        String thirdName = thirdJson.get("name")==null ? null : thirdJson.getString("name");
                                        String thirdUrl = thirdJson.get("url")==null ? null : thirdJson.getString("url");
                                        String permissionGrandSun = "GRAND" + UUID.randomUUID().toString().replaceAll("-","").toUpperCase();

                                        SaasPermission grandSun = new SaasPermission();
                                        grandSun.setParentPermissionId(thirdSaas.getPermissionId());
                                        grandSun.setPermissionName(thirdName);
                                        grandSun.setPermissionCode(permissionGrandSun);
                                        grandSun.setPermissionType(SaasConst.PermissionType.MENU);
                                        grandSun.setMenuUrl(thirdUrl);
                                        grandSun.setMenuOpenMode(SaasConst.MenuOpenMode.CURRENT_WINDOW);
                                        grandSun.setMerchantId(ssoLoginUser.getMerchantId());
                                        grandSun.setCreateTime(new Date());
                                        grandSun.setUpdateTime(new Date());
                                         this.addSaasPermission(grandSun);
                                    });
                                }
                            }
                        });
                    }

                }
            });
            logger.info("===========初始化菜单数据成功==========");
            return AjaxResponse.success(null);
        }
        logger.info("========初始化数据为空=========");
        return AjaxResponse.success(null);
    }



    /**
     * 一、增加一个权限
     **/
    public SaasPermission addSaasPermission(SaasPermission pemission) {
        //父权限不存在
        if (pemission.getParentPermissionId() != null && pemission.getParentPermissionId().intValue() > 0) {
            SaasPermission parentPermission = saasPermissionMapper.selectByPrimaryKey(pemission.getParentPermissionId());
            if (parentPermission == null) {
                logger.info("===============父权限不存在=========");
                return null;
            }
        }
        //权限代码已经存在
        List<SaasPermission> pos = saasPermissionExMapper.queryPermissions(null, null, pemission.getPermissionCode(), null, null, null);
        if (pos != null && pos.size() > 0) {
            logger.info("=============权限代码已经存在=========================================================================");
            return null;
        }
        //权限类型不合法
        List<Byte> permTypes = Arrays.asList(new Byte[]{SaasConst.PermissionType.MENU, SaasConst.PermissionType.BUTTON, SaasConst.PermissionType.DATA_AREA});
        if (!permTypes.contains(pemission.getPermissionType())) {
            logger.info("========权限类型不合法===========");
            return null;
        }

        int sortSeq = saasPermissionExMapper.selectMaxSortSeq(pemission.getParentPermissionId()).intValue() + 1;//自动生成排序的序号
        pemission.setValid(true);
        pemission.setSortSeq(sortSeq);
        saasPermissionExMapper.insertSelective(pemission);
        return pemission;
    }
}
