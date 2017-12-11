package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class ChangeOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer changeOrderId;
	private String changeOrderNo;
	private Integer changeMaterialIdSrc;
	private Integer changeMaterialIdDest;
	private Integer changeMaterialCount;
	private Integer realChangeMaterialCount;
	private String changeMaterialSnapshotSrc;
	private String changeMaterialSnapshotDest;
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

	public Integer getChangeMaterialIdSrc(){
		return changeMaterialIdSrc;
	}

	public void setChangeMaterialIdSrc(Integer changeMaterialIdSrc){
		this.changeMaterialIdSrc = changeMaterialIdSrc;
	}

	public Integer getChangeMaterialIdDest(){
		return changeMaterialIdDest;
	}

	public void setChangeMaterialIdDest(Integer changeMaterialIdDest){
		this.changeMaterialIdDest = changeMaterialIdDest;
	}

	public Integer getChangeMaterialCount(){
		return changeMaterialCount;
	}

	public void setChangeMaterialCount(Integer changeMaterialCount){
		this.changeMaterialCount = changeMaterialCount;
	}

	public Integer getRealChangeMaterialCount(){
		return realChangeMaterialCount;
	}

	public void setRealChangeMaterialCount(Integer realChangeMaterialCount){
		this.realChangeMaterialCount = realChangeMaterialCount;
	}

	public String getChangeMaterialSnapshotSrc(){
		return changeMaterialSnapshotSrc;
	}

	public void setChangeMaterialSnapshotSrc(String changeMaterialSnapshotSrc){
		this.changeMaterialSnapshotSrc = changeMaterialSnapshotSrc;
	}

	public String getChangeMaterialSnapshotDest(){
		return changeMaterialSnapshotDest;
	}

	public void setChangeMaterialSnapshotDest(String changeMaterialSnapshotDest){
		this.changeMaterialSnapshotDest = changeMaterialSnapshotDest;
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