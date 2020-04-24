package com.sq.transportmanage.gateway.service.base;

import com.sq.transportmanage.gateway.dao.entity.driverspark.base.BaseDriverTeam;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseDriverTeamExMapper;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.util.BeanUtil;
import com.sq.transportmanage.gateway.service.vo.BaseDriverTeamVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            String supplierArr[] = supplierIds.split(Constants.SPLIT);
            for(String id : supplierArr){
                supplierIdList.add(Integer.valueOf(id));
            }
        }

        List<Integer> teamIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(teamIds)){
            teamIdList = Arrays.asList();
            String teamIdsArr[] = teamIds.split(Constants.SPLIT);
            for(String teamId : teamIdsArr){
                teamIdList.add(Integer.valueOf(teamId));
            }
        }

        List<BaseDriverTeam> baseDriverTeamList = exMapper.queryServiceTeam(merchantId,supplierIdList,teamIdList,
                type);

        List<BaseDriverTeamVo> voList = BeanUtil.copyList(baseDriverTeamList,BaseDriverTeamVo.class);

        return voList;
    }




}
