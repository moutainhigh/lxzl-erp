package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrderListParam implements Serializable {

    private List<String> returnOrderNoList;   //退货单编号列表

    public List<String> getReturnOrderNoList() { return returnOrderNoList; }

    public void setReturnOrderNoList(List<String> returnOrderNoList) { this.returnOrderNoList = returnOrderNoList; }
}
