package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class K3ChangeOrderDO  extends BaseDO {

	private Integer id;
	private String changeOrderNo;
	private String k3CustomerNo;
	private String k3CustomerName;
	private Date changeTime;
	private String changeAddress;
	private String changeContacts;
	private String changePhone;
	private Integer changeMode;
	private BigDecimal logisticsAmount;
	private BigDecimal serviceAmount;
	private Integer changeOrderStatus;
	private Integer dataStatus;
	private String remark;

	private List<K3ChangeOrderDetailDO> k3ChangeOrderDetailDOList;
	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getChangeOrderNo(){
		return changeOrderNo;
	}

	public void setChangeOrderNo(String changeOrderNo){
		this.changeOrderNo = changeOrderNo;
	}

	public String getK3CustomerNo(){
		return k3CustomerNo;
	}

	public void setK3CustomerNo(String k3CustomerNo){
		this.k3CustomerNo = k3CustomerNo;
	}

	public String getK3CustomerName(){
		return k3CustomerName;
	}

	public void setK3CustomerName(String k3CustomerName){
		this.k3CustomerName = k3CustomerName;
	}

	public Date getChangeTime(){
		return changeTime;
	}

	public void setChangeTime(Date changeTime){
		this.changeTime = changeTime;
	}

	public String getChangeAddress(){
		return changeAddress;
	}

	public void setChangeAddress(String changeAddress){
		this.changeAddress = changeAddress;
	}

	public String getChangeContacts(){
		return changeContacts;
	}

	public void setChangeContacts(String changeContacts){
		this.changeContacts = changeContacts;
	}

	public String getChangePhone(){
		return changePhone;
	}

	public void setChangePhone(String changePhone){
		this.changePhone = changePhone;
	}

	public Integer getChangeMode(){
		return changeMode;
	}

	public void setChangeMode(Integer changeMode){
		this.changeMode = changeMode;
	}

	public BigDecimal getLogisticsAmount(){
		return logisticsAmount;
	}

	public void setLogisticsAmount(BigDecimal logisticsAmount){
		this.logisticsAmount = logisticsAmount;
	}

	public BigDecimal getServiceAmount(){
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount){
		this.serviceAmount = serviceAmount;
	}

	public Integer getChangeOrderStatus(){
		return changeOrderStatus;
	}

	public void setChangeOrderStatus(Integer changeOrderStatus){
		this.changeOrderStatus = changeOrderStatus;
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


	public List<K3ChangeOrderDetailDO> getK3ChangeOrderDetailDOList() {
		return k3ChangeOrderDetailDOList;
	}

	public void setK3ChangeOrderDetailDOList(List<K3ChangeOrderDetailDO> k3ChangeOrderDetailDOList) {
		this.k3ChangeOrderDetailDOList = k3ChangeOrderDetailDOList;
	}
}