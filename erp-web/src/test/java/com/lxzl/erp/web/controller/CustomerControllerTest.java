package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.ConfirmStatementStatus;
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
        customer.setCustomerNo("LXCC-2001-20180515-00017");
        customer.setOwner(500325);
        customer.setUnionUser(null);
        TestResult result = getJsonTestResult("/customer/updateOwnerAndUnionUser", customer);
    }

    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setOwner(500022);
//        customer.setUnionUser(500003);
        customer.setRemark("记住这是客户的备注");
        customer.setDeliveryMode(0);
        customer.setStatementDate(20);
//        customer.setFirstApplyAmount(new BigDecimal(84000));
//        customer.setLaterApplyAmount(new BigDecimal(50000));
        customer.setIsDefaultConsignAddress(1);
        List<CustomerConsignInfo> customerConsignInfoList = new ArrayList<>();
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setConsigneeName("收货测试员工");
        customerConsignInfo.setConsigneePhone("18566324595");
        customerConsignInfo.setIsMain(1);
        customerConsignInfo.setProvince(19);
        customerConsignInfo.setCity(207);
        customerConsignInfo.setDistrict(1991);
        customerConsignInfo.setAddress("彭企业信息详细地址测试1057788");
        customerConsignInfo.setIsBusinessAddress(0);
        customerConsignInfoList.add(customerConsignInfo);

//        CustomerConsignInfo customerConsignInfo2 = new CustomerConsignInfo();
//        customerConsignInfo2.setConsigneeName("收货测试员工2");
//        customerConsignInfo2.setConsigneePhone("13555555555");
//        customerConsignInfo2.setIsMain(0);
//        customerConsignInfo2.setProvince(2);
//        customerConsignInfo2.setCity(3);
//        customerConsignInfo2.setDistrict(19);
//        customerConsignInfo2.setAddress("广安路9号");
//        customerConsignInfo2.setIsBusinessAddress(0);
//        customerConsignInfoList.add(customerConsignInfo2);
//
        CustomerConsignInfo customerConsignInfo3 = new CustomerConsignInfo();
        customerConsignInfo3.setConsigneeName("火狐032007107");
        customerConsignInfo3.setConsigneePhone("13866253151");
        customerConsignInfo3.setIsMain(0);
        customerConsignInfo3.setProvince(2);
        customerConsignInfo3.setCity(3);
        customerConsignInfo3.setDistrict(19);
        customerConsignInfo3.setAddress("企业信息详细地址测试update");
        customerConsignInfo3.setIsBusinessAddress(1);
        customerConsignInfoList.add(customerConsignInfo3);

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
        customerCompany.setCompanyName("水钢葫芦娃集团");
        customerCompany.setIndustry("2");
        customerCompany.setIsLegalPersonApple(1);
        customerCompany.setProvince(2);
        customerCompany.setCity(3);
        customerCompany.setDistrict(19);
        customerCompany.setIsLegalPersonApple(1);
        customerCompany.setConnectRealName("测试紧急联系人");
        customerCompany.setConnectPhone("18566324590");
        customerCompany.setAddress("彭企业信息详细地址测试103");
        customerCompany.setProductPurpose("测试设备用途");
        customerCompany.setIndustry("2");
        customerCompany.setRemark("记住这是公司的备注不是客户的备注");
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

        customerCompany.setCustomerConsignInfoList(customerConsignInfoList);

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
        Thread.sleep(1000);
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
        customer.setStatementDate(20);
//        customer.setCustomerId(3);

        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("35052119870503651X");
        customerPerson.setConnectRealName("cd");
        customerPerson.setConnectPhone("18171408870");
        customerPerson.setRealName("李七马李七马请");
        customerPerson.setPhone("18171408871");
        customerPerson.setEmail("1234567@qq.com");
        customerPerson.setProvince(17);
        customerPerson.setCity(172);
        customerPerson.setDistrict(1687);
        customerPerson.setAddress("老地方烧烤摊儿11");
        customerPerson.setInternalName("这是内部名称这是内部名称这是内部名称这是");

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
        customer.setCustomerNo("LXCC-2001-20180515-00018");
        customer.setOwner(500022);
