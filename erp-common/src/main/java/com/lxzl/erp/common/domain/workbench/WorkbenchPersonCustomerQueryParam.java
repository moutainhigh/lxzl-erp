package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchPersonCustomerQueryParam {
    private List<CustomerPersonQueryParam> customerPersonQueryParamList;

    public List<CustomerPersonQueryParam> getCustomerPersonQueryParamList() {
        return customerPersonQueryParamList;
    }

    public void setCustomerPersonQueryParamList(List<CustomerPersonQueryParam> customerPersonQueryParamList) {
        this.customerPersonQueryParamList = customerPersonQueryParamList;
    }
}
