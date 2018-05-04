package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/3 16:30
 * @Description:
 */
public class StatisticsSalesmanDetailIncome {
	private Integer salesmanId; // 业务员id
	private Integer subCompanyId; // 分公司id
	private Integer rentLengthType; // 长租短租
	private BigDecimal awaitReceivable; // 待收
	private BigDecimal income; // 本期回款（已收）

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Integer getSubCompanyId() {
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId) {
		this.subCompanyId = subCompanyId;
	}

	public Integer getRentLengthType() {
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType) {
		this.rentLengthType = rentLengthType;
	}

	public BigDecimal getAwaitReceivable() {
		return awaitReceivable;
	}

	public void setAwaitReceivable(BigDecimal awaitReceivable) {
		this.awaitReceivable = awaitReceivable;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}
}
