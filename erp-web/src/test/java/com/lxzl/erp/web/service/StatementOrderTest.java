package com.lxzl.erp.web.service;

import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-27 10:19
 */
public class StatementOrderTest  extends BaseUnTransactionalTest {

    @Autowired
    private StatementService statementService;

    @Test
    public void handleNoPaidStatementOrder(){
        Date currentTime = new Date();
        statementService.handleOverdueStatementOrder(null,currentTime);
    }
}
