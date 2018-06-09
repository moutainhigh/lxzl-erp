package com.lxzl.erp.web.service;

import com.lxzl.erp.core.service.SynchronizeDataService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-27 10:19
 */
public class SynchronizeDataServiceTest extends BaseUnTransactionalTest {

    @Autowired
    private SynchronizeDataService synchronizeDataService;

    @Test
    public void testSynchronizeOrderList2BatchReCreateOrderStatement(){
        // 获取所有订单号，循环每个订单号调用批量重算结算单接口
        // 每次调用接口休眠100毫秒，发送50个订单号
        synchronizeDataService.synchronizeOrderList2BatchReCreateOrderStatement(100, 50);
    }

}
