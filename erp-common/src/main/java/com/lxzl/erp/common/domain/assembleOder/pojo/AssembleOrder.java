package com.lxzl.erp.common.domain.assembleOder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrder extends BasePO {
	@NotNull(message = ErrorCode.ASSEMBLE_ORDER_ID_NOT_NULL, groups = {UpdateGroup.class,IdGroup.class})
	private Integer assembleOrderId;   //唯一标识
	private String assembleOrderNo;   //组装单编号
	private Integer assembleProductId;   //组装商品ID
	@NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL ,groups = {AddGroup.class})
	private Integer assembleProductSkuId;   //组装商品SKU ID
	@Min(value = 1, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class})
	@NotNull(message = ErrorCode.PRODUCT_SKU_COUNT_ERROR, groups = {AddGroup.class})
	private Integer assembleProductCount;   //组装商品数量
	@NotNull(message = ErrorCode.WAREHOUSE_ID_NOT_NULL ,groups = {AddGroup.class})
	private Integer warehouseId;   //仓库ID，为哪个库房组装
	private Integer assembleOrderStatus;   //组装单状态，0初始化，4组装成功，8取消组装
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	@Valid
	@CollectionNotNull(message = ErrorCode.ASSEMBLE_ORDER_MATERIAL_NOT_NULL ,groups = {AddGroup.class})
	private List<AssembleOrderMaterial> assembleOrderMaterialList;  //组装单配件

	public List<AssembleOrderMaterial> getAssembleOrderMaterialList() {
		return assembleOrderMaterialList;
	}

	public void setAssembleOrderMaterialList(List<AssembleOrderMaterial> assembleOrderMaterialList) {
		this.assembleOrderMaterialList = assembleOrderMaterialList;
	}

	public Integer getAssembleOrderId(){
		return assembleOrderId;
	}

	public void setAssembleOrderId(Integer assembleOrderId){
		this.assembleOrderId = assembleOrderId;
	}

	public String getAssembleOrderNo(){
		return assembleOrderNo;
	}

	public void setAssembleOrderNo(String assembleOrderNo){
		this.assembleOrderNo = assembleOrderNo;
	}

	public Integer getAssembleProductId(){
		return assembleProductId;
	}

	public void setAssembleProductId(Integer assembleProductId){
		this.assembleProductId = assembleProductId;
	}

	public Integer getAssembleProductSkuId(){
		return assembleProductSkuId;
	}

	public void setAssembleProductSkuId(Integer assembleProductSkuId){
		this.assembleProductSkuId = assembleProductSkuId;
	}

	public Integer getAssembleProductCount(){
		return assembleProductCount;
	}

	public void setAssembleProductCount(Integer assembleProductCount){
		this.assembleProductCount = assembleProductCount;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getAssembleOrderStatus(){
		return assembleOrderStatus;
	}

	public void setAssembleOrderStatus(Integer assembleOrderStatus){
		this.assembleOrderStatus = assembleOrderStatus;
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