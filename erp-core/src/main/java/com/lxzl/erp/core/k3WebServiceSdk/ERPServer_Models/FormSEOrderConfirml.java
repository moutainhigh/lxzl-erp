package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 14:56 2018/5/23
 * @Modified By:
 */
public class FormSEOrderConfirml implements java.io.Serializable {
    private String orderNo; //订单编号
    private List<FormSEOrderConfirmlEntry> entrys; //订单确认详情
    private String PW;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<FormSEOrderConfirmlEntry> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<FormSEOrderConfirmlEntry> entrys) {
        this.entrys = entrys;
    }

    @JSONField(name="PW")
    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }
}
