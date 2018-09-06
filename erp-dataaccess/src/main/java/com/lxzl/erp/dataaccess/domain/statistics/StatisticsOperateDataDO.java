package com.lxzl.erp.dataaccess.domain.statistics;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatisticsOperateDataDO  extends BaseDO {

	private Integer id;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer ownerId;
	private String ownerName;
	private Integer statisticalDimension;
	private Integer longRentIncreaseCount;
	private BigDecimal longRentIncreaseAmount;
	private Integer longRentIncreaseCustomerCount;
	private Integer longRentRentingCustomerCount;
	private Integer longRentReturnCustomerCount;
	private Integer longRentReturnCount;
	private Integer longRentRentingCount;
	private Integer shortRentIncreaseCount;
	private BigDecimal shortRentIncreaseAmount;
	private Integer shortRentIncreaseCustomerCount;
	private Integer shortRentRentingCustomerCount;
	private Integer shortRentReturnCustomerCount;
	private Integer shortRentReturnCount;
	private Integer shortRentRentingCount;
	private Integer ultrashortRentIncreaseCount;
	private BigDecimal ultrashortRentIncreaseAmount;
	private Integer ultrashortRentIncreaseCustomerCount;
	private Integer ultrashortRentRentingCustomerCount;
	private Integer ultrashortRentReturnCustomerCount;
	private Integer ultrashortRentReturnCount;
	private Integer ultrashortRentRentingCount;
	private Integer increaseCustomerCount;
	private Integer increaseRiskCustomerCount;
	private Date startStatisticsTime;
	private Date endStatisticsTime;
	private Integer statisticalStatus;
	private Integer dataStatus;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public String getSubCompanyName(){
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName){
		this.subCompanyName = subCompanyName;
	}

	public Integer getOwnerId(){
		return ownerId;
	}

	public void setOwnerId(Integer ownerId){
		this.ownerId = ownerId;
	}

	public String getOwnerName(){
		return ownerName;
	}

	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}

	public Integer getStatisticalDimension(){
		return statisticalDimension;
	}

	public void setStatisticalDimension(Integer statisticalDimension){
		this.statisticalDimension = statisticalDimension;
	}

	public Integer getLongRentIncreaseCount(){
		return longRentIncreaseCount;
	}

	public void setLongRentIncreaseCount(Integer longRentIncreaseCount){
		this.longRentIncreaseCount = longRentIncreaseCount;
	}

	public BigDecimal getLongRentIncreaseAmount(){
		return longRentIncreaseAmount;
	}

	public void setLongRentIncreaseAmount(BigDecimal longRentIncreaseAmount){
		this.longRentIncreaseAmount = longRentIncreaseAmount;
	}

	public Integer getLongRentIncreaseCustomerCount(){
		return longRentIncreaseCustomerCount;
	}

	public void setLongRentIncreaseCustomerCount(Integer longRentIncreaseCustomerCount){
		this.longRentIncreaseCustomerCount = longRentIncreaseCustomerCount;
	}

	public Integer getLongRentRentingCustomerCount(){
		return longRentRentingCustomerCount;
	}

	public void setLongRentRentingCustomerCount(Integer longRentRentingCustomerCount){
		this.longRentRentingCustomerCount = longRentRentingCustomerCount;
	}

	public Integer getLongRentReturnCustomerCount(){
		return longRentReturnCustomerCount;
	}

	public void setLongRentReturnCustomerCount(Integer longRentReturnCustomerCount){
		this.longRentReturnCustomerCount = longRentReturnCustomerCount;
	}

	public Integer getLongRentReturnCount(){
		return longRentReturnCount;
	}

	public void setLongRentReturnCount(Integer longRentReturnCount){
		this.longRentReturnCount = longRentReturnCount;
	}

	public Integer getLongRentRentingCount(){
		return longRentRentingCount;
	}

	public void setLongRentRentingCount(Integer longRentRentingCount){
		this.longRentRentingCount = longRentRentingCount;
	}

	public Integer getShortRentIncreaseCount(){
		return shortRentIncreaseCount;
	}

	public void setShortRentIncreaseCount(Integer shortRentIncreaseCount){
		this.shortRentIncreaseCount = shortRentIncreaseCount;
	}

	public BigDecimal getShortRentIncreaseAmount(){
		return shortRentIncreaseAmount;
	}

	public void setShortRentIncreaseAmount(BigDecimal shortRentIncreaseAmount){
		this.shortRentIncreaseAmount = shortRentIncreaseAmount;
	}

	public Integer getShortRentIncreaseCustomerCount(){
		return shortRentIncreaseCustomerCount;
	}

	public void setShortRentIncreaseCustomerCount(Integer shortRentIncreaseCustomerCount){
		this.shortRentIncreaseCustomerCount = shortRentIncreaseCustomerCount;
	}

	public Integer getShortRentRentingCustomerCount(){
		return shortRentRentingCustomerCount;
	}

	public void setShortRentRentingCustomerCount(Integer shortRentRentingCustomerCount){
		this.shortRentRentingCustomerCount = shortRentRentingCustomerCount;
	}

	public Integer getShortRentReturnCustomerCount(){
		return shortRentReturnCustomerCount;
	}

	public void setShortRentReturnCustomerCount(Integer shortRentReturnCustomerCount){
		this.shortRentReturnCustomerCount = shortRentReturnCustomerCount;
	}

	public Integer getShortRentReturnCount(){
		return shortRentReturnCount;
	}

	public void setShortRentReturnCount(Integer shortRentReturnCount){
		this.shortRentReturnCount = shortRentReturnCount;
	}

	public Integer getShortRentRentingCount(){
		return shortRentRentingCount;
	}

	public void setShortRentRentingCount(Integer shortRentRentingCount){
		this.shortRentRentingCount = shortRentRentingCount;
	}

	public Integer getUltrashortRentIncreaseCount(){
		return ultrashortRentIncreaseCount;
	}

	public void setUltrashortRentIncreaseCount(Integer ultrashortRentIncreaseCount){
		this.ultrashortRentIncreaseCount = ultrashortRentIncreaseCount;
	}

	public BigDecimal getUltrashortRentIncreaseAmount(){
		return ultrashortRentIncreaseAmount;
	}

	public void setUltrashortRentIncreaseAmount(BigDecimal ultrashortRentIncreaseAmount){
		this.ultrashortRentIncreaseAmount = ultrashortRentIncreaseAmount;
	}

	public Integer getUltrashortRentIncreaseCustomerCount(){
		return ultrashortRentIncreaseCustomerCount;
	}

	public void setUltrashortRentIncreaseCustomerCount(Integer ultrashortRentIncreaseCustomerCount){
		this.ultrashortRentIncreaseCustomerCount = ultrashortRentIncreaseCustomerCount;
	}

	public Integer getUltrashortRentRentingCustomerCount(){
		return ultrashortRentRentingCustomerCount;
	}

	public void setUltrashortRentRentingCustomerCount(Integer ultrashortRentRentingCustomerCount){
		this.ultrashortRentRentingCustomerCount = ultrashortRentRentingCustomerCount;
	}

	public Integer getUltrashortRentReturnCustomerCount(){
		return ultrashortRentReturnCustomerCount;
	}

	public void setUltrashortRentReturnCustomerCount(Integer ultrashortRentReturnCustomerCount){
		this.ultrashortRentReturnCustomerCount = ultrashortRentReturnCustomerCount;
	}

	public Integer getUltrashortRentReturnCount(){
		return ultrashortRentReturnCount;
	}

	public void setUltrashortRentReturnCount(Integer ultrashortRentReturnCount){
		this.ultrashortRentReturnCount = ultrashortRentReturnCount;
	}

	public Integer getUltrashortRentRentingCount(){
		return ultrashortRentRentingCount;
	}

	public void setUltrashortRentRentingCount(Integer ultrashortRentRentingCount){
		this.ultrashortRentRentingCount = ultrashortRentRentingCount;
	}

	public Integer getIncreaseCustomerCount(){
		return increaseCustomerCount;
	}

	public void setIncreaseCustomerCount(Integer increaseCustomerCount){
		this.increaseCustomerCount = increaseCustomerCount;
	}

	public Integer getIncreaseRiskCustomerCount(){
		return increaseRiskCustomerCount;
	}

	public void setIncreaseRiskCustomerCount(Integer increaseRiskCustomerCount){
		this.increaseRiskCustomerCount = increaseRiskCustomerCount;
	}

	public Date getStartStatisticsTime(){
		return startStatisticsTime;
	}

	public void setStartStatisticsTime(Date startStatisticsTime){
		this.startStatisticsTime = startStatisticsTime;
	}

	public Date getEndStatisticsTime(){
		return endStatisticsTime;
	}

	public void setEndStatisticsTime(Date endStatisticsTime){
		this.endStatisticsTime = endStatisticsTime;
	}

	public Integer getStatisticalStatus(){
		return statisticalStatus;
	}

	public void setStatisticalStatus(Integer statisticalStatus){
		this.statisticalStatus = statisticalStatus;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}