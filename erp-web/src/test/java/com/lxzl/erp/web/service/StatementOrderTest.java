package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalNoLoginTest;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementReplaceOrderSupport;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-27 10:19
 */
public class StatementOrderTest  extends ERPUnTransactionalNoLoginTest {

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


    @Test
    public void stopTestMachineOrder(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(2018,8,7);
        statementOrderSupport.stopTestMachineOrder("LXO-20180905-0755-00001",calendar.getTime());
    }

    @Test
    public void stopOldOrder(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(2018,5,1);
        Map map=statementOrderSupport.stopOldMonthRentOrder("LXO-20180913-1000-00042",calendar.getTime());
        statementOrderSupport.fillOldOrderAmountToNew("LXO-20180913-1000-00042",map,new HashMap<String, String>());
        System.out.println(map);
    }
    @Autowired
    private StatementReplaceOrderSupport statementReplaceOrderSupport;
    @Test
    public void tt(){
        statementReplaceOrderSupport.reStatementReplaceOrder("LXREO20180925133753361");
    }
}
