package com.sq.transportmanage.gateway.dao.entity.rentcar;

import lombok.Data;

@Data
public class CarBizCarInfoVO {
    private Integer carId;

    private Integer supplierId;

    private String licensePlates;

    private String brand;

    private Integer carModelId;

    private String modelDetail;

    private String color;

    private String engineNo;

    private String frameNo;

    private String nextInspectDate;

    private String nextMaintenanceDate;

    private String rentalExpireDate;

    private String memo;

    private Integer status;

    private Integer createBy;

    private Integer updateBy;

    private String createDate;

    private String updateDate;

    private String imageUrl;//车辆图片
    //关联 司机的id
    private Integer driverid;
    //下次运营证检验时间
    private String nextOperationDate;
    //下次治安证检测时间
    private String nextSecurityDate;
    //下次等级验证时间
    private String nextClassDate;
    //二级维护时间
    private String twoLevelMaintenanceDate;

    private Integer cityId;
    private String cityName;
    private String modeName;
    private String supplierName;
    private String purchaseDate;
    private String carAge;
    private String timeOfnextInspect;
    private String timeOfnextMaintenance;
    private String driverName;
    private String carPhotographName;
    private String createName;
    private String updateName;

    // 原有车牌号
    private String licensePlates1;

    //购买日期 开始时间
    private String purchaseDateBegin;

    //购买日期 结束时间
    private String purchaseDateEnd;
    //下次维保开始时间
    private String nextMaintenanceDateBegin;
    //下次维保结束时间
    private String nextMaintenanceDateEnd;
    //下次治安证开始时间
    private String nextSecurityDateBegin;
    //下次治安证结束时间
    private String nextSecurityDateEnd;
    //下次运营证开始时间
    private String nextOperationDateBegin;
    //下次运营证结束时间
    private String nextOperationDateEnd;
    //下次等级鉴定开始时间
    private String nextClassDateBegin;
    //下次等级鉴定结束时间
    private String nextClassDateEnd;

    private String purchaseDateString;//购车时间的String
    private String rentalExpireDateString;//租赁时间的String
    private String nextInspectDateString;//下次车检时间的String
    private String nextMaintenanceDateString;//下次维保时间的String

    //核定载客位
    private String carryPassengers;
    //车辆厂牌
    private String vehicleBrand;
    //车牌颜色
    private String clicensePlatesColor;
    //车辆VIN码
    private String vehicleVINCode;
    //车辆注册日期
    private String vehicleRegistrationDate;
    //车辆燃料类型
    private Integer fuelType;
    //车辆发动机排量
    private String vehicleEngineDisplacement;
    //发动机功率
    private String vehicleEnginePower;
    //车辆轴距
    private String vehicleEngineWheelbase;
    //网络预约出租汽车运输证号
    private String transportNumber;
    //网络预约出租汽车运输证发证机构
    private String certificationAuthority;
    //经营区域
    private String operatingRegion;
    //网络预约出租汽车运输证有效期起
    private String transportNumberDateStart;
    //网络预约出租汽车运输证有效期止
    private String transportNumberDateEnd;
    //网约车初次登记日期
    private String firstDate;
    //车辆检修状态
    private Integer overHaulStatus;
    //年度审验状态
    private Integer auditingStatus;
    //车辆年度审验日期
    private String auditingDate;
    //发票打印设备序列号
    private String equipmentNumber;
    //卫星定位装置品牌
    private String gpsBrand;
    //卫星定位装置型号
    private String gpsType;
    //卫星定位装置IMEI号
    private String gpsImei;
    //卫星定位装置安装日期
    private String gpsDate;
    //所属车主
    private String vehicleOwner;
    //车辆类型（以机动车行驶证为主）
    private String vehicleType;
    //车辆行驶证扫描件
    private String vehicleDrivingLicense;
    // 车辆更改供应商，需要把关联司机从原有供应商更改
    private Integer oldSupplierId;

    private String modelName;

    private Integer groupId;

    private String groupName;

    private Integer seatNum;

    private Integer cooperationType;

    private Integer type;

    private Integer taxiInvoicePrint;

    //update 2019/6/28
    private Integer chengzuSupplierId;   //承租方id

    private Integer chengzuCooperationType;           //承租方加盟类型

    private Integer receivingState;      //待收车状态

    private Integer runawayState;        //失控状态

    private Integer rentArrearsState;    //欠租状态

    private Integer maintenanceState;    //维修状态

    private Integer complianceState;     //合租状态

    private Integer carRentStatusType;   //车辆类型状态
    //车辆是否空闲
    private Integer isFree;

    // 权限
    private String supplierIds;
    private String cities;

    //把查询车辆状态和加盟商类型转化为查询  供应商ids和承租方ids
    private String searchSupplierIds;
    private String searchChengzuSupplierIds;

    /**
     * 车辆性质，A：非营运，D：营运,G:租赁
     */
    private String carProperty;
}
