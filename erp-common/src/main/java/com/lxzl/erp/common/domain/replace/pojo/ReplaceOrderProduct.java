package com.lxzl.erp.common.domain.replace.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderProduct extends BasePO {

	private Integer replaceOrderProductId;   //唯一标识
	private Integer replaceOrderId;   //换货单ID
	private String replaceOrderNo;   //换货编号
	private Integer oldOrderProductId;   //原订单商品项id
	private Integer oldProductEntry;   //原订单行号
	private Integer newOrderProductId;   //新订单商品项id
	private Integer rentType;   //租赁方式，1按天租，2按月租
	private Integer rentTimeLength;   //租赁期限
	private Integer rentLengthType;   //租赁期限类型，1短租，2长租
	private Integer depositCycle;   //押金期数
	private Integer paymentCycle;   //付款期数
	private Integer payMode;   //支付方式：1先用后付，2先付后用
	private BigDecimal oldProductUnitAmount;   //原商品单价
	private Integer productId;   //商品ID
	private String productName;   //商品名称
	private Integer productSkuId;   //商品SKU ID
	private String productSkuName;   //商品SKU名称
	private Integer productCount;   //商品总数
	private BigDecimal productUnitAmount;   //商品单价
	private BigDecimal rentDepositAmount;   //租金押金金额
	private BigDecimal depositAmount;   //设备押金金额
	private BigDecimal creditDepositAmount;   //授信押金金额
	private Integer isNewProduct;   //是否是全新机，1是0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer realReplaceProductCount;   //实际换货数量
	private Integer isReletOrderReplace;   //是否是续租单换货，1是0否
	private Integer reletOrderItemId;   //续租项ID


	public Integer getReplaceOrderProductId(){
		return replaceOrderProductId;
	}

	public void setReplaceOrderProductId(Integer replaceOrderProductId){
		this.replaceOrderProductId = replaceOrderProductId;
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

	public Integer getOldOrderProductId(){
		return oldOrderProductId;
	}

	public void setOldOrderProductId(Integer oldOrderProductId){
		this.oldOrderProductId = oldOrderProductId;
	}

	public Integer getOldProductEntry(){
		return oldProductEntry;
	}

	public void setOldProductEntry(Integer oldProductEntry){
		this.oldProductEntry = oldProductEntry;
	}

	public Integer getNewOrderProductId(){
		return newOrderProductId;
	}

	public void setNewOrderProductId(Integer newOrderProductId){
		this.newOrderProductId = newOrderProductId;
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