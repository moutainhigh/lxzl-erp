package com.lxzl.erp.dataaccess.domain.warehouse;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class StockOrderEquipmentDO  extends BaseDO {

	private Integer id;
	private String stockOrderNo;
	private Integer itemReferId;
	private Integer equipmentId;
	private String equipmentNo;
	private Integer dataStatus;
	private String remark;

	@Transient
	private Integer productId;
	@Transient
	private String productName;
	@Transient
	private Integer productSkuId;
	@Transient
	private String productSkuName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getStockOrderNo(){
		return stockOrderNo;
	}

	public void setStockOrderNo(String stockOrderNo){
		this.stockOrderNo = stockOrderNo;
	}

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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId) {
		this.productSkuId = productSkuId;
	}

	public String getProductSkuName() {
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName) {
		this.productSkuName = productSkuName;
	}

	public Integer getItemReferId() {
		return itemReferId;
	}

	public void setItemReferId(Integer itemReferId) {
		this.itemReferId = itemReferId;
	}
}