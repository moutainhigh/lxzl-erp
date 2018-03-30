package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.BankType;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.ClaimParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @Author: Pengbinjie
 * @Description：
 * @Date: Created in 16:04 2018/3/21
 * @Modified By:
 */
public class BankSlipControllerTest extends ERPTransactionalTest {
    @Test
    public void verifyBankSlipDetail() throws Exception {


        BankSlip bankSlip = new BankSlip();
        bankSlip.setBankSlipId(144);
        TestResult result = getJsonTestResult("/bankSlip/confirmBankSlip", bankSlip);
    }

    @Test
    public void claimBankSlipDetail() throws Exception {

        BankSlipClaim bankSlipClaim = new BankSlipClaim();
        bankSlipClaim.setBankSlipDetailId(1323);
        ArrayList<ClaimParam> list = new ArrayList<>();
        ClaimParam claimParam =  new ClaimParam();
        claimParam.setClaimAmount(new BigDecimal(100));
        claimParam.setCustomerNo("LXCC-1000-20180328-00825");
        ClaimParam claimParam1 =  new ClaimParam();
        claimParam1.setClaimAmount(new BigDecimal(1700));
        claimParam1.setCustomerNo("LXCC-027-20180323-00790");
        list.add(claimParam);
//        list.add(claimParam1);
        bankSlipClaim.setClaimParam(list);
        TestResult result = getJsonTestResult("/bankSlip/claimBankSlipDetail", bankSlipClaim);
    }

    @Test
    public void pushDownBankSlip() throws Exception {
        BankSlip bankSlip = new BankSlip();
        bankSlip.setBankSlipId(149);
        TestResult result = getJsonTestResult("/bankSlip/pushDownBankSlip", bankSlip);
    }

