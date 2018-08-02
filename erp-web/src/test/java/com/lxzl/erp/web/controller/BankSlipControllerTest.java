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
import java.util.Date;
import java.util.List;

/**
 * @Author: Pengbinjie
 * @Description：
 * @Date: Created in 16:04 2018/3/21
 * @Modified By:
 */
public class BankSlipControllerTest extends ERPUnTransactionalTest {
    @Test
    public void confirmSingleBankSlip() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(676);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/confirmBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void cancelLocalizationBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail =  new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(12);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/cancelLocalizationBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void localizationBankSlipDetail() throws Exception {
        BankSlip bankSlip =  new BankSlip();
        bankSlip.setLocalizationSubCompanyId(8);

        List<BankSlipDetail> list = new ArrayList<>();

        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(3);
        list.add(bankSlipDetail);
        bankSlip.setBankSlipDetailList(list);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/localizationBankSlipDetail",bankSlip);
    }

    @Test
    public void confirmSingleBankSlipJson() throws Exception {
        String str = "{\"bankSlipDetailId\":9}";
        BankSlipDetail bankSlipDetail = JSONUtil.convertJSONToBean(str, BankSlipDetail.class);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/confirmBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void queryUnknownBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail =  new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(2);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/queryUnknownBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void pageUnknownBankSlipDetail() throws Exception {
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
        bankSlipDetailQueryParam.setPageNo(1);
        bankSlipDetailQueryParam.setPageSize(15);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/pageUnknownBankSlipDetail",bankSlipDetailQueryParam);
    }

    @Test
    public void unknownBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(1);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/unknownBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void queryBankSlipClaim() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(9);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/queryBankSlipClaim",bankSlipDetail);
    }

