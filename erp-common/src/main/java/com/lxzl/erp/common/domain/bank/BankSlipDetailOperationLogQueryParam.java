package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetailOperationLogQueryParam extends BasePageParam {

	private Integer bankSlipDetailId;   //银行对公流水明细ID

	public Integer getBankSlipDetailId() {
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId) {
		this.bankSlipDetailId = bankSlipDetailId;
	}
}