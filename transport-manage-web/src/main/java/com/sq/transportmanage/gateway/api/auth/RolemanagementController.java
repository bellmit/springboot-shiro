package com.sq.transportmanage.gateway.api.auth;


import com.sq.transportmanage.gateway.dao.entity.driverspark.SaasRole;
import com.sq.transportmanage.gateway.service.auth.PermissionManagementService;
import com.sq.transportmanage.gateway.service.common.constants.SaasConst;
import com.sq.transportmanage.gateway.service.common.dto.PageDTO;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RequestFunction;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import com.sq.transportmanage.gateway.service.auth.RoleManagementService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.sq.transportmanage.gateway.service.common.enums.MenuEnum.*;


/**
 * @Author fanht
 * 角色管理
 */
@RestController
public class RolemanagementController{
	@Autowired
	private RoleManagementService roleManagementService;

	@Autowired
	private PermissionManagementService permissionManagementService;
	
	/**一、增加一个角色**/
	@RequestMapping("/addSaasRole")
	@RequiresPermissions(value = { "ADD_SAAS_ROLE" } )
	@RequestFunction(menu = ROLE_ADD)
	public AjaxResponse addSaasRole(@Verify(param="roleCode",rule="required") String roleCode, @Verify(param="roleName",rule="required") String roleName,String roleDesc) {
		SaasRole role = new SaasRole();
		role.setRoleCode(roleCode.trim());
		role.setRoleName(roleName.trim());
		role.setMerchantId(WebSessionUtil.getCurrentLoginUser().getMerchantId());
		role.setValid(true);
		role.setRoleDesc(roleDesc);
		role.setUpdateTime(new Date());
		role.setCreateTime(new Date());
		role.setRoleDesc(roleDesc);
		role.setCreaterId(WebSessionUtil.getCurrentLoginUser().getId());
		return roleManagementService.addSaasRole(role);
	}

	/**二、禁用一个角色**/
	@RequestMapping("/disableSaasRole")
	@RequiresPermissions(value = { "DISABLE_SAAS_ROLE" } )
	@RequestFunction(menu = ROLE_DISABLE)
	public AjaxResponse disableSaasRole ( @Verify(param="roleId",rule="required|min(1)") Integer roleId ) {
		return roleManagementService.disableSaasRole(roleId);
	}
	
	/**三、启用一个角色**/
	@RequestMapping("/enableSaasRole")
	@RequiresPermissions(value = { "ENABLE_SAAS_ROLE" } )
	@RequestFunction(menu = ROLE_ENABLE)
	public AjaxResponse enableSaasRole ( @Verify(param="roleId",rule="required|min(1)") Integer roleId ) {
		return roleManagementService.enableSaasRole(roleId);
	}
	
	/**四、修改一个角色**/
	@RequestMapping("/changeRole")
	@RequiresPermissions(value = { "CHANGE_SAAS_ROLE" } )
	@RequestFunction(menu = ROLE_UPDATE)
	public 	AjaxResponse changeRole( @Verify(param="roleId",rule="required|min(1)") Integer roleId ,
									 @Verify(param="roleName",rule="required") String roleName,
									  String roleDesc ) {
		SaasRole roleForupdate = new SaasRole();
		roleForupdate.setRoleId(roleId);
		roleForupdate.setRoleName(roleName.trim());
		roleForupdate.setRoleDesc(roleDesc);
		return roleManagementService.changeRole(roleForupdate);
	}

	/**五、查询一个角色中的权限（返回的数据格式：列表、树形）**/
	@RequestMapping("/getAllPermissions")
	@RequiresPermissions(value = { "GET_ALL_ROLE_PERMISSIONS" } )
	@RequestFunction(menu = ROLE_PERMISSION_LIST)
	public AjaxResponse getAllPermissions( @Verify(param="roleId",rule="required|min(1)") Integer roleId,  String dataFormat ){
		if( !SaasConst.PermissionDataFormat.TREE.equalsIgnoreCase(dataFormat) && !SaasConst.PermissionDataFormat.LIST.equalsIgnoreCase(dataFormat) ) {
			/**默认为树形*/
			dataFormat = SaasConst.PermissionDataFormat.TREE;
		}
		List<SaasPermissionDTO> permissionDtos = roleManagementService.getAllPermissions(roleId, dataFormat);
		return AjaxResponse.success(permissionDtos);
	}

	/**六、查询一个角色中的权限ID**/
	@RequestMapping("/getAllPermissionIds")
	@RequiresPermissions(value = { "GET_ALL_ROLE_PERMISSIONS" } )
	@RequestFunction(menu = ROLE_PERMISSION_IDS)
	public AjaxResponse getAllPermissionIds( @Verify(param="roleId",rule="required|min(1)") Integer roleId){
		List<String> permissionIds = permissionManagementService.queryPermissionsOfRoleId(roleId);
		return AjaxResponse.success(permissionIds);
	}



	/**六、查询一个角色中的权限ID 以Integer类型返回**/
	@RequestMapping("/getAllPermissionIdsOfInteger")
	@RequiresPermissions(value = { "GET_ALL_ROLE_PERMISSIONS" } )
	@RequestFunction(menu = ROLE_PERMISSION_IDS)
	public AjaxResponse getAllPermissionIdsOfInteger( @Verify(param="roleId",rule="required|min(1)") Integer roleId){
		List<Integer> permissionIds = roleManagementService.getAllPermissionIds(roleId);
		return AjaxResponse.success(permissionIds);
	}
	
