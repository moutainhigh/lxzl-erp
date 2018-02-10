package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 9:00
 */
public class StatementOrderStatus {
    public static final Integer STATEMENT_ORDER_STATUS_INIT = 0;        // 初始化
    public static final Integer STATEMENT_ORDER_STATUS_SETTLED_PART = 4;     // 部分结算完成
    public static final Integer STATEMENT_ORDER_STATUS_SETTLED = 8;     // 结算完成
    public static final Integer STATEMENT_ORDER_STATUS_NO = 16;     // 无需结算
    public static final Integer STATEMENT_ORDER_STATUS_CORRECTED = 20;     // 已冲正
}
