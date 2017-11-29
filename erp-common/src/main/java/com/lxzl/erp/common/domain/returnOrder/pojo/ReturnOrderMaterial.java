package com.lxzl.erp.common.domain.returnOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderMaterial implements Serializable {

	private Integer returnOrderMaterialId;   //唯一标识
	private Integer returnOrderId;   //退还ID
	private String returnOrderNo;   //退还编号
	private Integer returnMaterialId;   //退还物料ID
	private Integer returnMaterialCount;   //退还物料数量
	private Integer realReturnMaterialCount;   //实际退还物料数量
	private String returnMaterialSnapshot;   //退还物料快照
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getReturnOrderMaterialId(){
		return returnOrderMaterialId;
	}

	public void setReturnOrderMaterialId(Integer returnOrderMaterialId){
		this.returnOrderMaterialId = returnOrderMaterialId;
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