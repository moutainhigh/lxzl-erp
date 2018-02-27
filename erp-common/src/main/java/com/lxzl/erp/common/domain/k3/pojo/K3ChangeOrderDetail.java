package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ChangeOrderDetail extends BasePO {

	private Integer k3ChangeOrderDetailId;   //唯一标识
	private Integer changeOrderId;   //K3换货单ID
	private String orderNo;   //订单号
	private String orderEntry;   //订单行号
	private String productNo;   //产品代码
	private String productName;   //产品名称
	private Integer changeSkuId;   //SKU ID
	private Integer changeMaterialId;   //物料 ID
	private String changeProductNo;   //换货产品代码
	private String changeProductName;   //换货产品名称
	private Integer productCount;   //换货数量
	private BigDecimal productDiffAmount;   //商品差价
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getK3ChangeOrderDetailId(){
		return k3ChangeOrderDetailId;
	}

	public void setK3ChangeOrderDetailId(Integer k3ChangeOrderDetailId){
		this.k3ChangeOrderDetailId = k3ChangeOrderDetailId;
	}

	public Integer getChangeOrderId(){
		return changeOrderId;
	}

	public void setChangeOrderId(Integer changeOrderId){
		this.changeOrderId = changeOrderId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public String getOrderEntry(){
		return orderEntry;
	}

	public void setOrderEntry(String orderEntry){
		this.orderEntry = orderEntry;
	}

	public String getProductNo(){
		return productNo;
	}

	public void setProductNo(String productNo){
		this.productNo = productNo;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public Integer getChangeSkuId(){
		return changeSkuId;
	}

	public void setChangeSkuId(Integer changeSkuId){
		this.changeSkuId = changeSkuId;
	}

	public Integer getChangeMaterialId(){
		return changeMaterialId;
	}

	public void setChangeMaterialId(Integer changeMaterialId){
		this.changeMaterialId = changeMaterialId;
	}

	public String getChangeProductNo(){
		return changeProductNo;
	}

	public void setChangeProductNo(String changeProductNo){
		this.changeProductNo = changeProductNo;
	}

	public String getChangeProductName(){
		return changeProductName;
	}

	public void setChangeProductName(String changeProductName){
		this.changeProductName = changeProductName;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
	}

	public BigDecimal getProductDiffAmount(){
		return productDiffAmount;
	}

	public void setProductDiffAmount(BigDecimal productDiffAmount){
		this.productDiffAmount = productDiffAmount;
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