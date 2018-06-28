package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckStatementOrderDetailBase extends BasePO {

    private String orderNo; //订单编号
    private Integer orderType;    // 单子类型，详见ORDER_TYPE
    private String itemName;  //商品名
    private String itemSkuName;  //商品名
    private Integer itemCount;  //数量
    private BigDecimal unitAmount;  //"单价（元/台/月）"
    private BigDecimal statementRentAmount;             // 租金小计
    private BigDecimal statementRentDepositAmount;      // 租金押金
    private BigDecimal statementDepositAmount;      // 设备押金
    private BigDecimal statementOverdueAmount;      // 逾期金额
    private BigDecimal statementOtherAmount;      // 其它费用
    private BigDecimal statementCorrectAmount;      // 冲正金额
    private BigDecimal statementAmount;      // 应付金额
    private Date statementExpectPayTime;      // 应付日期
    private Integer statementDetailStatus;      // 状态
    private BigDecimal statementDepositPaidAmount; //支付押金

    private Integer businessType;      //业务类型
    private Date rentStartTime;     //租赁开始日期
    private Date expectReturnTime;  //租赁结束日期
    private Integer month; //月
    private Integer day; //日
    private String allRentTimeLength;  //租赁总期限
    private String  allPeriodStartAndEnd;  //本期起止（总的期数起止）

    private Date statementStartTime;     //结算开始日期
    private Date statementEndTime;  //结算结束日期
    private Integer statementMonth; //月
    private Integer statementDay; //日
    private String rentTimeLength;  //期限
    private String  currentPeriodStartAndEnd;  //本期起止（当前期数起止）
    private Integer  isNew;  //是否全新
    private String  k3ReturnOrderDONo;  //退还编号
    private String  returnReasonType;  //退还原因
    private String  statementCorrectNo;  //冲正单号
    private String  statementCorrectReason;  //冲正原因
    private Integer payMode;            // 支付方式
    private Integer depositCycle;            // 押金周期
    private Integer paymentCycle;            // 付款周期
    private String rentProgramme;           // 租赁方案
    private Date statementExpectPayEndTime;            // 本期付款月数
    private BigDecimal statementDetailEndAmount;            // 本期应付数
    private BigDecimal statementDetailRentEndAmount;            // 本期应付租金

    public String getK3ReturnOrderDONo() {
        return k3ReturnOrderDONo;
    }

    public void setK3ReturnOrderDONo(String k3ReturnOrderDONo) {
        this.k3ReturnOrderDONo = k3ReturnOrderDONo;
    }

    public String getReturnReasonType() {
        return returnReasonType;
    }

    public void setReturnReasonType(String returnReasonType) {
        this.returnReasonType = returnReasonType;
    }

    public String getStatementCorrectNo() {
        return statementCorrectNo;
    }

    public void setStatementCorrectNo(String statementCorrectNo) {
        this.statementCorrectNo = statementCorrectNo;
    }

    public String getStatementCorrectReason() {
        return statementCorrectReason;
    }

    public void setStatementCorrectReason(String statementCorrectReason) {
        this.statementCorrectReason = statementCorrectReason;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public BigDecimal getStatementRentAmount() {
        return statementRentAmount;
    }

    public void setStatementRentAmount(BigDecimal statementRentAmount) {
        this.statementRentAmount = statementRentAmount;
    }

    public BigDecimal getStatementRentDepositAmount() {
        return statementRentDepositAmount;
    }

    public void setStatementRentDepositAmount(BigDecimal statementRentDepositAmount) {
        this.statementRentDepositAmount = statementRentDepositAmount;
    }

    public BigDecimal getStatementDepositAmount() {
        return statementDepositAmount;
    }

    public void setStatementDepositAmount(BigDecimal statementDepositAmount) {
        this.statementDepositAmount = statementDepositAmount;
    }

    public BigDecimal getStatementOverdueAmount() {
        return statementOverdueAmount;
    }

    public void setStatementOverdueAmount(BigDecimal statementOverdueAmount) {
        this.statementOverdueAmount = statementOverdueAmount;
    }

    public BigDecimal getStatementOtherAmount() {
        return statementOtherAmount;
    }

    public void setStatementOtherAmount(BigDecimal statementOtherAmount) {
        this.statementOtherAmount = statementOtherAmount;
    }

    public BigDecimal getStatementCorrectAmount() {
        return statementCorrectAmount;
    }

    public void setStatementCorrectAmount(BigDecimal statementCorrectAmount) {
        this.statementCorrectAmount = statementCorrectAmount;
    }

    public BigDecimal getStatementAmount() {
        return statementAmount;
    }

    public void setStatementAmount(BigDecimal statementAmount) {
        this.statementAmount = statementAmount;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getAllRentTimeLength() {
        return allRentTimeLength;
    }

    public void setAllRentTimeLength(String allRentTimeLength) {
        this.allRentTimeLength = allRentTimeLength;
    }

    public String getAllPeriodStartAndEnd() {
        return allPeriodStartAndEnd;
    }

    public void setAllPeriodStartAndEnd(String allPeriodStartAndEnd) {
        this.allPeriodStartAndEnd = allPeriodStartAndEnd;
    }

    public String getCurrentPeriodStartAndEnd() {
        return currentPeriodStartAndEnd;
    }

    public void setCurrentPeriodStartAndEnd(String currentPeriodStartAndEnd) {
        this.currentPeriodStartAndEnd = currentPeriodStartAndEnd;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(String rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Date getStatementStartTime() {
        return statementStartTime;
    }

    public void setStatementStartTime(Date statementStartTime) {
        this.statementStartTime = statementStartTime;
    }

    public Date getStatementEndTime() {
        return statementEndTime;
    }

    public void setStatementEndTime(Date statementEndTime) {
        this.statementEndTime = statementEndTime;
    }

    public Integer getStatementMonth() {
        return statementMonth;
    }

    public void setStatementMonth(Integer statementMonth) {
        this.statementMonth = statementMonth;
    }

    public Integer getStatementDay() {
        return statementDay;
    }

    public void setStatementDay(Integer statementDay) {
        this.statementDay = statementDay;
    }

    public BigDecimal getStatementDepositPaidAmount() {
        return statementDepositPaidAmount;
    }

    public void setStatementDepositPaidAmount(BigDecimal statementDepositPaidAmount) {
        this.statementDepositPaidAmount = statementDepositPaidAmount;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public Integer getDepositCycle() {
        return depositCycle;
    }

    public void setDepositCycle(Integer depositCycle) {
        this.depositCycle = depositCycle;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(Integer paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public String getItemSkuName() {
        return itemSkuName;
    }

    public void setItemSkuName(String itemSkuName) {
        this.itemSkuName = itemSkuName;
    }

    public String getRentProgramme() {
        return rentProgramme;
    }

    public void setRentProgramme(String rentProgramme) {
        this.rentProgramme = rentProgramme;
    }

    public Date getStatementExpectPayEndTime() {
        return statementExpectPayEndTime;
    }

    public void setStatementExpectPayEndTime(Date statementExpectPayEndTime) {
        this.statementExpectPayEndTime = statementExpectPayEndTime;
    }

    public BigDecimal getStatementDetailEndAmount() {
        return statementDetailEndAmount;
    }

    public void setStatementDetailEndAmount(BigDecimal statementDetailEndAmount) {
        this.statementDetailEndAmount = statementDetailEndAmount;
    }

    public BigDecimal getStatementDetailRentEndAmount() {
        return statementDetailRentEndAmount;
    }

    public void setStatementDetailRentEndAmount(BigDecimal statementDetailRentEndAmount) {
        this.statementDetailRentEndAmount = statementDetailRentEndAmount;
    }
}