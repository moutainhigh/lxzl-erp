package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerReturnVisit;
import com.lxzl.erp.common.domain.validGroup.customer.IdCustomerReturnVisit;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerReturnVisit;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnVisit extends BasePO {

	@NotNull(message = ErrorCode.CUSTOMER_RETURN_ID_NOT_NULL,groups={UpdateCustomerReturnVisit.class,IdCustomerReturnVisit.class})
	private Integer returnVisitId;   //唯一标识
	@NotBlank(message = ErrorCode.CUSTOMER_RETURN_VISIT_DESCRIBE_NOT_NULL,groups={AddCustomerReturnVisit.class,UpdateCustomerReturnVisit.class})
	private String returnVisitDescribe;   //回访描述
	@NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups={AddCustomerReturnVisit.class})
	private String customerNo;   //客戶编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	@Valid
	private List<Image> customerReturnVisitImageList; //客户回访图片


	public Integer getReturnVisitId(){
		return returnVisitId;
	}

	public void setReturnVisitId(Integer returnVisitId){
		this.returnVisitId = returnVisitId;
	}

	public String getReturnVisitDescribe(){
		return returnVisitDescribe;
	}

	public void setReturnVisitDescribe(String returnVisitDescribe){
		this.returnVisitDescribe = returnVisitDescribe;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
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

	public List<Image> getCustomerReturnVisitImageList() {
		return customerReturnVisitImageList;
	}

	public void setCustomerReturnVisitImageList(List<Image> customerReturnVisitImageList) {
		this.customerReturnVisitImageList = customerReturnVisitImageList;
	}
}