package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.order.*;
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
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("LXO-20180308-1000-00056");
        TestResult testResult = getJsonTestResult("/order/cancel", order);

    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();

        order.setDeliveryMode(DeliveryMode.DELIVERY_MODE_EXPRESS);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("2018.3.22 18:52 测试");
        order.setRentStartTime(new Date());
        order.setExpectDeliveryTime(new Date());
        order.setOrderSubCompanyId(8);
//        order.setDeliverySubCompanyId(1);

        order.setRentType(OrderRentType.RENT_TYPE_DAY);
        order.setRentTimeLength(6);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct.setProductSkuId(218);
        orderProduct.setProductCount(2);
        orderProduct.setIsNewProduct(0);
//        orderProduct.setInsuranceAmount(new BigDecimal(33.33333));
        orderProduct.setProductUnitAmount(new BigDecimal(16.66666));
//        orderProduct.setInsuranceAmount(new BigDecimal(33.33333));
        orderProduct.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct.setDepositAmount(new BigDecimal(100.00));
        orderProductList.add(orderProduct);

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct1.setProductSkuId(218);
        orderProduct1.setProductCount(5);
        orderProduct1.setIsNewProduct(0);
        orderProduct1.setInsuranceAmount(new BigDecimal(33.33333));
        orderProduct1.setProductUnitAmount(new BigDecimal(33.33333));
        orderProduct1.setInsuranceAmount(new BigDecimal(33.33333));
        orderProduct1.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct1.setDepositAmount(new BigDecimal(600.0));
//        orderProductList.add(orderProduct1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct2.setProductSkuId(218);
        orderProduct2.setProductCount(5);
        orderProduct2.setIsNewProduct(1);
        orderProduct2.setInsuranceAmount(new BigDecimal(600.0));
        orderProduct2.setProductUnitAmount(new BigDecimal(600.0));
        orderProduct2.setInsuranceAmount(new BigDecimal(600.0));
        orderProduct2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct2.setDepositAmount(new BigDecimal(600.0));
//        orderProductList.add(orderProduct2);

        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setMaterialId(40);
        orderMaterial.setMaterialCount(3);
        orderMaterial.setIsNewMaterial(1);
        orderMaterial.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial.setDepositAmount(new BigDecimal("30"));
        orderMaterialList.add(orderMaterial);

        OrderMaterial orderMaterial1 = new OrderMaterial();
        orderMaterial1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial1.setMaterialId(40);
        orderMaterial1.setMaterialCount(3);
        orderMaterial1.setIsNewMaterial(0);
        orderMaterial1.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial1.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterial1.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial1.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial1.setDepositAmount(new BigDecimal("30"));
//        orderMaterialList.add(orderMaterial1);

        OrderMaterial orderMaterial2 = new OrderMaterial();
        orderMaterial2.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial2.setMaterialId(40);
        orderMaterial2.setMaterialCount(3);
        orderMaterial2.setIsNewMaterial(1);
        orderMaterial2.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial2.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterial2.setInsuranceAmount(new BigDecimal(600.0));
        orderMaterial2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial2.setDepositAmount(new BigDecimal("30"));
//        orderMaterialList.add(orderMaterial2);

//        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerNo("LXCC-027-20180322-00784");
        order.setCustomerConsignId(5445);
        order.setRentStartTime(new Date());
        order.setIsPeer(0);
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
        order.setDeliveryMode(DeliveryMode.DELIVERY_MODE_EXPRESS);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("2018.3.21 14:52 测试");
        order.setRentStartTime(new Date());
        order.setExpectDeliveryTime(new Date());
        order.setOrderSubCompanyId(8);
