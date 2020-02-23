package com.sq.transportmanage.gateway.dao.mapper.mpdriver.ex;

import com.sq.transportmanage.gateway.dao.entity.mdbcarmanage.SaasUserRoleRalation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConferenceUserRoleExMapper {
    List< Integer> queryRoleIdsOfUser(@Param("userId") Integer userId);

    int deleteRoleIdsOfUser(@Param("userId") Integer userId);

    int insertBatch(@Param("records") List<SaasUserRoleRalation> records);

    List<Integer> queryUserIdsOfRole(@Param("roleIds") List<Integer> roleIds);
}