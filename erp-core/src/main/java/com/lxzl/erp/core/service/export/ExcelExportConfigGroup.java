package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 16:02
 */

public class ExcelExportConfigGroup {

    public static ExcelExportConfig bankSlipDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statementOrderConfig = new ExcelExportConfig();
    public static ExcelExportConfig statementOrderDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statisticsSalesmanDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statementOrderPayDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statementOrderCheckConfig = new ExcelExportConfig();
    public static ExcelExportConfig statisticsFinanceWeeklyConfig = new ExcelExportConfig();
    public static ExcelExportConfig statisticsRentProductDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statisticsReturnProductDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig statementOrderCheckConfigNew = new ExcelExportConfig();

    static {
        initBankSlipDetailConfig();
        initStatementOrderConfig();
        initStatementOrderDetailConfig();
        initStatisticsSalesmanDetailConfig();
        initStatementOrderPayDetailConfig();
        initStatementOrderCheckConfig();
        initStatisticsFinanceWeeklyConfig();
        initStatisticsRentProductDetailConfig();
        initStatisticsReturnProductDetailConfig();
        initStatementOrderCheckConfigNew();
    }
    private static ExcelExportView createRentTypeExcelExportView() {
        ExcelExportView rentTypeExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer rentType = (Integer) o;
                    if (OrderRentType.RENT_TYPE_DAY == rentType) {
                        return "按天租";
                    } else if (OrderRentType.RENT_TYPE_MONTH == rentType) {
                        return "按月租";
                    }
                }
                return null;
            }
        };
        return rentTypeExcelExportView;
    }

    private static ExcelExportView createRentLengthTypeExcelExportView() {
        ExcelExportView rentTypeExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer rentLengthType = (Integer) o;
                    if (StatisticsRentLengthType.RENT_LENGTH_TYPE_SHORT == rentLengthType) {
                        return "短租";
                    } else if (StatisticsRentLengthType.RENT_LENGTH_TYPE_LONG == rentLengthType) {
                        return "长租";
                    } else if (StatisticsRentLengthType.RENT_LENGTH_TYPE_SHORTEST == rentLengthType) {
                        return "短短租";
                    }
                }
                return null;
            }
        };
        return rentTypeExcelExportView;
    }

    private static  ExcelExportView createSubCompanyExcelExportView() {
        ExcelExportView subCompanyExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer rentLengthType = (Integer) o;
                    if (1 == rentLengthType) {
                        return "总公司";
                    } else if (2 == rentLengthType) {
                        return "深圳分公司";
                    } else if (3 == rentLengthType) {
                        return "上海分公司";
                    } else if (4 == rentLengthType) {
                        return "北京分公司";
                    } else if (5 == rentLengthType) {
                        return "广州分公司";
                    } else if (6 == rentLengthType) {
                        return "南京分公司";
                    } else if (7 == rentLengthType) {
                        return "厦门分公司";
                    } else if (8 == rentLengthType) {
                        return "武汉分公司";
                    } else if (9 == rentLengthType) {
                        return "成都分公司";
                    } else if (10 == rentLengthType) {
                        return "电销";
                    } else if (11 == rentLengthType) {
                        return "大客户";
                    }
                }
                return null;
            }
        };
        return subCompanyExcelExportView;
    }
    private static ExcelExportView createCustomerOriginExcelExportView() {
        ExcelExportView customerOriginExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer customerOrigin = (Integer) o;
                    if (1 == customerOrigin) {
                        return "地推活动";
                    } else if (2 == customerOrigin) {
                        return "展会了解";
                    } else if (3 == customerOrigin) {
                        return "业务联系";
                    } else if (4 == customerOrigin) {
                        return "百度推广";
                    } else if (5 == customerOrigin) {
                        return "朋友推荐";
                    } else if (6 == customerOrigin) {
                        return "其他广告";
                    }
                }
                return "";
            }
        };
        return customerOriginExcelExportView;
    }

    private static ExcelExportView createCustomerIndustryExcelExportView() {
        ExcelExportView customerIndustryExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    String customerIndustry = (String) o;
                    if ("1".equals(customerIndustry)) {
                        return "计算机软硬件(互联网推广_APP开发等)";
                    } else if ("2".equals(customerIndustry)) {
                        return "互联网电子商务";
                    } else if ("3".equals(customerIndustry)) {
                        return "网络游戏_视频直播_微信营销";
                    } else if ("4".equals(customerIndustry))  {
                        return "通信_电信_电子产品";
                    } else if ("5".equals(customerIndustry)) {
                        return "互联网金融_贷款_期货现货贵金属_分期支付_担保拍卖";
                    } else if ("6".equals(customerIndustry)) {
                        return "实业投资_保险_证券_银行";
                    } else if ("7".equals(customerIndustry)) {
                        return "教育_培训";
                    } else if ("8".equals(customerIndustry)) {
                        return "政府_公共事业_非盈利性机构";
                    } else if ("9".equals(customerIndustry)) {
                        return "媒体_出版_影视_文化传播";
                    } else if ("10".equals(customerIndustry)) {
                        return "婚纱摄影_旅游度假_酒店餐饮";
                    } else if ("11".equals(customerIndustry)) {
                        return "服务娱乐_医疗美容";
                    } else if ("12".equals(customerIndustry)) {
                        return "专业服务(租赁服务_企业注册_人力资源_贸易报关_中介咨询_实体广告等)";
                    } else if ("13".equals(customerIndustry)) {
                        return "展览会议_公关活动";
                    } else if ("14".equals(customerIndustry)) {
                        return "房地产_建筑建设_开发";
                    } else if ("15".equals(customerIndustry)) {
                        return "家居建材_装饰设计";
                    } else if ("16".equals(customerIndustry)) {
                        return "交通运输_物流仓储_供应链";
                    } else if ("17".equals(customerIndustry)) {
                        return "维修安装_家政_叫车服务";
                    } else if ("18".equals(customerIndustry)) {
                        return "加工制造_工业自动化_汽车摩托车销售";
                    } else if ("19".equals(customerIndustry)) {
                        return "快速消费品(服饰日化_食品烟酒等)";
                    } else if ("99".equals(customerIndustry)) {
                        return "其他";
                    } else {
                        return "其他";
                    }
                }
                return "";
            }
        };
        return customerIndustryExcelExportView;
    }
    private static ExcelExportView createReturnReasonExcelExportView() {
        ExcelExportView returnReasonExcelExportView = new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer returnReasonType = (Integer) o;
                    if (1 == returnReasonType) {
                        return "客户方设备不愿或无法退还";
                    } else if (2 == returnReasonType) {
                        return "期满正常收回";
                    } else if (3 == returnReasonType) {
                        return "提前退租";
                    } else if (4 == returnReasonType) {
                        return "未按时付款或风险等原因上门收回";
                    } else if (5 == returnReasonType) {
                        return "设备故障等我方原因导致退货";
                    } else if (6 == returnReasonType) {
                        return "主观因素等客户方原因导致退货";
                    } else if (7 == returnReasonType) {
                        return "更换设备";
                    } else if (8 == returnReasonType) {
                        return "公司倒闭";
                    } else if (9 == returnReasonType) {
                        return "设备闲置";
                    } else if (10 == returnReasonType) {
                        return "满三个月或六个月随租随还";
                    } else if (11 == returnReasonType) {
                        return "其它";
                    } else {
                        return "其它";
                    }
                }
                return null;
            }
        };
        return returnReasonExcelExportView;
    }


    private static void initStatisticsRentProductDetailConfig() {
        statisticsRentProductDetailConfig.addConfig(new ColConfig("orderNo", "订单编号"))
                .addConfig(new ColConfig("orderSubCompanyId", "订单分公司", createSubCompanyExcelExportView()))
                .addConfig(new ColConfig("deliverySubCompanyId", "发货分公司", createSubCompanyExcelExportView()))
                .addConfig(new ColConfig("customerName","客户名称"))
                .addConfig(new ColConfig("customerOrigin","客户来源", createCustomerOriginExcelExportView()))
                .addConfig(new ColConfig("industry","客户行业", createCustomerIndustryExcelExportView()))
                .addConfig(new ColConfig("k3CustomerCode","K3客户编码"))
                .addConfig(new ColConfig("rentType","租赁类型", createRentTypeExcelExportView()))
                .addConfig(new ColConfig("rentTimeLength","租赁时长"))
                .addConfig(new ColConfig("rentLengthType","租赁长短类型", createRentLengthTypeExcelExportView()))
                .addConfig(new ColConfig("productName","商品名称"))
                .addConfig(new ColConfig("k3ProductNo","商品编号"))
                .addConfig(new ColConfig("productCount","商品数量"))
                .addConfig(new ColConfig("productUnitAmount","商品单价"))
                .addConfig(new ColConfig("rentStartTime","订单起租时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("expectReturnTime","订单截止时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("isNewProduct", "是否全新", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        if (o != null) {
                            Integer isNewProduct = (Integer) o;
                            if (CommonConstant.YES == isNewProduct) {
                                return "是";
                            } else if (CommonConstant.NO == isNewProduct) {
                                return "否";
                            }
                        }
                        return null;
                    }
                }))
                .addConfig(new ColConfig("deliveryTime","发货时间", DateExcelExportView.getInstance()));
    }

    private static void initStatisticsReturnProductDetailConfig() {
        statisticsReturnProductDetailConfig.addConfig(new ColConfig("returnOrderNo", "退货单号"))
                .addConfig(new ColConfig("productNo", "商品编号"))
                .addConfig(new ColConfig("productName", "商品名称"))
                .addConfig(new ColConfig("productUnitAmount", "商品单价"))
                .addConfig(new ColConfig("productCount", "商品预计退货数量"))
                .addConfig(new ColConfig("realProductCount", "商品实际退货数量"))
                .addConfig(new ColConfig("orderNo", "订单编号"))
                .addConfig(new ColConfig("orderSubCompanyId", "订单分公司", createSubCompanyExcelExportView()))
                .addConfig(new ColConfig("deliverySubCompanyId", "发货分公司", createSubCompanyExcelExportView()))
                .addConfig(new ColConfig("customerNo", "客户编号"))
                .addConfig(new ColConfig("customerName", "客户名称"))
                .addConfig(new ColConfig("customerOrigin","客户来源", createCustomerOriginExcelExportView()))
                .addConfig(new ColConfig("industry","客户行业", createCustomerIndustryExcelExportView()))
                .addConfig(new ColConfig("k3CustomerCode","K3客户编码"))
                .addConfig(new ColConfig("rentType","租赁类型", createRentTypeExcelExportView()))
                .addConfig(new ColConfig("rentTimeLength","租赁时长"))
                .addConfig(new ColConfig("rentLengthType","租赁长短类型", createRentLengthTypeExcelExportView()))
                .addConfig(new ColConfig("returnTime","退货时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("rentStartTime","订单起租时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("expectReturnTime","订单截止时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("returnReasonType","退货原因", createReturnReasonExcelExportView()));
    }


    private static void initStatisticsFinanceWeeklyConfig() {
        statisticsFinanceWeeklyConfig.addConfig(new ColConfig("orderOrigin", "订单来源", new ExcelExportView() {
            @Override
            public Object view(Object o) {
                if (o != null) {
                    Integer orderOriginType = (Integer) o;
                    if (StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_KA == orderOriginType) {
                        return "KA";
                    } else if (StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_TMK == orderOriginType) {
                        return "电销";
                    } else if (StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_BCC == orderOriginType) {
                        return "大客户渠道";
                    } else if (StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL == orderOriginType) {
                        return "合计";
                    }
                }
                return null;
            }
        }))
         .addConfig(new ColConfig("rentLengthType", "租赁类型", createRentLengthTypeExcelExportView()))
         .addConfig(new ColConfig("dealsCountType", "统计科目类型", new ExcelExportView() {
             @Override
             public Object view(Object o) {
                 if (o != null) {
                     Integer dealsCountType = (Integer) o;
                     if (StatisticsDealsCountType.DEALS_COUNT_TYPE_CUSTOMER == dealsCountType) {
                         return "成交客户数量";
                     } else if (StatisticsDealsCountType.DEALS_COUNT_TYPE_NEW_CUSTOMER == dealsCountType) {
                         return "成交新客户数量";
                     } else if (StatisticsDealsCountType.DEALS_COUNT_TYPE_RENT_PRODUCT == dealsCountType){
                         return "出库商品数量";
                     } else if (StatisticsDealsCountType.DEALS_COUNT_TYPE_RETURN_PRODUCT == dealsCountType){
                         return "退货商品数量";
                     } else if (StatisticsDealsCountType.DEALS_COUNT_TYPE_INCREASE_PRODUCT == dealsCountType){
                         return "净增商品数量";
                     } else if (StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL == dealsCountType){
                         return "合计";
                     }
                 }
                 return null;
             }
          }))
          .addConfig(new ColConfig("shenZhenDealsCount","深圳"))
          .addConfig(new ColConfig("beiJingDealsCount","北京"))
          .addConfig(new ColConfig("shangHaiDealsCount","上海"))
          .addConfig(new ColConfig("guangZhouDealsCount","广州"))
          .addConfig(new ColConfig("wuHanDealsCount","武汉"))
          .addConfig(new ColConfig("nanJingDealsCount","南京"))
          .addConfig(new ColConfig("chengDuDealsCount","成都"))
          .addConfig(new ColConfig("xiaMenDealsCount","厦门"))
          .addConfig(new ColConfig("sumDealsCount","合计"));
    }


    public static void initStatisticsSalesmanDetailConfig() {
        statisticsSalesmanDetailConfig.addConfig(new ColConfig("salesmanId", "业务员id" ))
                .addConfig(new ColConfig("salesmanName", "业务员姓名"))
                .addConfig(new ColConfig("rentLengthType", "长租短租", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        if (o != null) {
                            Integer rentLengthType = (Integer) o;
                            if (RentLengthType.RENT_LENGTH_TYPE_SHORT == rentLengthType) {
                                return "短租";
                            } else if (RentLengthType.RENT_LENGTH_TYPE_LONG == rentLengthType) {
                                return "长租";
                            }
                        }
                        return null;
                    }
                }))
                .addConfig(new ColConfig("subCompanyName", "分公司名", 31))
                .addConfig(new ColConfig("dealsCount", "成交单数"))
                .addConfig(new ColConfig("dealsProductCount", "成交台数"))
                .addConfig(new ColConfig("dealsAmount", "成交金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("income", "本期回款（已收）", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("receive", "应收", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("pureIncrease", "净增台数", AmountExcelExportView.getInstance()));
    }


    public static void initStatementOrderDetailConfig() {
        statementOrderDetailConfig.addConfig(new ColConfig("orderType", "类型", new ExcelExportView() {
            @Override
            public Object view(Object o) {
                Integer type = (Integer) o;
                if (OrderType.ORDER_TYPE_ORDER.equals(type)) {
                    return "订单";
                } else if (OrderType.ORDER_TYPE_DEPARTMENT.equals(type)) {
                    return "调配单";
                } else if (OrderType.ORDER_TYPE_CHANGE.equals(type)) {
                    return "换货单";
                } else if (OrderType.ORDER_TYPE_RETURN.equals(type)) {
                    return "退货单";
                } else if (OrderType.ORDER_TYPE_REPAIR.equals(type)) {
                    return "维修单";
                }
                return "";
            }
        }))
                .addConfig(new ColConfig("orderNo", "订单编号"))
                .addConfig(new ColConfig("orderItemType", "订单项类型", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer orderItemType = (Integer) o;
                        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType)) {
                            return "商品";
                        } else if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType)) {
                            return "配件";
                        }
                        return "";
                    }
                }))
                .addConfig(new ColConfig("itemName", "商品名"))
                .addConfig(new ColConfig("unitAmount", "单价", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("itemCount", "商品数量"))
                .addConfig(new ColConfig("itemRentType", "租赁类型", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer itemRentType = (Integer) o;
                        if (itemRentType == null) {
                            return "";
                        } else if (RentLengthType.RENT_LENGTH_TYPE_SHORT == itemRentType) {
                            return "按天租";
                        } else if (RentLengthType.RENT_LENGTH_TYPE_LONG == itemRentType) {
                            return "按月租";
                        }
                        return "";
                    }
                }))
                .addConfig(new ColConfig("statementDetailPhase", "期数"))
                .addConfig(new ColConfig("statementExpectPayTime", "预计支付时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailPaidTime", "结算支付时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailAmount", "结算单金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCouponAmount", "抵扣金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentAmount", "租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentDepositAmount", "租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailDepositAmount", "押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailOtherAmount", "其它费用", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailOverdueAmount", "截止上期未付", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailPaidAmount", "已付金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentPaidAmount", "已付租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentDepositPaidAmount", "已付租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailDepositPaidAmount", "已付押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentDepositReturnAmount", "退还租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailDepositReturnAmount", "退还押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailCorrectAmount", "冲正金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailStatus", "状态", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer type = (Integer) o;
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(type)) {
                            return "未支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(type)) {
                            return "部分支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(type)) {
                            return "支付完成";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(type)) {
                            return "无需支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_CORRECTED.equals(type)) {
                            return "已冲正";
                        }
                        return "";
                    }
                }))
                .addConfig(new ColConfig("statementStartTime", "结算开始时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "结算结束时间", DateExcelExportView.getInstance()));

    }

    public static void initStatementOrderConfig() {
        statementOrderConfig.addConfig(new ColConfig("statementOrderNo", "结算单编号"))
                .addConfig(new ColConfig("statementStatus", "状态", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer type = (Integer) o;
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(type)) {
                            return "未支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(type)) {
                            return "部分支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(type)) {
                            return "支付完成";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(type)) {
                            return "无需支付";
                        } else if (StatementOrderStatus.STATEMENT_ORDER_STATUS_CORRECTED.equals(type)) {
                            return "已冲正";
                        }
                        return "";
                    }
                }))
                .addConfig(new ColConfig("ownerName", "业务员"))
                .addConfig(new ColConfig("customerName", "客户名"))
                .addConfig(new ColConfig("statementExpectPayTime", "预计支付时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementPaidTime", "实际支付时间", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementStartTime", "结算开始日期", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "结算结束日期", DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositAmount", "租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositPaidAmount", "已付租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositReturnAmount", "已退租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositAmount", "设备押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositPaidAmount", "已付设备押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositReturnAmount", "已退设备押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentAmount", "租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentPaidAmount", "已付租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementAmount", "结算单总金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementPaidAmount", "已付总金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOverdueAmount", "逾期金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOtherAmount", "其它费用", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCorrectAmount", "冲正金额", AmountExcelExportView.getInstance()));

    }


    public static void initBankSlipDetailConfig() {
        bankSlipDetailConfig.addConfig(new ColConfig("bankSlipBankType", "银行", new ExcelExportView() {
            @Override
            public Object view(Object o) {
                Integer bankType = (Integer) o;
                if (BankType.ALIPAY.equals(bankType)) {
                    return "支付宝";
                } else if (BankType.BOC_BANK.equals(bankType)) {
                    return "中国银行";
                } else if (BankType.TRAFFIC_BANK.equals(bankType)) {
                    return "交通银行";
                } else if (BankType.NAN_JING_BANK.equals(bankType)) {
                    return "南京银行";
                } else if (BankType.AGRICULTURE_BANK.equals(bankType)) {
                    return "农业银行";
                } else if (BankType.ICBC_BANK.equals(bankType)) {
                    return "工商银行";
                } else if (BankType.CCB_BANK.equals(bankType)) {
                    return "建设银行";
                } else if (BankType.PING_AN_BANK.equals(bankType)) {
                    return "平安银行";
                } else if (BankType.CMBC_BANK.equals(bankType)) {
                    return "招商银行";
                } else if (BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)) {
                    return "浦发银行";
                } else if (BankType.HAN_KOU_BANK.equals(bankType)) {
                    return "汉口银行";
                }else if (BankType.LYCHEE_PAY.equals(bankType)) {
                    return "快付通";
                }else if (BankType.STOCK_CASH.equals(bankType)) {
                    return "现金库存";
                }else if (BankType.SWIFT_PASS.equals(bankType)) {
                    return "威富通";
                }else if (BankType.UNKNOWN_CHANNEL_PAY_TYPE.equals(bankType)) {
                    return "支付未知渠道";
                }else if (BankType.JING_DONG.equals(bankType)) {
                    return "京东";
                }else if (BankType.CHINA_UNION_PAY_TYPE.equals(bankType)) {
                    return "银联";
                }
                return "";
            }
        }))
                .addConfig(new ColConfig("tradeTime", "交易日期", 11, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("payerName", "付款人名称", 31))
                .addConfig(new ColConfig("tradeAmount", "交易金额(元)"))
                .addConfig(new ColConfig("merchantOrderNo", "商户订单号", 39))
                .addConfig(new ColConfig("bankSlipClaimList", "K3客户编码", 39, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        List<BankSlipClaim> bankSlipClaimList = (List<BankSlipClaim>) o;
                        StringBuffer k3CustomerNo = new StringBuffer("");
                        for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                            String dbK3CustomerNo = bankSlipClaim.getK3CustomerNo();
                            k3CustomerNo.append(dbK3CustomerNo + "\r\n");
                        }
                        return k3CustomerNo.toString().trim();
                    }
                }))
                .addConfig(new ColConfig("bankSlipClaimList", "对应公司名称", 39, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        List<BankSlipClaim> bankSlipClaimList = (List<BankSlipClaim>) o;
                        StringBuffer customerName = new StringBuffer("");
                        for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                            String dbCustomerName = bankSlipClaim.getCustomerName();
                            String claimAmount = String.valueOf(AmountExcelExportView.getInstance().view(bankSlipClaim.getClaimAmount()));
                            customerName.append(dbCustomerName + "(" + claimAmount + "元)" + "\r\n");
                        }
                        return customerName.toString().trim();
                    }
                }))
                .addConfig(new ColConfig("customerSubCompanyNameStringList", "客户所属分公司"))
                .addConfig(new ColConfig("ownerSubCompanyName", "数据分公司"))
                .addConfig(new ColConfig("subCompanyName", "客户归属地"))
                .addConfig(new ColConfig("tradeSerialNo", "交易流水号",39));
    }

    public static void initStatementOrderPayDetailConfig() {
        statementOrderPayDetailConfig.addConfig(new ColConfig("orderNo", "订单号", 31))
                .addConfig(new ColConfig("customerName", "客户名称", 31))
                .addConfig(new ColConfig("customerNo", "客户编码", 31))
                .addConfig(new ColConfig("k3CustomerCode", "K3客户编码", 31))
                .addConfig(new ColConfig("customerCompany", "客户分公司", 19))
                .addConfig(new ColConfig("orderCompany", "订单分公司", 19))
                .addConfig(new ColConfig("deliveryCompany", "发货分公司", 19))
                .addConfig(new ColConfig("rentPaidAmount", "租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("depositPaidAmount", "押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("totalPaidAmount", "合计", AmountExcelExportView.getInstance()));
    }

    public static void initStatementOrderCheckConfig() {
        statementOrderCheckConfig.addConfig(new ColConfig("orderNo", "租赁订单号", 10, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("businessType", "业务类型", 6, HSSFColor.WHITE.index, new ExcelExportView() {
            @Override
            public Object view(Object o) {
                Integer businessType = (Integer) o;
                if (OrderType.ORDER_TYPE_ORDER.equals(businessType)) {
                    return "租赁";
                } else if (OrderType.ORDER_TYPE_RETURN.equals(businessType)) {
                    return "退货";
                } else if (CommonConstant.ORDER_TYPE_RELET.equals(businessType)){
                    return "续租";
                } else if (CommonConstant.ORDER_TYPE_RELET_RETURN.equals(businessType)){
                    return "续租退货";
                }
                return null;
            }
        }))
                .addConfig(new ColConfig("orderType", " 订单项类型", 6, HSSFColor.WHITE.index, new ExcelExportView() {

                    @Override
                    public Object view(Object o) {
                        Integer orderType = (Integer) o;
                        if(OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderType)){
                            return "商品";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderType)){
                            return "物料";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT.equals(orderType)){
                            return "换货商品";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL.equals(orderType)){
                            return "换货物料";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(orderType)){
                            return "退货商品";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(orderType)){
                            return "退货物料";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.equals(orderType)){
                            return "退货其他";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER.equals(orderType)){
                            return "换货其他";
                        }else if(OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(orderType)){
                            return "其他";
                        }
                        return "";
                    }
                }))
                .addConfig(new ColConfig("itemName", "商品名", 16, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("itemSkuName", "配置", 23, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("isNew", "新旧程度", 6, HSSFColor.WHITE.index, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer isNew = (Integer) o;
                        if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)){
                            return "次新";
                        }else if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)){
                            return "全新";
                        }
                        return "";
                    }
                }))
                //-------------------以下是全部结算单-----------------------------
                .addConfig(new ColConfig("rentStartTime", "租赁开始日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("expectReturnTime", "租赁结束日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("month", "月", 3))
                .addConfig(new ColConfig("day", "日", 3))
                .addConfig(new ColConfig("allRentTimeLength", "租赁总期限", 8))
                .addConfig(new ColConfig("allPeriodStartAndEnd", "租赁期限", 15, HSSFColor.WHITE.index))
                //-------------------以上是全部结算单-----------------------------

                //-------------------以下是本期结算单-----------------------------
                .addConfig(new ColConfig("statementStartTime", "本期开始日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "本期结束日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementMonth", "月", 3))
                .addConfig(new ColConfig("statementDay", "日", 3))
                .addConfig(new ColConfig("rentTimeLength", "租赁期限", 6))
                .addConfig(new ColConfig("currentPeriodStartAndEnd", "本期费用起止", 12,HSSFColor.LEMON_CHIFFON.index))

                .addConfig(new ColConfig("payMode", "支付方式", 7, HSSFColor.LEMON_CHIFFON.index, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer payMode = (Integer) o;
                        if(OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)){
                            return "先付后用";
                        }else if(OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)){
                            return "先用后付";
                        }else if(OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(payMode)){
                            return "首付30%";
                        }
                        return "";
                    }
                }))
                //-------------------以下是全部结算单-----------------------------
                .addConfig(new ColConfig("rentProgramme", "租赁方案", 7, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("itemCount", "租赁数量", 4, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("unitAmountInfo", "单价\n（元/台）", 10, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("statementDetailRentEndAmount", "本期租金", 6, HSSFColor.TAN.index, AmountExcelExportView.getInstance()))
//                .addConfig(new ColConfig("statementRentDepositAmount", " 租金押金", 6, HSSFColor.WHITE.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositAmount", " 押金", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
//                .addConfig(new ColConfig("statementDepositPaidAmount", " 支付押金", 8, HSSFColor.WHITE.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOverdueAmount", " 逾期金额", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOtherAmount", " 其它费用", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCorrectAmount", " 调整金额", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailEndAmount", " 本期应付金额", 10, HSSFColor.TAN.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementExpectPayEndTime", " 本期应付日期", 8, HSSFColor.TAN.index,  DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementAmount", " 本月应付金额", 10, HSSFColor.GOLD.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailStatus", " 状态", 6, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Integer statementDetailStatus = (Integer) o;
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

                        return "";
                    }
                })).addConfig(new ColConfig("k3ReturnOrderDONo", " 退换订单号", 9))
                .addConfig(new ColConfig("returnReasonType", " 退换货原因", 9))
                .addConfig(new ColConfig("statementCorrectNo", " 冲正订单号", 9))
                .addConfig(new ColConfig("statementCorrectReason", " 冲正原因", 9));
    }


    public static void initStatementOrderCheckConfigNew() {
        statementOrderCheckConfigNew
                .addConfig(new ColConfig("orderNo", "租赁订单号", 10, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("businessType", "业务类型", 6, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("orderItemType", " 订单项类型", 6, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("itemName", "商品名", 16, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("itemSkuName", "配置", 23, HSSFColor.WHITE.index))
                .addConfig(new ColConfig("isNew", "新旧程度", 6, HSSFColor.WHITE.index))
                //-------------------以下是全部结算单-----------------------------
                .addConfig(new ColConfig("rentStartTime", "租赁开始日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("expectReturnTime", "租赁结束日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("month", "月", 3))
                .addConfig(new ColConfig("day", "日", 3))
                .addConfig(new ColConfig("allRentTimeLength", "租赁总期限", 8))
                .addConfig(new ColConfig("allPeriodStartAndEnd", "租赁期限", 15, HSSFColor.WHITE.index))
                //-------------------以上是全部结算单-----------------------------

                //-------------------以下是本期结算单-----------------------------
                .addConfig(new ColConfig("statementStartTime", "本期开始日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "本期结束日期", 10, DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementMonth", "月", 3))
                .addConfig(new ColConfig("statementDay", "日", 3))
                .addConfig(new ColConfig("rentTimeLength", "租赁期限", 6))
                .addConfig(new ColConfig("currentPeriodStartAndEnd", "本期费用起止", 12,HSSFColor.LEMON_CHIFFON.index))

                .addConfig(new ColConfig("payMode", "支付方式", 7, HSSFColor.LEMON_CHIFFON.index))
                //-------------------以下是全部结算单-----------------------------
                .addConfig(new ColConfig("rentProgramme", "租赁方案", 7, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("itemCount", "租赁数量", 4, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("unitAmountInfo", "单价\n（元/台）", 10, HSSFColor.LEMON_CHIFFON.index))
                .addConfig(new ColConfig("statementDetailRentAmount", "本期租金", 6, HSSFColor.TAN.index, AmountExcelExportView.getInstance()))
//                .addConfig(new ColConfig("statementRentDepositAmount", " 租金押金", 6, HSSFColor.WHITE.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositAmount", " 押金", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
//                .addConfig(new ColConfig("statementDepositPaidAmount", " 支付押金", 8, HSSFColor.WHITE.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOverdueAmount", " 逾期金额", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOtherAmount", " 其它费用", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCorrectAmount", " 调整金额", 6, HSSFColor.LEMON_CHIFFON.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailAmount", " 本期应付金额", 10, HSSFColor.TAN.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementExpectPayTime", " 本期应付日期", 8, HSSFColor.TAN.index,  DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("monthPayableAmount", " 本月应付金额", 10, HSSFColor.GOLD.index,  AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailStatus", " 状态", 6))
                .addConfig(new ColConfig("k3ReturnOrderDONo", " 退换订单号", 9))
                .addConfig(new ColConfig("returnReasonType", " 退换货原因", 9))
                .addConfig(new ColConfig("statementCorrectNo", " 冲正订单号", 9))
                .addConfig(new ColConfig("statementCorrectReason", " 冲正原因", 9));
    }
}
