package com.lxzl.erp.web.config;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class LoginConfig {

	public String[] loginExcludeUrls;

	public String loginCookieName;

	public String loginAuthKey;

	public String validateLoginUrl;

	public int maxKeepTime;

	public String application;

	public boolean isSingleClient;

	public String[] getLoginExcludeUrls() {
		return loginExcludeUrls;
	}

	public void setLoginExcludeUrls(String[] loginExcludeUrls) {
		this.loginExcludeUrls = loginExcludeUrls;
	}

	public String getLoginCookieName() {
		return loginCookieName;
	}

	public void setLoginCookieName(String loginCookieName) {
		this.loginCookieName = loginCookieName;
	}

	public String getLoginAuthKey() {
		return loginAuthKey;
	}

	public void setLoginAuthKey(String loginAuthKey) {
		this.loginAuthKey = loginAuthKey;
	}

	public String getValidateLoginUrl() {
		return validateLoginUrl;
	}

	public void setValidateLoginUrl(String validateLoginUrl) {
		this.validateLoginUrl = validateLoginUrl;
	}

	public int getMaxKeepTime() {
		return maxKeepTime;
	}

	public void setMaxKeepTime(int maxKeepTime) {
		this.maxKeepTime = maxKeepTime;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public boolean getIsSingleClient() {
		return isSingleClient;
	}

	public void setIsSingleClient(boolean isSingleClient) {
		this.isSingleClient = isSingleClient;
	}

	@Override
	public String toString() {
		return "LoginConfig [loginExcludeUrls=" + Arrays.toString(loginExcludeUrls) + ", loginCookieName=" + loginCookieName + ", loginAuthKey=" + loginAuthKey
				+ ", validateLoginUrl=" + validateLoginUrl + ", maxKeepTime=" + maxKeepTime + ", application=" + application + ", isSingleClient="
				+ isSingleClient + "]";
	}

}
