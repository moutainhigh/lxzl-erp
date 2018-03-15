package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CustomerStatus;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.*;
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
    public void updateOwnerAndUnionUser() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180314-00333");
        customer.setOwner(500032);
        customer.setUnionUser(null);
        TestResult result = getJsonTestResult("/customer/updateOwnerAndUnionUser", customer);
    }

    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setOwner(500021);
//        customer.setUnionUser(500003);
        customer.setRemark("记住这是客户的备注");
        customer.setDeliveryMode(1);
//        customer.setFirstApplyAmount(new BigDecimal(84000));
//        customer.setLaterApplyAmount(new BigDecimal(50000));
//        customer.setIsDefaultConsignAddress(1);

        List<CustomerCompanyNeed> customerCompanyNeedFirstList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed1 = new CustomerCompanyNeed();
        customerCompanyNeed1.setSkuId(70);
        customerCompanyNeed1.setRentCount(10);
        customerCompanyNeed1.setIsNew(0);
        customerCompanyNeed1.setRentType(1);
        customerCompanyNeed1.setRentTimeLength(30);

//        CustomerCompanyNeed customerCompanyNeed2 = new CustomerCompanyNeed();
//        customerCompanyNeed2.setSkuId(57);
//        customerCompanyNeed2.setRentCount(20);
//        customerCompanyNeed2.setTotalPrice(new BigDecimal(4000));
        customerCompanyNeedFirstList.add(customerCompanyNeed1);
//        customerCompanyNeedFirstList.add(customerCompanyNeed2);

        List<CustomerCompanyNeed> customerCompanyNeedLaterList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed3 = new CustomerCompanyNeed();
        customerCompanyNeed3.setSkuId(60);
        customerCompanyNeed3.setRentCount(10);
        customerCompanyNeedLaterList.add(customerCompanyNeed3);

        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCustomerOrigin(1);
        customerCompany.setCompanyName("齐天文化");
        customerCompany.setIndustry("2");
        customerCompany.setIsLegalPersonApple(1);
//        customerCompany.setConnectRealName("测试紧急联系人");
//        customerCompany.setConnectPhone("18566324590");
//        customerCompany.setAddress("彭企业信息详细地址测试103");
//        customerCompany.setProductPurpose("测试设备用途");
//        customerCompany.setIndustry("2");
//        customerCompany.setRemark("记住这是公司的备注不是客户的备注");
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);
//        customerCompany.setCustomerCompanyNeedLaterList(customerCompanyNeedLaterList);
//        customerCompany.setIsLegalPersonApple(1);
        customerCompany.setAgentPersonPhone("18566324595");
        customerCompany.setAgentPersonNo("422827199009080030");
        customerCompany.setAgentPersonName("人民的公义");
//        customerCompany.setLegalPersonPhone("12345678900");
//        customerCompany.setLegalPersonNo("422827199009080031");
//        customerCompany.setLegalPerson("woshi lisiaaaaaaa");
/*        Image image1 = new Image();
//        image1.setImgId(18);
//        customerCompany.setBusinessLicensePictureImage(image1);

        Image image2 = new Image();
//        image2.setImgId(16);
//        customerCompany.setLegalPersonNoPictureFrontImage(image2);

        Image image3 = new Image();
        image3.setImgId(17);
        customerCompany.setLegalPersonNoPictureBackImage(image3);
*/

        //加入经营场所租赁合同
        List<Image> managerPlaceRentContractImageList = new ArrayList<>();
        Image image4= new Image();
        image4.setImgId(608);
        Image image5= new Image();
        image5.setImgId(609);
        Image image6= new Image();
        image6.setImgId(610);
        managerPlaceRentContractImageList.add(image4);
        managerPlaceRentContractImageList.add(image5);
        managerPlaceRentContractImageList.add(image6);

//        customerCompany.setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);

        customer.setCustomerCompany(customerCompany);
        TestResult result = getJsonTestResult("/customer/addCompany", customer);
        Thread.sleep(100000);
    }

    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        customer.setOwner(500021);
        customer.setOwner(500003);
        customer.setIsDefaultConsignAddress(1);
        customer.setFirstApplyAmount(new BigDecimal(0));
        customer.setLaterApplyAmount(new BigDecimal(0));

        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("35052119870503651X");
        customerPerson.setConnectRealName("cd");
        customerPerson.setConnectPhone("18171408870");
        customerPerson.setRealName("cc");
        customerPerson.setPhone("18171408871");
        customerPerson.setEmail("1234567@qq.com");
        customerPerson.setProvince(17);
        customerPerson.setCity(172);
        customerPerson.setDistrict(1687);
        customerPerson.setAddress("老地方烧烤摊儿");

        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/addPerson", customer);
    }

    @Test
    public void testAddCustomerPerson() throws Exception {
        String str = "{\"unionUser\":\"500356\",\"owner\":\"500356\",\"firstApplyAmount\":\"0\",\"laterApplyAmount\":\"0\",\"customerPerson\":{\"realName\":\"cc\",\"personNo\":\"35052119870503651X\",\"email\":\"1234567@qq.com\",\"phone\":\"18171408870\",\"unionUserName\":\"陈涵\",\"unionUser\":\"500356\",\"ownerName\":\"陈涵\",\"owner\":\"500356\",\"firstApplyAmount\":\"0\",\"laterApplyAmount\":\"0\",\"province\":\"17\",\"city\":\"172\",\"district\":\"1687\",\"address\":\"老地方烧烤摊儿\",\"connectRealName\":\"cc\",\"connectPhone\":\"18171408870\",\"remark\":\"个人客户的备注\"}}";

        Customer customer = FastJsonUtil.toBean(str, Customer.class);

        TestResult result = getJsonTestResult("/customer/addPerson", customer);
    }

    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180314-00333");
        customer.setOwner(500014);
        customer.setUnionUser(500003);
        customer.setDeliveryMode(3);
