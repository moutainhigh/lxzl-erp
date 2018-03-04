package com.lxzl.erp.dataaccess.domain.delivery;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.util.List;


public class DeliveryOrderDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private String deliveryUser;
	private Date deliveryTime;
	private Integer subCompanyId;
	private Integer dataStatus;
	private String remark;

	private List<DeliveryOrderProductDO> deliveryOrderProductDOList;
	private List<DeliveryOrderMaterialDO> deliveryOrderMaterialDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public String getDeliveryUser(){
		return deliveryUser;
	}

	public void setDeliveryUser(String deliveryUser){
		this.deliveryUser = deliveryUser;
	}

	public Date getDeliveryTime(){
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
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
	
	public List<DeliveryOrderProductDO> getDeliveryOrderProductDOList() {
		return deliveryOrderProductDOList;
	}

	public void setDeliveryOrderProductDOList(List<DeliveryOrderProductDO> deliveryOrderProductDOList) {
		this.deliveryOrderProductDOList = deliveryOrderProductDOList;
	}

	public List<DeliveryOrderMaterialDO> getDeliveryOrderMaterialDOList() {
		return deliveryOrderMaterialDOList;
	}

	public void setDeliveryOrderMaterialDOList(List<DeliveryOrderMaterialDO> deliveryOrderMaterialDOList) {
		this.deliveryOrderMaterialDOList = deliveryOrderMaterialDOList;
	}
}