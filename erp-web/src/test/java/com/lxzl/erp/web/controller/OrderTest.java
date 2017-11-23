package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
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

        order.setRentType(OrderRentType.RENT_TYPE_DAY);
        order.setRentTimeLength(6);
        order.setPayMode(2);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(2000013);
        orderProduct.setProductSkuId(40);
        orderProduct.setProductCount(1);

        List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
        ProductSkuProperty productSkuProperty = new ProductSkuProperty();
        productSkuProperty.setPropertyId(1);
        productSkuProperty.setPropertyValueId(2);
        productSkuPropertyList.add(productSkuProperty);
        ProductSkuProperty productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(2);
        productSkuProperty2.setPropertyValueId(5);
        productSkuPropertyList.add(productSkuProperty2);
        productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(5);
        productSkuProperty2.setPropertyValueId(11);
        productSkuPropertyList.add(productSkuProperty2);
        productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(6);
        productSkuProperty2.setPropertyValueId(13);
        productSkuPropertyList.add(productSkuProperty2);
        productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(7);
        productSkuProperty2.setPropertyValueId(15);
        productSkuPropertyList.add(productSkuProperty2);
        orderProduct.setProductSkuPropertyList(productSkuPropertyList);
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setMaterialId(5);
        orderMaterial.setMaterialCount(1);

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerId(1);
        order.setCustomerConsignId(1);
        order.setRentStartTime(new Date());
        TestResult result = getJsonTestResult("/order/create", order);
    }

    @Test
    public void testCommitOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711151901080841608");
        order.setVerifyUser(1);
        TestResult result = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711151901080841608");
        order.setVerifyUser(1);
        TestResult result = getJsonTestResult("/order/cancel", order);
    }
}