//        customer.setIsDefaultConsignAddress(1);
        customer.setShortLimitReceivableAmount(new BigDecimal(2000));
        CustomerCompany customerCompany = new CustomerCompany();

        //首次所需设备
        List<CustomerCompanyNeed> customerCompanyNeedFirstList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed1 = new CustomerCompanyNeed();
        customerCompanyNeed1.setSkuId(70);
        customerCompanyNeed1.setRentCount(15);
        customerCompanyNeed1.setIsNew(0);
        customerCompanyNeed1.setRentTimeLength(3);
        customerCompanyNeed1.setRentType(1);

        CustomerCompanyNeed customerCompanyNeed2 = new CustomerCompanyNeed();
        customerCompanyNeed2.setSkuId(57);
        customerCompanyNeed2.setRentCount(30);
        customerCompanyNeed2.setIsNew(0);
        customerCompanyNeed2.setRentTimeLength(3);
        customerCompanyNeed2.setRentType(1);

        customerCompanyNeedFirstList.add(customerCompanyNeed1);
        customerCompanyNeedFirstList.add(customerCompanyNeed2);

        //后续所需设备
        List<CustomerCompanyNeed> customerCompanyNeedLaterList = new ArrayList<>();
        CustomerCompanyNeed customerCompanyNeed3 = new CustomerCompanyNeed();
        customerCompanyNeed3.setSkuId(60);
        customerCompanyNeed3.setRentCount(10);

        customerCompanyNeedLaterList.add(customerCompanyNeed3);

        //加入首次租赁设备
        customerCompany.setCustomerCompanyNeedFirstList(customerCompanyNeedFirstList);
        //加入后续租赁设备
