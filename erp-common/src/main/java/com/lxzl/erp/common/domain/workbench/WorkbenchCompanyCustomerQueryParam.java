package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchCompanyCustomerQueryParam {
    private List<CustomerCompanyQueryParam> customerCompanyQueryParamList;

    public List<CustomerCompanyQueryParam> getCustomerCompanyQueryParamList() {
        return customerCompanyQueryParamList;
    }

    public void setCustomerCompanyQueryParamList(List<CustomerCompanyQueryParam> customerCompanyQueryParamList) {
        this.customerCompanyQueryParamList = customerCompanyQueryParamList;
    }
}
