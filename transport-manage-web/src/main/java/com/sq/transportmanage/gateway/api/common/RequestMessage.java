package com.sq.transportmanage.gateway.api.common;

import lombok.Data;

import java.util.Map;

@Data
public class RequestMessage {

    private String url;

    private Map<String, String> header;

    private String httpMethod;

    private String requestBody;


}
