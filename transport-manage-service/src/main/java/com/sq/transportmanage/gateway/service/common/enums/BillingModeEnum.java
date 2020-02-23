package com.sq.transportmanage.gateway.service.common.enums;

public enum BillingModeEnum {

    ACTUAL((byte)0,"实时计价"),
    BUYOUT((byte)1,"一口价");

    private byte billingMode;
    private String billingModeName;

    BillingModeEnum(byte billingMode,String billingModeName) {
        this.billingMode = billingMode;
        this.billingModeName = billingModeName;
    }

    public byte getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(byte billingMode) {
        this.billingMode = billingMode;
    }

    public String getBillingModeName() {
        return billingModeName;
    }

    public void setBillingModeName(String billingModeName) {
        this.billingModeName = billingModeName;
    }

    public static Byte getBillingMode(String billingModeName) {
        for (BillingModeEnum o : BillingModeEnum.values()) {
            if (o.getBillingModeName().equals(billingModeName.trim())) {
                return o.getBillingMode();
            }
        }
        return null;
    }

    public static String getBillingModeName(byte billingMode) {
        for (BillingModeEnum o : BillingModeEnum.values()) {
            if (o.getBillingMode()==billingMode) {
                return o.getBillingModeName();
            }
        }
        return null;
    }

}
