package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterialBulk;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class PeerDeploymentOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer materialId;
	private BigDecimal materialUnitAmount;
	private BigDecimal materialAmount;
	private Integer materialCount;
	private String materialSnapshot;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;

	private String materialNo;   //配件编号

	private List<PeerDeploymentOrderMaterialBulkDO> peerDeploymentOrderMaterialBulkDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public BigDecimal getMaterialUnitAmount(){
		return materialUnitAmount;
	}

	public void setMaterialUnitAmount(BigDecimal materialUnitAmount){
		this.materialUnitAmount = materialUnitAmount;
	}

	public BigDecimal getMaterialAmount(){
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount){
		this.materialAmount = materialAmount;
	}

	public Integer getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount) {
		this.materialCount = materialCount;
	}

	public String getMaterialSnapshot() {
		return materialSnapshot;
	}

	public void setMaterialSnapshot(String materialSnapshot) {
		this.materialSnapshot = materialSnapshot;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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

	public List<PeerDeploymentOrderMaterialBulkDO> getPeerDeploymentOrderMaterialBulkDOList() { return peerDeploymentOrderMaterialBulkDOList; }

	public void setPeerDeploymentOrderMaterialBulkDOList(List<PeerDeploymentOrderMaterialBulkDO> peerDeploymentOrderMaterialBulkDOList) { this.peerDeploymentOrderMaterialBulkDOList = peerDeploymentOrderMaterialBulkDOList; }

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
}