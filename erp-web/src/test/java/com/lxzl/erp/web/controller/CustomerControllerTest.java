package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CustomerStatus;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.ManualDeductParam;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setOwner(500021);
        customer.setUnionUser(500003);
        customer.setRemark("记住这是客户的备注");
        customer.setIsDefaultConsignAddress(1);

        List<CustomerCompanyNeed> customerCompanyNeedFirstList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed = new CustomerCompanyNeed();
        customerCompanyNeed.setSkuId(70);
        customerCompanyNeed.setUnitPrice(new BigDecimal(1560));
        customerCompanyNeed.setRentCount(10);
        customerCompanyNeed.setRentLength(24);
        customerCompanyNeedFirstList.add(customerCompanyNeed);

        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCustomerOrigin(1);
        customerCompany.setCompanyName("三九文化");
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324590");
        customerCompany.setAddress("彭企业信息详细地址测试");
        customerCompany.setProductPurpose("测试设备用途");
        customerCompany.setRemark("记住这是公司的备注不是客户的备注");
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);
        customerCompany.setIsLegalPersonApple(1);
        customerCompany.setAgentPersonPhone("18566324595");
        customerCompany.setAgentPersonNo("aaaaaaa");
        customerCompany.setAgentPersonName("人民的公义");
        customerCompany.setLegalPersonPhone("12345678900");
        customerCompany.setLegalPersonNo("oooooooo");
        customerCompany.setLegalPerson("woshi lisiaaaaaaa");
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
        Thread.sleep(1000000);
    }

    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        customer.setOwner(500021);
        customer.setIsDefaultConsignAddress(1);

        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("422827999857463210");
        customerPerson.setConnectRealName("赵二天");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("砰砰");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/addPerson", customer);
    }

    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180211-00006");
        customer.setOwner(500025);
//        customer.setUnionUser(500029);
        customer.setIsDefaultConsignAddress(0);
        CustomerCompany customerCompany = new CustomerCompany();

        //首次所需设备
        List<CustomerCompanyNeed> customerCompanyNeedFirstList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed = new CustomerCompanyNeed();
        customerCompanyNeed.setSkuId(208);
        customerCompanyNeed.setUnitPrice(new BigDecimal(1560));
        customerCompanyNeed.setRentCount(15);
        customerCompanyNeed.setRentLength(24);
        customerCompanyNeedFirstList.add(customerCompanyNeed);


        customerCompany.setCustomerOrigin(1);
        customerCompany.setCompanyName("彭测试公司名a123453");
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324578");
        customerCompany.setAddress("企业信息详细地址测试update");
        customerCompany.setProductPurpose("测试设备用途");
        //加入首次租赁设备
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);
        customerCompany.setIsLegalPersonApple(0);
        customerCompany.setLegalPersonPhone("13866253152");
        customerCompany.setAgentPersonPhone("13866253151");
        customerCompany.setAgentPersonName("火狐666");
        customerCompany.setAgentPersonNo("422827199009080013");
//        Image image1 = new Image();
//        image1.setImgId(28);
//        customerCompany.setBusinessLicensePictureImage(image1);
//
//        Image image2 = new Image();
//        image2.setImgId(26);
//        customerCompany.setLegalPersonNoPictureFrontImage(image2);
//
//        Image image3 = new Image();
//        image3.setImgId(27);
//        customerCompany.setLegalPersonNoPictureBackImage(image3);
        customer.setCustomerCompany(customerCompany);

        //加入经营场所租赁合同
        List<Image> managerPlaceRentContractImageList = new ArrayList<>();
        Image image4 = new Image();
        image4.setImgId(40);
        Image image5 = new Image();
        image5.setImgId(41);
        Image image6 = new Image();
        image6.setImgId(42);
        managerPlaceRentContractImageList.add(image4);
        managerPlaceRentContractImageList.add(image5);
        managerPlaceRentContractImageList.add(image6);
