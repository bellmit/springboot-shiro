package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasUserRoleRalation;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasUserRoleRalationExMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/2/27 上午10:16
 * @Version 1.0
 */
@Service
public class SaasUserRoleRalationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaasUserRoleRalationExMapper saasUserRoleRalationExMapper;


    /**查询一个用户的所有角色ID**/
    public List<Integer> queryRoleIdsOfUser(Integer userId){
        return saasUserRoleRalationExMapper.queryRoleIdsOfUser(userId);
    }
    /**删除一个用户的所有角色ID**/
    public int deleteRoleIdsOfUser(Integer userId){
        return saasUserRoleRalationExMapper.deleteRoleIdsOfUser(userId);
    }
    /**保存一个用户的所有角色ID(批量插入)**/
    public int insertBatch(List<SaasUserRoleRalation> records){
        return  saasUserRoleRalationExMapper.insertBatch(records);
    }


    /**查询多个角色所对应的用户ID**/
    public List<Integer> queryUserIdsOfRole(List<Integer> roleIds){
        return saasUserRoleRalationExMapper.queryUserIdsOfRole(roleIds);
    }
}
