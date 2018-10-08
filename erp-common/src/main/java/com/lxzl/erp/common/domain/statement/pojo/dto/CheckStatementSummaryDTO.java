package com.lxzl.erp.common.domain.statement.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CheckStatementUtil;

import java.math.BigDecimal;

/**
 * 对账单汇总数据传输对象
 *
 * @author daiqi
 * @create 2018-07-04 10:48
 */
public class CheckStatementSummaryDTO {
    /**
     * 本月应付金额
     */
    private BigDecimal thisMonthPayableAmount;
    /**
     * 本月已付金额
     */
    private BigDecimal thisMonthPaidAmount;
    /**
     * 本月未付金额
     */
    private BigDecimal thisMonthUnpaidAmount;
    /**
     * 截止上期未付金额
     */
    private BigDecimal previousPeriodEndUnpaidAmount;
    /**
     * 累计未付金额
     */
    private BigDecimal totalUnpaidAmount;
    /**
     * 账户余额
     */
    private BigDecimal accountAmount;
    /**
     * 需要支付的金额
     */
    private BigDecimal needPayAmount;

    public CheckStatementSummaryDTO() {
        this.thisMonthPayableAmount = BigDecimal.ZERO;
        this.thisMonthPaidAmount = BigDecimal.ZERO;
        this.thisMonthUnpaidAmount = BigDecimal.ZERO;
        this.previousPeriodEndUnpaidAmount = BigDecimal.ZERO;
        this.totalUnpaidAmount = BigDecimal.ZERO;
    }

    public BigDecimal getThisMonthPayableAmount() {
        return thisMonthPayableAmount;
    }

    public void setThisMonthPayableAmount(BigDecimal thisMonthPayableAmount) {
        this.thisMonthPayableAmount = thisMonthPayableAmount;
    }

    public BigDecimal getThisMonthPaidAmount() {
        return thisMonthPaidAmount;
    }

    public void setThisMonthPaidAmount(BigDecimal thisMonthPaidAmount) {
        this.thisMonthPaidAmount = thisMonthPaidAmount;
    }

    public BigDecimal getThisMonthUnpaidAmount() {
        return thisMonthUnpaidAmount;
    }

    public void setThisMonthUnpaidAmount(BigDecimal thisMonthUnpaidAmount) {
        this.thisMonthUnpaidAmount = thisMonthUnpaidAmount;
    }

    public BigDecimal getPreviousPeriodEndUnpaidAmount() {
        return previousPeriodEndUnpaidAmount;
    }

    public void setPreviousPeriodEndUnpaidAmount(BigDecimal previousPeriodEndUnpaidAmount) {
        this.previousPeriodEndUnpaidAmount = previousPeriodEndUnpaidAmount;
    }

    public BigDecimal getTotalUnpaidAmount() {
        return totalUnpaidAmount;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public BigDecimal getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(BigDecimal needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public void setTotalUnpaidAmount(BigDecimal totalUnpaidAmount) {
        this.totalUnpaidAmount = totalUnpaidAmount;
    }

    public CheckStatementSummaryDTO buildMonthAmount(BaseCheckStatementDetailDTO checkStatementDetailDTO) {
        this.thisMonthPayableAmount = BigDecimalUtil.add(this.thisMonthPayableAmount, checkStatementDetailDTO.getMonthPayableAmount());
        this.thisMonthPaidAmount = BigDecimalUtil.add(this.thisMonthPaidAmount, checkStatementDetailDTO.getMonthPaidAmount());
        this.thisMonthUnpaidAmount = BigDecimalUtil.add(this.thisMonthUnpaidAmount, checkStatementDetailDTO.getMonthUnpaidAmount());
        return this;
    }

    /**
     * 构建周期未付金额
     */
    public void buildPeriodsUnpaidAmount(BigDecimal previousPeriodEndUnpaidAmount) {
        this.previousPeriodEndUnpaidAmount = previousPeriodEndUnpaidAmount;
        this.totalUnpaidAmount = BigDecimalUtil.add(previousPeriodEndUnpaidAmount, this.thisMonthUnpaidAmount);
        this.needPayAmount = BigDecimalUtil.sub(this.totalUnpaidAmount, this.accountAmount);
        if (BigDecimalUtil.compare(this.needPayAmount, BigDecimal.ZERO) <= 0) {
            this.needPayAmount = BigDecimal.ZERO;
        }
    }

    @JSONField(serialize = false)
    public String getThisMonthPayableAmountStr() {
        return CheckStatementUtil.roundScale(this.thisMonthPayableAmount).toString();
    }

    @JSONField(serialize = false)
    public String getThisMonthPaidAmountStr() {
        return CheckStatementUtil.roundScale(this.thisMonthPaidAmount).toString();
    }

    @JSONField(serialize = false)
    public String getThisMonthUnpaidAmountStr() {
        return CheckStatementUtil.roundScale(this.thisMonthUnpaidAmount).toString();
    }

    @JSONField(serialize = false)
    public String getPreviousPeriodEndUnpaidAmountStr() {
        return CheckStatementUtil.roundScale(this.previousPeriodEndUnpaidAmount).toString();
    }

    @JSONField(serialize = false)
    public String getTotalUnpaidAmountStr() {
        return CheckStatementUtil.roundScale(this.totalUnpaidAmount).toString();
    }

    @JSONField(serialize = false)
    public String getAccountAmountStr() {
        return CheckStatementUtil.roundScale(this.accountAmount).toString();
    }

    @JSONField(serialize = false)
    public String getNeedPayAmountStr() {
        return CheckStatementUtil.roundScale(this.needPayAmount).toString();
    }
}
