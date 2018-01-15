package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PeerDeploymentOrderProductEquipmentDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderProductId;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer equipmentId;
	private String equipmentNo;
	private Date returnTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

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

}