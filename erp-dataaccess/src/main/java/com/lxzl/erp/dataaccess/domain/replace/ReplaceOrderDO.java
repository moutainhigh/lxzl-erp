package com.lxzl.erp.dataaccess.domain.replace;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderDO  extends BaseDO {

	private Integer id;
	private String replaceOrderNo;
	private Integer orderId;
	private String orderNo;
	private Integer customerId;
	private String customerNo;
	private Date replaceTime;
	private Integer totalReplaceProductCount;
	private Integer totalReplaceMaterialCount;
	private Integer realTotalReplaceProductCount;
	private Integer realTotalReplaceMaterialCount;
	private BigDecimal serviceCost;
	private BigDecimal logisticsCost;
	private BigDecimal repairCost;
	private Integer replaceReasonType;
	private String replaceReason;
	private Integer replaceMode;
	private Integer replaceOrderStatus;
	private Integer dataStatus;
	private String remark;
	private Date confirmReplaceTime;
	private String confirmReplaceUser;
	private String address;   //换货地址
	private String consigneeName;   //联系人
	private String consigneePhone;   //联系电话
	private List<ReplaceOrderProductDO> replaceOrderProductDOList;
	private List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList;
	@Transient
	private String createUserName;   //添加人姓名
	@Transient
	private String updateUserName;   //修改人
	@Transient
	private String confirmReplaceUserName;   //确认换货人
	private Integer isReletOrderReplace;   //是否是续租单换货，1是0否
	private String reletOrderNo;   //续租单编号
	private String customerName;  //客户名称
	private String deliverySubCompanyName;  //发货分公司名称
	private Integer deliverySubCompanyId;  //发货分公司
	private Date replaceDeliveryTime;   //换货单发货时间
	private Date realReplaceTime;   //实际换货时间
	private BigDecimal oldTotalCreditDepositAmount;   //换货前订单授信押金金额
	private BigDecimal newTotalCreditDepositAmount;   //换货后订单授信押金金额
	private BigDecimal updateTotalCreditDepositAmount;   //换货修改授信押金金额
	private String orderSellerId;//订单业务员ID
	private String orderSellerName;//订单业务员姓名
	private String orderSellerPhone;//订单业务员手机号
	private Date orderRentStartTime;   //订单起租时间
	private Date orderExpectReturnTime;   //订单预计归还时间
	private String originalOrderNo;  //原订单单号

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getReplaceOrderNo(){
		return replaceOrderNo;
	}

	public void setReplaceOrderNo(String replaceOrderNo){
		this.replaceOrderNo = replaceOrderNo;
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

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Date getReplaceTime(){
		return replaceTime;
	}

	public void setReplaceTime(Date replaceTime){
		this.replaceTime = replaceTime;
	}

	public Integer getTotalReplaceProductCount(){
		return totalReplaceProductCount;
	}

	public void setTotalReplaceProductCount(Integer totalReplaceProductCount){
		this.totalReplaceProductCount = totalReplaceProductCount;
	}

	public Integer getTotalReplaceMaterialCount(){
		return totalReplaceMaterialCount;
	}

	public void setTotalReplaceMaterialCount(Integer totalReplaceMaterialCount){
		this.totalReplaceMaterialCount = totalReplaceMaterialCount;
	}

	public Integer getRealTotalReplaceProductCount(){
		return realTotalReplaceProductCount;
	}

	public void setRealTotalReplaceProductCount(Integer realTotalReplaceProductCount){
		this.realTotalReplaceProductCount = realTotalReplaceProductCount;
	}

	public Integer getRealTotalReplaceMaterialCount(){
		return realTotalReplaceMaterialCount;
	}

	public void setRealTotalReplaceMaterialCount(Integer realTotalReplaceMaterialCount){
		this.realTotalReplaceMaterialCount = realTotalReplaceMaterialCount;
	}

	public BigDecimal getServiceCost(){
		return serviceCost;
	}

	public void setServiceCost(BigDecimal serviceCost){
		this.serviceCost = serviceCost;
	}

	public BigDecimal getLogisticsCost(){
		return logisticsCost;
	}

	public void setLogisticsCost(BigDecimal logisticsCost){
		this.logisticsCost = logisticsCost;
	}

	public BigDecimal getRepairCost(){
		return repairCost;
	}

	public void setRepairCost(BigDecimal repairCost){
		this.repairCost = repairCost;
	}

	public Integer getReplaceReasonType(){
		return replaceReasonType;
	}

	public void setReplaceReasonType(Integer replaceReasonType){
		this.replaceReasonType = replaceReasonType;
	}

	public String getReplaceReason(){
		return replaceReason;
	}

	public void setReplaceReason(String replaceReason){
		this.replaceReason = replaceReason;
	}

	public Integer getReplaceMode(){
		return replaceMode;
	}

	public void setReplaceMode(Integer replaceMode){
		this.replaceMode = replaceMode;
	}

	public Integer getReplaceOrderStatus(){
		return replaceOrderStatus;
	}

	public void setReplaceOrderStatus(Integer replaceOrderStatus){
		this.replaceOrderStatus = replaceOrderStatus;
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

	public Date getConfirmReplaceTime(){
		return confirmReplaceTime;
	}

	public void setConfirmReplaceTime(Date confirmReplaceTime){
		this.confirmReplaceTime = confirmReplaceTime;
	}

	public String getConfirmReplaceUser(){
		return confirmReplaceUser;
	}

	public void setConfirmReplaceUser(String confirmReplaceUser){
		this.confirmReplaceUser = confirmReplaceUser;
	}

	public List<ReplaceOrderProductDO> getReplaceOrderProductDOList() {
		return replaceOrderProductDOList;
	}

	public void setReplaceOrderProductDOList(List<ReplaceOrderProductDO> replaceOrderProductDOList) {
		this.replaceOrderProductDOList = replaceOrderProductDOList;
	}

	public List<ReplaceOrderMaterialDO> getReplaceOrderMaterialDOList() {
		return replaceOrderMaterialDOList;
	}

	public void setReplaceOrderMaterialDOList(List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList) {
		this.replaceOrderMaterialDOList = replaceOrderMaterialDOList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getConfirmReplaceUserName() {
		return confirmReplaceUserName;
	}

	public void setConfirmReplaceUserName(String confirmReplaceUserName) {
		this.confirmReplaceUserName = confirmReplaceUserName;
	}

	public Integer getIsReletOrderReplace() {
		return isReletOrderReplace;
	}

	public void setIsReletOrderReplace(Integer isReletOrderReplace) {
		this.isReletOrderReplace = isReletOrderReplace;
	}

	public String getReletOrderNo() {
		return reletOrderNo;
	}

	public void setReletOrderNo(String reletOrderNo) {
		this.reletOrderNo = reletOrderNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeliverySubCompanyName() {
		return deliverySubCompanyName;
	}

	public void setDeliverySubCompanyName(String deliverySubCompanyName) {
		this.deliverySubCompanyName = deliverySubCompanyName;
	}

	public Integer getDeliverySubCompanyId() {
		return deliverySubCompanyId;
	}

	public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
		this.deliverySubCompanyId = deliverySubCompanyId;
	}

	public Date getReplaceDeliveryTime() {
		return replaceDeliveryTime;
	}

	public void setReplaceDeliveryTime(Date replaceDeliveryTime) {
		this.replaceDeliveryTime = replaceDeliveryTime;
	}

	public Date getRealReplaceTime() {
		return realReplaceTime;
	}

	public void setRealReplaceTime(Date realReplaceTime) {
		this.realReplaceTime = realReplaceTime;
	}

	public BigDecimal getOldTotalCreditDepositAmount() {
		return oldTotalCreditDepositAmount;
	}

	public void setOldTotalCreditDepositAmount(BigDecimal oldTotalCreditDepositAmount) {
		this.oldTotalCreditDepositAmount = oldTotalCreditDepositAmount;
	}

	public BigDecimal getNewTotalCreditDepositAmount() {
		return newTotalCreditDepositAmount;
	}

	public void setNewTotalCreditDepositAmount(BigDecimal newTotalCreditDepositAmount) {
		this.newTotalCreditDepositAmount = newTotalCreditDepositAmount;
	}

	public BigDecimal getUpdateTotalCreditDepositAmount() {
		return updateTotalCreditDepositAmount;
	}

	public void setUpdateTotalCreditDepositAmount(BigDecimal updateTotalCreditDepositAmount) {
		this.updateTotalCreditDepositAmount = updateTotalCreditDepositAmount;
	}

	public String getOrderSellerId() {
		return orderSellerId;
	}

	public void setOrderSellerId(String orderSellerId) {
		this.orderSellerId = orderSellerId;
	}

	public String getOrderSellerName() {
		return orderSellerName;
	}

	public void setOrderSellerName(String orderSellerName) {
		this.orderSellerName = orderSellerName;
	}

	public String getOrderSellerPhone() {
		return orderSellerPhone;
	}

	public void setOrderSellerPhone(String orderSellerPhone) {
		this.orderSellerPhone = orderSellerPhone;
	}

	public Date getOrderRentStartTime() {
		return orderRentStartTime;
	}

	public void setOrderRentStartTime(Date orderRentStartTime) {
		this.orderRentStartTime = orderRentStartTime;
	}

	public Date getOrderExpectReturnTime() {
		return orderExpectReturnTime;
	}

	public void setOrderExpectReturnTime(Date orderExpectReturnTime) {
		this.orderExpectReturnTime = orderExpectReturnTime;
	}

	public String getOriginalOrderNo() {
		return originalOrderNo;
	}

	public void setOriginalOrderNo(String originalOrderNo) {
		this.originalOrderNo = originalOrderNo;
	}
}