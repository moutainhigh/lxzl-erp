package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetailOperationLog extends BasePO {

	private Integer bankSlipDetailOperationLogId;   //唯一标识
	private Integer bankSlipDetailId;   //银行对公流水明细ID
	private Integer operationType;   //1-下推，2-属地化，3-自动属地化，4-属地化，5-取消属地化，6-隐藏,7-取消隐藏，8-自动认领，9-认领，10-确认
	private String operationContent;   //操作内容
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getBankSlipDetailOperationLogId(){
		return bankSlipDetailOperationLogId;
	}

	public void setBankSlipDetailOperationLogId(Integer bankSlipDetailOperationLogId){
		this.bankSlipDetailOperationLogId = bankSlipDetailOperationLogId;
	}

	public Integer getBankSlipDetailId(){
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId){
		this.bankSlipDetailId = bankSlipDetailId;
	}

	public Integer getOperationType(){
		return operationType;
	}

	public void setOperationType(Integer operationType){
		this.operationType = operationType;
	}

	public String getOperationContent(){
		return operationContent;
	}

	public void setOperationContent(String operationContent){
		this.operationContent = operationContent;
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

}