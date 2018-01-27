package com.lxzl.erp.core.service.payment;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.payment.WeixinPayParam;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.ManualDeductParam;
import com.lxzl.se.core.service.BaseService;

import java.math.BigDecimal;

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
    ServiceResult<String, String> wechatCharge(String customerNo, String businessOrderRemark, BigDecimal payAmount,String openId, String ip);

    /**
     * 退还用户押金
     *
     * @param customerNo              客户编号
     * @param returnRentDepositAmount 退还租金押金
     * @param returnDepositAmount     退还设备押金
     * @return 退还结果
     */
    ServiceResult<String, Boolean> returnDeposit(String customerNo, BigDecimal returnRentDepositAmount, BigDecimal returnDepositAmount);



}
