package com.lxzl.erp.common.domain.transferOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.TramsferOrderMaterialOutGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.DumpTransferOrderMaterialOutGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrderMaterial extends BasePO {

	private Integer transferOrderMaterialId;   //唯一标识
	@NotNull(message = ErrorCode.TRANSFER_ORDER_ID_NOT_NULL,groups = {UpdateGroup.class,TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
	private Integer transferOrderId;   //转移单ID
	private Integer materialId;   //物料ID
	@NotBlank(message = ErrorCode.TRANSFER_ORDER_MATERIAL_NO_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class,TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
	private String materialNo;   //物料编号
	@NotNull(message = ErrorCode.TRANSFER_ORDER_MATERIAL_COUNT_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class,TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
	private Integer materialCount;   //物料数量
	@NotNull(message = ErrorCode.TRANSFER_ORDER_IS_NEW_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class,TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
	private Integer isNew;   //是否全新，1是，0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<TransferOrderMaterialBulk> transferOrderMaterialBulkList;

	public Integer getTransferOrderMaterialId(){
		return transferOrderMaterialId;
	}

	public void setTransferOrderMaterialId(Integer transferOrderMaterialId){
		this.transferOrderMaterialId = transferOrderMaterialId;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialNo(){
		return materialNo;
	}

	public void setMaterialNo(String materialNo){
		this.materialNo = materialNo;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
	}

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

	public List<TransferOrderMaterialBulk> getTransferOrderMaterialBulkList() {
		return transferOrderMaterialBulkList;
	}

	public void setTransferOrderMaterialBulkList(List<TransferOrderMaterialBulk> transferOrderMaterialBulkList) {
		this.transferOrderMaterialBulkList = transferOrderMaterialBulkList;
	}
}