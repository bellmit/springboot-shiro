package com.sq.transportmanage.gateway.dao.entity.rentcar;

import lombok.Data;

import java.util.Date;

/**
 * @author admin
 */
@Data
public class CarBizDriverInfo {
    private Integer driverId;

    private String password;

    private String phone;

    private Integer gender;

    private String name;

    private Integer supplierId;

    private Integer age;

    private String currentAddress;

    private String emergencyContactPerson;

    private String emergencyContactNumber;

    private String idCardNo;

    private String superintendNo;

    private String superintendUrl;

    private String drivingLicenseType;

    private Integer drivingYears;

    private String archivesNo;

    private Date issueDate;

    private Date expireDate;

    private Integer serviceCity;
    private Integer serviceCityId;

    private String attachmentName;

    private String attachmentAddr;

    private String accountBank;

    private String bankAccountNo;

    private String licensePlates;

    private Date updateDate;

    private Date createDate;

    private Integer updateBy;

    private Integer createBy;

    private Integer status;

    private String mac;

    private String photosrct;

    private String pushId;

    private Integer isBindingCreditCard;

    private String appVersion;

    private String creditCardNo;

    private String storableCardNo;

    private String creditOpenAccountBank;

    private String creditCardPeriodOfValidity;

    private Integer grabNotice;

    private String creditCvn2;

    private Date creditBindDate;

    private String quickpayCustomerid;

    private Integer isRead;

    private String bankid;

    private String goHomeAddress;

    private String goHomePointLobd;

    private String goHomePointLabd;

    private String goHomePointLa;

    private String goHomePointLo;

    private Integer goHomeStatus;

    private String updateGoHomeDate;

    private String imei;

    private String driverlicensenumber;

    private String drivinglicenseimg;

    private String firstdrivinglicensedate;

    private String firstmeshworkdrivinglicensedate;

    private String nationality;

    private String householdregister;

    private String nation;

    private String marriage;

    private String foreignlanguage;

    private String address;

    private String education;

    private String coursename;

    private String coursedate;

    private String coursedatestart;

    private String coursedateend;

    private String coursetime;

    private String corptype;

    private String trafficviolations;

    private String signdate;

    private String signdateend;

    private String contractdate;

    private Integer isxydriver;

    private String parttimejobdri;

    private String phonetype;

    private String phonecorp;

    private String appversion;

    private String maptype;

    private String emergencycontactaddr;

    private String assessment;

    private String driverlicenseissuingdatestart;

    private String driverlicenseissuingdateend;

    private String driverlicenseissuingcorp;

    private String driverlicenseissuingnumber;

    private String xyDriverNumber;

    private String driverLicenseIssuingRegisterDate;

    private String driverLicenseIssuingFirstDate;

    private String driverLicenseIssuingGrantDate;

    private String birthDay;

    private String houseHoldRegisterPermanent;

    private Integer isUploadCard;

    private Integer isMustConfirmation;

    private String contractType;

    private String contractNo;

    private String contractFile;

    private Integer cooperationType;

    private Integer accountType;

    private Integer groupId;

    private Integer groupid;

    private Integer isTwoShifts;

    private Integer isImage;

    private String memo;

    private String supplierName;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
}