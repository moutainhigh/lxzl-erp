package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


public class WorkflowVerifyUserGroupDO  extends BaseDO {

	private Integer id;
	private Integer verifyUserGroupId;
	private Integer verifyType;
	private Integer verifyUser;
	private Date verifyTime;
	private Integer verifyStatus;
	private String verifyOpinion;
	private Integer dataStatus;
	private String remark;

	@Transient
	private String verifyUserName;

	private List<ImageDO> imageDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getVerifyUserGroupId(){
		return verifyUserGroupId;
	}

	public void setVerifyUserGroupId(Integer verifyUserGroupId){
		this.verifyUserGroupId = verifyUserGroupId;
	}

	public Integer getVerifyType(){
		return verifyType;
	}

	public void setVerifyType(Integer verifyType){
		this.verifyType = verifyType;
	}

	public Integer getVerifyUser(){
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser){
		this.verifyUser = verifyUser;
	}

	public Date getVerifyTime(){
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime){
		this.verifyTime = verifyTime;
	}

	public Integer getVerifyStatus(){
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus){
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyOpinion(){
		return verifyOpinion;
	}

	public void setVerifyOpinion(String verifyOpinion){
		this.verifyOpinion = verifyOpinion;
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

	public String getVerifyUserName() { return verifyUserName; }

	public void setVerifyUserName(String verifyUserName) { this.verifyUserName = verifyUserName; }

	public List<ImageDO> getImageDOList() { return imageDOList; }

	public void setImageDOList(List<ImageDO> imageDOList) { this.imageDOList = imageDOList; }
}