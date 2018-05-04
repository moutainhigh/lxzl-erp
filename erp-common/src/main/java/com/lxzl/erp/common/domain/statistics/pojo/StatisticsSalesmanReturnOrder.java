package com.lxzl.erp.common.domain.statistics.pojo;

import java.util.Date;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/3 14:12
 * @Description: 业务员提成统计关联退货单
 */
public class StatisticsSalesmanReturnOrder {
	private Integer eopId;
	private Integer euId; // 业务员id
	private Integer escId; // 分公司id
	private Integer rentLengthType; // 长短租类型
	private Date returnTime;
	private Integer productCount;
	private String productNo;
	private Date createTime; // 订单创建时间
	private Date localizationTime; // 属地化时间
	private Integer returnType; // 月租日租
	private Date rentStartTime; // 订单起租时间
	private Integer rentTimeLength; // 租赁月数
	private Date customerUpdateTime; // 退货单关联的最新的客户变更时间，如无则为null
	private Double productCountFactor; // 商品折扣系数

	public Integer getEopId() {
		return eopId;
	}

	public void setEopId(Integer eopId) {
		this.eopId = eopId;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public Integer getEuId() {
		return euId;
	}

	public void setEuId(Integer euId) {
		this.euId = euId;
	}

	public Integer getEscId() {
		return escId;
	}

	public void setEscId(Integer escId) {
		this.escId = escId;
	}

	public Integer getRentLengthType() {
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType) {
		this.rentLengthType = rentLengthType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLocalizationTime() {
		return localizationTime;
	}

	public void setLocalizationTime(Date localizationTime) {
		this.localizationTime = localizationTime;
	}

	public Integer getReturnType() {
		return returnType;
	}

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public Date getRentStartTime() {
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime) {
		this.rentStartTime = rentStartTime;
	}

	public Integer getRentTimeLength() {
		return rentTimeLength;
	}

	public void setRentTimeLength(Integer rentTimeLength) {
		this.rentTimeLength = rentTimeLength;
	}

	public Double getProductCountFactor() {
		return productCountFactor;
	}

	public void setProductCountFactor(Double productCountFactor) {
		this.productCountFactor = productCountFactor;
	}

	public Date getCustomerUpdateTime() {
		return customerUpdateTime;
	}

	public void setCustomerUpdateTime(Date customerUpdateTime) {
		this.customerUpdateTime = customerUpdateTime;
	}
}
