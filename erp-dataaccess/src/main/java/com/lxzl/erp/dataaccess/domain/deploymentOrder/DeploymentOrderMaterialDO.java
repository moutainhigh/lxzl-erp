package com.lxzl.erp.dataaccess.domain.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class DeploymentOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer deploymentOrderId;
	private String deploymentOrderNo;
	private Integer deploymentMaterialId;
	private BigDecimal deploymentMaterialUnitAmount;
	private BigDecimal deploymentMaterialAmount;
	private Integer deploymentProductMaterialCount;
	private String deploymentProductMaterialSnapshot;
	private Integer dataStatus;
	private String remark;
	private Integer isNew;

	private List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList;

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

	public List<DeploymentOrderMaterialBulkDO> getDeploymentOrderMaterialBulkDOList() {
		return deploymentOrderMaterialBulkDOList;
	}

	public void setDeploymentOrderMaterialBulkDOList(List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList) {
		this.deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkDOList;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
}