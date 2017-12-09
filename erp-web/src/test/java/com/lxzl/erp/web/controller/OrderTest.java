package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.ProcessOrderParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        order.setPayMode(2);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderProduct.setRentTimeLength(6);
        orderProduct.setProductSkuId(40);
        orderProduct.setProductCount(2);
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProduct.setProductUnitAmount(new BigDecimal(20.0));
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
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
        TestResult result = getJsonTestResult("/order/create", order);
    }

    @Test
    public void testCreateOrderJSON() throws Exception{
        String str = "{\n" +
                "\t\"buyerCustomerNo\": \"CC201712071948510861631\",\n" +
                "\t\"rentStartTime\": 1512691200000,\n" +
                "\t\"payMode\": \"1\",\n" +
                "\t\"logisticsAmount\": \"98\",\n" +
                "\t\"orderSellerId\": 500013,\n" +
                "\t\"orderSubCompanyId\": 1,\n" +
                "\t\"buyerRemark\": \"第一次下单\",\n" +
                "\t\"customerConsignId\": \"31\",\n" +
                "\t\"orderProductList\": [{\n" +
                "\t\t\"productId\": \"2000013\",\n" +
                "\t\t\"productSkuId\": \"40\",\n" +
                "\t\t\"productUnitAmount\": \"100\",\n" +
                "\t\t\"productCount\": \"10\",\n" +
                "\t\t\"rentType\": \"2\",\n" +
                "\t\t\"rentTimeLength\": \"10\",\n" +
                "\t\t\"insuranceAmount\": \"10\"\n" +
                "\t}]\n" +
                "}";
        Order order= JSONUtil.convertJSONToBean(str, Order.class);

        TestResult result = getJsonTestResult("/order/create", order);

    }

    @Test
    public void testUpdateOrder() throws Exception {
        Order order = new Order();

        order.setPayMode(2);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderProduct.setRentTimeLength(6);
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
        orderMaterial.setMaterialId(5);
        orderMaterial.setMaterialCount(1);
        orderMaterial.setMaterialUnitAmount(new BigDecimal(18.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(15.0));

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setOrderNo("O201712071453234351744");
        order.setBuyerCustomerNo("C201711152010206581143");
        order.setCustomerConsignId(7);
        order.setRentStartTime(new Date());
        TestResult result = getJsonTestResult("/order/update", order);
    }

    @Test
    public void testCommitOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712051948457121036");
        order.setVerifyUser(500006);
        TestResult result = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711151901080841608");
        order.setVerifyUser(1);
        TestResult result = getJsonTestResult("/order/cancel", order);
    }

    @Test
    public void testIsNeedVerify() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712071453234351744");
        TestResult result = getJsonTestResult("/order/isNeedVerify", order);
    }

    @Test
    public void testProcessOrder() throws Exception {
        ProcessOrderParam processOrderParam = new ProcessOrderParam();
        processOrderParam.setOrderNo("O201712051948457121036");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017120110036");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017120110037");
        processOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult result = getJsonTestResult("/order/process", processOrderParam);
    }


    @Test
    public void testDelivery() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712051948457121036");
        TestResult result = getJsonTestResult("/order/delivery", order);
    }


    @Test
    public void queryAllOrder() throws Exception {
        OrderQueryParam param = new OrderQueryParam();

        TestResult result = getJsonTestResult("/order/queryAllOrder", param);
    }


    @Test
    public void queryOrderByNo() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712091506212631627");
        TestResult result = getJsonTestResult("/order/queryOrderByNo", order);
    }
}
