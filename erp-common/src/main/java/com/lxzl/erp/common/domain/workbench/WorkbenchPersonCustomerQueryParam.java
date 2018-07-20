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
    private List<Integer> customerPersonStatusList;

    public List<Integer> getCustomerPersonStatusList() {
        return customerPersonStatusList;
    }

    public void setCustomerPersonStatusList(List<Integer> customerPersonStatusList) {
        this.customerPersonStatusList = customerPersonStatusList;
    }
}
