package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\15 0015 15:10
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipClaimDetailQueryParam extends BasePageParam {
    private Integer bankSlipId; //资金流水ID
    private Integer bankSlipDetailId; //资金流水明细ID
    private String customerNo;   //客戶编码
    private String customerName;   //客戶名称
    private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
    private BigDecimal startTradeAmount;   //交易金额查询开始金额
    private BigDecimal endTradeAmount;   //交易金额查询截止金额
    private BigDecimal startClaimAmount;   //认领金额查询开始金额
    private BigDecimal endClaimAmount;   //认领金额查询截止金额
    private Date startTradeTime;   //交易日期开始时间
    private Date endTradeTime;   //交易日期结束时间
    private String payerName;   //付款人名称
    private Integer detailStatus;   //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
    private Date startClaimUpdateTime;   //认领修改时间开始时间
    private Date endClaimUpdateTime;   //认领修改时间结束时间
    private String claimUpdateUserName;   //认领修改人姓名
    private Date startSlipDetailUpdateTime;   //流水明细修改时间开始时间
    private Date endSlipDetailUpdateTime;   //流水明细修改时间结束时间
    private String slipDetailUpdateUserName;   //流水明细修改人
    private Integer subCompanyId;  //属地化分公司ID
    private Integer isLocalization;  //是否已属地化,0-否，1-是[总公司时有值]
    private Integer ownerSubCompanyId;  //数据归属公司id

    public Integer getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(Integer bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public Integer getBankSlipDetailId() {
        return bankSlipDetailId;
    }

    public void setBankSlipDetailId(Integer bankSlipDetailId) {
        this.bankSlipDetailId = bankSlipDetailId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public BigDecimal getStartTradeAmount() {
        return startTradeAmount;
    }

    public void setStartTradeAmount(BigDecimal startTradeAmount) {
        this.startTradeAmount = startTradeAmount;
    }

    public BigDecimal getEndTradeAmount() {
        return endTradeAmount;
    }

    public void setEndTradeAmount(BigDecimal endTradeAmount) {
        this.endTradeAmount = endTradeAmount;
    }

    public BigDecimal getStartClaimAmount() {
        return startClaimAmount;
    }

    public void setStartClaimAmount(BigDecimal startClaimAmount) {
        this.startClaimAmount = startClaimAmount;
    }

    public BigDecimal getEndClaimAmount() {
        return endClaimAmount;
    }

    public void setEndClaimAmount(BigDecimal endClaimAmount) {
        this.endClaimAmount = endClaimAmount;
    }

    public Date getStartTradeTime() {
        return startTradeTime;
    }

    public void setStartTradeTime(Date startTradeTime) {
        this.startTradeTime = startTradeTime;
    }

    public Date getEndTradeTime() {
        return endTradeTime;
    }

    public void setEndTradeTime(Date endTradeTime) {
        this.endTradeTime = endTradeTime;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }

    public Date getStartClaimUpdateTime() {
        return startClaimUpdateTime;
    }

    public void setStartClaimUpdateTime(Date startClaimUpdateTime) {
        this.startClaimUpdateTime = startClaimUpdateTime;
    }

    public Date getEndClaimUpdateTime() {
        return endClaimUpdateTime;
    }

    public void setEndClaimUpdateTime(Date endClaimUpdateTime) {
        this.endClaimUpdateTime = endClaimUpdateTime;
    }

    public String getClaimUpdateUserName() {
        return claimUpdateUserName;
    }

    public void setClaimUpdateUserName(String claimUpdateUserName) {
        this.claimUpdateUserName = claimUpdateUserName;
    }

    public Date getStartSlipDetailUpdateTime() {
        return startSlipDetailUpdateTime;
    }

    public void setStartSlipDetailUpdateTime(Date startSlipDetailUpdateTime) {
        this.startSlipDetailUpdateTime = startSlipDetailUpdateTime;
    }

    public Date getEndSlipDetailUpdateTime() {
        return endSlipDetailUpdateTime;
    }

    public void setEndSlipDetailUpdateTime(Date endSlipDetailUpdateTime) {
        this.endSlipDetailUpdateTime = endSlipDetailUpdateTime;
    }

    public String getSlipDetailUpdateUserName() {
        return slipDetailUpdateUserName;
    }

    public void setSlipDetailUpdateUserName(String slipDetailUpdateUserName) {
        this.slipDetailUpdateUserName = slipDetailUpdateUserName;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getIsLocalization() {
        return isLocalization;
    }

    public void setIsLocalization(Integer isLocalization) {
        this.isLocalization = isLocalization;
    }

    public Integer getOwnerSubCompanyId() {
        return ownerSubCompanyId;
    }

    public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
        this.ownerSubCompanyId = ownerSubCompanyId;
    }

}
