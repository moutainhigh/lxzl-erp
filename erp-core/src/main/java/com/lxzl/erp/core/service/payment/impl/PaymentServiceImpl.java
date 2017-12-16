package com.lxzl.erp.core.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.PaymentSystemConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.payment.account.pojo.BalancePayParam;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccountQueryParam;
import com.lxzl.erp.common.domain.payment.account.pojo.ManualChargeParam;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.exception.BusinessException;
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

    @Override
    public CustomerAccount queryCustomerAccount(String customerNo) {
        CustomerAccountQueryParam param = new CustomerAccountQueryParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountURL, requestJson, headerBuilder, "UTF-8");
            Result result = JSON.parseObject(response, Result.class);
            if (ErrorCode.SUCCESS.equals(result.getCode())) {
                return JSON.parseObject(JSON.toJSONString(result.getResultMap().get("data")), CustomerAccount.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ServiceResult<String, Boolean> manualCharge(String customerNo, BigDecimal chargeAmount) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        ManualChargeParam param = new ManualChargeParam();
        param.setBusinessCustomerNo(customerNo);
        param.setChargeAmount(chargeAmount);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemManualChargeURL, requestJson, headerBuilder, "UTF-8");
            Result paymentResult = JSON.parseObject(response, Result.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            result.setErrorCode(paymentResult.getCode());
            return result;
        } catch (Exception e) {
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }
    }

    @Override
    public ServiceResult<String, Boolean> balancePay(String customerNo, BigDecimal payAmount) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        BalancePayParam param = new BalancePayParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessOrderAmount(payAmount);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemBalancePayURL, requestJson, headerBuilder, "UTF-8");
            Result paymentResult = JSON.parseObject(response, Result.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }
    }
}
