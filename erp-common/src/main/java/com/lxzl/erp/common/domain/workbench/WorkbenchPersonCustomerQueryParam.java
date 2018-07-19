package com.lxzl.erp.common.domain.workbench;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchPersonCustomerQueryParam {
    private List<Integer> personCustomerStatusList;

    public List<Integer> getPersonCustomerStatusList() {
        return personCustomerStatusList;
    }

    public void setPersonCustomerStatusList(List<Integer> personCustomerStatusList) {
        this.personCustomerStatusList = personCustomerStatusList;
    }
}
