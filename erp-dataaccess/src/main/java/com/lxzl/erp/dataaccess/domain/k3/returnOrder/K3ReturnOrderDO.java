package com.lxzl.erp.dataaccess.domain.k3.returnOrder;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class K3ReturnOrderDO  extends BaseDO {

	private Integer id;
	private String returnOrderNo;
	private String k3CustomerNo;
	private String k3CustomerName;   //K3客户名称
	private Date returnTime;
	private String returnAddress;
	private String returnContacts;
	private String returnPhone;
	private Integer returnMode;   //退还方式，1-上门取件，2邮寄
	private Integer returnOrderStatus;   // 是否推送到K3 1是0否
	private BigDecimal logisticsAmount;	// 运费
	private BigDecimal serviceAmount;	// 服务费
	private Integer dataStatus;
	private String remark;
	private Integer returnReasonType;	// 退货原因
	private Integer deliverySubCompanyId;//配送分公司id
	@Transient
	private String deliverySubCompanyName;//配送分公司
	private Integer successStatus;//处理成功状态---1处理成功 0 未处理成功
	private List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList;

	public Integer getReturnReasonType() {
		return returnReasonType;
	}

	public void setReturnReasonType(Integer returnReasonType) {
		this.returnReasonType = returnReasonType;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getReturnOrderNo(){
		return returnOrderNo;
	}

	public void setReturnOrderNo(String returnOrderNo){
		this.returnOrderNo = returnOrderNo;
	}

	public String getK3CustomerNo(){
		return k3CustomerNo;
	}

	public void setK3CustomerNo(String k3CustomerNo){
		this.k3CustomerNo = k3CustomerNo;
	}

	public Date getReturnTime(){
		return returnTime;
	}

	public void setReturnTime(Date returnTime){
		this.returnTime = returnTime;
	}

	public String getReturnAddress(){
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress){
		this.returnAddress = returnAddress;
	}

	public String getReturnContacts(){
		return returnContacts;
	}

	public void setReturnContacts(String returnContacts){
		this.returnContacts = returnContacts;
	}

	public String getReturnPhone(){
		return returnPhone;
	}

	public void setReturnPhone(String returnPhone){
		this.returnPhone = returnPhone;
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

	public List<K3ReturnOrderDetailDO> getK3ReturnOrderDetailDOList() {
		return k3ReturnOrderDetailDOList;
	}

	public void setK3ReturnOrderDetailDOList(List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList) {
		this.k3ReturnOrderDetailDOList = k3ReturnOrderDetailDOList;
	}

	public String getK3CustomerName() {
		return k3CustomerName;
	}

	public void setK3CustomerName(String k3CustomerName) {
		this.k3CustomerName = k3CustomerName;
	}

	public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}

	public Integer getReturnMode() {
		return returnMode;
	}

	public void setReturnMode(Integer returnMode) {
		this.returnMode = returnMode;
	}

	public BigDecimal getLogisticsAmount() {
		return logisticsAmount;
	}

	public void setLogisticsAmount(BigDecimal logisticsAmount) {
		this.logisticsAmount = logisticsAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Integer getDeliverySubCompanyId() {
		return deliverySubCompanyId;
	}

	public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
		this.deliverySubCompanyId = deliverySubCompanyId;
	}

	public String getDeliverySubCompanyName() {
		return deliverySubCompanyName;
	}

	public void setDeliverySubCompanyName(String deliverySubCompanyName) {
		this.deliverySubCompanyName = deliverySubCompanyName;
	}

	public Integer getSuccessStatus() {
		if (successStatus == null) {
			successStatus = CommonConstant.COMMON_CONSTANT_YES;
		}
		return successStatus;
	}

	public void setSuccessStatus(Integer successStatus) {
		this.successStatus = successStatus;
	}
}