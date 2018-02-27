package com.lxzl.erp.common.constant;

/**
 * 描述:  结算单明细类型,1-租金,2-押金,3-抵消租金（退租）,4-退押金,5-其他费用,6- 逾期费用，这个只供冲正使用
 * @author gaochao
 * @date 2018-01-23 14:22
 */
public class StatementDetailType {
    public static final Integer STATEMENT_DETAIL_TYPE_RENT = 1;     // 租金
    public static final Integer STATEMENT_DETAIL_TYPE_DEPOSIT = 2;         // 押金
    public static final Integer STATEMENT_DETAIL_TYPE_OFFSET_RENT = 3;         // 抵消租金（退租）
    public static final Integer STATEMENT_DETAIL_TYPE_RETURN_DEPOSIT = 4;         // 退押金
    public static final Integer STATEMENT_DETAIL_TYPE_OTHER = 5;         // 其他费用
    public static final Integer STATEMENT_DETAIL_TYPE_OVERDUE = 6;         // 逾期费用，这个只供冲正使用
}
