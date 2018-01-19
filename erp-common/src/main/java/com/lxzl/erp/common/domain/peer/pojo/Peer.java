package com.lxzl.erp.common.domain.peer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer extends BasePO {

	private Integer peerId;   //唯一标识
	@NotNull(message = ErrorCode.PEER_NO_NOT_EXISTS,groups = {UpdateGroup.class, IdGroup.class})
	private String peerNo;   //供应商编码
	@NotNull(message = ErrorCode.PEER_NAME_NOT_NULL,groups = {AddGroup.class})
	private String peerName;   //供应商名称
	@NotNull(message = ErrorCode.PEER_CODE_NOT_NULL,groups = {AddGroup.class})
	private String peerCode;   //供应商自定义编码
	@Min(value = 0, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class})
	@NotNull(message = ErrorCode.PROVINCE_ID_NOT_NULL,groups = {AddGroup.class})
	private Integer province;   //省份ID，省份ID
	@Min(value = 0, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class})
	@NotNull(message = ErrorCode.CITY_ID_NOT_NULL,groups = {AddGroup.class})
	private Integer city;   //城市ID，对应城市ID
	@Min(value = 0, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class})
	@NotNull(message = ErrorCode.DISTRICT_ID_NOT_NULL,groups = {AddGroup.class})
	private Integer district;   //区ID，对应区ID
	private String address;   //详细地址
	private String tel;   //电话号码
	private String contactName;   //联系人姓名
	private String contactPhone;   //联系手机号
	@Length(max = 100,message = ErrorCode.BENEFICIARY_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
	private String beneficiaryName;   //收款户名
	@Length(min = 16,max = 19,message = ErrorCode.BANK_NO_ERROR,groups = {AddGroup.class, UpdateGroup.class})
	@Pattern(regexp = "^[0-9-]+$",message = ErrorCode.BANK_NO_ERROR,groups = {AddGroup.class, UpdateGroup.class})
	private String beneficiaryAccount;   //收款帐号
	@Length(max = 100,message = ErrorCode.BENEFICIARY_BANK_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
	private String beneficiaryBankName;   //收款开户行
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private String provinceName;  //省份名称
	private String cityName;  //城市名称
	private String districtName;  //区名称

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

	public Integer getPeerId(){
		return peerId;
	}

	public void setPeerId(Integer peerId){
		this.peerId = peerId;
	}

	public String getPeerNo(){
		return peerNo;
	}

	public void setPeerNo(String peerNo){
		this.peerNo = peerNo;
	}

	public String getPeerName(){
		return peerName;
	}

	public void setPeerName(String peerName){
		this.peerName = peerName;
	}

	public String getPeerCode(){
		return peerCode;
	}

	public void setPeerCode(String peerCode){
		this.peerCode = peerCode;
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

	public String getBeneficiaryName(){
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName){
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryAccount(){
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount){
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public String getBeneficiaryBankName(){
		return beneficiaryBankName;
	}

	public void setBeneficiaryBankName(String beneficiaryBankName){
		this.beneficiaryBankName = beneficiaryBankName;
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