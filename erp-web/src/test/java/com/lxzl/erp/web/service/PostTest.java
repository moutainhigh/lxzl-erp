package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.core.service.k3.PostHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostTest extends ERPUnTransactionalTest {
    @Autowired
    private PostHelper postHelper;

    @Test
    public void postCustomer() throws InterruptedException {
        Customer customer = new Customer();
        customer.setCustomerId(123);
        customer.setCustomerName("测试客户");
        postHelper.post("/ksService/customer", customer, PostK3Type.POST_K3_TYPE_CUSTOMER);
        postHelper.post("/ksService/customer", customer, PostK3Type.POST_K3_TYPE_CUSTOMER);
        System.out.println("这是同步的");
        Thread.sleep(100000);
    }
}
