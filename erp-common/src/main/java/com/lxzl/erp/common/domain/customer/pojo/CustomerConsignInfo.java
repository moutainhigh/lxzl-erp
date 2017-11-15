package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerConsignInfo implements Serializable {

	private Integer customerConsignInfoId;   //唯一标识
	private Integer customerId;   //用户ID
	private String consigneeName;   //收货人姓名
	private String consigneePhone;   //收货人手机号
	private Integer province;   //省份ID，省份ID
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	private String address;   //详细地址
	private Integer isMain;   //是否为默认地址，0否1是
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getCustomerConsignInfoId(){
		return customerConsignInfoId;
	}

	public void setCustomerConsignInfoId(Integer customerConsignInfoId){
		this.customerConsignInfoId = customerConsignInfoId;
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

}