package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.ERPUnTransactionalTest;
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
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.order.impl.support.PenaltySupport;
import com.lxzl.erp.core.service.product.ProductService;
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
public class K3ControllerTest extends ERPUnTransactionalTest {

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
        k3ReturnOrder.setK3CustomerNo("01.SZ201604160012");
        k3ReturnOrder.setK3CustomerName("深圳宜达互联科技有限公司");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂");
        k3ReturnOrder.setReturnContacts("宋老三");
        k3ReturnOrder.setReturnPhone("13809908800");
        k3ReturnOrder.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL);

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXO-20180325-0755-02126");
        k3ReturnOrderDetail1.setOrderItemId("3362");
        k3ReturnOrderDetail1.setOrderEntry("1");

        ProductDO product = productMapper.findById(2000064);
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




        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail2.setOrderNo("LXO-20180326-0755-02225");
        k3ReturnOrderDetail2.setOrderItemId("3561");
        k3ReturnOrderDetail2.setOrderEntry("1");

        ProductDO product2 = productMapper.findById(2000064);
        K3MappingCategoryDO k3MappingCategoryDO2 = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
        K3MappingBrandDO k3MappingBrandDO2 = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
        String number2 = "";
        if(StringUtil.isNotEmpty(product2.getK3ProductNo())){
            number2 = product2.getK3ProductNo();
        }else {
            number2 = "10." + k3MappingCategoryDO2.getK3CategoryCode() + "." + k3MappingBrandDO2.getK3BrandCode() + "." + product2.getProductModel();
        }
        k3ReturnOrderDetail2.setProductNo(number2);
        k3ReturnOrderDetail2.setProductName(product2.getProductName());
        k3ReturnOrderDetail2.setProductCount(1);


        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);

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
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180406151028399");
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
        k3ReturnOrder.setReturnOrderNo("6f52d4caa5a643109d7ccca400218d0c");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂1号");
        k3ReturnOrder.setReturnContacts("张三1");
        k3ReturnOrder.setReturnPhone("13809908801");

        TestResult testResult = getJsonTestResult("/k3/updateReturnOrder", k3ReturnOrder);
    }

    @Test
    public void addReturnOrder() throws Exception {

        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("d5ce9f41a45245e4b213e977f06ce92c");
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("LXSE2018020716");
        k3ReturnOrderDetail1.setOrderEntry("1");
        k3ReturnOrderDetail1.setProductNo("10.LPC.AP.PROH133");
        k3ReturnOrderDetail1.setProductCount(1);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);

        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail2.setOrderNo("LXSE2018020716");
        k3ReturnOrderDetail2.setOrderEntry("4");
        k3ReturnOrderDetail2.setProductNo("20.MOU.XX.PCMOU");
        k3ReturnOrderDetail2.setProductCount(2);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail2);
        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        TestResult testResult = getJsonTestResult("/k3/addReturnOrder", k3ReturnOrder);
    }

    @Test
    public void deleteReturnOrder() throws Exception {
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setK3ReturnOrderDetailId(2);
        TestResult testResult = getJsonTestResult("/k3/deleteReturnOrder", k3ReturnOrderDetail);
    }



    @Test
    public void queryReturnOrder() throws Exception {
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        TestResult testResult = getJsonTestResult("/k3/queryReturnOrder", param);
    }

    @Test
    public void queryReturnOrderByNo() throws Exception {
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        param.setReturnOrderNo("6f52d4caa5a643109d7ccca400218d0c");
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
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180301211219301");
        TestResult testResult = getJsonTestResult("/k3/cancelK3ReturnOrder", k3ReturnOrder);
    }

    @Test
    public void commitK3ReturnOrder() throws Exception {
        K3ReturnOrderCommitParam k3ReturnOrderCommitParam = new K3ReturnOrderCommitParam();
        k3ReturnOrderCommitParam.setReturnOrderNo("LXK3RO20180406185029623");
        k3ReturnOrderCommitParam.setVerifyUserId(500006);
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
        param.setPageSize(10);
        TestResult testResult = getJsonTestResult("/k3/transferOrder", param);
        Thread.sleep(1000);
    }


}
