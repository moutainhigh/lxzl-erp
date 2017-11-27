package com.lxzl.erp.common.domain.deploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentOrderMaterialBulk implements Serializable {

	private Integer deploymentOrderMaterialBulkId;   //唯一标识
	private Integer deploymentOrderMaterialId;   //货物调拨物料项ID
	private Integer deploymentOrderId;   //货物调拨单ID
	private String deploymentOrderNo;   //货物调拨单编号
	private Integer bulkMaterialId;   //散料ID
	private String bulkMaterialNo;   //散料编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getDeploymentOrderMaterialBulkId(){
		return deploymentOrderMaterialBulkId;
	}

	public void setDeploymentOrderMaterialBulkId(Integer deploymentOrderMaterialBulkId){
		this.deploymentOrderMaterialBulkId = deploymentOrderMaterialBulkId;
	}

	public Integer getDeploymentOrderMaterialId(){
		return deploymentOrderMaterialId;
	}

	public void setDeploymentOrderMaterialId(Integer deploymentOrderMaterialId){
		this.deploymentOrderMaterialId = deploymentOrderMaterialId;
	}

	public Integer getDeploymentOrderId(){
		return deploymentOrderId;
	}

	public void setDeploymentOrderId(Integer deploymentOrderId){
		this.deploymentOrderId = deploymentOrderId;
	}

	public String getDeploymentOrderNo(){
		return deploymentOrderNo;
	}

	public void setDeploymentOrderNo(String deploymentOrderNo){
		this.deploymentOrderNo = deploymentOrderNo;
	}

	public Integer getBulkMaterialId(){
		return bulkMaterialId;
	}

	public void setBulkMaterialId(Integer bulkMaterialId){
		this.bulkMaterialId = bulkMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
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