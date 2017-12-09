package com.lxzl.erp.dataaccess.domain.basic;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class BrandDO  extends BaseDO {

	private Integer id;
	private String brandName;
	private String brandEnglishName;
	private String brandDesc;
	private String brandStory;
	private String logoUrl;
	private String homeUrl;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandEnglishName(){
		return brandEnglishName;
	}

	public void setBrandEnglishName(String brandEnglishName){
		this.brandEnglishName = brandEnglishName;
	}

	public String getBrandDesc(){
		return brandDesc;
	}

	public void setBrandDesc(String brandDesc){
		this.brandDesc = brandDesc;
	}

	public String getBrandStory(){
		return brandStory;
	}

	public void setBrandStory(String brandStory){
		this.brandStory = brandStory;
	}

	public String getLogoUrl(){
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl){
		this.logoUrl = logoUrl;
	}

	public String getHomeUrl(){
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl){
		this.homeUrl = homeUrl;
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