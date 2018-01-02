package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.ManualDeductParam;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setUnionUser(500006);

        List<CustomerCompanyNeed> customerCompanyNeedFirstList= new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed = new CustomerCompanyNeed();
        customerCompanyNeed.setSkuId(70);
        customerCompanyNeed.setUnitPrice(new BigDecimal(1560));
        customerCompanyNeed.setRentCount(10);
        customerCompanyNeed.setRentLength(24);
        customerCompanyNeedFirstList.add(customerCompanyNeed);

        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCustomerOrigin(1);
        customerCompany.setCompanyName("测试公司名");
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324590");
        customerCompany.setAddress("企业信息详细地址测试");
        customerCompany.setProductPurpose("测试设备用途");
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);

/*        Image image1 = new Image();
//        image1.setImgId(18);
//        customerCompany.setBusinessLicensePictureImage(image1);

        Image image2 = new Image();
//        image2.setImgId(16);
//        customerCompany.setLegalPersonNoPictureFrontImage(image2);

        Image image3 = new Image();
        image3.setImgId(17);
        customerCompany.setLegalPersonNoPictureBackImage(image3);

        //加入经营场所租赁合同
        List<Image> managerPlaceRentContractImageList = new ArrayList<>();
        Image image4= new Image();
        image4.setImgId(19);
        Image image5= new Image();
        image5.setImgId(20);
        Image image6= new Image();
        image6.setImgId(21);
        managerPlaceRentContractImageList.add(image4);
        managerPlaceRentContractImageList.add(image5);
        managerPlaceRentContractImageList.add(image6);
        customerCompany.setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);*/

        customer.setCustomerCompany(customerCompany);
        TestResult result = getJsonTestResult("/customer/addCompany", customer);
    }

    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("422827999857463210");
        customerPerson.setPersonName("测试个人用户名");
        customerPerson.setConnectRealName("测试个人用户紧急联系人姓名");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("叶良辰测试名");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/addPerson", customer);
    }

    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010100005");
        CustomerCompany customerCompany = new CustomerCompany();

        //首次所需设备
        List<CustomerCompanyNeed> customerCompanyNeedFirstList= new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed = new CustomerCompanyNeed();
        customerCompanyNeed.setSkuId(71);
        customerCompanyNeed.setUnitPrice(new BigDecimal(1560));
        customerCompanyNeed.setRentCount(15);
        customerCompanyNeed.setRentLength(24);
        customerCompanyNeedFirstList.add(customerCompanyNeed);

        customerCompany.setCustomerOrigin(1);
        customerCompany.setCompanyName("测试公司名");
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324578");
        customerCompany.setAddress("企业信息详细地址测试");
        customerCompany.setProductPurpose("测试设备用途");
        //加入首次租赁设备
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);


        Image image1 = new Image();
        image1.setImgId(28);
        customerCompany.setBusinessLicensePictureImage(image1);

        Image image2 = new Image();
        image2.setImgId(26);
        customerCompany.setLegalPersonNoPictureFrontImage(image2);

        Image image3 = new Image();
        image3.setImgId(27);
        customerCompany.setLegalPersonNoPictureBackImage(image3);
        customer.setCustomerCompany(customerCompany);

        //加入经营场所租赁合同
        List<Image> managerPlaceRentContractImageList = new ArrayList<>();
        Image image4= new Image();
        image4.setImgId(19);
        Image image5= new Image();
        image5.setImgId(20);
        Image image6= new Image();
        image6.setImgId(21);
        managerPlaceRentContractImageList.add(image4);
        managerPlaceRentContractImageList.add(image5);
        managerPlaceRentContractImageList.add(image6);
        customerCompany.setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);


        TestResult result = getJsonTestResult("/customer/updateCompany", customer);
    }

    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCD10002017123000030");
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("422827999857463210");
        customerPerson.setPersonName("update测试个人用户名");
        customerPerson.setConnectRealName("update测试个人用户紧急联系人姓名");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("叶良辰测试名update");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("update个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/updatePerson", customer);
    }

    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCustomerNo("LXCC10002018010100005");
//        customerCompanyQueryParam.setCompanyName("百");
//        customerCompanyQueryParam.setProductPurpose("测试");

        TestResult result = getJsonTestResult("/customer/pageCustomerCompany", customerCompanyQueryParam);
    }

    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
