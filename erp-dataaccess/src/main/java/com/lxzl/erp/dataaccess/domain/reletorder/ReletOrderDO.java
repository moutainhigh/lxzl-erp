package com.lxzl.erp.dataaccess.domain.reletorder;


import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrderDO  extends BaseDO {

	private Integer id;
	private String reletOrderNo;
	private Integer orderId;
	private String orderNo;
	private Integer buyerCustomerId;
	private String buyerCustomerNo;
	private String buyerCustomerName;
	private Integer orderSubCompanyId;
	private Integer deliverySubCompanyId;
	private Integer rentType;
	private Integer rentTimeLength;
	private Integer rentLengthType;
	private Date rentStartTime;
	private Integer totalProductCount;
	private BigDecimal totalProductAmount;
	private Integer totalMaterialCount;
	private BigDecimal totalMaterialAmount;
	private BigDecimal totalOrderAmount;
	private BigDecimal totalPaidOrderAmount;
	private Integer orderSellerId;
	private Integer orderUnionSellerId;
	private BigDecimal totalDiscountAmount;
	private Integer reletOrderStatus;
	private Integer payStatus;
	private Date payTime;
	private Date expectReturnTime;
	private Date actualReturnTime;
	private Integer highTaxRate;
	private Integer lowTaxRate;
	private Integer statementDate;
	private String buyerRemark;
	private String productSummary;
	private Integer dataStatus;
	private String remark;
	private Integer owner;

	@Transient
	private String orderSellerName;
	@Transient
	private String orderSubCompanyName;
	@Transient
	private String deliverySubCompanyName;                         // 发货所属分公司名称

	private List<ReletOrderProductDO> reletOrderProductDOList;  //续租订单商品项
	private List<ReletOrderMaterialDO> reletOrderMaterialDOList;   //续租订单配件项

	public List<ReletOrderProductDO> getReletOrderProductDOList() {
		return reletOrderProductDOList;
	}

	public void setReletOrderProductDOList(List<ReletOrderProductDO> reletOrderProductDOList) {
		this.reletOrderProductDOList = reletOrderProductDOList;
	}

	public List<ReletOrderMaterialDO> getReletOrderMaterialDOList() {
		return reletOrderMaterialDOList;
	}

	public void setReletOrderMaterialDOList(List<ReletOrderMaterialDO> reletOrderMaterialDOList) {
		this.reletOrderMaterialDOList = reletOrderMaterialDOList;
	}


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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