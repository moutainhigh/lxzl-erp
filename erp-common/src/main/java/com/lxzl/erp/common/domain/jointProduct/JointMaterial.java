package com.lxzl.erp.common.domain.jointProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JointMaterial implements Serializable {
	private Integer jointMaterialId;   //唯一标识
	private Integer jointProductId;   //组合商品ID
	@NotNull(message = ErrorCode.JOINT_PRODUCT_MATERIAL_ID_IS_NOT_NULL ,groups = {AddGroup.class,UpdateGroup.class})
	private Integer materialId;   //物料ID
	@Min(value=0,message = ErrorCode.JOINT_PRODUCT_MATERIAL_COUNT_MIN_IS_ZERO ,groups = {AddGroup.class,UpdateGroup.class})
	@NotNull(message = ErrorCode.JOINT_PRODUCT_MATERIAL_COUNT_IS_NOT_NULL ,groups = {AddGroup.class,UpdateGroup.class})
	private Integer materialCount;   //物料数量
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getJointMaterialId(){
		return jointMaterialId;
	}

	public void setJointMaterialId(Integer jointMaterialId){
		this.jointMaterialId = jointMaterialId;
	}

	public Integer getJointProductId(){
		return jointProductId;
	}

	public void setJointProductId(Integer jointProductId){
		this.jointProductId = jointProductId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
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