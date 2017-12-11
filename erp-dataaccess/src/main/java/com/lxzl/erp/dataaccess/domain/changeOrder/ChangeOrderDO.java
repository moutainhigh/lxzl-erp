package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class ChangeOrderDO  extends BaseDO {

	private Integer id;
	private String changeOrderNo;
	private Integer customerId;
	private String customerNo;
	private Integer totalChangeProductCount;
	private Integer totalChangeMaterialCount;
	private Integer realTotalChangeProductCount;
	private Integer realTotalChangeMaterialCount;
	private BigDecimal totalDifferencePrice;
	private BigDecimal serviceCost;
	private BigDecimal damageCost;
	private Integer isDamage;
	private Integer changeReasonType;
	private String changeReason;
	private Integer changeMode;
	private Integer changeOrderStatus;
	private Integer dataStatus;
	private String remark;
	private Integer owner;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
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

	public Integer getTotalChangeProductCount(){
		return totalChangeProductCount;
	}

	public void setTotalChangeProductCount(Integer totalChangeProductCount){
		this.totalChangeProductCount = totalChangeProductCount;
	}

	public Integer getTotalChangeMaterialCount(){
		return totalChangeMaterialCount;
	}

	public void setTotalChangeMaterialCount(Integer totalChangeMaterialCount){
		this.totalChangeMaterialCount = totalChangeMaterialCount;
	}

	public Integer getRealTotalChangeProductCount(){
		return realTotalChangeProductCount;
	}

	public void setRealTotalChangeProductCount(Integer realTotalChangeProductCount){
		this.realTotalChangeProductCount = realTotalChangeProductCount;
	}

	public Integer getRealTotalChangeMaterialCount(){
		return realTotalChangeMaterialCount;
	}

	public void setRealTotalChangeMaterialCount(Integer realTotalChangeMaterialCount){
		this.realTotalChangeMaterialCount = realTotalChangeMaterialCount;
	}

	public BigDecimal getTotalDifferencePrice(){
		return totalDifferencePrice;
	}

	public void setTotalDifferencePrice(BigDecimal totalDifferencePrice){
		this.totalDifferencePrice = totalDifferencePrice;
	}

	public BigDecimal getServiceCost(){
		return serviceCost;
	}

	public void setServiceCost(BigDecimal serviceCost){
		this.serviceCost = serviceCost;
	}

	public BigDecimal getDamageCost(){
		return damageCost;
	}

	public void setDamageCost(BigDecimal damageCost){
		this.damageCost = damageCost;
	}

	public Integer getIsDamage(){
		return isDamage;
	}

	public void setIsDamage(Integer isDamage){
		this.isDamage = isDamage;
	}

	public Integer getChangeReasonType(){
		return changeReasonType;
	}

	public void setChangeReasonType(Integer changeReasonType){
		this.changeReasonType = changeReasonType;
	}

	public String getChangeReason(){
		return changeReason;
	}

	public void setChangeReason(String changeReason){
		this.changeReason = changeReason;
	}

	public Integer getChangeMode(){
		return changeMode;
	}

	public void setChangeMode(Integer changeMode){
		this.changeMode = changeMode;
	}

	public Integer getChangeOrderStatus(){
		return changeOrderStatus;
	}

	public void setChangeOrderStatus(Integer changeOrderStatus){
		this.changeOrderStatus = changeOrderStatus;
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

}