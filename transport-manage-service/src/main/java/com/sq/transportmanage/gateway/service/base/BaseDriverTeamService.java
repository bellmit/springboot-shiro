package com.sq.transportmanage.gateway.service.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseDriverTeam;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseDriverTeamExMapper;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.enums.TeamType;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import com.sq.transportmanage.gateway.service.vo.BaseDriverGroupVo;
import com.sq.transportmanage.gateway.service.vo.BaseDriverTeamVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/13 下午5:33
 * @Version 1.0
 */
@Service
public class BaseDriverTeamService {

    @Autowired
    private BaseDriverTeamExMapper exMapper;



    public List<BaseDriverTeamVo>  queryServiceTeam(Integer merchantId,
                                        String  supplierIds,
                                        String teamIds,
                                        Integer type){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIds)){
            String[] teamIdsArr = teamIds.split(Constants.SPLIT);
            for(String teamId : teamIdsArr){
                teamIdList.add(Integer.valueOf(teamId));
            }
        }

        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryServiceTeam(merchantId,supplierIdList,teamIdList,
                type);

        List<BaseDriverTeamVo> voList = BeanUtil.copyList(baseDriverTeamList,BaseDriverTeamVo.class);

        return voList;
    }


    public List<Integer>  queryServiceTeamIds(Integer merchantId, String  supplierIds, String cityIds){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }

        List<Integer> teamIds = exMapper.queryIds(merchantId,supplierIdList,cityIdList, null, TeamType.TEAM.getCode());

        return teamIds;
    }

    public List<BaseDriverTeamVo>  queryServiceTeamIdsForVo(Integer merchantId, String  supplierIds, String cityIds){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }

        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryForVos(merchantId,supplierIdList,cityIdList, null,TeamType.TEAM.getCode());
        List<BaseDriverTeamVo> voList = BeanUtil.copyList(baseDriverTeamList,BaseDriverTeamVo.class);
        return voList;
    }


    public List<Integer>  queryServiceGroupIds(Integer merchantId, String  supplierIds, String cityIds, String teamIds){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }

        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIds)){
            String[] teamIdsArr = teamIds.split(Constants.SPLIT);
            for(String teamId : teamIdsArr){
                teamIdList.add(Integer.valueOf(teamId));
            }
        }

        List<Integer> groupIds = exMapper.queryIds(merchantId,supplierIdList,cityIdList,teamIdList,TeamType.GROUP.getCode());
        return groupIds;
    }


    public List<BaseDriverTeamVo>  queryServiceGroupIdsForVo(Integer merchantId, String  supplierIds, String cityIds, String teamIds){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[]  supplierArr= supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }

        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIds)){
            String[] teamIdsArr = teamIds.split(Constants.SPLIT);
            for(String teamId : teamIdsArr){
                teamIdList.add(Integer.valueOf(teamId));
            }
        }

        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryForVos(merchantId,supplierIdList,cityIdList, teamIdList,TeamType.GROUP.getCode());
        List<BaseDriverTeamVo> voList = BeanUtil.copyList(baseDriverTeamList,BaseDriverTeamVo.class);
        return voList;
    }



    /**根据车队id获取车队名称*/
    public List<BaseDriverTeamVo>  queryTeamIdAndNames(Integer merchantId,String teamIds,Integer supplierId,Integer cityId){
        List<Integer> teamIdList = new ArrayList<>();

        if(StringUtils.isNotEmpty(teamIds)){
            String[] teamIdsArr = teamIds.split(Constants.SPLIT);
            for(String teamId : teamIdsArr){
                teamIdList.add(Integer.valueOf(teamId));
            }
        }

        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryTeamIdAndNames(merchantId,supplierId,cityId,null,teamIdList,null);
        if(CollectionUtils.isEmpty(baseDriverTeamList)){
            return null;
        }
        List<BaseDriverTeamVo> voList = BeanUtil.copyList(baseDriverTeamList,BaseDriverTeamVo.class);
        return voList;
    }



    /**根据班组id获取班组名称*/
    public List<BaseDriverGroupVo>  queryGroupIdAndNames(Integer merchantId,
                                                         Integer supplierId,
                                                         Integer cityId,
                                                         Integer teamId,
                                                         String groupIds){
        List<Integer> groupIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(groupIds)){
            String[] groupIdArr = groupIds.split(Constants.SPLIT);
            for(String id : groupIdArr){
                groupIdList.add(Integer.valueOf(id));
            }
        }


        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryTeamIdAndNames(merchantId,supplierId,cityId,teamId,null,groupIdList);
        if(CollectionUtils.isEmpty(baseDriverTeamList)){
            return null;
        }
        List<BaseDriverGroupVo> voList = new ArrayList<>();
        baseDriverTeamList.forEach(team ->{
            BaseDriverGroupVo vo = new BaseDriverGroupVo();
            vo.setId(team.getId());
            vo.setGroupName(team.getTeamName());
            voList.add(vo);
        });
        return voList;
    }




    /**查询车队级别的*/
    public List<Integer>  queryTeamsLevel(Integer merchantId, String  supplierIds, String cityIds,String teamIdsStr){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }


        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIdsStr)){
            String[] teamArr = teamIdsStr.split(Constants.SPLIT);
            for(String team : teamArr){
                teamIdList.add(Integer.valueOf(team));
            }
        }
        List<Integer> teamIds = exMapper.queryTeamsLevel(merchantId,supplierIdList,cityIdList, teamIdList, TeamType.TEAM.getCode());

        return teamIds;
    }


    /**查询车队级别的*/
    public List<Integer>  queryGroupLevel(Integer merchantId, String  supplierIds, String cityIds,String teamIdsStr,
                                          String groupIdsStr){

        List<Integer> supplierIdList = null;
        if(StringUtils.isNotEmpty(supplierIds)){
            supplierIdList = new ArrayList<>();
            String[] supplierArr = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> cityIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(cityIds)){
            String[] cityIdsArr = cityIds.split(Constants.SPLIT);
            for(String cityId : cityIdsArr){
                cityIdList.add(Integer.valueOf(cityId));
            }
        }


        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIdsStr)){
            String[] teamArr = teamIdsStr.split(Constants.SPLIT);
            for(String team : teamArr){
                teamIdList.add(Integer.valueOf(team));
            }
        }


        List<Integer> groupList = new ArrayList<>();
        if(StringUtils.isNotEmpty(groupIdsStr)){
            String[] groupArr = groupIdsStr.split(Constants.SPLIT);
            for(String group : groupArr){
                groupList.add(Integer.valueOf(group));
            }
        }
        List<Integer> groupIds = exMapper.queryGroupsLevel(merchantId,supplierIdList,cityIdList, teamIdList,groupList, TeamType.GROUP.getCode());

        return groupIds;
    }


}