//        customerCompany.setCustomerCompanyNeedLaterList(customerCompanyNeedLaterList);

        customerCompany.setCustomerOrigin(1);
        customerCompany.setIndustry("3");
        customerCompany.setCompanyName("齐跑文化");
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324578");
        customerCompany.setAddress("企业信息详细地址测试update");
        customerCompany.setProductPurpose("测试设备用途");
        customerCompany.setIsLegalPersonApple(0);
        customerCompany.setLegalPerson("布东溪");
        customerCompany.setLegalPersonPhone("13866253152");
        customerCompany.setLegalPersonNo("422827199009080023");
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
                "        address: \"外滩18路\"\n" +
                "        affiliatedEnterprise: \"\"\n" +
                "        agentPersonName: \"Nicklaus\"\n" +
                "        agentPersonNo: \"430902199104028554\"\n" +
                "        agentPersonPhone: \"13645987659\"\n" +
                "        city: \"75\"\n" +
                "        companyAbb: \"\"\n" +
                "        companyFoundTime: \"2011-05-02\"\n" +
                "        companyName: \"芒果文化\"\n" +
                "        connectPhone: \"13645465487\"\n" +
                "        connectRealName: \"Nick\"\n" +
                "        customerCompanyNeedFirstList: [{\n" +
                "            skuId: 214,\n" +
                "            rentCount: \"10\"\n" +
                "        }]\n" +
                "        customerCompanyNeedLaterList: []\n" +
                "        customerOrigin: \"3\"\n" +
                "        district: \"787\"\n" +
                "        firstApplyAmount: \"51000\"\n" +
                "        industry: \"9\"\n" +
                "        isDefaultConsignAddress: 0\n" +
                "        isLegalPersonApple: \"1\"\n" +
                "        isNewProduct: \"0\"\n" +
                "        landline: \"0755-4568756\"\n" +
                "        laterApplyAmount: \"0\"\n" +
                "        legalPerson: \"Nicklaus\"\n" +
                "        legalPersonNo: \"430902199104028554\"\n" +
                "        legalPersonPhone: \"13645987659\"\n" +
                "        officeNumber: \"100\"\n" +
                "        operatingArea: \"2000\"\n" +
                "        owner: \"500031\"\n" +
                "        ownerName: \"san\"\n" +
                "        productCount - 214 - 85: \"10\"\n" +
                "        productId: \"2000062\"\n" +
                "        productPurpose: \"办公\"\n" +
                "        province: \"9\"\n" +
                "        registeredCapital: \"1000\"\n" +
                "        skuId: \"214\"\n" +
                "        unifiedCreditCode: \"13546\"\n" +
                "        unionUser: \"500029\"\n" +
                "        unionUserName: \"鹏鹏\"\n" +
                "        unitInsuredNumber: \"100\"\n" +
                "        firstApplyAmount: \"51000\"\n" +
                "        isDefaultConsignAddress: 0\n" +
                "        laterApplyAmount: \"0\"\n" +
                "        owner: \"500031\"\n" +
                "        unionUser: \"500029\"\n" +
                "    }";

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
        customer.setCustomerNo("LXCP-1000-20180308-00329");
        customer.setOwner(500025);
        customer.setDeliveryMode(3);
        customer.setIsDefaultConsignAddress(1);
        customer.setShortLimitReceivableAmount(new BigDecimal(1000));
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("422827999857463210");
        customerPerson.setConnectRealName("赵二天111");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("武神么11");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("update个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/updatePerson", customer);
    }

    @Test
    public void commitCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180305-00300");
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
        customerCompanyQueryParam.setOwnerSubCompanyId(3);
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany", customerCompanyQueryParam);
    }

    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        customerPersonQueryParam.setPageNo(1);
        customerPersonQueryParam.setPageSize(5);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        customerPersonQueryParam.setOwnerSubCompanyId(2);
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
        customer.setCustomerNo("LXCC-1000-20180307-00316");

        TestResult result = getJsonTestResult("/customer/detailCustomerCompany", customer);
    }

    @Test
    public void detailCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180304-00029");
        TestResult result = getJsonTestResult("/customer/detailCustomer", customer);
    }

    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCD-1000-20180304-00032");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson", customer);
    }

    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("LXCC-1000-20180314-00333");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setPayMode(1);
        customerRiskManagement.setReturnVisitFrequency(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setIsFullDeposit(0);
        customerRiskManagement.setIsLimitApple(0);
        customerRiskManagement.setApplePayMode(1);
        customerRiskManagement.setAppleDepositCycle(11);
        customerRiskManagement.setApplePaymentCycle(12);
        customerRiskManagement.setSingleLimitPrice(new BigDecimal(1000));
        customerRiskManagement.setIsLimitNew(0);
        customerRiskManagement.setNewPayMode(1);
        customerRiskManagement.setNewDepositCycle(11);
        customerRiskManagement.setNewPaymentCycle(12);
        customerRiskManagement.setSingleLimitPrice(new BigDecimal(1000));
        customerRiskManagement.setRemark("这是一个备注");
        TestResult result = getJsonTestResult("/customer/updateRisk", customerRiskManagement);
    }

    @Test
    public void updateRiskCreditAmountUsed() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("CP201712060843154191841");
        customerRiskManagement.setCreditAmountUsed(new BigDecimal(9000001));

        TestResult result = getJsonTestResult("/customer/updateRiskCreditAmountUsed", customerRiskManagement);
    }

    @Test
    public void updateRiskJSON() throws Exception {
        String jsonStr = "{\"creditAmount\":\"1000000\",\"depositCycle\":\"10\",\"paymentCycle\":\"10\",\"payMode\":\"1\",\"appleDepositCycle\":\"10\",\"applePaymentCycle\":\"10\",\"applePayMode\":\"1\",\"newDepositCycle\":\"10\",\"newPaymentCycle\":\"10\",\"newPayMode\":\"1\",\"remark\":\"这是一个优质客户, 可以考虑提高授信额度\",\"customerNo\":\"CP201712060843154191841\"}";
        TestResult result = getJsonTestResult("/customer/updateRisk", JSONUtil.convertJSONToBean(jsonStr, CustomerRiskManagement.class));
    }

    @Test
    public void addCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo("LXCC-1000-20180314-00333");
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
        customerConsignInfo.setCustomerConsignInfoId(994);
        customerConsignInfo.setConsigneeName("update联系人");
        customerConsignInfo.setConsigneePhone("13566253478");
        customerConsignInfo.setProvince(29);
        customerConsignInfo.setCity(172); //武汉市
        customerConsignInfo.setDistrict(1685); //汉阳区
        customerConsignInfo.setAddress("修改后的测试地址你看准不准");
        customerConsignInfo.setRemark("update备注");
        customerConsignInfo.setIsMain(0);

        TestResult result = getJsonTestResult("/customer/updateCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void deleteCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(382);

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

    @Test
    public void commitCustomerToWorkflow() throws Exception {
        CustomerCommitParam param = new CustomerCommitParam();
        param.setCustomerNo("LXCC-2000-20180307-00307");
        param.setVerifyUserId(500207);

        TestResult testResult = getJsonTestResult("/customer/commitCustomerToWorkflow",param);
    }

    @Test
    public void rejectCustomer() throws Exception {
        CustomerRejectParam param = new CustomerRejectParam();
        param.setCustomerNo("LXCC-2000-20180307-00307");
        param.setRemark("客户信息有错误，需更改数据");

        TestResult testResult = getJsonTestResult("/customer/rejectCustomer",param);
    }
}