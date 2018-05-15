package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

public class OrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private Integer rentType;
	private Integer rentTimeLength;
	private Integer rentLengthType;
	private Integer materialId;
	private String materialName;
	private Integer materialCount;
	private BigDecimal materialUnitAmount;
	private BigDecimal materialAmount;
	private BigDecimal rentDepositAmount;
	private BigDecimal depositAmount;
	private BigDecimal creditDepositAmount;
	private BigDecimal insuranceAmount;
	private String materialSnapshot;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;
	private Integer isNewMaterial;
	private Integer rentingMaterialCount;
	private Integer dataStatus;
	private String remark;

	private String serialNumber;        // 序号

	private Integer orderJointProductId; // 订单组合商品id
	@Transient
	private Integer identityNo; // 标识号，只在业务逻辑处理时使用

	// 以下为K3的数据字段
	private Integer FEntryID;
	private String productNumber;

	public Integer getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(Integer identityNo) {
		this.identityNo = identityNo;
	}

	public Integer getOrderJointProductId() {
		return orderJointProductId;
	}

	public void setOrderJointProductId(Integer orderJointProductId) {
		this.orderJointProductId = orderJointProductId;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
	}

	public BigDecimal getMaterialUnitAmount(){
		return materialUnitAmount;
	}

	public void setMaterialUnitAmount(BigDecimal materialUnitAmount){
		this.materialUnitAmount = materialUnitAmount;
	}

	public BigDecimal getMaterialAmount(){
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount){
		this.materialAmount = materialAmount;
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

	public BigDecimal getInsuranceAmount(){
		return insuranceAmount;
	}

	public void setInsuranceAmount(BigDecimal insuranceAmount){
		this.insuranceAmount = insuranceAmount;
	}

	public String getMaterialSnapshot(){
		return materialSnapshot;
	}

	public void setMaterialSnapshot(String materialSnapshot){
		this.materialSnapshot = materialSnapshot;
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

	public Integer getRentType() {
		return rentType;
	}

	public void setRentType(Integer rentType) {
		this.rentType = rentType;
	}

	public Integer getRentTimeLength() {
		return rentTimeLength;
	}

	public void setRentTimeLength(Integer rentTimeLength) {
		this.rentTimeLength = rentTimeLength;
	}

	public Integer getDepositCycle() {
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle) {
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Integer getIsNewMaterial() {
		return isNewMaterial;
	}

	public void setIsNewMaterial(Integer isNewMaterial) {
		this.isNewMaterial = isNewMaterial;
	}

	public BigDecimal getRentDepositAmount() {
		return rentDepositAmount;
	}

	public void setRentDepositAmount(BigDecimal rentDepositAmount) {
		this.rentDepositAmount = rentDepositAmount;
	}

	public Integer getRentLengthType() {
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType) {
		this.rentLengthType = rentLengthType;
	}

	public Integer getRentingMaterialCount() {
		return rentingMaterialCount;
	}

	public void setRentingMaterialCount(Integer rentingMaterialCount) {
		this.rentingMaterialCount = rentingMaterialCount;
	}

	public Integer getFEntryID() {
		return FEntryID;
	}

	public void setFEntryID(Integer FEntryID) {
		this.FEntryID = FEntryID;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getSerialNumber() { return serialNumber; }

	public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
}