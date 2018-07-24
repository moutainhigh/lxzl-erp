package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/6/21
 * @Time : Created in 20:50
 */
public class ReturnReasonType {
    public static final Integer NOT_REFUNDABLE = 1; //1-客户方设备不愿或无法退还
    public static final Integer EXPIRATION_OF_NORMAL = 2; //2-期满正常收回
    public static final Integer RETIRING_IN_ADVANCE = 3; //3-提前退租
    public static final Integer NO_PAYMENT_ON_TIME = 4; //4-未按时付款或风险等原因上门收回。
    public static final Integer EQUIPMENT_FAILURE = 5; //5-设备故障等我方原因导致退货
    public static final Integer SUBJECTIVE_FACTORS = 6; //6-主观因素等客户方原因导致退货
    public static final Integer REPLACEMENT_EQUIPMENT = 7; //7-更换设备
    public static final Integer COMPANY_CLOSURES = 8; //8-公司经营不善/倒闭
    public static final Integer IDLE_EQUIPMENT = 9; //9-项目结束闲置
    public static final Integer FOLLOW_THE_RENT = 10; //10-满三个月或六个月随租随还
    public static final Integer OTHER = 11; //11-其它',
    public static final Integer COMMODITY_FAILURE_REPLACEMENT = 12; //12-商品故障更换,
    public static final Integer CONFIGURATION_UPGRADE_REPLACEMENT = 13; //13-配置升级更换,
    public static final Integer ORDER_INVALIDATION = 14; //14-订单作废/取消,
    public static final Integer PRICE_CAUSE_TO_PURCHASE = 15; //15-价格原因转购买,
    public static final Integer PRICE_REASONS_FOR_SUPPLIERS = 16; //16-价格原因换供应商,
    public static final Integer PURCHASE_OF_COMMODITY_QUALITY = 17; //17-商品质量问题转购买,
    public static final Integer COMMODITY_QUALITY_PROBLEMS_FOR_SUPPLIERS = 18; //18-商品质量问题换供应商,
    public static final Integer THE_SERVICE_IS_NOT_RETURNED_IN_TIME = 19; //19-服务不及时造成退货,
    public static final Integer STAFF_LEAVING_OR_STUDENT_GRADUATION_IDLE = 20; //20-人员离职/学生毕业闲置,


}
