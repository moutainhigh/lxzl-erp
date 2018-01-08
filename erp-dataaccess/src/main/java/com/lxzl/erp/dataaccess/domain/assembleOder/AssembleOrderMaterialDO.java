package com.lxzl.erp.dataaccess.domain.assembleOder;

import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class AssembleOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer assembleOrderId;
	private Integer materialId;
	private String materialNo;
	private Integer materialCount;
	private Integer dataStatus;
	private String remark;

	private MaterialDO materialDO;

	public MaterialDO getMaterialDO() {
		return materialDO;
	}

	public void setMaterialDO(MaterialDO materialDO) {
		this.materialDO = materialDO;
	}

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