package com.sq.transportmanage.gateway.service.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DispatchRecordQueryBO {
    private Integer conferenceId;
    private List<String> passengerTypeList;
    private Integer driverId;
    private String startDate;
    private String endDate;
    private List<Integer> status;
}
