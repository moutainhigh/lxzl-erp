package com.lxzl.erp.common.domain.reletorder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrder extends BasePO {

	private Integer reletOrderId;   //唯一标识
	private String reletOrderNo;   //续租订单编号
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Integer buyerCustomerId;   //购买人ID
	private String buyerCustomerNo;   //购买人编号
	private String buyerCustomerName;   //客户名称
	private Integer orderSubCompanyId;   //订单所属分公司
	private Integer deliverySubCompanyId;   //订单发货分公司
	private Integer rentType;   //租赁类型
	private Integer rentTimeLength;   //租赁时长
	private Integer rentLengthType;   //租赁时长类型
	private Date rentStartTime;   //起租时间
	private Integer totalProductCount;   //商品总数
	private BigDecimal totalProductAmount;   //商品总价
	private Integer totalMaterialCount;   //配件总数
	private BigDecimal totalMaterialAmount;   //配件总价
	private BigDecimal totalOrderAmount;   //订单总价，实际支付价格，商品金额+配件金额-优惠(无运费，区别与订单)
	private BigDecimal totalPaidOrderAmount;   //已经支付金额
	private Integer orderSellerId;   //订单销售员
	private Integer orderUnionSellerId;   //订单联合销售员
	private BigDecimal totalDiscountAmount;   //共计优惠金额
	private Integer reletOrderStatus;   //订单状态，0-待提交,4-审核中,8-续租中,12-部分归还,16-全部归还,20-取消,24-结束
	private Integer payStatus;   //支付状态，0未支付，1已支付，2已退款,3逾期中
	private Date payTime;   //支付时间
	private Date expectReturnTime;   //预计归还时间
	private Date actualReturnTime;   //实际归还时间，最后一件设备归还的时间
	private Integer highTaxRate;   //17%税率
	private Integer lowTaxRate;   //6%税率
	private Integer statementDate;   //结算时间（天），20和31两种情况，如果为空取系统设定
	private String buyerRemark;   //购买人备注
	private String productSummary;   //商品摘要
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Integer owner;   //数据归属人
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<ReletOrderProduct> reletOrderProductList;  //续租订单商品项
	private List<ReletOrderMaterial> reletOrderMaterialList;   //续租订单配件项

	private String orderSellerName;    // 业务员姓名

	private String orderSubCompanyName;

	private String deliverySubCompanyName;    // 发货所属分公司名称


	public ReletOrder(Order order){
		this.orderId = order.getOrderId();
		this.orderNo = order.getOrderNo();
		this.buyerCustomerId = order.getBuyerCustomerId();
		this.buyerCustomerNo = order.getBuyerCustomerNo();
		this.buyerCustomerName = order.getBuyerCustomerName();
		this.orderSubCompanyId = order.getOrderSubCompanyId();
		this.deliverySubCompanyId = order.getDeliverySubCompanyId();
		this.rentType = order.getRentType();
		this.rentTimeLength = order.getRentTimeLength();
		this.rentLengthType = order.getRentLengthType();
		this.rentStartTime = order.getExpectReturnTime(); //续租时间：订单预计归还时间
		this.totalProductCount = order.getTotalProductCount();
		this.totalProductAmount = order.getTotalProductAmount();
		this.totalMaterialCount = order.getTotalMaterialCount();
		this.totalMaterialAmount = order.getTotalMaterialAmount();
		this.totalOrderAmount = order.getTotalOrderAmount();
		//this.totalPaidOrderAmount = order.getTotalPaidOrderAmount();
		this.orderSellerId = order.getOrderSellerId();
		//this.orderUnionSellerId = order.getOrder();
		//this.totalDiscountAmount = order.getTotalDiscountAmount();
		//this.reletOrderStatus
		this.highTaxRate = order.getHighTaxRate();
		this.lowTaxRate = order.getLowTaxRate();
		this.statementDate = order.getStatementDate();
		this.buyerRemark = order.getBuyerRemark();
		this.productSummary = order.getProductSummary();
		this.remark = order.getRemark();

		reletOrderProductList = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {

			this.reletOrderProductList.clear();
			for (OrderProduct orderProduct : order.getOrderProductList()) {
				ReletOrderProduct reletOrderProduct = new ReletOrderProduct(orderProduct, order.getOrderNo());
				this.reletOrderProductList.add(reletOrderProduct);
			}
		}

		reletOrderMaterialList = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {

			this.reletOrderMaterialList.clear();
			for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
				ReletOrderMaterial reletOrderMaterial = new ReletOrderMaterial(orderMaterial, order.getOrderNo());
				this.reletOrderMaterialList.add(reletOrderMaterial);
			}
		}
	}

	public ReletOrder(){

	}

	public List<ReletOrderProduct> getReletOrderProductList() {
		return reletOrderProductList;
	}

	public void setReletOrderProductList(List<ReletOrderProduct> reletOrderProductList) {
		this.reletOrderProductList = reletOrderProductList;
	}

	public List<ReletOrderMaterial> getReletOrderMaterialList() {
		return reletOrderMaterialList;
	}

	public void setReletOrderMaterialList(List<ReletOrderMaterial> reletOrderMaterialList) {
		this.reletOrderMaterialList = reletOrderMaterialList;
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

	public Integer getBuyerCustomerId(){
		return buyerCustomerId;
	}

	public void setBuyerCustomerId(Integer buyerCustomerId){
		this.buyerCustomerId = buyerCustomerId;
	}

	public String getBuyerCustomerNo(){
		return buyerCustomerNo;
	}

	public void setBuyerCustomerNo(String buyerCustomerNo){
		this.buyerCustomerNo = buyerCustomerNo;
	}

	public String getBuyerCustomerName(){
		return buyerCustomerName;
	}

	public void setBuyerCustomerName(String buyerCustomerName){
		this.buyerCustomerName = buyerCustomerName;
	}

	public Integer getOrderSubCompanyId(){
		return orderSubCompanyId;
	}

	public void setOrderSubCompanyId(Integer orderSubCompanyId){
		this.orderSubCompanyId = orderSubCompanyId;
	}

	public Integer getDeliverySubCompanyId(){
		return deliverySubCompanyId;
	}

	public void setDeliverySubCompanyId(Integer deliverySubCompanyId){
		this.deliverySubCompanyId = deliverySubCompanyId;
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

	public Date getRentStartTime(){
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime){
		this.rentStartTime = rentStartTime;
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

	public BigDecimal getTotalPaidOrderAmount(){
		return totalPaidOrderAmount;
	}

	public void setTotalPaidOrderAmount(BigDecimal totalPaidOrderAmount){
		this.totalPaidOrderAmount = totalPaidOrderAmount;
	}

	public Integer getOrderSellerId(){
		return orderSellerId;
	}

	public void setOrderSellerId(Integer orderSellerId){
		this.orderSellerId = orderSellerId;
	}

	public Integer getOrderUnionSellerId(){
		return orderUnionSellerId;
	}

	public void setOrderUnionSellerId(Integer orderUnionSellerId){
		this.orderUnionSellerId = orderUnionSellerId;
	}

	public BigDecimal getTotalDiscountAmount(){
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount){
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Integer getReletOrderStatus(){
		return reletOrderStatus;
	}

	public void setReletOrderStatus(Integer reletOrderStatus){
		this.reletOrderStatus = reletOrderStatus;
	}

	public Integer getPayStatus(){
		return payStatus;
	}

	public void setPayStatus(Integer payStatus){
		this.payStatus = payStatus;
	}

	public Date getPayTime(){
		return payTime;
	}

	public void setPayTime(Date payTime){
		this.payTime = payTime;
	}

	public Date getExpectReturnTime(){
		return expectReturnTime;
	}

	public void setExpectReturnTime(Date expectReturnTime){
		this.expectReturnTime = expectReturnTime;
	}

	public Date getActualReturnTime(){
		return actualReturnTime;
	}

	public void setActualReturnTime(Date actualReturnTime){
		this.actualReturnTime = actualReturnTime;
	}

	public Integer getHighTaxRate(){
		return highTaxRate;
	}

	public void setHighTaxRate(Integer highTaxRate){
		this.highTaxRate = highTaxRate;
	}

	public Integer getLowTaxRate(){
		return lowTaxRate;
	}

	public void setLowTaxRate(Integer lowTaxRate){
		this.lowTaxRate = lowTaxRate;
	}


	public Integer getStatementDate(){
		return statementDate;
	}

	public void setStatementDate(Integer statementDate){
		this.statementDate = statementDate;
	}

	public String getBuyerRemark(){
		return buyerRemark;
	}

	public void setBuyerRemark(String buyerRemark){
		this.buyerRemark = buyerRemark;
	}

	public String getProductSummary(){
		return productSummary;
	}

	public void setProductSummary(String productSummary){
		this.productSummary = productSummary;
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

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
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


	public String getOrderSellerName() {
		return orderSellerName;
	}

	public void setOrderSellerName(String orderSellerName) {
		this.orderSellerName = orderSellerName;
	}

	public String getOrderSubCompanyName() {
		return orderSubCompanyName;
	}

	public void setOrderSubCompanyName(String orderSubCompanyName) {
		this.orderSubCompanyName = orderSubCompanyName;
	}

	public String getDeliverySubCompanyName() { return deliverySubCompanyName; }

	public void setDeliverySubCompanyName(String deliverySubCompanyName) { this.deliverySubCompanyName = deliverySubCompanyName; }

}