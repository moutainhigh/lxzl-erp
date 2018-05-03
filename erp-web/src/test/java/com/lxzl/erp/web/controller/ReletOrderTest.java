package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 续租单测试
 *
 * @author ZhaoZiXuan
 * @date 2018/4/24 9:53
 */
public class ReletOrderTest extends ERPUnTransactionalTest {


    @Test
    public void testCreateReletByOrderNo() throws  Exception{
        Order order = new Order();
        order.setOrderNo("LXO-20180305-2000-00032");
        TestResult testResult = getJsonTestResult("/reletOrder/create", order);
    }


    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
//        order.setOrderId(3000010);
//        order.setOrderNo("LXO-20180305-027-00010");
//
//        order.setBuyerCustomerNo("LXCP-027-20180315-00669"); //客户编号
        order.setBuyerRemark("2018.4.25 16:52 测试zzx");

//        order.setOrderId(3001287);
//        order.setOrderNo("LXO-20180328-1000-01286");
//
//        order.setBuyerCustomerNo("LXCC-0755-20180112-00001"); //客户编号
//        order.setBuyerRemark("2018.4.25 16:52 测试zzx");


        order.setRentType(OrderRentType.RENT_TYPE_DAY); //租赁类型
        order.setRentTimeLength(6);//租赁时长



        order.setOrderId(3000005);
        order.setOrderNo("LXO-20180305-027-00005");

        order.setBuyerCustomerNo("LXCC-027-20180305-00004"); //客户编号
//        order.setBuyerRemark("2018.4.25 16:52 测试zzx");


        order.setRentStartTime(new Date());
        order.setRentStartTime(new Date());

        order.setOrderSubCompanyId(8);  //订单所属分公司
        order.setDeliverySubCompanyId(8); //订单发货分公司


        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(9);
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct.setProductSkuId(104);
        orderProduct.setProductCount(2);
        orderProduct.setIsNewProduct(0);

        orderProduct.setRentingProductCount(2);
        orderProduct.setProductUnitAmount(new BigDecimal(16.66666));
        orderProductList.add(orderProduct);

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setOrderProductId(9);
        orderProduct1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct1.setProductSkuId(218);
        orderProduct1.setProductCount(5);
        orderProduct1.setIsNewProduct(0);

        orderProduct1.setRentingProductCount(3);
        orderProduct1.setProductUnitAmount(new BigDecimal(33.33333));
        orderProductList.add(orderProduct1);

        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setOrderMaterialId(41);
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setMaterialId(40);
        orderMaterial.setMaterialCount(3);
        orderMaterial.setIsNewMaterial(1);

        orderMaterial.setRentingMaterialCount(3);
        orderMaterial.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterialList.add(orderMaterial);

        OrderMaterial orderMaterial1 = new OrderMaterial();
        orderMaterial1.setOrderMaterialId(41);
        orderMaterial1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial1.setMaterialId(40);
        orderMaterial1.setMaterialCount(3);
        orderMaterial1.setIsNewMaterial(0);

        orderMaterial1.setRentingMaterialCount(3);
        orderMaterial1.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterialList.add(orderMaterial1);

        order.setOrderMaterialList(orderMaterialList);


        TestResult testResult = getJsonTestResult("/reletOrder/create", order);
    }






    @Test
    public void testQueryAllReletOrder() throws Exception{
        ReletOrderQueryParam reletOrderQueryParam = new ReletOrderQueryParam();
 //       reletOrderQueryParam.setBuyerCustomerId(704447);
//        reletOrderQueryParam.setBuyerCustomerNo("LXO-20180305-0755-00028");
//        reletOrderQueryParam.setBuyerRealName("湖北华天翼建设工程有限公司");
////        reletOrderQueryParam.setCreateEndTime();
////        reletOrderQueryParam.setCreateStartTime();
//        reletOrderQueryParam.setOrderId(3000005);
//        reletOrderQueryParam.setOrderNo("LXO-20180305-027-00010");
//
//        //reletOrderQueryParam.setOrderStatus(0);
//        List<Integer> statusList = new ArrayList<>();
//        statusList.add(0);
//        statusList.add(8);
//        reletOrderQueryParam.setOrderStatusList(statusList);
//
//        reletOrderQueryParam.setOrderSellerId(500038);
//        reletOrderQueryParam.setPayStatus(0);
//
//        reletOrderQueryParam.setSubCompanyId(1);
//        reletOrderQueryParam.setDeliverySubCompanyId(8);
        TestResult testResult = getJsonTestResult("/reletOrder/page", reletOrderQueryParam);
    }


    @Test
    public void testQueryReletOrderDetailById() throws Exception{
        ReletOrderQueryParam reletOrderQueryParam = new ReletOrderQueryParam();
//        reletOrderQueryParam.setBuyerCustomerId(704200);
//        reletOrderQueryParam.setBuyerCustomerNo("LXO-20180305-0755-00028");
//        reletOrderQueryParam.setBuyerRealName("湖北华天翼建设工程有限公司");
////        reletOrderQueryParam.setCreateEndTime();
////        reletOrderQueryParam.setCreateStartTime();
//        reletOrderQueryParam.setOrderId(3000005);

        reletOrderQueryParam.setReletOrderId(18);
//
//        //reletOrderQueryParam.setOrderStatus(0);
//        List<Integer> statusList = new ArrayList<>();
//        statusList.add(0);
//        statusList.add(8);
//        reletOrderQueryParam.setOrderStatusList(statusList);
//
//        reletOrderQueryParam.setOrderSellerId(500038);
//        reletOrderQueryParam.setPayStatus(0);
//
//        reletOrderQueryParam.setSubCompanyId(1);
//        reletOrderQueryParam.setDeliverySubCompanyId(8);
        TestResult testResult = getJsonTestResult("/reletOrder/queryReletOrderDetailById", reletOrderQueryParam);
    }

}
