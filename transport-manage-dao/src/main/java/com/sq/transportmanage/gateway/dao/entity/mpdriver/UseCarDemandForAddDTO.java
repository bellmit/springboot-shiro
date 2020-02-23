package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 分组用车需求
 */
@Data
public class UseCarDemandForAddDTO {
    /**
     * 乘客分组
     */
    @NotBlank(message = "passengerType不能为空")
    private String passengerType;

    /**
     * 车型ID
     */
    @NotNull(message = "carTypeNo不能为空")
    private Integer carTypeNo;

    /**
     * 车型名称
     */
    @NotBlank(message = "carTypeName不能为空")
    private String carTypeName;

    /**
     * 车型数量
     */
    @NotNull(message = "carCount不能为空")
    private Integer carCount;

    /**
     * 会议ID
     */
    @NotNull(message = "conferenceId不能为空")
    private Integer conferenceId;

    /**
     * 用车时间
     */
    @NotNull(message = "useDate不能为空")
    private String useDate;
}