package com.lxzl.erp.common.domain.interfaceSwitch.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Switch extends BasePO {

	@NotNull(message = ErrorCode.SWITCH_ID_NOT_NULL, groups = {IdGroup.class,UpdateGroup.class})
	private Integer switchId;   //唯一标识
	@NotBlank(message = ErrorCode.SWITCH_INTERFACE_URL_NOT_NULL, groups = {AddGroup.class,UpdateGroup.class})
	private String interfaceUrl;   //接口URL
	@NotNull(message = ErrorCode.SWITCH_IS_OPEN_NOT_NULL, groups = {UpdateGroup.class})
	@In(value = {CommonConstant.YES,CommonConstant.NO},message = ErrorCode.SWITCH_IS_OPEN_IS_FAIL,groups = {UpdateGroup.class})
	private Integer isOpen;   //是否开启，0-否，1-是
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getSwitchId(){
		return switchId;
	}

	public void setSwitchId(Integer switchId){
		this.switchId = switchId;
	}

	public String getInterfaceUrl(){
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl){
		this.interfaceUrl = interfaceUrl;
	}

	public Integer getIsOpen(){
		return isOpen;
	}

	public void setIsOpen(Integer isOpen){
		this.isOpen = isOpen;
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