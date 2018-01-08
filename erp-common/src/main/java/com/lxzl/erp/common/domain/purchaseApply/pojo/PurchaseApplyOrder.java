package com.lxzl.erp.common.domain.purchaseApply.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.springframework.data.annotation.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyOrder extends BasePO {

	private Integer purchaseApplyOrderId;   //唯一标识
	@NotNull(message = ErrorCode.PURCHASE_APPLY_ORDER_NO_NOT_NULL,groups = {IdGroup.class})
	private String purchaseApplyOrderNo;   //申请单编号
	private Integer applyUserId;   //申请人ID
	private Integer warehouseId;   //仓库ID，总公司可以给分公司采购，申请人为分公司，则分公司只允许采购到本分公司仓库
	@NotNull(message = ErrorCode.DEPARTMENT_ID_NOT_NULL,groups = {AddGroup.class,UpdateGroup.class})
	private Integer departmentId;   //部门ID
	private Integer purchaseApplyOrderStatus;   //采购申请单状态，0-待提交，3-审核中，6-待采购，9-采购中，12-部分采购，15-全部采购，18-取消
	private Date allUseTime;   //整单计划使用时间
	private Date purchaseStartTime;   //采购开始时间
	private Date purchaseEndTime;   //全部采购完成时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	@Valid
	private List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList;
	@Valid
	private List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList;

	private String applyUserName;
	private String warehouseName;
	private String departmentName;


	public Integer getPurchaseApplyOrderId(){
		return purchaseApplyOrderId;
	}

	public void setPurchaseApplyOrderId(Integer purchaseApplyOrderId){
		this.purchaseApplyOrderId = purchaseApplyOrderId;
	}

	public String getPurchaseApplyOrderNo(){
		return purchaseApplyOrderNo;
	}

	public void setPurchaseApplyOrderNo(String purchaseApplyOrderNo){
		this.purchaseApplyOrderNo = purchaseApplyOrderNo;
	}

	public Integer getApplyUserId(){
		return applyUserId;
	}

	public void setApplyUserId(Integer applyUserId){
		this.applyUserId = applyUserId;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getDepartmentId(){
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId){
		this.departmentId = departmentId;
	}

	public Integer getPurchaseApplyOrderStatus(){
		return purchaseApplyOrderStatus;
	}

	public void setPurchaseApplyOrderStatus(Integer purchaseApplyOrderStatus){
		this.purchaseApplyOrderStatus = purchaseApplyOrderStatus;
	}

	public Date getAllUseTime(){
		return allUseTime;
	}

	public void setAllUseTime(Date allUseTime){
		this.allUseTime = allUseTime;
	}

	public Date getPurchaseStartTime(){
		return purchaseStartTime;
	}

	public void setPurchaseStartTime(Date purchaseStartTime){
		this.purchaseStartTime = purchaseStartTime;
	}

	public Date getPurchaseEndTime(){
		return purchaseEndTime;
	}

	public void setPurchaseEndTime(Date purchaseEndTime){
		this.purchaseEndTime = purchaseEndTime;
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

	public List<PurchaseApplyOrderProduct> getPurchaseApplyOrderProductList() {
		return purchaseApplyOrderProductList;
	}

	public void setPurchaseApplyOrderProductList(List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList) {
		this.purchaseApplyOrderProductList = purchaseApplyOrderProductList;
	}

	public List<PurchaseApplyOrderMaterial> getPurchaseApplyOrderMaterialList() {
		return purchaseApplyOrderMaterialList;
	}

	public void setPurchaseApplyOrderMaterialList(List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList) {
		this.purchaseApplyOrderMaterialList = purchaseApplyOrderMaterialList;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}