package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderDetailDO extends BaseDO {

    private Integer id;
    private Integer statementOrderId;
    private Integer customerId;
    private Integer orderType;
    private Integer orderId;
    private Integer orderItemType;
    private Integer orderItemReferId;
    private Integer returnReferId;     // 退款的时候，关联的结算单detail ID
    private Integer statementDetailType;
    private Integer statementDetailPhase;
    private Date statementExpectPayTime;
    private BigDecimal statementDetailAmount;
    private BigDecimal statementDetailOtherAmount;
    private BigDecimal statementDetailOtherPaidAmount;
    private BigDecimal statementDetailRentDepositAmount;
    private BigDecimal statementDetailRentDepositPaidAmount;
    private BigDecimal statementDetailRentDepositReturnAmount;
    private Date statementDetailRentDepositReturnTime;
    private BigDecimal statementDetailDepositAmount;
    private BigDecimal statementDetailDepositPaidAmount;
    private BigDecimal statementDetailDepositReturnAmount;
    private Date statementDetailDepositReturnTime;
    private BigDecimal statementDetailRentAmount;
    private BigDecimal statementDetailRentPaidAmount;
    private Date statementDetailPaidTime;
    private BigDecimal statementDetailOverdueAmount;
    private BigDecimal statementDetailOverduePaidAmount;
    private BigDecimal statementDetailPenaltyAmount;
    private BigDecimal statementDetailPenaltyPaidAmount;
    private Integer statementDetailOverdueDays;
    private Integer statementDetailOverduePhaseCount;
    private Integer statementDetailStatus;
    private BigDecimal statementDetailCorrectAmount;  //结算单冲正金额
    private Date statementStartTime;
    private Date statementEndTime;
    private Integer dataStatus;
    private String remark;
    private BigDecimal statementCouponAmount;   //结算单优惠券优惠总和

    @Transient
    private Integer rentType;
    @Transient
    private Integer goodsCount;
    @Transient
    private BigDecimal goodsUnitAmount;

    @Transient
    private Integer salesmanId;
    @Transient
    private Integer subCompanyId;
    @Transient
    private Integer rentLengthType;

    private String itemName;
    private Integer itemIsNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getStatementDetailAmount() {
        return statementDetailAmount;
    }

    public void setStatementDetailAmount(BigDecimal statementDetailAmount) {
        this.statementDetailAmount = statementDetailAmount;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
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

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getStatementDetailOverdueAmount() {
        return statementDetailOverdueAmount;
    }

    public void setStatementDetailOverdueAmount(BigDecimal statementDetailOverdueAmount) {
        this.statementDetailOverdueAmount = statementDetailOverdueAmount;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public BigDecimal getStatementDetailRentDepositAmount() {
        return statementDetailRentDepositAmount;
    }

    public void setStatementDetailRentDepositAmount(BigDecimal statementDetailRentDepositAmount) {
        this.statementDetailRentDepositAmount = statementDetailRentDepositAmount;
    }

    public BigDecimal getStatementDetailRentDepositPaidAmount() {
        return statementDetailRentDepositPaidAmount;
    }

    public void setStatementDetailRentDepositPaidAmount(BigDecimal statementDetailRentDepositPaidAmount) {
        this.statementDetailRentDepositPaidAmount = statementDetailRentDepositPaidAmount;
    }

    public BigDecimal getStatementDetailRentDepositReturnAmount() {
        return statementDetailRentDepositReturnAmount;
    }

    public void setStatementDetailRentDepositReturnAmount(BigDecimal statementDetailRentDepositReturnAmount) {
        this.statementDetailRentDepositReturnAmount = statementDetailRentDepositReturnAmount;
    }

    public BigDecimal getStatementDetailRentAmount() {
        return statementDetailRentAmount;
    }

    public void setStatementDetailRentAmount(BigDecimal statementDetailRentAmount) {
        this.statementDetailRentAmount = statementDetailRentAmount;
    }

    public BigDecimal getStatementDetailRentPaidAmount() {
        return statementDetailRentPaidAmount;
    }

    public void setStatementDetailRentPaidAmount(BigDecimal statementDetailRentPaidAmount) {
        this.statementDetailRentPaidAmount = statementDetailRentPaidAmount;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getStatementDetailPaidTime() {
        return statementDetailPaidTime;
    }

    public void setStatementDetailPaidTime(Date statementDetailPaidTime) {
        this.statementDetailPaidTime = statementDetailPaidTime;
    }

    public BigDecimal getStatementDetailDepositAmount() {
        return statementDetailDepositAmount;
    }

    public void setStatementDetailDepositAmount(BigDecimal statementDetailDepositAmount) {
        this.statementDetailDepositAmount = statementDetailDepositAmount;
    }

    public BigDecimal getStatementDetailDepositPaidAmount() {
        return statementDetailDepositPaidAmount;
    }

    public void setStatementDetailDepositPaidAmount(BigDecimal statementDetailDepositPaidAmount) {
        this.statementDetailDepositPaidAmount = statementDetailDepositPaidAmount;
    }

    public BigDecimal getStatementDetailDepositReturnAmount() {
        return statementDetailDepositReturnAmount;
    }

    public void setStatementDetailDepositReturnAmount(BigDecimal statementDetailDepositReturnAmount) {
        this.statementDetailDepositReturnAmount = statementDetailDepositReturnAmount;
    }

    public Integer getStatementDetailPhase() {
        return statementDetailPhase;
    }

    public void setStatementDetailPhase(Integer statementDetailPhase) {
        this.statementDetailPhase = statementDetailPhase;
    }

    public Date getStatementDetailDepositReturnTime() {
        return statementDetailDepositReturnTime;
    }

    public void setStatementDetailDepositReturnTime(Date statementDetailDepositReturnTime) {
        this.statementDetailDepositReturnTime = statementDetailDepositReturnTime;
    }

    public Date getStatementDetailRentDepositReturnTime() {
        return statementDetailRentDepositReturnTime;
    }

    public void setStatementDetailRentDepositReturnTime(Date statementDetailRentDepositReturnTime) {
        this.statementDetailRentDepositReturnTime = statementDetailRentDepositReturnTime;
    }

    public BigDecimal getStatementDetailOtherAmount() {
        return statementDetailOtherAmount;
    }

    public void setStatementDetailOtherAmount(BigDecimal statementDetailOtherAmount) {
        this.statementDetailOtherAmount = statementDetailOtherAmount;
    }

    public Integer getStatementDetailType() {
        return statementDetailType;
    }

    public void setStatementDetailType(Integer statementDetailType) {
        this.statementDetailType = statementDetailType;
    }

    public BigDecimal getStatementDetailOtherPaidAmount() {
        return statementDetailOtherPaidAmount;
    }

    public void setStatementDetailOtherPaidAmount(BigDecimal statementDetailOtherPaidAmount) {
        this.statementDetailOtherPaidAmount = statementDetailOtherPaidAmount;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getGoodsUnitAmount() {
        return goodsUnitAmount;
    }

    public void setGoodsUnitAmount(BigDecimal goodsUnitAmount) {
        this.goodsUnitAmount = goodsUnitAmount;
    }

    public Integer getReturnReferId() {
        return returnReferId;
    }

    public void setReturnReferId(Integer returnReferId) {
        this.returnReferId = returnReferId;
    }

    public Integer getStatementDetailOverdueDays() {
        return statementDetailOverdueDays;
    }

    public void setStatementDetailOverdueDays(Integer statementDetailOverdueDays) {
        this.statementDetailOverdueDays = statementDetailOverdueDays;
    }

    public Integer getStatementDetailOverduePhaseCount() {
        return statementDetailOverduePhaseCount;
    }

    public void setStatementDetailOverduePhaseCount(Integer statementDetailOverduePhaseCount) {
        this.statementDetailOverduePhaseCount = statementDetailOverduePhaseCount;
    }

    public BigDecimal getStatementDetailCorrectAmount() {
        return statementDetailCorrectAmount;
    }

    public void setStatementDetailCorrectAmount(BigDecimal statementDetailCorrectAmount) {
        this.statementDetailCorrectAmount = statementDetailCorrectAmount;
    }

    public BigDecimal getStatementDetailOverduePaidAmount() {
        return statementDetailOverduePaidAmount;
    }

    public void setStatementDetailOverduePaidAmount(BigDecimal statementDetailOverduePaidAmount) {
        this.statementDetailOverduePaidAmount = statementDetailOverduePaidAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemIsNew() {
        return itemIsNew;
    }

    public void setItemIsNew(Integer itemIsNew) {
        this.itemIsNew = itemIsNew;
    }

    public BigDecimal getStatementCouponAmount() {
        return statementCouponAmount;
    }

    public void setStatementCouponAmount(BigDecimal statementCouponAmount) {
        this.statementCouponAmount = statementCouponAmount;
    }

    public BigDecimal getStatementDetailPenaltyAmount() {
        return statementDetailPenaltyAmount;
    }

    public void setStatementDetailPenaltyAmount(BigDecimal statementDetailPenaltyAmount) {
        this.statementDetailPenaltyAmount = statementDetailPenaltyAmount;
    }

    public BigDecimal getStatementDetailPenaltyPaidAmount() {
        return statementDetailPenaltyPaidAmount;
    }

    public void setStatementDetailPenaltyPaidAmount(BigDecimal statementDetailPenaltyPaidAmount) {
        this.statementDetailPenaltyPaidAmount = statementDetailPenaltyPaidAmount;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }
}