//        order.setDeliverySubCompanyId(1);

        order.setRentType(OrderRentType.RENT_TYPE_MONTH);
        order.setRentTimeLength(20);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct.setOrderProductId(565);
        orderProduct.setProductSkuId(216);
        orderProduct.setProductCount(5);
        orderProduct.setIsNewProduct(1);
        orderProduct.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct.setProductUnitAmount(new BigDecimal(1000.0));
        orderProduct.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct.setDepositAmount(new BigDecimal(1000.0));
        orderProductList.add(orderProduct);

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct1.setOrderProductId(564);
        orderProduct1.setProductSkuId(216);
        orderProduct1.setProductCount(5);
        orderProduct1.setIsNewProduct(0);
        orderProduct1.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct1.setProductUnitAmount(new BigDecimal(1000.0));
        orderProduct1.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct1.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct1.setDepositAmount(new BigDecimal(1000.0));
        orderProductList.add(orderProduct1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct2.setProductSkuId(216);
        orderProduct2.setProductCount(4);
        orderProduct2.setIsNewProduct(1);
        orderProduct2.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct2.setProductUnitAmount(new BigDecimal(1000.0));
        orderProduct2.setInsuranceAmount(new BigDecimal(1000.0));
        orderProduct2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct2.setDepositAmount(new BigDecimal(1000.0));
        orderProductList.add(orderProduct2);

        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderMaterial.setOrderMaterialId(385);
        orderMaterial.setMaterialId(40);
        orderMaterial.setMaterialCount(10);
        orderMaterial.setIsNewMaterial(1);
        orderMaterial.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial.setMaterialUnitAmount(new BigDecimal(100.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial.setDepositAmount(new BigDecimal(30.0));
        orderMaterialList.add(orderMaterial);

        OrderMaterial orderMaterial1 = new OrderMaterial();
        orderMaterial1.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderMaterial1.setOrderMaterialId(384);
        orderMaterial1.setMaterialId(40);
        orderMaterial1.setMaterialCount(2);
        orderMaterial1.setIsNewMaterial(0);
        orderMaterial1.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial1.setMaterialUnitAmount(new BigDecimal(100.0));
        orderMaterial1.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial1.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial1.setDepositAmount(new BigDecimal(30.0));
        orderMaterialList.add(orderMaterial1);

        OrderMaterial orderMaterial2 = new OrderMaterial();
        orderMaterial2.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderMaterial2.setMaterialId(40);
        orderMaterial2.setMaterialCount(5);
        orderMaterial2.setIsNewMaterial(1);
        orderMaterial2.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial2.setMaterialUnitAmount(new BigDecimal(100.0));
        orderMaterial2.setInsuranceAmount(new BigDecimal(100.0));
        orderMaterial2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial2.setDepositAmount(new BigDecimal(30.0));
        orderMaterialList.add(orderMaterial2);

        order.setOrderMaterialList(orderMaterialList);

        order.setOrderNo("LXO-20180404-1000-00005");
        order.setBuyerCustomerNo("LXCC-027-20180322-00784");
        order.setCustomerConsignId(5445);
        order.setRentStartTime(new Date());
        order.setIsPeer(0);
        TestResult testResult = getJsonTestResult("/order/update", order);
    }

    @Test
    public void testCommitOrder() throws Exception {
        OrderCommitParam order = new OrderCommitParam();
        order.setOrderNo("LXO-20180328-027-01290");
        order.setVerifyUser(500228);//采购审核人
        List<Integer> imageList = new ArrayList<>();
        imageList.add(1818);
        order.setImgIdList(imageList);
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
    public void testforceCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712290930356291065");
        order.setVerifyUser(1);
        TestResult testResult = getJsonTestResult("/order/forceCancel", order);
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
//        param.setOrderId(1);
//        param.setBuyerRealName("二零一八三月");
//        param.setBuyerRealName("荣焱");
//        param.setIsPendingDelivery(1);
//        param.setOrderNo("LXO-20180307-1000-00014");
//        param.setDeliverySubCompanyId(2);
//        param.setOrderStatus(16);
        param.setEndRentStratTime(new Date());
//        param.setPayStatus(24);
        TestResult testResult = getJsonTestResult("/order/queryAllOrder", param);
    }
    @Test
    public void queryAllOrderJSON() throws Exception {
        String str = "{\"pageNo\":1,\"pageSize\":15,\"orderNo\":\"LXO-20180307-1000-00014\",\"buyerRealName\":\"\",\"createStartTime\":\"\",\"createEndTime\":\"\",\"createTimePicker\":\"\"}";
        OrderQueryParam param = FastJsonUtil.toBean(str,OrderQueryParam.class);
//        param.setBuyerRealName("荣焱");
        TestResult testResult = getJsonTestResult("/order/queryAllOrder", param);
    }


    @Test
    public void queryLastPrice() throws Exception {
        LastRentPriceRequest request = new LastRentPriceRequest();
        request.setCustomerNo("LXCD-1000-20180306-00302");
        request.setProductSkuId(219);
        request.setMaterialId(228);

        TestResult testResult = getJsonTestResult("/order/queryLastPrice", request);
    }


    @Test
    public void queryOrderByNo() throws Exception {
        Order order = new Order();
        //order.setOrderNo("LXO-20180305-027-00005");
        order.setOrderNo("LXO-20180305-0755-00028");
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

    @Test
    public void createOrderFirstPayAmount() throws Exception {
        Order order = new Order();

        order.setDeliveryMode(DeliveryMode.DELIVERY_MODE_EXPRESS);
        order.setLogisticsAmount(new BigDecimal(0.0));
        order.setBuyerRemark("2018.5.5 09:27 测试");
        order.setRentStartTime(new Date());
        order.setExpectDeliveryTime(new Date());
        order.setOrderSubCompanyId(8);

        order.setRentType(OrderRentType.RENT_TYPE_DAY);
        order.setRentTimeLength(5);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setSerialNumber("1");
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT);
        orderProduct.setProductSkuId(40);
        orderProduct.setIsNewProduct(1);
        orderProduct.setProductCount(1);
        orderProduct.setInsuranceAmount(new BigDecimal(0.0));
        orderProduct.setProductUnitAmount(new BigDecimal(10.0));
        orderProduct.setInsuranceAmount(new BigDecimal(0.0));
        orderProduct.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct.setDepositAmount(new BigDecimal(0.0));
        orderProductList.add(orderProduct);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setSerialNumber("2");
        orderProduct2.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT);
        orderProduct2.setProductSkuId(40);
        orderProduct2.setIsNewProduct(1);
        orderProduct2.setProductCount(1);
        orderProduct2.setInsuranceAmount(new BigDecimal(0.0));
        orderProduct2.setProductUnitAmount(new BigDecimal(10.0));
        orderProduct2.setInsuranceAmount(new BigDecimal(0.0));
        orderProduct2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderProduct2.setDepositAmount(new BigDecimal(0.0));
        orderProductList.add(orderProduct2);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setSerialNumber("1");
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setMaterialId(12);
        orderMaterial.setIsNewMaterial(1);
        orderMaterial.setMaterialCount(5);
        orderMaterial.setInsuranceAmount(new BigDecimal(1200));
        orderMaterial.setMaterialUnitAmount(new BigDecimal(1200.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(1200.0));
        orderMaterial.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial.setDepositAmount(new BigDecimal(1200.0));
        orderMaterialList.add(orderMaterial);

        OrderMaterial orderMaterial2 = new OrderMaterial();
        orderMaterial2.setSerialNumber("2");
        orderMaterial2.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial2.setMaterialId(12);
        orderMaterial2.setIsNewMaterial(1);
        orderMaterial2.setMaterialCount(5);
        orderMaterial2.setInsuranceAmount(new BigDecimal(1200));
        orderMaterial2.setMaterialUnitAmount(new BigDecimal(1200.0));
        orderMaterial2.setInsuranceAmount(new BigDecimal(1200.0));
        orderMaterial2.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        orderMaterial2.setDepositAmount(new BigDecimal(1200.0));
        orderMaterialList.add(orderMaterial2);
        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerNo("LXCC-2000-20180328-00823");
        order.setRentStartTime(new Date());
        TestResult testResult = getJsonTestResult("/order/createOrderFirstPayAmount", order);
    }
    @Test
    public void queryVerifyOrder() throws Exception {
        VerifyOrderQueryParam param =new VerifyOrderQueryParam();
//        param.setOrderId(3001219);
//        param.setPageNo(2);
//        param.setPageSize(5);
//        param.setBuyerRealName("无锡博蓝广告传播");
//        param.setBuyerRealName("深圳市点时");
//        param.setIsPendingDelivery(1);
//        param.setOrderNo("LXO-20180307-1000-00014");
//        param.setDeliverySubCompanyId(2);
//        param.setOrderStatus(16);

        TestResult testResult = getJsonTestResult("/order/queryVerifyOrder", param);
    }
}
