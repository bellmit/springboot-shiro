package com.sq.transportmanage.gateway.service.common.enums;

/**
 * @Description:订单状态信息
 */
public enum EnumStatus {

    //待下单
    ORDER_INIT(0,  "待下单"),
    //已下单
    ORDER_SUCCESS(1,"已下单"),
    //下单失败
    ORDER_FAIL(2,"下单失败"),
    //重新下单
    ORDER_REPALY(3,"重新下单");

    public int value;
    public String i18n;

    EnumStatus(int value, String i18n) {
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
        for (EnumStatus status : EnumStatus.values()) {
            if (status.getValue() == value) {
                return status.getI18n();
            }
        }
        return null;
    }

    public static int getValueByI18n(String i18n){
        for (EnumStatus status : EnumStatus.values()) {
            if (status.getI18n().equals(i18n)) {
                return status.getValue();
            }
        }
        return 0;
    }
}
