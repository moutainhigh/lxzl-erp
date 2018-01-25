package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PayStatus;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementPayOrderMapper;
import com.lxzl.erp.dataaccess.domain.statement.StatementPayOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-23 19:34
 */
@Component
public class StatementPaySupport {

    @Autowired
    private StatementPayOrderMapper statementPayOrderMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    /**
     * 保存支付记录，返回支付状态
     *
     * @param statementOrderId
     * @param payAmount        支付总金额
     * @param payType
     * @param currentTime
     * @return
     */
    public StatementPayOrderDO saveStatementPayOrder(Integer statementOrderId, BigDecimal payAmount, BigDecimal payRentAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount, BigDecimal otherAmount, Integer payType, Integer loginUserId, Date currentTime) {
        if (BigDecimalUtil.compare(payAmount, BigDecimal.ZERO) <= 0) {
            return null;
        }
        StatementPayOrderDO statementPayOrderDO = new StatementPayOrderDO();
        statementPayOrderDO.setStatementPayOrderNo(generateNoSupport.generateStatementPayOrderNo());
        statementPayOrderDO.setStatementOrderId(statementOrderId);
        statementPayOrderDO.setPayAmount(payAmount);
        statementPayOrderDO.setPayRentAmount(payRentAmount);
        statementPayOrderDO.setPayRentDepositAmount(payRentDepositAmount);
        statementPayOrderDO.setPayDepositAmount(payDepositAmount);
        statementPayOrderDO.setOtherAmount(otherAmount);
        statementPayOrderDO.setPayType(payType);
        statementPayOrderDO.setPayStatus(PayStatus.PAY_STATUS_PAYING);
        statementPayOrderDO.setPayTime(currentTime);
        statementPayOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        statementPayOrderDO.setCreateTime(currentTime);
        statementPayOrderDO.setCreateUser(loginUserId.toString());
        statementPayOrderDO.setUpdateTime(currentTime);
        statementPayOrderDO.setUpdateUser(loginUserId.toString());
        statementPayOrderMapper.save(statementPayOrderDO);
        return statementPayOrderDO;
    }

    public boolean updateStatementPayOrderStatus(Integer statementPayOrderId, Integer payStatus, String paymentOrderNo, Integer loginUserId, Date currentTime) {
        StatementPayOrderDO statementPayOrderDO = statementPayOrderMapper.findById(statementPayOrderId);
        if (statementPayOrderDO == null) {
            return false;
        }
        if (PayStatus.PAY_STATUS_PAID.equals(statementPayOrderDO.getPayStatus())
                || PayStatus.PAY_STATUS_FAILED.equals(statementPayOrderDO.getPayStatus())) {
            return false;
        }
        if (!PayStatus.PAY_STATUS_PAID.equals(payStatus)
                && !PayStatus.PAY_STATUS_FAILED.equals(payStatus)
                && !PayStatus.PAY_STATUS_TIME_OUT.equals(payStatus)) {
            return false;
        }
        statementPayOrderDO.setPayStatus(payStatus);
        statementPayOrderDO.setPaymentOrderNo(paymentOrderNo);
        statementPayOrderDO.setEndTime(currentTime);
        statementPayOrderDO.setUpdateTime(currentTime);
        statementPayOrderDO.setUpdateUser(loginUserId.toString());
        statementPayOrderMapper.update(statementPayOrderDO);
        return true;
    }

    public StatementPayOrderDO getLastRecord(Integer statementOrderId) {
        return statementPayOrderMapper.findByStatementOrderId(statementOrderId);
    }

    public StatementPayOrderDO findByNo(String statementPayOrderNo) {
        return statementPayOrderMapper.findByNo(statementPayOrderNo);
    }
}
