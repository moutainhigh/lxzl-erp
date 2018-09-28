package com.lxzl.erp.common.constant;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\12 0012 14:10
 * 换货原因类型, 0-设备故障等我方原因导致换货 ，1-设备故障等客户方原因导致的换货，2-主观因素等客户方原因导致换货，3-其他
 */
public class ReplaceReasonType {
    public static final Integer REPLACR_REASON_TYPE_COMPANY_DEMAGE = 0;//设备故障等我方原因导致换货
    public static final Integer REPLACR_REASON_TYPE_CUSTOMER_DEMAGE = 1;//设备故障等客户方原因导致的换货
    public static final Integer REPLACR_REASON_TYPE_CUSTOMER = 2;//主观因素等客户方原因导致换货
    public static final Integer REPLACR_REASON_TYPE_OTHER = 3;//其他
}
