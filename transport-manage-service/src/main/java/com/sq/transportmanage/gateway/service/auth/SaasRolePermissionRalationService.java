package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRolePermissionRalation;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRolePermissionRalationExMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/2/27 上午10:15
 * @Version 1.0
 */
@Service
public class SaasRolePermissionRalationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaasRolePermissionRalationExMapper saasRolePermissionRalationExMapper;

    /**查询一个角色的所有权限ID**/
    public List<Integer> queryPermissionIdsOfRole(Integer roleId){
        return saasRolePermissionRalationExMapper.queryPermissionIdsOfRole(roleId);
    }
    /**删除一个角色的所有权限ID**/
    public int deletePermissionIdsOfRole(Integer roleId){
        return saasRolePermissionRalationExMapper.deletePermissionIdsOfRole(roleId);
    }
    /**删除一个权限的所有角色ID**/
    public int deleteRoleIdsOfPermission(Integer permissionId){
        return saasRolePermissionRalationExMapper.deleteRoleIdsOfPermission(permissionId);
    }
    /**保存一个角色的所有权限ID(批量插入)**/
    public int insertBatch(List<SaasRolePermissionRalation> records){
        return saasRolePermissionRalationExMapper.insertBatch(records);
    }

    /**查询一个权限的所有角色ID**/
    public List<Integer> queryRoleIdsOfPermission(Integer permissionId){
        return saasRolePermissionRalationExMapper.queryRoleIdsOfPermission(permissionId);
    }

    public List<String> queryRoleNameList(Integer userId){
        return saasRolePermissionRalationExMapper.queryRoleNameList(userId);
    }
    /**查询某用户的所有角色ID */
    public List<String> queryRoleCodeList(Integer userId){
        return saasRolePermissionRalationExMapper.queryRoleCodeList(userId);
    }


}
