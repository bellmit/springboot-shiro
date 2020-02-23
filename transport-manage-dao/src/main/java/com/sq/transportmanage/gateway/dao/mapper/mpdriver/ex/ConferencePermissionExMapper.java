package com.sq.transportmanage.gateway.dao.mapper.mpdriver.ex;


import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferencePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConferencePermissionExMapper {
    List<ConferencePermission> queryPermissions(@Param("permissionIds") List<Integer> permissionIds, @Param("parentId") Integer parentId, @Param("permissionCode") String permissionCode, @Param("permissionTypes") List<Byte> permissionTypes, @Param("permissionName") String permissionName, @Param("valid") Byte valid);
    Number selectMaxSortSeq(@Param("parentId") Integer parentId);
    /**根据用户ID，查询其拥有的所有有效的权限ID**/
    List<Integer> queryPermissionIdsOfUser(@Param("userId") Integer userId);
    /**根据用户ID，查询其拥有的所有有效的权限代码**/
    List<String> queryPermissionCodesOfUser(@Param("userId") Integer userId);
}