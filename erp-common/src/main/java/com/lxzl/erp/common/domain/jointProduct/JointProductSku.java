package com.lxzl.erp.common.domain.jointProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JointProductSku implements Serializable {
	private Integer jointProductSkuId;   //唯一标识
	private Integer jointProductId;   //组合商品ID
	@NotNull(message = ErrorCode.JOINT_PRODUCT_SKU_ID_IS_NOT_NULL ,groups = {UpdateGroup.class,AddGroup.class})
	private Integer skuId;   //SKU_ID
	@Min(value=1,message = ErrorCode.JOINT_PRODUCT_SKU_COUNT_MIN_IS_ZERO ,groups = {AddGroup.class,UpdateGroup.class})
	@NotNull(message = ErrorCode.JOINT_PRODUCT_SKU_COUNT_IS_NOT_NULL,groups = {UpdateGroup.class,AddGroup.class})
	private Integer skuCount;   //sku数量
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getJointProductSkuId(){
		return jointProductSkuId;
	}

	public void setJointProductSkuId(Integer jointProductSkuId){
		this.jointProductSkuId = jointProductSkuId;
	}

	public Integer getJointProductId(){
		return jointProductId;
	}

	public void setJointProductId(Integer jointProductId){
		this.jointProductId = jointProductId;
	}

	public Integer getSkuId(){
		return skuId;
	}

	public void setSkuId(Integer skuId){
		this.skuId = skuId;
	}

	public Integer getSkuCount(){
		return skuCount;
	}

	public void setSkuCount(Integer skuCount){
		this.skuCount = skuCount;
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