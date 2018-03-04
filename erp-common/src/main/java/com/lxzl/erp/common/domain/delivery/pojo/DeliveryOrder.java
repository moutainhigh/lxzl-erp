package com.lxzl.erp.common.domain.delivery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrder extends BasePO {

	private Integer deliveryOrderId;   //唯一标识
	private String orderNo;   //订单No
	private Integer orderId;   //订单ID
	private String deliveryUser;   //发货人
	private String deliveryTimeStr;   //发货时间
	private Date deliveryTime;   //发货时间
	private String subCompanyCode;   //分公司Code
	private Integer subCompanyId;   //分公司ID
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<DeliveryOrderProduct> deliveryOrderProductList;
	private List<DeliveryOrderMaterial> deliveryOrderMaterialList;


	public Integer getDeliveryOrderId(){
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Integer deliveryOrderId){
		this.deliveryOrderId = deliveryOrderId;
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

	public List<DeliveryOrderProduct> getDeliveryOrderProductList() {
		return deliveryOrderProductList;
	}

	public void setDeliveryOrderProductList(List<DeliveryOrderProduct> deliveryOrderProductList) {
		this.deliveryOrderProductList = deliveryOrderProductList;
	}

	public List<DeliveryOrderMaterial> getDeliveryOrderMaterialList() {
		return deliveryOrderMaterialList;
	}

	public void setDeliveryOrderMaterialList(List<DeliveryOrderMaterial> deliveryOrderMaterialList) {
		this.deliveryOrderMaterialList = deliveryOrderMaterialList;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubCompanyCode() {
		return subCompanyCode;
	}

	public void setSubCompanyCode(String subCompanyCode) {
		this.subCompanyCode = subCompanyCode;
	}

	public String getDeliveryTimeStr() {
		return deliveryTimeStr;
	}

	public void setDeliveryTimeStr(String deliveryTimeStr) {
		this.deliveryTimeStr = deliveryTimeStr;
	}
}