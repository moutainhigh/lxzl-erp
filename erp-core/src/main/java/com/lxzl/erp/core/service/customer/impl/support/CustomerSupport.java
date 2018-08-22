package com.lxzl.erp.core.service.customer.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerRiskBusinessType;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskLogDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.se.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class CustomerSupport {
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerRiskLogMapper customerRiskLogMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;
    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;

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
     * @param customerId   客户ID （必填）
     * @param amount       更改金额（必填）
     * @param businessType 操作业务编码 （必填）
     * @param orderNo      订单编号 （可空）
     * @param remark       备注 （可空）
     * @return
     */
    public String addCreditAmountUsed(Integer customerId, BigDecimal amount, Integer businessType, String orderNo, String remark) {
        //切换到母公司关联客户（如果是公司客户，并且有母公司）
        Integer parentCustomerId = getParentCompanyCustomerId(customerId);
        if (amount == null || parentCustomerId == null) {
            throw new BusinessException();
        }
        //日志
        saveCustomerRiskLog(parentCustomerId, parentCustomerId.equals(customerId) ? null : customerId, amount, businessType, orderNo, remark);
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findById(parentCustomerId);
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
     * @param customerId   客户ID （必填）
     * @param amount       更改金额（必填）
     * @param businessType 操作业务编码 （必填）
     * @param orderNo      订单编号 （可空）
     * @param remark       备注 （可空）
     * @return
     */
    public String subCreditAmountUsed(Integer customerId, BigDecimal amount, Integer businessType, String orderNo, String remark) {
        //切换到母公司关联客户（如果是公司客户，并且有母公司）
        Integer parentCustomerId = getParentCompanyCustomerId(customerId);
        if (amount == null || parentCustomerId == null) {
            throw new BusinessException();
        }
        //日志
        saveCustomerRiskLog(parentCustomerId, parentCustomerId.equals(customerId) ? null : customerId, BigDecimalUtil.mul(amount,new BigDecimal(-1)), businessType, orderNo, remark);
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) < 0) {
            throw new BusinessException();
        } else if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
            return ErrorCode.SUCCESS;
        } else {
            CustomerDO customerDO = customerMapper.findById(parentCustomerId);
            if (customerDO == null) {
                throw new BusinessException();
            }
            CustomerRiskManagementDO customerRiskManagementDO = customerDO.getCustomerRiskManagementDO();
            if (customerRiskManagementDO == null) {
                customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerDO.getId());
                if (customerRiskManagementDO == null) {
                    return ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS;
                }
            }
            BigDecimal newValue = BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmountUsed(), amount);
            //额度减到0以下不处理
            if (BigDecimalUtil.compare(newValue, BigDecimal.ZERO) < 0) {
                return ErrorCode.SUCCESS;
            }
            //减成负值不处理，直接保存
            customerRiskManagementDO.setCreditAmountUsed(newValue);
            customerRiskManagementMapper.update(customerRiskManagementDO);
            return ErrorCode.SUCCESS;
        }
    }

    /**
     * 风控信息存在，校验风控信息是否完整
     */
    public boolean isFullRiskManagement(Integer customerId) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerId);
        if (customerRiskManagementDO == null) {
            return false;
        }
        if (customerRiskManagementDO.getCreditAmount() == null || customerRiskManagementDO.getDepositCycle() == null || customerRiskManagementDO.getPaymentCycle() == null || customerRiskManagementDO.getPayMode() == null) {
            return false;
        }
        if (!CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew()) && (customerRiskManagementDO.getNewDepositCycle() == null || customerRiskManagementDO.getNewPaymentCycle() == null || customerRiskManagementDO.getNewPayMode() == null)) {
            return false;
        }
        if (!CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple()) && (customerRiskManagementDO.getAppleDepositCycle() == null || customerRiskManagementDO.getApplePaymentCycle() == null || customerRiskManagementDO.getApplePayMode() == null)) {
            return false;
        }

        return true;
    }

    /**
     * 添加客户授信变更日志
     *
     * @param customerId       客户ID （必填）
     * @param manageCustomerId 关联客户ID （可空）
     * @param amount           更改金额（必填）
     * @param businessType     操作业务编码 （必填）
     * @param orderNo          订单编号 （可空）
     * @param remark           备注 （可空）
     */
    public String saveCustomerRiskLog(Integer customerId, Integer manageCustomerId, BigDecimal amount, Integer businessType, String orderNo, String remark) {
        Date date = new Date();
        if (amount == null || customerId == null || businessType == null) {
            throw new BusinessException();
        }
        if (BigDecimalUtil.compare(amount, BigDecimal.ZERO) == 0) {
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
                    return ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS;
                }
            }
            CustomerRiskLogDO customerRiskLogDO = new CustomerRiskLogDO();
            //变更授信额度
            if (CustomerRiskBusinessType.CUSTOMER_RISK_TYPE.equals(businessType)) {
                customerRiskLogDO.setCustomerId(customerId);
                customerRiskLogDO.setManageCustomerId(manageCustomerId);
                customerRiskLogDO.setOrderNo(orderNo);
                customerRiskLogDO.setRemark(remark);
                customerRiskLogDO.setBusinessType(businessType);
                customerRiskLogDO.setCreateTime(date);
                customerRiskLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                customerRiskLogDO.setOldCreditAmount(customerRiskManagementDO.getCreditAmount());
                customerRiskLogDO.setOldCreditAmountUsed(customerRiskManagementDO.getCreditAmountUsed());
                customerRiskLogDO.setNewCreditAmountUsed(customerRiskManagementDO.getCreditAmountUsed());
                BigDecimal newValue = BigDecimalUtil.add(customerRiskManagementDO.getCreditAmount(), amount);
                customerRiskLogDO.setNewCreditAmount(newValue);
            } else {//变更已使用授信额度
                customerRiskLogDO.setCustomerId(customerId);
                customerRiskLogDO.setManageCustomerId(manageCustomerId);
                customerRiskLogDO.setOrderNo(orderNo);
                customerRiskLogDO.setRemark(remark);
                customerRiskLogDO.setBusinessType(businessType);
                customerRiskLogDO.setCreateTime(date);
                customerRiskLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                customerRiskLogDO.setOldCreditAmount(customerRiskManagementDO.getCreditAmount());
                customerRiskLogDO.setOldCreditAmountUsed(customerRiskManagementDO.getCreditAmountUsed());
                customerRiskLogDO.setNewCreditAmount(customerRiskManagementDO.getCreditAmount());
                BigDecimal newValue = BigDecimalUtil.add(customerRiskManagementDO.getCreditAmountUsed(), amount);
                customerRiskLogDO.setNewCreditAmountUsed(newValue);
            }
            customerRiskLogMapper.save(customerRiskLogDO);
            return ErrorCode.SUCCESS;
        }
    }

    /**
     * @param custmerId
     * @return
     * @desc 如果当前客户存在母公司，则返回母公司客户id,否则直接返回当前客户id
     */
    private Integer getParentCompanyCustomerId(final Integer custmerId) {
        if (custmerId != null) {
            CustomerDO customerDO = customerMapper.findById(custmerId);
            if (customerDO != null && CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(custmerId);
                if (customerCompanyDO != null &&customerCompanyDO.getSubsidiary()!=null&& customerCompanyDO.getSubsidiary()) {
                    CustomerCompanyDO parentCustomerCompanyDO = customerCompanyMapper.findById(customerCompanyDO.getParentCompanyId());
                    if (parentCustomerCompanyDO != null) {
                        return parentCustomerCompanyDO.getCustomerId();
                    }
                }
            }
        }
        return custmerId;
    }

    /**
     * 根据客户ID，获取风控信息
     *
     * @param customerId
     * @return
     */
    public CustomerRiskManagementDO getCustomerRiskManagementDO(Integer customerId) {
        customerId = this.getParentCompanyCustomerId(customerId);
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerId);
        return customerRiskManagementDO;
    }

    /**
     * 如果
     * @param k3CustomerNo
     * @return
     */
    public CustomerDO getCustomerByK3CustomerNo(String k3CustomerNo){
        K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByK3Code(k3CustomerNo);
        CustomerDO customerDO = null;
        if (k3MappingCustomerDO == null) {
            customerDO = customerMapper.findByNo(k3CustomerNo);
        } else {
            customerDO = customerMapper.findByNo(k3MappingCustomerDO.getErpCustomerCode());
        }
       return customerDO;
    }
}
