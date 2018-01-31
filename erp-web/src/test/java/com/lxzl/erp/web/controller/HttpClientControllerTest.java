package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.DeploymentType;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.getIpAndMac.impl.IpAndMacServiceImpl;
import com.lxzl.se.common.util.secret.MD5Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public class HttpClientControllerTest extends ERPUnTransactionalTest {
    @Autowired
    CustomerService customerService;
    @Test
    public void test() throws Exception {
        ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByCompanyName("新的测试数据");
    }

    @Test
    public void importSupplierXlsxDataTest() throws Exception {

        TestResult testResult = getJsonTestResult("/http/importSupplier", null);
        Map<String, Object> map = testResult.getResultMap();
        Map<String, String> data = (Map<String, String>)map.get("data");
        if(data == null){
            System.out.println("没有错错误数据返回");
        }
        for (String key : data.keySet()) {
            String value = data.get(key);
            System.out.println("加入的:"+key);
        }
    }

    @Test
    public void importUserXlsxDataTest() throws Exception {

        TestResult testResult = getJsonTestResult("/http/importUser", null);
        Map<String, Object> map = testResult.getResultMap();
        Map<String, String> data = (Map<String, String>)map.get("data");
        if(data == null){
            System.out.println("没有错错误数据返回");
        }
        for (String key : data.keySet()) {
            String value = data.get(key);
            System.out.println("公司名称:"+key+":"+"最后一次错误的列:"+value+"(注意可能不何止错这一列)");
        }
    }

    @Test
    public void test11() throws Exception {
        TestResult testResult = getJsonTestResult("/http/test11", "");
        Map<String, Object> map = testResult.getResultMap();
        Map<String, String> data = (Map<String, String>)map.get("data");
        for (String key : data.keySet()) {
            String value = data.get(key);
            System.out.println("公司名称:"+key+":"+"错误的列"+value);
        }
    }

    @Test
    public void EXCTest() throws Exception {
        TestResult testResult = getJsonTestResult("/http/exct", null);
        Map<String, Object> map = testResult.getResultMap();
        Map<String, String> data = (Map<String, String>)map.get("data");
        if(data == null){
            System.out.println("没有错错误数据返回");
        }
        for (String key : data.keySet()) {
            String value = data.get(key);
            System.out.println("公司名称:"+key+":"+"最后一次错误的列:"+value+"(注意可能不何止错这一列)");
        }
    }

    @Test
    public void testChineseCharToEn() throws Exception {
//        TestResult testResult = getJsonTestResult("/http/change","");
        String value = MD5Util.encryptWithKey("xiaoluyu" + "Lxzl123456", ApplicationConfig.authKey);
        System.out.println(value);
    }

    @Test
    public void testCreateDeploymentOrder() throws Exception {
        IpAndMacServiceImpl ipAndMacService = new IpAndMacServiceImpl();
        IpAndMacServiceImpl.UdpGetClientMacAddr udpGetClientMacAddr = ipAndMacService.new UdpGetClientMacAddr("192.168.10.93");
        String macAddr = udpGetClientMacAddr.GetRemoteMacAddr();
        System.out.println(macAddr);
//        TestResult testResult = getJsonTestResult("/http/test11","");
    }

}