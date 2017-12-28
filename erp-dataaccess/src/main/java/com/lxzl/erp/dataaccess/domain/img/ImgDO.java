package com.lxzl.erp.dataaccess.domain.img;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class ImgDO  extends BaseDO {

	private Integer id;
	private Integer imgType;
	private String originalName;
	private String refId;
	private String imgUrl;
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

	public String getRefId(){
		return refId;
	}

	public void setRefId(String refId){
		this.refId = refId;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
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