package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class K3SendRecordDO  extends BaseDO {

	private Integer id;
	private Integer recordType;
	private String recordJson;
	private String responseJson;
	private Integer sendResult;
	private Integer receiveResult;
	private Date sendTime;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getRecordType(){
		return recordType;
	}

	public void setRecordType(Integer recordType){
		this.recordType = recordType;
	}

	public String getRecordJson(){
		return recordJson;
	}

	public void setRecordJson(String recordJson){
		this.recordJson = recordJson;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public Integer getSendResult(){
		return sendResult;
	}

	public void setSendResult(Integer sendResult){
		this.sendResult = sendResult;
	}

	public Integer getReceiveResult(){
		return receiveResult;
	}

	public void setReceiveResult(Integer receiveResult){
		this.receiveResult = receiveResult;
	}

	public Date getSendTime(){
		return sendTime;
	}

	public void setSendTime(Date sendTime){
		this.sendTime = sendTime;
	}

}