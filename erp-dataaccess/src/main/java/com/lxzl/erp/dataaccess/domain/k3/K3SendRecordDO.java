package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class K3SendRecordDO  extends BaseDO {

	private Integer id;
	private String recordType;
	private String recordJson;
	private String sendResult;
	private String receiveResult;
	private Date sendTime;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getRecordType(){
		return recordType;
	}

	public void setRecordType(String recordType){
		this.recordType = recordType;
	}

	public String getRecordJson(){
		return recordJson;
	}

	public void setRecordJson(String recordJson){
		this.recordJson = recordJson;
	}

	public String getSendResult(){
		return sendResult;
	}

	public void setSendResult(String sendResult){
		this.sendResult = sendResult;
	}

	public String getReceiveResult(){
		return receiveResult;
	}

	public void setReceiveResult(String receiveResult){
		this.receiveResult = receiveResult;
	}

	public Date getSendTime(){
		return sendTime;
	}

	public void setSendTime(Date sendTime){
		this.sendTime = sendTime;
	}

}