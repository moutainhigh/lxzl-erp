package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import org.junit.Test;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:03 2018/7/16
 * @Modified By:
 */
public class WorkbenchControllerTest extends ERPUnTransactionalTest {

    /**
     * 审核中的订单 4
     * 待发货的订单 8
     *
     * */
    @Test
    public void queryOrderTest() throws Exception{
        OrderQueryParam param = new OrderQueryParam();
        param.setOrderStatus(4);
//        param.setOrderStatus(8);

        TestResult testResult = getJsonTestResult("/workbench/queryOrder", param);
    }

    /** 到期未处理的订单 */
    @Test
    public void isReturnOverDueOrderTest() throws Exception{
        OrderQueryParam param = new OrderQueryParam();
        param.setIsReturnOverDue(1);

        TestResult testResult = getJsonTestResult("/workbench/queryOrder", param);
    }

    /** 可续租的订单 */

    /**
     * 未提交的退货单 0
     * 审核中的退货单 4
     * 处理中的退货单 12
     * 被驳回的退货单 24
     * */
    @Test
    public void queryReturnOrderTest() throws Exception{
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        param.setReturnOrderStatus(0);

        TestResult testResult = getJsonTestResult("/workbench/queryReturnOrder", param);
    }



}

