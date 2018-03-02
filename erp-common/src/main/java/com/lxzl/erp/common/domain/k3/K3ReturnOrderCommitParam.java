package com.lxzl.erp.common.domain.k3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.BaseCommitParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrderCommitParam extends BaseCommitParam {

    private String returnOrderNo;

    public String getReturnOrderNo() { return returnOrderNo; }

    public void setReturnOrderNo(String returnOrderNo) { this.returnOrderNo = returnOrderNo; }
}
