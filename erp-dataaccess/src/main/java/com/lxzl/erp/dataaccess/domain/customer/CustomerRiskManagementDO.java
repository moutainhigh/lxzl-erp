package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;

public class CustomerRiskManagementDO extends BaseDO {

    private Integer id;
    private Integer customerId;
    private BigDecimal creditAmount;
    private BigDecimal creditAmountUsed;
    private Integer depositCycle;
    private Integer paymentCycle;
    private Integer appleDepositCycle;    // 苹果设备租赁方案
    private Integer applePaymentCycle;
    private Integer newDepositCycle;    // 全新设备租赁方案
    private Integer newPaymentCycle;
    private Integer payMode;            // 其他设备支付方式
    private Integer applePayMode;        // 苹果设备支付方式
    private Integer newPayMode;            // 全新设备支付方式
    private Integer isLimitApple;        // 是否限制苹果
    private Integer isLimitNew;            // 是否限制全新
    private BigDecimal singleLimitPrice;    // 单台限制设备价值
    private Integer returnVisitFrequency;    // 回访频率
    private Integer dataStatus;
    private String remark;
    private Integer isFullDeposit;   //是否是全额押金客户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountUsed() {
        return creditAmountUsed;
    }

    public void setCreditAmountUsed(BigDecimal creditAmountUsed) {
        this.creditAmountUsed = creditAmountUsed;
    }

    public Integer getDepositCycle() {
        return depositCycle;
    }

    public void setDepositCycle(Integer depositCycle) {
        this.depositCycle = depositCycle;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(Integer paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAppleDepositCycle() {
        return appleDepositCycle;
    }

    public void setAppleDepositCycle(Integer appleDepositCycle) {
        this.appleDepositCycle = appleDepositCycle;
    }

    public Integer getApplePaymentCycle() {
        return applePaymentCycle;
    }

    public void setApplePaymentCycle(Integer applePaymentCycle) {
        this.applePaymentCycle = applePaymentCycle;
    }

    public Integer getNewDepositCycle() {
        return newDepositCycle;
    }

    public void setNewDepositCycle(Integer newDepositCycle) {
        this.newDepositCycle = newDepositCycle;
    }

    public Integer getNewPaymentCycle() {
        return newPaymentCycle;
    }

    public void setNewPaymentCycle(Integer newPaymentCycle) {
        this.newPaymentCycle = newPaymentCycle;
    }

    public Integer getApplePayMode() {
        return applePayMode;
    }

    public void setApplePayMode(Integer applePayMode) {
        this.applePayMode = applePayMode;
    }

    public Integer getNewPayMode() {
        return newPayMode;
    }

    public void setNewPayMode(Integer newPayMode) {
        this.newPayMode = newPayMode;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public Integer getIsLimitApple() {
        return isLimitApple;
    }

    public void setIsLimitApple(Integer isLimitApple) {
        this.isLimitApple = isLimitApple;
    }

    public Integer getIsLimitNew() {
        return isLimitNew;
    }

    public void setIsLimitNew(Integer isLimitNew) {
        this.isLimitNew = isLimitNew;
    }

    public BigDecimal getSingleLimitPrice() {
        return singleLimitPrice;
    }

    public void setSingleLimitPrice(BigDecimal singleLimitPrice) {
        this.singleLimitPrice = singleLimitPrice;
    }

    public Integer getReturnVisitFrequency() {
        return returnVisitFrequency;
    }

    public void setReturnVisitFrequency(Integer returnVisitFrequency) {
        this.returnVisitFrequency = returnVisitFrequency;
    }

    public Integer getIsFullDeposit() {
        return isFullDeposit;
    }

    public void setIsFullDeposit(Integer isFullDeposit) {
        this.isFullDeposit = isFullDeposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRiskManagementDO that = (CustomerRiskManagementDO) o;

        if (creditAmount.compareTo(that.creditAmount) != 0) return false;
        if (depositCycle != null ? !depositCycle.equals(that.depositCycle) : that.depositCycle != null) return false;
        if (paymentCycle != null ? !paymentCycle.equals(that.paymentCycle) : that.paymentCycle != null) return false;
        if (appleDepositCycle != null ? !appleDepositCycle.equals(that.appleDepositCycle) : that.appleDepositCycle != null)
            return false;
        if (applePaymentCycle != null ? !applePaymentCycle.equals(that.applePaymentCycle) : that.applePaymentCycle != null)
            return false;
        if (newDepositCycle != null ? !newDepositCycle.equals(that.newDepositCycle) : that.newDepositCycle != null)
            return false;
        if (newPaymentCycle != null ? !newPaymentCycle.equals(that.newPaymentCycle) : that.newPaymentCycle != null)
            return false;
        if (payMode != null ? !payMode.equals(that.payMode) : that.payMode != null) return false;
        if (applePayMode != null ? !applePayMode.equals(that.applePayMode) : that.applePayMode != null) return false;
        if (newPayMode != null ? !newPayMode.equals(that.newPayMode) : that.newPayMode != null) return false;
        if (isLimitApple != null ? !isLimitApple.equals(that.isLimitApple) : that.isLimitApple != null) return false;
        if (isLimitNew != null ? !isLimitNew.equals(that.isLimitNew) : that.isLimitNew != null) return false;
        if(that.singleLimitPrice==null&&singleLimitPrice!=null) return false;
        if(that.singleLimitPrice!=null&&singleLimitPrice==null) return false;
        if(that.singleLimitPrice!=null&&singleLimitPrice!=null&&singleLimitPrice.compareTo(that.singleLimitPrice) != 0) return false;

        if (!returnVisitFrequency.equals(that.returnVisitFrequency)) return false;
        return isFullDeposit.equals(that.isFullDeposit);
    }

    @Override
    public int hashCode() {
        int result = creditAmount.hashCode();
        result = 31 * result + (depositCycle != null ? depositCycle.hashCode() : 0);
        result = 31 * result + (paymentCycle != null ? paymentCycle.hashCode() : 0);
        result = 31 * result + (appleDepositCycle != null ? appleDepositCycle.hashCode() : 0);
        result = 31 * result + (applePaymentCycle != null ? applePaymentCycle.hashCode() : 0);
        result = 31 * result + (newDepositCycle != null ? newDepositCycle.hashCode() : 0);
        result = 31 * result + (newPaymentCycle != null ? newPaymentCycle.hashCode() : 0);
        result = 31 * result + (payMode != null ? payMode.hashCode() : 0);
        result = 31 * result + (applePayMode != null ? applePayMode.hashCode() : 0);
        result = 31 * result + (newPayMode != null ? newPayMode.hashCode() : 0);
        result = 31 * result + (isLimitApple != null ? isLimitApple.hashCode() : 0);
        result = 31 * result + (isLimitNew != null ? isLimitNew.hashCode() : 0);
        result = 31 * result + (singleLimitPrice != null ? singleLimitPrice.hashCode() : 0);
        result = 31 * result + returnVisitFrequency.hashCode();
        result = 31 * result + isFullDeposit.hashCode();
        return result;
    }
}