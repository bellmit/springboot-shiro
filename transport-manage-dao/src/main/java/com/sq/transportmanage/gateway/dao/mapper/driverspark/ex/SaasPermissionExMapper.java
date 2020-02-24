package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaasPermissionExMapper{
	/**查询父权限下所有子权限的最大排序序号**/
	Integer selectMaxSortSeq(@Param("parentId") Integer parentId);
	/**根据权限ID、父权限ID、权限代码、权限类型、权限名称、状态进行查询 **/
	List<SaasPermission> queryPermissions(@Param("permissionIds") List<Integer> permissionIds, @Param("parentId") Integer parentId, @Param("permissionCode") String permissionCode, @Param("permissionTypes") List<Byte> permissionTypes, @Param("permissionName") String permissionName, @Param("valid") Byte valid);

	/**根据用户ID，查询其拥有的所有有效的权限ID**/
	List<Integer> queryPermissionIdsOfUser(@Param("userId") Integer userId);
	/**根据用户ID，查询其拥有的所有有效的权限代码**/
	List<String> queryPermissionCodesOfUser(@Param("userId") Integer userId);
	
}