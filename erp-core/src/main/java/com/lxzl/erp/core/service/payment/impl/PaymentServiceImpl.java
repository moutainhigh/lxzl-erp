package com.lxzl.erp.core.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.PaymentSystemConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.PaymentResult;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.*;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.se.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 18:01
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserSupport userSupport;

    @Override
    public CustomerAccount queryCustomerAccount(String customerNo) {
        CustomerAccountQueryParam param = new CustomerAccountQueryParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                return JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), CustomerAccount.class);
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Boolean> manualCharge(ManualChargeParam param) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemManualChargeURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Boolean> manualDeduct(ManualDeductParam param) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemManualDeductURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Boolean> balancePay(String customerNo, String businessOrderNo, String businessOrderRemark, String businessNotifyUrl, BigDecimal payAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        BalancePayParam param = new BalancePayParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessOrderAmount(payAmount);
        param.setBusinessOrderDepositAmount(payDepositAmount);
        param.setBusinessOrderRentDepositAmount(payRentDepositAmount);
        param.setBusinessOrderNo(businessOrderNo);
        param.setBusinessOrderRemark(businessOrderRemark);
        param.setBusinessNotifyUrl(businessNotifyUrl);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemBalancePayURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Boolean> returnDeposit(String customerNo, BigDecimal returnRentDepositAmount, BigDecimal returnDepositAmount) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        ReturnDepositParam param = new ReturnDepositParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessReturnRentDepositAmount(returnRentDepositAmount);
        param.setBusinessReturnDepositAmount(returnDepositAmount);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemReturnDepositURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
