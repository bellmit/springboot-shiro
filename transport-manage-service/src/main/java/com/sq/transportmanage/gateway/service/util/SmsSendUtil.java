package com.sq.transportmanage.gateway.service.util;

import com.zhuanche.http.MpOkHttpUtil;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 发送短信(SMS)
 */
@SuppressWarnings("deprecation")
@Configuration
public  class SmsSendUtil {
	private static final  Logger        log  = LoggerFactory.getLogger(SmsSendUtil.class);
	private static OkHttpClient httpclient = null;
	private static Properties            properties; //短信URL等的配置

	public static String url;

	public static String appkey;

	public static String appsecret;

	@Value("${short_message_url}")
	public  void setUrl(String url) {
		SmsSendUtil.url = url;
	}

	@Value("${short_message_appkey}")
	public  void setAppkey(String appkey) {
		SmsSendUtil.appkey = appkey;
	}

	@Value("${short_message_appsecret}")
	public  void setAppsecret(String appsecret) {
		SmsSendUtil.appsecret = appsecret;
	}











	static{
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override  
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {  
                    }  
                    @Override  
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {  
                    }  
                    @Override  
                    public X509Certificate[] getAcceptedIssuers() {  
                        return new X509Certificate[]{};
                    }  
                }  
        }; 
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {  
            @Override  
            public boolean verify(String s, SSLSession sslSession) {
                return true;  
            }  
        };
        try{
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sSLSocketFactory = sslContext.getSocketFactory();
    		httpclient = new OkHttpClient.Builder().
		      connectionPool(new ConnectionPool(100, 3, TimeUnit.MINUTES))
		      .connectTimeout(30, TimeUnit.SECONDS)
		      .readTimeout(30, TimeUnit.SECONDS)
		      .sslSocketFactory(sSLSocketFactory)
		      .hostnameVerifier(hostnameVerifier)
		      .build();
        }catch(Exception ex ) {
        	log.error("短信发送工具类初始化异常！", ex);
			System.err.println("短信发送工具类初始化异常！");
			ex.printStackTrace();
			System.exit(-1);
        }
	}
	/**初始化配置    文件位置configLocation **/
	public static synchronized void init( String configLocation) {
		try{
			log.info("***************initializing SmsSendUtil["+configLocation+"]");
			properties = PropertiesLoaderUtils.loadAllProperties(configLocation);
			log.info( properties.toString()  );
			log.info("***************initialized SmsSendUtil["+configLocation+"]\n");
		} catch (Exception e) {
        	log.error("初始化SmsSendUtil发生异常！配置文件："+ configLocation, e);
			System.err.println("初始化SmsSendUtil发生异常！配置文件："+ configLocation );
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	
  /**发送短信**/
  public static void send( String mobile, String content ){
	  try{
		    log.info("发送短信="+mobile+"，内容=" + content );
		    String timestamp = String.valueOf(System.currentTimeMillis());
		    String signContent = "appkey=" + appkey + "&content=" + content + "&mobile=" + mobile + "&timestamp=" + timestamp + "&appsecret=" + appsecret;
		    String sign = DigestUtils.md5Hex(signContent).toUpperCase();
		    FormBody body = new FormBody.Builder().add("appkey", appkey).add("content", content).add("mobile", mobile).add("timestamp", timestamp).add("sign", sign).build();
		    Request request = new Request.Builder().url(url).post(body).build();
		    Response response = httpclient.newCall(request).execute();
		    ResponseBody responseBody = response.body();
			String responseBodyMsg = "";
		    if (responseBody != null) {
		      responseBodyMsg = responseBody.string();
		    }
		    log.info("发送短信="+mobile+"，响应="+responseBodyMsg);
	  }catch(Exception ex) {
		    log.info("发送短信="+mobile+"，异常！", ex );
	  }
  }

  public static void sendTemplate(String mobile,Integer templateId,List param){
	  String url            = properties.getProperty("template_short_message_url");
	  String appkey     = properties.getProperty("short_message_appkey");
	  String appsecret = properties.getProperty("short_message_appsecret");
	  try {
		  Long timestamp = System.currentTimeMillis();
		  String sha1 = "appkey=" + appkey + "&mobile=" + mobile + "&params=" + param + "&templetId=" + templateId
                  + "&timestamp=" + timestamp + "&appsecret=" + appsecret;
		  String md5Hex = DigestUtils.md5Hex(sha1).toUpperCase();
		  Map<String,Object> maps = new HashMap();
		  maps.put("templetId",templateId);
		  maps.put("appkey",appkey);
		  maps.put("params",param);
		  maps.put("mobile",mobile);
		  maps.put("timestamp",timestamp);
		  maps.put("sign",md5Hex);
		  String responseBody = MpOkHttpUtil.okHttpPost(url,maps,0,mobile);
		  log.info("发送短信="+mobile+", 响应："+responseBody);
	  } catch (Exception e) {
		  log.error("发送短信="+mobile+", 异常：{}",e);
	  }

  }
}