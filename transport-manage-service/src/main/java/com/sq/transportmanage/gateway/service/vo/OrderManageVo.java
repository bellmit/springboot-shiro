package com.sq.transportmanage.gateway.service.vo;

import java.math.BigDecimal;

public class OrderManageVo {

    /**订单编号**/
    private String orderNo;

    /**预约用车时间**/
    private String bookingDate;

    /**乘车人姓名**/
    private String riderName;

    /**乘车人电话**/
    private String riderPhone;

    /**预定上车地点**/
    private Integer bookingUserId;

    /**预定人手机号**/
    private String bookingUserPhone;

    /**预定上车地点**/
    private String bookingStartAddr;

    /**预定下车地点**/
    private String bookingEndAddr;

    /**航班号**/
    private String flightNumber;

    /**航班起飞时间**/
    private String flightFlyDate;

    /**服务类型id**/
    private Integer serviceId;

    /**服务类型名称**/
    private String serviceName;

    /**费用**/
    private BigDecimal actualPayAmount;

    /**订单状态**/
    private Integer dicValue;

    /**订单状态名称**/
    private String dicName;

    /**司机姓名**/
    private String driverName;

    /**车牌号**/
    private String licensePlates;

    /**司机手机号**/
    private String driverPhone;

    /**车型**/
    private String groupName;

    /**日租半日租主单号**/
    private String charteredOrderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
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
        this.bookingUserPhone = bookingUserPhone;
    }

    public String getBookingStartAddr() {
        return bookingStartAddr;
    }

    public void setBookingStartAddr(String bookingStartAddr) {
        this.bookingStartAddr = bookingStartAddr;
    }

    public String getBookingEndAddr() {
        return bookingEndAddr;
    }

    public void setBookingEndAddr(String bookingEndAddr) {
        this.bookingEndAddr = bookingEndAddr;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightFlyDate() {
        return flightFlyDate;
    }

    public void setFlightFlyDate(String flightFlyDate) {
        this.flightFlyDate = flightFlyDate;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public Integer getDicValue() {
        return dicValue;
    }

    public void setDicValue(Integer dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLicensePlates() {
        return licensePlates;
    }

    public void setLicensePlates(String licensePlates) {
        this.licensePlates = licensePlates;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCharteredOrderNo() {
        return charteredOrderNo;
    }

    public void setCharteredOrderNo(String charteredOrderNo) {
        this.charteredOrderNo = charteredOrderNo;
    }

    @Override
    public String toString() {
        return "OrderManageVo{" +
                "orderNo='" + orderNo + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", riderName='" + riderName + '\'' +
                ", riderPhone='" + riderPhone + '\'' +
                ", bookingUserId=" + bookingUserId +
                ", bookingUserPhone='" + bookingUserPhone + '\'' +
                ", bookingStartAddr='" + bookingStartAddr + '\'' +
                ", bookingEndAddr='" + bookingEndAddr + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", flightFlyDate='" + flightFlyDate + '\'' +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", actualPayAmount=" + actualPayAmount +
                ", dicValue=" + dicValue +
                ", dicName='" + dicName + '\'' +
                ", driverName='" + driverName + '\'' +
                ", licensePlates='" + licensePlates + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", groupName='" + groupName + '\'' +
                ", charteredOrderNo='" + charteredOrderNo + '\'' +
                '}';
    }
}
