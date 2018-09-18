package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointMaterial;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderMaterial extends BasePO {

	private Integer orderMaterialId;   //唯一标识
	private Integer orderId;   //订单ID
	private Integer rentType;	// 租赁类型
	private Integer rentTimeLength;		// 租赁时长
	private Integer rentLengthType;
	private Integer materialId;   //物料ID
	private String materialName;   //物料名称
	private Integer materialCount;   //物料总数
	private Integer stableMaterialCount;   //下单配件总数，该字段只在订单未提交时可变化
	private BigDecimal materialUnitAmount;   //物料单价
	private BigDecimal materialAmount;   //物料价格
	private BigDecimal rentDepositAmount;	// 租赁押金
	private BigDecimal depositAmount;   //押金金额
	private BigDecimal creditDepositAmount;   //授信押金金额
	private BigDecimal insuranceAmount;   //保险金额
	private String materialSnapshot;   //物料冗余信息，防止商品修改留存快照
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;
	private Integer isNewMaterial;
	private Integer rentingMaterialCount; //在租配件总数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	private Integer isItemDelivered; //是否已发货，0否1是

	private List<OrderMaterialBulk> orderMaterialBulkList;

	private BigDecimal firstNeedPayAmount;
	private BigDecimal firstNeedPayRentAmount;
	private BigDecimal firstNeedPayDepositAmount;      // 首付押金金额

	private String serialNumber;        // 序号

	private Integer orderJointProductId; // 订单组合商品id
	private Integer jointMaterialId; // 关联的组合商品配件项id
	@Transient
	private JointMaterial jointMaterial;
	@Transient
	private Integer identityNo; // 标识号，只在组合商品业务逻辑处理时使用，不持久化

	private Integer testMachineOrderMaterialId; //测试机配件项id

	// 以下为K3的数据字段
	private Integer FEntryID;
	private String productNumber;

	public JointMaterial getJointMaterial() {
		return jointMaterial;
	}

	public void setJointMaterial(JointMaterial jointMaterial) {
		this.jointMaterial = jointMaterial;
	}

	public Integer getJointMaterialId() {
		return jointMaterialId;
	}

	public void setJointMaterialId(Integer jointMaterialId) {
		this.jointMaterialId = jointMaterialId;
	}

	public Integer getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(Integer identityNo) {
		this.identityNo = identityNo;
	}

	public Integer getOrderMaterialId(){
		return orderMaterialId;
	}

	public void setOrderMaterialId(Integer orderMaterialId){
		this.orderMaterialId = orderMaterialId;
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

	public List<OrderMaterialBulk> getOrderMaterialBulkList() {
		return orderMaterialBulkList;
	}

	public void setOrderMaterialBulkList(List<OrderMaterialBulk> orderMaterialBulkList) {
		this.orderMaterialBulkList = orderMaterialBulkList;
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

	public Integer getIsNewMaterial() {
		return isNewMaterial;
	}

	public void setIsNewMaterial(Integer isNewMaterial) {
		this.isNewMaterial = isNewMaterial;
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

	public BigDecimal getRentDepositAmount() {
		return rentDepositAmount;
	}

	public void setRentDepositAmount(BigDecimal rentDepositAmount) {
		this.rentDepositAmount = rentDepositAmount;
	}

	public BigDecimal getFirstNeedPayAmount() {
		return firstNeedPayAmount;
	}

	public void setFirstNeedPayAmount(BigDecimal firstNeedPayAmount) {
		this.firstNeedPayAmount = firstNeedPayAmount;
	}

	public Integer getRentLengthType() {
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType) {
		this.rentLengthType = rentLengthType;
	}

	public BigDecimal getFirstNeedPayRentAmount() {
		return firstNeedPayRentAmount;
	}

	public void setFirstNeedPayRentAmount(BigDecimal firstNeedPayRentAmount) { this.firstNeedPayRentAmount = firstNeedPayRentAmount; }

	public BigDecimal getFirstNeedPayDepositAmount() { return firstNeedPayDepositAmount; }

	public void setFirstNeedPayDepositAmount(BigDecimal firstNeedPayDepositAmount) { this.firstNeedPayDepositAmount = firstNeedPayDepositAmount; }

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

	public Integer getOrderJointProductId() {
		return orderJointProductId;
	}

	public void setOrderJointProductId(Integer orderJointProductId) {
		this.orderJointProductId = orderJointProductId;
	}

	public Integer getStableMaterialCount() {
		return stableMaterialCount;
	}

	public void setStableMaterialCount(Integer stableMaterialCount) {
		this.stableMaterialCount = stableMaterialCount;
	}

	public Integer getIsItemDelivered() {
		return isItemDelivered;
	}

	public void setIsItemDelivered(Integer isItemDelivered) {
		this.isItemDelivered = isItemDelivered;
	}

	public Integer getTestMachineOrderMaterialId() {
		return testMachineOrderMaterialId;
	}

	public void setTestMachineOrderMaterialId(Integer testMachineOrderMaterialId) {
		this.testMachineOrderMaterialId = testMachineOrderMaterialId;
	}
}