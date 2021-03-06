package com.sq.transportmanage.gateway.service.common.dto;

import java.io.Serializable;
import java.util.*;

/**返回给前端的当前登录用户信息**/
public final class AjaxLoginUserDTO implements Serializable{
	private static final long serialVersionUID = 19849498400000000L;
	//---------------------------------------------------------------------------------------------------------用户基本信息BEGIN
	/**用户ID**/
	private Integer id;
	/**登录名**/
	private String loginName;
	/**手机号码**/
	private String mobile;
	/**真实姓名**/
	private String name;
	/**邮箱地址**/
	private String email;
	/**状态**/
	private Integer status;

	private Integer merchantId;

	private Integer dataLevel;
	//---------------------------------------------------------------------------------------------------------用户基本信息END
	
	//---------------------------------------------------------------------------------------------------------用户的菜单信息BEGIN
	private List<ConferencePermissionDTO> menus = new ArrayList<ConferencePermissionDTO>();

	private List<SaasPermissionDTO> permissionDTOS = new ArrayList<>();
	//---------------------------------------------------------------------------------------------------------用户的菜单信息END
	//---------------------------------------------------------------------------------------------------------用户的权限信息BEGIN
	private Set<String> holdPerms = new HashSet<String>();
	private Set<String> holdRoles = new HashSet<String>();
	//---------------------------------------------------------------------------------------------------------用户的权限信息END

	//---------------------------------------------------------------------------------------------------------用户的数据权限BEGIN
	/**此用户可以管理的城市ID**/
	private Set<Integer> cityIds = new HashSet<Integer>();

	/**此用户可以管理的供应商ID**/
	private String supplierIds ;
	//---------------------------------------------------------------------------------------------------------用户的数据权限END

	private Map<String, Object > configs = new HashMap<String,Object>();//配置信息

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List <ConferencePermissionDTO> getMenus() {
		return menus;
	}

	public void setMenus(List <ConferencePermissionDTO> menus) {
		this.menus = menus;
	}

	public Set<String> getHoldPerms() {
		return holdPerms;
	}

	public void setHoldPerms(Set<String> holdPerms) {
		this.holdPerms = holdPerms;
	}

	public Set<String> getHoldRoles() {
		return holdRoles;
	}

	public void setHoldRoles(Set<String> holdRoles) {
		this.holdRoles = holdRoles;
	}

	public Set<Integer> getCityIds() {
		return cityIds;
	}

	public void setCityIds(Set<Integer> cityIds) {
		this.cityIds = cityIds;
	}

	public Map<String, Object> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Object> configs) {
		this.configs = configs;
	}

	public List<SaasPermissionDTO> getPermissionDTOS() {
		return permissionDTOS;
	}

	public void setPermissionDTOS(List<SaasPermissionDTO> permissionDTOS) {
		this.permissionDTOS = permissionDTOS;
	}


	public String getSupplierIds() {
		return supplierIds;
	}

	public void setSupplierIds(String supplierIds) {
		this.supplierIds = supplierIds;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(Integer dataLevel) {
		this.dataLevel = dataLevel;
	}
}