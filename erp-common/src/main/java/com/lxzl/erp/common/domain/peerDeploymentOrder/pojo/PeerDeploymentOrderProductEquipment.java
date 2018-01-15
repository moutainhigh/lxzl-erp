package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderProductEquipment extends BasePO {

	private Integer peerDeploymentOrderProductEquipmentId;   //唯一标识
	private Integer peerDeploymentOrderProductId;   //货物调拨商品项ID
	private Integer peerDeploymentOrderId;   //货物调拨单ID
	private String peerDeploymentOrderNo;   //货物调拨单编号
	private Integer equipmentId;   //设备ID
	private String equipmentNo;   //设备编号
	private Date returnTime;   //退还时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getPeerDeploymentOrderProductEquipmentId(){
		return peerDeploymentOrderProductEquipmentId;
	}

	public void setPeerDeploymentOrderProductEquipmentId(Integer peerDeploymentOrderProductEquipmentId){ this.peerDeploymentOrderProductEquipmentId = peerDeploymentOrderProductEquipmentId; }

	public Integer getPeerDeploymentOrderProductId(){
		return peerDeploymentOrderProductId;
	}

	public void setPeerDeploymentOrderProductId(Integer peerDeploymentOrderProductId){ this.peerDeploymentOrderProductId = peerDeploymentOrderProductId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getEquipmentId(){
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId){
		this.equipmentId = equipmentId;
	}

	public String getEquipmentNo(){
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo){
		this.equipmentNo = equipmentNo;
	}

	public Date getReturnTime(){
		return returnTime;
	}

	public void setReturnTime(Date returnTime){
		this.returnTime = returnTime;
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