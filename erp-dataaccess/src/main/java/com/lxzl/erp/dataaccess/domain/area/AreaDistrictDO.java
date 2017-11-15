package com.lxzl.erp.dataaccess.domain.area;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class AreaDistrictDO  extends BaseDO {

	private Long id;
	private Integer provinceId;
	private Integer cityId;
	private String districtName;
	private String postCode;
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

	public String getDistrictName(){
		return districtName;
	}

	public void setDistrictName(String districtName){
		this.districtName = districtName;
	}

	public String getPostCode(){
		return postCode;
	}

	public void setPostCode(String postCode){
		this.postCode = postCode;
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