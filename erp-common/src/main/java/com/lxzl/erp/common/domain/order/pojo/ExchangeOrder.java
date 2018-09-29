package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeOrder extends BasePO {

	private Integer exchangeOrderId;   //
	private String exchangeOrderNo;   //变更单号
	@NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
	private String orderNo;   //原订单号
	private String newOrderNo;//新订单号
	private Date rentStartTime;   //起租时间
	private Integer depositCycle;   //押金期数
	private Integer paymentCycle;   //付款期数
	private Integer payMode;   //支付方式：1先用后付，2先付后用
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer businessType;   //业务编码
	private Integer status;   //变更单转换
	private String remark;   //备注

	/**
	 * 变更单商品明细
	 */
	private List<ExchangeOrderProduct> exchangeOrderProductList;

	/**
	 * 变更单配件明细
	 */
	private List<ExchangeOrderMaterial> exchangeOrderMaterialList;

	public List<ExchangeOrderProduct> getExchangeOrderProductList() {
		return exchangeOrderProductList;
	}

	public void setExchangeOrderProductList(List<ExchangeOrderProduct> exchangeOrderProductList) {
		this.exchangeOrderProductList = exchangeOrderProductList;
	}

	public List<ExchangeOrderMaterial> getExchangeOrderMaterialList() {
		return exchangeOrderMaterialList;
	}

	public void setExchangeOrderMaterialList(List<ExchangeOrderMaterial> exchangeOrderMaterialList) {
		this.exchangeOrderMaterialList = exchangeOrderMaterialList;
	}

	public Integer getExchangeOrderId(){
		return exchangeOrderId;
	}

	public void setExchangeOrderId(Integer exchangeOrderId){
		this.exchangeOrderId = exchangeOrderId;
	}

	public String getExchangeOrderNo(){
		return exchangeOrderNo;
	}

	public void setExchangeOrderNo(String exchangeOrderNo){
		this.exchangeOrderNo = exchangeOrderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Date getRentStartTime(){
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime){
		this.rentStartTime = rentStartTime;
	}

	public Integer getDepositCycle(){
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle){
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode(){
		return payMode;
	}

	public void setPayMode(Integer payMode){
		this.payMode = payMode;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
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

	public Integer getBusinessType(){
		return businessType;
	}

	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getNewOrderNo() {
		return newOrderNo;
	}

	public void setNewOrderNo(String newOrderNo) {
		this.newOrderNo = newOrderNo;
	}
}