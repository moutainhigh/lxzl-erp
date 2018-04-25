package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.constant.BankType;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 16:02
 */

public class ExcelExportConfigGroup {

    public static ExcelExportConfig bankSlipDetailConfig = new ExcelExportConfig();
    public static ExcelExportConfig bankSlipConfig = new ExcelExportConfig();

    static {
        initBankSlipDetailConfig();
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
                .addConfig(new ColConfig("tradeTime", "交易日期", new ExcelExportView() {
                    @Override
                    public Object view(Object o) {
                        Date tradeTime = new Date(o.toString());

                        String parse = new SimpleDateFormat("yyyy-MM-dd").format(tradeTime);
                        return parse;

                    }
                }))
                .addConfig(new ColConfig("payerName", "付款人名称"))
                .addConfig(new ColConfig("tradeAmount", "交易金额"))
                .addConfig(new ColConfig("merchantOrderNo", "商户订单号"))
                .addConfig(new ColConfig("bankSlipClaimList", "K3客户编码",new ExcelExportView(){
                    @Override
                    public Object view(Object o) {
                        List<BankSlipClaim> bankSlipClaimList = (List<BankSlipClaim>) o;
                        String k3CustomerNo = "";
                        for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                            String dbK3CustomerNo = bankSlipClaim.getK3CustomerNo();
                            k3CustomerNo = k3CustomerNo +"\r\n" + dbK3CustomerNo;
                        }
                        return k3CustomerNo;
                    }
                }))
                .addConfig(new ColConfig("payerName", "对应公司名称"))
                .addConfig(new ColConfig("subCompanyName", "客户归属地"))
                .addConfig(new ColConfig("tradeSerialNo", "交易流水号"))
        ;
    }

}
