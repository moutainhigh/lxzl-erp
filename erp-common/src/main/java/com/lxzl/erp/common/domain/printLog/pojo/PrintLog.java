package com.lxzl.erp.common.domain.printLog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PrintLogReferType;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintLog extends BasePO {

	private Integer printLogId;   //唯一标识
	@NotBlank(message = ErrorCode.PRINT_LOG_REFER_NO_NOT_NULL,groups = {AddGroup.class})
	private String referNo;   //关联NO
	@NotNull(message = ErrorCode.PRINT_LOG_REFER_TYPE_NOT_NULL,groups = {AddGroup.class})
	@In(value = {PrintLogReferType.ORDER_TYPE_CHANGE,PrintLogReferType.ORDER_TYPE_RETURN},message = ErrorCode.PRINT_LOG_REFER_TYPE_ERROR,groups = {AddGroup.class})
	private Integer referType;   //关联项类型，1-交货单,2-退货单
	private Integer printCount;   //打印次数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getPrintLogId(){
		return printLogId;
	}

	public void setPrintLogId(Integer printLogId){
		this.printLogId = printLogId;
	}

	public String getReferNo(){
		return referNo;
	}

	public void setReferNo(String referNo){
		this.referNo = referNo;
	}

	public Integer getReferType(){
		return referType;
	}

	public void setReferType(Integer referType){
		this.referType = referType;
	}

	public Integer getPrintCount(){
		return printCount;
	}

	public void setPrintCount(Integer printCount){
		this.printCount = printCount;
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