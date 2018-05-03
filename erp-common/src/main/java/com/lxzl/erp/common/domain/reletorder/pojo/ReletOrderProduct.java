package com.lxzl.erp.common.domain.reletorder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;

import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrderProduct extends BasePO {

	private Integer reletOrderProductId;   //唯一标识
	private Integer reletOrderId;   //续租订单ID
	private String reletOrderNo;   //续租订单编号
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Integer orderProductId;   //订单商品项ID
	private Integer productId;   //商品ID
	private String productName;   //商品名称
	private Integer productSkuId;   //商品SKU ID
	private String productSkuName;   //商品SKU名称
	private Integer productCount;   //商品总数
	private BigDecimal productUnitAmount;   //商品单价
	private BigDecimal productAmount;   //商品价格
	private String productSkuSnapshot;   //商品冗余信息，防止商品修改留存快照
	private Integer paymentCycle;   //付款期数
	private Integer payMode;   //支付方式：1先用后付，2先付后用
	private Integer isNewProduct;   //是否是全新机，1是0否
	private Integer rentingProductCount;   //在租商品总数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	public ReletOrderProduct(){

	}

	public ReletOrderProduct(OrderProduct orderProduct, String orderNo){
		this.orderNo = orderNo;
		this.orderId = orderProduct.getOrderId();
		this.orderProductId = orderProduct.getOrderProductId();
		this.productId = orderProduct.getProductId();
		this.productName = orderProduct.getProductName();
		this.productSkuId = orderProduct.getProductSkuId();
		this.productSkuName = orderProduct.getProductSkuName();
		this.productCount = orderProduct.getProductCount();
//		this.materialSnapshot = orderMaterial.getMaterialSnapshot();
		this.productUnitAmount = orderProduct.getProductUnitAmount();
		this.productAmount = orderProduct.getProductAmount();
		this.paymentCycle = orderProduct.getPaymentCycle();
		this.payMode = orderProduct.getPayMode();
		this.isNewProduct = orderProduct.getIsNewProduct();
		this.rentingProductCount = orderProduct.getRentingProductCount();
	}

	public Integer getReletOrderProductId(){
		return reletOrderProductId;
	}

	public void setReletOrderProductId(Integer reletOrderProductId){
		this.reletOrderProductId = reletOrderProductId;
	}

	public Integer getReletOrderId(){
		return reletOrderId;
	}

	public void setReletOrderId(Integer reletOrderId){
		this.reletOrderId = reletOrderId;
	}

	public String getReletOrderNo(){
		return reletOrderNo;
	}

	public void setReletOrderNo(String reletOrderNo){
		this.reletOrderNo = reletOrderNo;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
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

	public BigDecimal getProductAmount(){
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount){
		this.productAmount = productAmount;
	}

	public String getProductSkuSnapshot(){
		return productSkuSnapshot;
	}

	public void setProductSkuSnapshot(String productSkuSnapshot){
		this.productSkuSnapshot = productSkuSnapshot;
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

	public Integer getIsNewProduct(){
		return isNewProduct;
	}

	public void setIsNewProduct(Integer isNewProduct){
		this.isNewProduct = isNewProduct;
	}

	public Integer getRentingProductCount(){
		return rentingProductCount;
	}

	public void setRentingProductCount(Integer rentingProductCount){
		this.rentingProductCount = rentingProductCount;
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

}