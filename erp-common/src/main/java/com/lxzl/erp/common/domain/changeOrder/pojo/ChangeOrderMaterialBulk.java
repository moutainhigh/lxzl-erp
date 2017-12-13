package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderMaterialBulk implements Serializable {

	private Integer changeOrderMaterialBulkId;   //唯一标识
	private Integer changeOrderMaterialId;   //租赁换货物料项ID
	private Integer changeOrderId;   //换货ID
	private String changeOrderNo;   //换货编号
	private Integer srcBulkMaterialId;   //原散料ID
	private String srcBulkMaterialNo;   //原散料编号
	private Integer destBulkMaterialId;   //目标散料ID
	private String destBulkMaterialNo;   //目标散料编号
	private String orderNo;   //订单编号
	private BigDecimal priceDiff;   //差价，可以是正值或负值，差价计算标准为每月
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getChangeOrderMaterialBulkId(){
		return changeOrderMaterialBulkId;
	}

	public void setChangeOrderMaterialBulkId(Integer changeOrderMaterialBulkId){
		this.changeOrderMaterialBulkId = changeOrderMaterialBulkId;
	}

	public Integer getChangeOrderMaterialId(){
		return changeOrderMaterialId;
	}

	public void setChangeOrderMaterialId(Integer changeOrderMaterialId){
		this.changeOrderMaterialId = changeOrderMaterialId;
	}

	public Integer getChangeOrderId(){
		return changeOrderId;
	}

	public void setChangeOrderId(Integer changeOrderId){
		this.changeOrderId = changeOrderId;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
	}

	public Integer getSrcBulkMaterialId(){
		return srcBulkMaterialId;
	}

	public void setSrcBulkMaterialId(Integer srcBulkMaterialId){
		this.srcBulkMaterialId = srcBulkMaterialId;
	}

	public String getSrcBulkMaterialNo(){
		return srcBulkMaterialNo;
	}

	public void setSrcBulkMaterialNo(String srcBulkMaterialNo){
		this.srcBulkMaterialNo = srcBulkMaterialNo;
	}

	public Integer getDestBulkMaterialId(){
		return destBulkMaterialId;
	}

	public void setDestBulkMaterialId(Integer destBulkMaterialId){
		this.destBulkMaterialId = destBulkMaterialId;
	}

	public String getDestBulkMaterialNo(){
		return destBulkMaterialNo;
	}

	public void setDestBulkMaterialNo(String destBulkMaterialNo){
		this.destBulkMaterialNo = destBulkMaterialNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public BigDecimal getPriceDiff(){
		return priceDiff;
	}

	public void setPriceDiff(BigDecimal priceDiff){
		this.priceDiff = priceDiff;
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

}