package com.sq.transportmanage.gateway.dao.entity.mpdriver;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author admin
 */
@Data
public class UseCarConference {
    private Integer id;
    @NotNull(message = "开始时间不能为空")
    private String startTime;
    @NotNull(message = "结束时间不能为空")
    private String endTime;
    @NotNull(message = "会议名称不能为空")
    @Size(max = 20,message = "会议名称超长")
    private String name;
    @NotNull(message = "城市id不能为空")
    private Integer cityId;
    @NotNull(message = "城市名称不能为空")
    private String cityName;
    @NotNull(message = "主办方名称不能为空")
    @Size(max = 20, message = "主办方名称超长")
    private String sponsorName;
//    @NotNull(message = "主办方手机号不能为空")
//    @Size(max = 20,message = "主办方联系方式超长")
    private String sponsorMobile;
    @NotNull(message = "调度员名称不能为空")
    private String yardmanName;
    @NotNull(message = "调度员手机号不能为空")
    @Pattern(regexp = "^((1[0-9]))\\d{9}$", message = "调度员手机号格式不正确")
    private String yardmanMobile;

    private Integer type;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private String updateBy;

    /**
     * 1:进行中
     */
    private Integer underway;

    @NotNull(message = "所属企业id不能为空")
    private Integer companyId;
    @NotNull(message = "所属企业名称不能为空")
    private String companyName;
    @NotNull(message = "所属企业电话不能为空")
    private String companyPhone;
}