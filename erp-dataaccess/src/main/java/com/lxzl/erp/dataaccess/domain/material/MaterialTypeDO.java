package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class MaterialTypeDO  extends BaseDO {

	private Integer id;
	private String materialTypeName;
	private Integer isMainMaterial;
	private Integer isCapacityMaterial;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getMaterialTypeName(){
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName){
		this.materialTypeName = materialTypeName;
	}

	public Integer getIsMainMaterial(){
		return isMainMaterial;
	}

	public void setIsMainMaterial(Integer isMainMaterial){
		this.isMainMaterial = isMainMaterial;
	}

	public Integer getIsCapacityMaterial(){
		return isCapacityMaterial;
	}

	public void setIsCapacityMaterial(Integer isCapacityMaterial){
		this.isCapacityMaterial = isCapacityMaterial;
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