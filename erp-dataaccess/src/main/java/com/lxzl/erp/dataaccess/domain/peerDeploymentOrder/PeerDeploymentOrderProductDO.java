package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;


import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class PeerDeploymentOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer productSkuId;
	private BigDecimal productUnitAmount;
	private BigDecimal productAmount;
	private Integer productSkuCount;
	private String productSkuSnapshot;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;

	private List<PeerDeploymentOrderProductEquipmentDO> peerDeploymentOrderProductEquipmentDOList;

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

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public BigDecimal getProductUnitAmount(){
		return productUnitAmount;
	}

	public void setProductUnitAmount(BigDecimal productUnitAmount){
		this.productUnitAmount = productUnitAmount;
	}

	public BigDecimal getProductAmount(){
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount){
		this.productAmount = productAmount;
	}

	public Integer getProductSkuCount(){
		return productSkuCount;
	}

	public void setProductSkuCount(Integer productSkuCount){
		this.productSkuCount = productSkuCount;
	}

	public String getProductSkuSnapshot(){
		return productSkuSnapshot;
	}

	public void setProductSkuSnapshot(String productSkuSnapshot){
		this.productSkuSnapshot = productSkuSnapshot;
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

	public List<PeerDeploymentOrderProductEquipmentDO> getPeerDeploymentOrderProductEquipmentDOList() { return peerDeploymentOrderProductEquipmentDOList; }

	public void setPeerDeploymentOrderProductEquipmentDOList(List<PeerDeploymentOrderProductEquipmentDO> peerDeploymentOrderProductEquipmentDOList) { this.peerDeploymentOrderProductEquipmentDOList = peerDeploymentOrderProductEquipmentDOList; }
}