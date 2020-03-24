package com.sq.transportmanage.gateway.api.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zhuanche.http.MpOkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author fanht
 * @Description
 * @Date 2019/4/13 下午6:34
 * @Version 1.0
 */
@Component
public class DingdingAlarmUtil {

    private static Logger logger = LoggerFactory.getLogger(DingdingAlarmUtil.class);

    /**
     * 发送钉钉消息
     */
    public static void sendDingdingAlerm(String message, String dingding_token_url){


        Map<String,Object> map = Maps.newHashMap();
        map.put("msgtype","text");
        JSONObject content = new JSONObject();
        content.put("content",message);
        map.put("text",content);
        JSONObject isAtAll = new JSONObject();
        isAtAll.put("atMobiles",false);
        map.put("at",isAtAll);

        MpOkHttpUtil.okHttpPostAsync(dingding_token_url, map, 0, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("钉钉消息发送失败！失败信息:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("钉钉消息发送成功!" + response.toString());
            }
        });
    }
}
