package com.lxzl.erp.common.domain.company;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCompanyQueryParam extends BasePageParam {

    private Integer subCompanyId;
    private String subCompanyName;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }
}
