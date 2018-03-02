package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ChangeOrder extends BasePO {

	private Integer k3ChangeOrderId;   //唯一标识
	private String changeOrderNo;   //换货单编号
	private String k3CustomerNo;   //K3客户编码
	private String k3CustomerName;   //K3客户名称
	private Date changeTime;   //添加时间
	private String changeAddress;   //换货地址
	private String changeContacts;   //联系人
	private String changePhone;   //联系电话
	private Integer changeMode;   //换还方式，1-上门取件，2邮寄
	private BigDecimal logisticsAmount;   //运费
	private BigDecimal serviceAmount;   //服务费
	private Integer changeOrderStatus;   //换货订单状态，0-待提交，4-审核中，24-取消，28-已完成
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<K3ChangeOrderDetail> k3ChangeOrderDetailList;

	public Integer getK3ChangeOrderId(){
		return k3ChangeOrderId;
	}

	public void setK3ChangeOrderId(Integer k3ChangeOrderId){
		this.k3ChangeOrderId = k3ChangeOrderId;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
	}

	public String getK3CustomerNo(){
		return k3CustomerNo;
	}

	public void setK3CustomerNo(String k3CustomerNo){
		this.k3CustomerNo = k3CustomerNo;
	}

	public String getK3CustomerName(){
		return k3CustomerName;
	}

	public void setK3CustomerName(String k3CustomerName){
		this.k3CustomerName = k3CustomerName;
	}

	public Date getChangeTime(){
		return changeTime;
	}

	public void setChangeTime(Date changeTime){
		this.changeTime = changeTime;
	}

	public String getChangeAddress(){
		return changeAddress;
	}

	public void setChangeAddress(String changeAddress){
		this.changeAddress = changeAddress;
	}

	public String getChangeContacts(){
		return changeContacts;
	}

	public void setChangeContacts(String changeContacts){
		this.changeContacts = changeContacts;
	}

	public String getChangePhone(){
		return changePhone;
	}

	public void setChangePhone(String changePhone){
		this.changePhone = changePhone;
	}

	public Integer getChangeMode(){
		return changeMode;
	}

	public void setChangeMode(Integer changeMode){
		this.changeMode = changeMode;
	}

	public BigDecimal getLogisticsAmount(){
		return logisticsAmount;
	}

	public void setLogisticsAmount(BigDecimal logisticsAmount){
		this.logisticsAmount = logisticsAmount;
	}

	public BigDecimal getServiceAmount(){
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount){
		this.serviceAmount = serviceAmount;
	}

	public Integer getChangeOrderStatus(){
		return changeOrderStatus;
	}

	public void setChangeOrderStatus(Integer changeOrderStatus){
		this.changeOrderStatus = changeOrderStatus;
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

	public List<K3ChangeOrderDetail> getK3ChangeOrderDetailList() {
		return k3ChangeOrderDetailList;
	}

	public void setK3ChangeOrderDetailList(List<K3ChangeOrderDetail> k3ChangeOrderDetailList) {
		this.k3ChangeOrderDetailList = k3ChangeOrderDetailList;
	}
}