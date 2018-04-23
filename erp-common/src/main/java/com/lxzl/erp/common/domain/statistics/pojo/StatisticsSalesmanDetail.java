package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @Auther: huahongbin
 * @Date: 2018/4/23 13:53
 * @Description:
 */
public class StatisticsSalesmanDetail {
	private Integer id; // 唯一标识
	private Integer salesmanId; // 业务员id
	private String salesmanName; // 业务员姓名
	private Integer subCompanyId; // 分公司id
	private String subCompanyName; // 分公司名
	private Integer dealsCount; // 成交单数
	private Integer dealsProductCount; // 成交台数
	private BigDecimal dealsAmount; // 成交金额
	private BigDecimal awaitReceivable; // 待收
	private BigDecimal income; // 本期回款

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

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

	public Integer getDealsCount() {
		return dealsCount;
	}

	public void setDealsCount(Integer dealsCount) {
		this.dealsCount = dealsCount;
	}

	public Integer getDealsProductCount() {
		return dealsProductCount;
	}

	public void setDealsProductCount(Integer dealsProductCount) {
		this.dealsProductCount = dealsProductCount;
	}

	public BigDecimal getDealsAmount() {
		return dealsAmount;
	}

	public void setDealsAmount(BigDecimal dealsAmount) {
		this.dealsAmount = dealsAmount;
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
