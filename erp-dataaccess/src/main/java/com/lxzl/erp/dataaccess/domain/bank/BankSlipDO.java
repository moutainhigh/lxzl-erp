package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.Date;
import java.util.List;


public class BankSlipDO  extends BaseDO {

	private Integer id;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer bankType;
	private Date slipDay;
	private String accountNo;
	private Integer inCount;
	private Integer needClaimCount;
	private Integer claimCount;
	private Integer confirmCount;
	private Integer slipStatus;
	private String excelUrl;
	private Integer dataStatus;
	private String remark;
	private Integer localizationCount;  //属地化数量

	private List<BankSlipDetailDO> bankSlipDetailDOList;

	public Integer getLocalizationCount() {
		return localizationCount;
	}

	public void setLocalizationCount(Integer localizationCount) {
		this.localizationCount = localizationCount;
	}

	public List<BankSlipDetailDO> getBankSlipDetailDOList() {
		return bankSlipDetailDOList;
	}

	public void setBankSlipDetailDOList(List<BankSlipDetailDO> bankSlipDetailDOList) {
		this.bankSlipDetailDOList = bankSlipDetailDOList;
	}

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

	public Date getSlipDay(){
		return slipDay;
	}

	public void setSlipDay(Date slipDay){
		this.slipDay = slipDay;
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