package com.sq.transportmanage.gateway.dao.mapper.mpdriver.ex;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferenceRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConferenceRolePermissionExMapper {
    List<Integer> queryRoleIdsOfPermission(@Param("permissionId") Integer permissionId);
    List< Integer> queryPermissionIdsOfRole(@Param("roleId") Integer roleId);
    int deletePermissionIdsOfRole(@Param("roleId") Integer roleId);
    int insertBatch(@Param("records") List<ConferenceRolePermission> records);
    List<String> queryRoleNameList(@Param("userId") Integer userId);
    int deleteRoleIdsOfPermission(@Param("permissionId") Integer permissionId);
}