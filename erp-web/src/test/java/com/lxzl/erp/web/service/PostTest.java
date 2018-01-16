package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.core.service.k3.PostHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostTest extends ERPUnTransactionalTest {
    @Autowired
    private PostHelper postHelper;

    @Test
    public void postCustomer() throws InterruptedException {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC10002018010500022");
        customer.setCustomerName("测试公司名01");
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        customer.setOwner(500015);
        CustomerCompany customerCompany = new CustomerCompany();
        customer.setCustomerCompany(customerCompany);
        customerCompany.setConnectRealName("张三");
        customerCompany.setConnectPhone("13612342234");

        postHelper.post(PostK3Type.POST_K3_TYPE_CUSTOMER,customer);
        Thread.sleep(100000);
    }
}
