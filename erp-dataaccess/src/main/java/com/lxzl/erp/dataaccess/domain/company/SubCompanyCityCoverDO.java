package com.lxzl.erp.dataaccess.domain.company;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class SubCompanyCityCoverDO  extends BaseDO {

	private Integer id;
	private Integer provinceId;
	private Integer cityId;
	private Integer subCompanyId;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getProvinceId(){
		return provinceId;
	}

	public void setProvinceId(Integer provinceId){
		this.provinceId = provinceId;
	}

	public Integer getCityId(){
		return cityId;
	}

	public void setCityId(Integer cityId){
		this.cityId = cityId;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
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