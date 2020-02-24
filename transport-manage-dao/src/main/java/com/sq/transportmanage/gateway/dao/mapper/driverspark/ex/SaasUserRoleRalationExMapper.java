package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasUserRoleRalation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaasUserRoleRalationExMapper {
	/**查询一个用户的所有角色ID**/
	List<Integer> queryRoleIdsOfUser(@Param("userId") Integer userId);
	/**删除一个用户的所有角色ID**/
	int deleteRoleIdsOfUser(@Param("userId") Integer userId);
	/**保存一个用户的所有角色ID(批量插入)**/
	int insertBatch(@Param("records") List<SaasUserRoleRalation> records);

	/**查询多个角色所对应的用户ID**/
	List<Integer> queryUserIdsOfRole(@Param("roleIds") List<Integer> roleIds);
	
}