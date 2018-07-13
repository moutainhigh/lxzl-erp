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
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
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
    private CustomerCompanyMapper customerCompanyMapper;

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

            requestJson = JSON.toJSONString(paymentChargeRecordPageParam);

            String response = HttpClientUtil.post(PaymentSystemConfig.paymentSystemQueryChargeRecordPageURL, requestJson, headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                Page<JSONObject> paymentChargeRecordPage = JSON.parseObject(JSON.toJSONString(paymentResult.getResultMap().get("data")), Page.class);
                List<JSONObject> paymentChargeRecordPageList = paymentChargeRecordPage.getItemList();

                Page<ChargeRecord> chargeRecordPage = new Page<>();
                List<ChargeRecord> chargeRecordList = new ArrayList<>();
                List<String> customerNoList = new ArrayList<>();
                List<String> customerNameList = new ArrayList<>();
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
                    if (chargeRecord.getBusinessCustomerNo().startsWith("LX")) {
                        customerNoList.add(chargeRecord.getBusinessCustomerNo());
                    }else if(StringUtil.isNotEmpty(chargeRecord.getCustomerName())){
                        customerNameList.add(chargeRecord.getCustomerName());
                    }
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
                Map<Object, CustomerDO> customerNoMap = new HashMap<>();
                Map<Object, CustomerDO> customerNameMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(customerNoList)) {
                    List<CustomerDO> customerDONoList = customerMapper.findByCustomerNoList(customerNoList);
                    customerNoMap = ListUtil.listToMap(customerDONoList, "customerNo");
                }
                if (CollectionUtil.isNotEmpty(customerNameList)) {
                    List<CustomerDO> customerDONameList = customerMapper.findByCustomerNameList(customerNameList);
                    customerNameMap = ListUtil.listToMap(customerDONameList, "customerName");
                }

                if (CollectionUtil.isNotEmpty(chargeRecordList)){
                    for (ChargeRecord chargeRecord : chargeRecordList){
                        if (customerNoMap.get(chargeRecord.getBusinessCustomerNo()) != null){
                            CustomerDO customerDO = customerNoMap.get(chargeRecord.getBusinessCustomerNo());
                            chargeRecord.setSubCompanyId(customerDO.getOwnerSubCompanyId());
                            chargeRecord.setSubCompanyName(customerDO.getOwnerSubCompanyName());
                            chargeRecord.setCustomerName(customerDO.getCustomerName());
                            chargeRecord.setErpCustomerNo(customerDO.getCustomerNo());
                        }else if(customerNameMap.get(chargeRecord.getCustomerName()) != null){
                            CustomerDO customerDO = customerNameMap.get(chargeRecord.getBusinessCustomerNo());
                            chargeRecord.setErpCustomerNo(customerDO.getCustomerNo());
                        }
                    }
                }

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
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addOnlineBankSlip(AddOnlineBankSlipQueryParam addOnlineBankSlipQueryParam) throws ParseException {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setPageNo(CommonConstant.COMMON_ONE);
        param.setPageSize(Integer.MAX_VALUE);
        param.setChargeStatus(ChargeStatus.PAY_SUCCESS);
        param.setChargeOrderNo(addOnlineBankSlipQueryParam.getChargeOrderNo());

        ServiceResult<String, Page<ChargeRecord>> result = queryChargeRecordParamPage(param);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }

        if (CollectionUtil.isEmpty(result.getResult().getItemList())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_NOT_EXISTS);
            return serviceResult;
        }

        if (result.getResult().getItemList().size() != 1) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_DATA_FAIL);
            return serviceResult;
        }

        return saveChargeRecordToBankSlip(result.getResult().getItemList().get(0));

    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    ServiceResult<String, String> saveChargeRecordToBankSlip(ChargeRecord chargeRecord) throws ParseException {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        String userId = CommonConstant.SUPER_USER_ID.toString();

        //过滤手动充值,余额充值,对公加款
        if (ChargeType.MANUAL_CHARGE.equals(chargeRecord.getChargeType()) || ChargeType.BALANCE_PAID.equals(chargeRecord.getChargeType()) || ChargeType.BANK_SLIP_CHARGE.equals(chargeRecord.getChargeType())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_TYPE_IS_MANUAL_CHARGE_OR_PUBLIC_TRANSFER_PLUS);
            return serviceResult;
        }

        //过滤绑定过的客户(已经打款无需再次认领打款)
        CustomerDO customerDO = customerMapper.findByNo(chargeRecord.getBusinessCustomerNo());
        if (customerDO != null) {
            serviceResult.setErrorCode(ErrorCode.BINDING_CUSTOMER_NO);
            return serviceResult;
        }

        //判断是否有重复数据
        BankSlipDetailDO dbBankSlipDetailDO = bankSlipDetailMapper.findBankSlipDetailByTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());
        if (dbBankSlipDetailDO != null) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_EXIST);
            return serviceResult;
        }

        //导入时间统一
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(chargeRecord.getChargeTime());
        Date chargeTime = simpleDateFormat.parse(format);

        Integer channelType = null;
        if(ChannelType.LYCHEE_PAY.equals(chargeRecord.getChannelType())){
            channelType = BankType.LYCHEE_PAY;
        }else if(ChannelType.SWIFT_PASS.equals(chargeRecord.getChannelType())){
            channelType = BankType.SWIFT_PASS;
        }

        //保存流水总纪录
        BankSlipDO bankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(chargeRecord.getSubCompanyId(), channelType, chargeTime);
        if (bankSlipDO != null) {
            bankSlipDO.setInCount(bankSlipDO.getInCount() + 1);   //进款笔数
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);   //需认领笔数
            if (SlipStatus.ALL_CLAIM.equals(bankSlipDO.getSlipStatus())) {
                bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
            }
            bankSlipDO.setUpdateUser(userId);
            bankSlipDO.setUpdateTime(now);
            //跟新数据
            bankSlipMapper.update(bankSlipDO);
        } else {
            //保存银行流水
            bankSlipDO = new BankSlipDO();
            bankSlipDO.setSubCompanyId(chargeRecord.getSubCompanyId());   //分公司ID
            bankSlipDO.setSubCompanyName(chargeRecord.getSubCompanyName());   //分公司名称
            bankSlipDO.setBankType(channelType);   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行,11-汉口银行,12-快付通,13-库存现金,14-威富通
            bankSlipDO.setSlipDay(chargeTime);   //导入日期
            bankSlipDO.setInCount(CommonConstant.COMMON_ONE);   //进款笔数
            bankSlipDO.setNeedClaimCount(CommonConstant.COMMON_ONE);
            bankSlipDO.setClaimCount(CommonConstant.COMMON_ZERO);
            bankSlipDO.setConfirmCount(CommonConstant.COMMON_ZERO);   //已确认笔数
            bankSlipDO.setSlipStatus(SlipStatus.INITIALIZE);   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
            bankSlipDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);   //状态：0不可用；1可用；2删除
            bankSlipDO.setCreateTime(now);   //添加时间
            bankSlipDO.setCreateUser(userId);   //添加人
            bankSlipDO.setUpdateTime(now);   //修改时间
            bankSlipDO.setUpdateUser(userId);   //修改人
            bankSlipDO.setLocalizationCount(CommonConstant.COMMON_ZERO);  //属地化数量
            bankSlipMapper.save(bankSlipDO);
        }

        //保存流水项记录
        BankSlipDetailDO bankSlipDetailDO = new BankSlipDetailDO();
        bankSlipDetailDO.setPayerName(chargeRecord.getCustomerName());       //付款人名称
        bankSlipDetailDO.setTradeAmount(chargeRecord.getChargeAmountReal());       //交易金额
        bankSlipDetailDO.setTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());       //交易流水号
        bankSlipDetailDO.setTradeTime(chargeTime);       //交易日期
        bankSlipDetailDO.setTradeMessage(chargeRecord.getRemark());       //交易附言
        bankSlipDetailDO.setOtherSideAccountNo(chargeRecord.getBusinessCustomerNo());       //对方账号
        bankSlipDetailDO.setMerchantOrderNo(null);       //商户订单号
        bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);       //借贷标志,1-贷（收入），2-借（支出）
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);       //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
        bankSlipDetailDO.setDetailJson(null);       //明细json数据
        bankSlipDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);       //状态：0不可用；1可用；2删除
        bankSlipDetailDO.setRemark(chargeRecord.getRemark());       //备注
        bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());       //银行对公流水表id
        bankSlipDetailDO.setSubCompanyId(chargeRecord.getSubCompanyId());      //属地化分公司ID
        bankSlipDetailDO.setIsLocalization(CommonConstant.NO);       //是否已属地化,0-否，1-是[总公司时有值]
        bankSlipDetailDO.setCreateUser(userId);
        bankSlipDetailDO.setCreateTime(now);
        bankSlipDetailDO.setUpdateUser(userId);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.save(bankSlipDetailDO);

        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<>();
        bankSlipDetailDOList.add(bankSlipDetailDO);
        bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
        //自动认领和自动属地化
        automaticClaimAndLocalization(bankSlipDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 自动认领和自动归属化
     *
     * @param : result
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 11:31
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    void automaticClaimAndLocalization(BankSlipDO bankSlipDO) {
        Date now = new Date();
        String userId = CommonConstant.SUPER_USER_ID.toString();

        //跟新 需要认领数据
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
        //查一边是否是 流入金额
        List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();

        // 查询出导入时间的所有本公司的所有导入数据
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
                newBankSlipDetailDOList.add(bankSlipDetailDO);
            }
        }

        //查询一边所有本银行认领 只有一条认领数据的认领数据
        List<BankSlipClaimDO> dbBankSlipClaimDOList = bankSlipClaimMapper.findBankSlipClaimPaySuccess();

        //数据库的当前银行所有的已确认的银行流水项
        Map<String, BankSlipClaimDO> bankSlipClaimDOMap = ListUtil.listToMap(dbBankSlipClaimDOList, "otherSideAccountNo");

        //上传的当前银行所有的已确认的银行流水项
        Map<Integer, BankSlipDetailDO> newBankSlipDetailDOMap = ListUtil.listToMap(newBankSlipDetailDOList, "id");

        //认领数据批量跟新
        List<BankSlipClaimDO> bankSlipClaimDOList = new ArrayList<>();

        //没有认领的数据
        List<BankSlipDetailDO> lastBankSlipDetailDOList = new ArrayList<>();

        //没有认领的数据
        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        //对公流水项批量跟新
        int claimCount = 0;
        for (Integer id : newBankSlipDetailDOMap.keySet()) {
            //如果是就创建一条认领数据
            BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(id);
            String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
            if (bankSlipClaimDOMap.containsKey(otherSideAccountNo)) {
                BankSlipClaimDO bankSlipClaimDO = bankSlipClaimDOMap.get(otherSideAccountNo);
                BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                newBankSlipClaimDO.setBankSlipDetailId(id);
                newBankSlipClaimDO.setOtherSideAccountNo(otherSideAccountNo);
                newBankSlipClaimDO.setCustomerNo(bankSlipClaimDO.getCustomerNo());
                newBankSlipClaimDO.setCustomerName(bankSlipClaimDO.getCustomerName());
                newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
                newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                newBankSlipClaimDO.setCreateUser(userId);
                newBankSlipClaimDO.setCreateTime(now);
                newBankSlipClaimDO.setUpdateUser(userId);
                newBankSlipClaimDO.setUpdateTime(now);
                bankSlipClaimDOList.add(newBankSlipClaimDO);
                //改变流水项状态
                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                //已认领数量
                claimCount = claimCount + 1;
                // 添加已有认领数据自动认领操作日志
                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
                bankSlipDetailOperationLogDO.setOperationContent("自动认领(已有的认领数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + id + ",认领人：" + "系统" + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                bankSlipDetailOperationLogDO.setCreateTime(now);
                bankSlipDetailOperationLogDO.setCreateUser(userId);
                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
            } else {
                lastBankSlipDetailDOList.add(bankSlipDetailDO);
            }
        }


        //自动认领付款人名称和已有的公司简单名称相同的数据

        if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
            List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
            for (BankSlipDetailDO bankSlipDetailDO : lastBankSlipDetailDOList) {
                String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
                customerCompanyDO.setSimpleCompanyName(simpleName);
                customerCompanyDOList.add(customerCompanyDO);
            }
            List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
            if (CollectionUtil.isNotEmpty(dbCustomerCompanyDOList)) {
                Map<String, CustomerCompanyDO> customerCompanyDOMap = ListUtil.listToMap(dbCustomerCompanyDOList, "simpleCompanyName");

                Iterator<BankSlipDetailDO> iter = lastBankSlipDetailDOList.iterator();
                aaa:
                while (iter.hasNext()) {
                    BankSlipDetailDO bankSlipDetailDO = iter.next();
                    String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                    if (simple == null || "".equals(simple)) {
                        iter.remove();
                        continue aaa;
                    }
                    if (customerCompanyDOMap.containsKey(simple)) {
                        CustomerCompanyDO customerCompanyDO = customerCompanyDOMap.get(simple);

                        BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                        newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                        newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                        newBankSlipClaimDO.setCustomerNo(customerCompanyDO.getCustomerNo());
                        newBankSlipClaimDO.setCustomerName(customerCompanyDO.getSimpleCompanyName());
                        newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
                        newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                        newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                        newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        newBankSlipClaimDO.setCreateUser(userId);
                        newBankSlipClaimDO.setCreateTime(now);
                        newBankSlipClaimDO.setUpdateUser(userId);
                        newBankSlipClaimDO.setUpdateTime(now);
                        bankSlipClaimDOList.add(newBankSlipClaimDO);
                        //改变流水项状态
                        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                        //已认领数量
                        claimCount = claimCount + 1;
                        // 添加操作日志
                        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
                        bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + "系统" + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + customerCompanyDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        bankSlipDetailOperationLogDO.setCreateTime(now);
                        bankSlipDetailOperationLogDO.setCreateUser(userId);
                        bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                    } else {
                        iter.remove();
                    }
                }
            }
        }

        //改变总表的已认领数量和未认领数量
        bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - claimCount);
        bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + claimCount);

        //创建各个数据批量存储
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            bankSlipClaimMapper.saveBankSlipClaimDO(bankSlipClaimDOList);
        }
        if (CollectionUtil.isNotEmpty(newBankSlipDetailDOList)) {
            bankSlipDetailMapper.updateBankSlipDetailDO(newBankSlipDetailDOList);
            bankSlipMapper.update(bankSlipDO);
        }
        //跟新付款人名称和已有的公司简单名称相同的数据 的流水详情表状态
        if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
            bankSlipDetailMapper.updateBankSlipDetailDO(lastBankSlipDetailDOList);
        }

        //如果是总公司才操作
        List<BankSlipDetailDO> headerBankSlipDetailDOList = new ArrayList<>();
        //查询所有已归属的总公司数据
        List<BankSlipDetailDO> localizationBankSlipDetailDOList = bankSlipDetailMapper.findLocalizationBankSlipDetailDO();
        Map<String, BankSlipDetailDO> localizationBankSlipDetailDOMap = ListUtil.listToMap(localizationBankSlipDetailDOList, "otherSideAccountNo");
        int localizationCount = 0;
        for (Integer key : newBankSlipDetailDOMap.keySet()) {
            BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(key);
            String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
            if (localizationBankSlipDetailDOMap.containsKey(otherSideAccountNo)) {
                BankSlipDetailDO dbBankSlipDetailDO = localizationBankSlipDetailDOMap.get(otherSideAccountNo);
                bankSlipDetailDO.setSubCompanyId(dbBankSlipDetailDO.getSubCompanyId());
                bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_CONSTANT_YES);
                bankSlipDetailDO.setUpdateUser(userId);
                bankSlipDetailDO.setUpdateTime(now);
                localizationCount++;
                headerBankSlipDetailDOList.add(bankSlipDetailDO);
                // 添加操作日志
                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_LOCALIZATION);
                bankSlipDetailOperationLogDO.setOperationContent("自动属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + "系统" + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + dbBankSlipDetailDO.getSubCompanyName());
                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);

                bankSlipDetailOperationLogDO.setCreateTime(now);
                bankSlipDetailOperationLogDO.setCreateUser(userId);
                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
            }
        }
        if (CollectionUtil.isNotEmpty(headerBankSlipDetailDOList)) {
            bankSlipDetailMapper.updateSubCompanyAndIsLocalization(headerBankSlipDetailDOList);
        }
        bankSlipDO.setLocalizationCount((bankSlipDO.getLocalizationCount() == null ? 0 : bankSlipDO.getLocalizationCount()) + localizationCount);
        bankSlipMapper.update(bankSlipDO);

        // 保存日志list
        if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
        }
    }


}
