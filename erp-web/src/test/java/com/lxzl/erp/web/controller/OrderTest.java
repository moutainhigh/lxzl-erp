package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.ProcessOrderParam;
import com.lxzl.erp.common.domain.order.LastRentPriceRequest;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: 订单测试类
 *
 * @author gaochao
 * @date 2017-11-15 14:14
 */
public class OrderTest extends ERPUnTransactionalTest {

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();

        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderProduct.setRentTimeLength(6);
        orderProduct.setProductSkuId(40);
        orderProduct.setProductCount(5);
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProduct.setProductUnitAmount(new BigDecimal(20.0));
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderMaterial.setRentTimeLength(6);
        orderMaterial.setMaterialId(5);
        orderMaterial.setMaterialCount(3);
        orderMaterial.setInsuranceAmount(new BigDecimal(20));
        orderMaterial.setMaterialUnitAmount(new BigDecimal(18.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(15.0));

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerNo("C201711152010206581143");
        order.setCustomerConsignId(7);
        order.setRentStartTime(new Date());
        TestResult testResult = getJsonTestResult("/order/create", order);
    }

    @Test
    public void testCreateOrderJSON() throws Exception {
        String str = "{\n" +
                "  \"buyerCustomerNo\": \"LXCC10002018010800033\",\n" +
                "  \"rentStartTime\": 1516060800000,\n" +
                "  \"expectDeliveryTime\": 1516060800000,\n" +
                "  \"logisticsAmount\": \"100\",\n" +
                "  \"buyerRemark\": \"\",\n" +
                "  \"customerConsignId\": \"137\",\n" +
                "  \"highTaxRate\": \"12\",\n" +
                "  \"lowTaxRate\": \"88\",\n" +
                "  \"deliveryMode\": \"2\",\n" +
                "  \"orderProductList\": [\n" +
                "    {\n" +
                "      \"productId\": \"2000057\",\n" +
                "      \"productSkuId\": \"210\",\n" +
                "      \"productUnitAmount\": \"500\",\n" +
                "      \"productCount\": \"10\",\n" +
                "      \"rentType\": \"2\",\n" +
                "      \"rentTimeLength\": \"12\",\n" +
                "      \"insuranceAmount\": \"\",\n" +
                "      \"isNewProduct\": \"0\",\n" +
                "      \"depositAmount\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"productId\": \"2000057\",\n" +
                "      \"productSkuId\": \"210\",\n" +
                "      \"productUnitAmount\": \"500\",\n" +
                "      \"productCount\": \"10\",\n" +
                "      \"rentType\": \"2\",\n" +
                "      \"rentTimeLength\": \"12\",\n" +
                "      \"insuranceAmount\": \"\",\n" +
                "      \"isNewProduct\": \"1\",\n" +
                "      \"depositAmount\": 0\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Order order = JSONUtil.convertJSONToBean(str, Order.class);

        TestResult testResult = getJsonTestResult("/order/create", order);

    }

    @Test
    public void testUpdateOrderJSON() throws Exception {
        String str = "{\"orderNo\":\"LXO-20180116-701366-00068\",\"buyerCustomerNo\":\"LXCC10002018011200040\",\"rentStartTime\":1516147200000,\"expectDeliveryTime\":1516060800000,\"logisticsAmount\":\"80\",\"buyerRemark\":\"\",\"customerConsignId\":\"160\",\"highTaxRate\":\"18\",\"lowTaxRate\":\"82\",\"deliveryMode\":\"3\",\"orderProductList\":[{\"productId\":\"2000058\",\"productSkuId\":211,\"productUnitAmount\":\"10\",\"productCount\":\"10\",\"rentType\":\"1\",\"rentTimeLength\":\"10\",\"insuranceAmount\":\"\",\"payMode\":\"1\",\"isNewProduct\":0,\"depositAmount\":\"2000\"},{\"productId\":\"2000058\",\"productSkuId\":211,\"productUnitAmount\":\"9\",\"productCount\":\"9\",\"rentType\":\"2\",\"rentTimeLength\":\"10\",\"insuranceAmount\":\"\",\"isNewProduct\":1,\"depositAmount\":0}],\"orderMaterialList\":[{\"materialNo\":\"M201711201500267591516\",\"materialId\":\"9\",\"materialUnitAmount\":\"10\",\"materialCount\":\"1\",\"rentType\":\"1\",\"rentTimeLength\":\"10\",\"insuranceAmount\":\"\",\"payMode\":\"2\",\"isNewMaterial\":0,\"depositAmount\":\"100\"},{\"materialNo\":\"M201711201500267591516\",\"materialId\":\"9\",\"materialUnitAmount\":\"10\",\"materialCount\":\"1\",\"rentType\":\"2\",\"rentTimeLength\":\"10\",\"insuranceAmount\":\"\",\"isNewMaterial\":1,\"depositAmount\":0}]}";
        Order order = FastJsonUtil.toBean(str, Order.class);

        TestResult testResult = getJsonTestResult("/order/update", order);
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Order order = new Order();

        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderProduct.setRentTimeLength(6);
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct.setProductSkuId(40);
        orderProduct.setProductCount(1);
        orderProduct.setProductUnitAmount(new BigDecimal(20.0));
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderMaterial.setRentTimeLength(6);
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderMaterial.setMaterialId(5);
        orderMaterial.setMaterialCount(1);
        orderMaterial.setMaterialUnitAmount(new BigDecimal(18.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(15.0));

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setOrderNo("O201712111013379171126");
        order.setBuyerCustomerNo("C201711152010206581143");
        order.setCustomerConsignId(7);
        order.setRentStartTime(new Date());
        TestResult testResult = getJsonTestResult("/order/update", order);
    }

    @Test
    public void testCommitOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("LXO2017123070007200068");
        order.setVerifyUser(500006);//采购审核人员
        TestResult testResult = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712290930356291065");
        order.setVerifyUser(1);
        TestResult testResult = getJsonTestResult("/order/cancel", order);
    }

