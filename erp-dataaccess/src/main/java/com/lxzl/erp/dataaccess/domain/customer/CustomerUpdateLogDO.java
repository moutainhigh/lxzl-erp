package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class CustomerUpdateLogDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private Integer owner;
	private Integer unionUser;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}