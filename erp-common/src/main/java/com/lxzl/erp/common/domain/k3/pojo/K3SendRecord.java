package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3SendRecord extends BasePO {

	@NotNull(message = ErrorCode.K3_SEND_RECORD_ID_IS_NOT_EXISTS,groups = {IdGroup.class})
	private Integer k3SendRecordId;   //唯一标识
	private Integer recordType;   //记录类型，1推送客户、2推送供应商、3推送商品、4推送配件、5推送订单、6推送用户、7推送退货单
	private String recordJson;   //推送的json数据
	private String responseJson;   //返回的json数据
	private Integer sendResult;   //是否推送成功，1是0否
	private Integer receiveResult;   //是否接收成功，1是0否
	private Date sendTime;   //发送时间
	private Integer recordReferId;	//记录关联ID

	private String recordReferNo;

	public Integer getK3SendRecordId(){ return k3SendRecordId; }

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

	public Integer getRecordReferId() { return recordReferId; }

	public void setRecordReferId(Integer recordReferId) { this.recordReferId = recordReferId; }

	public String getRecordReferNo() { return recordReferNo; }

	public void setRecordReferNo(String recordReferNo) { this.recordReferNo = recordReferNo; }
}