//        customer.setUnionUser(500372);
        customer.setDeliveryMode(3);
        customer.setStatementDate(20);
//        customer.setIsDefaultConsignAddress(1);
        customer.setShortLimitReceivableAmount(new BigDecimal(2000));
        CustomerCompany customerCompany = new CustomerCompany();

        customer.setIsDefaultConsignAddress(0);
        List<CustomerConsignInfo> customerConsignInfoList = new ArrayList<>();
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(5646);
        customerConsignInfo.setConsigneeName("收货测试员工123");
        customerConsignInfo.setConsigneePhone("18566324595");
        customerConsignInfo.setIsMain(1);
        customerConsignInfo.setProvince(19);
        customerConsignInfo.setCity(207);
        customerConsignInfo.setDistrict(1991);
        customerConsignInfo.setAddress("南京路24号");
        customerConsignInfo.setIsBusinessAddress(0);
        customerConsignInfo.setVerifyStatus(0);
        customerConsignInfoList.add(customerConsignInfo);

        CustomerConsignInfo customerConsignInfo2 = new CustomerConsignInfo();
        customerConsignInfo2.setCustomerConsignInfoId(5647);
        customerConsignInfo2.setConsigneeName("收货测试员工03301021");
        customerConsignInfo2.setConsigneePhone("13555555555");
        customerConsignInfo2.setIsMain(0);
        customerConsignInfo2.setProvince(2);
        customerConsignInfo2.setCity(3);
        customerConsignInfo2.setDistrict(19);
        customerConsignInfo2.setAddress("广安路9号");
        customerConsignInfo2.setIsBusinessAddress(0);
        customerConsignInfo2.setVerifyStatus(2);
        customerConsignInfoList.add(customerConsignInfo2);

        CustomerConsignInfo customerConsignInfo3 = new CustomerConsignInfo();
        customerConsignInfo3.setCustomerConsignInfoId(5648);
        customerConsignInfo3.setConsigneeName("火狐03200710810");
        customerConsignInfo3.setConsigneePhone("13866253151");
        customerConsignInfo3.setIsMain(0);
        customerConsignInfo3.setProvince(2);
        customerConsignInfo3.setCity(3);
        customerConsignInfo3.setDistrict(19);
        customerConsignInfo3.setAddress("企业信息详细地址测试update");
        customerConsignInfo3.setIsBusinessAddress(0);
        customerConsignInfo3.setVerifyStatus(2);
        customerConsignInfoList.add(customerConsignInfo3);

