package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderConsignInfo;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterial;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderProduct;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class PeerDeploymentOrderDO  extends BaseDO {

	private Integer id;
	private String peerDeploymentOrderNo;
	private Integer peerId;
	private Date rentStartTime;
	private Integer rentType;
	private Integer rentTimeLength;
	private Integer warehouseId;
	private Integer warehousePositionId;
	private Integer deliveryMode;
	private Double taxRate;
	private Integer peerDeploymentOrderStatus;
	private Integer totalProductCount;
	private BigDecimal totalProductAmount;
	private Integer totalMaterialCount;
	private BigDecimal totalMaterialAmount;
	private BigDecimal totalOrderAmount;
	private BigDecimal totalDiscountAmount;
	private Date expectReturnTime;
	private Date realReturnTime;
	private Integer dataStatus;
	private String remark;

	@Transient
	private List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList;
	@Transient
	private List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList;
	@Transient
	private PeerDeploymentOrderConsignInfoDO peerDeploymentOrderConsignInfoDO;

	@Transient
	private String peerName;
	@Transient
	private String warehouseName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getPeerId(){
		return peerId;
	}

	public void setPeerId(Integer peerId){
		this.peerId = peerId;
	}

	public Date getRentStartTime(){ return rentStartTime; }

	public void setRentStartTime(Date rentStartTime){
		this.rentStartTime = rentStartTime;
	}

	public Integer getRentType(){
		return rentType;
	}

	public void setRentType(Integer rentType){
		this.rentType = rentType;
	}

	public Integer getRentTimeLength(){
		return rentTimeLength;
	}

	public void setRentTimeLength(Integer rentTimeLength){
		this.rentTimeLength = rentTimeLength;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getWarehousePositionId(){
		return warehousePositionId;
	}

	public void setWarehousePositionId(Integer warehousePositionId){
		this.warehousePositionId = warehousePositionId;
	}

	public Integer getDeliveryMode(){
		return deliveryMode;
	}

	public void setDeliveryMode(Integer deliveryMode){
		this.deliveryMode = deliveryMode;
	}

	public Double getTaxRate(){
		return taxRate;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate = taxRate;
	}

	public Integer getPeerDeploymentOrderStatus(){
		return peerDeploymentOrderStatus;
	}

	public void setPeerDeploymentOrderStatus(Integer peerDeploymentOrderStatus){ this.peerDeploymentOrderStatus = peerDeploymentOrderStatus; }

	public Integer getTotalProductCount(){
		return totalProductCount;
	}

	public void setTotalProductCount(Integer totalProductCount){
		this.totalProductCount = totalProductCount;
	}

	public BigDecimal getTotalProductAmount(){
		return totalProductAmount;
	}

	public void setTotalProductAmount(BigDecimal totalProductAmount){
		this.totalProductAmount = totalProductAmount;
	}

	public Integer getTotalMaterialCount(){
		return totalMaterialCount;
	}

	public void setTotalMaterialCount(Integer totalMaterialCount){
		this.totalMaterialCount = totalMaterialCount;
	}

	public BigDecimal getTotalMaterialAmount(){
		return totalMaterialAmount;
	}

	public void setTotalMaterialAmount(BigDecimal totalMaterialAmount){
		this.totalMaterialAmount = totalMaterialAmount;
	}

	public BigDecimal getTotalOrderAmount(){
		return totalOrderAmount;
	}

	public void setTotalOrderAmount(BigDecimal totalOrderAmount){
		this.totalOrderAmount = totalOrderAmount;
	}

	public BigDecimal getTotalDiscountAmount(){
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount){
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Date getExpectReturnTime(){
		return expectReturnTime;
	}

	public void setExpectReturnTime(Date expectReturnTime){
		this.expectReturnTime = expectReturnTime;
	}

	public Date getRealReturnTime(){
		return realReturnTime;
	}

	public void setRealReturnTime(Date realReturnTime){
		this.realReturnTime = realReturnTime;
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

	public String getWarehouseName() { return warehouseName; }

	public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

	public String getPeerName() { return peerName; }

	public void setPeerName(String peerName) { this.peerName = peerName; }

	public List<PeerDeploymentOrderProductDO> getPeerDeploymentOrderProductDOList() {
		return peerDeploymentOrderProductDOList;
	}

	public void setPeerDeploymentOrderProductDOList(List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList) { this.peerDeploymentOrderProductDOList = peerDeploymentOrderProductDOList; }

	public List<PeerDeploymentOrderMaterialDO> getPeerDeploymentOrderMaterialDOList() { return peerDeploymentOrderMaterialDOList; }

	public void setPeerDeploymentOrderMaterialDOList(List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList) { this.peerDeploymentOrderMaterialDOList = peerDeploymentOrderMaterialDOList; }

	public PeerDeploymentOrderConsignInfoDO getPeerDeploymentOrderConsignInfoDO() { return peerDeploymentOrderConsignInfoDO; }

	public void setPeerDeploymentOrderConsignInfoDO(PeerDeploymentOrderConsignInfoDO peerDeploymentOrderConsignInfoDO) { this.peerDeploymentOrderConsignInfoDO = peerDeploymentOrderConsignInfoDO; }
}