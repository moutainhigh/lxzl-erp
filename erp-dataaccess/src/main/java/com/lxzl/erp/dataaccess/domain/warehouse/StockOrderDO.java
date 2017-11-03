package com.lxzl.erp.dataaccess.domain.warehouse;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class StockOrderDO  extends BaseDO {

	private Integer id;
	private String stockOrderNo;
	private Integer operationType;
	private Integer causeType;
	private String referNo;
	private Integer orderStatus;
	private Integer srcWarehouseId;
	private Integer srcWarehousePositionId;
	private Integer targetWarehouseId;
	private Integer targetWarehousePositionId;
	private Integer owner;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getStockOrderNo(){
		return stockOrderNo;
	}

	public void setStockOrderNo(String stockOrderNo){
		this.stockOrderNo = stockOrderNo;
	}

	public Integer getOperationType(){
		return operationType;
	}

	public void setOperationType(Integer operationType){
		this.operationType = operationType;
	}

	public Integer getCauseType(){
		return causeType;
	}

	public void setCauseType(Integer causeType){
		this.causeType = causeType;
	}

	public String getReferNo(){
		return referNo;
	}

	public void setReferNo(String referNo){
		this.referNo = referNo;
	}

	public Integer getOrderStatus(){
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}

	public Integer getSrcWarehouseId(){
		return srcWarehouseId;
	}

	public void setSrcWarehouseId(Integer srcWarehouseId){
		this.srcWarehouseId = srcWarehouseId;
	}

	public Integer getSrcWarehousePositionId(){
		return srcWarehousePositionId;
	}

	public void setSrcWarehousePositionId(Integer srcWarehousePositionId){
		this.srcWarehousePositionId = srcWarehousePositionId;
	}

	public Integer getTargetWarehouseId(){
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(Integer targetWarehouseId){
		this.targetWarehouseId = targetWarehouseId;
	}

	public Integer getTargetWarehousePositionId(){
		return targetWarehousePositionId;
	}

	public void setTargetWarehousePositionId(Integer targetWarehousePositionId){
		this.targetWarehousePositionId = targetWarehousePositionId;
	}

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
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

}