package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class MaterialModelDO  extends BaseDO {

	private Integer id;
	private Integer materialType;
	private String modelName;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getMaterialType(){
		return materialType;
	}

	public void setMaterialType(Integer materialType){
		this.materialType = materialType;
	}

	public String getModelName(){
		return modelName;
	}

	public void setModelName(String modelName){
		this.modelName = modelName;
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