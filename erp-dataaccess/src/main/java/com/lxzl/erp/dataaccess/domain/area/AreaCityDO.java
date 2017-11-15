package com.lxzl.erp.dataaccess.domain.area;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class AreaCityDO  extends BaseDO {

	private Integer id;
	private Integer provinceId;
	private String cityName;
	private String cityCode;
	private String postCode;
	private String abbCn;
	private String abbEn;
	private Integer dataStatus;
	private String remark;

	@Transient
	private String provinceName;

	@Transient
	private List<AreaDistrictDO> areaDistrictDOList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public List<AreaDistrictDO> getAreaDistrictDOList() {
		return areaDistrictDOList;
	}

	public void setAreaDistrictDOList(List<AreaDistrictDO> areaDistrictDOList) {
		this.areaDistrictDOList = areaDistrictDOList;
	}
}