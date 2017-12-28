package com.lxzl.erp.common.domain.deploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentOrderProduct extends BasePO {

	private Integer deploymentOrderProductId;   //唯一标识
	private Integer deploymentOrderId;   //调拨单ID
	private String deploymentOrderNo;   //货物调拨单编号
	private Integer deploymentProductSkuId;   //货物调拨单商品SKU_ID
	private BigDecimal deploymentProductUnitAmount;   //商品单价
	private BigDecimal deploymentProductAmount;   //商品总价格
	private Integer deploymentProductSkuCount;   //货物调拨单商品SKU数量
	private String deploymentProductSkuSnapshot;   //货物调拨单商品SKU快照
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<DeploymentOrderProductEquipment> deploymentOrderProductEquipmentList;


	public Integer getDeploymentOrderProductId(){
		return deploymentOrderProductId;
	}

	public void setDeploymentOrderProductId(Integer deploymentOrderProductId){
		this.deploymentOrderProductId = deploymentOrderProductId;
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

	public Integer getDeploymentProductSkuId(){
		return deploymentProductSkuId;
	}

	public void setDeploymentProductSkuId(Integer deploymentProductSkuId){
		this.deploymentProductSkuId = deploymentProductSkuId;
	}

	public BigDecimal getDeploymentProductUnitAmount(){
		return deploymentProductUnitAmount;
	}

	public void setDeploymentProductUnitAmount(BigDecimal deploymentProductUnitAmount){
		this.deploymentProductUnitAmount = deploymentProductUnitAmount;
	}

	public BigDecimal getDeploymentProductAmount(){
		return deploymentProductAmount;
	}

	public void setDeploymentProductAmount(BigDecimal deploymentProductAmount){
		this.deploymentProductAmount = deploymentProductAmount;
	}

	public Integer getDeploymentProductSkuCount(){
		return deploymentProductSkuCount;
	}

	public void setDeploymentProductSkuCount(Integer deploymentProductSkuCount){
		this.deploymentProductSkuCount = deploymentProductSkuCount;
	}

	public String getDeploymentProductSkuSnapshot(){
		return deploymentProductSkuSnapshot;
	}

	public void setDeploymentProductSkuSnapshot(String deploymentProductSkuSnapshot){
		this.deploymentProductSkuSnapshot = deploymentProductSkuSnapshot;
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

	public List<DeploymentOrderProductEquipment> getDeploymentOrderProductEquipmentList() {
		return deploymentOrderProductEquipmentList;
	}

	public void setDeploymentOrderProductEquipmentList(List<DeploymentOrderProductEquipment> deploymentOrderProductEquipmentList) {
		this.deploymentOrderProductEquipmentList = deploymentOrderProductEquipmentList;
	}
}