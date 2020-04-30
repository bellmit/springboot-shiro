package com.sq.transportmanage.gateway.dao.mapper.driverspark.ex;

import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarAdmUserExMapper {
	/**根据账户查询**/
    CarAdmUser queryByAccount(@Param("account") String account,@Param("merchantId")Integer merchantId);
    /**查询用户列表**/
    List<CarAdmUser> queryUsers(@Param("merchantId")Integer merchantId,@Param("userIds") List<Integer> userIds, @Param("account") String account, @Param("userName") String userName,
                                @Param("phone") String phone, @Param("status") Integer status,
                                @Param("createStartTime") String createStartTime,@Param("createEndTime") String createEndTime);

    /**查询所有的登录账号名称**/
    List<String> queryAccountsOfUsers(@Param("userIds") List<Integer> userIds);


    List<CarAdmUser> selectUsersByIdList(List<Integer> ids);

    /**根据等级范围查询**/
    List<CarAdmUser> selectUsersByLevel(@Param("level") Integer level);

    CarAdmUser queryUserPermissionInfo(@Param("userId") Integer userId);

    List<Integer> queryIdListByName(@Param("userName") String createUser);

    String queryNameById(@Param("userId") Integer userId);

    int updateEmail(@Param("email") String email, @Param("userId") Integer userId);

    CarAdmUser verifyRepeat(@Param("account") String account,
                            @Param("email")String email,
                            @Param("phone")String phone
    );

    /**找回密码查询是否存在手机或者email**/
    CarAdmUser queryExist(@Param("email")String email,
                          @Param("phone")String phone);

    Integer queryMinUserId(@Param("merchantId")Integer merchantId);

    int updateMerchantId(@Param("merchantId") Integer merchantId, @Param("account") String account);

    CarAdmUser verifyRepeatWhenUpdate(@Param("phone")String phone,
                            @Param("email")String email,
                            @Param("userId")Integer userId
    );

}