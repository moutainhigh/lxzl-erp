package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class MaterialImgDO  extends BaseDO {

	private Integer id;
	private Integer imgType;
	private String originalName;
	private Integer materialId;
	private String imgUrl;
	private Integer isMain;
	private Integer imgOrder;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}