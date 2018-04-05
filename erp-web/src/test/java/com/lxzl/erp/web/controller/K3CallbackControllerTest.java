package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-11 15:23
 */
public class K3CallbackControllerTest extends ERPTransactionalTest {


    @Test
    public void addMaterial() throws Exception {
        String str = "{\"deliveryOrderMaterialList\":[{\"deliveryMaterialCount\":4,\"isNew\":0,\"orderMaterialId\":1284,\"remark\":\"无 \"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1285,\"remark\":\"无\"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1286,\"remark\":\"无\"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1287,\"remark\":\"无\"}],\"deliveryOrderProductList\":[{\"deliveryProductSkuCount\":2,\"isNew\":0,\"orderProductId\":630,\"remark\":\"无\"},{\"deliveryProductSkuCount\":2,\"isNew\":0,\"orderProductId\":631,\"remark\":\"无\"}],\"deliveryTimeStr\":\"2018/3/10 0:00:00\",\"deliveryUser\":\"黄颂裕\",\"orderNo\":\"LXO-20180310-0755-00443\",\"subCompanyCode\":\"01\"}";

        DeliveryOrder deliveryOrder = JSON.parseObject(str,DeliveryOrder.class );
        TestResult testResult = getJsonTestResult("/k3Callback/receiveDeliveryInfo",deliveryOrder);
    }
    @Test
    public void callbackReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180328035118307");
        k3ReturnOrder.setUpdateUserRealName("喻晓艳");

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setOrderNo("LXO-20180328-1000-01286");
        k3ReturnOrderDetail.setOrderItemId("1953");
        k3ReturnOrderDetail.setRealProductCount(666);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail);

        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);
        TestResult testResult = getJsonTestResult("/k3Callback/callbackReturnOrder",k3ReturnOrder);
    }
}
