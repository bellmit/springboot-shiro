package com.sq.transportmanage.gateway.dao.mapper.mdbcarmanage;


import com.sq.transportmanage.gateway.dao.entity.mdbcarmanage.CarAdmUser;

public interface CarAdmUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(CarAdmUser record);

    int insertSelective(CarAdmUser record);

    CarAdmUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(CarAdmUser record);

    int updateByPrimaryKey(CarAdmUser record);
}