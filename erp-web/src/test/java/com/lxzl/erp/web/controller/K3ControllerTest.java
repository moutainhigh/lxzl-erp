package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import org.junit.Test;

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
    public void createReturnOrder() throws Exception {
        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setK3CustomerNo("123456789");
        k3ReturnOrder.setK3CustomerName("一个大公司客户");
        k3ReturnOrder.setReturnTime(new Date());
        k3ReturnOrder.setReturnAddress("北京京西蓝靛厂");
        k3ReturnOrder.setReturnContacts("张三");
        k3ReturnOrder.setReturnPhone("13809908800");

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();

        K3ReturnOrderDetail k3ReturnOrderDetail1 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail1.setOrderNo("O456789");
        k3ReturnOrderDetail1.setOrderEntry("1");
        k3ReturnOrderDetail1.setProductNo("10.123.1258.156");
        k3ReturnOrderDetail1.setProductCount(1);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail1);

        K3ReturnOrderDetail k3ReturnOrderDetail2 = new K3ReturnOrderDetail();
        k3ReturnOrderDetail2.setOrderNo("O45678qqq9");
        k3ReturnOrderDetail2.setOrderEntry("1");
        k3ReturnOrderDetail2.setProductNo("10.234.1258.156");
        k3ReturnOrderDetail2.setProductCount(1);
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

}
