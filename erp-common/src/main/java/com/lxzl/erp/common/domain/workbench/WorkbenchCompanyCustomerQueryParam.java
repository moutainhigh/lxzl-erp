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
    private List<Integer> customerCompanyStatusList;

    public List<Integer> getCustomerCompanyStatusList() {
        return customerCompanyStatusList;
    }

    public void setCustomerCompanyStatusList(List<Integer> customerCompanyStatusList) {
        this.customerCompanyStatusList = customerCompanyStatusList;
    }
}
