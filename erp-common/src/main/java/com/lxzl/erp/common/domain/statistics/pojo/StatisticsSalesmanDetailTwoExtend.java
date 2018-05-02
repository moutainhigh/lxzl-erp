package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 19:04
 * @Description: 业务提成统计其他参数
 */
public class StatisticsSalesmanDetailTwoExtend {
	private Integer salesmanId;
	private String salesmanName;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer orderId;
	private String orderNo;
	private Integer eopId; // 商品项id
	private String categoryName;
	private Integer productCount; // 订单项商品数
	private BigDecimal productUnitAmount; // 商品标准单价
	private Integer rentType; // 租赁类型，月租还是天租
	private BigDecimal rentPrice; // 商品订单租赁单价
	private Date localizationTime; // 属地化时间
	private Date createTime; // 商品项关联订单确认收货时间
	private Date rentStartTime; // 租赁开始时间
	private Integer rentTimeLength; // 租赁时间长度
	private Double productCountFactor; // 商品项商品折算率
	private Date returnTime; // 退换单退还时间
	private Integer returnProductCount; // 退还单退还数量

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public Integer getSubCompanyId() {
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId) {
		this.subCompanyId = subCompanyId;
	}

	public String getSubCompanyName() {
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName) {
		this.subCompanyName = subCompanyName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getEopId() {
		return eopId;
	}

	public void setEopId(Integer eopId) {
		this.eopId = eopId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public BigDecimal getProductUnitAmount() {
		return productUnitAmount;
	}

	public void setProductUnitAmount(BigDecimal productUnitAmount) {
		this.productUnitAmount = productUnitAmount;
	}

	public Integer getRentType() {
		return rentType;
	}

	public void setRentType(Integer rentType) {
		this.rentType = rentType;
	}

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
	}

	public Date getLocalizationTime() {
		return localizationTime;
	}

	public void setLocalizationTime(Date localizationTime) {
		this.localizationTime = localizationTime;
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

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getReturnProductCount() {
		return returnProductCount;
	}

	public void setReturnProductCount(Integer returnProductCount) {
		this.returnProductCount = returnProductCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
