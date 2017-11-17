package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderProduct implements Serializable {

	private Integer purchaseReceiveOrderProductId;   //唯一标识
	private Integer purchaseReceiveOrderId;   //采购单ID
	private Integer purchaseOrderProductId;   //采购单项ID
	private Integer purchaseDeliveryOrderProductId;   //采购发货单项ID
	private Integer productId;   //商品ID，来源（采购发货单），不可变更
	private Integer productSkuId;   //商品SKU ID 不可变更
	private String productName;   //商品名称冗余，不可修改
	private String productSnapshot;   //商品冗余信息，防止商品修改留存快照，不可修改
	private Integer productCount;   //商品总数，来源（采购发货单），不可变更
	private Integer realProductId;   //实际商品ID
	private String realProductName;   //商品名称冗余，可修改
	private Integer realProductSkuId;   //商品SKU ID
	private String realProductSnapshot;   //商品冗余信息，防止商品修改留存快照，可修改
	private Integer realProductCount;   //实际商品总数
	private Integer isSrc;   //原单项标志，查原单时此标志要传入0，0-收货新添项，1-原单项
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<ProductMaterial> productMaterialList;

	public Integer getPurchaseReceiveOrderProductId(){
		return purchaseReceiveOrderProductId;
	}

	public void setPurchaseReceiveOrderProductId(Integer purchaseReceiveOrderProductId){
		this.purchaseReceiveOrderProductId = purchaseReceiveOrderProductId;
	}

	public Integer getPurchaseReceiveOrderId(){
		return purchaseReceiveOrderId;
	}

	public void setPurchaseReceiveOrderId(Integer purchaseReceiveOrderId){
		this.purchaseReceiveOrderId = purchaseReceiveOrderId;
	}

	public Integer getPurchaseOrderProductId(){
		return purchaseOrderProductId;
	}

	public void setPurchaseOrderProductId(Integer purchaseOrderProductId){
		this.purchaseOrderProductId = purchaseOrderProductId;
	}

	public Integer getPurchaseDeliveryOrderProductId(){
		return purchaseDeliveryOrderProductId;
	}

	public void setPurchaseDeliveryOrderProductId(Integer purchaseDeliveryOrderProductId){
		this.purchaseDeliveryOrderProductId = purchaseDeliveryOrderProductId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductName(String productName){
		this.productName = productName;
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

	public Integer getIsSrc(){
		return isSrc;
	}

	public void setIsSrc(Integer isSrc){
		this.isSrc = isSrc;
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

	public List<ProductMaterial> getProductMaterialList() {
		return productMaterialList;
	}

	public void setProductMaterialList(List<ProductMaterial> productMaterialList) {
		this.productMaterialList = productMaterialList;
	}
}