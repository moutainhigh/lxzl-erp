package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;

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

    static {
        initBankSlipDetailConfig();
        initStatementOrderConfig();
        initStatementOrderDetailConfig();
        initStatisticsSalesmanDetailConfig();
    }


    public static void initStatisticsSalesmanDetailConfig() {
        statisticsSalesmanDetailConfig.addConfig(new ColConfig("salesmanId","业务员id",8000))
                .addConfig(new ColConfig("salesmanName","业务员姓名",8000))
                .addConfig(new ColConfig("rentLengthType", "长租短租", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        if(o != null){
                            Integer rentLengthType = (Integer) o;
                            if(RentLengthType.RENT_LENGTH_TYPE_SHORT == rentLengthType){
                                return "短租";
                            }else if(RentLengthType.RENT_LENGTH_TYPE_LONG == rentLengthType){
                                return "长租";
                            }
                        }
                        return null;
                    }
                }))
                .addConfig(new ColConfig("subCompanyName","分公司名",8000))
                .addConfig(new ColConfig("dealsCount","成交单数"))
                .addConfig(new ColConfig("dealsProductCount","成交台数"))
                .addConfig(new ColConfig("dealsAmount","成交金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("income","本期回款（已收）", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("receive", "应收", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("pureIncrease","净增台数", AmountExcelExportView.getInstance()));
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
                .addConfig(new ColConfig("unitAmount", "单价",AmountExcelExportView.getInstance()))
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
                .addConfig(new ColConfig("statementExpectPayTime", "预计支付时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailPaidTime", "结算支付时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailAmount", "结算单金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCouponAmount", "抵扣金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentAmount", "租金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentDepositAmount", "租金押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailDepositAmount", "押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailOtherAmount", "其它费用", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailOverdueAmount", "逾期金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailPaidAmount", "已付金额", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentPaidAmount", "已付租金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDetailRentDepositPaidAmount", "已付租金押金",AmountExcelExportView.getInstance()))
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
                .addConfig(new ColConfig("statementStartTime", "结算开始时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "结算结束时间",DateExcelExportView.getInstance()));

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
                .addConfig(new ColConfig("statementExpectPayTime", "预计支付时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementPaidTime", "实际支付时间",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementStartTime", "结算开始日期",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementEndTime", "结算结束日期",DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositAmount", "租金押金", AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositPaidAmount", "已付租金押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentDepositReturnAmount", "已退租金押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositAmount", "设备押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositPaidAmount", "已付设备押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementDepositReturnAmount", "已退设备押金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentAmount", "租金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementRentPaidAmount", "已付租金",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementAmount", "结算单总金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementPaidAmount", "已付总金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOverdueAmount", "逾期金额",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementOtherAmount", "其它费用",AmountExcelExportView.getInstance()))
                .addConfig(new ColConfig("statementCorrectAmount", "冲正金额",AmountExcelExportView.getInstance()));

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
                }
                return "";
            }
        }))
                .addConfig(new ColConfig("tradeTime", "交易日期",5000,DateExcelExportView.getInstance()))
                .addConfig(new ColConfig("payerName", "付款人名称",8000))
                .addConfig(new ColConfig("tradeAmount", "交易金额(元)"))
                .addConfig(new ColConfig("merchantOrderNo", "商户订单号", 10000))
                .addConfig(new ColConfig("bankSlipClaimList", "K3客户编码",10000, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        List<BankSlipClaim> bankSlipClaimList = (List<BankSlipClaim>) o;
                        StringBuffer k3CustomerNo = new StringBuffer("");
                        for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                            String dbK3CustomerNo = bankSlipClaim.getK3CustomerNo();
                            k3CustomerNo.append(dbK3CustomerNo + "\r\n");
                        }
                        return k3CustomerNo;
                    }
                }))
                .addConfig(new ColConfig("bankSlipClaimList", "对应公司名称",10000, new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        List<BankSlipClaim> bankSlipClaimList = (List<BankSlipClaim>) o;
                        StringBuffer customerName = new StringBuffer("");
                        for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                            String dbCustomerName = bankSlipClaim.getCustomerName();
                            String claimAmount = String.valueOf(AmountExcelExportView.getInstance().view(bankSlipClaim.getClaimAmount()));
                            customerName.append(dbCustomerName + "("+claimAmount+"元)"+"\r\n");
                        }
                        return customerName;
                    }
                }))
                .addConfig(new ColConfig("subCompanyName", "客户归属地"))
                .addConfig(new ColConfig("tradeSerialNo", "交易流水号"));
    }


}
