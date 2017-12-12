package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class ChangeOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer changeOrderId;
	private String changeOrderNo;
	private Integer changeProductSkuIdSrc;
	private Integer changeProductSkuIdDest;
	private Integer changeProductSkuCount;
	private Integer realChangeProductSkuCount;
	private String changeProductSkuSnapshotSrc;
	private String changeProductSkuSnapshotDest;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public Integer getChangeProductSkuIdSrc(){
		return changeProductSkuIdSrc;
	}

	public void setChangeProductSkuIdSrc(Integer changeProductSkuIdSrc){
		this.changeProductSkuIdSrc = changeProductSkuIdSrc;
	}

	public Integer getChangeProductSkuIdDest(){
		return changeProductSkuIdDest;
	}

	public void setChangeProductSkuIdDest(Integer changeProductSkuIdDest){
		this.changeProductSkuIdDest = changeProductSkuIdDest;
	}

	public Integer getChangeProductSkuCount(){
		return changeProductSkuCount;
	}

	public void setChangeProductSkuCount(Integer changeProductSkuCount){
		this.changeProductSkuCount = changeProductSkuCount;
	}

	public Integer getRealChangeProductSkuCount(){
		return realChangeProductSkuCount;
	}

	public void setRealChangeProductSkuCount(Integer realChangeProductSkuCount){
		this.realChangeProductSkuCount = realChangeProductSkuCount;
	}

	public String getChangeProductSkuSnapshotSrc(){
		return changeProductSkuSnapshotSrc;
	}

	public void setChangeProductSkuSnapshotSrc(String changeProductSkuSnapshotSrc){
		this.changeProductSkuSnapshotSrc = changeProductSkuSnapshotSrc;
	}

	public String getChangeProductSkuSnapshotDest(){
		return changeProductSkuSnapshotDest;
	}

	public void setChangeProductSkuSnapshotDest(String changeProductSkuSnapshotDest){
		this.changeProductSkuSnapshotDest = changeProductSkuSnapshotDest;
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