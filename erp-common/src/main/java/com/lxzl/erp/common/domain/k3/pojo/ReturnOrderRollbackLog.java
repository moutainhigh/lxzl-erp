package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderRollbackLog extends BasePO {

	private Integer returnOrderRollbackLogId;   //唯一标识
	private String returnOrderNo;   //退货单编号
	private Integer returnOrderId;   //退货单ID
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getReturnOrderRollbackLogId(){
		return returnOrderRollbackLogId;
	}

	public void setReturnOrderRollbackLogId(Integer returnOrderRollbackLogId){
		this.returnOrderRollbackLogId = returnOrderRollbackLogId;
	}

	public String getReturnOrderNo(){
		return returnOrderNo;
	}

	public void setReturnOrderNo(String returnOrderNo){
		this.returnOrderNo = returnOrderNo;
	}

	public Integer getReturnOrderId(){
		return returnOrderId;
	}

	public void setReturnOrderId(Integer returnOrderId){
		this.returnOrderId = returnOrderId;
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

}