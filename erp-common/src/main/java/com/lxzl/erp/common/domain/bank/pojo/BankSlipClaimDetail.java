package com.lxzl.erp.common.domain.bank.pojo;


import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\15 0015 13:56
 */
public class BankSlipClaimDetail {
    private Integer bankSlipId; //资金流水ID
    private Integer bankSlipDetailId; //资金流水明细ID
    private String customerNo;   //客戶编码
    private String customerName;   //客戶名称
    private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
    private BigDecimal tradeAmount;   //交易金额
    private BigDecimal claimAmount;   //认领金额
    private Date tradeTime;   //交易日期
    private String payerName;   //付款人名称
    private Integer detailStatus;   //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date claimUpdateTime;   //认领修改时间
    private String claimUpdateUser;   //认领修改人
    private String claimUpdateUserName;   //认领修改人姓名
    private Date slipDetailUpdateTime;   //流水明细修改时间
    private String slipDetailUpdateUser;   //流水明细修改人
    private String slipDetailUpdateUserName;   //流水明细修改人
    private String slipDetailRemark;   //流水备注
    private String claimRemark;   //领取明细备注
    private Integer subCompanyId;  //属地化分公司ID
    private Integer isLocalization;  //是否已属地化,0-否，1-是[总公司时有值]
    private String subCompanyName;  //属地化分公司名称
    private Integer ownerSubCompanyId;  //数据归属公司id
    private String ownerSubCompanyName;  //数据归属公司名称
    private String createUserName;   //添加人姓名

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

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getClaimUpdateTime() {
        return claimUpdateTime;
    }

    public void setClaimUpdateTime(Date claimUpdateTime) {
        this.claimUpdateTime = claimUpdateTime;
    }

    public String getClaimUpdateUser() {
        return claimUpdateUser;
    }

    public void setClaimUpdateUser(String claimUpdateUser) {
        this.claimUpdateUser = claimUpdateUser;
    }

    public Date getSlipDetailUpdateTime() {
        return slipDetailUpdateTime;
    }

    public void setSlipDetailUpdateTime(Date slipDetailUpdateTime) {
        this.slipDetailUpdateTime = slipDetailUpdateTime;
    }

    public String getSlipDetailUpdateUser() {
        return slipDetailUpdateUser;
    }

    public void setSlipDetailUpdateUser(String slipDetailUpdateUser) {
        this.slipDetailUpdateUser = slipDetailUpdateUser;
    }

    public String getSlipDetailRemark() {
        return slipDetailRemark;
    }

    public void setSlipDetailRemark(String slipDetailRemark) {
        this.slipDetailRemark = slipDetailRemark;
    }

    public String getClaimRemark() {
        return claimRemark;
    }

    public void setClaimRemark(String claimRemark) {
        this.claimRemark = claimRemark;
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

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getOwnerSubCompanyId() {
        return ownerSubCompanyId;
    }

    public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
        this.ownerSubCompanyId = ownerSubCompanyId;
    }

    public String getOwnerSubCompanyName() {
        return ownerSubCompanyName;
    }

    public void setOwnerSubCompanyName(String ownerSubCompanyName) {
        this.ownerSubCompanyName = ownerSubCompanyName;
    }

    public String getClaimUpdateUserName() {
        return claimUpdateUserName;
    }

    public void setClaimUpdateUserName(String claimUpdateUserName) {
        this.claimUpdateUserName = claimUpdateUserName;
    }

    public String getSlipDetailUpdateUserName() {
        return slipDetailUpdateUserName;
    }

    public void setSlipDetailUpdateUserName(String slipDetailUpdateUserName) {
        this.slipDetailUpdateUserName = slipDetailUpdateUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
