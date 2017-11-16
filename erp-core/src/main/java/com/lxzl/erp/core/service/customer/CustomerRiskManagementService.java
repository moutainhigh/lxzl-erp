package com.lxzl.erp.core.service.customer;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;

public interface CustomerRiskManagementService {

    ServiceResult<String,Integer> add(CustomerRiskManagement customerRiskManagement);
    ServiceResult<String,Integer> update(CustomerRiskManagement customerRiskManagement);
    ServiceResult<String,CustomerRiskManagement> detail(String customerNo);
    String delete(Integer customerRiskManagementId);
}
