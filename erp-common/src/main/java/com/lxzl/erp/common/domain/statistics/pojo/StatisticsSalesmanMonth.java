package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsSalesmanMonth extends BasePO {
	@NotNull(message = ErrorCode.ID_NOT_NULL , groups = {UpdateGroup.class, IdGroup.class})
	private Integer statisticsSalesmanMonthId;   //唯一标识
	private Integer salesmanId;   //业务员ID
	private String salesmanName;   //业务员姓名
	private Integer subCompanyId;   //所属分公司ID
	private String subCompanyName;   //所属分公司名称
	private Integer rentLengthType;   //租赁时长类型，1短租，2长租
	private Integer dealsCount;   //成交单数
	private Integer dealsProductCount;   //成交台数
	private BigDecimal dealsAmount;   //成交金额
	private BigDecimal awaitReceivable;   //待收金额
	private BigDecimal income;   //本期回款（已收）
	private BigDecimal receive;   //应收 = 待收 + 本期回款
	private BigDecimal pureIncrease;   //净增
	private Date confirmTime;   //确认时间
	private String confirmUser;   //确认人
	@NotNull(message = ErrorCode.CONFIRM_STATUS_NOT_NULL , groups = {UpdateGroup.class, IdGroup.class})
	@In(value = {0,1,2}, message = ErrorCode.CONFIRM_STATUS_ERROR)
	private Integer confirmStatus;   //确认状态，0-未确认，1-同意，2-拒绝
	private String refuseReason;   //拒绝原因
	private Date month;   //年月
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private String updateUser;   //修改人
	private Date updateTime;   //修改时间


	public Integer getStatisticsSalesmanMonthId(){
		return statisticsSalesmanMonthId;
	}

	public void setStatisticsSalesmanMonthId(Integer statisticsSalesmanMonthId){
		this.statisticsSalesmanMonthId = statisticsSalesmanMonthId;
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