package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRoleExMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/2/27 下午4:37
 * @Version 1.0
 */
@Service
public class SaasRoleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaasRoleExMapper saasRoleExMapper;

    /**查询角色列表**/
    public List<SaasRole> queryRoles(String uuid, List<Integer> roleIds,String roleCode, String roleName, Byte valid){
        return saasRoleExMapper.queryRoles(uuid,roleIds,roleCode,roleName,valid);
    }

    /**根据用户ID，查询其拥有的所有有效的角色ID**/
    public List<Integer> queryRoleIdsOfUser(Integer userId){
        return saasRoleExMapper.queryRoleIdsOfUser(userId);
    }
    /**根据用户ID，查询其拥有的所有有效的角色代码**/
    public List<String> queryRoleCodesOfUser(@Param("userId") Integer userId){
        return saasRoleExMapper.queryRoleCodesOfUser(userId);
    }

    public int insert(SaasRole record){
        return saasRoleExMapper.insert(record);
    }


    public int getRoleId(String uuid){
        return saasRoleExMapper.getRoleId(uuid);
    }
}