//        customerCompany.setCustomerConsignInfoList(customerConsignInfoList);

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
        customerCompany.setCompanyName("拔萝卜公司");
        customerCompany.setConnectRealName("测试紧急联系人0332");
        customerCompany.setConnectPhone("18566324578");
        customerCompany.setAddress("企业信息详细地址测试update");
        customerCompany.setProductPurpose("测试设备用途");
        customerCompany.setIsLegalPersonApple(0);
        customerCompany.setLegalPerson("布东溪");
        customerCompany.setLegalPersonPhone("13866253152");
        customerCompany.setLegalPersonNo("422827199009080023");
        customerCompany.setAgentPersonPhone("13866253151");
        customerCompany.setAgentPersonName("火狐0320071011");
        customerCompany.setProvince(2);
        customerCompany.setCity(3);
        customerCompany.setDistrict(19);
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
        String str = "{\n" +
                "    \"customerNo\": \"LXCC-025-20180212-00490\",\n" +
                "    \"unionUser\": \"\",\n" +
                "    \"owner\": \"500227\",\n" +
                "    \"firstApplyAmount\": \"75000.00\",\n" +
                "    \"laterApplyAmount\": \"39690\",\n" +
                "    \"deliveryMode\": \"3\",\n" +
                "    \"shortLimitReceivableAmount\": \"\",\n" +
                "    \"isDefaultConsignAddress\": \"0\",\n" +
                "    \"statementDate\": \"20\",\n" +
                "    \"customerCompany\": {\n" +
                "        \"customerOrigin\": \"4\",\n" +
                "        \"ownerName\": \"何波\",\n" +
                "        \"owner\": \"500227\",\n" +
                "        \"unionUserName\": \"\",\n" +
                "        \"unionUser\": \"\",\n" +
                "        \"companyAbb\": \"\",\n" +
                "        \"industry\": \"1\",\n" +
                "        \"affiliatedEnterprise\": \"北京宇天恒瑞科技发展有限公司\",\n" +
                "        \"unitInsuredNumber\": \"\",\n" +
                "        \"landline\": \"\",\n" +
                "        \"shortLimitReceivableAmount\": \"\",\n" +
                "        \"deliveryMode\": \"3\",\n" +
                "        \"isLegalPersonApple\": \"0\",\n" +
                "        \"agentPersonName\": \"王莹\",\n" +
                "        \"agentPersonNo\": \"51010619880331002X\",\n" +
                "        \"agentPersonPhone\": \"13151076268\",\n" +
                "        \"statementDate\": \"20\",\n" +
                "        \"remark\": \"\",\n" +
                "        \"legalPerson\": \"许平\",\n" +
                "        \"legalPersonNo\": \"430304197808243050\",\n" +
                "        \"legalPersonPhone\": \"18080033060\",\n" +
                "        \"registeredCapital\": \"10000000\",\n" +
                "        \"officeNumber\": \"50\",\n" +
                "        \"unifiedCreditCode\": \"91110105672825056T\",\n" +
                "        \"operatingArea\": \"1000\",\n" +
                "        \"connectRealName\": \"许平\",\n" +
                "        \"connectPhone\": \"18080033060\",\n" +
                "        \"productPurpose\": \"办公\",\n" +
                "        \"firstApplyAmount\": \"75000.00\",\n" +
                "        \"laterApplyAmount\": \"39690\",\n" +
                "        \"companyFoundTime\": \"2017-08-02\",\n" +
                "        \"isDefaultConsignAddress\": \"0\",\n" +
                "        \"province\": \"\",\n" +
                "        \"city\": \"\",\n" +
                "        \"district\": \"\",\n" +
                "        \"address\": \"南京市栖霞区迈皋桥创业园科技研发基地寅春路18号-M747\",\n" +
                "        \"unitPrice\": \"2646.00\",\n" +
                "        \"isNewProduct\": \"0\",\n" +
                "        \"productId\": \"2000041\",\n" +
                "        \"skuId\": \"146\",\n" +
                "        \"rentCount-1667-96\": \"10\",\n" +
                "        \"rentType-1667-44\": \"2\",\n" +
                "        \"rentTimeLength-1667-76\": \"12\",\n" +
                "        \"firstListTotalPrice\": \"75000.00\",\n" +
                "        \"rentCount-146-61\": \"15\",\n" +
                "        \"rentType-146-8\": \"2\",\n" +
                "        \"rentTimeLength-146-62\": \"12\",\n" +
                "        \"laterListTotalPrice\": \"\",\n" +
                "        \"companyName\": \"北京宇天恒瑞科技发展有限公司南京分公司\",\n" +
                "        \"businessLicensePictureImage\": {\n" +
                "            \"imgId\": 363\n" +
                "        },\n" +
                "        \"legalPersonNoPictureFrontImage\": {\n" +
                "            \"imgId\": 364\n" +
                "        },\n" +
                "        \"legalPersonNoPictureBackImage\": {\n" +
                "            \"imgId\": 365\n" +
                "        },\n" +
                "        \"agentPersonNoPictureFrontImage\": {\n" +
                "            \"imgId\": 366\n" +
                "        },\n" +
                "        \"agentPersonNoPictureBackImage\": {\n" +
                "            \"imgId\": 367\n" +
                "        },\n" +
                "        \"customerCompanyNeedFirstList\": [\n" +
                "            {\n" +
                "                \"skuId\": 146,\n" +
                "                \"rentCount\": 10,\n" +
                "                \"isNew\": 0,\n" +
                "                \"rentType\": 2,\n" +
                "                \"rentTimeLength\": 12\n" +
                "            }\n" +
                "        ],\n" +
                "        \"customerCompanyNeedLaterList\": [\n" +
                "            {\n" +
                "                \"skuId\": 146,\n" +
                "                \"rentCount\": 15,\n" +
                "                \"isNew\": 0,\n" +
                "                \"rentType\": 2,\n" +
                "                \"rentTimeLength\": 12\n" +
                "            }\n" +
                "        ],\n" +
                "        \"customerConsignInfoList\": [\n" +
                "            {\n" +
                "                \"address\": \"南京市建邺区庐山路226号南京移动20楼\",\n" +
                "                \"consigneeName\": \"无收件人\",\n" +
                "                \"createTime\": 1518418296000,\n" +
                "                \"createUser\": \"500227\",\n" +
                "                \"createUserRealName\": \"何波\",\n" +
                "                \"customerConsignInfoId\": 1446,\n" +
                "                \"customerId\": 701446,\n" +
                "                \"dataStatus\": 1,\n" +
                "                \"isBusinessAddress\": 0,\n" +
                "                \"isMain\": 1,\n" +
                "                \"updateTime\": 1518418296000,\n" +
                "                \"updateUser\": \"500227\",\n" +
                "                \"updateUserRealName\": \"何波\",\n" +
                "                \"verifyStatus\": 0,\n" +
                "                \"workflowType\": 0\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        Customer customer = FastJsonUtil.toBean(str, Customer.class);
        TestResult result = getJsonTestResult("/customer/updateCompany", customer);
    }

    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCP-027-20180905-00009");
        customer.setOwner(500025);
        customer.setDeliveryMode(3);
        customer.setIsDefaultConsignAddress(1);
        customer.setStatementDate(31);
        customer.setShortLimitReceivableAmount(new BigDecimal(1000));
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setPersonNo("35052119870503651X");
        customerPerson.setConnectRealName("赵二天111");
        customerPerson.setConnectPhone("18563214987");
        customerPerson.setRealName("李七马");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("update个人信息详细地址测试");
        customerPerson.setInternalName("跟新内部名称");
        customerPerson.setInternalName("哦哦内部名称这是内部名称这是内部名称这是取");
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
//        customerCompanyQueryParam.setCustomerStatus(4);
//        customerCompanyQueryParam.setCustomerNo("LXCC10002018010100005");
          customerCompanyQueryParam.setCompanyName("12312");
