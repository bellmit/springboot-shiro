package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UseCarPassengerTravel {
    private Integer id;
    @NotNull(message = "会议id不能为空")
    private Integer conferenceId;
    @NotNull(message = "乘客分类不能为空")
    private String passengerType;

    private Integer carTypeNo;
    @NotNull(message = "乘坐车型不能为空")
    private String carTypeName;
    @NotNull(message = "车数不能为空")
    @Min(value = 1,message = "数量最小为1")
    private Integer carCount;
    @NotNull(message = "人数不能为空")
    @Min(value = 1,message = "数量最小为1")
    private Integer passengerCount;
    @NotNull(message = "联系方式不能为空")
    private String mobile;
    @NotNull(message = "上车地点不能为空")
    private String boardingLocation;

    private String dropOfPlace;
    @NotNull(message = "出发时间不能为空")
    private String goOfTimeStr;

    private Date goOfTime;

    private String arrivalTimeStr;

    private Date arrivalTime;
    @NotNull(message = "备注不能为空")
    private String remark;

    private Date createTime;

    private Date updateTime;

    private BigDecimal driverPrice;//司机价格

    private BigDecimal passengerPrice;//乘客价格

    /**
     * 上车坐标 高德;百度
     * xx.xxxxxx,xx.xxxxxx;xx.xxxxxx,xx.xxxxxx
     */
    private String coordinate;

    private String offCoordinate;
    @NotNull(message = "联络员名称不能为空")
    private String liaisonManName;

    private Integer status;

    private Integer customerId;

}