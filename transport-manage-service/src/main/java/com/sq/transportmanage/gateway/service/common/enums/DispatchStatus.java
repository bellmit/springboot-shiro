package com.sq.transportmanage.gateway.service.common.enums;

public enum DispatchStatus {

    NOT_ARRANGE(10, "未安排"),
    ARRANGED(13, "已安排"),
    DISPATCHING(14, "派单中"),
    DISPATCHED(15, "已派单"),
    SERVICING(20, "服务中"),
    DONE(50, "已完成"),
    CANCELED(60, "已删除");


    private int value;
    private String name;

    DispatchStatus(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getName(Integer status){
        switch (status){
            case 10:
                return NOT_ARRANGE.getName();
            case 13:
                return ARRANGED.getName();
            case 14:
                return DISPATCHING.getName();
            case 15:
                return DISPATCHED.getName();
            case 20:
                return SERVICING.getName();
            case 50:
                return DONE.getName();
            case 60:
                return CANCELED.getName();
        }
        return null;
    }
}
