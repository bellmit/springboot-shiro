package com.sq.transportmanage.gateway.service.common.shiro.realm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.service.common.dto.SaasPermissionDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**当前的登录用户信息**/
public final class SSOLoginUser implements Serializable{
	private static final long serialVersionUID = 1198761461846111111L;
	//eg:   {"loginName":"wangjiaqi","mobile":"17600606250","name":"王佳琪","id":287,"type":2,"email":"wangjiaqi@01zhuanche.com","status":1}
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
	/**TODO：**/
	private Integer type;
	/**状态**/
	private Integer status;
	/**帐号类型：[100 普通用户]、[900 管理员]**/
	private Integer accountType;
	//---------------------------------------------------------------------------------------------------------数据权限BEGIN

	/**此用户可以管理的城市ID**/
	private String cityIds ;
	/**此用户可以管理的车队ID**/
	private String teamIds ;
	/**此用户可以管理的班组ID***/
	private String groupIds ;
	/**此用户数据权限等级(参考PermissionLevelEnum枚举类型 1合作商 2城市 4车队 8班组 ) **/
	private Integer dataLevel;



	/**此用户可以管理的供应商ID**/
	private String supplierIds;

	/**此用户数据权限等级(参考PermissionLevelEnum枚举类型 1全国 2城市 4供应商 8小队 16班组) **/
	private Integer level;

	private Integer merchantId;

	private List<String> menuUrlList;

	private Map<Integer,String> menuPermissionMap;

	private Boolean isSuper;

	/**所有模块的权限**/
	private Map<Integer,List<SaasPermissionDTO>> mapPermission = Maps.newHashMap();

	private String merchantArea;
	//---------------------------------------------------------------------------------------------------------数据权限END
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}


	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}


	public List<String> getMenuUrlList() {
		return menuUrlList;
	}

	public void setMenuUrlList(List<String> menuUrlList) {
		this.menuUrlList = menuUrlList;
	}



	public Boolean getSuper() {
		return isSuper;
	}

	public void setSuper(Boolean aSuper) {
		isSuper = aSuper;
	}


	public Map<Integer, String> getMenuPermissionMap() {
		return menuPermissionMap;
	}

	public void setMenuPermissionMap(Map<Integer, String> menuPermissionMap) {
		this.menuPermissionMap = menuPermissionMap;
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

	public Map<Integer, List<SaasPermissionDTO>> getMapPermission() {
		return mapPermission;
	}

	public void setMapPermission(Map<Integer, List<SaasPermissionDTO>> mapPermission) {
		this.mapPermission = mapPermission;
	}


	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public String getTeamIds() {
		return teamIds;
	}

	public void setTeamIds(String teamIds) {
		this.teamIds = teamIds;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Integer getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(Integer dataLevel) {
		this.dataLevel = dataLevel;
	}


	public String getMerchantArea() {
		return merchantArea;
	}

	public void setMerchantArea(String merchantArea) {
		this.merchantArea = merchantArea;
	}

	@Override
	public String toString() {
		return "SSOLoginUser{" +
				"id=" + id +
				", loginName='" + loginName + '\'' +
				", mobile='" + mobile + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", type=" + type +
				", status=" + status +
				", accountType=" + accountType +
				", cityIds='" + cityIds + '\'' +
				", teamIds='" + teamIds + '\'' +
				", groupIds='" + groupIds + '\'' +
				", dataLevel=" + dataLevel +
				", supplierIds='" + supplierIds + '\'' +
				", level=" + level +
				", merchantId=" + merchantId +
				", menuUrlList=" + JSONObject.toJSONString(menuUrlList) +
				", menuPermissionMap=" + JSONObject.toJSONString(menuPermissionMap) +
				", isSuper=" + isSuper +
				", mapPermission=" + JSONObject.toJSONString(mapPermission) +
				'}';
	}

}

