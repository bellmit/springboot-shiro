package com.sq.transportmanage.gateway.service.common.enums;

public enum ServiceTypeEnum {
    APPOINTMENT(2,"预约"),
    PICKUP(3,"接机"),
    DELIVERY(5,"送机"),
    DAILY(6,"日租"),
    HALF_DAILY(7,"半日租"),
    AIRPORT_TRANSFER(8,"接送机");

    private int serviceTypeId;
    private String serviceTypeName;

    ServiceTypeEnum(int serviceTypeId,String serviceTypeName) {
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public static Integer getServiceTypeId(String serviceTypeName) {
        for (ServiceTypeEnum o : ServiceTypeEnum.values()) {
            if (o.getServiceTypeName().equals(serviceTypeName.trim())) {
                return o.getServiceTypeId();
            }
        }
        return null;
    }

    public static String getServiceTypeName(Integer serviceTypeId) {
        for (ServiceTypeEnum o : ServiceTypeEnum.values()) {
            if (o.getServiceTypeId()==serviceTypeId) {
                return o.getServiceTypeName();
            }
        }
        return null;
    }


}
