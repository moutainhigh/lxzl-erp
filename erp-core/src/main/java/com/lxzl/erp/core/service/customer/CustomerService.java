package com.lxzl.erp.core.service.customer;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;

public interface CustomerService {
    ServiceResult<String,String> addCompany(Customer customer);
    ServiceResult<String,String> addPerson(Customer customer);
    ServiceResult<String,String> updateCompany(Customer customer);
    ServiceResult<String,String> updatePerson(Customer customer);
    ServiceResult<String,Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam);
    ServiceResult<String,Page<Customer>> pageCustomerPerson(CustomerPersonQueryParam customerPersonQueryParam);
    ServiceResult<String,Customer> detailCustomerCompany(Customer customer);
    ServiceResult<String,Customer> detailCustomerPerson(Customer customer);
    ServiceResult<String,String> updateRisk(CustomerRiskManagement customerRiskManagement);

    ServiceResult<String,Integer> addCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    ServiceResult<String,Integer> updateCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    ServiceResult<String,Integer> deleteCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    ServiceResult<String,CustomerConsignInfo> detailCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    ServiceResult<String,Page<CustomerConsignInfo>> pageCustomerConsignInfo(CustomerConsignInfoQueryParam customerConsignInfoQueryParam);

    ServiceResult<String,Integer> updateAddressIsMain(CustomerConsignInfo customerConsignInfo);

    ServiceResult<String,Integer> getLastUseTime(CustomerConsignInfo customerConsignInfo);
}
