package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.*;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderListParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.ChangeOrderItemParam;
import com.lxzl.erp.common.domain.order.OrderConfirmChangeToK3Param;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.core.service.order.impl.support.PenaltySupport;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingBrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCategoryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCategoryDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.se.common.util.StringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-13 9:41
 */
public class K3ControllerTest extends ERPTransactionalTest {

    @Autowired
    private PenaltySupport penaltySupport;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;
    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;

    @Test
    public void queryOrder() throws Exception {
        K3OrderQueryParam param = new K3OrderQueryParam();
        TestResult testResult = getJsonTestResult("/k3/queryOrder", param);
        Object object = testResult.getResultMap().get("data");
        String json = JSON.toJSONString(object);
        Page<JSONObject> orderPage = JSON.parseObject(json, Page.class);

        List<JSONObject> orderList = orderPage.getItemList();

        for (int i = 0; i < orderList.size(); i++) {
            if (i > 1) {
                break;
            }
            JSONObject obj = orderList.get(i);
            Order order = JSON.parseObject(obj.toJSONString(), Order.class);
            testResult = getJsonTestResult("/statementOrder/createK3StatementOrder", order);
        }
    }

    @Test
    public void createReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setK3CustomerNo("LXCC-020-20180315-00773");
        k3ReturnOrder.setK3CustomerName("广州普蓝文化发展有限公司");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂");
        k3ReturnOrder.setReturnContacts("宋老三");
        k3ReturnOrder.setReturnPhone("13809908800");
        k3ReturnOrder.setDeliverySubCompanyId(8);
        k3ReturnOrder.setReturnReasonType(1);
        k3ReturnOrder.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL);

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXO-20180315-020-01207");
        k3ReturnOrderDetail1.setOrderItemId("1826");
        k3ReturnOrderDetail1.setOrderEntry("1");

        ProductDO product = productMapper.findById(2000435);
        K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
        String number = "";
        if(StringUtil.isNotEmpty(product.getK3ProductNo())){
            number = product.getK3ProductNo();
        }else {
            number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
        }
        k3ReturnOrderDetail1.setProductNo(number);
        k3ReturnOrderDetail1.setProductName(product.getProductName());
        k3ReturnOrderDetail1.setProductCount(-1);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXO-20180315-020-01207");
//        k3ReturnOrderDetail2.setOrderItemId("3644");
//        k3ReturnOrderDetail2.setOrderEntry("2");

//        ProductDO product2 = productMapper.findById(2000064);
//        K3MappingCategoryDO k3MappingCategoryDO2 = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
//        K3MappingBrandDO k3MappingBrandDO2 = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
//        String number2 = "";
//        if(StringUtil.isNotEmpty(product2.getK3ProductNo())){
//            number2 = product2.getK3ProductNo();
//        }else {
//            number2 = "10." + k3MappingCategoryDO2.getK3CategoryCode() + "." + k3MappingBrandDO2.getK3BrandCode() + "." + product2.getProductModel();
//        }
//        k3ReturnOrderDetail2.setProductNo(number2);
//        k3ReturnOrderDetail2.setProductName(product2.getProductName());
//        k3ReturnOrderDetail2.setProductCount(1);


        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
