package com.lxzl.erp.core.service.interfaceSwitch.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import org.springframework.stereotype.Component;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 22:42
 */
@Component
public class SwitchSupport {

    public String verifyInterfaceUrl(String interfaceUrl){
        if(!"/".equals(interfaceUrl.substring(CommonConstant.COMMON_ZERO, CommonConstant.COMMON_ONE))){
            interfaceUrl = "/"+interfaceUrl;
        }

        if("/".equals(interfaceUrl.substring(interfaceUrl.length() - 1, interfaceUrl.length()))){
            interfaceUrl = interfaceUrl.substring(CommonConstant.COMMON_ZERO, interfaceUrl.length() - 1);
        }
        return interfaceUrl;
    }


}
