package com.lxzl.erp.dataaccess.domain.businessSystemConfig;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class BusinessSystemConfigDO  extends BaseDO {

	private Integer id;
	private String businessSystemName;
	private Integer businessSystemType;
	private String businessAppId;
	private String businessAppSecret;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getBusinessSystemName(){
		return businessSystemName;
	}

	public void setBusinessSystemName(String businessSystemName){
		this.businessSystemName = businessSystemName;
	}

	public Integer getBusinessSystemType(){
		return businessSystemType;
	}

	public void setBusinessSystemType(Integer businessSystemType){
		this.businessSystemType = businessSystemType;
	}

	public String getBusinessAppId(){
		return businessAppId;
	}

	public void setBusinessAppId(String businessAppId){
		this.businessAppId = businessAppId;
	}

	public String getBusinessAppSecret(){
		return businessAppSecret;
	}

	public void setBusinessAppSecret(String businessAppSecret){
		this.businessAppSecret = businessAppSecret;
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