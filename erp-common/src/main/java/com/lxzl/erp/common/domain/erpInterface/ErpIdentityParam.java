package com.lxzl.erp.common.domain.erpInterface;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpIdentityParam implements Serializable {


	@NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL)
	private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
	@NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL)
	private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
	private String erpOperateUser;   //系统名称

	public String getErpAppId() {
		return erpAppId;
	}

	public void setErpAppId(String erpAppId) {
		this.erpAppId = erpAppId;
	}

	public String getErpAppSecret() {
		return erpAppSecret;
	}

	public void setErpAppSecret(String erpAppSecret) {
		this.erpAppSecret = erpAppSecret;
	}

	public String getErpOperateUser() {
		return erpOperateUser;
	}

	public void setErpOperateUser(String erpOperateUser) {
		this.erpOperateUser = erpOperateUser;
	}
}