package com.lxzl.erp.core.service.bank;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
public interface BankSlipService {

    ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam);


}
