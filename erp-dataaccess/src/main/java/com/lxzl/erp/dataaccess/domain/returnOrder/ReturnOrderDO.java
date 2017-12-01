package com.lxzl.erp.dataaccess.domain.returnOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class ReturnOrderDO  extends BaseDO {

	private Integer id;
	private String returnOrderNo;
	private Integer customerId;
	private String customerNo;
	private Integer isCharging;
	private Integer totalReturnProductCount;
	private Integer totalReturnMaterialCount;
	private Integer realTotalReturnProductCount;
	private Integer realTotalReturnMaterialCount;
	private BigDecimal totalRentCost;
	private BigDecimal serviceCost;
	private BigDecimal damageCost;
	private Integer returnOrderStatus;
	private Integer dataStatus;
	private String remark;
	private Integer owner;

	@Transient
	private ReturnOrderConsignInfoDO returnOrderConsignInfoDO;
	@Transient
	private List<ReturnOrderProductDO> returnOrderProductDOList;
	@Transient
	private List<ReturnOrderMaterialDO> returnOrderMaterialDOList;


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getReturnOrderNo(){
		return returnOrderNo;
	}

	public void setReturnOrderNo(String returnOrderNo){
		this.returnOrderNo = returnOrderNo;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getIsCharging(){
		return isCharging;
	}

	public void setIsCharging(Integer isCharging){
		this.isCharging = isCharging;
	}

	public Integer getTotalReturnProductCount(){
		return totalReturnProductCount;
	}

	public void setTotalReturnProductCount(Integer totalReturnProductCount){
		this.totalReturnProductCount = totalReturnProductCount;
	}

	public Integer getTotalReturnMaterialCount(){
		return totalReturnMaterialCount;
	}

	public void setTotalReturnMaterialCount(Integer totalReturnMaterialCount){
		this.totalReturnMaterialCount = totalReturnMaterialCount;
	}

	public Integer getRealTotalReturnProductCount(){
		return realTotalReturnProductCount;
	}

	public void setRealTotalReturnProductCount(Integer realTotalReturnProductCount){
		this.realTotalReturnProductCount = realTotalReturnProductCount;
	}

	public Integer getRealTotalReturnMaterialCount(){
		return realTotalReturnMaterialCount;
	}

	public void setRealTotalReturnMaterialCount(Integer realTotalReturnMaterialCount){
		this.realTotalReturnMaterialCount = realTotalReturnMaterialCount;
	}

	public BigDecimal getTotalRentCost(){
		return totalRentCost;
	}

	public void setTotalRentCost(BigDecimal totalRentCost){
		this.totalRentCost = totalRentCost;
	}

	public BigDecimal getServiceCost(){
		return serviceCost;
	}

	public void setServiceCost(BigDecimal serviceCost){
		this.serviceCost = serviceCost;
	}

	public BigDecimal getDamageCost(){
		return damageCost;
	}

	public void setDamageCost(BigDecimal damageCost){
		this.damageCost = damageCost;
	}

	public Integer getReturnOrderStatus(){
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus){
		this.returnOrderStatus = returnOrderStatus;
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

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
	}


	public List<ReturnOrderProductDO> getReturnOrderProductDOList() {
		return returnOrderProductDOList;
	}

	public void setReturnOrderProductDOList(List<ReturnOrderProductDO> returnOrderProductDOList) {
		this.returnOrderProductDOList = returnOrderProductDOList;
	}

	public List<ReturnOrderMaterialDO> getReturnOrderMaterialDOList() {
		return returnOrderMaterialDOList;
	}

	public void setReturnOrderMaterialDOList(List<ReturnOrderMaterialDO> returnOrderMaterialDOList) {
		this.returnOrderMaterialDOList = returnOrderMaterialDOList;
	}

	public ReturnOrderConsignInfoDO getReturnOrderConsignInfoDO() {
		return returnOrderConsignInfoDO;
	}

	public void setReturnOrderConsignInfoDO(ReturnOrderConsignInfoDO returnOrderConsignInfoDO) {
		this.returnOrderConsignInfoDO = returnOrderConsignInfoDO;
	}
}