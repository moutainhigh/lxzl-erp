package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class BankSlipDO  extends BaseDO {

	private Integer id;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer bankType;
	private Date slipMonth;
	private String accountNo;
	private Integer inCount;
	private Integer needClaimCount;
	private Integer claimCount;
	private Integer confirmCount;
	private Integer slipStatus;
	private String excelUrl;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}