//        customerPersonQueryParam.setPageNo(1);
//        customerPersonQueryParam.setPageSize(40);
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson", customerPersonQueryParam);
    }

    @Test
    public void detailCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010100009");
        TestResult result = getJsonTestResult("/customer/detailCustomerCompany", customer);
    }

    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCD10002018010100007");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson", customer);
    }

    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("CC201711301106206721011");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setRemark("这是一个备注");
        TestResult result = getJsonTestResult("/customer/updateRisk", customerRiskManagement);
    }

    @Test
    public void updateRiskJSON() throws Exception {
        String jsonStr = "{\"creditAmount\":\"1000000\",\"depositCycle\":\"10\",\"paymentCycle\":\"10\",\"payMode\":\"1\",\"appleDepositCycle\":\"10\",\"applePaymentCycle\":\"10\",\"applePayMode\":\"1\",\"newDepositCycle\":\"10\",\"newPaymentCycle\":\"10\",\"newPayMode\":\"1\",\"remark\":\"这是一个优质客户, 可以考虑提高授信额度\",\"customerNo\":\"CP201712060843154191841\"}";
        TestResult result = getJsonTestResult("/customer/updateRisk", JSONUtil.convertJSONToBean(jsonStr, CustomerRiskManagement.class));
    }

    @Test
    public void addCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo("CP01711221151519901204");
        customerConsignInfo.setConsigneeName("add测试联系人6677");
        customerConsignInfo.setConsigneePhone("13566253480");
        customerConsignInfo.setProvince(9);
        customerConsignInfo.setCity(90);
        customerConsignInfo.setDistrict(901);
        customerConsignInfo.setAddress("测试地址6677");
        customerConsignInfo.setRemark("测试增加");
        customerConsignInfo.setIsMain(0);

        TestResult result = getJsonTestResult("/customer/addCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void updateCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(29);
        customerConsignInfo.setConsigneeName("update测试联系人26622");
        customerConsignInfo.setConsigneePhone("13566253478");
        customerConsignInfo.setProvince(29);
        customerConsignInfo.setCity(172); //武汉市
        customerConsignInfo.setDistrict(1685); //汉阳区
        customerConsignInfo.setAddress("修改后的测试地址222");
        customerConsignInfo.setRemark("update备注");
        customerConsignInfo.setIsMain(0);

        TestResult result = getJsonTestResult("/customer/updateCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void deleteCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(18);
        customerConsignInfo.setIsMain(1);

        TestResult result = getJsonTestResult("/customer/deleteCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void detailCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(19);


        TestResult result = getJsonTestResult("/customer/detailCustomerConsignInfo", customerConsignInfo);
    }


    @Test
    public void pageCustomerConsignInfo() throws Exception {
        CustomerConsignInfoQueryParam customerConsignInfoQueryParam = new CustomerConsignInfoQueryParam();
        customerConsignInfoQueryParam.setCustomerNo("C201711152010206581143");
//        customerConsignInfoQueryParam.setPageNo(1);
//        customerConsignInfoQueryParam.setPageSize(3);

        TestResult result = getJsonTestResult("/customer/pageCustomerConsignInfo", customerConsignInfoQueryParam);
    }

    @Test
    public void updateAddressIsMain() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(9);

        TestResult result = getJsonTestResult("/customer/updateAddressIsMain", customerConsignInfo);
    }

    @Test
    public void updateLastUseTime() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
//        customerConsignInfo.setCustomerConsignInfoId(7);

        TestResult result = getJsonTestResult("/customer/updateLastUseTime", customerConsignInfo);
    }

    @Test
    public void queryAccount() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("500008");

        TestResult result = getJsonTestResult("/payment/queryAccount", customer);
    }

    @Test
    public void manualCharge() throws Exception {
        ManualChargeParam customer = new ManualChargeParam();
        customer.setBusinessCustomerNo("CC201712222002354621424");
        customer.setChargeAmount(new BigDecimal(11200.00));
        customer.setChargeRemark("测试手动加款1500元");
        TestResult result = getJsonTestResult("/payment/manualCharge", customer);
    }

    @Test
    public void manualDeduct() throws Exception {
        ManualDeductParam customer = new ManualDeductParam();
        customer.setBusinessCustomerNo("CC201712091546467081096");
        customer.setDeductAmount(new BigDecimal(1200));
        customer.setDeductRemark("测试手动扣款款1200元");
        TestResult result = getJsonTestResult("/payment/manualDeduct", customer);
    }

    @Test
    public void manualDeductJSON() throws Exception {
        String str = "{\n" +
                "\t\"businessCustomerNo\": \"CC201712261408524351576\",\n" +
                "\t\"deductAmount\": \"100\",\n" +
                "\t\"deductRemark\": \"备注\"\n" +
                "}";
        ManualDeductParam customer = JSONUtil.convertJSONToBean(str , ManualDeductParam.class);
        TestResult result = getJsonTestResult("/payment/manualDeduct", customer);
    }
}