package com.sq.transportmanage.gateway.api.controller.auth;

import com.alibaba.fastjson.JSONObject;
import com.sq.transportmanage.gateway.api.common.AuthEnum;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.SaasRoleMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasPermissionExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRoleExMapper;
import com.sq.transportmanage.gateway.service.auth.*;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.shiro.realm.UsernamePasswordRealm;
import com.sq.transportmanage.gateway.service.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.shiro.session.WebSessionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private CarAdmUserMapper carAdmUserMapper;
    @Autowired
    private CarAdmUserExMapper carAdmUserExMapper;
    @Autowired
    private SaasPermissionExMapper saasPermissionExMapper;
    @Autowired
    private UsernamePasswordRealm usernamePasswordRealm;
    @Autowired
    private RedisSessionDAO redisSessionDAO;
    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    @Autowired
    private RoleManagementService roleManagementService;

    @Autowired
    private SaasRoleExMapper saasRoleExMapper;

    @Autowired
    private SaasRoleMapper saasRoleMapper;

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
            @Verify(param = "email",rule = "required|email")String email) {
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser == null ||  !AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())|| StringUtils.isNotEmpty(loginUser.getUuid())){
            logger.info("当前用户不是系统管理员，不能创建商户");
            return AjaxResponse.fail(RestErrorCode.IS_NOT_SYS_ROLE);
        }
        try {
            CarAdmUser user  = new CarAdmUser();
            user.setAccount(account.trim());
            user.setUserName(userName.trim());
            user.setPhone(phone);
            user.setEmail(email);
            ////管理员
            user.setAccountType(AuthEnum.MANAGE.getAuthId());
            String uuid = System.currentTimeMillis()+ UUID.randomUUID().toString().replaceAll("-","");
            user.setUuid(uuid);
            //创建商户后获取uuid并创建商户的系统用户，赋予默认权限
            AjaxResponse ajaxResponse = userManagementService.addUser(user);
            if(ajaxResponse == null || ajaxResponse.getCode() != 0){
                return ajaxResponse;
            }
            logger.info("========创建商户成功=========" + ajaxResponse);
            if(ajaxResponse.getCode() == 0 && ajaxResponse.getData() != null ){
                SaasRole role = new SaasRole();
                role.setRoleCode("manage_"+System.currentTimeMillis());
                role.setRoleName("系统管理员");
                role.setValid(true);
                role.setUuid(uuid);

                List<SaasRole> roles = saasRoleExMapper.queryRoles(loginUser.getUuid(),null, role.getRoleCode(), null, null);
                if(roles!=null && roles.size()>0) {
                    return AjaxResponse.fail(RestErrorCode.ROLE_CODE_EXIST );
                }
                //保存
                role.setValid(true);
                int roleId = saasRoleMapper.insertSelective(role);
                if(roleId > 0){
                    //默认为树形
                    List<SaasPermissionDTO> allDtos = permissionManagementService.getAllPermissions(SaasConst.PermissionDataFormat.TREE);
                    List<Integer> permissions = new ArrayList<>();
                    if(!CollectionUtils.isEmpty(allDtos)){
                        allDtos.forEach(all ->{
                            permissions.add(all.getPermissionId());
                        });
                    }
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
            @Verify(param="permessionId",rule="required") Integer permissionId,
            @Verify(param="newParentPermessionId",rule="required") Integer newParentPermessionId) {
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        if(loginUser == null ||  !AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())|| StringUtils.isNotEmpty(loginUser.getUuid())){
            logger.info("当前用户不是系统管理员，不能移动菜单权限");
            return AjaxResponse.fail(RestErrorCode.CAN_NOT_CHANGE_MENU);
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
}