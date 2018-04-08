package com.lxzl.erp.core.service.interfaceSwitch.impl.support;

import com.lxzl.se.common.util.StringUtil;
import org.springframework.stereotype.Component;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 22:42
 */
@Component
public class SwitchSupport {

    public String formatSwitch(String interfaceUrl) {

        if(StringUtil.isEmpty(interfaceUrl)){
            return "";
        }
        interfaceUrl = interfaceUrl.trim();
        if ('/' != interfaceUrl.charAt(0)) {
            interfaceUrl = "/" + interfaceUrl;
        }
        if ('/' == interfaceUrl.charAt(interfaceUrl.length() - 1)) {
            interfaceUrl = interfaceUrl.substring(0, interfaceUrl.length() - 1);
        }
        return interfaceUrl;
    }


}
