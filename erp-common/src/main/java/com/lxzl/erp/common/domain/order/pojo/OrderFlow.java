package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderFlow extends BasePO {

	private Integer orderFlowId;   //
	private String originalOrderNo;   //原订单号
	private String nodeOrderNo;   //节点订单
	private String orderNo;   //当前订单号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Date originalExpectReturnTime;   //原订单预计归还时间
	private Date originalRentStartTime;   //原订单起租时间
	private Date rentStartTime;   //新订单起租时间
	private Date expectReturnTime;   //新订单预计归还时间


	public Integer getOrderFlowId(){
		return orderFlowId;
	}

	public void setOrderFlowId(Integer orderFlowId){
		this.orderFlowId = orderFlowId;
	}

	public String getOriginalOrderNo(){
		return originalOrderNo;
	}

	public void setOriginalOrderNo(String originalOrderNo){
		this.originalOrderNo = originalOrderNo;
	}

	public String getNodeOrderNo(){
		return nodeOrderNo;
	}

	public void setNodeOrderNo(String nodeOrderNo){
		this.nodeOrderNo = nodeOrderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
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

	public Date getOriginalExpectReturnTime(){
		return originalExpectReturnTime;
	}

	public void setOriginalExpectReturnTime(Date originalExpectReturnTime){
		this.originalExpectReturnTime = originalExpectReturnTime;
	}

	public Date getOriginalRentStartTime(){
		return originalRentStartTime;
	}

	public void setOriginalRentStartTime(Date originalRentStartTime){
		this.originalRentStartTime = originalRentStartTime;
	}

	public Date getRentStartTime(){
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime){
		this.rentStartTime = rentStartTime;
	}

	public Date getExpectReturnTime(){
		return expectReturnTime;
	}

	public void setExpectReturnTime(Date expectReturnTime){
		this.expectReturnTime = expectReturnTime;
	}

}