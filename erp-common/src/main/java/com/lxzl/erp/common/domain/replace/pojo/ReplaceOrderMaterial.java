package com.lxzl.erp.common.domain.replace.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderMaterial extends BasePO {

	private Integer replaceOrderMaterialId;   //唯一标识
	private Integer replaceOrderId;   //换货单ID
	private String replaceOrderNo;   //换货编号
	private Integer oldOrderMaterialId;   //原订单配件项id
	private Integer oldMaterialEntry;   //原订单行号
	private Integer newOrderMaterialId;   //新订单配件项id
	private Integer rentType;   //租赁方式，1按天租，2按月租
	private Integer rentTimeLength;   //租赁期限
	private Integer rentLengthType;   //租赁期限类型，1短租，2长租
	private Integer depositCycle;   //押金期数
	private Integer paymentCycle;   //付款期数
	private Integer payMode;   //支付方式：1先用后付，2先付后用
	private BigDecimal oldMaterialUnitAmount;   //原配件单价
	private Integer materialId;   //配件ID
	private String materialName;   //配件名称
	private Integer materialCount;   //配件总数
	private BigDecimal materialUnitAmount;   //配件单价
	private BigDecimal rentDepositAmount;   //租金押金金额
	private BigDecimal depositAmount;   //设备押金金额
	private BigDecimal creditDepositAmount;   //授信押金金额
	private Integer isNewMaterial;   //是否是全新机，1是0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer realReplaceMaterialCount;   //实际换货数量
	private Integer isReletOrderReplace;   //是否是续租单换货，1是0否
	private Integer reletOrderItemId;   //续租项ID


	public Integer getReplaceOrderMaterialId(){
		return replaceOrderMaterialId;
	}

	public void setReplaceOrderMaterialId(Integer replaceOrderMaterialId){
		this.replaceOrderMaterialId = replaceOrderMaterialId;
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

	public Integer getOldOrderMaterialId(){
		return oldOrderMaterialId;
	}

	public void setOldOrderMaterialId(Integer oldOrderMaterialId){
		this.oldOrderMaterialId = oldOrderMaterialId;
	}

	public Integer getOldMaterialEntry(){
		return oldMaterialEntry;
	}

	public void setOldMaterialEntry(Integer oldMaterialEntry){
		this.oldMaterialEntry = oldMaterialEntry;
	}

	public Integer getNewOrderMaterialId(){
		return newOrderMaterialId;
	}

	public void setNewOrderMaterialId(Integer newOrderMaterialId){
		this.newOrderMaterialId = newOrderMaterialId;
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

	public BigDecimal getOldMaterialUnitAmount(){
		return oldMaterialUnitAmount;
	}

	public void setOldMaterialUnitAmount(BigDecimal oldMaterialUnitAmount){
		this.oldMaterialUnitAmount = oldMaterialUnitAmount;
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

	public Integer getIsNewMaterial(){
		return isNewMaterial;
	}

	public void setIsNewMaterial(Integer isNewMaterial){
		this.isNewMaterial = isNewMaterial;
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

	public Integer getRealReplaceMaterialCount(){
		return realReplaceMaterialCount;
	}

	public void setRealReplaceMaterialCount(Integer realReplaceMaterialCount){
		this.realReplaceMaterialCount = realReplaceMaterialCount;
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