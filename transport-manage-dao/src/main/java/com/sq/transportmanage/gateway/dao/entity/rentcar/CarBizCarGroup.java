package com.sq.transportmanage.gateway.dao.entity.rentcar;

import java.util.Date;

public class CarBizCarGroup {
    private Integer groupId;

    private String groupName;

    private Integer seatNum;

    private Integer rank;

    private Integer sort;

    private Integer charteredStatus;

    private Integer status;

    private Integer createBy;

    private Integer updateBy;

    private Date createDate;

    private Date updateDate;

    private String selectedImgUrl;

    private String selectedUrlNew;

    private String unselectedUrlNew;

    private Integer type;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getCharteredStatus() {
        return charteredStatus;
    }

    public void setCharteredStatus(Integer charteredStatus) {
        this.charteredStatus = charteredStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSelectedImgUrl() {
        return selectedImgUrl;
    }

    public void setSelectedImgUrl(String selectedImgUrl) {
        this.selectedImgUrl = selectedImgUrl == null ? null : selectedImgUrl.trim();
    }

    public String getSelectedUrlNew() {
        return selectedUrlNew;
    }

    public void setSelectedUrlNew(String selectedUrlNew) {
        this.selectedUrlNew = selectedUrlNew == null ? null : selectedUrlNew.trim();
    }

    public String getUnselectedUrlNew() {
        return unselectedUrlNew;
    }

    public void setUnselectedUrlNew(String unselectedUrlNew) {
        this.unselectedUrlNew = unselectedUrlNew == null ? null : unselectedUrlNew.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}