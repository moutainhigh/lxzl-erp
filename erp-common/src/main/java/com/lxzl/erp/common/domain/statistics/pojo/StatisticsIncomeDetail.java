package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/17 14:55
 */
public class StatisticsIncomeDetail {

    private Integer id;  //唯一标识
    private String customerNo;  //客户编号
    private String customerName;  //客户姓名
    private Integer rentLengthType;  //业务类型，1-短租，2-长租
    private Integer subCompanyId;  //分公司ID
    private String subCompanyName;  //分公司名称
    private Integer orderSellerId;  //业务员ID
    private String orderSellerName;  //业务员姓名
    private BigDecimal depositPaidAmount;  //设备押金
    private BigDecimal rentDepositPaidAmount;  //租金押金
    private BigDecimal returnDepositPaidAmount;  //退设备押金
    private BigDecimal returnRentDepositPaidAmount;  //退租金押金
    private BigDecimal rentAmount;  //租金
    private BigDecimal prepayRentAmount;  //预付租金
    private BigDecimal otherPaidAmount;  //其他费用（运费等）
    private BigDecimal correctAmount;  //冲正金额
    private BigDecimal overdueAmount;  //逾期费用
    private BigDecimal incomeAmount;  //收入
    private Date payTime;  //支付时间
    private Integer orderItemReferId;  //订单项ID
    private Integer orderItemType;  //订单项类型，1为商品，2为配件


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
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

    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public BigDecimal getDepositPaidAmount() {
        return depositPaidAmount;
    }

    public void setDepositPaidAmount(BigDecimal depositPaidAmount) {
        this.depositPaidAmount = depositPaidAmount;
    }

    public BigDecimal getRentDepositPaidAmount() {
        return rentDepositPaidAmount;
    }

    public void setRentDepositPaidAmount(BigDecimal rentDepositPaidAmount) {
        this.rentDepositPaidAmount = rentDepositPaidAmount;
    }

    public BigDecimal getReturnDepositPaidAmount() {
        return returnDepositPaidAmount;
    }

    public void setReturnDepositPaidAmount(BigDecimal returnDepositPaidAmount) {
        this.returnDepositPaidAmount = returnDepositPaidAmount;
    }

    public BigDecimal getReturnRentDepositPaidAmount() {
        return returnRentDepositPaidAmount;
    }

    public void setReturnRentDepositPaidAmount(BigDecimal returnRentDepositPaidAmount) {
        this.returnRentDepositPaidAmount = returnRentDepositPaidAmount;
    }

    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }

    public BigDecimal getPrepayRentAmount() {
        return prepayRentAmount;
    }

    public void setPrepayRentAmount(BigDecimal prepayRentAmount) {
        this.prepayRentAmount = prepayRentAmount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public BigDecimal getOtherPaidAmount() {
        return otherPaidAmount;
    }

    public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
        this.otherPaidAmount = otherPaidAmount;
    }

    public BigDecimal getCorrectAmount() {
        return correctAmount;
    }

    public void setCorrectAmount(BigDecimal correctAmount) {
        this.correctAmount = correctAmount;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }
}
