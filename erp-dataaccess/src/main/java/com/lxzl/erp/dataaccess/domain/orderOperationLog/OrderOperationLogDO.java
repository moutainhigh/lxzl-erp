package com.lxzl.erp.dataaccess.domain.orderOperationLog;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class OrderOperationLogDO  extends BaseDO {

	private Integer id;
	private String orderNo;
	private Integer orderStatusBefore;
	private Integer orderStatusAfter;
	private String operationBefore;
	private String operationAfter;
	private Integer businessType;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getOrderStatusBefore(){
		return orderStatusBefore;
	}

	public void setOrderStatusBefore(Integer orderStatusBefore){
		this.orderStatusBefore = orderStatusBefore;
	}

	public Integer getOrderStatusAfter(){
		return orderStatusAfter;
	}

	public void setOrderStatusAfter(Integer orderStatusAfter){
		this.orderStatusAfter = orderStatusAfter;
	}

	public String getOperationBefore(){
		return operationBefore;
	}

	public void setOperationBefore(String operationBefore){
		this.operationBefore = operationBefore;
	}

	public String getOperationAfter(){
		return operationAfter;
	}

	public void setOperationAfter(String operationAfter){
		this.operationAfter = operationAfter;
	}

	public Integer getBusinessType(){
		return businessType;
	}

	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}

}