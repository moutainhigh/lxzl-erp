package com.lxzl.erp.core.service.getIpAndMac.impl;

import com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac;
import com.lxzl.erp.core.service.getIpAndMac.IpAndMacService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/19
 * @Time : Created in 19:11
 */
@Service
public class IpAndMacServiceImpl implements IpAndMacService {

    /**
    * 获取ip地址和mac地址
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/19 19:11
    * @param : request
    * @Return : com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac
    */
    @Override
    public IpAndMac getIpAndMacService(HttpServletRequest request) {
        IpAndMac ipAndMac = new IpAndMac();
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
        ip = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        String mac = "";

        if (!"127.0.0.1".equals(ip)) //本机过滤掉{
        {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("nbtstat -a " + ip);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    if (line.indexOf("MAC") > 0) {
                        mac = line.substring(line.indexOf("-") - 2);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ipAndMac.setIp(ip);
        ipAndMac.setMac(mac);
        return ipAndMac;
    }
}
