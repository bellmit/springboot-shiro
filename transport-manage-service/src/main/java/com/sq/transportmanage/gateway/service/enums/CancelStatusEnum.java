package com.sq.transportmanage.gateway.service.enums;

/**
 * @Description:取消来源信息
 */
public enum CancelStatusEnum {

    STATUS_1(1,"pc端提交"),
    STATUS_2(2,"手机端提交"),
    STATUS_3(3,"乘客端APP(超时)"),
    STATUS_4(4,"系统自动取消(日租/半日租乘客15分钟内未付定金) "),
    STATUS_5(5,"无司机，系统自动取消"),
    STATUS_6(6,"改派系统取消订单"),
    STATUS_7(7,"订单池取消订单(乘客点击)"),
    STATUS_8(8,"场站取消订单"),
    STATUS_9(9,"客服后台取消"),
    STATUS_10(10,"机构后台取消订单	"),
    STATUS_11(11,"拼车取消订单"),
    STATUS_12(12,"三方取消"),
    STATUS_13(13,"航班延误系统自动取消订单"),
    STATUS_14(14,"派单取消"),
    STATUS_15(15,"压测取消"),
    STATUS_16(16,"顺风车下单异常取消	"),
    STATUS_17(17,"顺风车支付ID与订单号绑定异常取消	"),
    STATUS_18(18,"系统自动取消(周边游景点/线路乘客15分钟内未支付)	"),
    STATUS_19(19,"系统自动取消（顺风车到约定时间未绑单，自动取消）	"),
    STATUS_20(20,"系统自动取消（顺风车支付后置，约定时间内未支付，自动取消）	"),
    STATUS_21(21,"司机端发起取消	"),
    STATUS_22(22,"风控取消"),
    STATUS_23(23,"航班取消，返航，备降 取消订单（由订单监听开放平台mq触发）"),
    STATUS_24(24,"saas后台取消");

    public int value;
    public String i18n;

    CancelStatusEnum(int value, String i18n) {
        this.value = value;
        this.i18n = i18n;
    }

    public String getI18n() {
        return this.i18n;
    }
    public int getValue() {
        return this.value;
    }

    public String getValueStr() {
        return String.valueOf(this.value);
    }
    // 普通方法
    public static String getDis(int value) {
        for (CancelStatusEnum status : CancelStatusEnum.values()) {
            if (status.getValue() == value) {
                return status.getI18n();
            }
        }
        return null;
    }

    public static int getValueByI18n(String i18n){
        for (CancelStatusEnum status : CancelStatusEnum.values()) {
            if (status.getI18n().equals(i18n)) {
                return status.getValue();
            }
        }
        return 0;
    }
}
