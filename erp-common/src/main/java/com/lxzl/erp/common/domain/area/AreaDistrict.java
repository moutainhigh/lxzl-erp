package com.lxzl.erp.common.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;



@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDistrict implements Serializable {

	private Integer areaDistrictId;   //唯一标识
	private Integer provinceId;   //地区省份ID
	private Integer cityId;   //地区省份ID
	private String districtName;   //地区名称
	private String postCode;   //邮政编码
	private String abbCn;   //中文简称
	private String abbEn;   //英文简称
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注


	public Integer getAreaDistrictId() {
		return areaDistrictId;
	}

	public void setAreaDistrictId(Integer areaDistrictId) {
		this.areaDistrictId = areaDistrictId;
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