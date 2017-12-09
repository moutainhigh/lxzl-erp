package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

public class CustomerDO  extends BaseDO {

	private Integer id;
	private Integer customerType;
	private String customerNo;
	private String customerName;
	private Integer isDisabled;
	private Integer dataStatus;
	private String remark;

	@Transient
	private CustomerCompanyDO customerCompanyDO;
	@Transient
	private CustomerPersonDO customerPersonDO;

	private CustomerRiskManagementDO customerRiskManagementDO;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getCustomerType(){
		return customerType;
	}

	public void setCustomerType(Integer customerType){
		this.customerType = customerType;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getIsDisabled(){
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled){
		this.isDisabled = isDisabled;
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

	public CustomerCompanyDO getCustomerCompanyDO() {
		return customerCompanyDO;
	}

	public void setCustomerCompanyDO(CustomerCompanyDO customerCompanyDO) {
		this.customerCompanyDO = customerCompanyDO;
	}

	public CustomerPersonDO getCustomerPersonDO() {
		return customerPersonDO;
	}

	public void setCustomerPersonDO(CustomerPersonDO customerPersonDO) {
		this.customerPersonDO = customerPersonDO;
	}

	public CustomerRiskManagementDO getCustomerRiskManagementDO() {
		return customerRiskManagementDO;
	}

	public void setCustomerRiskManagementDO(CustomerRiskManagementDO customerRiskManagementDO) {
		this.customerRiskManagementDO = customerRiskManagementDO;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}