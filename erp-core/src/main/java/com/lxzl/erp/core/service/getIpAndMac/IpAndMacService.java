package com.lxzl.erp.core.service.getIpAndMac;

import com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/19
 * @Time : Created in 19:06
 */
public interface IpAndMacService {
    /**
    * 获取ip地址和mac地址
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/19 19:10
    * @param : request
    * @Return : com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac
    */
    IpAndMac getIpAndMacService(HttpServletRequest request) throws Exception;
}
