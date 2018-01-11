package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class TransferOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;
	private Integer materialId;
	private String materialNo;
	private Integer materialCount;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;

	@Transient
	private List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList;


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

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialNo(){
		return materialNo;
	}

	public void setMaterialNo(String materialNo){
		this.materialNo = materialNo;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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

	public List<TransferOrderMaterialBulkDO> getTransferOrderMaterialBulkDOList() {
		return transferOrderMaterialBulkDOList;
	}

	public void setTransferOrderMaterialBulkDOList(List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList) {
		this.transferOrderMaterialBulkDOList = transferOrderMaterialBulkDOList;
	}
}