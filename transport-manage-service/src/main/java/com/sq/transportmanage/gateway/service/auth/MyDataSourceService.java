package com.sq.transportmanage.gateway.service.auth;
import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasRolePermissionRalationExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SaasUserRoleRalationExMapper;
import com.sq.transportmanage.gateway.service.common.annotation.MyDataSource;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceType;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
        return carAdmUserExMapper.queryByAccount(account,null);
    }


    /**
     * 获取用户的身份认证信息
     *
     * @param loginName
     * @return
     */
    @MyDataSource(value = DataSourceType.DRIVERSPARK_MASTER)
    public SSOLoginUser getSSOLoginUser(String loginName,Integer merchantId) {
        logger.info("[WebSessionUtil获取用户的身份认证信息开始]loginName={},merchantId={}" + loginName,merchantId);
        try {
            CarAdmUser adMUser = carAdmUserExMapper.queryByAccount(loginName,merchantId);
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
            loginUser.setMerchantId(adMUser.getMerchantId()); //merchantId,用户区别每个商户
            //---------------------------------------------------------------------------------------------------------数据权限BEGIN
            //---------------------------------------------------------------------------------------------------------数据权限END
            logger.info("[WebSessionUtil获取用户的身份认证信息]=" + loginUser);
            return loginUser;
        } catch (Exception e) {
            logger.error("WebSessionUtil获取用户的身份认证信息异常", e);
            return null;
        }
    }


}
