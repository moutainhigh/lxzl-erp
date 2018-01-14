package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class TransferOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;
	private Integer productId;
	private Integer productSkuId;
	private Integer productCount;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;
	private String productSkuSnapshot ;// 商品冗余信息，防止商品修改留存快照

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
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

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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

	public String getProductSkuSnapshot() {
		return productSkuSnapshot;
	}

	public void setProductSkuSnapshot(String productSkuSnapshot) {
		this.productSkuSnapshot = productSkuSnapshot;
	}
}