package com.lxzl.erp.core.service.payment;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
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
     * @param customerNo   客户编码
     * @param chargeAmount 充值金额
     * @return 充值結果
     */
    ServiceResult<String, Boolean> manualCharge(String customerNo, BigDecimal chargeAmount);
}
