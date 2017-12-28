package com.lxzl.erp.common.domain.returnOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderProductEquipment extends BasePO {

	private Integer returnOrderProductEquipmentId;   //唯一标识
	private Integer returnOrderProductId;   //租赁退还商品项ID
	private Integer returnOrderId;   //退还ID
	private String returnOrderNo;   //退还编号
	private String orderNo;   //订单编号
	private Integer equipmentId;   //设备ID
	private String equipmentNo;   //设备编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private ProductEquipment productEquipment;

	public Integer getReturnOrderProductEquipmentId(){
		return returnOrderProductEquipmentId;
	}

	public void setReturnOrderProductEquipmentId(Integer returnOrderProductEquipmentId){
		this.returnOrderProductEquipmentId = returnOrderProductEquipmentId;
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

	public ProductEquipment getProductEquipment() {
		return productEquipment;
	}

	public void setProductEquipment(ProductEquipment productEquipment) {
		this.productEquipment = productEquipment;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}