//package com.sq.mp.manage.service.vo;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.sq.mp.manage.service.entity.mpdriver.UsecarVenueInfo;
//
//import java.util.Date;
//
//public class UsecarVenueInfoVo extends UsecarVenueInfo {
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    private Date validityStartDate;
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    private Date validityEndDate;
//
//    private Boolean expired;
//    @Override
//    public Date getValidityStartDate() {
//        return validityStartDate;
//    }
//
//    @Override
//    public void setValidityStartDate(Date validityStartDate) {
//        this.validityStartDate = validityStartDate;
//    }
//
//    @Override
//    public Date getValidityEndDate() {
//        return validityEndDate;
//    }
//
//    @Override
//    public void setValidityEndDate(Date validityEndDate) {
//        this.validityEndDate = validityEndDate;
//    }
//
//    public Boolean getExpired() {
//        return expired;
//    }
//
//    public void setExpired(Boolean expired) {
//        this.expired = expired;
//    }
//}
