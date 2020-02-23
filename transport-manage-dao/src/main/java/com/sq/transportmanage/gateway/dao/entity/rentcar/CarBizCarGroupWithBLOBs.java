package com.sq.transportmanage.gateway.dao.entity.rentcar;

public class CarBizCarGroupWithBLOBs extends CarBizCarGroup {
    private String memo;

    private String imgUrl;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }
}