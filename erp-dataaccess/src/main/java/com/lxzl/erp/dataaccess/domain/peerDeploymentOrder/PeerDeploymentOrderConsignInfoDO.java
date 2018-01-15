package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PeerDeploymentOrderConsignInfoDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderId;
	private String contactName;
	private String contactPhone;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private Integer dataStatus;
	private String remark;

	private String provinceName;
	private String cityName;
	private String districtName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

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

	public String getProvinceName() { return provinceName; }

	public void setProvinceName(String provinceName) { this.provinceName = provinceName; }

	public String getCityName() { return cityName; }

	public void setCityName(String cityName) { this.cityName = cityName; }

	public String getDistrictName() { return districtName; }

	public void setDistrictName(String districtName) { this.districtName = districtName; }
}