    @Test
    public void ignoreBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(8903);
        TestResult result = getJsonTestResult("/bankSlip/ignoreBankSlipDetail", bankSlipDetail);
    }

    @Test
    public void pageBankSlip() throws Exception {
        BankSlipQueryParam bankSlipQueryParam = new BankSlipQueryParam();
        bankSlipQueryParam.setPageNo(1);
        bankSlipQueryParam.setPageSize(10);
//        bankSlipQueryParam.setBankType();
//        bankSlipQueryParam.setSlipMonth();
//        bankSlipQueryParam.setSlipStatus();
//        bankSlipQueryParam.setSubCompanyName("南京分公司");
        bankSlipQueryParam.setSubCompanyId(2);

        TestResult result = getJsonTestResult("/bankSlip/pageBankSlip", bankSlipQueryParam);
    }

    @Test
    public void pageBankSlip2Json() throws Exception {
        String json = "{\"pageNo\": 1, \"pageSize\": 15, \"bankType\": \"\", \"slipMonth\": \"\", \"slipStatus\": \"\", \"subCompanyId\": \"2\"}";
        BankSlipQueryParam bankSlipQueryParam = JSONUtil.convertJSONToBean(json, BankSlipQueryParam.class);
        TestResult result = getJsonTestResult("/bankSlip/pageBankSlip", bankSlipQueryParam);
    }

    @Test
    public void pageBankSlipDetail() throws Exception {
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
        bankSlipDetailQueryParam.setPageNo(1);
        bankSlipDetailQueryParam.setPageSize(20);
        bankSlipDetailQueryParam.setBankSlipId(1);
//        bankSlipQueryParam.setBankType();
//        bankSlipQueryParam.setSlipMonth();
//        bankSlipQueryParam.setSlipStatus();
//        bankSlipQueryParam.setSubCompanyName();

        TestResult result = getJsonTestResult("/bankSlip/pageBankSlipDetail", bankSlipDetailQueryParam);
    }

    @Test
    public void pageBankSlipDetailJson() throws Exception {
        String json = "{\"pageNo\":1,\"pageSize\":15,\"payerName\":\"\"}";
        BankSlipDetailQueryParam bankSlipDetailQueryParam = JSON.parseObject(json, BankSlipDetailQueryParam.class);
        TestResult result = getJsonTestResult("/bankSlip/pageBankSlipDetail", bankSlipDetailQueryParam);
    }


    @Test
    public void importBankSlip() throws Exception {
        //北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("北京分公司");
//        bankSlip.setBankType(BankType.BOC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqwr7uAQdKMAAQ8AHewM0Y003.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

        //测试北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.BOC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/10/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/23/wKgKyFq7nuuAdZVqAAA8N2O0BRI91.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //北京(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("北京分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqwr7uAQdKMAAQ8AHewM0Y003.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);


        //成都(交通银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("成都分公司");
//        bankSlip.setBankType(BankType.TRAFFIC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqw8o2AZ9bYAAEyALLhSg8377.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

        //成都(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("成都分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/01"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqw8o2AZ9bYAAEyALLhSg8377.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

        //广州(交通银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("广州分公司");
//        bankSlip.setSubCompanyId(5);
//        bankSlip.setBankType(BankType.TRAFFIC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/1F/wKgKyFqxAiGAMfJ_AAK-AFt5wrg351.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //广州(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("广州分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxAiGAMfJ_AAK-AFt5wrg351.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

//-----------------------------------------------------------
////        //南京(南京银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("南京分公司");
//        bankSlip.setSubCompanyId(6);
//        bankSlip.setBankType(BankType.NAN_JING_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/04/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/1F/wKgKyFqxCkKAGD1gAABNGc74JNo67.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //        //南京(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("南京分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/02"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxCkKAGD1gAABNGc74JNo67.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

//        //厦门(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("厦门分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/01"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxFTiAeyRxAABXgStoR6w55.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

        //        //厦门(农业
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("厦门分公司");
//        bankSlip.setBankType(BankType.AGRICULTURE_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxFTiAeyRxAABXgStoR6w55.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);
         //厦门(农业
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("厦门分公司");
//        bankSlip.setSubCompanyId(7);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2017/12/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/24/wKgKyFq9jsmAY58TAABXgStoR6w68.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);
//
// 上海分公司 工商银行
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("上海分公司");
//        bankSlip.setBankType(BankType.ICBC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxuq6ASfjoAAFNLCdPqtQ69.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);


// 上海分公司 中国建设银行
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("上海分公司");
//        bankSlip.setBankType(BankType.CCB_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxuq6ASfjoAAFNLCdPqtQ69.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);


//        // 上海分公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("上海分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxuq6ASfjoAAFNLCdPqtQ69.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

//        深圳分公司
        // 深圳分公司 平安银行
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.PING_AN_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/04/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/20/wKgKyFqxw3SAFbc2AAN8APwuql8246.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


        // 深圳分公司 招商银行
        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("深圳分公司");
        bankSlip.setBankType(BankType.CMBC_BANK);
        bankSlip.setSubCompanyId(2);
        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
        bankSlip.setExcelUrl("/group1/M00/00/20/wKgKyFqxw3SAFbc2AAN8APwuql8246.xls");
        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);
//

        // 深圳分公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw3SAFbc2AAN8APwuql8246.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

        // 深圳总公司
// 总公司(浦发银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/20/wKgKyFqxw-GAOZdvAAKWwZifgiU47.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

//        String json = "{\n" +
//                "            \"bankType\": 2,\n" +
//                "            \"excelUrl\": \"/group1/M00/00/21/wKgKyFq4ntOAJstnAAQ8ABSUHr4115.xls\",\n" +
//                "            \"slipMonth\": \"2018-03-08\",\n" +
//                "            \"subCompanyId\": 1,\n" +
//                "        }";
//        BankSlip bankSlip = JSON.parseObject(json, BankSlip.class);
//        TestResult result = getJsonTestResult("/bankSlip/importExcel", bankSlip);

//
// 总公司(招商银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("总公司");
//        bankSlip.setBankType(BankType.CMBC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw-GAOZdvAAKWwZifgiU47.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);
//
// 总公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("总公司");
//        bankSlip.setBankType(1);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw-GAOZdvAAKWwZifgiU47.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);


    }

}