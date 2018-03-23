package com.lxzl.erp.common.domain.bank;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:03 2018/3/20
 * @Modified By:
 */
public class BankSlipQueryParam extends BasePageParam {

    private String subCompanyName;   //分公司名称
    private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
    private Date slipMonth;   //月份
    private Integer slipStatus;   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
    private Integer bankSlipId;   //银行对公流水id

    public Integer getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(Integer bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public Date getSlipMonth() {
        return slipMonth;
    }

    public void setSlipMonth(Date slipMonth) {
        this.slipMonth = slipMonth;
    }

    public Integer getSlipStatus() {
        return slipStatus;
    }

    public void setSlipStatus(Integer slipStatus) {
        this.slipStatus = slipStatus;
    }
}
