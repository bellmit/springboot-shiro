package com.sq.transportmanage.gateway.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sq.transportmanage.gateway.service.enums.CancelStatusEnum;
import com.sq.transportmanage.gateway.service.common.enums.ServiceTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class UsecarMeetingOrderVo {

    /**主键ID**/
    private Long meetingOrderId;

    /**会议ID**/
    private Integer conferenceId;

    /**订单编号**/
    private String orderNo;

    /**预约用车时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bookingDate;

    /**创建时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**乘车人姓名**/
    private Integer cityId;

    private String riderName;

    /**乘车人电话**/
    private String riderPhone;

    /**预定上车地点**/
    private Integer bookingUserId;

    private String bookingUserPhone;

    private String bookingStartAddr;

    /**预定下车地点**/
    private String bookingEndAddr;

    /**乘坐车型**/
    private Integer bookingCarTypeNo;

    /**乘坐车型名称**/
    private String bookingCarTypeName;

    private Byte billingMode;

    private Boolean buyoutFlag;

    private BigDecimal buyoutPrice;

    private Integer estimatedId;

    private BigDecimal agreedPrice;

    private BigDecimal estimatedAmount;

    private String flightNumber;

    private Integer laterMinute;

    private Integer serviceTypeId;

    private BigDecimal actualPayAmount;

    private Integer businessId;

    private String bookingStartPoint;

    private String bookingEndPoint;

    private Integer orderStatus;

    private Integer status;

    private Integer driverId;

    private String driverName;

    private String licenseNumber;

    private String driverPhone;

    private String cancelStatus;

    private String cancelReason;

    private String serviceTypeName;

    public Long getMeetingOrderId() {
        return meetingOrderId;
    }

    public void setMeetingOrderId(Long meetingOrderId) {
        this.meetingOrderId = meetingOrderId;
    }

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName == null ? null : riderName.trim();
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone == null ? null : riderPhone.trim();
    }

    public Integer getBookingUserId() {
        return bookingUserId;
    }

    public void setBookingUserId(Integer bookingUserId) {
        this.bookingUserId = bookingUserId;
    }

    public String getBookingUserPhone() {
        return bookingUserPhone;
    }

    public void setBookingUserPhone(String bookingUserPhone) {
        this.bookingUserPhone = bookingUserPhone == null ? null : bookingUserPhone.trim();
    }

    public String getBookingStartAddr() {
        return bookingStartAddr;
    }

    public void setBookingStartAddr(String bookingStartAddr) {
        this.bookingStartAddr = bookingStartAddr == null ? null : bookingStartAddr.trim();
    }

    public String getBookingEndAddr() {
        return bookingEndAddr;
    }

    public void setBookingEndAddr(String bookingEndAddr) {
        this.bookingEndAddr = bookingEndAddr == null ? null : bookingEndAddr.trim();
    }

    public Integer getBookingCarTypeNo() {
        return bookingCarTypeNo;
    }

    public void setBookingCarTypeNo(Integer bookingCarTypeNo) {
        this.bookingCarTypeNo = bookingCarTypeNo;
    }

    public String getBookingCarTypeName() {
        return bookingCarTypeName;
    }

    public void setBookingCarTypeName(String bookingCarTypeName) {
        this.bookingCarTypeName = bookingCarTypeName == null ? null : bookingCarTypeName.trim();
    }

    public Byte getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(Byte billingMode) {
        this.billingMode = billingMode;
    }

    public Boolean getBuyoutFlag() {
        return buyoutFlag;
    }

    public void setBuyoutFlag(Boolean buyoutFlag) {
        this.buyoutFlag = buyoutFlag;
    }

    public BigDecimal getBuyoutPrice() {
        return buyoutPrice;
    }

    public void setBuyoutPrice(BigDecimal buyoutPrice) {
        this.buyoutPrice = buyoutPrice;
    }

    public Integer getEstimatedId() {
        return estimatedId;
    }

    public void setEstimatedId(Integer estimatedId) {
        this.estimatedId = estimatedId;
    }

    public BigDecimal getAgreedPrice() {
        return agreedPrice;
    }

    public void setAgreedPrice(BigDecimal agreedPrice) {
        this.agreedPrice = agreedPrice;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber == null ? null : flightNumber.trim();
    }

    public Integer getLaterMinute() {
        return laterMinute;
    }

    public void setLaterMinute(Integer laterMinute) {
        this.laterMinute = laterMinute;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public BigDecimal getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getBookingStartPoint() {
        return bookingStartPoint;
    }

    public void setBookingStartPoint(String bookingStartPoint) {
        this.bookingStartPoint = bookingStartPoint == null ? null : bookingStartPoint.trim();
    }

    public String getBookingEndPoint() {
        return bookingEndPoint;
    }

    public void setBookingEndPoint(String bookingEndPoint) {
        this.bookingEndPoint = bookingEndPoint == null ? null : bookingEndPoint.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName == null ? null : driverName.trim();
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber == null ? null : licenseNumber.trim();
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone == null ? null : driverPhone.trim();
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getCancelReason() {
        if (StringUtils.isNotEmpty(cancelStatus)){
            cancelReason= CancelStatusEnum.getDis(Integer.parseInt(cancelStatus));
        }
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getServiceTypeName() {
        if (getServiceTypeId()!=null){
            serviceTypeName= ServiceTypeEnum.getServiceTypeName(getServiceTypeId());
        }
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }
}
