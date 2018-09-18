package com.lxzl.erp.common.constant;

/**
 * 排序的订单类型
 * @author daiqi
 * @create 2018-07-24 16:50
 */
public class SortOrderItemType {
    /** 排序订单项类型---商品---租赁 */
    public static final Integer PRODUCT_RENT = 1;
    /** 排序订单项类型---商品---退租 */
    public static final Integer PRODUCT_RETURN = 2;
    /** 排序订单项类型---商品---续租 */
    public static final Integer PRODUCT_RELET = 3;
    /** 排序订单项类型---商品---续租退货 */
    public static final Integer PRODUCT_RELET_RETURN = 4;
    /** 排序订单项类型---配件---租赁 */
    public static final Integer MATERIAL_RENT = 5;
    /** 排序订单项类型---配件---退租 */
    public static final Integer MATERIAL_RETURN = 6;
    /** 排序订单项类型---配件---续租 */
    public static final Integer MATERIAL_RELET = 7;
    /** 排序订单项类型---配件---续租退货 */
    public static final Integer MATERIAL_RELET_RETURN = 8;
}
