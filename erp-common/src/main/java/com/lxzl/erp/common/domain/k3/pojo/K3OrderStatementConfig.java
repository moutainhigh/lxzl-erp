package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3OrderStatementConfig extends BasePO {

	private Integer k3OrderStatementConfigId;   //唯一标识
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Date rentStartTime;   //起租时间


	public Integer getK3OrderStatementConfigId(){
		return k3OrderStatementConfigId;
	}

	public void setK3OrderStatementConfigId(Integer k3OrderStatementConfigId){
		this.k3OrderStatementConfigId = k3OrderStatementConfigId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
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

}