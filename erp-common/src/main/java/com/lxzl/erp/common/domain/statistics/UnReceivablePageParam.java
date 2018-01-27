package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @author lk
 * @Description: 未收明细分页查询
 * @date 2018/1/17 14:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnReceivablePageParam extends BasePageParam {

    private String customerName; //客户名称模糊查询
    private Integer rentLengthType; //（业务类型）租赁时长类型，1-长租，2-短租
    private Integer subCompanyId; //所属公司ID
    private String salesmanName; //业务员姓名模糊查询

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }
}