    @Test
    public void unAssignBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(1);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/cancelLocalizationBankSlipDetail",bankSlipDetail);
    }

    @Test
    public void assignBankSlipDetail() throws Exception {
        BankSlip bankSlip = new BankSlip();
        bankSlip.setLocalizationSubCompanyId(1);
        List<BankSlipDetail> list = new ArrayList<>();
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(3);
//        bankSlipDetail.setLocalizationSubCompanyId(3);
//        BankSlipDetail bankSlipDetai2 = new BankSlipDetail();
//        bankSlipDetai2.setBankSlipDetailId(18225);
//        bankSlipDetai2.setLocalizationSubCompanyId(3);
        list.add(bankSlipDetail);
//        list.add(bankSlipDetai2);
        bankSlip.setBankSlipDetailList(list);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/localizationBankSlipDetail",bankSlip);
    }

    @Test
    public void assignBankSlipDetail1() throws Exception {
       String str = "{\"bankSlipDetailList\":[{\"bankSlipDetailId\":\"18200\"},{\"bankSlipDetailId\":\"18210\"}],\"localizationSubCompanyId\":3}";
        BankSlip bankSlip = JSONUtil.parseObject(str, BankSlip.class);
        TestResult jsonTestResult = getJsonTestResult("/bankSlip/localizationBankSlipDetail",bankSlip);
    }

    @Test
    public void hideBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail= new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(3);
        TestResult result = getJsonTestResult("/bankSlip/hideBankSlipDetail", bankSlipDetail);
    }

    @Test
    public void displayBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail= new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(3);
        TestResult result = getJsonTestResult("/bankSlip/displayBankSlipDetail", bankSlipDetail);
    }

    @Test
    public void deleteBankSlip() throws Exception {
        BankSlip bankSlip= new BankSlip();
        bankSlip.setBankSlipId(4);
        TestResult result = getJsonTestResult("/bankSlip/deleteBankSlip", bankSlip);
    }

    @Test
    public void queryBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail= new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(2);
        TestResult result = getJsonTestResult("/bankSlip/queryBankSlipDetail", bankSlipDetail);
    }

    @Test
    public void verifyBankSlipDetail() throws Exception {
        BankSlip bankSlip = new BankSlip();
        bankSlip.setBankSlipId(4);
        TestResult result = getJsonTestResult("/bankSlip/confirmBankSlip", bankSlip);
    }

    @Test
    public void claimBankSlipDetail() throws Exception {

        BankSlipClaim bankSlipClaim = new BankSlipClaim();
        bankSlipClaim.setBankSlipDetailId(27);
        ArrayList<ClaimParam> list = new ArrayList<>();
        ClaimParam claimParam =  new ClaimParam();
        claimParam.setClaimAmount(new BigDecimal("300"));
        claimParam.setCustomerNo("LXCC-1000-20180702-00001");
        ClaimParam claimParam1 =  new ClaimParam();
        claimParam1.setClaimAmount(new BigDecimal("200"));
        claimParam1.setCustomerNo("LXCC-2001-20180516-00019");
        ClaimParam claimParam2 =  new ClaimParam();
        claimParam2.setClaimAmount(new BigDecimal(500));
        claimParam2.setCustomerNo("LXCC-2001-20180515-00016");
        list.add(claimParam);
        list.add(claimParam1);
        list.add(claimParam2);
        bankSlipClaim.setClaimParam(list);
        bankSlipClaim.setRemark("aaaaaaaaaaaaaaaaaaa");
        TestResult result = getJsonTestResult("/bankSlip/claimBankSlipDetail", bankSlipClaim);
    }

    @Test
    public void pushDownBankSlip() throws Exception {
        BankSlip bankSlip = new BankSlip();
        bankSlip.setBankSlipId(1);
        TestResult result = getJsonTestResult("/bankSlip/pushDownBankSlip", bankSlip);
    }

    @Test
    public void ignoreBankSlipDetail() throws Exception {
        BankSlipDetail bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipDetailId(7909);
        TestResult result = getJsonTestResult("/bankSlip/ignoreBankSlipDetail", bankSlipDetail);
    }

    @Test
    public void pageBankSlip() throws Exception {
        BankSlipQueryParam bankSlipQueryParam = new BankSlipQueryParam();
        bankSlipQueryParam.setPageNo(1);
        bankSlipQueryParam.setPageSize(10);
        bankSlipQueryParam.setSlipDayStart(new SimpleDateFormat("yyyy-MM-dd").parse("2015-04-22"));
        bankSlipQueryParam.setSlipDayEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2015-04-23"));
//        bankSlipQueryParam.setBankType();
//        bankSlipQueryParam.setSlipMonth();
//        bankSlipQueryParam.setSlipStatus();
//        bankSlipQueryParam.setSubCompanyName("南京分公司");
//        bankSlipQueryParam.setSubCompanyId(5);
//        bankSlipQueryParam.setBankSlipId(2);

        TestResult result = getJsonTestResult("/bankSlip/pageBankSlip", bankSlipQueryParam);
    }
