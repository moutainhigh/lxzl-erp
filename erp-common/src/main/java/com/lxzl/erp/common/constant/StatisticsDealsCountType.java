package com.lxzl.erp.common.constant;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/18
 * Time: 11:04
 */
public class StatisticsDealsCountType {
    public static final int DEALS_COUNT_TYPE_ALL = 0;   //包含以下几种类型(做统计合计时使用）
    public static final int DEALS_COUNT_TYPE_CUSTOMER = 1;   // 客户
    public static final int DEALS_COUNT_TYPE_NEW_CUSTOMER = 2;  // 新增客户
    public static final int DEALS_COUNT_TYPE_RENT_PRODUCT = 3;   // 出库商品
    public static final int DEALS_COUNT_TYPE_RETURN_PRODUCT = 4;   // 退货商品
    public static final int DEALS_COUNT_TYPE_INCREASE_PRODUCT = 5; //净增商品

}
