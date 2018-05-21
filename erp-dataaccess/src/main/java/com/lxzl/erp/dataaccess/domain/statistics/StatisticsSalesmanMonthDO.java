package com.lxzl.erp.dataaccess.domain.statistics;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatisticsSalesmanMonthDO  extends BaseDO {

	private Integer id;
	private Integer salesmanId;
	private String salesmanName;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer rentLengthType;
	private Integer dealsCount;
	private Integer dealsProductCount;
	private BigDecimal dealsAmount;
	private BigDecimal awaitReceivable;
	private BigDecimal income;
	private BigDecimal receive;
	private BigDecimal pureIncrease;
	private Date confirmTime;
	private String confirmUser;
	private Integer confirmStatus;
	private String refuseReason;
	private Date month;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getSalesmanId(){
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId){
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName(){
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName){
		this.salesmanName = salesmanName;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public String getSubCompanyName(){
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName){
		this.subCompanyName = subCompanyName;
	}

	public Integer getRentLengthType(){
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType){
		this.rentLengthType = rentLengthType;
	}

	public Integer getDealsCount(){
		return dealsCount;
	}

	public void setDealsCount(Integer dealsCount){
		this.dealsCount = dealsCount;
	}

	public Integer getDealsProductCount(){
		return dealsProductCount;
	}

	public void setDealsProductCount(Integer dealsProductCount){
		this.dealsProductCount = dealsProductCount;
	}

	public BigDecimal getDealsAmount(){
		return dealsAmount;
	}

	public void setDealsAmount(BigDecimal dealsAmount){
		this.dealsAmount = dealsAmount;
	}

	public BigDecimal getAwaitReceivable(){
		return awaitReceivable;
	}

	public void setAwaitReceivable(BigDecimal awaitReceivable){
		this.awaitReceivable = awaitReceivable;
	}

	public BigDecimal getIncome(){
		return income;
	}

	public void setIncome(BigDecimal income){
		this.income = income;
	}

	public BigDecimal getReceive(){
		return receive;
	}

	public void setReceive(BigDecimal receive){
		this.receive = receive;
	}

	public BigDecimal getPureIncrease(){
		return pureIncrease;
	}

	public void setPureIncrease(BigDecimal pureIncrease){
		this.pureIncrease = pureIncrease;
	}

	public Date getConfirmTime(){
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime){
		this.confirmTime = confirmTime;
	}

	public String getConfirmUser(){
		return confirmUser;
	}

	public void setConfirmUser(String confirmUser){
		this.confirmUser = confirmUser;
	}

	public Integer getConfirmStatus(){
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus){
		this.confirmStatus = confirmStatus;
	}

	public String getRefuseReason(){
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason){
		this.refuseReason = refuseReason;
	}

	public Date getMonth(){
		return month;
	}

	public void setMonth(Date month){
		this.month = month;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

}