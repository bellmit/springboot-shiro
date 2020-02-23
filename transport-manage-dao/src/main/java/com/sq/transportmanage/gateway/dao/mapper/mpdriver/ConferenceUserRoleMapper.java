package com.sq.transportmanage.gateway.dao.mapper.mpdriver;

import com.sq.transportmanage.gateway.dao.entity.mpdriver.ConferenceUserRole;

public interface ConferenceUserRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConferenceUserRole record);

    int insertSelective(ConferenceUserRole record);

    ConferenceUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConferenceUserRole record);

    int updateByPrimaryKey(ConferenceUserRole record);
}