package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaasRoleExMapper{
	/**查询角色列表**/
	List<SaasRole> queryRoles(@Param("merchantIds")String merchantIds,@Param("roleIds") List<Integer> roleIds, @Param("roleCode") String roleCode, @Param("roleName") String roleName, @Param("valid") Byte valid);

	/**根据用户ID，查询其拥有的所有有效的角色ID**/
	List<Integer> queryRoleIdsOfUser(@Param("userId") Integer userId);
	/**根据用户ID，查询其拥有的所有有效的角色代码**/
	List<String> queryRoleCodesOfUser(@Param("userId") Integer userId);
    /**插入并获取主键id**/
	int insert(SaasRole record);
	/**根据merchantIds获取角色id**/
	Integer getRoleId(@Param("merchantIds") String merchantIds);
}