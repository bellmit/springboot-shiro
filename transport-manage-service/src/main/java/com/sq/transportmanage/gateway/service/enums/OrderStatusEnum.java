package com.sq.transportmanage.gateway.service.enums;

/**
 * @Description:订单详细状态信息
 */
public enum OrderStatusEnum {

    ORDER_YUDINGZHONG(10, "预定中/待支付"),
    ORDER_YISHOULI(13, "已受理"),
    ORDER_DAIFUKUAN(14, "待付款"),
    ORDER_DAIFUWU(15, "待服务"),
    ORDER_YICHUFA(20, "已出发"),
    ORDER_YIDAODA(25, "已到达"),
    ORDER_FUWUZHONG(30, "服务中"),
    ORDER_FUWUJIESHU(35, "服务结束"),
    ORDER_DAIJIESUAN(40, "待结算"),
    ORDER_ZHIFUZHONG(42,"支付中"),
    ORDER_KOUKUANZHONG(43,"扣款中"),
    ORDER_DAIZHIFU(44,"待支付"),
    ORDER_YIJIESUAN(45, "已结算"),
    ORDER_YIWANCHENG(50, "已完成"),
    ORDER_YIYI(55, "订单异议"),
    ORDER_WEIFUKUAN(58, "未付款"),
    ORDER_YIQUXIAO(60, "已取消");

    public int value;
    public String i18n;

    OrderStatusEnum(int value, String i18n) {
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
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getValue() == value) {
                return status.getI18n();
            }
        }
        return null;
    }

    public static int getValueByI18n(String i18n){
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getI18n().equals(i18n)) {
                return status.getValue();
            }
        }
        return 0;
    }
}