//        k3ReturnOrderDetail2.setOrderEntry("4");
//        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
//        k3ReturnOrderDetail2.setProductCount(2);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/createReturnOrder", k3ReturnOrder);
    }

    @Test
    public void sendToK3() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180428102808133");
        TestResult testResult = getJsonTestResult("/k3/sendToK3", k3ReturnOrder);
    }

    @Test
    public void createReturnOrderJSON() throws Exception {
        String str = "{\n" +
                "\t\"k3CustomerNo\": \"01.SZ201612310042\",\n" +
                "\t\"k3CustomerName\": \"深圳前海有一科技有限公司\",\n" +
                "\t\"returnTime\": 1519603200000,\n" +
                "\t\"returnAddress\": \"北京东区东直门小酒馆\",\n" +
                "\t\"returnContacts\": \"黎明\",\n" +
                "\t\"returnPhone\": \"18033402832\",\n" +
                "\t\"returnMode\": \"1\",\n" +
                "\t\"k3ReturnOrderDetailList\": [{\n" +
                "\t\t\"orderNo\": \"LXO-20180328-1000-01286\",\n" +
                "\t\t\"orderEntry\": 1,\n" +
                "\t\t\"productNo\": \"10.LPC.TH.P50S\",\n" +
                "\t\t\"productName\": \"ThinkPad P50S\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}]\n" +
                "}";

        K3ReturnOrder k3ReturnOrder = JSON.parseObject(str, K3ReturnOrder.class);
        TestResult testResult = getJsonTestResult("/k3/createReturnOrder", k3ReturnOrder);

    }

    @Test
    public void updateReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180411160705741");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂1号");
        k3ReturnOrder.setReturnContacts("张三1");
        k3ReturnOrder.setReturnPhone("13809908801");
//        k3ReturnOrder.setReturnReason("修改的退货原因1");
        k3ReturnOrder.setReturnMode(1);

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXO-20180315-020-01207");
        k3ReturnOrderDetail1.setOrderItemId("1826");
        k3ReturnOrderDetail1.setOrderEntry("1");

        ProductDO product = productMapper.findById(2000435);
        K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
        String number = "";
        if(StringUtil.isNotEmpty(product.getK3ProductNo())){
            number = product.getK3ProductNo();
        }else {
            number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
        }
        k3ReturnOrderDetail1.setProductNo(number);
        k3ReturnOrderDetail1.setProductName(product.getProductName());
        k3ReturnOrderDetail1.setProductCount(1);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXO-20180315-020-01207");
//        k3ReturnOrderDetail2.setOrderItemId("3644");
//        k3ReturnOrderDetail2.setOrderEntry("2");

//        ProductDO product2 = productMapper.findById(2000064);
//        K3MappingCategoryDO k3MappingCategoryDO2 = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
//        K3MappingBrandDO k3MappingBrandDO2 = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
//        String number2 = "";
//        if(StringUtil.isNotEmpty(product2.getK3ProductNo())){
//            number2 = product2.getK3ProductNo();
//        }else {
//            number2 = "10." + k3MappingCategoryDO2.getK3CategoryCode() + "." + k3MappingBrandDO2.getK3BrandCode() + "." + product2.getProductModel();
//        }
//        k3ReturnOrderDetail2.setProductNo(number2);
//        k3ReturnOrderDetail2.setProductName(product2.getProductName());
//        k3ReturnOrderDetail2.setProductCount(1);


        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
