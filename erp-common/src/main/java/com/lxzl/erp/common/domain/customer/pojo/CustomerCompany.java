package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCompany implements Serializable {

	private Integer customerCompanyId;   //唯一标识
	private Integer customerId;   //客户ID
	private String landline;   //座机电话
	@NotBlank(message = ErrorCode.CUSTOMER_COMPANY_CONNECT_NAME_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
	private String connectRealName;   //联系人
	@NotBlank(message = ErrorCode.CUSTOMER_COMPANY_CONNECT_PHONE_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
	private String connectPhone;   //联系人手机号
	@NotBlank(message = ErrorCode.CUSTOMER_COMPANY_NAME_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
	private String companyName;   //公司名称
	private String companyAbb;   //公司简称
	private Integer province;   //省份ID，省份ID
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	@NotBlank(message = ErrorCode.CUSTOMER_COMPANY_ADDRESS_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
	private String address;   //详细地址
	private String legalPerson;   //法人姓名
	private String legalPersonNo;   //法人身份证号
	private String businessLicenseNo;   //营业执照号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private String provinceName;// 省名
	private String cityName; //城市名
	private String districtName; //地区名

	public Integer getCustomerCompanyId(){
		return customerCompanyId;
	}

	public void setCustomerCompanyId(Integer customerCompanyId){
		this.customerCompanyId = customerCompanyId;
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