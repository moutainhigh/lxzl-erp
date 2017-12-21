package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import org.junit.Test;
/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:52
 */
public class StatementOrderControllerTest  extends ERPUnTransactionalTest {

    @Test
    public void createNew() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setStatementOrderNo("O201712211337503711677");
        TestResult testResult = getJsonTestResult("/statementOrder/createNew", param);
    }

    @Test
    public void pay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("SN201712181720037051794");
        TestResult testResult = getJsonTestResult("/statementOrder/pay", param);
    }

    @Test
    public void page() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        TestResult testResult = getJsonTestResult("/statementOrder/page", param);
    }
}
