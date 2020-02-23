package com.sq.transportmanage.gateway.dao.entity.rentcar;

import lombok.Data;

@Data
public class CarBizDriverInfoVo extends CarBizDriverInfo {
    private String driverLicenseNumber;//机动车驾驶证号

    private String drivingLicenseImg;//驾驶证扫描件URL

    private String firstDrivingLicenseDate;//初次领取驾驶证日期

    private String firstMeshworkDrivingLicenseDate;//网络预约出租汽车驾驶员证初领日期

    private String nationAlity;//国籍

    private String houseHoldRegister;//户籍

    private String nation;//驾驶员民族

    private String marriage;//驾驶员婚姻状况

    private String foreignLanguage;//驾驶员外语能力

    private String education;//驾驶员学历

    private String courseName;//驾驶员培训课程名称

    private String courseDate;//培训课程日期（多个日期用,分割）

    private String courseDateStart;//培训开始时间

    private String courseDateEnd;//培训结束时间

    private String courseTime;//培训时长

    private String corpType;//驾驶员合同（或协议）签署公司标识

    private String trafficViolations;//交通违章次数

    private String signDate;//签署日期

    private String signDateEnd;//合同（或协议）到期时间

    private String contractDate;//有效合同时间

    private Integer isXyDriver;//是否巡游出租汽车驾驶员

    private String xyDriverNumber;//巡游出租汽车驾驶员资格证号

    private String partTimeJobDri;//专职或兼职司机

    private String phoneType;//司机手机型号

    private String phoneCorp;//司机手机运营商

    private String mapType;//

    private String emergencyContactAddr;//紧急情况联系人通讯地址

    private String assessment;//评估

    private String driverLicenseIssuingDateStart;//资格证有效起始日期

    private String driverLicenseIssuingDateEnd;//资格证有效截止日期

    private String driverLicenseIssuingCorp;//网络预约出租汽车驾驶员证发证机构

    private String driverLicenseIssuingNumber;//网络预约出租汽车驾驶员资格证号

    private String driverLicenseIssuingRegisterDate;//注册日期

    private String driverLicenseIssuingFirstDate;//初次领取资格证日期

    private String driverLicenseIssuingGrantDate;//资格证发证日期

    private String birthDay;//出生日期

    private String houseHoldRegisterPermanent;//户口登记机关名称

    private String isDriverBlack;//是否在驾驶员黑名单内

    //update  zll  2017-04-11 交通委需要修改以下信息的记录
    private String oldPhone;//手机号
    private String oldIdCardNo;//身份证
    private String oldDriverLicenseNumber;//机动车驾驶证号
    private String oldDriverLicenseIssuingNumber;//网络预约出租汽车驾驶员资格证号

    //update 2017-04-28 司机更改供应商，需要把改司机从原有供应商下的车队和车组删除
    private Integer oldSupplierId;//

    /**
     * 银行卡开户行
     */
    private String bankCardBank;

    /**
     * 银行卡卡号
     */
    private String bankCardNumber;

    /**
     * 可扩展1:司机停运状态  1停运 2正常  司机新建是默认为2
     */
    private Integer ext1;

    /**
     * 可扩展2
     */
    private Integer ext2;

    /**
     * 可扩展3
     */
    private Integer ext3;

    /**
     * 可扩展4
     */
    private Integer ext4;

    /**
     * 可扩展5
     */
    private Integer ext5;

    /**
     * 可扩展6
     */
    private String ext6;

    /**
     * 可扩展7
     */
    private String ext7;

    /**
     * 可扩展8
     */
    private String ext8;

    /**
     * 可扩展9
     */
    private String ext9;

    /**
     * 可扩展10
     */
    private String ext10;

    private String teamid;//车队
    private String teamName;
    private String groupName;

    private String teamGroupId;
    private String teamGroupName;
    private Integer isTwoShifts; //是否是双班司机  0/空值:单班   1:双班
}
