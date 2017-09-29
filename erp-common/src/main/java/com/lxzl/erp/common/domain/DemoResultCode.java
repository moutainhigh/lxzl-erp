package com.lxzl.erp.common.domain;

import com.lxzl.se.common.domain.ResultCodeEnum;

public enum DemoResultCode implements ResultCodeEnum {

	A_BUSINESS_EXCEPTION("J023007", "用户名不允许为空");

	/**
	 * 结果码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String description;

	private DemoResultCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