	/**七、保存一个角色中的权限ID**/
	@RequestMapping("/savePermissionIds")
	@RequiresPermissions(value = { "GET_ALL_ROLE_PERMISSIONS" } )
	@RequestFunction(menu = ROLE_PERMISSION_SAVE)
	public AjaxResponse savePermissionIds(@Verify(param="roleId",rule="required|min(1)") Integer roleId,
										  @Verify(param="permissionIds",rule="required") String permissionIds) {
		List<Integer> newPermissionIds = new ArrayList<Integer>();
		if(StringUtils.isNotEmpty(permissionIds) ) {
			String[]  ids = permissionIds.split(",");
			if(ids.length>0) {
				for(String id : ids ) {
					if(StringUtils.isNotEmpty(id)) {
						try {
							String[] strs = id.split("-");
							if(strs.length > 0){
								for(String str : strs){
									if(StringUtils.isNotEmpty(str) && !newPermissionIds.contains(Integer.valueOf(str))){
										newPermissionIds.add(Integer.valueOf(str));
									}
								}
							}
						}catch(Exception ex) {
							return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
						}
					}
				}
			}
		}
		return roleManagementService.savePermissionIds(roleId, newPermissionIds);
	}
	
	/**八、查询角色列表**/
	@RequestMapping("/queryRoleList")
	@RequiresPermissions(value = { "ROLE_MANAGEMENT" } )
	@RequestFunction(menu = ROLE_LIST)
	public AjaxResponse queryRoleList( 
			@Verify(param="page",rule="required|min(1)") Integer page, 
			@Verify(param="pageSize",rule="required|min(10)") Integer pageSize,  
			String roleCode , 
			String roleName, 
			Byte valid,
			String createStartTime,
			String createEndTime) {
		PageDTO pageDto = roleManagementService.queryRoleList(page, pageSize, roleCode, roleName, valid,createStartTime,createEndTime);
		return AjaxResponse.success( pageDto );
	}
	
	/**九、删除一个角色（只能开发人员用）**/
	@RequestMapping("/deleteSaasRole")
	@RequestFunction(menu = ROLE_DELETE)
	public AjaxResponse deleteSaasRole ( @Verify(param="roleId",rule="required|min(1)") Integer roleId ) {
		return roleManagementService.deleteSaasRole(roleId);
	}

	/**一、增加一个角色**/
	@RequestMapping("/addAndSaveSaasRole")
	@RequiresPermissions(value = { "ADD_SAAS_ROLE" } )
	@RequestFunction(menu = ROLE_ADD)
	public AjaxResponse addAndSaveSaasRole(
									@Verify(param="roleName",rule="required") String roleName,
									String roleDesc,
									@Verify(param="permissionIds",rule="required") String permissionIds) {
		SaasRole role = new SaasRole();
		String	roleCode = UUID.randomUUID().toString().replaceAll("-","").toUpperCase()+ System.currentTimeMillis();
		role.setRoleCode(roleCode.trim());
		role.setRoleName(roleName.trim());
		role.setMerchantId(WebSessionUtil.getCurrentLoginUser().getMerchantId());
		role.setValid(true);
		role.setRoleDesc(roleDesc);
		role.setUpdateTime(new Date());
		role.setCreateTime(new Date());
		role.setRoleDesc(roleDesc);
		role.setCreaterId(WebSessionUtil.getCurrentLoginUser().getId());
		AjaxResponse response = roleManagementService.addSaasRole(role);
		if(response != null && response.getCode() == 0){
			role = (SaasRole) response.getData();
			List<Integer> newPermissionIds = new ArrayList<Integer>();
			if(StringUtils.isNotEmpty(permissionIds) ) {
				String[]  ids = permissionIds.split(",");
				if(ids.length>0) {
					for(String id : ids ) {
						if(StringUtils.isNotEmpty(id)) {
							try {
								String[] strs = id.split("-");
								if(strs.length > 0){
									for(String str : strs){
										if(StringUtils.isNotEmpty(str) && !newPermissionIds.contains(Integer.valueOf(str))){
											newPermissionIds.add(Integer.valueOf(str));
										}
									}
								}
							}catch(Exception ex) {
								return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
							}
						}
					}
				}
			}
			return roleManagementService.savePermissionIds(role.getRoleId(), newPermissionIds);
		}

		return  response;

	}

	/**
	 * 获取角色信息
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/getRoleDetail")
	@ResponseBody
	@RequiresPermissions(value = {"GET_ALL_ROLE_PERMISSIONS"})
	public AjaxResponse getRoleDetail(@Verify(param = "roleId",rule="required|min(1)") Integer roleId){
		SaasRole saasRole = roleManagementService.findByPrimaryKey(roleId);
		return AjaxResponse.success(saasRole);
	}


	/**接口专用 保存一个角色中的权限ID**/
	@RequestMapping("/saveAnyPermissionIds")
	@RequiresPermissions(value = { "GET_ALL_ROLE_PERMISSIONS" } )
	@RequestFunction(menu = ROLE_PERMISSION_SAVE)
	public AjaxResponse saveAnyPermissionIds(@Verify(param="roleId",rule="required|min(1)") Integer roleId,
										  @Verify(param="permissionIds",rule="required") String permissionIds) {
		List<Integer> newPermissionIds = new ArrayList<Integer>();
		if(StringUtils.isNotEmpty(permissionIds) ) {
			String[]  ids = permissionIds.split(",");
			if(ids.length > 0){
				for(String str : ids){
					if(StringUtils.isNotEmpty(str) && !newPermissionIds.contains(Integer.valueOf(str))){
						newPermissionIds.add(Integer.valueOf(str));
					}
				}
			}
		}
		return roleManagementService.savePermissionIds(roleId, newPermissionIds);
	}
}