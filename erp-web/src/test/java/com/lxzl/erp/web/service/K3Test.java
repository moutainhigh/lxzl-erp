package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.core.service.k3.K3Service;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 21:05
 */
public class K3Test extends ERPUnTransactionalTest {

    @Autowired
    private K3Service k3Service;

    @Test
    public void testGetData() {
        K3OrderQueryParam param = new K3OrderQueryParam();
        param.setPageNo(1);
        param.setPageSize(10);
        k3Service.queryAllOrder(param);
    }

    @Test
    public void queryOrder() {
        k3Service.queryOrder("LXSE2017121518");
    }
}
