package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PayStatus;
import com.lxzl.erp.common.constant.StatementDetailType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.AmountHasReturn;
import com.lxzl.erp.common.domain.statement.AmountNeedPay;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementPayOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectDetailMapper;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementPayOrderDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private StatementOrderCorrectDetailMapper statementOrderCorrectDetailMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    /**
     * 保存支付记录，返回支付状态
     *
     * @param statementOrderId
     * @param payAmount        支付总金额
     * @param payType
     * @param currentTime
     * @return
     */
    public StatementPayOrderDO saveStatementPayOrder(Integer statementOrderId, BigDecimal payAmount, BigDecimal payRentAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount, BigDecimal otherAmount, BigDecimal overdueAmount,Integer payType, Integer loginUserId, Date currentTime) {
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
        statementPayOrderDO.setOverdueAmount(overdueAmount);
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

    public ServiceResult<String, Boolean> updateStatementPayOrderStatus(Integer statementPayOrderId, Integer payStatus, String paymentOrderNo, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        StatementPayOrderDO statementPayOrderDO = statementPayOrderMapper.findById(statementPayOrderId);
        if (statementPayOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_PAY_ORDER_NOT_EXISTS);
            serviceResult.setResult(false);
            return serviceResult;
        }
        if (PayStatus.PAY_STATUS_PAID.equals(statementPayOrderDO.getPayStatus())
                || PayStatus.PAY_STATUS_FAILED.equals(statementPayOrderDO.getPayStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_PAY_ORDER_STATUS_IS_PAID_OR_FAILED);
            serviceResult.setResult(false);
            return serviceResult;
        }
        if (!PayStatus.PAY_STATUS_PAID.equals(payStatus)
                && !PayStatus.PAY_STATUS_FAILED.equals(payStatus)
                && !PayStatus.PAY_STATUS_TIME_OUT.equals(payStatus)) {

            serviceResult.setErrorCode(ErrorCode.STATEMENT_PAY_ORDER_STATUS_MUST_PAID_OR_FAILED_OR_TIME_OUT);
            serviceResult.setResult(false);
            return serviceResult;
        }

        statementPayOrderDO.setPayStatus(payStatus);
        statementPayOrderDO.setPaymentOrderNo(paymentOrderNo);
        statementPayOrderDO.setEndTime(currentTime);
        statementPayOrderDO.setUpdateTime(currentTime);
        statementPayOrderDO.setUpdateUser(loginUserId.toString());
        statementPayOrderMapper.update(statementPayOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(true);
        return serviceResult;
    }

    public StatementPayOrderDO getLastRecord(Integer statementOrderId) {
        return statementPayOrderMapper.findByStatementOrderId(statementOrderId);
    }

    public StatementPayOrderDO findByNo(String statementPayOrderNo) {
        return statementPayOrderMapper.findByNo(statementPayOrderNo);
    }

    /**
     * 计算订单结算单项实际需支付钱
     * @param statementOrderDetailDO
     * @return
     */
    public AmountNeedPay calculateStatementItemRealNeedPay(StatementOrderDetailDO statementOrderDetailDO){
        // 查询有没有冲正业务金额
        BigDecimal correctBusinessAmount = getStatementItemCorrectAmount(statementOrderDetailDO);
        //计算已退金额
        AmountHasReturn amountHasReturn=statementOrderSupport.getStatementItemHasReturn(statementOrderDetailDO);
        BigDecimal returnStatementAmount = amountHasReturn.getReturnStatementAmount(), returnStatementRentAmount = amountHasReturn.getReturnStatementRentAmount(), returnStatementDepositAmount =amountHasReturn.getReturnStatementDepositAmount(), returnStatementRentDepositAmount = amountHasReturn.getReturnStatementRentDepositAmount();

        //计算需支付金额
        BigDecimal needStatementDetailOtherPayAmount=BigDecimal.ZERO;
        BigDecimal noPaidStatementDetailOtherPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
        if (BigDecimalUtil.compare(noPaidStatementDetailOtherPayAmount, BigDecimal.ZERO) > 0) {
            needStatementDetailOtherPayAmount = BigDecimalUtil.sub(noPaidStatementDetailOtherPayAmount, correctBusinessAmount);
            needStatementDetailOtherPayAmount = BigDecimalUtil.compare(needStatementDetailOtherPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailOtherPayAmount;
            statementOrderDetailDO.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), needStatementDetailOtherPayAmount));
        }

        BigDecimal noPaidStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
        BigDecimal needStatementDetailRentPayAmount = BigDecimal.ZERO;
        if (BigDecimalUtil.compare(noPaidStatementDetailRentPayAmount, BigDecimal.ZERO) > 0) {
            needStatementDetailRentPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentPayAmount, correctBusinessAmount);
            needStatementDetailRentPayAmount = BigDecimalUtil.add(needStatementDetailRentPayAmount, returnStatementRentAmount);
            needStatementDetailRentPayAmount = BigDecimalUtil.compare(needStatementDetailRentPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentPayAmount;
            statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
        }
        BigDecimal needStatementDetailRentDepositPayAmount=BigDecimal.ZERO;
        BigDecimal noPaidStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
        if (BigDecimalUtil.compare(noPaidStatementDetailRentDepositPayAmount, BigDecimal.ZERO) > 0) {
            needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentDepositPayAmount, correctBusinessAmount);
            needStatementDetailRentDepositPayAmount = BigDecimalUtil.add(needStatementDetailRentDepositPayAmount, returnStatementRentDepositAmount);
            needStatementDetailRentDepositPayAmount = BigDecimalUtil.compare(needStatementDetailRentDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentDepositPayAmount;
            statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
        }

        BigDecimal noPaidStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
        BigDecimal needStatementDetailDepositPayAmount=BigDecimal.ZERO;
        if (BigDecimalUtil.compare(noPaidStatementDetailDepositPayAmount, BigDecimal.ZERO) > 0) {
            needStatementDetailDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailDepositPayAmount, correctBusinessAmount);
            needStatementDetailDepositPayAmount = BigDecimalUtil.add(needStatementDetailDepositPayAmount, returnStatementDepositAmount);
            needStatementDetailDepositPayAmount = BigDecimalUtil.compare(needStatementDetailDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailDepositPayAmount;
            statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
        }

        BigDecimal needStatementDetailOverduePayAmount=BigDecimal.ZERO;
        BigDecimal noPaidStatementDetailOverduePayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementOrderDetailDO.getStatementDetailOverduePaidAmount());
        if (BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, BigDecimal.ZERO) > 0) {
            // 查询有没有冲正逾期金额
            BigDecimal correctOverdueAmount = getStatementItemCorrectAmountByType(statementOrderDetailDO);
            needStatementDetailOverduePayAmount=BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, correctOverdueAmount) >= 0?BigDecimalUtil.sub(noPaidStatementDetailOverduePayAmount, correctOverdueAmount):BigDecimal.ZERO;
            statementOrderDetailDO.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), needStatementDetailOverduePayAmount));
        }
        return new AmountNeedPay(needStatementDetailDepositPayAmount,needStatementDetailOtherPayAmount,needStatementDetailRentPayAmount,needStatementDetailOverduePayAmount,BigDecimal.ZERO,needStatementDetailRentDepositPayAmount);
    }

    public BigDecimal getStatementItemCorrectAmountByType(StatementOrderDetailDO statementOrderDetailDO) {
        List<StatementOrderCorrectDetailDO> statementOrderCorrectOverdueDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), StatementDetailType.STATEMENT_DETAIL_TYPE_OVERDUE);
        BigDecimal correctOverdueAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderCorrectOverdueDetailDOList)) {
            for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectOverdueDetailDOList) {
                if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                    correctOverdueAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                }
            }
        }
        return correctOverdueAmount;
    }

    public  BigDecimal getStatementItemCorrectAmount(StatementOrderDetailDO statementOrderDetailDO) {
        List<StatementOrderCorrectDetailDO> statementOrderCorrectDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), statementOrderDetailDO.getStatementDetailType());
        BigDecimal correctBusinessAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDetailDOList)) {
            for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectDetailDOList) {
                if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                    correctBusinessAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                }
            }
        }
        return correctBusinessAmount;
    }
}
