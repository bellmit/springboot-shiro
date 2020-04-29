package com.sq.transportmanage.gateway.service.vo;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/13 下午6:14
 * @Version 1.0
 */
public class BaseMerchantCityConfigVo {

    private Integer id;

    private String cityName;

    private Integer cityId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
