package com.lxzl.erp.common.domain.k3.pojo.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ChangeOrderQueryParam extends BasePageParam implements Serializable {

    private String changeOrderNo;   //换货编号
    private String k3CustomerNo;   //K3客户编码
    private String k3CustomerName;   //K3客户名称
    private Date changeStartTime;   //换货开始时间
    private Date changeEndTime;   //换货结束时间

    public String getK3CustomerNo() {
        return k3CustomerNo;
    }

    public void setK3CustomerNo(String k3CustomerNo) {
        this.k3CustomerNo = k3CustomerNo;
    }

    public String getK3CustomerName() {
        return k3CustomerName;
    }

    public void setK3CustomerName(String k3CustomerName) {
        this.k3CustomerName = k3CustomerName;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Date getChangeStartTime() {
        return changeStartTime;
    }

    public void setChangeStartTime(Date changeStartTime) {
        this.changeStartTime = changeStartTime;
    }

    public Date getChangeEndTime() {
        return changeEndTime;
    }

    public void setChangeEndTime(Date changeEndTime) {
        this.changeEndTime = changeEndTime;
    }
}
