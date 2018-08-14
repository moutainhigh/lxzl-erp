package com.lxzl.erp.dataaccess.domain.statistics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/8/10
 * Time: 14:40
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsReturnProductDetail {

    private String returnOrderNo;          // 退货单号
    private String productNo;              // 商品编号
    private String productName;            // 商品名称
    private BigDecimal productUnitAmount;  // 商品单价
    private Integer productCount;          // 商品预计退货数量
    private Integer realProductCount;      // 商品实际退货数量
    private String orderNo;                 // 订单编号
    private Integer orderSubCompanyId;     // 订单分公司
    private Integer deliverySubCompanyId;  // 发货分公司
    private String customerNo;             // 客户编号
    private String customerName;           // 客户名称
    private Integer rentType;              // 租赁类型
    private Integer rentTimeLength;        // 租赁时长
    private Date returnTime;               // 退货时间
    private Date rentStartTime;            // 订单起租时间
    private Date expectReturnTime;         // 订单截止时间

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductUnitAmount() {
        return productUnitAmount;
    }

    public void setProductUnitAmount(BigDecimal productUnitAmount) {
        this.productUnitAmount = productUnitAmount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getRealProductCount() {
        return realProductCount;
    }

    public void setRealProductCount(Integer realProductCount) {
        this.realProductCount = realProductCount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderSubCompanyId() {
        return orderSubCompanyId;
    }

    public void setOrderSubCompanyId(Integer orderSubCompanyId) {
        this.orderSubCompanyId = orderSubCompanyId;
    }

    public Integer getDeliverySubCompanyId() {
        return deliverySubCompanyId;
    }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
        this.deliverySubCompanyId = deliverySubCompanyId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Date getExpectReturnTime() {
        return expectReturnTime;
    }

    public void setExpectReturnTime(Date expectReturnTime) {
        this.expectReturnTime = expectReturnTime;
    }
}
