package com.lxzl.erp.common.domain.transferOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrder extends BasePO {

	private Integer transferOrderId;   //唯一标识
	@NotBlank(message = ErrorCode.TRANSFER_ORDER_NO_NOT_NULL ,groups = {IdGroup.class, UpdateTransferOrderIntoGroup.class,UpdateTransferOrderOutGroup.class,TransferOrderProductEquipmentOutGroup.class,DumpTransferOrderProductEquipmentOutGroup.class})
	private String transferOrderNo;   //转移单编号
	@NotBlank(message = ErrorCode.TRANSFER_ORDER_NAME_NOT_NULL ,groups = {AddGroup.class,TransferOrderOutGroup.class,UpdateTransferOrderIntoGroup.class,UpdateTransferOrderOutGroup.class})
	private String transferOrderName;   //转移单名称
	private Integer transferOrderStatus;   //转移单状态，0初始化，2备货中,4审批中，8转移成功，16取消转移，20转移结束
	private Integer transferOrderMode;   //转移方式，1转入，2转出（凭空转入转出）
	@NotNull(message = ErrorCode.TRANSFER_ORDER_TYPE_NOT_NULL ,groups = {AddGroup.class,TransferOrderOutGroup.class})
	private Integer transferOrderType;   //	转入类型：1外借入库转入，2试验机转入，3原有资产，99其他。 转出类型：51丢失，52售出，53试验机归还，99其他
	private Integer warehouseId;   //仓库ID，哪个库房转移
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	@Valid
	private List<TransferOrderProduct> transferOrderProductList;
	@Valid
	private List<TransferOrderMaterial> transferOrderMaterialList;

	private String warehouseName;

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public String getTransferOrderNo(){
		return transferOrderNo;
	}

	public void setTransferOrderNo(String transferOrderNo){
		this.transferOrderNo = transferOrderNo;
	}

	public String getTransferOrderName(){
		return transferOrderName;
	}

	public void setTransferOrderName(String transferOrderName){
		this.transferOrderName = transferOrderName;
	}

	public Integer getTransferOrderStatus(){
		return transferOrderStatus;
	}

	public void setTransferOrderStatus(Integer transferOrderStatus){
		this.transferOrderStatus = transferOrderStatus;
	}

	public Integer getTransferOrderMode(){
		return transferOrderMode;
	}

	public void setTransferOrderMode(Integer transferOrderMode){
		this.transferOrderMode = transferOrderMode;
	}

	public Integer getTransferOrderType(){
		return transferOrderType;
	}

	public void setTransferOrderType(Integer transferOrderType){
		this.transferOrderType = transferOrderType;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
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

	public List<TransferOrderProduct> getTransferOrderProductList() {
		return transferOrderProductList;
	}

	public void setTransferOrderProductList(List<TransferOrderProduct> transferOrderProductList) {
		this.transferOrderProductList = transferOrderProductList;
	}

	public List<TransferOrderMaterial> getTransferOrderMaterialList() {
		return transferOrderMaterialList;
	}

	public void setTransferOrderMaterialList(List<TransferOrderMaterial> transferOrderMaterialList) {
		this.transferOrderMaterialList = transferOrderMaterialList;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
}