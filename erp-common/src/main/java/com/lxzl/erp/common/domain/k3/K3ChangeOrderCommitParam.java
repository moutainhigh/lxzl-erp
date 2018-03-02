package com.lxzl.erp.common.domain.k3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.BaseCommitParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ChangeOrderCommitParam extends BaseCommitParam {

    private String changeOrderNo;

    public String getChangeOrderNo() { return changeOrderNo; }

    public void setChangeOrderNo(String changeOrderNo) { this.changeOrderNo = changeOrderNo; }
}
