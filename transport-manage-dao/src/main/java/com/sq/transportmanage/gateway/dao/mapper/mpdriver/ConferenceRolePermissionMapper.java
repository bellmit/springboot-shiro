package com.sq.transportmanage.gateway.dao.mapper.mpdriver;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferenceRolePermission;

public interface ConferenceRolePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConferenceRolePermission record);

    int insertSelective(ConferenceRolePermission record);

    ConferenceRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConferenceRolePermission record);

    int updateByPrimaryKey(ConferenceRolePermission record);
}