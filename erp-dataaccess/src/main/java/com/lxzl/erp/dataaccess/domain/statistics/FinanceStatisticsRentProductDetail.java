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
public class FinanceStatisticsRentProductDetail {
    private String orderNo;               //订单编号
    private Integer orderSubCompanyId;    // 订单分公司
    private Integer deliverySubCompanyId; //发货分公司
    private String customerName;          // 客户名称
    private Integer customerOrigin;       //客户来源
    private String industry;              //客户行业
    private String k3CustomerCode;        //客户K3编码
    private Integer rentType;             // 租赁类型
    private Integer rentTimeLength;       //租赁时长
    private Integer rentLengthType;       // 租赁长短类型
    private String productName;           // 商品名称
    private String k3ProductNo;           // 商品编码
    private Integer productCount;         // 商品数量
    private BigDecimal productUnitAmount; // 商品单价
    private Date rentStartTime;           // 订单起租时间
    private Date expectReturnTime;        // 订单截止时间
    private Integer isNewProduct;         // 是否全新 (0-次新，1-全新)
    private Date deliveryTime;            // 订单发货时间

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

    public Integer getCustomerOrigin() {
        return customerOrigin;
    }

    public void setCustomerOrigin(Integer customerOrigin) {
        this.customerOrigin = customerOrigin;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getK3CustomerCode() {
        return k3CustomerCode;
    }

    public void setK3CustomerCode(String k3CustomerCode) {
        this.k3CustomerCode = k3CustomerCode;
    }

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getK3ProductNo() {
        return k3ProductNo;
    }

    public void setK3ProductNo(String k3ProductNo) {
        this.k3ProductNo = k3ProductNo;
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

    public Integer getIsNewProduct() {
        return isNewProduct;
    }

    public void setIsNewProduct(Integer isNewProduct) {
        this.isNewProduct = isNewProduct;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
