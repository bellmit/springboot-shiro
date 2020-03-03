package com.sq.transportmanage.gateway.api.controller.auth;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.api.common.AuthEnum;
import com.sq.transportmanage.gateway.api.common.Constants;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasUserRoleRalation;
import com.sq.transportmanage.gateway.service.auth.*;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author fanht
 * @Description
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

    /**
     * 创建商户
     * @param account
     * @param userName
     * @param phone
     * @param email
     * @return
     */
    @RequestMapping("/addMerchant")
    public AjaxResponse addMerchant(
            @Verify(param="account",rule="required|RegExp(^[a-zA-Z0-9_\\-]{3,30}$)") String account,
            @Verify(param="userName",rule="required") String userName,
            @Verify(param="phone",rule="required|mobile") String phone,
            @Verify(param = "email",rule = "required|email")String email) throws NoSuchAlgorithmException {
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser != null &&  AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            String md5=MD5Utils.getMD5DigestBase64(loginUser.getMerchantIds());
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
            String merchantIds = System.currentTimeMillis()+ UUID.randomUUID().toString().replaceAll("-","");
            user.setMerchantIds(merchantIds);
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
                role.setMerchantIds(merchantIds);


                List<SaasRole> roles = saasRoleService.queryRoles(loginUser.getMerchantIds(),null, role.getRoleCode(), null, null);
                if(roles!=null && roles.size()>0) {
                    return AjaxResponse.fail(RestErrorCode.ROLE_CODE_EXIST );
                }
                //保存
                role.setValid(true);
                int code = saasRoleService.insert(role);
                if(code > 0){
                    logger.info(">============给用户赋权============start");
                    //默认为树形,获取的是父菜单 此处为全部 添加的是list类型
                    Integer roleId = saasRoleService.getRoleId(merchantIds);
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
                md5 = MD5Utils.getMD5DigestBase64(loginUser.getMerchantIds());
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
}
