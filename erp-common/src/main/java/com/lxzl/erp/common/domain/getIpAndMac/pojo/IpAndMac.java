package com.lxzl.erp.common.domain.getIpAndMac.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/19
 * @Time : Created in 19:08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpAndMac  extends BasePO{

    private String ip;
    private String mac;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
