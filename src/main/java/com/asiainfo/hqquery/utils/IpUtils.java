package com.asiainfo.hqquery.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;

/**
 * @author king-pan
 * @date 2018/12/17
 * @Description ip工具类
 */
@Slf4j
public class IpUtils {


    private static final String UN_KNOWN = "unknown";

    private static final String LOCAL_IP = "127.0.0.1";

    private static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

    private static final String SPLIT = ".";

    private static final String COMMAND = "nbtstat -A ";

    private static final int NUMBER = 100;

    private static final String MAC_ADDRESS = "MAC Address";

    private IpUtils() {
    }

    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UN_KNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals(LOCAL_IP) || ipAddress.equals(LOCAL_IPV6)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(SPLIT) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获得MAC地址
     *
     * @param ip
     * @return 返回系统mac
     */
    public static String getMACAddress(String ip) {
        String str;
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec(COMMAND + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < NUMBER; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf(MAC_ADDRESS) > 1) {
                        macAddress = str.substring(str.indexOf(MAC_ADDRESS) + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return macAddress;
    }
}
