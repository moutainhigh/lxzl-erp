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
                "\t\"buyerCustomerNo\": \"CP201712060843154191841\",\n" +
                "\t\"rentStartTime\": 1513036800000,\n" +
                "\t\"logisticsAmount\": \"98\",\n" +
                "\t\"orderSellerId\": 500005,\n" +
                "\t\"orderSubCompanyId\": 1,\n" +
                "\t\"buyerRemark\": \"急用！\",\n" +
                "\t\"customerConsignId\": \"37\",\n" +
                "\t\"orderProductList\": [{\n" +
                "\t\t\"productId\": \"2000013\",\n" +
                "\t\t\"productSkuId\": \"40\",\n" +
                "\t\t\"productUnitAmount\": \"100\",\n" +
                "\t\t\"productCount\": \"1\",\n" +
                "\t\t\"rentType\": \"3\",\n" +
                "\t\t\"rentTimeLength\": \"10\",\n" +
                "\t\t\"insuranceAmount\": \"1000\",\n" +
                "\t\t\"payMode\": \"2\"\n" +
                "\t}],\n" +
                "\t\"orderMaterialList\": [{\n" +
                "\t\t\"materialId\": \"5\",\n" +
                "\t\t\"materialUnitAmount\": \"100\",\n" +
                "\t\t\"materialCount\": \"1\",\n" +
                "\t\t\"rentType\": \"3\",\n" +
                "\t\t\"rentTimeLength\": \"10\",\n" +
                "\t\t\"insuranceAmount\": \"1000\",\n" +
                "\t\t\"payMode\": \"2\"\n" +
                "\t}]\n" +
                "}";
        Order order = JSONUtil.convertJSONToBean(str, Order.class);

        TestResult testResult = getJsonTestResult("/order/create", order);

    }
    @Test
    public void testUpdateOrderJSON() throws Exception {
        String str = "{\"orderNo\":\"O201712171440529091845\",\"buyerCustomerNo\":\"CC201712091546467081096\",\"rentStartTime\":1513555200000,\"logisticsAmount\":\"1\",\"orderSellerId\":500001,\"orderSubCompanyId\":5,\"buyerRemark\":\"包装好好打着，别弄坏了\",\"customerConsignId\":\"40\",\"orderProductList\":[{\"productId\":\"2000013\",\"productSkuId\":\"40\",\"productUnitAmount\":\"150\",\"productCount\":\"2\",\"rentType\":\"2\",\"rentTimeLength\":\"12\",\"insuranceAmount\":\"1\",\"payMode\":\"2\"}]}";
        Order order = JSONUtil.convertJSONToBean(str, Order.class);

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
        order.setOrderNo("O201712211337503711677");
        order.setVerifyUser(500006);//采购审核人员
        TestResult testResult = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711151901080841608");
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
        processOrderParam.setOrderNo("O201712161345332381669");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010012");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017120110037");
        processOrderParam.setMaterialId(24);
        processOrderParam.setMaterialCount(2);
        processOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/order/process", processOrderParam);
    }

    @Test
    public void testProcessOrderJson() throws Exception{
        String str = "{\"equipmentNo\":\"LX-EQUIPMENT-4000001-2017122210000\",\"orderNo\":\"O201712222007232111047\",\"operationType\":1}\n";
        ProcessOrderParam processOrderParam = JSONUtil.convertJSONToBean(str,ProcessOrderParam.class );
        TestResult testResult = getJsonTestResult("/order/process", processOrderParam);
    }


    @Test
    public void testDelivery() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712151714072971428");
        TestResult testResult = getJsonTestResult("/order/delivery", order);
    }


    @Test
    public void queryAllOrder() throws Exception {
        OrderQueryParam param = new OrderQueryParam();
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
        order.setOrderNo("O201712161345332381669");
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
