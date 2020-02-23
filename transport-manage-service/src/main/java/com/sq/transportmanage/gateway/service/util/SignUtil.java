package com.sq.transportmanage.gateway.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 签名工具（派单规则）
 *
 */
public final class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * @param param
     * @param key
     * @return
     */
    public static final String doSign(final Map<String, Object> param, final String key) {
        Map<String, Object> dict = new TreeMap<>(param);
        dict.remove("sign");
        StringBuilder builder = new StringBuilder(128);
        dict.forEach((k, v) -> {
            if (StringUtils.isBlank(k)) {
                return;
            }
            if (v == null) {
                return;
            }
            if ((v instanceof String) && StringUtils.isBlank(v.toString())) {
                return;
            }

            builder.append(k).append("=").append(v).append("&");
        });

        builder.append("key=").append(key);
        String sign = DigestUtils.md5Hex(builder.toString());
        return sign;
    }

    /**
     * get sign for paying, using UTF-8 as default charset(32位签名)
     *
     * @param param
     * @param appKey
     * @return String
     */
    public static String sign(Map<String, ?> param, String appKey) {
        return sign(param, appKey, "UTF-8", null);
    }

    /**
     * @param param      参数
     * @param appKey     key值
     * @param appKeyName key名称
     * @return
     */
    public static String sign(Map<String, ?> param, String appKey, String appKeyName) {
        return sign(param, appKey, "UTF-8", appKeyName);
    }

    /**
     * base64签名
     *
     * @param param
     * @param appKey
     * @return
     */
    public static String signBase64(Map<String, ?> param, String appKey) {
        return generateSign(param, appKey, "UTF-8", true, null);
    }

    /**
     * get sign for paying
     *
     * @param param
     * @param appKey
     * @param md5Charset
     * @return String
     */
    public static String sign(Map<String, ?> param, String appKey, String md5Charset, String appKeyName) {

        return generateSign(param, appKey, md5Charset, false, appKeyName);
    }

    /**
     * 生成
     *
     * @param param
     * @param appKey
     * @param md5Charset
     * @param base64
     * @return String
     */
    private static String generateSign(Map<String, ?> param, String appKey, String md5Charset, boolean base64, String appKeyName) {
        if (CollectionUtils.isEmpty(param)) {
            throw new IllegalArgumentException("param mustn't be null");
        }

        StringBuilder sbuilder = new StringBuilder();

        Map<String, Object> dictMap = new TreeMap<>(param);
        for (Map.Entry<String, Object> entry : dictMap.entrySet()) {
            if (entry == null || StringUtils.isEmpty(entry.getKey()) || entry.getValue() == null || StringUtils.isBlank(entry.getValue().toString())) {
                logger.warn("entry is null, entry=[" + entry + "]");
                continue;
            } else if ("sign".equals(entry.getKey())) {
                continue;
            }
            sbuilder = sbuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (null == appKeyName) {
            sbuilder = sbuilder.append("key=").append(appKey);
        } else {
            sbuilder = sbuilder.append(appKeyName).append("=").append(appKey);
        }
        String sign;
        try {
            if (base64) {
                sign = MD5Utils.getMD5DigestBase64(sbuilder.toString());
            } else {
                sign = MD5Utils.getMD5DigestHex(sbuilder.toString());
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("generate sign error, caused by " + e.getMessage(), e);
            return null;
        }
        return sign;

    }

    public static String createMD5Sign(Map<String, Object> paramMap, String signKey) {
        List<String> sortedKeys = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }

            sortedKeys.add(entry.getKey());
        }

        if (sortedKeys.size() == 0) {
            // 没有参数
            return "";
        }

        Collections.sort(sortedKeys);

        StringBuffer buff = new StringBuffer("");
        for (String key : sortedKeys) {
            Object val = paramMap.get(key);
            if (val == null || StringUtils.isBlank(val.toString())) {
                continue;
            }
            buff.append(key).append("=").append(val).append("&");
        }
        buff.append("key=").append(signKey);
        try {
            return MD5Utils.getMD5DigestBase64(buff.toString());
        } catch (Exception e) {
            throw new RuntimeException("签名错误");
        }
    }

}