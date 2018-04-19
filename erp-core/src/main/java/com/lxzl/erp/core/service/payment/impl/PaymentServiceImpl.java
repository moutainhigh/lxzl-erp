package com.lxzl.erp.core.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.PaymentSystemConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.PaymentResult;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.*;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 18:01
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerAccount queryCustomerAccountNoLogin(String customerNo) {
        CustomerAccountQueryParam param = new CustomerAccountQueryParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountURL, requestJson, headerBuilder, "UTF-8");
            logger.info("query customer account no login response:{}", response);
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
    public CustomerAccount queryCustomerAccount(String customerNo) {
        CustomerAccountQueryParam param = new CustomerAccountQueryParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        Integer operateUser = userSupport.getCurrentUserId() == null ? CommonConstant.SUPER_USER_ID : userSupport.getCurrentUserId();
        param.setBusinessOperateUser(operateUser.toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountURL, requestJson, headerBuilder, "UTF-8");
            logger.info("query customer account response:{}", response);
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
            logger.info("manual charge response:{}", response);
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
            logger.info("manual deduct response:{}", response);
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
    public ServiceResult<String, Boolean> balancePay(String customerNo, String businessOrderNo, String businessOrderRemark, BigDecimal payRentAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount, BigDecimal payOtherAmount) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        BalancePayParam param = new BalancePayParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessOrderAmount(payRentAmount);
        param.setBusinessOrderDepositAmount(payDepositAmount);
        param.setBusinessOrderRentDepositAmount(payRentDepositAmount);
        param.setBusinessOrderOtherAmount(payOtherAmount);
        param.setBusinessOrderNo(businessOrderNo);
        param.setBusinessOrderRemark(businessOrderRemark);
        param.setBusinessNotifyUrl(null);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemBalancePayURL, requestJson, headerBuilder, "UTF-8");
            logger.info("balance pay response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((Boolean) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }

//            else{
//                result.setResult(Boolean.FALSE);
//                result.setErrorCode(paymentResult.getDescription());
//                return result;
//            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> wechatPay(String customerNo, String businessOrderNo, String businessOrderRemark, BigDecimal payAmount, String openId, String ip, Integer loginUserId) {
        ServiceResult<String, String> result = new ServiceResult<>();
        WeixinPayParam param = new WeixinPayParam();
        param.setBusinessCustomerNo(customerNo);
        param.setAmount(payAmount);
        param.setPayName("凌雄租赁");
        param.setPayDescription("凌雄租赁商品");
        param.setOpenId(openId);
        param.setBusinessOrderNo(businessOrderNo);
        param.setBusinessOrderRemark(businessOrderRemark);
        param.setBusinessNotifyUrl(PaymentSystemConfig.paymentSystemWeixinPayCallbackURL);
        param.setClientIp(ip);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(loginUserId.toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemWeixinPayURL, requestJson, headerBuilder, "UTF-8");
            logger.info("wechat pay response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((String) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> wechatCharge(String customerNo, BigDecimal amount, String openId, String ip) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        Date now = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        if (amount == null || BigDecimalUtil.compare(amount, BigDecimal.ZERO) <= 0) {
            result.setErrorCode(ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO);
            return result;
        }
        WeixinPayParam weixinPayParam = new WeixinPayParam();
        weixinPayParam.setBusinessCustomerNo(customerDO.getCustomerNo());
        weixinPayParam.setPayName("凌雄租赁");
        weixinPayParam.setAmount(amount);
        weixinPayParam.setPayDescription("凌雄租赁客户充值");
        weixinPayParam.setBusinessOrderNo(new SimpleDateFormat("yyyyMMddHHmmss").format(now));
        weixinPayParam.setOpenId(openId);
        weixinPayParam.setBusinessOrderRemark("TEST");
        weixinPayParam.setBusinessNotifyUrl(null);
        weixinPayParam.setClientIp(ip);
        weixinPayParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        weixinPayParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        weixinPayParam.setBusinessOperateUser(loginUserId.toString());

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(weixinPayParam);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemWeixinChargeURL, requestJson, headerBuilder, "UTF-8");
            logger.info("wechat charge response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                result.setResult((String) paymentResult.getResultMap().get("data"));
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Page<ChargeRecord>> queryChargeRecordPage(String customerNo) {
        ServiceResult<String, Page<ChargeRecord>> result = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        ChargeRecordPageParam chargeRecordPageParam = new ChargeRecordPageParam();
        chargeRecordPageParam.setBusinessCustomerNo(customerDO.getCustomerNo());
        chargeRecordPageParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        chargeRecordPageParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
            JSONObject jsonObject = JSON.parseObject(requestJson);
            jsonObject.remove("count");
            jsonObject.remove("subCompanyId");
            jsonObject.remove("count");
            if(jsonObject.containsKey("customerName") && StringUtil.isNotEmpty(jsonObject.get("customerName").toString())){
                jsonObject.put("businessCustomerName",jsonObject.get("customerName"));
            }else{
                jsonObject.remove("customerName");
            }
            requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryChargeRecordPageURL, requestJson, headerBuilder, "UTF-8");

            logger.info("query charge page response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Page<ChargeRecord> chargeRecordPage = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), Page.class);
                result.setResult(chargeRecordPage);
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Page<ChargeRecord>> queryChargeRecordParamPage(ChargeRecordPageParam chargeRecordPageParam) {

        ServiceResult<String, Page<ChargeRecord>> result = new ServiceResult<>();

        CustomerDO customerName = customerMapper.findByName(chargeRecordPageParam.getCustomerName());
        CustomerDO customerNo = customerMapper.findByNo(chargeRecordPageParam.getBusinessCustomerNo());


        if (customerName != null) {
            chargeRecordPageParam.setBusinessCustomerNo(customerName.getCustomerNo());
        } else if (customerNo != null) {
            chargeRecordPageParam.setBusinessCustomerNo(customerNo.getCustomerNo());
            chargeRecordPageParam.setCustomerName(customerNo.getCustomerName());
        }

        chargeRecordPageParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        chargeRecordPageParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = null;

            PaymentChargeRecordPageParam paymentChargeRecordPageParam = ConverterUtil.convert(chargeRecordPageParam ,PaymentChargeRecordPageParam.class);
            if (StringUtil.isEmpty(chargeRecordPageParam.getCustomerName())){
                paymentChargeRecordPageParam.setBusinessCustomerName(null);
            }else{
                paymentChargeRecordPageParam.setBusinessCustomerName(chargeRecordPageParam.getCustomerName());
            }
            if (chargeRecordPageParam.getSubCompanyId() != null){
                paymentChargeRecordPageParam.setChargeBodyId(chargeRecordPageParam.getSubCompanyId().toString());
            }
            if (StringUtil.isEmpty(chargeRecordPageParam.getBusinessCustomerNo())){
                paymentChargeRecordPageParam.setBusinessCustomerNo(null);
            }
            requestJson = JSON.toJSONString(paymentChargeRecordPageParam);


//            if ((chargeRecordPageParam.getCustomerName() != null && chargeRecordPageParam.getCustomerName() != "") && (chargeRecordPageParam.getBusinessCustomerNo() != null && chargeRecordPageParam.getBusinessCustomerNo() != "")) {
//                if (chargeRecordPageParam.getChargeType() != null) {
//                    if (chargeRecordPageParam.getChargeStatus() != null) {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    } else {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeStatus");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    }
//                } else {
//                    if (chargeRecordPageParam.getChargeStatus() != null) {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeType");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    } else {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeStatus");
//                        jsonObject.remove("chargeType");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    }
//                }
//            } else {
//                if (chargeRecordPageParam.getChargeType() != null) {
//                    if (chargeRecordPageParam.getChargeStatus() != null) {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("businessCustomerNo");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    } else {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeStatus");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("businessCustomerNo");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    }
//                } else {
//                    if (chargeRecordPageParam.getChargeStatus() != null) {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeType");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("businessCustomerNo");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    } else {
//                        requestJson = FastJsonUtil.toJSONString(chargeRecordPageParam);
//                        jsonObject = JSON.parseObject(requestJson);
//                        jsonObject.remove("chargeStatus");
//                        jsonObject.remove("chargeType");
//                        jsonObject.remove("customerName");
//                        jsonObject.remove("businessCustomerNo");
//                        jsonObject.remove("count");
//                        requestJson = jsonObject.toJSONString();
//                    }
//                }
//            }

            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryChargeRecordPageURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Page<JSONObject> paymentChargeRecordPage = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), Page.class);
                List<JSONObject> paymentChargeRecordPageList = paymentChargeRecordPage.getItemList();

                Page<ChargeRecord> chargeRecordPage = new Page<>();
                List<ChargeRecord> chargeRecordList = new ArrayList<>();
                for(JSONObject jsonObject : paymentChargeRecordPageList){
                    ChargeRecord chargeRecord = JSON.parseObject(jsonObject.toJSONString(), ChargeRecord.class);
                    if (jsonObject.get("chargeBodyId") != null){
                        chargeRecord.setSubCompanyId(Integer.parseInt(jsonObject.get("chargeBodyId").toString()));
                    }
                    if (jsonObject.get("chargeBodyName") != null){
                        chargeRecord.setSubCompanyName(jsonObject.get("chargeBodyName").toString());
                    }
                    if (jsonObject.get("businessCustomerName") != null){
                        chargeRecord.setCustomerName(jsonObject.get("businessCustomerName").toString());
                    }
//                    ChargeRecord chargeRecord = JSON.parseObject(JSON.toJSONString(paymentChargeRecord),ChargeRecord.class);
//                    PaymentChargeRecord paymentChargeRecordPojo = JSONUtil.parseObject(paymentChargeRecord, PaymentChargeRecord.class);
//                    ChargeRecord chargeRecord = ConverterUtil.convert(paymentChargeRecordPojo,ChargeRecord.class);
                    chargeRecordList.add(chargeRecord);
                }

//                for (int i = 0; i < paymentChargeRecordPageList.size(); i++) {
//                    ChargeRecord chargeRecord = JSONUtil.parseObject(paymentChargeRecordPageList.get(i), ChargeRecord.class);
//                    if (StringUtil.isBlank(chargeRecord.getBusinessCustomerName())) {
//                        CustomerDO dbCustomerDO = customerMapper.findByNo(chargeRecord.getBusinessCustomerNo());
//                        if (dbCustomerDO == null) {
//                            chargeRecord.setCustomerName("");
//                        } else {
//                            chargeRecord.setCustomerName(dbCustomerDO.getCustomerName());
//                        }
//                    } else {
//                        chargeRecord.setCustomerName(chargeRecord.getBusinessCustomerName());
//                    }
//                }

                chargeRecordPage.setItemList(chargeRecordList);
                chargeRecordPage.setPageSize(paymentChargeRecordPage.getPageSize());
                chargeRecordPage.setPageCount(paymentChargeRecordPage.getPageCount());
                chargeRecordPage.setTotalCount(paymentChargeRecordPage.getTotalCount());
                chargeRecordPage.setCurrentPage(paymentChargeRecordPage.getCurrentPage());

                result.setResult(chargeRecordPage);
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            } else if ("J000031".equals(paymentResult.getCode())) {
                Page<ChargeRecord> page = new Page<>();
                result.setResult(page);
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
            logger.info("return deposit response:{}", response);
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
    public ServiceResult<String, PayResult> queryPayResult(String orderNo, Integer payType, String customerNo) {
        ServiceResult<String, PayResult> result = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        PayResultQueryParam payResultQueryParam = new PayResultQueryParam();
        payResultQueryParam.setBusinessCustomerNo(customerDO.getCustomerNo());
        payResultQueryParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        payResultQueryParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        payResultQueryParam.setBusinessOrderNo(orderNo);
        payResultQueryParam.setPayType(payType.toString());

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(payResultQueryParam);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryPayResultURL, requestJson, headerBuilder, "UTF-8");
            logger.info("query pay result response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                PayResult payResult = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), PayResult.class);
                result.setResult(payResult);
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Map<String, Object>> queryCustomerAccountLogPage(CustomerAccountLogParam customerAccountLogParam) {
        ServiceResult<String, Map<String, Object>> result = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerAccountLogParam.getBusinessCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        customerAccountLogParam.setBusinessCustomerNo(customerDO.getCustomerNo());
        customerAccountLogParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        customerAccountLogParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");

            String requestJson;
            JSONObject jsonObject;
            if (customerAccountLogParam.getCustomerAccountLogType() == null) {
                requestJson = FastJsonUtil.toJSONString(customerAccountLogParam);
                jsonObject = JSON.parseObject(requestJson);
                jsonObject.remove("customerAccountLogType");
                jsonObject.remove("count");
                requestJson = jsonObject.toJSONString();
            } else {
                requestJson = FastJsonUtil.toJSONString(customerAccountLogParam);
                jsonObject = JSON.parseObject(requestJson);
                jsonObject.remove("count");
                requestJson = jsonObject.toJSONString();
            }
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountLogPageURL, requestJson, headerBuilder, "UTF-8");

            logger.info("query Customer Account Log Page response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Map<String, Object> map = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), HashMap.class);

                result.setResult(map);
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Map<String, Object>> weixinQueryCustomerAccountLogPage(InterfaceCustomerAccountLogParam param) {
        ServiceResult<String, Map<String, Object>> result = new ServiceResult<>();

        CustomerAccountLogParam customerAccountLogParam = new CustomerAccountLogParam();
        customerAccountLogParam.setPageSize(param.getPageSize());
        customerAccountLogParam.setPageNo(param.getPageNo());
        customerAccountLogParam.setBusinessCustomerNo(param.getCustomerNo());
        customerAccountLogParam.setCustomerAccountLogType(param.getCustomerAccountLogType());
        customerAccountLogParam.setQueryStartTime(param.getQueryStartTime());
        customerAccountLogParam.setQueryEndTime(param.getQueryEndTime());
        customerAccountLogParam.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        customerAccountLogParam.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");

            String requestJson;
            JSONObject jsonObject;
            if (customerAccountLogParam.getCustomerAccountLogType() == null) {
                requestJson = FastJsonUtil.toJSONString(customerAccountLogParam);
                jsonObject = JSON.parseObject(requestJson);
                jsonObject.remove("customerAccountLogType");
                jsonObject.remove("count");
                requestJson = jsonObject.toJSONString();
            } else {
                requestJson = FastJsonUtil.toJSONString(customerAccountLogParam);
                jsonObject = JSON.parseObject(requestJson);
                jsonObject.remove("count");
                requestJson = jsonObject.toJSONString();
            }
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryCustomerAccountLogPageURL, requestJson, headerBuilder, "UTF-8");

            logger.info("weixin Query Customer Account Log Page response:{}", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Map<String, Object> map = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), HashMap.class);

                result.setResult(map);
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(paymentResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

}
