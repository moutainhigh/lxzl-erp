package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: huahongbin
 * @Date: 2018/4/23 13:45
 * @Description: 业务员提成统计数据
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsSalesman implements Serializable {
	private Integer totalCount = 0;	// 总条数
	private Integer totalDealsCount = 0; // 总成交单数
	private Integer totalDealsProductCount = 0; // 总成交台数
	private BigDecimal totalDealsAmount = BigDecimal.valueOf(0); // 总成交金额
	private BigDecimal totalAwaitReceivable = BigDecimal.valueOf(0); // 总待收
	private BigDecimal totalReceive = BigDecimal.valueOf(0); // 总应收
	private BigDecimal totalIncome = BigDecimal.valueOf(0); // 本期回款（查询期间内的结算收入）

	private Page<StatisticsSalesmanDetailTwo> statisticsSalesmanDetailPage; // 统计项分页

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalDealsCount() {
		return totalDealsCount;
	}

	public void setTotalDealsCount(Integer totalDealsCount) {
		this.totalDealsCount = totalDealsCount;
	}

	public Integer getTotalDealsProductCount() {
		return totalDealsProductCount;
	}

	public void setTotalDealsProductCount(Integer totalDealsProductCount) {
		this.totalDealsProductCount = totalDealsProductCount;
	}

	public BigDecimal getTotalAwaitReceivable() {
		return totalAwaitReceivable;
	}

	public void setTotalAwaitReceivable(BigDecimal totalAwaitReceivable) {
		this.totalAwaitReceivable = totalAwaitReceivable;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalDealsAmount() {
		return totalDealsAmount;
	}

	public void setTotalDealsAmount(BigDecimal totalDealsAmount) {
		this.totalDealsAmount = totalDealsAmount;
	}

	public BigDecimal getTotalReceive() {
		return totalReceive;
	}

	public void setTotalReceive(BigDecimal totalReceive) {
		this.totalReceive = totalReceive;
	}

	public Page<StatisticsSalesmanDetailTwo> getStatisticsSalesmanDetailPage() {
		return statisticsSalesmanDetailPage;
	}

	public void setStatisticsSalesmanDetailPage(Page<StatisticsSalesmanDetailTwo> statisticsSalesmanDetailPage) {
		this.statisticsSalesmanDetailPage = statisticsSalesmanDetailPage;
	}
}
