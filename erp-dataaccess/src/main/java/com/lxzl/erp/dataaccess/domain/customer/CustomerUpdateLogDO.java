package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class CustomerUpdateLogDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private Integer owner;
	private Integer unionUser;
	private Integer dataStatus;
	private String remark;
	private Integer isOwnerUpdateFlag; // 是否变更了开发人
	private Integer isUnionUserUpdateFlag; // 是否变更了联合开发员
	private Integer oldOwner;
	private Integer oldUnionUser;

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

	public Integer getIsOwnerUpdateFlag() {
		return isOwnerUpdateFlag;
	}

	public void setIsOwnerUpdateFlag(Integer isOwnerUpdateFlag) {
		this.isOwnerUpdateFlag = isOwnerUpdateFlag;
	}

	public Integer getIsUnionUserUpdateFlag() {
		return isUnionUserUpdateFlag;
	}

	public void setIsUnionUserUpdateFlag(Integer isUnionUserUpdateFlag) {
		this.isUnionUserUpdateFlag = isUnionUserUpdateFlag;
	}

	public Integer getOldOwner() {
		return oldOwner;
	}

	public void setOldOwner(Integer oldOwner) {
		this.oldOwner = oldOwner;
	}

	public Integer getOldUnionUser() {
		return oldUnionUser;
	}

	public void setOldUnionUser(Integer oldUnionUser) {
		this.oldUnionUser = oldUnionUser;
	}
}