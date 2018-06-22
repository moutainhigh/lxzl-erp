package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3StatementDateChange extends BasePO {

	private Integer k3StatementDateChangeId;   //唯一标识
	@NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL)
	private String orderNo;   //订单编号
	@NotNull(message = ErrorCode.STATEMENT_DATE_SPLIT_TIME_NOT_NULL)
	private Date statementDateChangeTime;   //结算类型修改时间
	@NotNull(message = ErrorCode.BEFORE_STATEMENT_DATE_NOT_NULL)
	private Integer beforeStatementDate;   //修改前结算时间（天），-1,20和31三种情况，如果为空取系统设定
	@NotNull(message = ErrorCode.AFTER_STATEMENT_DATE_NOT_NULL)
	private Integer afterStatementDate;   //修改后结算时间（天），-1,20和31三种情况，如果为空取系统设定
	@NotNull(message = ErrorCode.STATEMENT_DATE_CHANGE_TYPE_NOT_NULL)
	private Integer changeType;   //改变类型，0-当月，1-下月
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人



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

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}