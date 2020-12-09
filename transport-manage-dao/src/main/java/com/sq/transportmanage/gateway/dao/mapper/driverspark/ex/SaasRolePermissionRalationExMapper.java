package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRolePermissionRalation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fanht
 */
public interface SaasRolePermissionRalationExMapper {
	/**
	 * 查询一个角色的所有权限ID
	 * @param roleId
	 * @return
	 */
	List<Integer> queryPermissionIdsOfRole(@Param("roleId") Integer roleId);

	/**
	 * 删除一个角色的所有权限ID
	 * @param roleId
	 * @return
	 */
	int deletePermissionIdsOfRole(@Param("roleId") Integer roleId);

	/**
	 * 删除一个权限的所有角色ID
	 * @param permissionId
	 * @return
	 */
	int deleteRoleIdsOfPermission(@Param("permissionId") Integer permissionId);

	/**
	 * 保存一个角色的所有权限ID(批量插入)
	 * @param records
	 * @return
	 */
	int insertBatch(@Param("records") List<SaasRolePermissionRalation> records);

	/**
	 * 查询一个权限的所有角色ID
	 * @param permissionId
	 * @return
	 */
	List<Integer> queryRoleIdsOfPermission(@Param("permissionId") Integer permissionId);

	/**
	 * 根据userId查询
	 * @param userId
	 * @return
	 */
    List<String> queryRoleNameList(@Param("userId") Integer userId);

	/**
	 * 查询某用户的所有角色ID
	 * @param userId
	 * @return
	 */
	List<String> queryRoleCodeList(@Param("userId") Integer userId);

	/**
	 * 根据商户查询商户最小的角色所有的权限id
	 * @param merchantId
	 * @return
	 */
	List<Integer> queryPermissionByMerchantId(@Param("merchantId")Integer merchantId);
}