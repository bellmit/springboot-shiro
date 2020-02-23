package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;

/**
 * 分组用车需求
 */
@Data
public class UseCarDemandForCar {

    private Integer id;
    /**
     * 乘客分组
     */
    private String passengerType;

    /**
     * 车型ID
     */
    private Integer carTypeNo;

    /**
     * 车型名称
     */
    private String carTypeName;

    /**
     * 车型数量
     */
    private Integer carCount;

    /**
     * 实际用车数量
     */
    private Integer factCarCount;

}