package com.sq.transportmanage.gateway.api.web.interceptor;


import java.util.Map;

/**
 * @author (yangbo)
 * @Date: 2019/5/8 17:21
 */
public class CharacterFilter {

    public static String validScriptFilter(Map<String, String> param) {
        for(String key : param.keySet()) {
            if(param.get(key).toLowerCase().indexOf("<") != -1) {
                return key;
            }
            if(param.get(key).toLowerCase().indexOf(">") != -1){
                return key;
            }
            if(param.get(key).toUpperCase().indexOf("%3C") != -1){
                return key;
            }
            if(param.get(key).toUpperCase().indexOf("%3E") != -1){
                return key;
            }
            if(param.get(key).toLowerCase().indexOf("script") != -1){
                return key;
            }
            if(param.get(key).toLowerCase().indexOf("javascript:") != -1){
                return key;
            }
            if(param.get(key).toLowerCase().indexOf("alert") != -1){
                return key;
            }
            if(param.get(key).toLowerCase().indexOf("onerror") != -1){
                return key;
            }
        }
        return null;
    }
}
