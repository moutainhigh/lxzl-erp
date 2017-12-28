package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.DeploymentType;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public class HttpClientControllerTest extends ERPUnTransactionalTest {
    @Test
    public void testChineseCharToEn() throws Exception {
        TestResult testResult = getJsonTestResult("/http/change","");
    }

    @Test
    public void testCreateDeploymentOrder() throws Exception {
        TestResult testResult = getJsonTestResult("/http/test","");
    }
}