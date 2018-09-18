package com.lxzl.erp.common.domain.statement.pojo.dto;

/**
 * 对账单详情数据传输对象
 *
 * @author daiqi
 * @create 2018-07-04 10:46
 */
public interface CheckStatementDetailDTOInf {
    /** 获取租赁订单号  */
    String getOrderNoStr();
    /** 获取业务类型字符串 */
    String getBusinessTypeStr();
    /** 获取订单向类型字符串 */
    String getOrderItemTypeStr();
    /** 获取商品名 */
    String getItemNameStr();
    /** 获取配置 */
    String getItemSkuNameStr();
    /** 获取新旧程度字符串 */
    String getIsNewStr();
    /** 获取租赁开始日期字符串格式 */
    String getRentStartTimeStr();
    /** 获取租赁结束日期字符串 */
    String getExpectReturnTimeStr();
    /** 获取月份字符串 */
    String getMonthStr();
    /** 获取天字符串 */
    String getDayStr();
    /** 获取租赁总期限字符串 */
    String getAllRentTimeLengthStr();
    /** 获取租赁期限字符串 */
    String getAllPeriodStartAndEndStr();
    /** 获取本期开始日期字符串 */
    String getStatementStartTimeStr();
    /** 获取本期结束日期字符串 */
    String getStatementEndTimeStr();
    /** 获取结算的月 */
    String getStatementMonthStr();
    /** 获取结算的天 */
    String getStatementDayStr();
    /** 获取租赁期限字符串 */
    String getRentTimeLengthStr();
    /** 获取本期费用起止字符串 */
    String getCurrentPeriodStartAndEndStr();
    /** 获取支付方式字符串 */
    String getPayModeStr();
    /** 获取租赁方案字符串 */
    String getRentProgrammeStr();
    /** 获取租赁数量字符串 */
    String getItemCountStr();
    /** 获取单价 */
    String getUnitAmountInfoStr();
    /** 获取本期租金 */
    String getStatementDetailRentAmountStr();
    /** 获取押金字符串 */
    String getStatementDepositAmountStr();
    /** 获取逾期金额字符串 */
    String getStatementOverdueAmountStr();
    /** 获取其他费用字符串 */
    String getStatementOtherAmountStr();
    /** 获取调整金额字符串 */
    String getStatementCorrectAmountStr();
    /** 获取本期应付金额 */
    String getStatementDetailAmountStr();
    /** 获取本期应付日期 */
    String getStatementExpectPayTimeStr();
    /** 获取本月应付金额 */
    String getMonthPayableAmountStr();
    /** 获取支付状态字符串 */
    String getStatementDetailStatusStr();
    /** 获取退租订单号 */
    String getK3ReturnOrderDONoStr();
    /** 获取退货原因字符串 */
    String getReturnReasonTypeStr();
    /** 获取冲正订单号 */
    String getStatementCorrectNoStr();
    /** 获取冲正原因 */
    String getStatementCorrectReasonStr();
}
