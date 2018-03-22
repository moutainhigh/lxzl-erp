package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.BankType;
import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.core.service.system.ImageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/20
 * @Time : Created in 14:03
 */
public class BankSlipControllerTest extends ERPTransactionalTest {

    @Autowired
    private ImageService imageService;

    @Test
    public void importBankSlip() throws Exception {
        //北京(中国银行)
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("北京分公司");
//        bankSlip.setBankType(BankType.BOC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqwr7uAQdKMAAQ8AHewM0Y003.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

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
//        bankSlip.setSubCompanyName("广州分公司");
//        bankSlip.setBankType(BankType.TRAFFIC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxAiGAMfJ_AAK-AFt5wrg351.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

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
//        bankSlip.setSubCompanyName("南京分公司");
//        bankSlip.setBankType(BankType.NAN_JING_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/1F/wKgKyFqxCkKAGD1gAABNGc74JNo67.xlsx");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);

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
//        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setBankType(BankType.PING_AN_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw3SAFbc2AAN8APwuql8246.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);


                // 深圳分公司 招商银行
//        BankSlip bankSlip = new BankSlip();
//        bankSlip.setSubCompanyName("深圳分公司");
//        bankSlip.setBankType(BankType.CMBC_BANK);
//        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
//        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw3SAFbc2AAN8APwuql8246.xls");
//        TestResult result = getJsonTestResult("/import/bankSlip",bankSlip);
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
        BankSlip bankSlip = new BankSlip();
        bankSlip.setSubCompanyName("总公司");
        bankSlip.setBankType(BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK);
        bankSlip.setSlipMonth(new SimpleDateFormat("yyyy/MM/dd").parse("2018/03/20"));
        bankSlip.setExcelUrl(ConstantConfig.imageDomain+"/group1/M00/00/20/wKgKyFqxw-GAOZdvAAKWwZifgiU47.xlsx");
        TestResult result = getJsonTestResult("/bankSlip/importExcel",bankSlip);
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