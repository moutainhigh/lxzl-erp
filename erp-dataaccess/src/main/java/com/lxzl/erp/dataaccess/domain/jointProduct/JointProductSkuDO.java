package com.lxzl.erp.dataaccess.domain.jointProduct;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class JointProductSkuDO  extends BaseDO {

	private Integer id;
	private Integer jointProductId;
	private Integer skuId;
	private Integer skuCount;
	private Integer dataStatus;
	private String remark;

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

	public Integer getSkuId(){
		return skuId;
	}

	public void setSkuId(Integer skuId){
		this.skuId = skuId;
	}

	public Integer getSkuCount(){
		return skuCount;
	}

	public void setSkuCount(Integer skuCount){
		this.skuCount = skuCount;
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