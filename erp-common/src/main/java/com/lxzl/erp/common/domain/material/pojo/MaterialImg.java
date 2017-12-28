package com.lxzl.erp.common.domain.material.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialImg extends BasePO {

	private Integer materialImgId;   //图片ID，唯一
	private Integer imgType;   //图片类型
	private String originalName;   //文件原名
	private Integer materialId;   //所属物料ID
	private String imgUrl;   //图片URL
	private Integer isMain;   //是否是主图，0否1是
	private Integer imgOrder;   //图片排序，越大越前面
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	private String imgDomain;	//域名


	public Integer getMaterialImgId(){
		return materialImgId;
	}

	public void setMaterialImgId(Integer materialImgId){
		this.materialImgId = materialImgId;
	}

	public Integer getImgType(){
		return imgType;
	}

	public void setImgType(Integer imgType){
		this.imgType = imgType;
	}

	public String getOriginalName(){
		return originalName;
	}

	public void setOriginalName(String originalName){
		this.originalName = originalName;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}

	public Integer getIsMain(){
		return isMain;
	}

	public void setIsMain(Integer isMain){
		this.isMain = isMain;
	}

	public Integer getImgOrder(){
		return imgOrder;
	}

	public void setImgOrder(Integer imgOrder){
		this.imgOrder = imgOrder;
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

	public String getImgDomain() {
		return imgDomain;
	}

	public void setImgDomain(String imgDomain) {
		this.imgDomain = imgDomain;
	}
}