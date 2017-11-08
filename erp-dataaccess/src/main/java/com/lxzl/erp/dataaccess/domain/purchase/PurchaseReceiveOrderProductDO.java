package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PurchaseReceiveOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer purchaseReceiveOrderId;
	private Integer purchaseOrderProductId;
	private Integer purchaseDeliveryOrderProductId;
	private Integer productId;
	private Integer productSkuId;
	private String productName;
	private String productSnapshot;
	private Integer productCount;
	private Integer realProductId;
	private String realProductName;
	private Integer realProductSkuId;
	private String realProductSnapshot;
	private Integer realProductCount;
	private Integer isSrc;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}