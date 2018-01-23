package com.lxzl.erp.common.domain.userLoginLog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginLog extends BasePO {

	private Integer userLoginLogId;   //唯一标识
	private String userName;   //用户名
	private String loginIp;   //登录IP
	private String loginMacAddress;   //登录IP
	private String remark;   //备注
	private Date createTime;   //添加时间


	public Integer getUserLoginLogId(){
		return userLoginLogId;
	}

	public void setUserLoginLogId(Integer userLoginLogId){
		this.userLoginLogId = userLoginLogId;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getLoginIp(){
		return loginIp;
	}

	public void setLoginIp(String loginIp){
		this.loginIp = loginIp;
	}

	public String getLoginMacAddress(){
		return loginMacAddress;
	}

	public void setLoginMacAddress(String loginMacAddress){
		this.loginMacAddress = loginMacAddress;
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

}