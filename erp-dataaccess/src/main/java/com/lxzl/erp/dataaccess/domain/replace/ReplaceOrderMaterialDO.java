package com.lxzl.erp.dataaccess.domain.replace;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer replaceOrderId;
	private String replaceOrderNo;
	private Integer oldOrderMaterialId;
	private Integer oldMaterialEntry;
	private Integer newOrderMaterialId;
	private Integer rentType;
	private Integer rentTimeLength;
	private Integer rentLengthType;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;
	private BigDecimal oldMaterialUnitAmount;
	private Integer materialId;
	private String materialName;
	private Integer materialCount;
	private BigDecimal materialUnitAmount;
	private BigDecimal rentDepositAmount;
	private BigDecimal depositAmount;
	private BigDecimal creditDepositAmount;
	private Integer isNewMaterial;
	private Integer dataStatus;
	private String remark;
	private Integer realReplaceMaterialCount;
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