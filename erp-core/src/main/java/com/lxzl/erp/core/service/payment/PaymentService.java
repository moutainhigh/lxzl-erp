package com.lxzl.erp.core.service.payment;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.account.pojo.ManualDeductParam;
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
     *
     * @param customerNo 客户编码
     * @param payAmount 余额
     * @return 充值结果
     */

    /**
     * 余额支付
     *
     * @param customerNo           余额
     * @param businessOrderNo      业务系统订单号
     * @param businessOrderRemark  业务系统备注
     * @param businessNotifyUrl    回调URL
     * @param payAmount            支付金额
     * @param payRentDepositAmount 租金押金
     * @param payDepositAmount     设备押金
     * @return 支付结果
     */
    ServiceResult<String, Boolean> balancePay(String customerNo, String businessOrderNo, String businessOrderRemark, String businessNotifyUrl, BigDecimal payAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount);
}
