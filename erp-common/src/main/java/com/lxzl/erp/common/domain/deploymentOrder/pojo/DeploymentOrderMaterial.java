package com.lxzl.erp.common.domain.deploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentOrderMaterial extends BasePO {

	private Integer deploymentOrderMaterialId;   //唯一标识
	private Integer deploymentOrderId;   //货物调拨单ID
	private String deploymentOrderNo;   //货物调拨单编号
	private Integer deploymentMaterialId;   //货物调拨物料ID
	private BigDecimal deploymentMaterialUnitAmount;   //物料单价
	private BigDecimal deploymentMaterialAmount;   //物料总价格
	private Integer deploymentProductMaterialCount;   //货物调拨物料数量
	private String deploymentProductMaterialSnapshot;   //货物调拨物料快照
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	private Integer isNew;

	private List<DeploymentOrderMaterialBulk> deploymentOrderMaterialBulkList;


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

	public Integer getDeploymentMaterialId(){
		return deploymentMaterialId;
	}

	public void setDeploymentMaterialId(Integer deploymentMaterialId){
		this.deploymentMaterialId = deploymentMaterialId;
	}

	public BigDecimal getDeploymentMaterialUnitAmount(){
		return deploymentMaterialUnitAmount;
	}

	public void setDeploymentMaterialUnitAmount(BigDecimal deploymentMaterialUnitAmount){
		this.deploymentMaterialUnitAmount = deploymentMaterialUnitAmount;
	}

	public BigDecimal getDeploymentMaterialAmount(){
		return deploymentMaterialAmount;
	}

	public void setDeploymentMaterialAmount(BigDecimal deploymentMaterialAmount){
		this.deploymentMaterialAmount = deploymentMaterialAmount;
	}

	public Integer getDeploymentProductMaterialCount(){
		return deploymentProductMaterialCount;
	}

	public void setDeploymentProductMaterialCount(Integer deploymentProductMaterialCount){
		this.deploymentProductMaterialCount = deploymentProductMaterialCount;
	}

	public String getDeploymentProductMaterialSnapshot(){
		return deploymentProductMaterialSnapshot;
	}

	public void setDeploymentProductMaterialSnapshot(String deploymentProductMaterialSnapshot){
		this.deploymentProductMaterialSnapshot = deploymentProductMaterialSnapshot;
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

	public List<DeploymentOrderMaterialBulk> getDeploymentOrderMaterialBulkList() {
		return deploymentOrderMaterialBulkList;
	}

	public void setDeploymentOrderMaterialBulkList(List<DeploymentOrderMaterialBulk> deploymentOrderMaterialBulkList) {
		this.deploymentOrderMaterialBulkList = deploymentOrderMaterialBulkList;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
}