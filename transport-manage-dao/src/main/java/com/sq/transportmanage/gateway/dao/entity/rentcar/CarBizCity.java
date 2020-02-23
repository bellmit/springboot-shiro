package com.sq.transportmanage.gateway.dao.entity.rentcar;

import lombok.Data;

import java.util.Date;

@Data
public class CarBizCity {
    private Integer cityId;

    private String cityName;

    private String code;

    private String cityspell;

    private Integer status;

    private Integer testStatus;

    private Integer sort;

    private Integer createBy;

    private Integer updateBy;

    private Date createDate;

    private Date updateDate;

    private String centrelo;

    private String centrela;

    private String centrelobd;

    private String centrelabd;

    private Integer serviceStatus;

    private Integer multiStatus;

    private String regionNumber;

    private Integer cooperationCity;

    private Integer invoice;

    private Integer startaddr;

    private Integer isPostpaid;

    private Integer isShowTime;

    private String memo;


}