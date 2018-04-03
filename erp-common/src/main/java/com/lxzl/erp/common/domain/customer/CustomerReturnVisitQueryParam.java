package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 14:34 2018/4/2
 * @Modified By:
 */
public class CustomerReturnVisitQueryParam extends BasePageParam {

    private String customerNo;   //客戶编号

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
}
