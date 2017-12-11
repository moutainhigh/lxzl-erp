package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrder implements Serializable {

	private Integer statementOrderId;   //唯一标识
	private String statementOrderNo;   //结算单编码
	private Integer customerId;   //客户ID
	private Date statementExpectPayTime; // 结算单预计支付时间
	private Integer statementMonth;   //结算月份
	private BigDecimal statementAmount;   //结算单金额，结算单明细总和
	private Integer statementStatus;   //结算状态，0未结算，1已结算
	private Date statementStartTime;   //对账开始时间，结算单明细最早的一个
	private Date statementEndTime;   //对账结束时间，结算单明细最晚的一个
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<StatementOrderDetail> statementOrderDetailList;


	public Integer getStatementOrderId(){
		return statementOrderId;
	}

	public void setStatementOrderId(Integer statementOrderId){
		this.statementOrderId = statementOrderId;
	}

	public String getStatementOrderNo(){
		return statementOrderNo;
	}

	public void setStatementOrderNo(String statementOrderNo){
		this.statementOrderNo = statementOrderNo;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public BigDecimal getStatementAmount(){
		return statementAmount;
	}

	public void setStatementAmount(BigDecimal statementAmount){
		this.statementAmount = statementAmount;
	}

	public Integer getStatementStatus(){
		return statementStatus;
	}

	public void setStatementStatus(Integer statementStatus){
		this.statementStatus = statementStatus;
	}

	public Date getStatementStartTime(){
		return statementStartTime;
	}

	public void setStatementStartTime(Date statementStartTime){
		this.statementStartTime = statementStartTime;
	}

	public Date getStatementEndTime(){
		return statementEndTime;
	}

	public void setStatementEndTime(Date statementEndTime){
		this.statementEndTime = statementEndTime;
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

	public List<StatementOrderDetail> getStatementOrderDetailList() {
		return statementOrderDetailList;
	}

	public void setStatementOrderDetailList(List<StatementOrderDetail> statementOrderDetailList) {
		this.statementOrderDetailList = statementOrderDetailList;
	}

	public Integer getStatementMonth() {
		return statementMonth;
	}

	public void setStatementMonth(Integer statementMonth) {
		this.statementMonth = statementMonth;
	}

	public Date getStatementExpectPayTime() {
		return statementExpectPayTime;
	}

	public void setStatementExpectPayTime(Date statementExpectPayTime) {
		this.statementExpectPayTime = statementExpectPayTime;
	}
}