package com.lxzl.erp.dataaccess.domain.replace;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class ReplaceOrderDetailDO  extends BaseDO {

	private Integer id;
	private Integer replaceOrderId;
	private String replaceOrderNo;
	private Integer orderItemType;
	private Integer oldOrderItemId;
	private Integer oldOrderEntry;
	private Integer newOrderItemId;
	private Integer rentType;
	private Integer rentTimeLength;
	private Integer rentLengthType;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;
	private BigDecimal oldProductUnitAmount;
	private Integer productId;
	private String productName;
	private Integer productSkuId;
	private String productSkuName;
	private Integer productCount;
	private BigDecimal productUnitAmount;
	private BigDecimal rentDepositAmount;
	private BigDecimal depositAmount;
	private BigDecimal creditDepositAmount;
	private Integer isNewProduct;
	private Integer dataStatus;
	private String remark;
	private Integer realReplaceProductCount;
	private Integer isReletOrderReplace;
	private Integer reletOrderItemId;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getReplaceOrderId(){
		return replaceOrderId;
	}

	public void setReplaceOrderId(Integer replaceOrderId){
		this.replaceOrderId = replaceOrderId;
	}

	public String getReplaceOrderNo(){
		return replaceOrderNo;
	}

	public void setReplaceOrderNo(String replaceOrderNo){
		this.replaceOrderNo = replaceOrderNo;
	}

	public Integer getOrderItemType(){
		return orderItemType;
	}

	public void setOrderItemType(Integer orderItemType){
		this.orderItemType = orderItemType;
	}

	public Integer getOldOrderItemId(){
		return oldOrderItemId;
	}

	public void setOldOrderItemId(Integer oldOrderItemId){
		this.oldOrderItemId = oldOrderItemId;
	}

	public Integer getOldOrderEntry(){
		return oldOrderEntry;
	}

	public void setOldOrderEntry(Integer oldOrderEntry){
		this.oldOrderEntry = oldOrderEntry;
	}

	public Integer getNewOrderItemId(){
		return newOrderItemId;
	}

	public void setNewOrderItemId(Integer newOrderItemId){
		this.newOrderItemId = newOrderItemId;
	}

	public Integer getRentType(){
		return rentType;
	}

	public void setRentType(Integer rentType){
		this.rentType = rentType;
	}

	public Integer getRentTimeLength(){
		return rentTimeLength;
	}

	public void setRentTimeLength(Integer rentTimeLength){
		this.rentTimeLength = rentTimeLength;
	}

	public Integer getRentLengthType(){
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType){
		this.rentLengthType = rentLengthType;
	}

	public Integer getDepositCycle(){
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle){
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode(){
		return payMode;
	}

	public void setPayMode(Integer payMode){
		this.payMode = payMode;
	}

	public BigDecimal getOldProductUnitAmount(){
		return oldProductUnitAmount;
	}

	public void setOldProductUnitAmount(BigDecimal oldProductUnitAmount){
		this.oldProductUnitAmount = oldProductUnitAmount;
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

	public String getProductSkuName(){
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName){
		this.productSkuName = productSkuName;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
	}

	public BigDecimal getProductUnitAmount(){
		return productUnitAmount;
	}

	public void setProductUnitAmount(BigDecimal productUnitAmount){
		this.productUnitAmount = productUnitAmount;
	}

	public BigDecimal getRentDepositAmount(){
		return rentDepositAmount;
	}

	public void setRentDepositAmount(BigDecimal rentDepositAmount){
		this.rentDepositAmount = rentDepositAmount;
	}

	public BigDecimal getDepositAmount(){
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount){
		this.depositAmount = depositAmount;
	}

	public BigDecimal getCreditDepositAmount(){
		return creditDepositAmount;
	}

	public void setCreditDepositAmount(BigDecimal creditDepositAmount){
		this.creditDepositAmount = creditDepositAmount;
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

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Integer getRealReplaceProductCount(){
		return realReplaceProductCount;
	}

	public void setRealReplaceProductCount(Integer realReplaceProductCount){
		this.realReplaceProductCount = realReplaceProductCount;
	}

	public Integer getIsReletOrderReplace(){
		return isReletOrderReplace;
	}

	public void setIsReletOrderReplace(Integer isReletOrderReplace){
		this.isReletOrderReplace = isReletOrderReplace;
	}

	public Integer getReletOrderItemId(){
		return reletOrderItemId;
	}

	public void setReletOrderItemId(Integer reletOrderItemId){
		this.reletOrderItemId = reletOrderItemId;
	}

}