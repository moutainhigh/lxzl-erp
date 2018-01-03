package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterial;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderProduct;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.util.List;


public class TransferOrderDO  extends BaseDO {

	private Integer id;
	private String transferOrderNo;
	private String transferOrderName;
	private Integer transferOrderStatus;
	private Integer transferOrderMode;
	private Integer transferOrderType;
	private Integer warehouseId;
	private Integer dataStatus;
	private String remark;



	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getTransferOrderNo(){
		return transferOrderNo;
	}

	public void setTransferOrderNo(String transferOrderNo){
		this.transferOrderNo = transferOrderNo;
	}

	public String getTransferOrderName(){
		return transferOrderName;
	}

	public void setTransferOrderName(String transferOrderName){
		this.transferOrderName = transferOrderName;
	}

	public Integer getTransferOrderStatus(){
		return transferOrderStatus;
	}

	public void setTransferOrderStatus(Integer transferOrderStatus){
		this.transferOrderStatus = transferOrderStatus;
	}

	public Integer getTransferOrderMode(){
		return transferOrderMode;
	}

	public void setTransferOrderMode(Integer transferOrderMode){
		this.transferOrderMode = transferOrderMode;
	}

	public Integer getTransferOrderType(){
		return transferOrderType;
	}

	public void setTransferOrderType(Integer transferOrderType){
		this.transferOrderType = transferOrderType;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
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