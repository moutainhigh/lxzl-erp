package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class ChangeOrderProductEquipmentSrcDO  extends BaseDO {

	private Integer id;
	private Integer changeOrderProductId;
	private Integer changeOrderId;
	private String changeOrderNo;
	private String orderNo;
	private Integer equipmentId;
	private String equipmentNo;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getChangeOrderProductId(){
		return changeOrderProductId;
	}

	public void setChangeOrderProductId(Integer changeOrderProductId){
		this.changeOrderProductId = changeOrderProductId;
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

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getEquipmentId(){
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId){
		this.equipmentId = equipmentId;
	}

	public String getEquipmentNo(){
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo){
		this.equipmentNo = equipmentNo;
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