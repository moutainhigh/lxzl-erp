package com.lxzl.erp.dataaccess.domain.k3.returnOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class K3ReturnOrderDetailDO  extends BaseDO {

	private Integer id;
	private Integer returnOrderId;
	private String orderNo;
	private String orderItemId;
	private String orderEntry;
	private String productNo;
	private String productName;
	private Integer productCount;
	private Integer realProductCount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getReturnOrderId(){
		return returnOrderId;
	}

	public void setReturnOrderId(Integer returnOrderId){
		this.returnOrderId = returnOrderId;
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

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getRealProductCount() {
		return realProductCount;
	}

	public void setRealProductCount(Integer realProductCount) {
		this.realProductCount = realProductCount;
	}
}