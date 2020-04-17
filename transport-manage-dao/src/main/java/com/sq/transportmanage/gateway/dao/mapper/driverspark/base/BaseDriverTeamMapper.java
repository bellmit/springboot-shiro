package com.sq.transportmanage.gateway.dao.mapper.driverspark.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseDriverTeam;

public interface BaseDriverTeamMapper {
    int insert(BaseDriverTeam record);

    int insertSelective(BaseDriverTeam record);

    BaseDriverTeam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseDriverTeam record);

    int updateByPrimaryKey(BaseDriverTeam record);
}