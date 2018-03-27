package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @创建人 liuzy
 * @创建日期 2018/3/27
 * @描述: 风控历史记录
 */
public class CustomerRiskManageHistoryQueryParam extends BasePageParam {

    @NotEmpty(message = ErrorCode.CUSTOMER_NO_NOT_NULL)
    private String customerNo; //客户编号

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

}
