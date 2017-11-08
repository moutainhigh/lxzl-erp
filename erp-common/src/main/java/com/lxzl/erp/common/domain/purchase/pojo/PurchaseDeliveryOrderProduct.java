package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDeliveryOrderProduct implements Serializable {

	private Integer purchaseDeliveryOrderProductId;   //唯一标识
	private Integer purchaseDeliveryOrderId;   //采购发货单ID
	private Integer purchaseOrderProductId;   //采购单项ID
	private Integer productId;   //商品ID，来源（采购单），不可变更
	private String productName;   //商品名称冗余，不可修改
	private Integer productSkuId;   //商品SKU ID 不可变更
	private String productSnapshot;   //商品冗余信息，防止商品修改留存快照，不可修改
	private Integer productCount;   //商品总数，来源（采购单），不可变更
	private Integer realProductId;   //实际商品ID
	private String realProductName;   //商品名称冗余，可修改
	private Integer realProductSkuId;   //商品SKU ID 可修改
	private String realProductSnapshot;   //商品冗余信息，防止商品修改留存快照，可修改
	private Integer realProductCount;   //实际商品总数
	private BigDecimal productAmount;   //商品单价
	private String remark;   //备注
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getPurchaseDeliveryOrderProductId(){
		return purchaseDeliveryOrderProductId;
	}

	public void setPurchaseDeliveryOrderProductId(Integer purchaseDeliveryOrderProductId){
		this.purchaseDeliveryOrderProductId = purchaseDeliveryOrderProductId;
	}

	public Integer getPurchaseDeliveryOrderId(){
		return purchaseDeliveryOrderId;
	}

	public void setPurchaseDeliveryOrderId(Integer purchaseDeliveryOrderId){
		this.purchaseDeliveryOrderId = purchaseDeliveryOrderId;
	}

	public Integer getPurchaseOrderProductId(){
		return purchaseOrderProductId;
	}

	public void setPurchaseOrderProductId(Integer purchaseOrderProductId){
		this.purchaseOrderProductId = purchaseOrderProductId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public String getProductSnapshot(){
		return productSnapshot;
	}

	public void setProductSnapshot(String productSnapshot){
		this.productSnapshot = productSnapshot;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
	}

	public Integer getRealProductId(){
		return realProductId;
	}

	public void setRealProductId(Integer realProductId){
		this.realProductId = realProductId;
	}

	public String getRealProductName(){
		return realProductName;
	}

	public void setRealProductName(String realProductName){
		this.realProductName = realProductName;
	}

	public Integer getRealProductSkuId(){
		return realProductSkuId;
	}

	public void setRealProductSkuId(Integer realProductSkuId){
		this.realProductSkuId = realProductSkuId;
	}

	public String getRealProductSnapshot(){
		return realProductSnapshot;
	}

	public void setRealProductSnapshot(String realProductSnapshot){
		this.realProductSnapshot = realProductSnapshot;
	}

	public Integer getRealProductCount(){
		return realProductCount;
	}

	public void setRealProductCount(Integer realProductCount){
		this.realProductCount = realProductCount;
	}

	public BigDecimal getProductAmount(){
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount){
		this.productAmount = productAmount;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
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