package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderMaterial extends BasePO {

	private Integer peerDeploymentOrderMaterialId;   //唯一标识
	private Integer peerDeploymentOrderId;   //货物调拨单ID
	private String peerDeploymentOrderNo;   //货物调拨单编号
	private Integer materialId;   //货物调拨配件ID
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_UNIT_AMOUNT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal materialUnitAmount;   //配件单价
	@NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddGroup.class, UpdateGroup.class})
	private String materialNo;   //配件编号
	private BigDecimal materialAmount;   //配件总价格
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_COUNT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	private Integer materialCount;   //货物调拨配件数量
	private String materialSnapshot;   //货物调拨配件快照
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_IS_NEW_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	@In(value = {CommonConstant.YES, CommonConstant.NO}, message = ErrorCode.IS_NEW_VALUE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
	private Integer isNew;   //是否全新机
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<PeerDeploymentOrderMaterialBulk> peerDeploymentOrderMaterialBulkList;


	public Integer getPeerDeploymentOrderMaterialId(){
		return peerDeploymentOrderMaterialId;
	}

	public void setPeerDeploymentOrderMaterialId(Integer peerDeploymentOrderMaterialId){ this.peerDeploymentOrderMaterialId = peerDeploymentOrderMaterialId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public BigDecimal getMaterialUnitAmount(){
		return materialUnitAmount;
	}

	public void setMaterialUnitAmount(BigDecimal materialUnitAmount){
		this.materialUnitAmount = materialUnitAmount;
	}

	public BigDecimal getMaterialAmount(){
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount){
		this.materialAmount = materialAmount;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer productMaterialCount){
		this.materialCount = materialCount;
	}

	public String getMaterialSnapshot(){
		return materialSnapshot;
	}

	public void setMaterialSnapshot(String productMaterialSnapshot){ this.materialSnapshot = materialSnapshot; }

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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

	public List<PeerDeploymentOrderMaterialBulk> getPeerDeploymentOrderMaterialBulkList() { return peerDeploymentOrderMaterialBulkList; }

	public void setPeerDeploymentOrderMaterialBulkList(List<PeerDeploymentOrderMaterialBulk> peerDeploymentOrderMaterialBulkList) { this.peerDeploymentOrderMaterialBulkList = peerDeploymentOrderMaterialBulkList; }

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
}