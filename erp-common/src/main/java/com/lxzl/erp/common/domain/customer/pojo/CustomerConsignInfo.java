package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerConsignInfo extends BasePO {

	@NotNull(message = ErrorCode.CUSTOMER_CONSIGN_INFO_ID_NOT_NULL,groups = {IdGroup.class,UpdateGroup.class})
	private Integer customerConsignInfoId;   //唯一标识
	@NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {AddGroup.class})
	private String customerNo; //客户编号
	private Integer customerId;   //用户ID
	@NotBlank(message = ErrorCode.CONSIGNEE_NAME_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class})
	private String consigneeName;   //收货人姓名
	@NotBlank(message = ErrorCode.CONSIGNEE_PHONE_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class})
	private String consigneePhone;   //收货人手机号
	private Integer province;   //省份ID，省份ID
	@NotNull(message = ErrorCode.CITY_ID_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class})
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	@NotBlank(message = ErrorCode.ADDRESS_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class})
	private String address;   //详细地址
	@In(value = {CommonConstant.YES,CommonConstant.NO}, message=ErrorCode.CUSTOMER_CONSIGN_INFO_IS_MAIN_ERROR,groups = {AddGroup.class,UpdateGroup.class})
	private Integer isMain;   //是否为默认地址，0否1是
	@In(value = {CommonConstant.YES,CommonConstant.NO}, message=ErrorCode.CUSTOMER_CONSIGN_INFO_IS_BUSINESS_ADDRESS_ERROR,groups = {AddGroup.class,UpdateGroup.class})
	private Integer isBusinessAddress;	//是否为经营地址，0否1是
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date lastUseTime;  //最后使用时间
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private String provinceName; //省名称
	private String cityName; //城市名
	private String districtName; //地区名

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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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

	public void setLastUseTime(Date lastUseTme) {
		this.lastUseTime = lastUseTme;
	}

	public Integer getIsBusinessAddress() { return isBusinessAddress; }

	public void setIsBusinessAddress(Integer isBusinessAddress) { this.isBusinessAddress = isBusinessAddress; }
}