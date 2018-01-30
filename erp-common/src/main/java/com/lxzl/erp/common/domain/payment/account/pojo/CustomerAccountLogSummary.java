package com.lxzl.erp.common.domain.payment.account.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.payment.CustomerAccountLogPage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-01-30 14:18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccountLogSummary extends BasePO {

    private Integer businessCustomerAccountLogId;   //id
    private Integer customerId; //客户id
    private Integer customerAccountLogType; //客户账户日志记录类型，1:手动充值 2:扣款 3:余额支付 4:退款 5:线上充值 其他待拓展
    private BigDecimal tradeAmount; //	交易金额—在汇总对象中就为汇总的交易金额
    private BigDecimal oldBalanceAmount; //	用户可用余额（未操作前）
    private BigDecimal oldTotalFrozenAmount; //	总冻结金额（为操作前）
    private BigDecimal oldDepositAmount; //		押金金额（为操作前）
    private BigDecimal oldRentDepositAmount; //	租金存款（操作前）
    private BigDecimal newBalanceAmount; //	用户可用余额（操作后）
    private BigDecimal newTotalFrozenAmount; //	总冻结金额（操作后）
    private BigDecimal newDepositAmount; //		押金金额（操作后）
    private BigDecimal newRentDepositAmount; //		租金存款（操作后）
    private String remark;  //备注
    private Date createTime;  //创建时间
    private Date updateTime;  //更新时间

    private List<CustomerAccountLogPage> customerAccountLogPage;

    public Integer getBusinessCustomerAccountLogId() { return businessCustomerAccountLogId; }

    public void setBusinessCustomerAccountLogId(Integer businessCustomerAccountLogId) { this.businessCustomerAccountLogId = businessCustomerAccountLogId; }

    public Integer getCustomerId() { return customerId; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getCustomerAccountLogType() { return customerAccountLogType; }

    public void setCustomerAccountLogType(Integer customerAccountLogType) { this.customerAccountLogType = customerAccountLogType; }

    public BigDecimal getTradeAmount() { return tradeAmount; }

    public void setTradeAmount(BigDecimal tradeAmount) { this.tradeAmount = tradeAmount; }

    public BigDecimal getOldBalanceAmount() { return oldBalanceAmount; }

    public void setOldBalanceAmount(BigDecimal oldBalanceAmount) { this.oldBalanceAmount = oldBalanceAmount; }

    public BigDecimal getOldTotalFrozenAmount() { return oldTotalFrozenAmount; }

    public void setOldTotalFrozenAmount(BigDecimal oldTotalFrozenAmount) { this.oldTotalFrozenAmount = oldTotalFrozenAmount; }

    public BigDecimal getOldDepositAmount() { return oldDepositAmount; }

    public void setOldDepositAmount(BigDecimal oldDepositAmount) { this.oldDepositAmount = oldDepositAmount; }

    public BigDecimal getOldRentDepositAmount() { return oldRentDepositAmount; }

    public void setOldRentDepositAmount(BigDecimal oldRentDepositAmount) { this.oldRentDepositAmount = oldRentDepositAmount; }

    public BigDecimal getNewBalanceAmount() { return newBalanceAmount; }

    public void setNewBalanceAmount(BigDecimal newBalanceAmount) { this.newBalanceAmount = newBalanceAmount; }

    public BigDecimal getNewTotalFrozenAmount() { return newTotalFrozenAmount; }

    public void setNewTotalFrozenAmount(BigDecimal newTotalFrozenAmount) { this.newTotalFrozenAmount = newTotalFrozenAmount; }

    public BigDecimal getNewDepositAmount() { return newDepositAmount; }

    public void setNewDepositAmount(BigDecimal newDepositAmount) { this.newDepositAmount = newDepositAmount; }

    public BigDecimal getNewRentDepositAmount() { return newRentDepositAmount; }

    public void setNewRentDepositAmount(BigDecimal newRentDepositAmount) { this.newRentDepositAmount = newRentDepositAmount; }

    public String getRemark() { return remark; }

    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public List<CustomerAccountLogPage> getCustomerAccountLogPage() { return customerAccountLogPage; }

    public void setCustomerAccountLogPage(List<CustomerAccountLogPage> customerAccountLogPage) { this.customerAccountLogPage = customerAccountLogPage; }
}
