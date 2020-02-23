package com.sq.transportmanage.gateway.service.common.dto.mpdriver;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {

    private int orderId;
    private String orderNo;
}
