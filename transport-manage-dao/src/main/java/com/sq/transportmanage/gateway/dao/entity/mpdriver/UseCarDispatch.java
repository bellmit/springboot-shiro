package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 调度Entity
 *
 * @author admin
 */
@Data
public class UseCarDispatch {
    private Integer id;
    /**
     * 乘客行程表id
     */
    private Integer passengerTravelId;

    /**
     * 会议id
     */
    private Integer conferenceId;

    /**
     * 乘客类型
     */
    private String passengerType;

    /**
     * 乘坐车型id
     */
    private Integer carTypeNo;

    /**
     * 乘坐车型名称
     */
    private String carTypeName;

    /**
     * 车型数量
     */
    private Integer carCount;

    /**
     * 乘客数量
     */
    private Integer passengerCount;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 联络员姓名
     */
    private String liaisonManName;

    /**
     * 上车地点
     */
    private String boardingLocation;

    /**
     * 下车地点
     */
    private String dropOfPlace;

    /**
     * 出发时间
     */
    private Date goOfTime;

    private String goOfTimeStr;

    /**
     * 到达时间
     */
    private Date arrivalTime;

    private String arrivalTimeStr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 司机ID
     */
    private Integer driverId;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 车牌号
     */
    private String licenseNumber;

    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 派单状态
     */
    private Integer dispatchStatus;

    /**
     * 绑定订单号
     */
    private String orderNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 坐标 高德;百度
     * xx.xxxxxx,xx.xxxxxx;xx.xxxxxx,xx.xxxxxx
     */
    private String coordinate;

    private String offCoordinate;

    private Integer customerId;

    private BigDecimal driverPrice;
    private BigDecimal passengerPrice;

}