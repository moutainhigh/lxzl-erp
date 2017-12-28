package com.lxzl.erp.dataaccess.domain.jointProduct;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class JointProductDO  extends BaseDO {

	private Integer id;
	private String jointProductName;
	private Integer dataStatus;
	private String remark;
	@Transient
	private List<JointMaterialDO> jointMaterialDOList;   //组合商品物料项表
	@Transient
	private List<JointProductSkuDO> jointProductSkuDOList;   //组合商品sku项表


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getJointProductName(){
		return jointProductName;
	}

	public void setJointProductName(String jointProductName){
		this.jointProductName = jointProductName;
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


	public List<JointMaterialDO> getJointMaterialDOList() {
		return jointMaterialDOList;
	}

	public void setJointMaterialDOList(List<JointMaterialDO> jointMaterialDOList) {
		this.jointMaterialDOList = jointMaterialDOList;
	}

	public List<JointProductSkuDO> getJointProductSkuDOList() {
		return jointProductSkuDOList;
	}

	public void setJointProductSkuDOList(List<JointProductSkuDO> jointProductSkuDOList) {
		this.jointProductSkuDOList = jointProductSkuDOList;
	}
}