package com.sq.transportmanage.gateway.dao.mapper.driverspark;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasUserRoleRalation;

public interface SaasUserRoleRalationMapper {
    int insert(SaasUserRoleRalation record);

    int insertSelective(SaasUserRoleRalation record);

    SaasUserRoleRalation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SaasUserRoleRalation record);

    int updateByPrimaryKey(SaasUserRoleRalation record);
}