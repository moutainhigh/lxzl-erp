package com.lxzl.erp.common.constant;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 10:41 2018/3/14
 * @Modified By:
 */
public class RoleType {

    public static final Integer ROLE_TYPE_SUPER_ADMIN = 1000;  //超级管理员

    //总公司总裁办与分公司总经办
    public static final Integer ROLE_TYPE_ADMIN = 100001;  //管理员
    public static final Integer ROLE_TYPE_CHAIRMAN = 100002;  //董事长
    public static final Integer ROLE_TYPE_VICE_PRESIDENT = 100003;  //副总裁
    public static final Integer ROLE_TYPE_GENERAL_MANAGER = 100004;  //分公司总经理
    public static final Integer ROLE_TYPE_VICE_GENERAL_MANAGER = 100005;  //分公司副总经理
    public static final Integer ROLE_TYPE_GENERAL_MANAGER_ASSISTANT = 100006;  //总经理助理
    public static final Integer ROLE_TYPE_SECRETARY = 100007;  //总裁办秘书

    //总公司市场营销部
    public static final Integer ROLE_TYPE_MARKET_PROMOTION = 200001;  //市场推广
    public static final Integer ROLE_TYPE_MARKET_MANAGER = 200002;  //市场经理
    public static final Integer ROLE_TYPE_ELECTRIC_BUSINESS_OPERATION = 200003;  //电商运营
    public static final Integer ROLE_TYPE_VISUAL_DESIGN = 200004;  //视觉设计
    public static final Integer ROLE_TYPE_SEO_PROMOTION = 200005;  //SEO推广
    public static final Integer ROLE_TYPE_COPYWRITING_PLAN = 200006;  //文案策划

    //总公司研发部
    public static final Integer ROLE_TYPE_RESEARCH_MANAGER = 300001;  //研发经理
    public static final Integer ROLE_TYPE_JAVA_ENGINEER = 300002;  //JAVA工程师
    public static final Integer ROLE_TYPE_SENIOR_JAVA_ENGINEER = 300003;  //高级JAVA工程师
    public static final Integer ROLE_TYPE_TEST_ENGINEER = 300004;  //测试工程师
    public static final Integer ROLE_TYPE_FRONT_ENGINEER_CHARGEMAN = 300005;  //前端工程师组长
    public static final Integer ROLE_TYPE_FRONT_ENGINEER = 300006;  //前端工程师
    public static final Integer ROLE_TYPE_PRODUCT_MANAGER = 300007;  //产品经理

    //总公司风控部
    public static final Integer ROLE_TYPE_RISK_MANAGEMENT_MANAGER = 400001;  //风控负责人
    public static final Integer ROLE_TYPE_RISK_MANAGEMENT_CHARGE = 400002;  //风控主管
    public static final Integer ROLE_TYPE_RISK_MANAGEMENT_COMMISSIONER = 400003;  //风控专员

    //总公司采购部与分公司采购部
    public static final Integer ROLE_TYPE_PURCHASE_MANAGER = 500001;  //采购经理
    public static final Integer ROLE_TYPE_PURCHASE_CHARGE = 500002;  //采购主管
    public static final Integer ROLE_TYPE_PURCHASE_COMMISSIONER = 500003;  //采购专员
    public static final Integer ROLE_TYPE_PURCHASE_ASSISTANT = 500004;  //采购助理

    //总公司商务部与分公司商务部门
    public static final Integer ROLE_TYPE_BUSINESS_AFFAIRS_MANAGER = 600001;  //商务经理
    public static final Integer ROLE_TYPE_BUSINESS_AFFAIRS_CHARGE = 600002;  //商务主管
    public static final Integer ROLE_TYPE_BUSINESS_AFFAIRS_COMMISSIONER = 600003;  //商务专员
    public static final Integer ROLE_TYPE_BUSINESS_AFFAIRS_ASSISTANT = 600004;  //商务助理
    public static final Integer ROLE_TYPE_PLATFORM_BUSINESS_AFFAIRS = 600005;  //平台商务

