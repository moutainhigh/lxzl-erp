package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalNoLoginTest;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        calendar.set(2018,3,1);
        Map map=statementOrderSupport.stopOldMonthRentOrder("LXO-20180910-1000-00037",calendar.getTime());
        System.out.println(map);
    }
}
