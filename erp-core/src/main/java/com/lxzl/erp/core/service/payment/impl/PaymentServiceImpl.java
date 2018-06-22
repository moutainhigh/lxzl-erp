package com.lxzl.erp.core.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.PaymentSystemConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.PaymentResult;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.PayResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipClaimMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailOperationLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.ParseException;
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
    @Autowired
    private BankSlipMapper bankSlipMapper;
    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;
    @Autowired
    private BankSlipClaimMapper bankSlipClaimMapper;
    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;
    @Autowired
    private BankSlipSupport bankSlipSupport;

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
    public String returnDepositExpand(String customerNo, BigDecimal businessReturnRentAmount, BigDecimal businessReturnOtherAmount, BigDecimal businessReturnRentDepositAmount,
                                      BigDecimal businessReturnDepositAmount, String remark) {
        ReturnDepositExpandParam param = new ReturnDepositExpandParam();
        param.setBusinessCustomerNo(customerNo);
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        Integer operateUser = userSupport.getCurrentUserId() == null ? CommonConstant.SUPER_USER_ID : userSupport.getCurrentUserId();
        param.setBusinessOperateUser(operateUser.toString());
        param.setBusinessReturnDepositAmount(businessReturnDepositAmount);
        param.setBusinessReturnOtherAmount(businessReturnOtherAmount);
        param.setBusinessReturnRentAmount(businessReturnRentAmount);
        param.setBusinessReturnRentDepositAmount(businessReturnRentDepositAmount);
        param.setRemark(remark);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemReturnDepositExpandURL, requestJson, headerBuilder, "UTF-8");
            logger.info("returnDepositExpand response:", response);
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (paymentResult == null) {
                throw new BusinessException("支付网关没有响应，强制取消已支付订单失败");
            }
            if (!ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                throw new BusinessException(paymentResult.getDescription());
            }
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
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
    public ServiceResult<String, Boolean> publicTransferPlusCharge(PublicTransferPlusChargeParam param) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        param.setBusinessAppId(PaymentSystemConfig.paymentSystemAppId);
        param.setBusinessAppSecret(PaymentSystemConfig.paymentSystemAppSecret);
        param.setBusinessOperateUser(userSupport.getCurrentUserId().toString());
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemPublicTransferPlusChargeURL, requestJson, headerBuilder, "UTF-8");
            logger.info("public transfer plus charge response:{}", response);
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
            if (jsonObject.containsKey("customerName") && StringUtil.isNotEmpty(jsonObject.get("customerName").toString())) {
                jsonObject.put("businessCustomerName", jsonObject.get("customerName"));
            } else {
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

            PaymentChargeRecordPageParam paymentChargeRecordPageParam = ConverterUtil.convert(chargeRecordPageParam, PaymentChargeRecordPageParam.class);
            if (StringUtil.isEmpty(chargeRecordPageParam.getCustomerName())) {
                paymentChargeRecordPageParam.setBusinessCustomerName(null);
            } else {
                paymentChargeRecordPageParam.setBusinessCustomerName(chargeRecordPageParam.getCustomerName());
            }
            if (chargeRecordPageParam.getSubCompanyId() != null) {
                paymentChargeRecordPageParam.setChargeBodyId(chargeRecordPageParam.getSubCompanyId().toString());
            }
            if (StringUtil.isEmpty(chargeRecordPageParam.getBusinessCustomerNo())) {
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
//            String response = HttpClientUtil.post("http://testpayment.52rental.com/payment-system/charge/queryChargeRecordPage", requestJson, headerBuilder, "UTF-8");
            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryChargeRecordPageURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Page<JSONObject> paymentChargeRecordPage = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), Page.class);
                List<JSONObject> paymentChargeRecordPageList = paymentChargeRecordPage.getItemList();

                Page<ChargeRecord> chargeRecordPage = new Page<>();
                List<ChargeRecord> chargeRecordList = new ArrayList<>();
                for (JSONObject jsonObject : paymentChargeRecordPageList) {
                    ChargeRecord chargeRecord = JSON.parseObject(jsonObject.toJSONString(), ChargeRecord.class);
                    if (jsonObject.get("chargeBodyId") != null) {
                        chargeRecord.setSubCompanyId(Integer.parseInt(jsonObject.get("chargeBodyId").toString()));
                    }
                    if (jsonObject.get("chargeBodyName") != null) {
                        chargeRecord.setSubCompanyName(jsonObject.get("chargeBodyName").toString());
                    }
                    if (jsonObject.get("businessCustomerName") != null) {
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
        User loginUser = userSupport.getCurrentUser();
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID:loginUser.getUserId();
        param.setBusinessOperateUser(loginUserId.toString());
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

    @Override
    public ServiceResult<String, String> exportHistoryChargeRecord(ExportChargeRecordPageParam exportChargeRecordPageParam) throws ParseException {
        //传入查询参数
        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setChannelNo(exportChargeRecordPageParam.getChannelNo());
        param.setPageNo(CommonConstant.COMMON_ONE);
        param.setPageSize(Integer.MAX_VALUE);
        param.setChargeStatus(ChargeStatus.PAY_SUCCESS);
        param.setQueryStartTime(exportChargeRecordPageParam.getStartTime());
        param.setQueryEndTime(exportChargeRecordPageParam.getEndTime());
        param.setSubCompanyId(exportChargeRecordPageParam.getSubCompanyId());
        param.setBusinessCustomerNo(exportChargeRecordPageParam.getCustomerNo());
        //调接口查询结果
        ServiceResult<String, Page<ChargeRecord>> result = queryChargeRecordParamPage(param);
        List<ChargeRecord> itemList = result.getResult().getItemList();

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (CollectionUtil.isEmpty(itemList)) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }
        //过滤已有的数据
        Map<String, ChargeRecord> chargeRecordMap = ListUtil.listToMap(itemList, "thirdPartyPayOrderId");
        List<String> chargeRecordList = new ArrayList<>(chargeRecordMap.keySet());
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findBankSlipDetailByTradeSerialNoList(chargeRecordList);
        if(CollectionUtil.isNotEmpty(bankSlipDetailDOList)){
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                chargeRecordMap.remove(bankSlipDetailDO.getTradeSerialNo());
            }
            itemList = ListUtil.mapToList(chargeRecordMap);
        }
        //保存过滤完成的数据
        if(CollectionUtil.isNotEmpty(itemList)){
            for (ChargeRecord chargeRecord : itemList) {
                saveConstantlyExportQueryChargeRecordToBankSlip(chargeRecord);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> constantlyExportQueryChargeRecord(ExportChargeRecordPageParam exportChargeRecordPageParam) throws ParseException {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setPageNo(CommonConstant.COMMON_ONE);
        param.setPageSize(Integer.MAX_VALUE);
        param.setChargeStatus(ChargeStatus.PAY_SUCCESS);
        param.setChargeOrderNo(exportChargeRecordPageParam.getChargeOrderNo());

        ServiceResult<String, Page<ChargeRecord>> result = queryChargeRecordParamPage(param);
        if(!ErrorCode.SUCCESS.equals(result.getErrorCode())){
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }

        if(CollectionUtil.isEmpty(result.getResult().getItemList())){
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return  serviceResult;
        }
        return saveConstantlyExportQueryChargeRecordToBankSlip(result.getResult().getItemList().get(0));
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    ServiceResult<String, String> saveConstantlyExportQueryChargeRecordToBankSlip(ChargeRecord chargeRecord) throws ParseException {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        if (chargeRecord == null) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }

        //判断是否有重复数据
        BankSlipDetailDO dbBankSlipDetailDO = bankSlipDetailMapper.findBankSlipDetailByTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());
        if(dbBankSlipDetailDO != null){
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_EXIST);
            return serviceResult;
        }

        // 添加操作日志
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(chargeRecord.getChargeTime());
        Date chargeTime = simpleDateFormat.parse(format);
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        try {
            //保存流水总纪录
            BankSlipDO bankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(chargeRecord.getSubCompanyId(), BankType.LYCHEE_PAY, chargeTime);
            if (bankSlipDO != null) {
                bankSlipDO.setInCount(bankSlipDO.getInCount() + 1);   //进款笔数
                bankSlipDO.setConfirmCount(bankSlipDO.getConfirmCount() + 1);   //已确认笔数
                //跟新数据
                bankSlipMapper.update(bankSlipDO);
            } else {
                //保存银行流水
                bankSlipDO = new BankSlipDO();
                bankSlipDO.setSubCompanyId(chargeRecord.getSubCompanyId());   //分公司ID
                bankSlipDO.setSubCompanyName(chargeRecord.getSubCompanyName());   //分公司名称
                bankSlipDO.setBankType(BankType.LYCHEE_PAY);   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
                bankSlipDO.setSlipDay(chargeTime);   //导入日期
                bankSlipDO.setInCount(CommonConstant.COMMON_ONE);   //进款笔数
                bankSlipDO.setNeedClaimCount(CommonConstant.COMMON_ZERO);
                bankSlipDO.setClaimCount(CommonConstant.COMMON_ZERO);
                bankSlipDO.setConfirmCount(CommonConstant.COMMON_ONE);   //已确认笔数
                bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
                bankSlipDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);   //状态：0不可用；1可用；2删除
                bankSlipDO.setCreateTime(now);   //添加时间
                bankSlipDO.setCreateUser(userSupport.getCurrentUserId().toString());   //添加人
                bankSlipDO.setUpdateTime(now);   //修改时间
                bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());   //修改人
                bankSlipDO.setLocalizationCount(CommonConstant.COMMON_ZERO);  //属地化数量
                bankSlipMapper.save(bankSlipDO);
            }

            //保存流水记录
            BankSlipDetailDO bankSlipDetailDO = new BankSlipDetailDO();
            bankSlipDetailDO.setPayerName(chargeRecord.getCustomerName());       //付款人名称
            bankSlipDetailDO.setTradeAmount(chargeRecord.getChargeAmountReal());       //交易金额
            bankSlipDetailDO.setTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());       //交易流水号
            bankSlipDetailDO.setTradeTime(chargeTime);       //交易日期
            bankSlipDetailDO.setTradeMessage(chargeRecord.getRemark());       //交易附言
            bankSlipDetailDO.setOtherSideAccountNo(chargeRecord.getOpenId());       //对方账号
            bankSlipDetailDO.setMerchantOrderNo(null);       //商户订单号
            bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);       //借贷标志,1-贷（收入），2-借（支出）
            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CONFIRMED);       //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
            bankSlipDetailDO.setDetailJson(null);       //明细json数据
            bankSlipDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);       //状态：0不可用；1可用；2删除
            bankSlipDetailDO.setRemark(chargeRecord.getRemark());       //备注
            bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());       //银行对公流水表id
            bankSlipDetailDO.setSubCompanyId(chargeRecord.getSubCompanyId());      //属地化分公司ID
            bankSlipDetailDO.setIsLocalization(CommonConstant.NO);       //是否已属地化,0-否，1-是[总公司时有值]
            bankSlipDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setCreateTime(now);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
            bankSlipDetailMapper.save(bankSlipDetailDO);

            //保存认领信息
            BankSlipClaimDO bankSlipClaimDO = new BankSlipClaimDO();
            bankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());   //银行对公流水明细ID
            bankSlipClaimDO.setOtherSideAccountNo(chargeRecord.getOpenId());   //对方账号
            bankSlipClaimDO.setCustomerNo(chargeRecord.getBusinessCustomerNo());   //客戶编码
            bankSlipClaimDO.setClaimAmount(chargeRecord.getChargeAmountReal());   //认领金额
            bankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());   //认领流水号（时间戳）
            bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_SUCCESS);   //充值状态，0-初始化，1-正在充值，2-充值成功，3-充值失败
            bankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);   //状态：0不可用；1可用；2删除
            bankSlipClaimDO.setRemark(chargeRecord.getRemark());   //备注
            bankSlipClaimDO.setCreateTime(now);   //添加时间
            bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());   //添加人
            bankSlipClaimDO.setUpdateTime(now);   //修改时间
            bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipClaimMapper.save(bankSlipClaimDO);
            //加款
            bankSlipSupport.constantlyPaymentClaim(bankSlipClaimDO, bankSlipDO, bankSlipDetailDO,bankSlipDetailOperationLogDO, now);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.EXPORT_CHARGE_RECORD_IS_FAIL);
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> exportTodayLeaveOutChargeRecord() throws ParseException {
        ExportChargeRecordPageParam exportChargeRecordPageParam = new ExportChargeRecordPageParam();
        exportChargeRecordPageParam.setStartTime(DateUtil.getDayByCurrentOffset(CommonConstant.COMMON_ZERO));
        exportChargeRecordPageParam.setEndTime(DateUtil.getDayByCurrentOffset(CommonConstant.COMMON_ONE));
        return exportHistoryChargeRecord(exportChargeRecordPageParam);
    }

}
