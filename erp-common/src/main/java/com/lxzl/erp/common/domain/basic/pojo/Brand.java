package com.lxzl.erp.common.domain.basic.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand implements Serializable {

	private Integer brandId;   //唯一标识
	private String brandName;   //品牌名称
	private String brandEnglishName;   //品牌英文名称
	private String brandDesc;   //品牌描述
	private String brandStory;   //品牌故事
	private String logoUrl;   //logo地址
	private String homeUrl;   //官网地址
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getBrandId(){
		return brandId;
	}

	public void setBrandId(Integer brandId){
		this.brandId = brandId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}