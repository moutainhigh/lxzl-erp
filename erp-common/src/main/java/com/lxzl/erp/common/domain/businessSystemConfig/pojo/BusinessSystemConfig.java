package com.lxzl.erp.common.domain.businessSystemConfig.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessSystemConfig extends BasePO {

	private Integer businessSystemConfigId;   //唯一标识
	private String businessSystemName;   //系统名称
	private Integer businessSystemType;   //业务系统类型，1为凌雄商城
	private String businessAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
	private String businessAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private Date updateTime;   //添加时间
	private String createUser;   //添加人
	private String updateUser;   //修改人


	public Integer getBusinessSystemConfigId(){
		return businessSystemConfigId;
	}

	public void setBusinessSystemConfigId(Integer businessSystemConfigId){
		this.businessSystemConfigId = businessSystemConfigId;
	}

	public String getBusinessSystemName(){
		return businessSystemName;
	}

	public void setBusinessSystemName(String businessSystemName){
		this.businessSystemName = businessSystemName;
	}

	public Integer getBusinessSystemType(){
		return businessSystemType;
	}

	public void setBusinessSystemType(Integer businessSystemType){
		this.businessSystemType = businessSystemType;
	}

	public String getBusinessAppId(){
		return businessAppId;
	}

	public void setBusinessAppId(String businessAppId){
		this.businessAppId = businessAppId;
	}

	public String getBusinessAppSecret(){
		return businessAppSecret;
	}

	public void setBusinessAppSecret(String businessAppSecret){
		this.businessAppSecret = businessAppSecret;
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

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}