package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 21:08 2018/3/20
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetailQueryParam extends BasePageParam {
    private Integer bankSlipId;   //对公流水单ID
    private String payerName;   //付款人名称
    private Integer loanSign;   //借贷标志,1-贷（收入），2-借（支出）
    private Integer detailStatus;   //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
    private Integer isLocalization;   //'是否已属地化,0-否，1-是[总公司时有值]',
    private Date slipMonth;   //月份
    private String otherSideAccountNo;   //对方账号

    public String getOtherSideAccountNo() {
        return otherSideAccountNo;
    }

    public void setOtherSideAccountNo(String otherSideAccountNo) {
        this.otherSideAccountNo = otherSideAccountNo;
    }

    public Date getSlipMonth() {
        return slipMonth;
    }

    public void setSlipMonth(Date slipMonth) {
        this.slipMonth = slipMonth;
    }

    public Integer getIsLocalization() {
        return isLocalization;
    }

    public void setIsLocalization(Integer isLocalization) {
        this.isLocalization = isLocalization;
    }

    public Integer getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(Integer bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Integer getLoanSign() {
        return loanSign;
    }

    public void setLoanSign(Integer loanSign) {
        this.loanSign = loanSign;
    }

    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }
}
