package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:52
 */
public class StatementOrderControllerTest  extends ERPUnTransactionalTest {

    @Test
    public void testCommitDeploymentOrder() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setOrderNo("O201712111915479441688");
        Result result = getJsonTestResult("/statementOrder/createNew", param);
    }
}
