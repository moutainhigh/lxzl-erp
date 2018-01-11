package com.lxzl.erp.common.domain.transferOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrderProduct extends BasePO {

	private Integer transferOrderProductId;   //唯一标识
	private Integer transferOrderId;   //转移单ID
	private Integer productId;   //转移商品ID
	@NotNull(message = ErrorCode.TRANSFER_ORDER_PRODUCT_SKU_ID_NOT_NULL ,groups = {AddGroup.class})
	private Integer productSkuId;   //转移商品SKU ID
	@NotNull(message = ErrorCode.TRANSFER_ORDER_PRODUCT_COUNT_NOT_NULL ,groups = {AddGroup.class})
	private Integer productCount;   //商品数量
	@NotNull(message = ErrorCode.TRANSFER_ORDER_PRODUCT_IS_NEW_NOT_NULL ,groups = {AddGroup.class})
	private Integer isNew;   //是否全新，1是，0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	@Valid
	private List<TransferOrderProductEquipment> transferOrderProductEquipmentList;


	public Integer getTransferOrderProductId(){
		return transferOrderProductId;
	}

	public void setTransferOrderProductId(Integer transferOrderProductId){
		this.transferOrderProductId = transferOrderProductId;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
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

	public List<TransferOrderProductEquipment> getTransferOrderProductEquipmentList() {
		return transferOrderProductEquipmentList;
	}

	public void setTransferOrderProductEquipmentList(List<TransferOrderProductEquipment> transferOrderProductEquipmentList) {
		this.transferOrderProductEquipmentList = transferOrderProductEquipmentList;
	}
}