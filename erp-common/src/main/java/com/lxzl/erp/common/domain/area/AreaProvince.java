package com.lxzl.erp.common.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;



@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaProvince implements Serializable {

	private Long areaProvinceId;   //唯一标识
	private String provinceName;   //地区名称
	private Integer areaType;   //区域类型，1-华东，2-华南，3-华中，4-华北，5-西北，6-西南，7-东北，8-港澳台
	private String abbCn;   //中文简称
	private String abbEn;   //英文简称
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注


	public Long getAreaProvinceId(){
		return areaProvinceId;
	}

	public void setAreaProvinceId(Long areaProvinceId){
		this.areaProvinceId = areaProvinceId;
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