package com.lxzl.erp.dataaccess.domain.peer;

import com.lxzl.erp.common.domain.area.AreaCity;
import com.lxzl.erp.common.domain.area.AreaDistrict;
import com.lxzl.erp.common.domain.area.AreaProvince;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.area.AreaDistrictDO;
import com.lxzl.erp.dataaccess.domain.area.AreaProvinceDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PeerDO  extends BaseDO {

	private Integer id;
	private String peerNo;
	private String peerName;
	private String peerCode;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private String tel;
	private String contactName;
	private String contactPhone;
	private String beneficiaryName;
	private String beneficiaryAccount;
	private String beneficiaryBankName;
	private Integer dataStatus;
	private String remark;

	private AreaCityDO areaCityDO;  //地区城市
	private AreaDistrictDO areaDistrictDO;  //地区行政区
	private AreaProvinceDO areaProvinceDO;  //地区省份

	public AreaCityDO getAreaCityDO() {
		return areaCityDO;
	}

	public void setAreaCityDO(AreaCityDO areaCityDO) {
		this.areaCityDO = areaCityDO;
	}

	public AreaDistrictDO getAreaDistrictDO() {
		return areaDistrictDO;
	}

	public void setAreaDistrictDO(AreaDistrictDO areaDistrictDO) {
		this.areaDistrictDO = areaDistrictDO;
	}

	public AreaProvinceDO getAreaProvinceDO() {
		return areaProvinceDO;
	}

	public void setAreaProvinceDO(AreaProvinceDO areaProvinceDO) {
		this.areaProvinceDO = areaProvinceDO;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}