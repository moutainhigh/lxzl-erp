package com.lxzl.erp.common.domain.erpInterface.subCompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;

/**
 * @Author : kai
 * @Date : Created in 2018/1/30
 * @Time : Created in 21:07
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceSubCompanyQueryParam extends BasePageParam {

    private Integer subCompanyId;
    private String subCompanyName;
    private Integer subCompanyType;

    @NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL)
    private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
    @NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL)
    private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
    private String erpOperateUser;   //系统名称

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getSubCompanyType() {
        return subCompanyType;
    }

    public void setSubCompanyType(Integer subCompanyType) {
        this.subCompanyType = subCompanyType;
    }

    public String getErpAppId() { return erpAppId; }

    public void setErpAppId(String erpAppId) { this.erpAppId = erpAppId; }

    public String getErpAppSecret() { return erpAppSecret; }

    public void setErpAppSecret(String erpAppSecret) { this.erpAppSecret = erpAppSecret; }

    public String getErpOperateUser() { return erpOperateUser; }

    public void setErpOperateUser(String erpOperateUser) { this.erpOperateUser = erpOperateUser; }
}
