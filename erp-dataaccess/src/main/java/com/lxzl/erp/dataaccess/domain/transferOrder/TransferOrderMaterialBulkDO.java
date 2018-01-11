package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class TransferOrderMaterialBulkDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;
	private Integer transferOrderMaterialId;
	private String bulkMaterialNo;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getTransferOrderMaterialId(){
		return transferOrderMaterialId;
	}

	public void setTransferOrderMaterialId(Integer transferOrderMaterialId){
		this.transferOrderMaterialId = transferOrderMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
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