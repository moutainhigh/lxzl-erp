package com.lxzl.erp.common.domain.material.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialType extends BasePO {

	private Integer materialTypeId;   //唯一标识
	private String materialTypeName;   //配件类型名称
	private Integer isMainMaterial;   //是否为主要配件
	private Integer isCapacityMaterial;   //是否为带字面量配件,硬盘、内存之类是
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getMaterialTypeId(){
		return materialTypeId;
	}

	public void setMaterialTypeId(Integer materialTypeId){
		this.materialTypeId = materialTypeId;
	}

	public String getMaterialTypeName(){
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName){
		this.materialTypeName = materialTypeName;
	}

	public Integer getIsMainMaterial(){
		return isMainMaterial;
	}

	public void setIsMainMaterial(Integer isMainMaterial){
		this.isMainMaterial = isMainMaterial;
	}

	public Integer getIsCapacityMaterial(){
		return isCapacityMaterial;
	}

	public void setIsCapacityMaterial(Integer isCapacityMaterial){
		this.isCapacityMaterial = isCapacityMaterial;
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