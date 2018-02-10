package com.lxzl.erp.common.domain.statementOrderCorrect.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderCorrectDetail extends BasePO {

	private Integer statementOrderCorrectDetailId;   //唯一标识
	private Integer statementOrderCorrectId;   //冲正单ID
	private Integer statementOrderDetailId;   //结算单订单项ID
	private Integer statementOrderDetailType;   //冲正单冲正类型
	private BigDecimal statementCorrectAmount;   //冲正金额
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getStatementOrderCorrectDetailId(){
		return statementOrderCorrectDetailId;
	}

	public void setStatementOrderCorrectDetailId(Integer statementOrderCorrectDetailId){
		this.statementOrderCorrectDetailId = statementOrderCorrectDetailId;
	}

	public Integer getStatementOrderCorrectId(){
		return statementOrderCorrectId;
	}

	public void setStatementOrderCorrectId(Integer statementOrderCorrectId){
		this.statementOrderCorrectId = statementOrderCorrectId;
	}

	public Integer getStatementOrderDetailId(){
		return statementOrderDetailId;
	}

	public void setStatementOrderDetailId(Integer statementOrderDetailId){
		this.statementOrderDetailId = statementOrderDetailId;
	}

	public Integer getStatementOrderDetailType() {
		return statementOrderDetailType;
	}

	public void setStatementOrderDetailType(Integer statementOrderDetailType) {
		this.statementOrderDetailType = statementOrderDetailType;
	}

	public BigDecimal getStatementCorrectAmount(){
		return statementCorrectAmount;
	}

	public void setStatementCorrectAmount(BigDecimal statementCorrectAmount){
		this.statementCorrectAmount = statementCorrectAmount;
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