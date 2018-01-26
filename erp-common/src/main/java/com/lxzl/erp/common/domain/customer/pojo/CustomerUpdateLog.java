package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerUpdateLog extends BasePO {

	private Integer customerUpdateLogId;   //唯一标识
	private Integer customerId;   //客户ID
	private Integer owner;   //数据归属人，跟单员
	private Integer unionUser;   //联合开发人
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getCustomerUpdateLogId(){
		return customerUpdateLogId;
	}

	public void setCustomerUpdateLogId(Integer customerUpdateLogId){
		this.customerUpdateLogId = customerUpdateLogId;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
	}

	public Integer getUnionUser(){
		return unionUser;
	}

	public void setUnionUser(Integer unionUser){
		this.unionUser = unionUser;
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

}