package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class K3ChangeOrderDetailDO  extends BaseDO {

	private Integer id;
	private Integer changeOrderId;
	private String orderNo;
	private String orderEntry;
	private String productNo;
	private String productName;
	private Integer changeSkuId;
	private Integer changeMaterialId;
	private String changeProductNo;
	private String changeProductName;
	private Integer productCount;
	private BigDecimal productDiffAmount;
	private Integer dataStatus;
	private String remark;
	private String orderItemId;
	private Integer rentType;

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

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getRentType() {
		return rentType;
	}

	public void setRentType(Integer rentType) {
		this.rentType = rentType;
	}
}