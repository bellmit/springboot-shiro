package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import java.math.BigDecimal;
import java.util.Date;

public class UsecarMeetingOrder {
    private Long meetingOrderId;

    private Integer conferenceId;

    private Integer cityId;

    private String orderNo;

    private Date bookingDate;

    private Date createTime;

    private Date updateTime;

    private String riderName;

    private String riderPhone;

    private Integer bookingUserId;

    private String bookingUserPhone;

    private String bookingStartAddr;

    private String bookingEndAddr;

    private Integer bookingCarTypeNo;

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
        this.cancelStatus = cancelStatus == null ? null : cancelStatus.trim();
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason == null ? null : cancelReason.trim();
    }
}