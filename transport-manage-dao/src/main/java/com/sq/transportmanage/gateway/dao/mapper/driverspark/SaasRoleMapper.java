package com.sq.transportmanage.gateway.dao.mapper.driverspark;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;

public interface SaasRoleMapper {
    int insert(SaasRole record);

    int insertSelective(SaasRole record);

    SaasRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(SaasRole record);

    int updateByPrimaryKey(SaasRole record);

    int deleteByPrimaryKey(Integer roleId);

}