//    @Test
//    public void exportPageBankSlip() throws Exception {
//        BankSlipQueryParam bankSlipQueryParam = new BankSlipQueryParam();
//        bankSlipQueryParam.setPageNo(1);
//        bankSlipQueryParam.setPageSize(10);
////        bankSlipQueryParam.setBankType();
////        bankSlipQueryParam.setSlipMonth();
////        bankSlipQueryParam.setSlipStatus();
////        bankSlipQueryParam.setSubCompanyName("南京分公司");
////        bankSlipQueryParam.setSubCompanyId(5);
////        bankSlipQueryParam.setBankSlipId(167);
//
//        TestResult result = getJsonTestResult("/exportExcel/exportPageBankSlip", bankSlipQueryParam);
//    }

    @Test
    public void pageBankSlip1() throws Exception {
       String str = "{\n" +
               "\t\"bankSlipId\": 159,\n" +
               "\t\"bankType\": \"\",\n" +
               "\t\"pageNo\": 1,\n" +
               "\t\"pageSize\": 15,\n" +
               "\t\"slipMonth\": \"\",\n" +
               "\t\"slipStatus\": \"\",\n" +
               "\t\"subCompanyId\": \"\"\n" +
               "}\n";
        BankSlipQueryParam bankSlipQueryParam = JSONUtil.convertJSONToBean(str,BankSlipQueryParam.class);
        TestResult result = getJsonTestResult("/bankSlip/pageBankSlip", bankSlipQueryParam);
    }

    @Test
    public void exportPageBankSlip() throws Exception {
        BankSlipQueryParam bankSlipQueryParam = new BankSlipQueryParam();
        bankSlipQueryParam.setPageNo(1);
        bankSlipQueryParam.setPageSize(10);
//        bankSlipQueryParam.setBankType();
//        bankSlipQueryParam.setSlipMonth();
//        bankSlipQueryParam.setSlipStatus();
//        bankSlipQueryParam.setSubCompanyName("南京分公司");
//        bankSlipQueryParam.setSubCompanyId(5);
//        bankSlipQueryParam.setBankSlipId(167);

        TestResult result = getJsonTestResult("/exportExcel/exportPageBankSlip", bankSlipQueryParam);
    }

    @Test
    public void pageBankSlip2Json() throws Exception {
        String json = "{\"pageNo\": 1, \"pageSize\": 15, \"bankType\": \"\", \"slipMonth\": \"\", \"slipStatus\": \"\", \"subCompanyId\": \"\"}";
        BankSlipQueryParam bankSlipQueryParam = JSONUtil.convertJSONToBean(json, BankSlipQueryParam.class);
        TestResult result = getJsonTestResult("/exportExcel/exportPageBankSlip", bankSlipQueryParam);
    }

    @Test
    public void pageBankSlipDetail() throws Exception {
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
        bankSlipDetailQueryParam.setPageNo(1);
        bankSlipDetailQueryParam.setPageSize(15);
        bankSlipDetailQueryParam.setSubCompanyId(4);
//        bankSlipDetailQueryParam.setSlipDayStart(new SimpleDateFormat("yyyy-MM-dd").parse("2015-04-22"));
//        bankSlipDetailQueryParam.setSlipDayEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2015-04-23"));
//        bankSlipDetailQueryParam.setBankSlipId(203);
//        bankSlipQueryParam.setBankType();
//        bankSlipQueryParam.setSlipMonth();
//        bankSlipQueryParam.setSlipStatus();
//        bankSlipDetailQueryParam.setPayerName("上海司羽通信设备有限公司");
//        bankSlipDetailQueryParam.setIsLocalization(1);
//        bankSlipDetailQueryParam.setSlipMonth(new SimpleDateFormat("yyyy-MM-dd").parse("2016-04-01"));
        TestResult result = getJsonTestResult("/bankSlip/pageBankSlipDetail", bankSlipDetailQueryParam);
    }

    @Test
    public void pageBankSlipDetailJson() throws Exception {
        String json = "{\"pageNo\":1,\"pageSize\":15,\"loanSign\":1,\"payerName\":\"\",\"bankSlipId\":\"\",\"isLocalization\":\"0\",\"detailStatus\":\"\",\"slipMonth\":null}";
        BankSlipDetailQueryParam bankSlipDetailQueryParam = JSON.parseObject(json, BankSlipDetailQueryParam.class);
        TestResult result = getJsonTestResult("/bankSlip/pageBankSlipDetail", bankSlipDetailQueryParam);
    }


    @Test
    public void importBankSlip() throws Exception {

        //测试京东
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.JING_DONG);
//        bankSlip.setSlipDay(new SimpleDateFormat("yyyy/MM/dd").parse("2018/07/12"));
//        bankSlip.setExcelUrl("group1/M00/00/7B/wKgKyFthll2AL0gYAAAsCKm0Rmk60.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //测试银联
        BankSlip bankSlip = new BankSlip();
        bankSlip.setSubCompanyId(1);
        bankSlip.setBankType(BankType.CHINA_UNION_PAY_TYPE);
        bankSlip.setSlipDay(new SimpleDateFormat("yyyy/MM/dd").parse("2018/07/12"));
        bankSlip.setExcelUrl( "group1/M00/00/7B/wKgKyFtinGKAIhyCAAAlaMeQWXk93.xlsx");
        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //测试北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.BOC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/10/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/23/wKgKyFq7nuuAdZVqAAA8N2O0BRI91.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


