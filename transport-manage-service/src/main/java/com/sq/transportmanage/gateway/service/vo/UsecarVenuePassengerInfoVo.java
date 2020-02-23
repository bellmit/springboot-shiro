package com.sq.transportmanage.gateway.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UsecarVenuePassengerInfoVo {

    private Integer conferenceId;

    private Integer venueId;

    private String venueName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date serviceDate;

    private String driverPhone;

    private String licensePlate;

    private Integer passengerSum;

    private String createTimeStr;

    private Integer frequency;
    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getPassengerSum() {
        return passengerSum;
    }

    public void setPassengerSum(Integer passengerSum) {
        this.passengerSum = passengerSum;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
