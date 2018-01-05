package com.lxzl.erp.dataaccess.domain.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class DeploymentOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer deploymentOrderId;
	private String deploymentOrderNo;
	private Integer deploymentProductSkuId;
	private BigDecimal deploymentProductUnitAmount;
	private BigDecimal deploymentProductAmount;
	private Integer deploymentProductSkuCount;
	private String deploymentProductSkuSnapshot;
	private Integer dataStatus;
	private String remark;
	private Integer isNew;

	private List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public List<DeploymentOrderProductEquipmentDO> getDeploymentOrderProductEquipmentDOList() {
		return deploymentOrderProductEquipmentDOList;
	}

	public void setDeploymentOrderProductEquipmentDOList(List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList) {
		this.deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentDOList;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
}