package com.lxzl.erp.dataaccess.domain;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

public class LogDO extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1302361476647649564L;

	/**
	 * 未处理
	 */
	public static final Integer STATUS_UNPROCESS = 0;

	/**
	 * 处理中
	 */
	public static final Integer STATUS_PROCESSING = 1;

	/**
	 * 已处理失败
	 */
	public static final Integer STATUS_PROCESSED_FAILED = 2;

	/**
	 * 已处理成功
	 */
	public static final Integer STATUS_PROCESSED_SUCCESS = 3;

	private Integer id;

	private String content;

	private Integer executeTimes;

	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getExecuteTimes() {
		return executeTimes;
	}

	public void setExecuteTimes(Integer executeTimes) {
		this.executeTimes = executeTimes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LogDO [id=" + id + ", content=" + content + ", executeTimes=" + executeTimes + ", status=" + status + "]";
	}

}
