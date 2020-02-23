package com.sq.transportmanage.gateway.dao.mapper.mpdriver;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferenceRole;

public interface ConferenceRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(ConferenceRole record);

    int insertSelective(ConferenceRole record);

    ConferenceRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(ConferenceRole record);

    int updateByPrimaryKey(ConferenceRole record);
}