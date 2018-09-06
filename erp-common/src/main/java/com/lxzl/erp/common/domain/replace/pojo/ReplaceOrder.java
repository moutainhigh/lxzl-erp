package com.lxzl.erp.common.domain.replace.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrder extends BasePO {

	private Integer replaceOrderId;   //唯一标识
	private String replaceOrderNo;   //换货编号
	private Integer orderId;   //原订单ID
	@NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL,groups = {AddGroup.class})
	private String orderNo;   //原订单编号
	private Integer customerId;   //客户ID
	@NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {AddGroup.class})
	private String customerNo;   //客户编号
//	@NotNull(message = ErrorCode.REPLACE_TIME_NOT_NULL, groups = {AddGroup.class})
	private Date replaceTime;   //换货时间
	private Integer totalReplaceProductCount;   //换货商品总数
	private Integer totalReplaceMaterialCount;   //换货配件总数
	private Integer realTotalReplaceProductCount;   //实际换货商品总数
	private Integer realTotalReplaceMaterialCount;   //实际换货配件总数
	private BigDecimal serviceCost;   //服务费
	private BigDecimal logisticsCost;   //运费
	private BigDecimal repairCost;   //维修费
//	@Min(value = 0, message = ErrorCode.REPLACE_REASON_TYPE_ERROR, groups = {AddGroup.class})
//	@Max(value = 2, message = ErrorCode.REPLACE_REASON_TYPE_ERROR, groups = {AddGroup.class})
	private Integer replaceReasonType;   //换货原因类型,0-升级 ，1-损坏，2-其他
	private String replaceReason;   //换货原因
//	@NotNull(message = ErrorCode.REPLACE_MODE_IS_NULL, groups = {AddGroup.class})
	private Integer replaceMode;   //换货方式，1-上门取件，2邮寄
	private Integer replaceOrderStatus;   //换货订单状态，0-待提交，4-审核中，8-待发货，12-处理中，16-已发货，20-已完成，24-取消
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Date confirmReplaceTime;   //确认换货时间
	private String confirmReplaceUser;   //确认换货人
	@Valid
//	@CollectionNotNull(message = ErrorCode.REPLACE_ORDER_DETAIL_LIST_NOT_NULL, groups = {AddGroup.class})
	private List<ReplaceOrderDetail> replaceOrderDetailList;


	public Integer getReplaceOrderId(){
		return replaceOrderId;
	}

	public void setReplaceOrderId(Integer replaceOrderId){
		this.replaceOrderId = replaceOrderId;
	}

	public String getReplaceOrderNo(){
		return replaceOrderNo;
	}

	public void setReplaceOrderNo(String replaceOrderNo){
		this.replaceOrderNo = replaceOrderNo;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
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

	public Date getReplaceTime(){
		return replaceTime;
	}

	public void setReplaceTime(Date replaceTime){
		this.replaceTime = replaceTime;
	}

	public Integer getTotalReplaceProductCount(){
		return totalReplaceProductCount;
	}

	public void setTotalReplaceProductCount(Integer totalReplaceProductCount){
		this.totalReplaceProductCount = totalReplaceProductCount;
	}

	public Integer getTotalReplaceMaterialCount(){
		return totalReplaceMaterialCount;
	}

	public void setTotalReplaceMaterialCount(Integer totalReplaceMaterialCount){
		this.totalReplaceMaterialCount = totalReplaceMaterialCount;
	}

	public Integer getRealTotalReplaceProductCount(){
		return realTotalReplaceProductCount;
	}

	public void setRealTotalReplaceProductCount(Integer realTotalReplaceProductCount){
		this.realTotalReplaceProductCount = realTotalReplaceProductCount;
	}

	public Integer getRealTotalReplaceMaterialCount(){
		return realTotalReplaceMaterialCount;
	}

	public void setRealTotalReplaceMaterialCount(Integer realTotalReplaceMaterialCount){
		this.realTotalReplaceMaterialCount = realTotalReplaceMaterialCount;
	}

	public BigDecimal getServiceCost(){
		return serviceCost;
	}

	public void setServiceCost(BigDecimal serviceCost){
		this.serviceCost = serviceCost;
	}

	public BigDecimal getLogisticsCost(){
		return logisticsCost;
	}

	public void setLogisticsCost(BigDecimal logisticsCost){
		this.logisticsCost = logisticsCost;
	}

	public BigDecimal getRepairCost(){
		return repairCost;
	}

	public void setRepairCost(BigDecimal repairCost){
		this.repairCost = repairCost;
	}

	public Integer getReplaceReasonType(){
		return replaceReasonType;
	}

	public void setReplaceReasonType(Integer replaceReasonType){
		this.replaceReasonType = replaceReasonType;
	}

	public String getReplaceReason(){
		return replaceReason;
	}

	public void setReplaceReason(String replaceReason){
		this.replaceReason = replaceReason;
	}

	public Integer getReplaceMode(){
		return replaceMode;
	}

	public void setReplaceMode(Integer replaceMode){
		this.replaceMode = replaceMode;
	}

	public Integer getReplaceOrderStatus(){
		return replaceOrderStatus;
	}

	public void setReplaceOrderStatus(Integer replaceOrderStatus){
		this.replaceOrderStatus = replaceOrderStatus;
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

	public Date getConfirmReplaceTime(){
		return confirmReplaceTime;
	}

	public void setConfirmReplaceTime(Date confirmReplaceTime){
		this.confirmReplaceTime = confirmReplaceTime;
	}

	public String getConfirmReplaceUser(){
		return confirmReplaceUser;
	}

	public void setConfirmReplaceUser(String confirmReplaceUser){
		this.confirmReplaceUser = confirmReplaceUser;
	}

	public List<ReplaceOrderDetail> getReplaceOrderDetailList() {
		return replaceOrderDetailList;
	}

	public void setReplaceOrderDetailList(List<ReplaceOrderDetail> replaceOrderDetailList) {
		this.replaceOrderDetailList = replaceOrderDetailList;
	}
}