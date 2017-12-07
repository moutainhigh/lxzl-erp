package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerPersonGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerPersonGroup;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerPerson implements Serializable {

	private Integer customerPersonId;   //唯一标识
	private Integer customerId;   //客户ID
	@NotBlank(message = ErrorCode.CUSTOMER_PERSON_NAME_NOT_NULL,groups={AddCustomerPersonGroup.class,UpdateCustomerPersonGroup.class})
	private String realName;   //真实姓名
	@Email(message = ErrorCode.EMAIL_ERROR,groups={AddCustomerPersonGroup.class,UpdateCustomerPersonGroup.class})
	private String email;   //电子邮件
	@NotBlank(message = ErrorCode.CUSTOMER_PERSON_PHONE_NOT_NULL,groups={AddCustomerPersonGroup.class})
	private String phone;   //手机号
	private Integer province;   //省份ID，省份ID
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	@NotBlank(message = ErrorCode.CUSTOMER_PERSON_ADDRESS_NOT_NULL,groups={AddCustomerPersonGroup.class})
	private String address;   //详细地址
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private String provinceName;
	private String cityName;
	private String districtName;


	public Integer getCustomerPersonId(){
		return customerPersonId;
	}

	public void setCustomerPersonId(Integer customerPersonId){
		this.customerPersonId = customerPersonId;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public String getRealName(){
		return realName;
	}

	public void setRealName(String realName){
		this.realName = realName;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
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