package com.lxzl.erp.dataaccess.domain.printLog;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PrintLogDO  extends BaseDO {

	private Integer id;
	private String referNo;
	private Integer referType;
	private Integer printCount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getReferNo(){
		return referNo;
	}

	public void setReferNo(String referNo){
		this.referNo = referNo;
	}

	public Integer getReferType(){
		return referType;
	}

	public void setReferType(Integer referType){
		this.referType = referType;
	}

	public Integer getPrintCount(){
		return printCount;
	}

	public void setPrintCount(Integer printCount){
		this.printCount = printCount;
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