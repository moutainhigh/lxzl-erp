package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsOperateData extends BasePO {

	private Integer statisticsOperateDataId;   //唯一标识ID
	private Integer subCompanyId;   //分公司ID
	private String subCompanyName;   //分公司名称
	private Integer ownerId;   //业务员ID
	private String ownerName;   //业务员姓名
	private Integer statisticalDimension;   //统计维度：1-分公司维度  2-业务员维度
	private Integer longRentIncreaseCount;   //长租新增台数
	private BigDecimal longRentIncreaseAmount;   //长租新增租金
	private Integer longRentIncreaseCustomerCount;   //长租新增客户数
	private Integer longRentRentingCustomerCount;   //长租在租客户数
	private Integer longRentReturnCustomerCount;   //长租退货客户数
	private Integer longRentReturnCount;   //长租退货台数
	private Integer longRentRentingCount;   //长租在租台数
	private Integer shortRentIncreaseCount;   //短租新增台数
	private BigDecimal shortRentIncreaseAmount;   //短租新增租金
	private Integer shortRentIncreaseCustomerCount;   //短租新增客户数
	private Integer shortRentRentingCustomerCount;   //短租在租客户数
	private Integer shortRentReturnCustomerCount;   //短租退货客户数
	private Integer shortRentReturnCount;   //短租退货台数
	private Integer shortRentRentingCount;   //短租在租台数
	private Integer ultrashortRentIncreaseCount;   //短短租新增台数
	private BigDecimal ultrashortRentIncreaseAmount;   //短短组新增租金
	private Integer ultrashortRentIncreaseCustomerCount;   //短短租新增客户数
	private Integer ultrashortRentRentingCustomerCount;   //短短租在租客户数
	private Integer ultrashortRentReturnCustomerCount;   //短短租退货客户数
	private Integer ultrashortRentReturnCount;   //短短租退货台数
	private Integer ultrashortRentRentingCount;   //短短租在租台数

	private Integer increaseCustomerCount;   //新增录入客户
	private Integer increaseRiskCustomerCount;   //新增授信客户数
	private Date startStatisticsTime;   //统计开始时间
	private Date endStatisticsTime;   //统计结束时间
	private Integer statisticalStatus;   //统计时间类型：1-按天统计  2-按周统计  3-按月统计
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getStatisticsOperateDataId(){
		return statisticsOperateDataId;
	}

	public void setStatisticsOperateDataId(Integer statisticsOperateDataId){
		this.statisticsOperateDataId = statisticsOperateDataId;
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

}