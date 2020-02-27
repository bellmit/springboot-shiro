package com.sq.transportmanage.gateway.service.auth;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRolePermissionRalationExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasUserRoleRalationExMapper;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用于设置方法数据源
 **/
@Service
public class MyDataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(MyDataSourceService.class);

    @Autowired
    private SaasUserRoleRalationExMapper saasUserRoleRalationExMapper;

    @Autowired
    private CarAdmUserExMapper carAdmUserExMapper;

    @Autowired
    private SaasRolePermissionRalationExMapper saasRolePermissionRalationExMapper;

    @MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
    public List<Integer> queryRoleIdsOfPermission(Integer permissionId) {
        return saasRolePermissionRalationExMapper.queryRoleIdsOfPermission(permissionId);
    }

    @MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
    public List<Integer> queryUserIdsOfRole(List<Integer> roleIds) {
        return saasUserRoleRalationExMapper.queryUserIdsOfRole(roleIds);
    }

    @MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
    public List<String> queryAccountsOfUsers(List<Integer> userIds) {
        return carAdmUserExMapper.queryAccountsOfUsers(userIds);
    }


    @MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
    public CarAdmUser queryByAccount(String account) {
        //todo 此处uuid取值有问题，但是不知道怎么获取uuid
        return carAdmUserExMapper.queryByAccount(account,null);
    }


    /**
     * 获取用户的身份认证信息
     *
     * @param loginName
     * @return
     */
    @MyDataSource(value = DataSourceType.MDBCARMANAGER_MASTER)
    public SSOLoginUser getSSOLoginUser(String loginName,String uuid) {
        logger.info("[WebSessionUtil获取用户的身份认证信息开始]loginName={},uuid={}" + loginName,uuid);
        try {
            CarAdmUser adMUser = carAdmUserExMapper.queryByAccount(loginName,uuid);
            SSOLoginUser loginUser = new SSOLoginUser();  //当前登录的用户
            loginUser.setId(adMUser.getUserId());                //用户ID
            loginUser.setLoginName(adMUser.getAccount());//登录名
            loginUser.setMobile(adMUser.getPhone());         //手机号码
            loginUser.setName(adMUser.getUserName());    //真实姓名
            loginUser.setEmail(adMUser.getEmail()); //邮箱地址
            loginUser.setType(null);   //
            loginUser.setStatus(adMUser.getStatus());           //状态
            loginUser.setAccountType(adMUser.getAccountType());   //自有的帐号类型：[100 普通用户]、[900 管理员]
            loginUser.setLevel(adMUser.getLevel());
            loginUser.setUuid(adMUser.getUuid()); //uuid,用户区别每个商户
            //---------------------------------------------------------------------------------------------------------数据权限BEGIN
            /**此用户可以管理的城市ID**/
            if (StringUtils.isNotEmpty(adMUser.getCities())) {
                String[] idStrs = adMUser.getCities().trim().split(",");
                Set<Integer> ids = new HashSet<Integer>(idStrs.length * 2 + 2);
                for (String id : idStrs) {
                    if (StringUtils.isNotEmpty(id)) {
                        try {
                            ids.add(Integer.valueOf(id.trim()));
                        } catch (Exception e) {
                        }
                    }
                }
                loginUser.setCityIds(ids);
            }
            //---------------------------------------------------------------------------------------------------------数据权限END
            logger.info("[WebSessionUtil获取用户的身份认证信息]=" + loginUser);
            return loginUser;
        } catch (Exception e) {
            logger.error("WebSessionUtil获取用户的身份认证信息异常", e);
            return null;
        }
    }


}
