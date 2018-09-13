package com.lxzl.erp.common.domain.statement;

import com.lxzl.erp.common.util.BigDecimalUtil;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;

import java.math.BigDecimal;

/**
 * @author: huanglong
 * @date: 2018/6/15/015 11:29
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class AmountNeedReturn {
    //已付设备押金
    BigDecimal depositPaidAmount = BigDecimal.ZERO;
    //已付其他费用
    BigDecimal otherPaidAmount = BigDecimal.ZERO;
    // 已付租金
    BigDecimal rentPaidAmount = BigDecimal.ZERO;
    //已付逾期费用
    BigDecimal overduePaidAmount = BigDecimal.ZERO;
    //已付违约金
    BigDecimal penaltyPaidAmount = BigDecimal.ZERO;
    //已付租金押金
    BigDecimal rentDepositPaidAmount = BigDecimal.ZERO;

    public AmountNeedReturn add(AmountNeedReturn amountNeedReturn){
        this.depositPaidAmount= BigDecimalUtil.add(this.depositPaidAmount,amountNeedReturn.getDepositPaidAmount());
        this.otherPaidAmount=BigDecimalUtil.add(this.otherPaidAmount,amountNeedReturn.getOtherPaidAmount());
        this.rentPaidAmount=BigDecimalUtil.add(this.rentPaidAmount,amountNeedReturn.getRentPaidAmount());
        this.overduePaidAmount=BigDecimalUtil.add(this.overduePaidAmount,amountNeedReturn.getOverduePaidAmount());
        this.penaltyPaidAmount= BigDecimalUtil.add(this.penaltyPaidAmount,amountNeedReturn.getPenaltyPaidAmount());
        this.rentDepositPaidAmount=BigDecimalUtil.add(this.rentDepositPaidAmount,amountNeedReturn.getRentDepositPaidAmount());
        return this;
    }
    public AmountNeedReturn() {
    }

    public AmountNeedReturn(BigDecimal depositPaidAmount, BigDecimal rentPaidAmount, BigDecimal rentDepositPaidAmount) {
        this.depositPaidAmount = depositPaidAmount;
        this.rentPaidAmount = rentPaidAmount;
        this.rentDepositPaidAmount = rentDepositPaidAmount;
    }

    public BigDecimal getDepositPaidAmount() {
        return depositPaidAmount;
    }

    public void setDepositPaidAmount(BigDecimal depositPaidAmount) {
        this.depositPaidAmount = depositPaidAmount;
    }

    public BigDecimal getOtherPaidAmount() {
        return otherPaidAmount;
    }

    public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
        this.otherPaidAmount = otherPaidAmount;
    }

    public BigDecimal getRentPaidAmount() {
        return rentPaidAmount;
    }

    public void setRentPaidAmount(BigDecimal rentPaidAmount) {
        this.rentPaidAmount = rentPaidAmount;
    }

    public BigDecimal getOverduePaidAmount() {
        return overduePaidAmount;
    }

    public void setOverduePaidAmount(BigDecimal overduePaidAmount) {
        this.overduePaidAmount = overduePaidAmount;
    }

    public BigDecimal getPenaltyPaidAmount() {
        return penaltyPaidAmount;
    }

    public void setPenaltyPaidAmount(BigDecimal penaltyPaidAmount) {
        this.penaltyPaidAmount = penaltyPaidAmount;
    }

    public BigDecimal getRentDepositPaidAmount() {
        return rentDepositPaidAmount;
    }

    public void setRentDepositPaidAmount(BigDecimal rentDepositPaidAmount) {
        this.rentDepositPaidAmount = rentDepositPaidAmount;
    }
}
