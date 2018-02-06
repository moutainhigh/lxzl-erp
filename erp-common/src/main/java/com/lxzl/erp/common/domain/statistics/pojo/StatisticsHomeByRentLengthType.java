package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lk
 * @Description: 首页长短租统计
 * @date 2018/1/31 16:16
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsHomeByRentLengthType {

    private Integer newCustomerCount = 0 ;   //长租新增客户数
    private Integer oldCustomerCount = 0 ;   //长租老客户数
    private Integer orderCountByNewCustomer = 0 ;   //新客户下单订单数
    private Integer orderCountByOldCustomer = 0 ;   //老客户下单订单数
    private Integer totalOrderCount = 0 ;   //下单订单总数（新客户下单订单数+老客户下单订单数）
    private Integer productCountByNewCustomer = 0 ;   //老客户下单台数
    private Integer productCountByOldCustomer = 0 ;   //新客户下单台数
    private Integer totalProductCount = 0 ;   //下单总台数（新客户下单台数+老客户下单台数）
    private Integer returnProductCount = 0 ;   //退租台数
    private Integer increaseProductCount = 0 ;   //净增台数(新增减退租)
    private BigDecimal rentDeposit = BigDecimal.ZERO;   //租金押金
    private BigDecimal deposit = BigDecimal.ZERO;   //设备押金
    private BigDecimal returnDeposit = BigDecimal.ZERO;   //退设备押金
    private BigDecimal returnRentDeposit = BigDecimal.ZERO;   //退租金押金
    private BigDecimal rent = BigDecimal.ZERO;   //租金
    private BigDecimal prepayRent = BigDecimal.ZERO;   //预付租金
    private BigDecimal otherAmount = BigDecimal.ZERO;   //其他费用
    private BigDecimal rentIncome = BigDecimal.ZERO;   //租金收入(长租时为：租金+预付租金+租金押金+设备押金-退押金)(短租时为：租金+预付租金)

    private Date timeNode;   //时间节点

    public Integer getNewCustomerCount() {
        return newCustomerCount;
    }

    public void setNewCustomerCount(Integer newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }

    public Integer getOldCustomerCount() {
        return oldCustomerCount;
    }

    public void setOldCustomerCount(Integer oldCustomerCount) {
        this.oldCustomerCount = oldCustomerCount;
    }

    public Integer getOrderCountByNewCustomer() {
        return orderCountByNewCustomer;
    }

    public void setOrderCountByNewCustomer(Integer orderCountByNewCustomer) {
        this.orderCountByNewCustomer = orderCountByNewCustomer;
    }

    public Integer getOrderCountByOldCustomer() {
        return orderCountByOldCustomer;
    }

    public void setOrderCountByOldCustomer(Integer orderCountByOldCustomer) {
        this.orderCountByOldCustomer = orderCountByOldCustomer;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Integer getProductCountByNewCustomer() {
        return productCountByNewCustomer;
    }

    public void setProductCountByNewCustomer(Integer productCountByNewCustomer) {
        this.productCountByNewCustomer = productCountByNewCustomer;
    }

    public Integer getProductCountByOldCustomer() {
        return productCountByOldCustomer;
    }

    public void setProductCountByOldCustomer(Integer productCountByOldCustomer) {
        this.productCountByOldCustomer = productCountByOldCustomer;
    }

    public Integer getTotalProductCount() {
        return totalProductCount;
    }

    public void setTotalProductCount(Integer totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public Integer getReturnProductCount() {
        return returnProductCount;
    }

    public void setReturnProductCount(Integer returnProductCount) {
        this.returnProductCount = returnProductCount;
    }

    public Integer getIncreaseProductCount() {
        return increaseProductCount;
    }

    public void setIncreaseProductCount(Integer increaseProductCount) {
        this.increaseProductCount = increaseProductCount;
    }

    public BigDecimal getRentDeposit() {
        return rentDeposit;
    }

    public void setRentDeposit(BigDecimal rentDeposit) {
        this.rentDeposit = rentDeposit;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getReturnDeposit() {
        return returnDeposit;
    }

    public void setReturnDeposit(BigDecimal returnDeposit) {
        this.returnDeposit = returnDeposit;
    }

    public BigDecimal getReturnRentDeposit() {
        return returnRentDeposit;
    }

    public void setReturnRentDeposit(BigDecimal returnRentDeposit) {
        this.returnRentDeposit = returnRentDeposit;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public BigDecimal getPrepayRent() {
        return prepayRent;
    }

    public void setPrepayRent(BigDecimal prepayRent) {
        this.prepayRent = prepayRent;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getRentIncome() {
        return rentIncome;
    }

    public void setRentIncome(BigDecimal rentIncome) {
        this.rentIncome = rentIncome;
    }

    public Date getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(Date timeNode) {
        this.timeNode = timeNode;
    }
}

