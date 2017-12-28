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

	private int   companyOrigin; //客户来源,1转介绍,2老客户,3公司资源,4主动开发
	private Date   foundTime;  //企业成立时间'
	private String   industry;  //所属行业'
	private double   registeredCapital;  //注册资本'
	private int   officeNumber;  //办公人数'
	private String   productPurpose;  //设备用途'
	private String   listFirstNeedProducts;  //首次所需设备'
	private String   listLaterNeedProducts;  //后期所需设备'
	private String   agentPersonName;  //经办人姓名'
	private String   agentPersonPhone;  //经办人电话'
	private String   agentPersonNo;  //经办人身份证号码'
	private String   consignAddress;  //收货地址'
	private String   unifiedCreditCode;  //统一信用代码'

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

	public int getCompanyOrigin() {
		return companyOrigin;
	}

	public void setCompanyOrigin(int companyOrigin) {
		this.companyOrigin = companyOrigin;
	}

	public Date getFoundTime() {
		return foundTime;
	}

	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public double getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(double registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public int getOfficeNumber() {
		return officeNumber;
	}

	public void setOfficeNumber(int officeNumber) {
		this.officeNumber = officeNumber;
	}

	public String getProductPurpose() {
		return productPurpose;
	}

	public void setProductPurpose(String productPurpose) {
		this.productPurpose = productPurpose;
	}

	public String getListFirstNeedProducts() {
		return listFirstNeedProducts;
	}

	public void setListFirstNeedProducts(String listFirstNeedProducts) {
		this.listFirstNeedProducts = listFirstNeedProducts;
	}

	public String getListLaterNeedProducts() {
		return listLaterNeedProducts;
	}

	public void setListLaterNeedProducts(String listLaterNeedProducts) {
		this.listLaterNeedProducts = listLaterNeedProducts;
	}

	public String getAgentPersonName() {
		return agentPersonName;
	}

	public void setAgentPersonName(String agentPersonName) {
		this.agentPersonName = agentPersonName;
	}

	public String getAgentPersonPhone() {
		return agentPersonPhone;
	}

	public void setAgentPersonPhone(String agentPersonPhone) {
		this.agentPersonPhone = agentPersonPhone;
	}

	public String getAgentPersonNo() {
		return agentPersonNo;
	}

	public void setAgentPersonNo(String agentPersonNo) {
		this.agentPersonNo = agentPersonNo;
	}

	public String getConsignAddress() {
		return consignAddress;
	}

	public void setConsignAddress(String consignAddress) {
		this.consignAddress = consignAddress;
	}

	public String getUnifiedCreditCode() {
		return unifiedCreditCode;
	}

	public void setUnifiedCreditCode(String unifiedCreditCode) {
		this.unifiedCreditCode = unifiedCreditCode;
	}
}