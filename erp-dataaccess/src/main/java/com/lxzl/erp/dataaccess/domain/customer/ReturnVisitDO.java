package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class ReturnVisitDO  extends BaseDO {

	private Integer id;
	private String returnVisitDescribe;
	private String customerNo;
	private Integer dataStatus;
	private String remark;

	@Transient
	private List<ImageDO> customerReturnVisitImageDOList; //客户回访图片


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public List<ImageDO> getCustomerReturnVisitImageDOList() {
		return customerReturnVisitImageDOList;
	}

	public void setCustomerReturnVisitImageDOList(List<ImageDO> customerReturnVisitImageDOList) {
		this.customerReturnVisitImageDOList = customerReturnVisitImageDOList;
	}

}