    //售后部仓库与技术部门
    public static final Integer ROLE_TYPE_CUSTOMER_SERVICE_MANAGER = 700001;  //售后经理
    public static final Integer ROLE_TYPE_WAREHOUSE_CHARGE = 700002;  //仓库主管
    public static final Integer ROLE_TYPE_WAREHOUSE_COMMISSIONER = 700003;  //仓库专员
    public static final Integer ROLE_TYPE_WAREHOUSE_ASSISTANT = 700004;  //仓库文员
    public static final Integer ROLE_TYPE_400_CUSTOMER_SERVICE = 700005;  //400售后客服
    public static final Integer ROLE_TYPE_FIELD_PERSONNEL_CHARGE = 700006;  //外勤主管
    public static final Integer ROLE_TYPE_FIELD_PERSONNEL_ENGINEER = 700007;  //外勤工程师
    public static final Integer ROLE_TYPE_NETWORK_ENGINEER = 700008;  //网络工程师
    public static final Integer ROLE_TYPE_FIELD_PERSONNEL_DRIVER = 700009;  //司机
    public static final Integer ROLE_TYPE_CUSTOMER_SERVICE_CHARGE = 700010;  //售后主管

    //租赁业务部
    public static final Integer ROLE_TYPE_CUSTOMER_MANAGER = 800001;  //客户经理
    public static final Integer ROLE_TYPE_BUSINESS_CHARGE = 800002;  //业务主管
    public static final Integer ROLE_TYPE_BUSINESS_COMMISSIONER = 800003;  //业务员
    public static final Integer ROLE_TYPE_BUSINESS_ASSISTANT= 800004;  //业务助理

    //人事行政部
    public static final Integer ROLE_TYPE_PERSONNEL_ADMINISTRATION_MANAGER = 900001;  //人事行政经理
    public static final Integer ROLE_TYPE_PERSONNEL_ADMINISTRATION_CHARGE = 900002;  //人事行政主管
    public static final Integer ROLE_TYPE_PERSONNEL_ADMINISTRATION_COMMISSIONER = 900003;  //人事行政专员
    public static final Integer ROLE_TYPE_PERSONNEL_ADMINISTRATION_ASSISTANT = 900004;  //人事行政助理
    public static final Integer ROLE_TYPE_SALARY_PERFORMANCE_CHARGE = 900005;  //薪酬绩效主管
    public static final Integer ROLE_TYPE_ADMINISTRATION_COMMISSIONER = 900006;  //行政前台
    public static final Integer ROLE_TYPE_RECRUIT_CHARGE = 900007;  //招聘主管
    public static final Integer ROLE_TYPE_RECRUIT_COMMISSIONER = 900008;  //招聘专员

    //财务部
    public static final Integer ROLE_TYPE_FINANCE_CHIEF_INSPECTOR = 1000001;  //财务总监
    public static final Integer ROLE_TYPE_FINANCE_MANAGER = 1000002;  //财务经理
    public static final Integer ROLE_TYPE_PERSONNEL_FINANCE_CHARGE = 1000003;  //财务主管
    public static final Integer ROLE_TYPE_FINANCE_COMMISSIONER = 1000004;  //财务专员
    public static final Integer ROLE_TYPE_FINANCE_ASSISTANT = 1000005;  //财务助理
    public static final Integer ROLE_OVER_ALL_FINANCE_COMMISSIONER = 1000006;  //全盘会计
    public static final Integer ROLE_TYPE_CASHIER_COMMISSIONER = 1000007;  //出纳专员
    public static final Integer ROLE_TYPE_FINANCE_TRAINEE = 1000008;  //会计实习生
    public static final Integer ROLE_TYPE_DATA_OPERATION_ENGINEER = 1000009;  //数据运维工程师

    //回收销售部
    public static final Integer ROLE_PERSONNEL_RECOVERY_SALE_CHARGE = 1100001;  //回收销售主管
    public static final Integer ROLE_TYPE_RECOVERY_SALE_COMMISSIONER = 1100002;  //回收销售专员

    //电销
    public static final Integer ROLE_TYPE_ELECTRIC_INSPECTOR = 1200001;  //电销总监
    public static final Integer ROLE_TYPE_ELECTRIC_MANAGER = 1200002;  //电销经理
    public static final Integer ROLE_TYPE_ELECTRIC_CHARGE = 1200003;  //电销主管
    public static final Integer ROLE_TYPE_ELECTRIC_COMMISSIONER = 1200004;  //电销专员
    public static final Integer ROLE_TYPE_ELECTRIC_ASSISTANT = 1200005;  //电销助理
    public static final Integer ROLE_TYPE_400_CALL_SERVICE = 1200006;  //400电话客服
    public static final Integer ROLE_TYPE_ELECTRIC_TRAINER = 1200007;  //电销培训师

    //渠道大客户
    public static final Integer ROLE_TYPE_CHANNEL_MANAGER = 1300001;  //渠道总经理

}
