package com.lxzl.erp.dataaccess.domain.assembleOder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class AssembleOrderProductEquipmentDO  extends BaseDO {

	private Integer id;
	private Integer assembleOrderId;
	private String productEquipmentNo;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getAssembleOrderId(){
		return assembleOrderId;
	}

	public void setAssembleOrderId(Integer assembleOrderId){
		this.assembleOrderId = assembleOrderId;
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