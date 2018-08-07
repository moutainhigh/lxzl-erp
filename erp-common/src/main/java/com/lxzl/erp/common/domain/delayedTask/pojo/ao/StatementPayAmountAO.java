package com.lxzl.erp.common.domain.delayedTask.pojo.ao;

import java.math.BigDecimal;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\7 0007 14:58
 */
public class StatementPayAmountAO {
    private BigDecimal beforePeriodUnpaid;
    private BigDecimal allPeriodUnpaid;

    public BigDecimal getBeforePeriodUnpaid() {
        return beforePeriodUnpaid;
    }

    public void setBeforePeriodUnpaid(BigDecimal beforePeriodUnpaid) {
        this.beforePeriodUnpaid = beforePeriodUnpaid;
    }

    public BigDecimal getAllPeriodUnpaid() {
        return allPeriodUnpaid;
    }

    public void setAllPeriodUnpaid(BigDecimal allPeriodUnpaid) {
        this.allPeriodUnpaid = allPeriodUnpaid;
    }
}