//        k3ReturnOrderDetail2.setOrderEntry("4");
//        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
//        k3ReturnOrderDetail2.setProductCount(2);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/updateReturnOrder", k3ReturnOrder);
    }

    @Test
    public void addReturnOrder() throws Exception {

        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180411110757738");
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderItemId("1953");
        k3ReturnOrderDetail1.setOrderNo("LXSE2018020716");
        k3ReturnOrderDetail1.setOrderEntry("1");
        k3ReturnOrderDetail1.setProductName("测试机器1");
        k3ReturnOrderDetail1.setProductNo("10.LPC.AP.PROH133");
        k3ReturnOrderDetail1.setProductCount(1);
        k3ReturnOrderDetail1.setRemark("备注测试");
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);

        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail2.setOrderItemId("1953");
        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
        k3ReturnOrderDetail2.setOrderEntry("4");
        k3ReturnOrderDetail2.setProductName("测试机器2");
        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
        k3ReturnOrderDetail2.setProductCount(2);
        k3ReturnOrderDetail2.setRemark("备注测试");
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/addReturnOrder", k3ReturnOrder);
    }
    @Test
    public void updateReturnOrderFromERPJSON() throws Exception {
        String str = "{\"returnOrderNo\":\"LXK3RO20180626101110713\",\"returnTime\":1530316800000,\"returnAddress\":\"湖北省武汉市洪山区你家大门口\",\"returnContacts\":\"咩咩\",\"returnPhone\":\"18566324590\",\"returnMode\":\"1\",\"logisticsAmount\":\"500\",\"serviceAmount\":\"0\",\"remark\":\"\",\"returnReasonType\":\"4\",\"deliverySubCompanyId\":\"4\"}";

        K3ReturnOrder k3ReturnOrder = JSON.parseObject(str, K3ReturnOrder.class);


        TestResult testResult = getJsonTestResult("/k3/updateReturnOrderFromERP", k3ReturnOrder);
    }
    @Test
    public void addReturnOrderJSON() throws Exception {
        String str ="{\"returnOrderNo\":\"LXK3RO20180626152302027\",\"k3ReturnOrderDetailList\":[{\"orderNo\":\"LXO-20180626-027-00162\",\"orderEntry\":3595,\"productNo\":\"10.TPC.AP.IPADPRO-64G\",\"productName\":\"苹果IPADPRO-64G\",\"productCount\":\"5\",\"rentingCount\":5,\"canReturnCount\":5,\"k3CustomerNo\":\"LXCC-027-20180620-00135\",\"k3CustomerName\":\"分直结算十一号\",\"orderItemId\":3595,\"productId\":2000577,\"rentType\":2,\"rentLengthType\":2,\"rentTimeLength\":12,\"remark\":\"\",\"orderSubCompanyId\":8,\"orderSubCompanyName\":\"武汉分公司\",\"deliverySubCompanyName\":\"深圳分公司\",\"skuStr\":\"双核 | 64G | 银色 | 12.9\"}]}";

        K3ReturnOrder k3ReturnOrder = JSON.parseObject(str, K3ReturnOrder.class);


        TestResult testResult = getJsonTestResult("/k3/addReturnOrder", k3ReturnOrder);
    }

    @Test
    public void deleteReturnOrder() throws Exception {
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setK3ReturnOrderDetailId(83);
        TestResult testResult = getJsonTestResult("/k3/deleteReturnOrder", k3ReturnOrderDetail);
    }



    @Test
    public void queryReturnOrder() throws Exception {
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        param.setOrderNo("LXO-20180416-1000-00087");
//        param.setReturnOrderStatus(16);
//        param.setK3CustomerNo("LXCC-027-20180416-00026");
        param.setPageSize(3);
        TestResult testResult = getJsonTestResult("/k3/queryReturnOrder", param);
    }


    @Test
    public void queryReturnOrderByNo() throws Exception {
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        param.setReturnOrderNo("LXK3RO20180411110757738");
        TestResult testResult = getJsonTestResult("/k3/queryReturnOrderByNo", param);
    }

    @Test
    public void createChangeOrder() throws Exception {

        K3ChangeOrder k3ChangeOrder = new K3ChangeOrder();
        k3ChangeOrder.setChangeAddress("测试");
        k3ChangeOrder.setChangeContacts("123123123");
        k3ChangeOrder.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        k3ChangeOrder.setK3CustomerNo("123");
        k3ChangeOrder.setK3CustomerName("test");
        k3ChangeOrder.setChangeTime(new Date());
        k3ChangeOrder.setChangePhone("13612342234");
        k3ChangeOrder.setLogisticsAmount(new BigDecimal(12.12));
        k3ChangeOrder.setServiceAmount(new BigDecimal(123.12));
        k3ChangeOrder.setRemark("13123");
        List<K3ChangeOrderDetail> k3ChangeOrderDetailList = new ArrayList<>();
        K3ChangeOrderDetail k3ChangeOrderDetail0 = new K3ChangeOrderDetail();
        K3ChangeOrderDetail k3ChangeOrderDetail1 = new K3ChangeOrderDetail();

        k3ChangeOrderDetailList.add(k3ChangeOrderDetail0);
        k3ChangeOrderDetailList.add(k3ChangeOrderDetail1);

        k3ChangeOrderDetail0.setOrderNo("123123");
        k3ChangeOrderDetail0.setOrderEntry("1");
        k3ChangeOrderDetail0.setProductNo("123123");
        k3ChangeOrderDetail0.setProductName("test1");
        k3ChangeOrderDetail0.setChangeSkuId(1);
        k3ChangeOrderDetail0.setChangeMaterialId(1);
        k3ChangeOrderDetail0.setChangeProductNo("123123");
        k3ChangeOrderDetail0.setChangeProductName("test2");
        k3ChangeOrderDetail0.setProductCount(1);
        k3ChangeOrderDetail0.setProductDiffAmount(new BigDecimal(12));
        k3ChangeOrderDetail0.setRemark("12312312");

        k3ChangeOrderDetail1.setOrderNo("123123");
        k3ChangeOrderDetail1.setOrderEntry("1");
        k3ChangeOrderDetail1.setProductNo("123123");
        k3ChangeOrderDetail1.setProductName("test1");
        k3ChangeOrderDetail1.setChangeSkuId(1);
        k3ChangeOrderDetail1.setChangeMaterialId(1);
        k3ChangeOrderDetail1.setChangeProductNo("123123");
        k3ChangeOrderDetail1.setChangeProductName("test2");
        k3ChangeOrderDetail1.setProductCount(1);
        k3ChangeOrderDetail1.setProductDiffAmount(new BigDecimal(12));
        k3ChangeOrderDetail1.setRemark("12312312");

        k3ChangeOrder.setK3ChangeOrderDetailList(k3ChangeOrderDetailList);
        TestResult testResult = getJsonTestResult("/k3/createChangeOrder", k3ChangeOrder);
    }

    @Test
    public void updateChangeOrder() throws Exception {
        K3ChangeOrder k3ChangeOrder = new K3ChangeOrder();
        k3ChangeOrder.setChangeOrderNo("cd3ae33b8929487688b59188d9522575");
        k3ChangeOrder.setChangeAddress("测试");
        k3ChangeOrder.setChangeContacts("123123123");
        k3ChangeOrder.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        k3ChangeOrder.setK3CustomerNo("123");
        k3ChangeOrder.setK3CustomerName("test");
        k3ChangeOrder.setChangeTime(new Date());
        k3ChangeOrder.setChangePhone("13612342234");
        k3ChangeOrder.setLogisticsAmount(new BigDecimal(122.12));
        k3ChangeOrder.setServiceAmount(new BigDecimal(123.12));
        k3ChangeOrder.setRemark("13123");

        TestResult testResult = getJsonTestResult("/k3/updateChangeOrder", k3ChangeOrder);
    }

    @Test
    public void addChangeOrder() throws Exception {

        K3ChangeOrder k3ChangeOrder = new K3ChangeOrder();
        k3ChangeOrder.setChangeOrderNo("cd3ae33b8929487688b59188d9522575");
        List<K3ChangeOrderDetail> k3ChangeOrderDetailList = new ArrayList<>();
        K3ChangeOrderDetail k3ChangeOrderDetail0 = new K3ChangeOrderDetail();
        K3ChangeOrderDetail k3ChangeOrderDetail1 = new K3ChangeOrderDetail();

        k3ChangeOrderDetailList.add(k3ChangeOrderDetail0);
        k3ChangeOrderDetailList.add(k3ChangeOrderDetail1);

        k3ChangeOrderDetail0.setOrderNo("123123");
        k3ChangeOrderDetail0.setOrderEntry("1");
        k3ChangeOrderDetail0.setProductNo("123123");
        k3ChangeOrderDetail0.setProductName("test1");
        k3ChangeOrderDetail0.setChangeSkuId(1);
        k3ChangeOrderDetail0.setChangeMaterialId(1);
        k3ChangeOrderDetail0.setChangeProductNo("123123");
        k3ChangeOrderDetail0.setChangeProductName("test2");
        k3ChangeOrderDetail0.setProductCount(1);
        k3ChangeOrderDetail0.setProductDiffAmount(new BigDecimal(12));
        k3ChangeOrderDetail0.setRemark("添加测试1");

        k3ChangeOrderDetail1.setOrderNo("123123");
        k3ChangeOrderDetail1.setOrderEntry("1");
        k3ChangeOrderDetail1.setProductNo("123123");
        k3ChangeOrderDetail1.setProductName("test1");
        k3ChangeOrderDetail1.setChangeSkuId(1);
        k3ChangeOrderDetail1.setChangeMaterialId(1);
        k3ChangeOrderDetail1.setChangeProductNo("123123");
        k3ChangeOrderDetail1.setChangeProductName("test2");
        k3ChangeOrderDetail1.setProductCount(1);
        k3ChangeOrderDetail1.setProductDiffAmount(new BigDecimal(12));
        k3ChangeOrderDetail1.setRemark("添加测试2");

        k3ChangeOrder.setK3ChangeOrderDetailList(k3ChangeOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/addChangeOrder", k3ChangeOrder);
    }

    @Test
    public void deleteChangeOrder() throws Exception {
        K3ChangeOrderDetail k3ChangeOrderDetail = new K3ChangeOrderDetail();
        k3ChangeOrderDetail.setK3ChangeOrderDetailId(7);
        TestResult testResult = getJsonTestResult("/k3/deleteChangeOrder", k3ChangeOrderDetail);
    }

    @Test
    public void sendChangeOrderToK3() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("6f52d4caa5a643109d7ccca400218d0c");
        TestResult testResult = getJsonTestResult("/k3/sendToK3", k3ReturnOrder);
    }

    @Test
    public void queryChangeOrder() throws Exception {
        K3ChangeOrderQueryParam param = new K3ChangeOrderQueryParam();

        param.setChangeOrderNo("ff97b7d9cfab4a7bae468be48e1f1ee1");
        param.setK3CustomerNo("123");
        param.setK3CustomerName("test");
        param.setChangeEndTime(new Date());

        TestResult testResult = getJsonTestResult("/k3/queryChangeOrder", param);
    }

    @Test
    public void queryChangeOrderByNo() throws Exception {
        K3ChangeOrderQueryParam param = new K3ChangeOrderQueryParam();
        param.setChangeOrderNo("ff97b7d9cfab4a7bae468be48e1f1ee1");
        TestResult testResult = getJsonTestResult("/k3/queryChangeOrderByNo", param);
    }

    @Test
    public void k3OrderPenalty() throws Exception {
        String returnOrderNo = "test-001";
        ServiceResult<String, BigDecimal> totalPenalty = penaltySupport.k3OrderPenalty(returnOrderNo);
        System.out.println(totalPenalty);
    }

    @Test
    public void cancelK3ReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180411110757738");
        TestResult testResult = getJsonTestResult("/k3/cancelK3ReturnOrder", k3ReturnOrder);
    }

    @Test
    public void commitK3ReturnOrder() throws Exception {
        K3ReturnOrderCommitParam k3ReturnOrderCommitParam = new K3ReturnOrderCommitParam();
        k3ReturnOrderCommitParam.setReturnOrderNo("LXK3RO20180626172204382");
        k3ReturnOrderCommitParam.setVerifyUserId(500013);
        TestResult testResult = getJsonTestResult("/k3/commitK3ReturnOrder", k3ReturnOrderCommitParam);
    }

    @Test
    public void revokeReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180406180514926");
        TestResult testResult = getJsonTestResult("/k3/revokeReturnOrder", k3ReturnOrder);
    }

    @Test
    public void cancelK3ChangeOrder() throws Exception {
        K3ChangeOrder k3ChangeOrder = new K3ChangeOrder();
        k3ChangeOrder.setChangeOrderNo("05e9b5d1327a43368bbabd8e44b81974");
        TestResult testResult = getJsonTestResult("/k3/cancelK3ChangeOrder", k3ChangeOrder);
    }

    @Test
    public void commitK3ChangeOrder() throws Exception {
        K3ChangeOrderCommitParam k3ChangeOrderCommitParam = new K3ChangeOrderCommitParam();
        k3ChangeOrderCommitParam.setChangeOrderNo("05e9b5d1327a43368bbabd8e44b81974");
        k3ChangeOrderCommitParam.setVerifyUserId(500006);
        TestResult testResult = getJsonTestResult("/k3/commitK3ChangeOrder", k3ChangeOrderCommitParam);
    }

    @Test
    public void queryK3SendRecord() throws Exception {
        K3SendRecordParam param = new K3SendRecordParam();
        param.setPageNo(1);
        param.setPageSize(15);
//        param.setRecordReferNo("LXCC-1000-20180305-00299");
        param.setRecordType(5);
//        param.setSendResult(1);
        param.setReceiveResult(1);
        TestResult testResult = getJsonTestResult("/k3/queryK3SendRecord", param);
    }

    @Test
    public void seedAgainK3SendRecord() throws Exception {
        K3SendRecord k3SendRecord = new K3SendRecord();
        k3SendRecord.setK3SendRecordId(1417);
        TestResult testResult = getJsonTestResult("/k3/seedAgainK3SendRecord", k3SendRecord);
        Thread.sleep(1000);
    }

    @Test
    public void batchSendAgainK3SendRecord() throws Exception {
        String start = "2018-03-08";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStartTime = sdf.parse(start);

        K3SendRecordBatchParam param = new K3SendRecordBatchParam();
        param.setRecordType(1);
        param.setBatchType(1);
        param.setStartTime(rentStartTime);
        param.setEndTime(new Date());
        param.setIntervalTime(1000);
        TestResult testResult = getJsonTestResult("/k3/batchSendDataToK3", param);
        Thread.sleep(1000);
    }

    @Test
    public void transferOrder() throws Exception {
        K3OrderQueryParam param = new K3OrderQueryParam();
        param.setPageNo(1);
        param.setPageSize(300);
        param.setOrderNo("LXSE2017121166");
        param.setCreateStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-15"));
        param.setCreateEndTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-15"));
        param.setSubCompanyNo("03");
        TestResult testResult = getJsonTestResult("/k3/transferOrder", param);
        Thread.sleep(1000);
    }

    @Test
    public void queryOrderForReturn() throws Exception {
        OrderForReturnQueryParam param = new OrderForReturnQueryParam();
//        param.setCustomerNo("LXCC-027-20180305-00198");
//        param.setCreateStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2001-03-01"));
//        param.setCreateEndTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-22"));
//        param.setOrderStatus(20);
//        param.setOrderStatus(16);
//        param.setCustomerNo("LXO-20180613-027-00096");
        param.setOrderNo("LXO-20180613-027-00096");
        TestResult testResult = getJsonTestResult("/k3/queryOrderForReturn", param);
    }
    @Test
    public void queryOrderForReturnJson() throws Exception {
        String str = "{\"pageNo\":1,\"pageSize\":15,\"orderNo\":\"\",\"orderStatus\":\"\",\"createStartTime\":\"983376000000\",\"createEndTime\":\"1530374399999\",\"createTimePicker\":\"2001-03-01 - 2018-06-30\",\"customerNo\":\"LXCC-027-20180305-00198\"}";

        OrderForReturnQueryParam param = JSON.parseObject(str, OrderForReturnQueryParam.class);
        TestResult testResult = getJsonTestResult("/k3/queryOrderForReturn", param);
    }
    @Test
    public void queryOrderByNoForReturn() throws Exception {
        OrderForReturnQueryParam param = new OrderForReturnQueryParam();
        param.setOrderNo("LXSE2017122061");
        TestResult testResult = getJsonTestResult("/k3/queryOrderByNoForReturn", param);
    }
    @Test
    public void createReturnOrderFromERPJson() throws Exception {
        String str = "{\"k3CustomerNo\":\"LXCC-027-20180620-00136\",\"k3CustomerName\":\"分直结算十二号\",\"returnTime\":1591779200000,\"returnAddress\":\"湖北省武汉市洪山区你家大门口\",\"returnContacts\":\"咩咩\",\"returnPhone\":\"18566324590\",\"returnMode\":\"2\",\"logisticsAmount\":\"0\",\"serviceAmount\":0,\"remark\":\"\",\"returnReasonType\":\"1\",\"deliverySubCompanyId\":\"8\",\"k3ReturnOrderDetailList\":[{\"orderNo\":\"LXO-20180627-027-00164\",\"orderEntry\":3598,\"productNo\":\"10.TPC.AP.IPADPRO-64G\",\"productName\":\"苹果IPADPRO-64G\",\"productCount\":\"3\",\"remark\":\"\",\"orderItemId\":3598}]}";
        K3ReturnOrder k3ReturnOrder = JSON.parseObject(str, K3ReturnOrder.class);


        TestResult testResult = getJsonTestResult("/k3/createReturnOrderFromERP", k3ReturnOrder);
    }
    @Test
    public void createReturnOrderFromERP() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setK3CustomerNo("LXCC-027-20180417-00031");
        k3ReturnOrder.setK3CustomerName("退货ing五号");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京市北京东城区天安门");
        k3ReturnOrder.setReturnContacts("皮皮狗");
        k3ReturnOrder.setReturnPhone("18566324590");
        k3ReturnOrder.setLogisticsAmount(new BigDecimal(1000));
        k3ReturnOrder.setServiceAmount(new BigDecimal(0));
        k3ReturnOrder.setDeliverySubCompanyId(2);
        k3ReturnOrder.setReturnReasonType(4);
        k3ReturnOrder.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL);

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXO-20180517-027-00082");
        k3ReturnOrderDetail1.setOrderItemId("2828");
        k3ReturnOrderDetail1.setOrderEntry("2828");
        k3ReturnOrderDetail1.setProductNo("10.TPC.AP.IPADPRO-64G");
        k3ReturnOrderDetail1.setProductName("苹果IPADPRO-64G");
        k3ReturnOrderDetail1.setProductCount(-1);


