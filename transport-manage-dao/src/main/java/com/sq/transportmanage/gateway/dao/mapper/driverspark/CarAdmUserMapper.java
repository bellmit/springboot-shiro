package com.sq.transportmanage.gateway.dao.mapper.driverspark;

import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;

public interface CarAdmUserMapper {
    int insert(CarAdmUser record);

    int insertSelective(CarAdmUser record);

    CarAdmUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(CarAdmUser record);

    int updateByPrimaryKey(CarAdmUser record);
}