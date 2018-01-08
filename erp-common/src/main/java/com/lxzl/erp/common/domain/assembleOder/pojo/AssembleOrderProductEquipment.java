package com.lxzl.erp.common.domain.assembleOder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrderProductEquipment extends BasePO {

	private Integer assembleOrderProductEquipmentId;   //唯一标识
	private Integer assembleOrderId;   //组装单ID
	private String productEquipmentNo;   //商品设备唯一编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getAssembleOrderProductEquipmentId(){
		return assembleOrderProductEquipmentId;
	}

	public void setAssembleOrderProductEquipmentId(Integer assembleOrderProductEquipmentId){
		this.assembleOrderProductEquipmentId = assembleOrderProductEquipmentId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}