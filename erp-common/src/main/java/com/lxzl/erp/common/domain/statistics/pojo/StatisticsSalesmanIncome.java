package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/3 17:32
 * @Description:
 */
public class StatisticsSalesmanIncome {
	private BigDecimal totalAwaitReceivable = BigDecimal.valueOf(0); // 总待收
	private BigDecimal totalReceive = BigDecimal.valueOf(0); // 总应收
	private BigDecimal totalIncome = BigDecimal.valueOf(0); // 本期回款（查询期间内的结算收入）

	public BigDecimal getTotalAwaitReceivable() {
		return totalAwaitReceivable;
	}

	public void setTotalAwaitReceivable(BigDecimal totalAwaitReceivable) {
		this.totalAwaitReceivable = totalAwaitReceivable;
	}

	public BigDecimal getTotalReceive() {
		return totalReceive;
	}

	public void setTotalReceive(BigDecimal totalReceive) {
		this.totalReceive = totalReceive;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
}
