package com.sq.transportmanage.gateway.api.web.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author fanht
 * @Description  http重写为https
 * @Date 2020/3/11 下午8:22
 * @Version 1.0
 */
@Component
public class HttpTransWrapper extends HttpServletResponseWrapper{


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HttpServletRequest request;

     /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public HttpTransWrapper(final HttpServletRequest req, HttpServletResponse response) {
        super(response);
        this.request = req;
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        if(StringUtils.isEmpty(location)){
            super.sendRedirect(location);
            return;
        }

        try {
            final URI uri = new URI(location);
            if(uri.getScheme() != null){
                super.sendRedirect(location);
                return;
            }
        } catch (URISyntaxException e) {
            logger.error("=======跳转异常========" + e);
            super.sendRedirect(location);
        }

        String finalUrl = "https://" + this.request.getServerName();
        if(request.getServerPort() != 80 && request.getServerPort() != 443 ){
            finalUrl += ":" + request.getServerPort();
        }
        finalUrl += location;
        if(finalUrl.indexOf("localhost") > 0){
            //todo 如果是本地测试 仍然用http的
            super.sendRedirect(location);
        }else {
            super.sendRedirect(finalUrl);
        }
    }
}