//        ProductDO product = productMapper.findById(2000435);
//        K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
//        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
//        String number = "";
//        if(StringUtil.isNotEmpty(product.getK3ProductNo())){
//            number = product.getK3ProductNo();
//        }else {
//            number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
//        }
//        k3ReturnOrderDetail1.setProductNo(number);
//        k3ReturnOrderDetail1.setProductName(product.getProductName());
//        k3ReturnOrderDetail1.setProductCount(1);


//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXO-20180315-020-01207");
//        k3ReturnOrderDetail2.setOrderItemId("3644");
//        k3ReturnOrderDetail2.setOrderEntry("2");

//        ProductDO product2 = productMapper.findById(2000064);
//        K3MappingCategoryDO k3MappingCategoryDO2 = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
//        K3MappingBrandDO k3MappingBrandDO2 = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
//        String number2 = "";
//        if(StringUtil.isNotEmpty(product2.getK3ProductNo())){
//            number2 = product2.getK3ProductNo();
//        }else {
//            number2 = "10." + k3MappingCategoryDO2.getK3CategoryCode() + "." + k3MappingBrandDO2.getK3BrandCode() + "." + product2.getProductModel();
//        }
//        k3ReturnOrderDetail2.setProductNo(number2);
//        k3ReturnOrderDetail2.setProductName(product2.getProductName());
//        k3ReturnOrderDetail2.setProductCount(1);


        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
