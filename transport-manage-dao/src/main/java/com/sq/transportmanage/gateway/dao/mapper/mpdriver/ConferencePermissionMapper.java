package com.sq.transportmanage.gateway.dao.mapper.mpdriver;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferencePermission;

public interface ConferencePermissionMapper {
    int deleteByPrimaryKey(Integer permissionId);

    int insert(ConferencePermission record);

    int insertSelective(ConferencePermission record);

    ConferencePermission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(ConferencePermission record);

    int updateByPrimaryKey(ConferencePermission record);
}