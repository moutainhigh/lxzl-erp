package com.lxzl.erp.common.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;



@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaCity implements Serializable {

	private Long areaCityId;   //唯一标识
	private Integer provinceId;   //地区省份ID
	private String cityName;   //地区名称
	private String cityCode;   //城市区号
	private String postCode;   //邮政编码
	private String abbCn;   //中文简称
	private String abbEn;   //英文简称
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注


	public Long getAreaCityId(){
		return areaCityId;
	}

	public void setAreaCityId(Long areaCityId){
		this.areaCityId = areaCityId;
	}

	public Integer getProvinceId(){
		return provinceId;
	}

	public void setProvinceId(Integer provinceId){
		this.provinceId = provinceId;
	}

	public String getCityName(){
		return cityName;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityCode(){
		return cityCode;
	}

	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
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