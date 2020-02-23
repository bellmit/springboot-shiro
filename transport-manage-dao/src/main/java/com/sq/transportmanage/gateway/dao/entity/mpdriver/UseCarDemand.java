package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;

import java.util.Date;

/**
 * 分组用车需求
 */
@Data
public class UseCarDemand {
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
     * 用车时间
     */
    private String useDate;

    /**
     * 车型名称
     */
    private String carTypeName;

    /**
     * 车型数量
     */
    private Integer carCount;

    /**
     * 会议ID
     */
    private Integer conferenceId;

    private Date createTime;

    private Date updateTime;
}