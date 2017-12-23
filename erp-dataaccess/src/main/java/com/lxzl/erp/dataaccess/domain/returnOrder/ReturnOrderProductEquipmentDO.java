package com.lxzl.erp.dataaccess.domain.returnOrder;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class ReturnOrderProductEquipmentDO  extends BaseDO {

	private Integer id;
	private Integer returnOrderProductId;
	private Integer returnOrderId;
	private String returnOrderNo;
	private String orderNo;
	private Integer equipmentId;
	private String equipmentNo;
	private Integer dataStatus;
	private String remark;

	private ProductEquipmentDO productEquipmentDO;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getReturnOrderProductId(){
		return returnOrderProductId;
	}

	public void setReturnOrderProductId(Integer returnOrderProductId){
		this.returnOrderProductId = returnOrderProductId;
	}

	public Integer getReturnOrderId(){
		return returnOrderId;
	}

	public void setReturnOrderId(Integer returnOrderId){
		this.returnOrderId = returnOrderId;
	}

	public String getReturnOrderNo(){
		return returnOrderNo;
	}

	public void setReturnOrderNo(String returnOrderNo){
		this.returnOrderNo = returnOrderNo;
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

	public ProductEquipmentDO getProductEquipmentDO() {
		return productEquipmentDO;
	}

	public void setProductEquipmentDO(ProductEquipmentDO productEquipmentDO) {
		this.productEquipmentDO = productEquipmentDO;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}