package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
	private Integer defaultAddressReferId; //默认地址关联ID
	private String legalPerson;
	private String legalPersonNo;
	private String businessLicenseNo;
	private Integer dataStatus;
	private String remark;

	private Integer customerOrigin; //客户来源,1地推活动，2展会了解，3业务联系，4百度推广，5朋友推荐，6其他广告
	private Date companyFoundTime;  //企业成立时间
	private String industry;  //所属行业'
	private BigDecimal registeredCapital;  //注册资本'
	private Integer officeNumber;  //办公人数'
	private String productPurpose;  //设备用途'
	private String customerCompanyNeedFirstJson;  //首次所需设备
	private String customerCompanyNeedLaterJson;  //后期所需设备
	private String agentPersonName;  //经办人姓名'
	private String agentPersonPhone;  //经办人电话'
	private String agentPersonNo;  //经办人身份证号码'
	private String unifiedCreditCode;  //统一信用代码'
	private String customerNo;  //客户编码
	private Integer isLegalPersonApple;   //是否法人代表申请
	private String legalPersonPhone;   //法人手机号
	private Double operatingArea; //经营面积
	private Integer unitInsuredNumber; //单位参保人数
	private String affiliatedEnterprise; //关联企业
	private String simpleCompanyName;//公司简单名称字段（新增）

	@Transient
	private String cityName;
	@Transient
	private String districtName;
	@Transient
	private String provinceName;
	@Transient
	private List<CustomerConsignInfoDO> customerConsignInfoList;

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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public BigDecimal getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(BigDecimal registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public Integer getOfficeNumber() {
		return officeNumber;
	}

	public void setOfficeNumber(Integer officeNumber) {
		this.officeNumber = officeNumber;
	}

	public String getProductPurpose() {
		return productPurpose;
	}

	public void setProductPurpose(String productPurpose) {
		this.productPurpose = productPurpose;
	}

	public String getCustomerCompanyNeedFirstJson() {
		return customerCompanyNeedFirstJson;
	}

	public void setCustomerCompanyNeedFirstJson(String customerCompanyNeedFirstJson) {
		this.customerCompanyNeedFirstJson = customerCompanyNeedFirstJson;
	}

	public String getCustomerCompanyNeedLaterJson() {
		return customerCompanyNeedLaterJson;
	}

	public void setCustomerCompanyNeedLaterJson(String customerCompanyNeedLaterJson) {
		this.customerCompanyNeedLaterJson = customerCompanyNeedLaterJson;
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

	public String getUnifiedCreditCode() {
		return unifiedCreditCode;
	}

	public void setUnifiedCreditCode(String unifiedCreditCode) {
		this.unifiedCreditCode = unifiedCreditCode;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Integer getIsLegalPersonApple() {
		return isLegalPersonApple;
	}

	public void setIsLegalPersonApple(Integer isLegalPersonApple) {
		this.isLegalPersonApple = isLegalPersonApple;
	}

	public String getLegalPersonPhone() {
		return legalPersonPhone;
	}

	public void setLegalPersonPhone(String legalPersonPhone) {
		this.legalPersonPhone = legalPersonPhone;
	}

	public Integer getCustomerOrigin() {
		return customerOrigin;
	}

	public void setCustomerOrigin(Integer customerOrigin) {
		this.customerOrigin = customerOrigin;
	}

	public Date getCompanyFoundTime() {
		return companyFoundTime;
	}

	public void setCompanyFoundTime(Date companyFoundTime) {
		this.companyFoundTime = companyFoundTime;
	}

	public Double getOperatingArea() {
		return operatingArea;
	}

	public void setOperatingArea(Double operatingArea) {
		this.operatingArea = operatingArea;
	}

	public Integer getUnitInsuredNumber() {
		return unitInsuredNumber;
	}

	public void setUnitInsuredNumber(Integer unitInsuredNumber) {
		this.unitInsuredNumber = unitInsuredNumber;
	}

	public String getAffiliatedEnterprise() {
		return affiliatedEnterprise;
	}

	public void setAffiliatedEnterprise(String affiliatedEnterprise) {
		this.affiliatedEnterprise = affiliatedEnterprise;
	}

	public Integer getDefaultAddressReferId() {
		return defaultAddressReferId;
	}

	public void setDefaultAddressReferId(Integer defaultAddressReferId) {
		this.defaultAddressReferId = defaultAddressReferId;
	}

	public List<CustomerConsignInfoDO> getCustomerConsignInfoList() { return customerConsignInfoList; }

	public void setCustomerConsignInfoList(List<CustomerConsignInfoDO> customerConsignInfoList) { this.customerConsignInfoList = customerConsignInfoList; }

	public String getSimpleCompanyName() {
		return simpleCompanyName;
	}

	public void setSimpleCompanyName(String simpleCompanyName) {
		this.simpleCompanyName = simpleCompanyName;
	}
}