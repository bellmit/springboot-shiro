package com.sq.transportmanage.gateway.dao.mapper.mpdriver.ex;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferenceRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConferenceRoleExMapper {
    List< Integer> queryRoleIdsOfUser(@Param("userId") Integer userId);
    List<ConferenceRole> queryRoles(@Param("roleIds") List<Integer> roleIds, @Param("roleCode") String roleCode, @Param("roleName") String roleName, @Param("valid") Byte valid);
    /**根据用户ID，查询其拥有的所有有效的角色代码**/
    List<String> queryRoleCodesOfUser(@Param("userId") Integer userId);
}