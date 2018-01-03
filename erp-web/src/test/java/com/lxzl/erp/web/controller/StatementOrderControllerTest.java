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
        param.setOrderNo("LXO2018010370132700010");
        TestResult testResult = getJsonTestResult("/statementOrder/createNew", param);
    }

    @Test
    public void createChangeOrderStatement() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setChangeOrderNo("CO201712181114261141769");
        TestResult testResult = getJsonTestResult("/statementOrder/createChangeOrderStatement", param);
    }

    @Test
    public void createReturnOrderStatement() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setReturnOrderNo("LXRO7000042018010100001");
        TestResult testResult = getJsonTestResult("/statementOrder/createReturnOrderStatement", param);
    }

    @Test
    public void pay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("SN201712211624375361012");
        TestResult testResult = getJsonTestResult("/statementOrder/pay", param);
    }

    @Test
    public void page() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        TestResult testResult = getJsonTestResult("/statementOrder/page", param);
    }

    @Test
    public void detail() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("SN201712311225340681748");
        TestResult testResult = getJsonTestResult("/statementOrder/detail", param);
    }
}
