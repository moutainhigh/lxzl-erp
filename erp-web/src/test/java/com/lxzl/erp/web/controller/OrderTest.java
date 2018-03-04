package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.order.LastRentPriceRequest;
import com.lxzl.erp.common.domain.order.OrderCommitParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.ProcessOrderParam;
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
public class OrderTest extends ERPTransactionalTest {

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();

        order.setDeliveryMode(DeliveryMode.DELIVERY_MODE_EXPRESS);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");
        order.setRentStartTime(new Date());
        order.setExpectDeliveryTime(new Date());

        order.setRentType(OrderRentType.RENT_TYPE_DAY);
        order.setRentTimeLength(6);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct.setProductSkuId(222);
        orderProduct.setProductCount(5);
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProduct.setProductUnitAmount(new BigDecimal(20.0));
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProduct.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setMaterialId(78);
        orderMaterial.setMaterialCount(3);
        orderMaterial.setInsuranceAmount(new BigDecimal(20));
        orderMaterial.setMaterialUnitAmount(new BigDecimal(18.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(15.0));
        orderMaterial.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerNo("LXCD10002017123000034");
        order.setCustomerConsignId(89);
        order.setRentStartTime(new Date());
        TestResult testResult = getJsonTestResult("/order/create", order);
    }

    @Test
    public void testCreateOrderJSON() throws Exception {
        String str = "{\"buyerCustomerNo\":\"LXCC-1000-20180226-00136\",\"rentStartTime\":1520121600000,\"expectDeliveryTime\":1520121600000,\"logisticsAmount\":\"12\",\"buyerRemark\":\"\",\"customerConsignId\":\"23284\",\"highTaxRate\":\"2\",\"lowTaxRate\":\"98\",\"deliveryMode\":\"1\",\"rentType\":\"2\",\"rentTimeLength\":\"2\",\"orderProductList\":[{\"productId\":\"2000075\",\"productSkuId\":242,\"productUnitAmount\":\"500\",\"productCount\":\"1\",\"rentTimeLength\":\"\",\"insuranceAmount\":\"\",\"isNewProduct\":0,\"depositAmount\":0}],\"orderMaterialList\":[{\"materialId\":\"75\",\"materialUnitAmount\":\"200\",\"materialCount\":\"2\",\"rentTimeLength\":\"\",\"insuranceAmount\":\"\",\"isNewMaterial\":0,\"depositAmount\":0}]}";
        Order order = JSONUtil.convertJSONToBean(str, Order.class);

        TestResult testResult = getJsonTestResult("/order/create", order);

    }

    @Test
    public void testUpdateOrderJSON() throws Exception {
        String str = "{\"orderNo\":\"LXO-20180119-731443-00066\",\"buyerCustomerNo\":\"LXCC-1000-20180119-13729\",\"rentStartTime\":1514764800000,\"expectDeliveryTime\":1514764800000,\"logisticsAmount\":\"0\",\"buyerRemark\":\"\",\"customerConsignId\":\"22907\",\"highTaxRate\":\"25\",\"lowTaxRate\":\"75\",\"deliveryMode\":\"2\",\"orderProductList\":[{\"productId\":\"2000055\",\"productSkuId\":210,\"productUnitAmount\":\"100\",\"productCount\":\"1\",\"rentType\":\"2\",\"rentTimeLength\":\"3\",\"insuranceAmount\":\"\",\"isNewProduct\":0,\"depositAmount\":0},{\"productId\":\"2000055\",\"productSkuId\":210,\"productUnitAmount\":\"100\",\"productCount\":\"1\",\"rentType\":\"1\",\"rentTimeLength\":\"10\",\"insuranceAmount\":\"\",\"payMode\":\"3\",\"isNewProduct\":1,\"depositAmount\":\"100\"}]}";
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
        order.setOrderNo("LXO-20180304-704187-00001");
        order.setVerifyUser(500006);//采购审核人员
        TestResult testResult = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCommitOrderJSON() throws Exception {
        String str = "{\n" +
                "\t\"orderNo\": \"LXO-20180227-701528-00019\",\n" +
                "\t\"verifyUser\": \"500006\",\n" +
                "\t\"commitRemark\": \"提交订单\",\n" +
                "\t\"imgIdList\": [414]\n" +
                "}";

        OrderCommitParam orderCommitParam = JSON.parseObject(str, OrderCommitParam.class);
        TestResult testResult = getJsonTestResult("/order/commit", orderCommitParam);
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
        processOrderParam.setOrderNo("LXO-20180127-729331-00106");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null AND is_new = 0
//        processOrderParam.setEquipmentNo("LX-E-4000001-2017122918884");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017120110037");
        processOrderParam.setMaterialId(76);
        processOrderParam.setMaterialCount(10);
        processOrderParam.setIsNewMaterial(0);
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
        order.setOrderNo("LXO-20180301-731846-00002");
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