//        customerCompany.setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);


        TestResult result = getJsonTestResult("/customer/updateCompany", customer);
    }

    @Test
    public void addCustomerCompanyJSON() throws Exception {
        String str = "{{\n" +
                "\t\"unionUser\": \"500006\",\n" +
                "\t\"owner\": \"500008\",\n" +
                "\t\"firstApplyAmount\": \"1000\",\n" +
                "\t\"laterApplyAmount\": \"10000\",\n" +
                "\t\"customerCompany\": {\n" +
                "\t\t\"customerOrigin\": \"2\",\n" +
                "\t\t\"unionUserName\": \"毛涛\",\n" +
                "\t\t\"unionUser\": \"500006\",\n" +
                "\t\t\"companyName\": \"测试公司名111\",\n" +
                "\t\t\"companyAbb\": \"测试公司名111\",\n" +
                "\t\t\"landline\": \"0755-33806405\",\n" +
                "\t\t\"connectRealName\": \"测试紧急联系人\",\n" +
                "\t\t\"connectPhone\": \"18566324590\",\n" +
                "\t\t\"companyFoundTime\": \"2018-01-09\",\n" +
                "\t\t\"industry\": \"动漫梦工厂\",\n" +
                "\t\t\"affiliatedEnterprise\": \"百度\",\n" +
                "\t\t\"registeredCapital\": \"100000000\",\n" +
                "\t\t\"officeNumber\": \"1000\",\n" +
                "\t\t\"unifiedCreditCode\": \"8888888\",\n" +
                "\t\t\"operatingArea\": \"1000\",\n" +
                "\t\t\"productPurpose\": \"测试设备用途\",\n" +
                "\t\t\"unitInsuredNumber\": \"10\",\n" +
                "\t\t\"ownerName\": \"毛桃a\",\n" +
                "\t\t\"owner\": \"500008\",\n" +
                "\t\t\"firstApplyAmount\": \"1000\",\n" +
                "\t\t\"laterApplyAmount\": \"10000\",\n" +
                "\t\t\"isLegalPersonApple\": \"0\",\n" +
                "\t\t\"province\": \"1\",\n" +
                "\t\t\"city\": \"1\",\n" +
                "\t\t\"district\": \"1\",\n" +
                "\t\t\"address\": \"企业信息详细地址测试\",\n" +
                "\t\t\"legalPerson\": \"高超\",\n" +
                "\t\t\"legalPersonNo\": \"110226198501272116\",\n" +
                "\t\t\"legalPersonPhone\": \"18033402836\",\n" +
                "\t\t\"agentPersonName\": \"黎明\",\n" +
                "\t\t\"agentPersonNo\": \"110226198501272116\",\n" +
                "\t\t\"agentPersonPhone\": \"18033402832\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"businessLicensePictureImage\": {\n" +
                "\t\t\t\"imgId\": 305\n" +
                "\t\t},\n" +
                "\t\t\"legalPersonNoPictureFrontImage\": {\n" +
                "\t\t\t\"imgId\": 306\n" +
                "\t\t},\n" +
                "\t\t\"legalPersonNoPictureBackImage\": {\n" +
                "\t\t\t\"imgId\": 307\n" +
                "\t\t},\n" +
                "\t\t\"legalPersonCreditReportImageList\": [{\n" +
                "\t\t\t\"imgId\": 308\n" +
                "\t\t}],\n" +
                "\t\t\"fixedAssetsProveImageList\": [{\n" +
                "\t\t\t\"imgId\": 309\n" +
                "\t\t}],\n" +
                "\t\t\"publicAccountFlowBillImageList\": [{\n" +
                "\t\t\t\"imgId\": 310\n" +
                "\t\t}],\n" +
                "\t\t\"socialSecurityRoProvidentFundImageList\": [{\n" +
                "\t\t\t\"imgId\": 311\n" +
                "\t\t}],\n" +
                "\t\t\"cooperationAgreementImageList\": [{\n" +
                "\t\t\t\"imgId\": 312\n" +
                "\t\t}],\n" +
                "\t\t\"localeChecklistsImageList\": [{\n" +
                "\t\t\t\"imgId\": 314\n" +
                "\t\t}],\n" +
                "\t\t\"managerPlaceRentContractImageList\": [{\n" +
                "\t\t\t\"imgId\": 313\n" +
                "\t\t}],\n" +
                "\t\t\"otherDateImageList\": []\n" +
                "\t},\n" +
                "\t\"customerNo\": \"LXCC10002018010800035\"\n" +
                "}\n";

        Customer customer = FastJsonUtil.toBean(str, Customer.class);
        TestResult result = getJsonTestResult("/customer/addCompany", customer);
    }

    @Test
    public void updateCustomerCompanyJSON() throws Exception {
        String str = "{\"isDisabled\":\"0\",\"unionUser\":\"500018\",\"customerCompany\":{\"customerOrigin\":\"3\",\"unionUserName\":\"QWER\",\"unionUser\":\"500018\",\"companyName\":\"高超专用测试账户1\",\"companyAbb\":\"测试地址\",\"landline\":\"0755-89741234\",\"connectRealName\":\"陈斯托夫斯基\",\"connectPhone\":\"13112341234\",\"legalPerson\":\"张江\",\"legalPersonNo\":\"230321198606131214\",\"legalPersonPhone\":\"13808088080\",\"companyFoundTime\":\"2018-01-06\",\"industry\":\"\",\"affiliatedEnterprise\":\"\",\"registeredCapital\":\"0\",\"officeNumber\":\"\",\"productPurpose\":\"\",\"agentPersonName\":\"\",\"agentPersonPhone\":\"\",\"agentPersonNo\":\"\",\"unifiedCreditCode\":\"\",\"operatingArea\":\"\",\"unitInsuredNumber\":\"\",\"isDisabled\":\"0\",\"remark\":\"\",\"province\":\"1\",\"city\":\"1\",\"district\":\"1\",\"address\":\"32\",\"businessLicensePictureImage\":{\"imgId\":188},\"legalPersonNoPictureFrontImage\":{\"imgId\":189},\"legalPersonNoPictureBackImage\":{\"imgId\":190},\"legalPersonCreditReportImageList\":[{\"imgId\":191}],\"fixedAssetsProveImageList\":[{\"imgId\":192}],\"publicAccountFlowBillImageList\":[{\"imgId\":193}],\"socialSecurityRoProvidentFundImageList\":[{\"imgId\":194}],\"cooperationAgreementImageList\":[{\"imgId\":195}],\"legalPersonEducationImageList\":[{\"imgId\":196}],\"legalPersonPositionalTitleImageList\":[{\"imgId\":197}],\"localeChecklistsImageList\":[{\"imgId\":200}],\"managerPlaceRentContractImageList\":[{\"imgId\":199}],\"otherDateImageList\":[{\"imgId\":198}]},\"customerNo\":\"LXCC10002018010600026\"}";

        Customer customer = FastJsonUtil.toBean(str, Customer.class);
        TestResult result = getJsonTestResult("/customer/updateCompany", customer);
    }

    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCD-1000-20180127-00058");
        customer.setOwner(500025);
        customer.setIsDefaultConsignAddress(1);
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("422827999857463210");
        customerPerson.setConnectRealName("update测试个人用户紧急联系人姓名");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("叶良辰666");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("update个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/updatePerson", customer);
    }

    @Test
    public void commitCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180129-00060");
        customer.setIsDefaultConsignAddress(1);

        TestResult result = getJsonTestResult("/customer/commitCustomer", customer);
    }

    @Test
    public void verifyCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180212-00084");
        customer.setCustomerStatus(CustomerStatus.STATUS_PASS);
