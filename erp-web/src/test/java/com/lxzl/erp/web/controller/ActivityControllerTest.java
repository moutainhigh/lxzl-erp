package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.activity.ActivityOrderParam;
import org.junit.Test;


public class ActivityControllerTest extends ERPTransactionalTest {

    @Test
    public void getActivityorder() throws Exception {
        ActivityOrderParam activityOrderParam = new ActivityOrderParam();
        activityOrderParam.setPageSize(1);
        TestResult result = getJsonTestResult("/activity/getActivityOrder", activityOrderParam);
    }
}