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
}