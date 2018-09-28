package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeOrderProduct extends BasePO {

	private Integer exchangeOrderProductId;   //
	private Integer exchangeOrderId;   //变更单id
	private Integer orderProductId;   //订单商品项id
	private String productName;   //商品名称
	private Integer productSkuId;   //商品SKU ID
	private String productSkuName;   //商品SKU名称
	private BigDecimal productUnitAmount;   //商品单价
	private BigDecimal oldProductUnitAmount;   //原商品单价
	private Integer isNewProduct;   //是否是全新机，1是0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除


	public Integer getExchangeOrderProductId(){
		return exchangeOrderProductId;
	}

	public void setExchangeOrderProductId(Integer exchangeOrderProductId){
		this.exchangeOrderProductId = exchangeOrderProductId;
	}

	public Integer getExchangeOrderId(){
		return exchangeOrderId;
	}

	public void setExchangeOrderId(Integer exchangeOrderId){
		this.exchangeOrderId = exchangeOrderId;
	}

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
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

	public String getProductSkuName(){
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName){
		this.productSkuName = productSkuName;
	}

	public BigDecimal getProductUnitAmount(){
		return productUnitAmount;
	}

	public void setProductUnitAmount(BigDecimal productUnitAmount){
		this.productUnitAmount = productUnitAmount;
	}

	public BigDecimal getOldProductUnitAmount(){
		return oldProductUnitAmount;
	}

	public void setOldProductUnitAmount(BigDecimal oldProductUnitAmount){
		this.oldProductUnitAmount = oldProductUnitAmount;
	}

	public Integer getIsNewProduct(){
		return isNewProduct;
	}

	public void setIsNewProduct(Integer isNewProduct){
		this.isNewProduct = isNewProduct;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}