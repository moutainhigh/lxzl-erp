package com.lxzl.erp.dataaccess.domain.statistics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/17
 * Time: 17:34
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsDealsCountBySubCompany {
    private Integer subCompanyId;
    private Integer dealsCount = 0;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getDealsCount() {
        return dealsCount;
    }

    public void setDealsCount(Integer dealsCount) {
        this.dealsCount = dealsCount;
    }
}