    @Test
    public void testIsNeedVerify() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712111523581951498");
        TestResult testResult = getJsonTestResult("/order/isNeedVerify", order);
    }

    @Test
    public void testProcessOrder() throws Exception {
        ProcessOrderParam processOrderParam = new ProcessOrderParam();
        processOrderParam.setOrderNo("LXO2018010470133700031");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null
        processOrderParam.setEquipmentNo("LX-E-4000001-2017122918884");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017120110037");
//        processOrderParam.setMaterialId(61);
//        processOrderParam.setMaterialCount(6);
        processOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/order/process", processOrderParam);
    }

    @Test
    public void testProcessOrderJson() throws Exception {
        String str = "{\"equipmentNo\":\"LX-E-4000001-2017122918883\",\"orderNo\":\"LXO2017123070005600071\",\"operationType\":1}\n";
        ProcessOrderParam processOrderParam = JSONUtil.convertJSONToBean(str, ProcessOrderParam.class);
        TestResult testResult = getJsonTestResult("/order/process", processOrderParam);
    }


    @Test
    public void testDelivery() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712291512335441732");
        TestResult testResult = getJsonTestResult("/order/delivery", order);
    }


    @Test
    public void queryAllOrder() throws Exception {
        OrderQueryParam param = new OrderQueryParam();
//        param.setBuyerRealName("荣焱");
        param.setIsPendingDelivery(1);
        TestResult testResult = getJsonTestResult("/order/queryAllOrder", param);
    }
    @Test
    public void queryAllOrderJSON() throws Exception {
        String str = "{\"pageNo\":1,\"pageSize\":15,\"orderNo\":\"\",\"buyerRealName\":\"\",\"createStartTime\":\"\",\"createEndTime\":\"\",\"createTimePicker\":\"\"}";
        OrderQueryParam param = FastJsonUtil.toBean(str,OrderQueryParam.class);
//        param.setBuyerRealName("荣焱");
        TestResult testResult = getJsonTestResult("/order/queryAllOrder", param);
    }


    @Test
    public void queryLastPrice() throws Exception {
        LastRentPriceRequest request = new LastRentPriceRequest();
        request.setCustomerNo("CP201712060843154191841");
        request.setProductSkuId(40);
        request.setMaterialId(5);

        TestResult testResult = getJsonTestResult("/order/queryLastPrice", request);
    }


    @Test
    public void queryOrderByNo() throws Exception {
        Order order = new Order();
        order.setOrderNo("LXO2018010970137000019");
        TestResult testResult = getJsonTestResult("/order/queryOrderByNo", order);
    }

    @Test
    public void pay() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712161358517931662");
        TestResult testResult = getJsonTestResult("/order/pay", order);
    }


    @Test
    public void returnEquipment() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", "O201712121749510561848");
        map.put("returnEquipmentNo", "LX-EQUIPMENT-4000001-2017120110015");
        map.put("changeEquipmentNo", "LX-EQUIPMENT-4000001-2017120110015");
        TestResult testResult = getJsonTestResult("/order/returnEquipment", map);
    }
}
