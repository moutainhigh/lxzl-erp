package com.lxzl.erp.dataaccess.domain.returnOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class ReturnOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer returnOrderId;
	private String returnOrderNo;
	private Integer returnMaterialId;
	private Integer returnMaterialCount;
	private Integer realReturnMaterialCount;
	private String returnMaterialSnapshot;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getReturnOrderId(){
		return returnOrderId;
	}

	public void setReturnOrderId(Integer returnOrderId){
		this.returnOrderId = returnOrderId;
	}

	public String getReturnOrderNo(){
		return returnOrderNo;
	}

	public void setReturnOrderNo(String returnOrderNo){
		this.returnOrderNo = returnOrderNo;
	}

	public Integer getReturnMaterialId(){
		return returnMaterialId;
	}

	public void setReturnMaterialId(Integer returnMaterialId){
		this.returnMaterialId = returnMaterialId;
	}

	public Integer getReturnMaterialCount(){
		return returnMaterialCount;
	}

	public void setReturnMaterialCount(Integer returnMaterialCount){
		this.returnMaterialCount = returnMaterialCount;
	}

	public Integer getRealReturnMaterialCount(){
		return realReturnMaterialCount;
	}

	public void setRealReturnMaterialCount(Integer realReturnMaterialCount){
		this.realReturnMaterialCount = realReturnMaterialCount;
	}

	public String getReturnMaterialSnapshot() {
		return returnMaterialSnapshot;
	}

	public void setReturnMaterialSnapshot(String returnMaterialSnapshot) {
		this.returnMaterialSnapshot = returnMaterialSnapshot;
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