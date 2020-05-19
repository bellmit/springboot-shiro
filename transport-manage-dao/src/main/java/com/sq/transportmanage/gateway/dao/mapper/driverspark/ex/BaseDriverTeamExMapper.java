package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseDriverTeam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDriverTeamExMapper {

    /**查询所有有效的车队**/
    List<BaseDriverTeam> queryServiceTeam(@Param("merchantId") Integer merchantId,
                                          @Param("supplierIds") List<Integer> supplierIds,
                                          @Param("teamIds") List<Integer> teamIds,
                                          @Param("type") Integer type);

    /**查询车队或者班组id集合**/
    List<Integer> queryIds(@Param("merchantId") Integer merchantId,
                                          @Param("supplierIds") List<Integer> supplierIds,
                                          @Param("cityIds") List<Integer> cityIds,
                                          @Param("teamIds") List<Integer> teamIds,
                                          @Param("type") Integer type);

    /**查询车队或者班组id集合**/
    List<BaseDriverTeam> queryForVos(@Param("merchantId") Integer merchantId,
                           @Param("supplierIds") List<Integer> supplierIds,
                           @Param("cityIds") List<Integer> cityIds,
                           @Param("teamIds") List<Integer> teamIds,
                           @Param("type") Integer type);

    List<BaseDriverTeam> queryTeamIdAndNames(@Param("merchantId")Integer merchantId,
                                             @Param("supplierId") Integer supplierId,
                                             @Param("cityId") Integer cityId,
                                             @Param("teamId") Integer teamId,
                                             @Param("teamIdList") List<Integer> teamIdList,
                                             @Param("groupList") List<Integer> groupList);

    /**查询车队级别的*/
    List<Integer> queryTeamsLevel(@Param("merchantId") Integer merchantId,
                                     @Param("supplierIds") List<Integer> supplierIds,
                                     @Param("cityIds") List<Integer> cityIds,
                                     @Param("teamIds") List<Integer> teamIds,
                                     @Param("type") Integer type);

    /**查询班组级别的*/
    List<Integer> queryGroupsLevel(@Param("merchantId") Integer merchantId,
                                  @Param("supplierIds") List<Integer> supplierIds,
                                  @Param("cityIds") List<Integer> cityIds,
                                   @Param("teamIds") List<Integer> teamIds,
                                   @Param("groupIds") List<Integer> groupIds,
                                   @Param("type") Integer type);
}