//        k3ReturnOrderDetail2.setOrderEntry("4");
//        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
//        k3ReturnOrderDetail2.setProductCount(2);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/createReturnOrderFromERP", k3ReturnOrder);
    }
    @Test
    public void updateReturnOrderFromERP() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180411160705741");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂1号");
        k3ReturnOrder.setReturnContacts("张三1");
        k3ReturnOrder.setReturnPhone("13809908801");
//        k3ReturnOrder.setReturnReason("修改的退货原因1");
        k3ReturnOrder.setReturnMode(1);

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXO-20180315-020-01207");
        k3ReturnOrderDetail1.setOrderItemId("1826");
        k3ReturnOrderDetail1.setOrderEntry("1");

        ProductDO product = productMapper.findById(2000435);
        K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
        String number = "";
        if(StringUtil.isNotEmpty(product.getK3ProductNo())){
            number = product.getK3ProductNo();
        }else {
            number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
        }
        k3ReturnOrderDetail1.setProductNo(number);
        k3ReturnOrderDetail1.setProductName(product.getProductName());
        k3ReturnOrderDetail1.setProductCount(1);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXO-20180315-020-01207");
//        k3ReturnOrderDetail2.setOrderItemId("3644");
//        k3ReturnOrderDetail2.setOrderEntry("2");