//          customerCompanyQueryParam.setIsRisk(1);
//        customerCompanyQueryParam.setProductPurpose("测试");
//        customerCompanyQueryParam.setIsDisabled(0);
//        customerCompanyQueryParam.setCustomerStatus(CustomerStatus.STATUS_PASS);
//        customerCompanyQueryParam.setConnectPhone("13726273851");

//        customerCompanyQueryParam.setOwnerSubCompanyId(3);
//        customerCompanyQueryParam.setConfirmStatementStatus(ConfirmStatementStatus.CONFIRM_STATUS_YES);
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany", customerCompanyQueryParam);
    }

    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
//        customerPersonQueryParam.setPageNo(1);
//        customerPersonQueryParam.setPageSize(5);
//        customerPersonQueryParam.setRealName("a");
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
//        customerPersonQueryParam.setOwnerSubCompanyId(2);
//        customerPersonQueryParam.setIsRisk(1);
//        customerPersonQueryParam.setCustomerStatus(3);
//        customerPersonQueryParam.setConfirmStatementStatus(ConfirmStatementStatus.CONFIRM_STATUS_YES);
        customerPersonQueryParam.setInternalName("哦哦内部名称这是内部名称这是内部名称这是");
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
        customer.setCustomerNo("LXCC-0755-20180112-00002");

        TestResult result = getJsonTestResult("/customer/detailCustomerCompany", customer);
        System.out.println(JSONUtil.convertBeanToJSON(result).toString());
    }

    @Test
    public void detailCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180515-00015");
        TestResult result = getJsonTestResult("/customer/detailCustomer", customer);
    }

    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCP-027-20180905-00009");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson", customer);
    }

    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("LXCC-1000-20180322-00350");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setPayMode(1);
        customerRiskManagement.setReturnVisitFrequency(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setIsFullDeposit(0);
        customerRiskManagement.setIsLimitApple(0);
        customerRiskManagement.setApplePayMode(1);
        customerRiskManagement.setAppleDepositCycle(11);
        customerRiskManagement.setApplePaymentCycle(11);
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
        customerConsignInfo.setCustomerConsignInfoId(1205);
        customerConsignInfo.setConsigneeName("update联系人123");
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
        customerConsignInfoQueryParam.setCustomerNo("LXCC-027-20180329-00837");
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
        param.setCustomerNo("LXCC-027-20180402-00002");
        param.setVerifyUserId(500016);
        param.setRemark("commit");

        TestResult testResult = getJsonTestResult("/customer/commitCustomerToWorkflow",param);
    }

    @Test
    public void rejectCustomer() throws Exception {
        CustomerRejectParam param = new CustomerRejectParam();
        param.setCustomerNo("LXCC-027-20180329-00846");
        param.setRemark("客户信息有错误，需更改数据");

        TestResult testResult = getJsonTestResult("/customer/rejectCustomer",param);
    }

    /**风控历史记录分页*/
    @Test
    public void pageCustomerRiskManagementHistory() throws Exception {
        CustomerRiskManageHistoryQueryParam customerRiskManageHistoryQueryParam = new CustomerRiskManageHistoryQueryParam();
        customerRiskManageHistoryQueryParam.setCustomerNo("LXCC-027-20180326-00798");
        TestResult result = getJsonTestResult("/customer/pageCustomerRiskManagementHistory", customerRiskManageHistoryQueryParam);
    }

    /**风控历史记录详情*/
    @Test
    public void detailCustomerRiskManagementHistory() throws Exception {
        CustomerRiskManagementHistory customerRiskManagementHistory = new CustomerRiskManagementHistory();
        customerRiskManagementHistory.setCustomerRiskManagementHistoryId(3);
        TestResult result = getJsonTestResult("/customer/detailCustomerRiskManagementHistory", customerRiskManagementHistory);
    }
    /**测试根据公司名称更新公司简单名称字段的测试用例*/
    @Test
    public void  customerCompanySimpleNameProcessingTest() throws Exception{
        TestResult result = getJsonTestResult("/customer/customerCompanySimpleNameProcessing", null);
    }

    /**提交地址审批*/
    @Test
    public void commitCustomerConsignInfo() throws Exception {
        CustomerConsignCommitParam param = new CustomerConsignCommitParam();
        param.setCustomerConsignId(5657);
        param.setVerifyUserId(500216);
//        param.setVerifyUserId(500016);
        param.setRemark("commit");

        TestResult testResult = getJsonTestResult("/customer/commitCustomerConsignInfo",param);
    }


    @Test
    public void addCustomerReturnVisit() throws Exception {

        ReturnVisit returnVisit = new ReturnVisit();
        returnVisit.setCustomerNo("LXCC-1000-20180330-00827");
        returnVisit.setReturnVisitDescribe("第6次回访，不知道填什么好");
        returnVisit.setRemark("回访是个什么感觉");

        List<Image> customerReturnVisitImageList = new ArrayList<>();
        Image customerReturnVisitImage1 = new Image();
        Image customerReturnVisitImage2 = new Image();
        customerReturnVisitImage1.setImgId(1833);
        customerReturnVisitImage2.setImgId(1832);

        customerReturnVisitImageList.add(customerReturnVisitImage1);
        customerReturnVisitImageList.add(customerReturnVisitImage2);

        returnVisit.setCustomerReturnVisitImageList(customerReturnVisitImageList);

        TestResult testResult = getJsonTestResult("/customer/addCustomerReturnVisit",returnVisit);
    }

    @Test
    public void updateCustomerReturnVisit() throws Exception {
        ReturnVisit returnVisit = new ReturnVisit();
        returnVisit.setReturnVisitId(18);
        returnVisit.setReturnVisitDescribe("更改第6次回访，这次就看看有不有效果");
        returnVisit.setRemark("更改回访是个什么感觉");

        List<Image> customerReturnVisitImageList = new ArrayList<>();
        Image customerReturnVisitImage1 = new Image();
        Image customerReturnVisitImage2 = new Image();
        Image customerReturnVisitImage3 = new Image();
        customerReturnVisitImage1.setImgId(1833);
        customerReturnVisitImage2.setImgId(1832);
        customerReturnVisitImage3.setImgId(1834);

        customerReturnVisitImageList.add(customerReturnVisitImage1);
        customerReturnVisitImageList.add(customerReturnVisitImage2);
//        customerReturnVisitImageList.add(customerReturnVisitImage3);

        returnVisit.setCustomerReturnVisitImageList(customerReturnVisitImageList);

        TestResult testResult = getJsonTestResult("/customer/updateCustomerReturnVisit",returnVisit);
     }

    @Test
    public void deleteCustomerReturnVisit() throws Exception {

        ReturnVisit returnVisit = new ReturnVisit();
        returnVisit.setReturnVisitId(24);

        TestResult testResult = getJsonTestResult("/customer/deleteCustomerReturnVisit",returnVisit);
    }

    @Test
    public void detailCustomerReturnVisit() throws Exception {

        ReturnVisit returnVisit = new ReturnVisit();
        returnVisit.setReturnVisitId(16);

        TestResult testResult = getJsonTestResult("/customer/detailCustomerReturnVisit",returnVisit);
    }


    @Test
    public void pageCustomerReturnVisit() throws Exception {
        CustomerReturnVisitQueryParam customerReturnVisitQueryParam = new CustomerReturnVisitQueryParam();
        customerReturnVisitQueryParam.setPageNo(1);
        customerReturnVisitQueryParam.setPageSize(10);
        customerReturnVisitQueryParam.setCustomerNo("LXCC-1000-20180330-00827");

        TestResult testResult = getJsonTestResult("/customer/pageCustomerReturnVisit",customerReturnVisitQueryParam);
    }
    @Test
    public void setIsRisk() throws Exception {

        TestResult testResult = getJsonTestResult("/customer/setIsRisk",null);
    }

    @Test
    public void testConfirmStatement() throws Exception {
        Customer customer = new Customer();
//        customer.setCustomerNo("LXCP-2001-20180605-00001");
        customer.setCustomerNo("LXCP-1000-20180523-00020");
        TestResult testResult = getJsonTestResult("/customer/confirmStatement",customer);
    }
    @Test
    public void queryRentCountByCustomerNo() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-027-20180305-00153");
        TestResult result = getJsonTestResult("/customer/queryRentCountByCustomerNo", customer);
    }

    @Test
    public void addParentCompany() throws Exception {
        CustomerCompanyAddParent customerCompanyAddParent = new CustomerCompanyAddParent();
        customerCompanyAddParent.setParentCustomerId(700432);
        List<Integer> customerIdList = new ArrayList<>();
        customerIdList.add(700435);
        customerIdList.add(700436);
        customerCompanyAddParent.setCustomerIdList(customerIdList);
        TestResult result = getJsonTestResult("/customer/addParentCompany", customerCompanyAddParent);
    }

    @Test
    public void queryParentCompanyPage() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCustomerStatus(4);
        customerCompanyQueryParam.setCustomerNo("LXCC-0755-20180112-00001");
//        customerCompanyQueryParam.setCompanyName("12312");
//          customerCompanyQueryParam.setIsRisk(1);
//        customerCompanyQueryParam.setProductPurpose("测试");
//        customerCompanyQueryParam.setIsDisabled(0);
//        customerCompanyQueryParam.setCustomerStatus(CustomerStatus.STATUS_PASS);
//        customerCompanyQueryParam.setConnectPhone("13726273851");

//        customerCompanyQueryParam.setOwnerSubCompanyId(3);
//        customerCompanyQueryParam.setConfirmStatementStatus(ConfirmStatementStatus.CONFIRM_STATUS_YES);
        TestResult result = getJsonTestResult("/customer/queryParentCompanyPage", customerCompanyQueryParam);
    }
}