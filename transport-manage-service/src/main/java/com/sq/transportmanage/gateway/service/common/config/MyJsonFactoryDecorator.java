package com.sq.transportmanage.gateway.service.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import net.logstash.logback.decorate.JsonFactoryDecorator;

/**
 * 禁用对非ascii码进行escape编码的特性(打印日志使用)
 */
public class MyJsonFactoryDecorator implements JsonFactoryDecorator {
    @Override
    public MappingJsonFactory decorate(MappingJsonFactory factory) {
        factory.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        return factory;
    }
}