//        customer.setVerifyRemark("资料驳回的备注，请一定要填写好看的数据");
        customer.setVerifyRemark("资料通过的备注，请一定要多多珍惜这个数据");
        customer.setVerifyRemark(null);

        TestResult result = getJsonTestResult("/customer/verifyCustomer", customer);
    }

    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCustomerNo("LXCC10002018010100005");
//        customerCompanyQueryParam.setCompanyName("百");
//        customerCompanyQueryParam.setProductPurpose("测试");
//        customerCompanyQueryParam.setIsDisabled(0);
//        customerCompanyQueryParam.setCustomerStatus(CustomerStatus.STATUS_PASS);
//        customerCompanyQueryParam.setConnectPhone("13726273851");
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany", customerCompanyQueryParam);
    }

    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        customerPersonQueryParam.setPageNo(1);
        customerPersonQueryParam.setPageSize(15);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson", customerPersonQueryParam);
    }

    @Test
    public void testPageCustomerPerson() throws Exception {
        String str = " {\n" +
                "        \"pageNo\": 1,\n" +
                "            \"pageSize\": 15,\n" +
                "            \"realName\": \"\",\n" +
                "            \"phone\": \"\",\n" +
                "            \"customerNo\": \"\"\n" +
                "    }";

        CustomerPersonQueryParam customerPersonQueryParam = FastJsonUtil.toBean(str, CustomerPersonQueryParam.class);
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson", customerPersonQueryParam);
    }


    @Test
    public void detailCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180211-00060");

        TestResult result = getJsonTestResult("/customer/detailCustomerCompany", customer);
    }

    @Test
    public void detailCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180211-00060");
        TestResult result = getJsonTestResult("/customer/detailCustomer", customer);
    }

    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCD10002018010300016");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson", customer);
    }

    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("LXCC-1000-20180126-00054");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setIsFullDeposit(0);
        customerRiskManagement.setIsLimitApple(1);
        customerRiskManagement.setApplePayMode(0);
        customerRiskManagement.setAppleDepositCycle(11);
        customerRiskManagement.setApplePaymentCycle(12);
        customerRiskManagement.setSingleLimitPrice(new BigDecimal(1000));
        customerRiskManagement.setIsLimitNew(1);
        customerRiskManagement.setNewPayMode(0);
        customerRiskManagement.setNewDepositCycle(11);
        customerRiskManagement.setNewPaymentCycle(12);
        customerRiskManagement.setSingleLimitPrice(new BigDecimal(1000));
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
        ManualDeductParam customer = JSONUtil.convertJSONToBean(str, ManualDeductParam.class);
        TestResult result = getJsonTestResult("/payment/manualDeduct", customer);
    }

    @Test
    public void disabledCustomerTest() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010800035");
        TestResult testResult = getJsonTestResult("/customer/disabledCustomer",customer);
    }

    @Test
    public void enableCustomerTest() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010800035");
        TestResult testResult = getJsonTestResult("/customer/enableCustomer",customer);
    }

    @Test
    public void addShortReceivableAmount() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010800035");
        customer.setShortLimitReceivableAmount(new BigDecimal(5000));
        TestResult testResult = getJsonTestResult("/customer/addShortReceivableAmount",customer);
    }

    @Test
    public void addStatementDate() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010800035");
        customer.setStatementDate(20);
        TestResult testResult = getJsonTestResult("/customer/addStatementDate",customer);
    }
}