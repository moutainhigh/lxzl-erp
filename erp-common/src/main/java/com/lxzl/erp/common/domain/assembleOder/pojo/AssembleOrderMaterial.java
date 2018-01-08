package com.lxzl.erp.common.domain.assembleOder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrderMaterial extends BasePO {
	private Integer assembleOrderMaterialId;   //唯一标识
	private Integer assembleOrderId;   //组装单ID
	@NotNull(message = ErrorCode.MATERIAL_NOT_EXISTS, groups = {AddGroup.class,UpdateGroup.class})
	private Integer materialId;   //物料ID
	private String materialNo;   //物料编号
	@Min(value = 1, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class,UpdateGroup.class})
	@NotNull(message = ErrorCode.MATERIAL_COUNT_ERROR, groups = {AddGroup.class,UpdateGroup.class})
	private Integer materialCount;   //物料数量
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private Material material;  //物料

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Integer getAssembleOrderMaterialId(){
		return assembleOrderMaterialId;
	}

	public void setAssembleOrderMaterialId(Integer assembleOrderMaterialId){
		this.assembleOrderMaterialId = assembleOrderMaterialId;
	}

	public Integer getAssembleOrderId(){
		return assembleOrderId;
	}

	public void setAssembleOrderId(Integer assembleOrderId){
		this.assembleOrderId = assembleOrderId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialNo(){
		return materialNo;
	}

	public void setMaterialNo(String materialNo){
		this.materialNo = materialNo;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
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