package com.lxzl.erp.common.domain.deploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentOrder extends BasePO {

	private Integer deploymentOrderId;   //唯一标识
	private String deploymentOrderNo;   //调配单编号
	private Integer deploymentType;   //调配类型，1借调，2售调
	private Integer srcWarehouseId;   //源仓库ID
	private Integer srcWarehousePositionId;   //源仓位ID
	private Integer targetWarehouseId;   //目标仓库ID
	private Integer targetWarehousePositionId;   //目标仓位ID
	private Integer deploymentOrderStatus;   //调配单状态，0未提交，1审批中，2处理中，3确认收货
	private Integer totalProductCount;   //商品总数
	private BigDecimal totalProductAmount;   //商品总价
	private Integer totalMaterialCount;   //物料总数
	private BigDecimal totalMaterialAmount;   //物料总价
	private BigDecimal totalOrderAmount;   //订单总价
	private BigDecimal totalDiscountAmount;   //共计优惠金额
	private Date expectReturnTime;   //预计归还时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<DeploymentOrderProduct> deploymentOrderProductList;
	private List<DeploymentOrderMaterial> deploymentOrderMaterialList;

	private Integer verifyUser;
	private String srcWarehouseName;
	private String targetWarehouseName;


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

	public List<DeploymentOrderProduct> getDeploymentOrderProductList() {
		return deploymentOrderProductList;
	}

	public void setDeploymentOrderProductList(List<DeploymentOrderProduct> deploymentOrderProductList) {
		this.deploymentOrderProductList = deploymentOrderProductList;
	}

	public List<DeploymentOrderMaterial> getDeploymentOrderMaterialList() {
		return deploymentOrderMaterialList;
	}

	public void setDeploymentOrderMaterialList(List<DeploymentOrderMaterial> deploymentOrderMaterialList) {
		this.deploymentOrderMaterialList = deploymentOrderMaterialList;
	}

	public Integer getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getSrcWarehouseName() {
		return srcWarehouseName;
	}

	public void setSrcWarehouseName(String srcWarehouseName) {
		this.srcWarehouseName = srcWarehouseName;
	}

	public String getTargetWarehouseName() {
		return targetWarehouseName;
	}

	public void setTargetWarehouseName(String targetWarehouseName) {
		this.targetWarehouseName = targetWarehouseName;
	}
}