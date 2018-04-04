package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class CustomerConsignInfoDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private String consigneeName;
	private String consigneePhone;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private Integer isMain;
	private Integer dataStatus;
	private Integer verifyStatus;   //审核状态：0未提交；1.已提交 2.初审通过；3.终审通过 ；4.审批驳回
	private String remark;
	private Date lastUseTime;
	private Integer isBusinessAddress;	//是否为经营地址，0否1是
	private Integer workflowType;	//工作流类型

	@Transient
	private String provinceName;
	@Transient
	private String cityName;
	@Transient
	private String districtName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public String getConsigneeName(){
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName){
		this.consigneeName = consigneeName;
	}

	public String getConsigneePhone(){
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone){
		this.consigneePhone = consigneePhone;
	}

	public Integer getProvince(){
		return province;
	}

	public void setProvince(Integer province){
		this.province = province;
	}

	public Integer getCity(){
		return city;
	}

	public void setCity(Integer city){
		this.city = city;
	}

	public Integer getDistrict(){
		return district;
	}

	public void setDistrict(Integer district){
		this.district = district;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public Integer getIsMain(){
		return isMain;
	}

	public void setIsMain(Integer isMain){
		this.isMain = isMain;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Date getLastUseTime() {
		return lastUseTime;
	}

	public void setLastUseTime(Date lastUseTme) { this.lastUseTime = lastUseTme; }

	public Integer getIsBusinessAddress() { return isBusinessAddress; }

	public void setIsBusinessAddress(Integer isBusinessAddress) { this.isBusinessAddress = isBusinessAddress; }

	public Integer getVerifyStatus() { return verifyStatus; }

	public void setVerifyStatus(Integer verifyStatus) { this.verifyStatus = verifyStatus; }

	public Integer getWorkflowType() { return workflowType; }

	public void setWorkflowType(Integer workflowType) { this.workflowType = workflowType; }
}