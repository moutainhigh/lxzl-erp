package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.core.service.statement.StatementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
        k3ReturnOrder.setK3CustomerNo("10.10201608030004");
        k3ReturnOrder.setK3CustomerName("上海紫盛网络科技有限公司");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂");
        k3ReturnOrder.setReturnContacts("宋老三");
        k3ReturnOrder.setReturnPhone("13809908800");
        k3ReturnOrder.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL);

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
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setReturnOrderId(1);
        k3ReturnOrderDetail.setOrderNo("O4562789");
        k3ReturnOrderDetail.setOrderEntry("1");
        k3ReturnOrderDetail.setProductNo("10.123.1228.156");
        k3ReturnOrderDetail.setProductCount(1);
        k3ReturnOrderDetail.setRemark("123456");

        TestResult testResult = getJsonTestResult("/k3/addReturnOrder", k3ReturnOrderDetail);
    }

    @Test
    public void deleteReturnOrder() throws Exception {
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setK3ReturnOrderDetailId(2);
        TestResult testResult = getJsonTestResult("/k3/deleteReturnOrder", k3ReturnOrderDetail);
    }

    @Test
    public void sendToK3() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("6f52d4caa5a643109d7ccca400218d0c");
        TestResult testResult = getJsonTestResult("/k3/sendToK3", k3ReturnOrder);
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

    @Autowired
    private StatementService statementService;

}
