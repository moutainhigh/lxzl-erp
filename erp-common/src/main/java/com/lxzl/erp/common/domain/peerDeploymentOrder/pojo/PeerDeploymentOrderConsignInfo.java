package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.Pattern;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderConsignInfo extends BasePO {

	private Integer peerDeploymentOrderConsignInfoId;   //唯一标识
	private Integer peerDeploymentOrderId;   //货物调拨单ID
	private String contactName;   //联系人姓名
	@Pattern(regexp = "^[0-9-]+$",message = ErrorCode.PEER_DEPLOYMENT_ORDER_CONSIGN_INFO_PHONE_IS_MATH,groups = {AddGroup.class, UpdateGroup.class})
	private String contactPhone;   //联系人手机号
	private Integer province;   //省份ID，省份ID
	private Integer city;   //城市ID，对应城市ID
	private Integer district;   //区ID，对应区ID
	private String address;   //详细地址
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private String provinceName;
	private String cityName;
	private String districtName;


	public Integer getPeerDeploymentOrderConsignInfoId(){
		return peerDeploymentOrderConsignInfoId;
	}

	public void setPeerDeploymentOrderConsignInfoId(Integer peerDeploymentOrderConsignInfoId){ this.peerDeploymentOrderConsignInfoId = peerDeploymentOrderConsignInfoId; }

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

	public String getProvinceName() { return provinceName; }

	public void setProvinceName(String provinceName) { this.provinceName = provinceName; }

	public String getCityName() { return cityName; }

	public void setCityName(String cityName) { this.cityName = cityName; }

	public String getDistrictName() { return districtName; }

	public void setDistrictName(String districtName) { this.districtName = districtName; }
}