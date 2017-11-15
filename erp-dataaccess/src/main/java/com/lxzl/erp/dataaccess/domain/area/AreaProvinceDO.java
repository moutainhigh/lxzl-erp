package com.lxzl.erp.dataaccess.domain.area;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class AreaProvinceDO  extends BaseDO {

	private Long id;
	private String provinceName;
	private Integer areaType;
	private String abbCn;
	private String abbEn;
	private Integer dataStatus;
	private String remark;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getProvinceName(){
		return provinceName;
	}

	public void setProvinceName(String provinceName){
		this.provinceName = provinceName;
	}

	public Integer getAreaType(){
		return areaType;
	}

	public void setAreaType(Integer areaType){
		this.areaType = areaType;
	}

	public String getAbbCn(){
		return abbCn;
	}

	public void setAbbCn(String abbCn){
		this.abbCn = abbCn;
	}

	public String getAbbEn(){
		return abbEn;
	}

	public void setAbbEn(String abbEn){
		this.abbEn = abbEn;
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