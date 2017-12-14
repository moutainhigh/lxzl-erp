package com.lxzl.erp.core.service.payment;

import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 18:01
 */
public interface PaymentService extends BaseService {
    CustomerAccount queryCustomerAccount(String customerNo);
}
