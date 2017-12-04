package com.lxzl.erp.common.domain.returnOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrder implements Serializable {

	private Integer returnOrderId;   //唯一标识
	@NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL , groups = {IdGroup.class, ExtendGroup.class})
	private String returnOrderNo;   //退还编号
	private Integer customerId;   //客户ID
	private String customerNo;   //客户编号
	private Integer isCharging;   //是否计租赁费用
	private Integer totalReturnProductCount;   //退还商品总数
	private Integer totalReturnMaterialCount;   //退还物料总数
	private Integer realTotalReturnProductCount;   //实际退还商品总数
	private Integer realTotalReturnMaterialCount;   //实际退还物料总数

	private BigDecimal totalRentCost;   //租赁期间产生总费用
	@NotNull(message = ErrorCode.RETURN_ORDER_SERVICE_COST_NOT_NULL , groups = {ExtendGroup.class})
	private BigDecimal serviceCost;   //服务费
	@NotNull(message = ErrorCode.RETURN_ORDER_DAMAGE_COST_NOT_NULL , groups = {ExtendGroup.class})
	private BigDecimal damageCost;   //损坏加收费用
	private Integer returnOrderStatus;   //归还订单状态，1-待取货，5-处理中，9-已完成
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Integer owner;   //数据归属人
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private ReturnOrderConsignInfo returnOrderConsignInfo;
	private List<ReturnOrderProduct> returnOrderProductList;
	private List<ReturnOrderMaterial> returnOrderMaterialList;
	private String ownerName;

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

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getIsCharging(){
		return isCharging;
	}

	public void setIsCharging(Integer isCharging){
		this.isCharging = isCharging;
	}

	public Integer getTotalReturnProductCount(){
		return totalReturnProductCount;
	}

	public void setTotalReturnProductCount(Integer totalReturnProductCount){
		this.totalReturnProductCount = totalReturnProductCount;
	}

	public Integer getTotalReturnMaterialCount(){
		return totalReturnMaterialCount;
	}

	public void setTotalReturnMaterialCount(Integer totalReturnMaterialCount){
		this.totalReturnMaterialCount = totalReturnMaterialCount;
	}

	public Integer getRealTotalReturnProductCount(){
		return realTotalReturnProductCount;
	}

	public void setRealTotalReturnProductCount(Integer realTotalReturnProductCount){
		this.realTotalReturnProductCount = realTotalReturnProductCount;
	}

	public Integer getRealTotalReturnMaterialCount(){
		return realTotalReturnMaterialCount;
	}

	public void setRealTotalReturnMaterialCount(Integer realTotalReturnMaterialCount){
		this.realTotalReturnMaterialCount = realTotalReturnMaterialCount;
	}

	public BigDecimal getTotalRentCost(){
		return totalRentCost;
	}

	public void setTotalRentCost(BigDecimal totalRentCost){
		this.totalRentCost = totalRentCost;
	}

	public BigDecimal getServiceCost(){
		return serviceCost;
	}

	public void setServiceCost(BigDecimal serviceCost){
		this.serviceCost = serviceCost;
	}

	public BigDecimal getDamageCost(){
		return damageCost;
	}

	public void setDamageCost(BigDecimal damageCost){
		this.damageCost = damageCost;
	}

	public Integer getReturnOrderStatus(){
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus){
		this.returnOrderStatus = returnOrderStatus;
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

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
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

	public List<ReturnOrderProduct> getReturnOrderProductList() {
		return returnOrderProductList;
	}

	public void setReturnOrderProductList(List<ReturnOrderProduct> returnOrderProductList) {
		this.returnOrderProductList = returnOrderProductList;
	}

	public List<ReturnOrderMaterial> getReturnOrderMaterialList() {
		return returnOrderMaterialList;
	}

	public void setReturnOrderMaterialList(List<ReturnOrderMaterial> returnOrderMaterialList) {
		this.returnOrderMaterialList = returnOrderMaterialList;
	}

	public ReturnOrderConsignInfo getReturnOrderConsignInfo() {
		return returnOrderConsignInfo;
	}

	public void setReturnOrderConsignInfo(ReturnOrderConsignInfo returnOrderConsignInfo) {
		this.returnOrderConsignInfo = returnOrderConsignInfo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}