////        北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("北京分公司");
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.CCB_BANK);
//        bankSlip.setSlipDay(new SimpleDateFormat("yyyy/MM/dd").parse("2018/07/12"));
//        bankSlip.setExcelUrl("/group1/M00/00/68/wKgKyFtHEr-AHhRkAAAs2UKwAHs10.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //测试北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.BOC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/10/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/23/wKgKyFq7nuuAdZVqAAA8N2O0BRI91.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //北京(支付宝)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("北京分公司");
//        bankSlip.setSubCompanyId(4);
//        bankSlip.setBankType(BankType.HAN_KOU_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2017/01/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNymmAFtceAAHeAPhb5II064.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


        //成都(交通银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("成都分公司");
//        bankSlip.setSubCompanyId(4);
//        bankSlip.setBankType(BankType.TRAFFIC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrMx1-AQ0NeAAEUAA8SXI4949.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


        //成都(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(9);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/01"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrMx1-AQ0NeAAEUAA8SXI4949.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //广州(交通银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("广州分公司");
//        bankSlip.setSubCompanyId(5);
//        bankSlip.setBankType(BankType.TRAFFIC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrMyKOAfor-AAK8ANduVWk391.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //广州(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(5);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrMyKOAfor-AAK8ANduVWk391.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

//-----------------------------------------------------------
////        //南京(南京银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("南京分公司");
//        bankSlip.setSubCompanyId(6);
//        bankSlip.setBankType(BankType.NAN_JING_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2017/04/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNZt2AGkgVAABMrL2vaP027.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //        //南京(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(6);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/02"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNZt2AGkgVAABMrL2vaP027.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

//        //厦门(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(7);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/01/01"));
//        bankSlip.setExcelUrl("group1/M00/00/2C/wKgKyFrNaGKAAf_aAABWPLh25dg46.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        //        //厦门(农业
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("厦门分公司");
//        bankSlip.setSubCompanyId(7);
//        bankSlip.setBankType(BankType.AGRICULTURE_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNaGKAAf_aAABWPLh25dg46.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

//
// 上海分公司 工商银行
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("上海分公司");
//        bankSlip.setSubCompanyId(3);
//        bankSlip.setBankType(BankType.ICBC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2017/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNa_mAWLNiAAFJdGxWZPw99.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


// 上海分公司 中国建设银行
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("上海分公司");
//        bankSlip.setSubCompanyId(3);
//        bankSlip.setBankType(BankType.CCB_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNbfSADkCTAAFJ2uGDdTw30.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


//        // 上海分公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(3);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNbfSADkCTAAFJ2uGDdTw30.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

//        深圳分公司
        // 深圳分公司 平安银行
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.PING_AN_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/04/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNdRGAJbGmAAM8ACVJ5W0996.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);


        // 深圳分公司 招商银行
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setBankType(BankType.CMBC_BANK);
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setRemark("测试是否有备注");
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNdcyAczHlAAN8ABNN5qw255.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);
//

        // 深圳分公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(2);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/04/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNdcyAczHlAAN8ABNN5qw255.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        // 深圳总公司
// 总公司(浦发银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNd3KAQW5EAAKTNiS4Vjo63.xlsx");
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
////        bankSlip.setSubCompanyName("总公司");
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.CMBC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNeMeAR7UmAAKUap6Tf6A10.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);
//
// 总公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyId(1);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNeMeAR7UmAAKUap6Tf6A10.xlsx");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        // 武汉公司(汉口银行)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("总公司");
//        bankSlip.setSubCompanyId(8);
//        bankSlip.setBankType(BankType.HAN_KOU_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNffCAS7kjAAHeAIl1Mfk020.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

        // 武汉公司(支付宝)
//        BankSlip bankSlip = new BankSlip();
////        bankSlip.setSubCompanyName("总公司");
//        bankSlip.setSubCompanyId(8);
//        bankSlip.setBankType(BankType.ALIPAY);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl("/group1/M00/00/2C/wKgKyFrNffCAS7kjAAHeAIl1Mfk020.xls");
//        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);

    }

}
