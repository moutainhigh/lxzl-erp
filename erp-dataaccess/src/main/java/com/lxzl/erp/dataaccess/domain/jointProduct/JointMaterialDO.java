package com.lxzl.erp.dataaccess.domain.jointProduct;

import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class JointMaterialDO  extends BaseDO {

	private Integer id;
	private Integer jointProductId;
	private Integer materialId;
	private Integer materialCount;
	private Integer dataStatus;
	private String remark;
	private String materialNo;   //物料编号

	private MaterialDO materialDO;

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

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

	public Integer getJointProductId(){
		return jointProductId;
	}

	public void setJointProductId(Integer jointProductId){
		this.jointProductId = jointProductId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
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