package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @author lk
 * @Description: 未收汇总
 * @date 2018/1/17 14:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsUnReceivablePageParam extends BasePageParam {

    private Integer subCompanyId; //所属公司ID
    private String salesmanName; //业务员姓名模糊查询

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
