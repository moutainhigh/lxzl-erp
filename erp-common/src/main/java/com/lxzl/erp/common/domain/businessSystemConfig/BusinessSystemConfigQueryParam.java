package com.lxzl.erp.common.domain.businessSystemConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 15:21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessSystemConfigQueryParam   extends BasePageParam {
    private String businessSystemName;   //系统名称
    private String businessAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
    private String businessAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统

    public String getBusinessSystemName() {
        return businessSystemName;
    }

    public void setBusinessSystemName(String businessSystemName) {
        this.businessSystemName = businessSystemName;
    }

    public String getBusinessAppId() {
        return businessAppId;
    }

    public void setBusinessAppId(String businessAppId) {
        this.businessAppId = businessAppId;
    }

    public String getBusinessAppSecret() {
        return businessAppSecret;
    }

    public void setBusinessAppSecret(String businessAppSecret) {
        this.businessAppSecret = businessAppSecret;
    }
}
