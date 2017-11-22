package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class CustomerCompanyDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private String landline;
	private String connectRealName;
	private String connectPhone;
	private String companyName;
	private String companyAbb;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private String legalPerson;
	private String legalPersonNo;
	private String businessLicenseNo;
	private Integer dataStatus;
	private String remark;

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

	public String getLandline(){
		return landline;
	}

	public void setLandline(String landline){
		this.landline = landline;
	}

	public String getConnectRealName(){
		return connectRealName;
	}

	public void setConnectRealName(String connectRealName){
		this.connectRealName = connectRealName;
	}

	public String getConnectPhone(){
		return connectPhone;
	}

	public void setConnectPhone(String connectPhone){
		this.connectPhone = connectPhone;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyAbb(){
		return companyAbb;
	}

	public void setCompanyAbb(String companyAbb){
		this.companyAbb = companyAbb;
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

	public String getLegalPerson(){
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson){
		this.legalPerson = legalPerson;
	}

	public String getLegalPersonNo(){
		return legalPersonNo;
	}

	public void setLegalPersonNo(String legalPersonNo){
		this.legalPersonNo = legalPersonNo;
	}

	public String getBusinessLicenseNo(){
		return businessLicenseNo;
	}

	public void setBusinessLicenseNo(String businessLicenseNo){
		this.businessLicenseNo = businessLicenseNo;
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
}