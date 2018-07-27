package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsDataWeekly extends BasePO {

	private Integer financeStatisticsDataWeeklyId;   //唯一标识ID
	private Integer orderOrigin;   //订单来源：1-KA  2-电销  3-大客户渠道
	private Integer rentLengthType;   //租赁类型：1-短租 2-长租 3-短短租
	private Integer subCompanyId;   //分公司ID
	private Integer customerDealsCount;   //客户成交数量
	private Integer newCustomerDealsCount;   //新客户成交数量
	private Integer rentProductDealsCount;   //租赁商品成交数量
	private Integer returnProductDealsCount;   //退货商品成交数量
	private Integer increaseProductDealsCount;   //净增长商品成交数量
	private Integer year;   //年份
	private Integer month;   //月份
	private Integer weekOfMonth;   //当月第几周
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getFinanceStatisticsDataWeeklyId(){
		return financeStatisticsDataWeeklyId;
	}

	public void setFinanceStatisticsDataWeeklyId(Integer financeStatisticsDataWeeklyId){
		this.financeStatisticsDataWeeklyId = financeStatisticsDataWeeklyId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}