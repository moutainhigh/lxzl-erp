package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PeerDeploymentOrderMaterialBulkDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderMaterialId;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private Date returnTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPeerDeploymentOrderMaterialId(){
		return peerDeploymentOrderMaterialId;
	}

	public void setPeerDeploymentOrderMaterialId(Integer peerDeploymentOrderMaterialId){ this.peerDeploymentOrderMaterialId = peerDeploymentOrderMaterialId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getBulkMaterialId(){
		return bulkMaterialId;
	}

	public void setBulkMaterialId(Integer bulkMaterialId){
		this.bulkMaterialId = bulkMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
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