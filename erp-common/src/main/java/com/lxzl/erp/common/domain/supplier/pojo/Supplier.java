package com.lxzl.erp.common.domain.supplier.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier extends BasePO {

	private Integer supplierId;   //字典ID，唯一
	private String supplierNo;   //供应商编号
	private String supplierName;   //供应商名称
	private Integer supplierType;   //供应商类型
	private Integer province;   //省份ID，对应字典ID
	private Integer city;   //城市ID，对应字典ID
	private Integer district;   //区ID，对应字典ID
	private String address;   //详细地址
	private String tel;   //电话号码
	private String contactName;   //联系人姓名
	private String contactPhone;   //联系手机号

	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	@Length(max = 100,message = ErrorCode.BENEFICIARY_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
	private String beneficiaryName;   //收款户名
	private String beneficiaryAccount;   //收款帐号
	@Length(max = 100,message = ErrorCode.BENEFICIARY_BANK_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
	private String beneficiaryBankName;   //收款开户行
	@Length(max = 20,message = ErrorCode.SUPPLIER_CODE_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
	@Pattern(regexp = "^[A-Za-z0-9-_]+$",message = ErrorCode.SUPPLIER_CODE_NOT_CN,groups = {AddGroup.class, UpdateGroup.class})
	private String supplierCode;	//供应商自定义编码

	private String provinceName;	//省份名称
	private String cityName;		//城市名称
	private String districtName;	//地区名称


	public Integer getSupplierId(){
		return supplierId;
	}

	public void setSupplierId(Integer supplierId){
		this.supplierId = supplierId;
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

	public Integer getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Integer supplierType) {
		this.supplierType = supplierType;
	}
}