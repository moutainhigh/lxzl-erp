package com.lxzl.erp.common.util;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.rent.BaseCheckStatementDetailRentDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.unrent.BaseCheckStatementDetailUnRentDTO;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author daiqi
 * @create 2018-07-06 10:19
 */
public class CheckStatementUtil {
    public static final String FORMAT_MONTH = "yyyy-MM";
    /** 默认的占位符 */
    public static final String PLACEHOLDER_DEFAULT = "-";
    public static String getBusinessTypeStr(Integer orderType) {
        if (OrderType.ORDER_TYPE_ORDER.equals(orderType)) {
            return "租赁";
        } else if (OrderType.ORDER_TYPE_RETURN.equals(orderType)) {
            return "退货";
        } else if (OrderType.ORDER_TYPE_RELET.equals(orderType)) {
            return "续租";
        } else if (OrderType.ORDER_TYPE_RELET_RETURN.equals(orderType)) {
            return "续租退货";
        }
        return PLACEHOLDER_DEFAULT;
    }
    public static String getOrderItemTypeStr(Integer orderItemType) {
        if(OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType)){
            return "商品";
        }else if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType)){
            return "物料";
        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT.equals(orderItemType)){
            return "换货商品";
        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL.equals(orderItemType)){
            return "换货物料";
        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(orderItemType)){
            return "退货商品";
        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(orderItemType)){
            return "退货物料";
        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.equals(orderItemType)){
            return "退货其他";
        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER.equals(orderItemType)){
            return "换货其他";
        }else if(OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(orderItemType)){
            return "其他";
        }
        return PLACEHOLDER_DEFAULT;
    }
    public static BigDecimal roundScale(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return BigDecimalUtil.round(bigDecimal, 2);
    }
    public static String getIsNewStr(Integer isNew) {
        if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)){
            return "次新";
        }else if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)){
            return "全新";
        }
        return PLACEHOLDER_DEFAULT;
    }

    public static String getPayModeStr(Integer payMode) {
        if(OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)){
            return "先付后用";
        }else if(OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)){
            return "先用后付";
        }else if(OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(payMode)){
            return "首付30%";
        }
        return null;
    }

    public static String getStatementDetailStatusStr(Integer statementDetailStatus) {
        if(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementDetailStatus)){
            return "未支付";
        }else  if(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(statementDetailStatus)){
            return "部分结算完成";
        }else if(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementDetailStatus)){
            return "支付完成";
        }else if(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementDetailStatus)){
            return "无需结算";
        }else if(StatementOrderStatus.STATEMENT_ORDER_STATUS_CORRECTED.equals(statementDetailStatus)){
            return "已冲正";
        }
        return PLACEHOLDER_DEFAULT;
    }
    public static String getYearMonthStr(Date needMonthDate) {
        if (needMonthDate == null) {
            return null;
        }
        return DateFormatUtils.format(needMonthDate, FORMAT_MONTH);
    }

    public static String getMonthStr(Date needMonthDate) {
        if (needMonthDate == null) {
            return null;
        }
        return DateFormatUtils.format(needMonthDate, "MM");
    }
    public static String getDayStr(Date needMonthDate) {
        if (needMonthDate == null) {
            return null;
        }
        return DateFormatUtils.format(needMonthDate, "dd");
    }

    /**
     * 获取期限字符串
     */
    public static String getPeriod(Date beginDate, Date endDate, String lengthStr) {
        String beginDateStr = DateFormatUtils.format(beginDate, "yyyy-MM-dd");
        String endDateStr = DateFormatUtils.format(endDate, "yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("开始:").append(beginDateStr).append("\n");
        sb.append("结束:").append(endDateStr).append("\n");
        sb.append("期限:").append(lengthStr);
        return sb.toString();
    }

    public static BigDecimal initBigDecimal(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            bigDecimal = BigDecimal.ZERO;
        }
        return roundScale(bigDecimal);
    }

    public static boolean isReturnOrder(BaseCheckStatementDetailDTO checkStatementDetailDTO) {
        if (checkStatementDetailDTO == null) {
            return false;
        }
        if (checkStatementDetailDTO instanceof BaseCheckStatementDetailUnRentDTO) {
            return true;
        }
        return false;
    }
    public static boolean isRentOrder(BaseCheckStatementDetailDTO checkStatementDetailDTO) {
        if (checkStatementDetailDTO == null) {
            return false;
        }
        if (checkStatementDetailDTO instanceof BaseCheckStatementDetailRentDTO) {
            return true;
        }
        return false;
    }
}
