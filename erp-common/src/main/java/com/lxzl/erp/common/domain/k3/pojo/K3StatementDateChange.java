package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3StatementDateChange extends BasePO {

	private Integer k3StatementDateChangeId;   //唯一标识
	private String orderNo;   //订单编号
	private Date statementDateChangeTime;   //结算类型修改时间
	private Integer beforeStatementDate;   //修改前结算时间（天），-1,20和31三种情况，如果为空取系统设定
	private Integer afterStatementDate;   //修改后结算时间（天），-1,20和31三种情况，如果为空取系统设定
	private Integer changeType;   //改变类型，0-当月，1-下月



	public Integer getK3StatementDateChangeId(){
		return k3StatementDateChangeId;
	}

	public void setK3StatementDateChangeId(Integer k3StatementDateChangeId){
		this.k3StatementDateChangeId = k3StatementDateChangeId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Date getStatementDateChangeTime(){
		return statementDateChangeTime;
	}

	public void setStatementDateChangeTime(Date statementDateChangeTime){
		this.statementDateChangeTime = statementDateChangeTime;
	}

	public Integer getBeforeStatementDate(){
		return beforeStatementDate;
	}

	public void setBeforeStatementDate(Integer beforeStatementDate){
		this.beforeStatementDate = beforeStatementDate;
	}

	public Integer getAfterStatementDate(){
		return afterStatementDate;
	}

	public void setAfterStatementDate(Integer afterStatementDate){
		this.afterStatementDate = afterStatementDate;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
}