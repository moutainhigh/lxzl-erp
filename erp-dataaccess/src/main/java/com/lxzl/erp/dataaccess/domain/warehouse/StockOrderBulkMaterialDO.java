package com.lxzl.erp.dataaccess.domain.warehouse;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class StockOrderBulkMaterialDO  extends BaseDO {

	private Integer id;
	private String stockOrderNo;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private Integer itemReferId;
	private Integer dataStatus;
	private String remark;

	@Transient
	private String bulkMaterialName;


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

	public String getBulkMaterialName() {
		return bulkMaterialName;
	}

	public void setBulkMaterialName(String bulkMaterialName) {
		this.bulkMaterialName = bulkMaterialName;
	}

	public Integer getItemReferId() {
		return itemReferId;
	}

	public void setItemReferId(Integer itemReferId) {
		this.itemReferId = itemReferId;
	}
}