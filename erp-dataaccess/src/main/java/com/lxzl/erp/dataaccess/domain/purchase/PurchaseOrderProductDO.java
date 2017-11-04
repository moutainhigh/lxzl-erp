package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.math.BigDecimal;

public class PurchaseOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer purchaseOrderId;
	private Integer productId;
	private String productName;
	private String productSnapshot;
	private Integer productSkuId;
	private Integer productCount;
	private BigDecimal productAmount;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
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

	public String getProductSnapshot() {
		return productSnapshot;
	}

	public void setProductSnapshot(String productSnapshot) {
		this.productSnapshot = productSnapshot;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
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

}