package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.util.List;


public class ExchangeOrderDO  extends BaseDO {

	private Integer id;
	private String exchangeOrderNo;
	private String orderNo;
	private String newOrderNo;
	private Date rentStartTime;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;
	private Integer dataStatus;
	private Integer businessType;
	private Integer status;
	private String remark;

	private List<ExchangeOrderMaterialDO> exchangeOrderMaterialDOList;

	private List<ExchangeOrderProductDO> exchangeOrderProductDOList;

	public List<ExchangeOrderMaterialDO> getExchangeOrderMaterialDOList() {
		return exchangeOrderMaterialDOList;
	}

	public void setExchangeOrderMaterialDOList(List<ExchangeOrderMaterialDO> exchangeOrderMaterialDOList) {
		this.exchangeOrderMaterialDOList = exchangeOrderMaterialDOList;
	}

	public List<ExchangeOrderProductDO> getExchangeOrderProductDOList() {
		return exchangeOrderProductDOList;
	}

	public void setExchangeOrderProductDOList(List<ExchangeOrderProductDO> exchangeOrderProductDOList) {
		this.exchangeOrderProductDOList = exchangeOrderProductDOList;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getExchangeOrderNo(){
		return exchangeOrderNo;
	}

	public void setExchangeOrderNo(String exchangeOrderNo){
		this.exchangeOrderNo = exchangeOrderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Date getRentStartTime(){
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime){
		this.rentStartTime = rentStartTime;
	}

	public Integer getDepositCycle(){
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle){
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode(){
		return payMode;
	}

	public void setPayMode(Integer payMode){
		this.payMode = payMode;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Integer getBusinessType(){
		return businessType;
	}

	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getNewOrderNo() {
		return newOrderNo;
	}

	public void setNewOrderNo(String newOrderNo) {
		this.newOrderNo = newOrderNo;
	}
}