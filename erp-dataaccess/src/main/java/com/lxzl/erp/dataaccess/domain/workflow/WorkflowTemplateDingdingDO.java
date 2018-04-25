package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class WorkflowTemplateDingdingDO  extends BaseDO {

	private Integer id;
	private String dingdingProcessCode;
	private String name;
	private Integer nameIndex;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getDingdingProcessCode(){
		return dingdingProcessCode;
	}

	public void setDingdingProcessCode(String dingdingProcessCode){
		this.dingdingProcessCode = dingdingProcessCode;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getNameIndex(){
		return nameIndex;
	}

	public void setNameIndex(Integer nameIndex){
		this.nameIndex = nameIndex;
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