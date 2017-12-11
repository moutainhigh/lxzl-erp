package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class ChangeOrderMaterialBulkSrcDO  extends BaseDO {

	private Integer id;
	private Integer changeOrderMaterialId;
	private Integer changeOrderId;
	private String changeOrderNo;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private String orderNo;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getChangeOrderMaterialId(){
		return changeOrderMaterialId;
	}

	public void setChangeOrderMaterialId(Integer changeOrderMaterialId){
		this.changeOrderMaterialId = changeOrderMaterialId;
	}

	public Integer getChangeOrderId(){
		return changeOrderId;
	}

	public void setChangeOrderId(Integer changeOrderId){
		this.changeOrderId = changeOrderId;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
	}

	public Integer getBulkMaterialId(){
		return bulkMaterialId;
	}

	public void setBulkMaterialId(Integer bulkMaterialId){
		this.bulkMaterialId = bulkMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
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

}