package com.lxzl.erp.dataaccess.domain.statistics;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.beans.BeanUtils;


public class FinanceStatisticsDataWeeklyDO extends BaseDO {

	private Integer id;
	private Integer orderOrigin; //订单来源：1-KA  2-电销  3-大客户渠道
	private Integer rentLengthType;  // 租赁类型：1-短租 2-长租 3-短短租
	private Integer subCompanyId; // 分公司ID
	private Integer customerDealsCount; // 客户成交数量
	private Integer newCustomerDealsCount; //新客户成交数量
	private Integer rentProductDealsCount; //租赁商品成交数量
	private Integer returnProductDealsCount; // 退货商品成交数量
	private Integer increaseProductDealsCount; // 净增长商品成交数量
	private Integer year; // 年份
	private Integer month; // 月份
	private Integer weekOfMonth;// 当月第几周
	private Integer dataStatus;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getOrderOrigin(){
		return orderOrigin;
	}

	public void setOrderOrigin(Integer orderOrigin){
		this.orderOrigin = orderOrigin;
	}

	public Integer getRentLengthType(){
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType){
		this.rentLengthType = rentLengthType;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public Integer getCustomerDealsCount(){
		return customerDealsCount;
	}

	public void setCustomerDealsCount(Integer customerDealsCount){
		this.customerDealsCount = customerDealsCount;
	}

	public Integer getNewCustomerDealsCount(){
		return newCustomerDealsCount;
	}

	public void setNewCustomerDealsCount(Integer newCustomerDealsCount){
		this.newCustomerDealsCount = newCustomerDealsCount;
	}

	public Integer getRentProductDealsCount(){
		return rentProductDealsCount;
	}

	public void setRentProductDealsCount(Integer rentProductDealsCount){
		this.rentProductDealsCount = rentProductDealsCount;
	}

	public Integer getReturnProductDealsCount(){
		return returnProductDealsCount;
	}

	public void setReturnProductDealsCount(Integer returnProductDealsCount){
		this.returnProductDealsCount = returnProductDealsCount;
	}

	public Integer getIncreaseProductDealsCount(){
		return increaseProductDealsCount;
	}

	public void setIncreaseProductDealsCount(Integer increaseProductDealsCount){
		this.increaseProductDealsCount = increaseProductDealsCount;
	}

	public Integer getYear(){
		return year;
	}

	public void setYear(Integer year){
		this.year = year;
	}

	public Integer getMonth(){
		return month;
	}

	public void setMonth(Integer month){
		this.month = month;
	}

	public Integer getWeekOfMonth(){
		return weekOfMonth;
	}

	public void setWeekOfMonth(Integer weekOfMonth){
		this.weekOfMonth = weekOfMonth;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}
	@Override
	public String toString() {
		return "FinanceStatisticsDataWeeklyDO{" +
				"orderOrigin=" + orderOrigin +
				", rentLengthType=" + rentLengthType +
				", subCompanyId=" + subCompanyId +
				", customerDealsCount=" + customerDealsCount +
				", newCustomerDealsCount=" + newCustomerDealsCount +
				", rentProductDealsCount=" + rentProductDealsCount +
				", returnProductDealsCount=" + returnProductDealsCount +
				", increaseProductDealsCount=" + increaseProductDealsCount +
				", year=" + year +
				", month=" + month +
				", weekOfMonth=" + weekOfMonth +
				'}';
	}

	public FinanceStatisticsDataWeeklyDO diff(FinanceStatisticsDataWeeklyDO lastFinanceWeekStatisticsDataWeekly) {
		if (lastFinanceWeekStatisticsDataWeekly == null) {
			return this;
		}
		FinanceStatisticsDataWeeklyDO diff = new FinanceStatisticsDataWeeklyDO();
		BeanUtils.copyProperties(this, diff);
		diff.setCustomerDealsCount(this.getCustomerDealsCount()-lastFinanceWeekStatisticsDataWeekly.getCustomerDealsCount());
		diff.setNewCustomerDealsCount(this.getNewCustomerDealsCount() - lastFinanceWeekStatisticsDataWeekly.getNewCustomerDealsCount());
		diff.setRentProductDealsCount(this.getRentProductDealsCount() - lastFinanceWeekStatisticsDataWeekly.getRentProductDealsCount());
		diff.setReturnProductDealsCount(this.getReturnProductDealsCount() - lastFinanceWeekStatisticsDataWeekly.getReturnProductDealsCount());
		diff.setIncreaseProductDealsCount(this.getIncreaseProductDealsCount() - lastFinanceWeekStatisticsDataWeekly.getIncreaseProductDealsCount());
		return diff;
	}
}