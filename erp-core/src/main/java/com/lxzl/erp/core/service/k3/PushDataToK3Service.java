package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.customer.pojo.Customer;

public interface PushDataToK3Service {
    void pushCustomer(String customerNo);
    void pushCustomer(Customer customer);
}
