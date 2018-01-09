package com.lxzl.erp.dataaccess.domain.supplier;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class SupplierDO  extends BaseDO {

	private Integer id;
	private String supplierNo;
	private String supplierName;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private String tel;
	private String contactName;
	private String contactPhone;
	private Integer dataStatus;
	private String remark;
	@Transient
	private String provinceName;
	@Transient
	private String cityName;
	@Transient
	private String districtName;

	private String beneficiaryName;   //收款户名
	private String beneficiaryAccount;   //收款帐号
	private String beneficiaryBankName;   //收款开户行
	private String supplierCode;	//供应商自定义编码

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getSupplierName(){
		return supplierName;
	}

	public void setSupplierName(String supplierName){
		this.supplierName = supplierName;
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

	public String getTel(){
		return tel;
	}

	public void setTel(String tel){
		this.tel = tel;
	}

	public String getContactName(){
		return contactName;
	}

	public void setContactName(String contactName){
		this.contactName = contactName;
	}

	public String getContactPhone(){
		return contactPhone;
	}

	public void setContactPhone(String contactPhone){
		this.contactPhone = contactPhone;
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

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getBeneficiaryName() { return beneficiaryName; }

	public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }

	public String getBeneficiaryAccount() { return beneficiaryAccount; }

	public void setBeneficiaryAccount(String beneficiaryAccount) { this.beneficiaryAccount = beneficiaryAccount; }

	public String getBeneficiaryBankName() { return beneficiaryBankName; }

	public void setBeneficiaryBankName(String beneficiaryBankName) { this.beneficiaryBankName = beneficiaryBankName; }

	public String getSupplierCode() { return supplierCode; }

	public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
}