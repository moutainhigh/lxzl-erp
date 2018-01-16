package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3SendRecord extends BasePO {

	private Integer k3SendRecordId;   //唯一标识
	private Integer recordType;   //记录类型，1客户、2供应商、3商品、4配件、5订单
	private String recordJson;   //推送的json数据
	private String responseJson;   //返回的json数据
	private Integer sendResult;   //是否推送成功，1是0否
	private Integer receiveResult;   //是否接收成功，1是0否
	private Date sendTime;   //发送时间


	public Integer getK3SendRecordId(){
		return k3SendRecordId;
	}

	public void setK3SendRecordId(Integer k3SendRecordId){
		this.k3SendRecordId = k3SendRecordId;
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