package com.lxzl.erp.core.service.customer.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.se.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CustomerSupport {
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 内部调用增加已用授信额度
     *
     * @param customerNo
     * @param amount
     * @return
     */
    public String addCreditAmountUsed(String customerNo, BigDecimal amount) {
        if (amount == null || customerNo == null) {
            throw new BusinessException();
        }
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findByNo(customerNo);
            if (customerDO == null) {
                throw new BusinessException();
            }
            CustomerRiskManagementDO customerRiskManagementDO = customerDO.getCustomerRiskManagementDO();
            BigDecimal newValue = BigDecimalUtil.add(customerRiskManagementDO.getCreditAmountUsed(), amount);
            //如果超过了可用授信额度
            if (BigDecimalUtil.compare(newValue, customerRiskManagementDO.getCreditAmount()) > 0) {
                return ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW;
            }
            customerRiskManagementDO.setCreditAmountUsed(newValue);
            customerRiskManagementMapper.update(customerRiskManagementDO);
            return ErrorCode.SUCCESS;
        }
    }

    /**
     * 内部调用减少已用授信额度
     *
     * @param customerNo
     * @param amount
     * @return
     */
    public String subCreditAmountUsed(String customerNo, BigDecimal amount) {
        if (amount == null || customerNo == null) {
            throw new BusinessException();
        }
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findByNo(customerNo);
            if (customerDO == null) {
                throw new BusinessException();
            }
            CustomerRiskManagementDO customerRiskManagementDO = customerDO.getCustomerRiskManagementDO();
            BigDecimal newValue = BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmountUsed(), amount);
            //减成负值不处理，直接保存
            customerRiskManagementDO.setCreditAmountUsed(newValue);
            customerRiskManagementMapper.update(customerRiskManagementDO);
            return ErrorCode.SUCCESS;
        }
    }

    /**
     * 内部调用增加已用授信额度
     *
     * @param customerId
     * @param amount
     * @return
     */
    public String addCreditAmountUsed(Integer customerId, BigDecimal amount) {
        if (amount == null || customerId == null) {
            throw new BusinessException();
        }
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findById(customerId);
            if (customerDO == null) {
                throw new BusinessException();
            }
            CustomerRiskManagementDO customerRiskManagementDO = customerDO.getCustomerRiskManagementDO();
            BigDecimal newValue = BigDecimalUtil.add(customerRiskManagementDO.getCreditAmountUsed(), amount);
            //如果超过了可用授信额度
            if (BigDecimalUtil.compare(newValue, customerRiskManagementDO.getCreditAmount()) > 0) {
                return ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW;
            }
            customerRiskManagementDO.setCreditAmountUsed(newValue);
            customerRiskManagementMapper.update(customerRiskManagementDO);
            return ErrorCode.SUCCESS;
        }
    }

    /**
     * 内部调用减少已用授信额度
     *
     * @param customerId
     * @param amount
     * @return
     */
    public String subCreditAmountUsed(Integer customerId, BigDecimal amount) {
        if (amount == null || customerId == null) {
            throw new BusinessException();
        }
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findById(customerId);
            if (customerDO == null) {
                throw new BusinessException();
            }
            CustomerRiskManagementDO customerRiskManagementDO = customerDO.getCustomerRiskManagementDO();
            if (customerRiskManagementDO == null) {
                customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerDO.getId());
                if (customerRiskManagementDO == null) {
                    throw new BusinessException();
                }
                BigDecimal newValue = BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmountUsed(), amount);
                //减成负值不处理，直接保存
                customerRiskManagementDO.setCreditAmountUsed(newValue);
            }
            customerRiskManagementMapper.update(customerRiskManagementDO);
            return ErrorCode.SUCCESS;
        }
    }
}
