package com.lxzl.erp.web.service;

import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-27 10:19
 */
public class StatementOrderTest  extends BaseUnTransactionalTest {

    @Autowired
    private StatementService statementService;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Test
    public void handleNoPaidStatementOrder(){
        Date currentTime = new Date();
        statementService.handleOverdueStatementOrder(null,currentTime);
    }

    @Test
    public void testGetShortRentReceivable(){
        BigDecimal totalShortRentReceivable = statementOrderSupport.getShortRentReceivable(731449);
        System.out.println(totalShortRentReceivable);
    }

    @Test
    public void getOverdueStatementOrderList(){
        List<StatementOrderDO> statementOrderDOList = statementOrderSupport.getOverdueStatementOrderList(731477);
        System.out.println(statementOrderDOList.size());
    }
}
