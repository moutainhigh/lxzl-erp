package com.lxzl.erp.dataaccess.domain.returnOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class ReturnOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer returnOrderId;
	private String returnOrderNo;
	private Integer returnProductSkuId;
	private Integer returnProductSkuCount;
	private Integer realReturnProductSkuCount;
	private String returnProductSkuSnapshot;
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

	public Integer getReturnProductSkuId(){
		return returnProductSkuId;
	}

	public void setReturnProductSkuId(Integer returnProductSkuId){
		this.returnProductSkuId = returnProductSkuId;
	}

	public Integer getReturnProductSkuCount(){
		return returnProductSkuCount;
	}

	public void setReturnProductSkuCount(Integer returnProductSkuCount){
		this.returnProductSkuCount = returnProductSkuCount;
	}

	public Integer getRealReturnProductSkuCount(){
		return realReturnProductSkuCount;
	}

	public void setRealReturnProductSkuCount(Integer realReturnProductSkuCount){
		this.realReturnProductSkuCount = realReturnProductSkuCount;
	}

	public String getReturnProductSkuSnapshot(){
		return returnProductSkuSnapshot;
	}

	public void setReturnProductSkuSnapshot(String returnProductSkuSnapshot){
		this.returnProductSkuSnapshot = returnProductSkuSnapshot;
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