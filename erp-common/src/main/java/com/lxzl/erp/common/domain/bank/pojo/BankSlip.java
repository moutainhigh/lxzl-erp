package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlip extends BasePO {

	private Integer bankSlipId;   //唯一标识
	private Integer subCompanyId;   //分公司ID
	private String subCompanyName;   //分公司名称
	private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
	private Date slipMonth;   //月份
	private String accountNo;   //查询账号
	private Integer inCount;   //进款笔数
	private Integer needClaimCount;   //需认领笔数
	private Integer claimCount;   //已认领笔数
	private Integer confirmCount;   //已确认笔数
	private Integer slipStatus;   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
	private String excelUrl;   //表格URL
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getBankSlipId(){
		return bankSlipId;
	}

	public void setBankSlipId(Integer bankSlipId){
		this.bankSlipId = bankSlipId;
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

	public Integer getBankType(){
		return bankType;
	}

	public void setBankType(Integer bankType){
		this.bankType = bankType;
	}

	public Date getSlipMonth(){
		return slipMonth;
	}

	public void setSlipMonth(Date slipMonth){
		this.slipMonth = slipMonth;
	}

	public String getAccountNo(){
		return accountNo;
	}

	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}

	public Integer getInCount(){
		return inCount;
	}

	public void setInCount(Integer inCount){
		this.inCount = inCount;
	}

	public Integer getNeedClaimCount(){
		return needClaimCount;
	}

	public void setNeedClaimCount(Integer needClaimCount){
		this.needClaimCount = needClaimCount;
	}

	public Integer getClaimCount(){
		return claimCount;
	}

	public void setClaimCount(Integer claimCount){
		this.claimCount = claimCount;
	}

	public Integer getConfirmCount(){
		return confirmCount;
	}

	public void setConfirmCount(Integer confirmCount){
		this.confirmCount = confirmCount;
	}

	public Integer getSlipStatus(){
		return slipStatus;
	}

	public void setSlipStatus(Integer slipStatus){
		this.slipStatus = slipStatus;
	}

	public String getExcelUrl(){
		return excelUrl;
	}

	public void setExcelUrl(String excelUrl){
		this.excelUrl = excelUrl;
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