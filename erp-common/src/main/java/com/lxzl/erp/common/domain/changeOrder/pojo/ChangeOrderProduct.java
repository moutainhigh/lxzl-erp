package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderProduct implements Serializable {

	private Integer changeOrderProductId;   //唯一标识
	private Integer changeOrderId;   //换货ID
	private String changeOrderNo;   //换货编号
	private Integer changeProductSkuIdSrc;   //换货前商品SKU_ID
	private Integer changeProductSkuIdDest;   //换货后商品SKU_ID
	private Integer changeProductSkuCount;   //换货商品SKU数量
	private Integer realChangeProductSkuCount;   //实际换货数量
	private String changeProductSkuSnapshotSrc;   //换货前商品SKU快照
	private String changeProductSkuSnapshotDest;   //换货后商品SKU快照
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


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