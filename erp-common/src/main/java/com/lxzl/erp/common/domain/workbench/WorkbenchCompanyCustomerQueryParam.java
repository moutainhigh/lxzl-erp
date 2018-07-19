package com.lxzl.erp.common.domain.workbench;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchCompanyCustomerQueryParam {
    private List<Integer> companyCustomerStatus;

    public List<Integer> getCompanyCustomerStatus() {
        return companyCustomerStatus;
    }

    public void setCompanyCustomerStatus(List<Integer> companyCustomerStatus) {
        this.companyCustomerStatus = companyCustomerStatus;
    }
}
