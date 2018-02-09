package com.lxzl.erp.common.domain.getIpAndMac;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主机A向主机B发送“UDP－NetBIOS－NS”询问包，即向主机B的137端口，发Query包来询问主机B的NetBIOS Names信息。
 * 其次，主机B接收到“UDP－NetBIOS－NS”询问包，假设主机B正确安装了NetBIOS服务........... 而且137端口开放，则主机B会向主机A发送一个“UDP－NetBIOS－NS”应答包，即发Answer包给主机A。
 * 并利用UDP(NetBIOS Name Service)来快速获取远程主机MAC地址的方法
 *
 */
public class UdpGetClientMacAddr {
    /**
     * Gets the client ip addr.
     *
     * @param request the request
     * @return the client ip addr
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Gets the real ips.
     *
     * @return the real ips
     */
    public static List<String> getRealIps() {
        List<String> ips = new ArrayList<String>();
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    ips.add(netip);
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                    ips.add(localip);
                }
            }
        }
        return ips;
    }
}
