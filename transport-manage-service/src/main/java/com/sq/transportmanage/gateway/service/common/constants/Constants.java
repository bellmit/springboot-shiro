package com.sq.transportmanage.gateway.service.common.constants;

/**
 * 系统常量
 *
 * @author answer
 * @Date: 2019/8/28 09:53
 */
public final class Constants {

    public static final Integer ERROR = 1;
    public static final Integer SUCCESS = 0;

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;

}
