package com.lxzl.erp.dataaccess.domain.userLoginLog;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class UserLoginLogDO  extends BaseDO {

	private Integer id;
	private String userName;
	private String loginIp;
	private String loginMacAddress;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}