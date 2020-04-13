package com.sq.transportmanage.gateway.api.auth;

import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.service.common.dto.PageDTO;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.*;
import com.sq.transportmanage.gateway.service.auth.UserManagementService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sq.transportmanage.gateway.service.common.enums.MenuEnum.*;


/**
 * @Author fanht
 * @Description
 * @Date 2020/2/26 下午5:55
 * @Version 1.0
 */
@RestController
public class UserManagementController {


	@Autowired
	private UserManagementService userManagementService;


	
	/**一、增加一个用户**/
	@RequestMapping("/addUser")
	@RequiresPermissions(value = { "ADD_USER" } )
	@RequestFunction(menu = USER_ADD)
	public AjaxResponse addUser(
			@Verify(param="account",rule="required|registerAccount(^[a-zA-Z0-9_\\-]{1,50}$)") String account,
			@Verify(param="userName",rule="required") String userName, 
			@Verify(param="phone",rule="required|mobile") String phone,
			@Verify(param = "email",rule = "required|email")String email) {
		SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
		if(loginUser == null){
			return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
		}
		CarAdmUser user  = new CarAdmUser();
		user.setAccount(account.trim());
		user.setUserName(userName.trim());
		user.setPhone(phone);
		user.setEmail(email);
		user.setCities( null );
		user.setSuppliers( null );
		user.setTeamId( null );
		user.setGroupIds(null);
		if(loginUser != null && loginUser.getMerchantId() > 0){
			user.setMerchantId(loginUser.getMerchantId());
		}
		// 暂时不用
		AjaxResponse ajaxResponse = userManagementService.addUser(user);


		return ajaxResponse;
	}
	
	/**二、禁用一个用户**/
	@RequestMapping("/disableUser")
	@RequiresPermissions(value = { "DISABLE_USER" } )
	@RequestFunction(menu = USER_DISABLE)
	@SysLogAnn(module="CarAdmUser",methods="enableUser",
			serviceClass="userManagementService",queryMethod="findByPrimaryKey",parameterType="Integer",parameterKey="userId",objClass=CarAdmUser.class ,extendParam="{status:100}")
	public AjaxResponse disableUser ( @Verify(param="userId",rule="required|min(1)") Integer userId ) {
		return userManagementService.disableUser(userId);
	}
	
	/**三、启用一个用户**/
	@RequestMapping("/enableUser")
	@RequiresPermissions(value = { "ENABLE_USER" } )
	@RequestFunction(menu = USER_ENABLE)
	@SysLogAnn(module="CarAdmUser",methods="enableUser",
			serviceClass="userManagementService",queryMethod="findByPrimaryKey",parameterType="Integer",parameterKey="userId",objClass=CarAdmUser.class ,extendParam="{status:200}")
	public AjaxResponse enableUser ( @Verify(param="userId",rule="required|min(1)") Integer userId ) {
		return userManagementService.enableUser(userId);
	}
	
	/**四、修改一个用户**/

	@RequestMapping("/changeUser")
	@RequiresPermissions(value = { "CHANGE_USER" } )
	@RequestFunction(menu = USER_UPDATE)
	public 	AjaxResponse changeUser(
			@Verify(param="userId",rule="min(1)") Integer userId,
			 String userName,
			@Verify(param="phone",rule="mobile") String phone,
			 @Verify(param = "email",rule = "required")String email,
			 String suppliers,
			 Integer dataLevel,
			 String cities,
			String teamIds,
			String groupIds
  		) {
		CarAdmUser newUser = new CarAdmUser();
		if(userId != null){
			newUser.setUserId(userId);
		}
		if(StringUtils.isNotEmpty(userName)){
			newUser.setUserName(userName);
		}
		if(StringUtils.isNotEmpty(phone)){
			newUser.setPhone(phone);
		}
		if(StringUtils.isNotEmpty(suppliers)){
			newUser.setSuppliers(suppliers);
		}
		if(dataLevel != null){
			newUser.setDataLevel(dataLevel);
		}
		if(StringUtils.isNotEmpty(email)){
			newUser.setEmail(email);
		}
		if(StringUtils.isNotEmpty(cities)){
			newUser.setCities(cities);
		}
		if(StringUtils.isNotEmpty(teamIds)){
			newUser.setTeamId(teamIds);
		}
		if(StringUtils.isNotEmpty(groupIds)){
			newUser.setGroupIds(groupIds);
		}
		return userManagementService.changeUser(newUser);
	}
	
	/**六、查询一个用户中的角色ID**/
	@RequestMapping("/getAllRoleIds")
	@RequiresPermissions(value = { "GET_ALL_ROLEIDS_OF_USER" } )
	@RequestFunction(menu = USER_ROLE_LIST)
	public AjaxResponse getAllRoleIds( @Verify(param="userId",rule="required|min(1)") Integer userId ){
		List<Integer> roleIds = userManagementService.getAllRoleIds(userId);
		return AjaxResponse.success( roleIds  );
	}
	
	/**七、保存一个用户中的角色ID**/
	@RequestMapping("/saveRoleIds")
	@RequiresPermissions(value = { "SAVE_ROLEIDS_OF_USER" } )
	@RequestFunction(menu = USER_ROLE_SAVE)
	public AjaxResponse saveRoleIds( @Verify(param="userId",rule="required|min(1)") Integer userId,  @Verify(param="roleIds",rule="RegExp(^([0-9]+,)*[0-9]+$)") String roleIds) {

		List<Integer> newroleIds = new ArrayList<Integer>();
		if(roleIds!=null) {
			String[]  ids = roleIds.split(",");
			if(ids.length>0) {
				for(String id : ids ) {
					if(StringUtils.isNotEmpty(id)) {
						try {
							newroleIds.add(Integer.valueOf(id));
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}
		AjaxResponse rep = userManagementService.saveRoleIds(userId, newroleIds);
		return rep;
	}
	
	/**八、查询用户列表**/
	@RequestMapping("/queryUserList")
	@RequiresPermissions(value = { "USER_MANAGEMENT" } )
	@RequestFunction(menu = USER_LIST)
	public AjaxResponse queryUserList( 
			@Verify(param="page",rule="required|min(1)") Integer page, 
			@Verify(param="pageSize",rule="required|min(10)") Integer pageSize,  
			String account , 
			String userName, 
			@Verify(param="phone",rule="mobile") String phone , 
			Integer status ,
			Integer roleId,
			String createStartTime,
			String createEndTime
			) {
		PageDTO pageDto = userManagementService.queryUserList(page, pageSize, roleId, account, userName, phone, status,createStartTime,createEndTime);
    	return AjaxResponse.success(pageDto);
	}

	/**九、重置用户密码**/
	@RequestMapping("/resetPassword")
	@RequiresPermissions(value = { "RESET_USER_PASSWORD" } )
	@RequestFunction(menu = USER_RESET_PASSWORD)
	public AjaxResponse resetPassword( @Verify(param="userId",rule="required|min(1)") Integer userId ) {
		return userManagementService.resetPassword(userId);
	}

	/**九、根据用户id查询用户**/
	@RequestMapping("/queryUserById")
	@RequestFunction(menu = USER_RESET_PASSWORD)
	public AjaxResponse queryUserById( @Verify(param="userId",rule="required|min(1)") Integer userId ) {
		CarAdmUser carAdmUser = userManagementService.findByPrimaryKey(userId);
		return AjaxResponse.success(carAdmUser);
	}


}