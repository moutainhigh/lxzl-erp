package com.lxzl.erp.dataaccess.domain.interfaceSwitch;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class SwitchDO  extends BaseDO {

	private Integer id;
	private String interfaceUrl;
	private Integer isOpen;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getInterfaceUrl(){
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl){
		this.interfaceUrl = interfaceUrl;
	}

	public Integer getIsOpen(){
		return isOpen;
	}

	public void setIsOpen(Integer isOpen){
		this.isOpen = isOpen;
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