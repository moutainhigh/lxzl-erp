package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;



import java.util.Date;
import java.util.List;

public class FormSEOrderOelet {
    private String orderNo;         //订单编号
    private String pw;              //请求密码
    private String fFetchAdd;       //交货地址
    private String fAlterReason;    //续租说明
    private Integer orderType;      //订单类型
    private String fFetchDate;        //续租后日期
    private List<FormSEOrderOeletEntry> entrys;   //订单详情



    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getfFetchAdd() {
        return fFetchAdd;
    }

    public void setfFetchAdd(String fFetchAdd) {
        this.fFetchAdd = fFetchAdd;
    }


    public String getfAlterReason() {
        return fAlterReason;
    }

    public void setfAlterReason(String fAlterReason) {
        this.fAlterReason = fAlterReason;
    }


    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getfFetchDate() {
        return fFetchDate;
    }

    public void setfFetchDate(String fFetchDate) {
        this.fFetchDate = fFetchDate;
    }

    public List<FormSEOrderOeletEntry> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<FormSEOrderOeletEntry> entrys) {
        this.entrys = entrys;
    }
}
