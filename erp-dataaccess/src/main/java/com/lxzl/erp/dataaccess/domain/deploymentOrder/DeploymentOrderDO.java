package com.lxzl.erp.dataaccess.domain.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class DeploymentOrderDO  extends BaseDO {

	private Integer id;
	private String deploymentOrderNo;
	private Integer deploymentType;
	private Integer srcWarehouseId;
	private Integer srcWarehousePositionId;
	private Integer targetWarehouseId;
	private Integer targetWarehousePositionId;
	private Integer deploymentOrderStatus;
	private Integer totalProductCount;
	private BigDecimal totalProductAmount;
	private Integer totalMaterialCount;
	private BigDecimal totalMaterialAmount;
	private BigDecimal totalOrderAmount;
	private BigDecimal totalDiscountAmount;
	private Date expectReturnTime;
	private Integer dataStatus;
	private String remark;

	private List<DeploymentOrderProductDO> deploymentOrderProductDOList;
	private List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList;


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getDeploymentOrderNo(){
		return deploymentOrderNo;
	}

	public void setDeploymentOrderNo(String deploymentOrderNo){
		this.deploymentOrderNo = deploymentOrderNo;
	}

	public Integer getDeploymentType(){
		return deploymentType;
	}

	public void setDeploymentType(Integer deploymentType){
		this.deploymentType = deploymentType;
	}

	public Integer getSrcWarehouseId(){
		return srcWarehouseId;
	}

	public void setSrcWarehouseId(Integer srcWarehouseId){
		this.srcWarehouseId = srcWarehouseId;
	}

	public Integer getSrcWarehousePositionId(){
		return srcWarehousePositionId;
	}

	public void setSrcWarehousePositionId(Integer srcWarehousePositionId){
		this.srcWarehousePositionId = srcWarehousePositionId;
	}

	public Integer getTargetWarehouseId(){
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(Integer targetWarehouseId){
		this.targetWarehouseId = targetWarehouseId;
	}

	public Integer getTargetWarehousePositionId(){
		return targetWarehousePositionId;
	}

	public void setTargetWarehousePositionId(Integer targetWarehousePositionId){
		this.targetWarehousePositionId = targetWarehousePositionId;
	}

	public Integer getDeploymentOrderStatus(){
		return deploymentOrderStatus;
	}

	public void setDeploymentOrderStatus(Integer deploymentOrderStatus){
		this.deploymentOrderStatus = deploymentOrderStatus;
	}

	public Integer getTotalProductCount(){
		return totalProductCount;
	}

	public void setTotalProductCount(Integer totalProductCount){
		this.totalProductCount = totalProductCount;
	}

	public BigDecimal getTotalProductAmount(){
		return totalProductAmount;
	}

	public void setTotalProductAmount(BigDecimal totalProductAmount){
		this.totalProductAmount = totalProductAmount;
	}

	public Integer getTotalMaterialCount(){
		return totalMaterialCount;
	}

	public void setTotalMaterialCount(Integer totalMaterialCount){
		this.totalMaterialCount = totalMaterialCount;
	}

	public BigDecimal getTotalMaterialAmount(){
		return totalMaterialAmount;
	}

	public void setTotalMaterialAmount(BigDecimal totalMaterialAmount){
		this.totalMaterialAmount = totalMaterialAmount;
	}

	public BigDecimal getTotalOrderAmount(){
		return totalOrderAmount;
	}

	public void setTotalOrderAmount(BigDecimal totalOrderAmount){
		this.totalOrderAmount = totalOrderAmount;
	}

	public BigDecimal getTotalDiscountAmount(){
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount){
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Date getExpectReturnTime(){
		return expectReturnTime;
	}

	public void setExpectReturnTime(Date expectReturnTime){
		this.expectReturnTime = expectReturnTime;
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

	public List<DeploymentOrderMaterialDO> getDeploymentOrderMaterialDOList() {
		return deploymentOrderMaterialDOList;
	}

	public void setDeploymentOrderMaterialDOList(List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList) {
		this.deploymentOrderMaterialDOList = deploymentOrderMaterialDOList;
	}

	public List<DeploymentOrderProductDO> getDeploymentOrderProductDOList() {
		return deploymentOrderProductDOList;
	}

	public void setDeploymentOrderProductDOList(List<DeploymentOrderProductDO> deploymentOrderProductDOList) {
		this.deploymentOrderProductDOList = deploymentOrderProductDOList;
	}
}