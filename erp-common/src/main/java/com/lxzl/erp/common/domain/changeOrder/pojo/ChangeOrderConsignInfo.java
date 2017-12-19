package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderConsignInfo implements Serializable {

	private Integer changeOrderConsignInfoId;   //唯一标识
	private Integer changeOrderId;   //换货ID
	private String changeOrderNo;   //换货编号
	@NotBlank(message = ErrorCode.CONSIGNEE_NAME_NOT_NULL,groups = {AddChangeOrderGroup.class})
	private String consigneeName;   //收货人姓名
	@NotBlank(message = ErrorCode.CONSIGNEE_PHONE_NOT_NULL,groups = {AddChangeOrderGroup.class})
	private String consigneePhone;   //收货人手机号
	private Integer province;   //省份ID，省份ID
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	@NotBlank(message = ErrorCode.ADDRESS_NOT_NULL,groups = {AddChangeOrderGroup.class})
	private String address;   //详细地址
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private String cityName;
	private String districtName;
	private String provinceName;

	public Integer getChangeOrderConsignInfoId(){
		return changeOrderConsignInfoId;
	}

	public void setChangeOrderConsignInfoId(Integer changeOrderConsignInfoId){
		this.changeOrderConsignInfoId = changeOrderConsignInfoId;
	}

	public Integer getChangeOrderId(){
		return changeOrderId;
	}

	public void setChangeOrderId(Integer changeOrderId){
		this.changeOrderId = changeOrderId;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
}