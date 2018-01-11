package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class TransferOrderProductEquipmentDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;
	private Integer transferOrderProductId;
	private String productEquipmentNo;
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

	public Integer getTransferOrderProductId(){
		return transferOrderProductId;
	}

	public void setTransferOrderProductId(Integer transferOrderProductId){
		this.transferOrderProductId = transferOrderProductId;
	}

	public String getProductEquipmentNo(){
		return productEquipmentNo;
	}

	public void setProductEquipmentNo(String productEquipmentNo){
		this.productEquipmentNo = productEquipmentNo;
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