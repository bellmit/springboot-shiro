package com.sq.transportmanage.gateway.dao.entity.driverspark.base;

import java.util.Date;

public class BaseDriverTeam {
    private Integer id;

    private String teamName;

    private Integer merchantId;

    private String merchantName;

    private Integer supplierId;

    private String supplierName;

    private Integer cityId;

    private String cityName;

    private Integer type;

    private Integer status;

    private Integer pId;

    private String oneChargeName;

    private Integer oneChargePhone;

    private String twoChargeName;

    private Integer twoChargePhone;

    private String threeChargeName;

    private Integer threeChargePhone;

    private String nickname;

    private String remark;

    private Integer ext1;

    private String ext2;

    private String ext3;

    private Integer createUser;

    private Integer updateUser;

    private String createUserName;

    private String updateUserName;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
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

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getOneChargeName() {
        return oneChargeName;
    }

    public void setOneChargeName(String oneChargeName) {
        this.oneChargeName = oneChargeName == null ? null : oneChargeName.trim();
    }

    public Integer getOneChargePhone() {
        return oneChargePhone;
    }

    public void setOneChargePhone(Integer oneChargePhone) {
        this.oneChargePhone = oneChargePhone;
    }

    public String getTwoChargeName() {
        return twoChargeName;
    }

    public void setTwoChargeName(String twoChargeName) {
        this.twoChargeName = twoChargeName == null ? null : twoChargeName.trim();
    }

    public Integer getTwoChargePhone() {
        return twoChargePhone;
    }

    public void setTwoChargePhone(Integer twoChargePhone) {
        this.twoChargePhone = twoChargePhone;
    }

    public String getThreeChargeName() {
        return threeChargeName;
    }

    public void setThreeChargeName(String threeChargeName) {
        this.threeChargeName = threeChargeName == null ? null : threeChargeName.trim();
    }

    public Integer getThreeChargePhone() {
        return threeChargePhone;
    }

    public void setThreeChargePhone(Integer threeChargePhone) {
        this.threeChargePhone = threeChargePhone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getExt1() {
        return ext1;
    }

    public void setExt1(Integer ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}