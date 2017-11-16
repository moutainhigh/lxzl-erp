package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.core.service.customer.CustomerRiskManagementService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CustomerRiskManagementServiceImpl implements CustomerRiskManagementService{

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
    @Override
    public ServiceResult<String, Integer> add(CustomerRiskManagement customerRiskManagement) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(customerRiskManagement.getCustomerNo());
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        String checkErrorCode = check(customerRiskManagement);
        if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
            serviceResult.setErrorCode(checkErrorCode);
            return  serviceResult;
        }
        Date now = new Date();
        CustomerRiskManagementDO customerRiskManagementDO = CustomerRiskManagementConverter.convertCustomerRiskManagement(customerRiskManagement);
        customerRiskManagementDO.setId(null);
        customerRiskManagementDO.setCreditAmountUsed(BigDecimal.ZERO);
        customerRiskManagementDO.setCustomerId(customerDO.getId());
        customerRiskManagementDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerRiskManagementDO.setCreateTime(now);
        customerRiskManagementDO.setUpdateTime(now);
        customerRiskManagementDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerRiskManagementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerRiskManagementMapper.save(customerRiskManagementDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerRiskManagementDO.getId());
        return serviceResult;
    }
    private String check(CustomerRiskManagement customerRiskManagement){
        String serviceResult = ErrorCode.SUCCESS;
        if(customerRiskManagement.getCreditAmount()!=null&&customerRiskManagement.getCreditAmount().compareTo(BigDecimal.ZERO)<0){
            return ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_ERROR;
        }
        if(customerRiskManagement.getDepositCycle()!=null&&customerRiskManagement.getDepositCycle()<0){
            return ErrorCode.CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_ERROR;
        }
        if(customerRiskManagement.getPaymentCycle()!=null&&customerRiskManagement.getPaymentCycle()<1){
            return ErrorCode.CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_ERROR;
        }
        return serviceResult;
    }
    @Override
    public ServiceResult<String, Integer> update(CustomerRiskManagement customerRiskManagement) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findById(customerRiskManagement.getCustomerRiskManagementId());
        if(customerRiskManagementDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            return serviceResult;
        }
        String checkErrorCode = check(customerRiskManagement);
        if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
            serviceResult.setErrorCode(checkErrorCode);
            return  serviceResult;
        }
        CustomerRiskManagementDO newCustomerRiskManagementDO = new CustomerRiskManagementDO();
        newCustomerRiskManagementDO.setId(customerRiskManagementDO.getId());
        newCustomerRiskManagementDO.setUpdateTime(new Date());
        newCustomerRiskManagementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        newCustomerRiskManagementDO.setCreditAmount(customerRiskManagement.getCreditAmount());
        newCustomerRiskManagementDO.setDepositCycle(customerRiskManagement.getDepositCycle());
        newCustomerRiskManagementDO.setPaymentCycle(customerRiskManagement.getPaymentCycle());
        customerRiskManagementMapper.update(newCustomerRiskManagementDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(newCustomerRiskManagementDO.getId());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, CustomerRiskManagement> detail(String customerNo) {
        ServiceResult<String, CustomerRiskManagement> serviceResult = new ServiceResult<>()
;        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerDO.getId());
        CustomerRiskManagement customerRiskManagement = CustomerRiskManagementConverter.convertCustomerRiskManagementDO(customerRiskManagementDO);
        if(StringUtil.isNotEmpty(customerDO.getPersonName())){
            customerRiskManagement.setCustomerName(customerDO.getPersonName());
        }else if(StringUtil.isNotEmpty(customerDO.getCompanyName())){
            customerRiskManagement.setCustomerName(customerDO.getCompanyName());
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerRiskManagement);
        return serviceResult;
    }

    @Override
    public String delete(Integer customerRiskManagementId) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findById(customerRiskManagementId);
        if(customerRiskManagementDO==null){
            return ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS;
        }
        customerRiskManagementDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        customerRiskManagementDO.setUpdateTime(new Date());
        customerRiskManagementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerRiskManagementMapper.update(customerRiskManagementDO);
        return ErrorCode.SUCCESS;
    }
}