//        ProductDO product2 = productMapper.findById(2000064);
//        K3MappingCategoryDO k3MappingCategoryDO2 = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
//        K3MappingBrandDO k3MappingBrandDO2 = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
//        String number2 = "";
//        if(StringUtil.isNotEmpty(product2.getK3ProductNo())){
//            number2 = product2.getK3ProductNo();
//        }else {
//            number2 = "10." + k3MappingCategoryDO2.getK3CategoryCode() + "." + k3MappingBrandDO2.getK3BrandCode() + "." + product2.getProductModel();
//        }
//        k3ReturnOrderDetail2.setProductNo(number2);
//        k3ReturnOrderDetail2.setProductName(product2.getProductName());
//        k3ReturnOrderDetail2.setProductCount(1);


        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);

//        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
//        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
//        k3ReturnOrderDetail2.setOrderEntry("4");
//        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
//        k3ReturnOrderDetail2.setProductCount(2);
//        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/updateReturnOrderFromERP", k3ReturnOrder);
    }

    @Test
    public void confirmOrder() throws Exception {
        OrderConfirmChangeToK3Param orderConfirmChangeToK3Param = new OrderConfirmChangeToK3Param();
        orderConfirmChangeToK3Param.setOrderNo("LXO-20180528-027-00150");
        orderConfirmChangeToK3Param.setOrderId(3001954);

        List<ChangeOrderItemParam> changeOrderItemParamList = new ArrayList<>();
        ChangeOrderItemParam changeOrderItemParam1 = new ChangeOrderItemParam();
        changeOrderItemParam1.setItemId(2961);
        changeOrderItemParam1.setItemType(1);
        changeOrderItemParam1.setReturnCount(1);

        changeOrderItemParamList.add(changeOrderItemParam1);
        orderConfirmChangeToK3Param.setChangeOrderItemParamList(changeOrderItemParamList);

        TestResult testResult = getJsonTestResult("/k3/confirmOrder", orderConfirmChangeToK3Param);
    }

    @Test
    public void batchImportK3HistoricalReturnList() throws Exception {
        K3ReturnOrderListParam k3ReturnOrderListParam = new K3ReturnOrderListParam();
        List<String> list = new ArrayList<>();
        list.add("LXK3RO20180611153506891");
        k3ReturnOrderListParam.setReturnOrderNoList(list);
        TestResult testResult = getJsonTestResult("/k3/batchImportK3HistoricalReturnList", k3ReturnOrderListParam);
    }
}
