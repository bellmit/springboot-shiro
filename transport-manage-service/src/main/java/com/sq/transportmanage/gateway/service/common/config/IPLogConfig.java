package com.sq.transportmanage.gateway.service.common.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取本地ip
 */
public class IPLogConfig extends ClassicConverter {

    public static String localIp;

    private static final Logger logger = LoggerFactory.getLogger(IPLogConfig.class);

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        if (localIp == null){
            try {
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()){
                    NetworkInterface netInterface = allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()){
                        InetAddress ip = addresses.nextElement();
                        if (ip != null
                                && ip instanceof Inet4Address
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":")==-1){
                            localIp = ip.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                logger.error("IPLogConfig SocketException error {}",e);
            } catch (Exception e){
                logger.error("IPLogConfig Exception error {}",e);
            }
        }
        return localIp;
    }

}
