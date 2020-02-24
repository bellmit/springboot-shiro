package com.sq.transportmanage.gateway.dao.mapper.driverspark;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasPermission;

public interface SaasPermissionMapper {
    int insert(SaasPermission record);

    int insertSelective(SaasPermission record);

    SaasPermission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(SaasPermission record);

    int updateByPrimaryKey(SaasPermission record);

    int deleteByPrimaryKey(Integer permissionId);

}