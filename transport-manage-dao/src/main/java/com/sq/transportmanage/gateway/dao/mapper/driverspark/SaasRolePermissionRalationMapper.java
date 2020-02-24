package com.sq.transportmanage.gateway.dao.mapper.driverspark;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRolePermissionRalation;

public interface SaasRolePermissionRalationMapper {
    int insert(SaasRolePermissionRalation record);

    int insertSelective(SaasRolePermissionRalation record);

    SaasRolePermissionRalation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SaasRolePermissionRalation record);

    int updateByPrimaryKey(SaasRolePermissionRalation record);
}