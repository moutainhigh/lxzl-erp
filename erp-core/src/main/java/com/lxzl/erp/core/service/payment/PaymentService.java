package com.lxzl.erp.core.service.payment;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccountLogSummary;
import com.lxzl.erp.common.domain.payment.account.pojo.PayResult;
import com.lxzl.se.core.service.BaseService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 18:01
 */
public interface PaymentService extends BaseService {
    /**
     * 查询客户账户
     *
     * @param customerNo 客户编码
     * @return 客户帐户信息
     */
    CustomerAccount queryCustomerAccount(String customerNo);

    /**
     * 不登录状态查询客户账户
     *
     * @param customerNo 客户编码
     * @return 客户帐户信息
     */
    CustomerAccount queryCustomerAccountNoLogin(String customerNo);

    /**
     * 手动充值
     *
     * @param param 手动充值参数
     * @return 充值結果
     */
    ServiceResult<String, Boolean> manualCharge(ManualChargeParam param);


    /**
     * 手动扣款
     *
     * @param param 手动扣款参数
     * @return 扣款结果
     */
    ServiceResult<String, Boolean> manualDeduct(ManualDeductParam param);

    /**
     * 余额支付
     *
     * @param customerNo           余额
     * @param businessOrderNo      业务系统订单号
     * @param businessOrderRemark  业务系统备注
     * @param payRentAmount        支付租金金额
     * @param payRentDepositAmount 租金押金
     * @param payDepositAmount     设备押金
     * @return 支付结果
     */
    ServiceResult<String, Boolean> balancePay(String customerNo, String businessOrderNo, String businessOrderRemark, BigDecimal payRentAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount, BigDecimal payOtherAmount);

    /**
     * 微信支付
     * @param customerNo
     * @param businessOrderNo
     * @param businessOrderRemark
     * @param payAmount
     * @param openId
     * @param ip
     * @param loginUserId
     * @return
     */
    ServiceResult<String, String> wechatPay(String customerNo, String businessOrderNo, String businessOrderRemark, BigDecimal payAmount, String openId, String ip, Integer loginUserId);

    /**
     * 微信充值
     *
     * @param ip
     * @return
     */
    ServiceResult<String, String> wechatCharge(String customerNo,BigDecimal amount,String openId, String ip);

    /**
     * 给微信端充值客户记录分页
     * @param customerNo
     * @return
     */
    ServiceResult<String, Page<ChargeRecord>> queryChargeRecordPage(String customerNo);

    /**
     * 给ERP查看充值分页查询
     * @param chargeRecordParam
     * @return
     */
    ServiceResult<String, Page<ChargeRecord>> queryChargeRecordParamPage(ChargeRecordParam chargeRecordParam);

    /**
     * 退还用户押金
     *
     * @param customerNo              客户编号
     * @param returnRentDepositAmount 退还租金押金
     * @param returnDepositAmount     退还设备押金
     * @return 退还结果
     */
    ServiceResult<String, Boolean> returnDeposit(String customerNo, BigDecimal returnRentDepositAmount, BigDecimal returnDepositAmount);


    /**
     * 根据参数查询订单结果
     * @param orderNo 订单编码
     * @param payType 支付类型
     * @param customerNo 客户编码
     * @return
     */
    ServiceResult<String, PayResult> queryPayResult(String orderNo, Integer payType, String customerNo);

    /**
     * 查询客户账户流水
     * @param customerAccountLogParam
     * @return
     */
    ServiceResult<String, Map<String,Object>> queryCustomerAccountLogPage(CustomerAccountLogParam customerAccountLogParam);

    /**
     * 微信端客户帐户流水接口
     * @param param
     * @return
     */
    ServiceResult<String, Map<String,Object>> weixinQueryCustomerAccountLogPage(InterfaceCustomerAccountLogParam param);
}
