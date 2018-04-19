package com.lxzl.erp.common.domain.coupon;

import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\17 0017 19:03
 */
public class StatementCouponParam {
    private StatementOrderDetail statementOrderDetail;
    private Coupon coupon;

    public StatementOrderDetail getStatementOrderDetail() {
        return statementOrderDetail;
    }

    public void setStatementOrderDetail(StatementOrderDetail statementOrderDetail) {
        this.statementOrderDetail = statementOrderDetail;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
