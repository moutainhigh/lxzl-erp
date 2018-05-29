package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetailOperationLogQueryParam extends BasePageParam {
	@NotNull(message = ErrorCode.BANK_SLIP_DETAIL_ID_NULL,groups = {QueryGroup.class})
	private Integer bankSlipDetailId;   //银行对公流水明细ID

	public Integer getBankSlipDetailId() {
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId) {
		this.bankSlipDetailId = bankSlipDetailId;
	}
}