package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsDataWeeklyExcel extends BasePO {

//	private Integer financeStatisticsDataWeeklyId;   //唯一标识ID
	private Integer orderOrigin;   //订单来源：1-KA  2-电销  3-大客户渠道
	private Integer rentLengthType;   //租赁类型：1-短租 2-长租 3-短短租
	//private Integer subCompanyId;   //分公司ID
	private Integer dealsCountType;  //成交数量类型 ：1-客户成交数量 2-新客户成交数量 3-租赁商品成交数量 4-退货商品成交数量 5-净增长商品成交数量
	private Integer shenZhenDealsCount = 0;  //深圳分公司成交数量
	private Integer beiJingDealsCount = 0;  //北京分公司成交数量
	private Integer shangHaiDealsCount = 0;  //上海分公司成交数量
	private Integer guangZhouDealsCount = 0;  //广州分公司成交数量
	private Integer wuHanDealsCount = 0;  //武汉分公司成交数量
	private Integer nanJingDealsCount = 0;  //南京分公司成交数量
	private Integer chengDuDealsCount = 0;  //成都分公司成交数量
	private Integer xiaMenDealsCount = 0;  //厦门分公司成交数量
	private Integer sumDealsCount = 0;     // 合计成交数量
	/*private Integer newCustomerDealsCount;   //新客户成交数量
	private Integer rentProductDealsCount;   //租赁商品成交数量
	private Integer returnProductDealsCount;   //退货商品成交数量
	private Integer increaseProductDealsCount;   //净增长商品成交数量*/
	private Integer year;   //年份
	private Integer month;   //月份
	private Integer weekOfMonth;   //当月第几周
	/*private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人*/


	/*public Integer getFinanceStatisticsDataWeeklyId(){
		return financeStatisticsDataWeeklyId;
	}

	public void setFinanceStatisticsDataWeeklyId(Integer financeStatisticsDataWeeklyId){
		this.financeStatisticsDataWeeklyId = financeStatisticsDataWeeklyId;
	}*/

	public Integer getOrderOrigin(){
		return orderOrigin;
	}

	public void setOrderOrigin(Integer orderOrigin){
		this.orderOrigin = orderOrigin;
	}

	public Integer getRentLengthType(){
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType){
		this.rentLengthType = rentLengthType;
	}

//	public Integer getSubCompanyId(){
//		return subCompanyId;
//	}
//
//	public void setSubCompanyId(Integer subCompanyId){
//		this.subCompanyId = subCompanyId;
//	}

	public Integer getDealsCountType() {
		return dealsCountType;
	}

	public void setDealsCountType(Integer dealsCountType) {
		this.dealsCountType = dealsCountType;
	}

	public Integer getShenZhenDealsCount() {
		return shenZhenDealsCount;
	}

	public void setShenZhenDealsCount(Integer shenZhenDealsCount) {
		this.shenZhenDealsCount = shenZhenDealsCount;
	}

	public Integer getBeiJingDealsCount() {
		return beiJingDealsCount;
	}

	public void setBeiJingDealsCount(Integer beiJingDealsCount) {
		this.beiJingDealsCount = beiJingDealsCount;
	}

	public Integer getShangHaiDealsCount() {
		return shangHaiDealsCount;
	}

	public void setShangHaiDealsCount(Integer shangHaiDealsCount) {
		this.shangHaiDealsCount = shangHaiDealsCount;
	}

	public Integer getGuangZhouDealsCount() {
		return guangZhouDealsCount;
	}

	public void setGuangZhouDealsCount(Integer guangZhouDealsCount) {
		this.guangZhouDealsCount = guangZhouDealsCount;
	}

	public Integer getWuHanDealsCount() {
		return wuHanDealsCount;
	}

	public void setWuHanDealsCount(Integer wuHanDealsCount) {
		this.wuHanDealsCount = wuHanDealsCount;
	}

	public Integer getNanJingDealsCount() {
		return nanJingDealsCount;
	}

	public void setNanJingDealsCount(Integer nanJingDealsCount) {
		this.nanJingDealsCount = nanJingDealsCount;
	}

	public Integer getChengDuDealsCount() {
		return chengDuDealsCount;
	}

	public void setChengDuDealsCount(Integer chengDuDealsCount) {
		this.chengDuDealsCount = chengDuDealsCount;
	}

	public Integer getXiaMenDealsCount() {
		return xiaMenDealsCount;
	}

	public void setXiaMenDealsCount(Integer xiaMenDealsCount) {
		this.xiaMenDealsCount = xiaMenDealsCount;
	}

	public Integer getSumDealsCount(){
		sumDealsCount =  shenZhenDealsCount + beiJingDealsCount + guangZhouDealsCount + shangHaiDealsCount + wuHanDealsCount + nanJingDealsCount + xiaMenDealsCount + chengDuDealsCount;
		return sumDealsCount;
	}

	public Integer getYear(){
		return year;
	}

	public void setYear(Integer year){
		this.year = year;
	}

	public Integer getMonth(){
		return month;
	}

	public void setMonth(Integer month){
		this.month = month;
	}

	public Integer getWeekOfMonth(){
		return weekOfMonth;
	}

	public void setWeekOfMonth(Integer weekOfMonth){
		this.weekOfMonth = weekOfMonth;
	}

	/*public Integer getDataStatus(){
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
	}*/

	public void fillCountBySubCompanyId(Integer subCompanyId, int dealsCount) {
		if (subCompanyId != null) {
			switch (subCompanyId) {
				case 2:
					setShenZhenDealsCount(dealsCount);
					break;
				case 3:
					setShangHaiDealsCount(dealsCount);
					break;
				case 4:
					setBeiJingDealsCount(dealsCount);
					break;
				case 5:
					setGuangZhouDealsCount(dealsCount);
					break;
				case 6:
					setNanJingDealsCount(dealsCount);
					break;
				case 7:
					setXiaMenDealsCount(dealsCount);
					break;
				case 8:
					setWuHanDealsCount(dealsCount);
					break;
				case 9:
					setChengDuDealsCount(dealsCount);
					break;
				default:
					break;
			}
		}
	}

	public void sumCountBySubCompanyId(Integer subCompanyId, int dealsCount) {
		if (subCompanyId != null) {
			switch (subCompanyId) {
				case 2:
					setShenZhenDealsCount(getShenZhenDealsCount() + dealsCount);
					break;
				case 3:
					setShangHaiDealsCount(getShangHaiDealsCount() + dealsCount);
					break;
				case 4:
					setBeiJingDealsCount(getBeiJingDealsCount() + dealsCount);
					break;
				case 5:
					setGuangZhouDealsCount(getGuangZhouDealsCount() + dealsCount);
					break;
				case 6:
					setNanJingDealsCount(getNanJingDealsCount() + dealsCount);
					break;
				case 7:
					setXiaMenDealsCount(getXiaMenDealsCount() + dealsCount);
					break;
				case 8:
					setWuHanDealsCount(getWuHanDealsCount() + dealsCount);
					break;
				case 9:
					setChengDuDealsCount(getChengDuDealsCount() + dealsCount);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public String toString() {
		return "FinanceStatisticsDataWeeklyExcel{" +
				", orderOrigin=" + orderOrigin +
				", rentLengthType=" + rentLengthType +
				", dealsCountType=" + dealsCountType +
				", shenZhenDealsCount=" + shenZhenDealsCount +
				", beiJingZhenDealsCount=" + beiJingDealsCount +
				", shangHaiDealsCount=" + shangHaiDealsCount +
				", guangZhouDealsCount=" + guangZhouDealsCount +
				", wuHanDealsCount=" + wuHanDealsCount +
				", nanJingDealsCount=" + nanJingDealsCount +
				", chengDuDealsCount=" + chengDuDealsCount +
				", xiaMenDealsCount=" + xiaMenDealsCount +
				", sumDealsCount=" + getSumDealsCount() +
				", year=" + year +
				", month=" + month +
				", weekOfMonth=" + weekOfMonth +
				'}';
	}
}