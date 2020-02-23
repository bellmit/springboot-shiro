package com.sq.transportmanage.gateway.service.common.enums;

public enum FlightStateEnum {
    PLAN(1,"计划"),
    TAKE_OFF(2,"起飞"),
    ARRIVE(3,"到达"),
    INCUR_LOSS_THROUGH_DELAY(11,"延误"),
    CANCEL(12,"取消"),
    ALTERNATE_LANDING(13,"备降"),
    RETURN_TO_NAVIGATION(14,"返航"),
    EARLY_WARNING(22,"预警");
    private int flightState;
    private String flightStateName;

    FlightStateEnum(int flightState,String flightStateName) {
        this.flightState = flightState;
        this.flightStateName = flightStateName;
    }

    public int getFlightState() {
        return flightState;
    }

    public void setFlightState(int flightState) {
        this.flightState = flightState;
    }

    public String getFlightStateName() {
        return flightStateName;
    }

    public void setFlightStateName(String flightStateName) {
        this.flightStateName = flightStateName;
    }

    public static Integer getFlightState(String flightStateName) {
        for (FlightStateEnum o : FlightStateEnum.values()) {
            if (o.getFlightStateName().equals(flightStateName.trim())) {
                return o.getFlightState();
            }
        }
        return 0;
    }
}
