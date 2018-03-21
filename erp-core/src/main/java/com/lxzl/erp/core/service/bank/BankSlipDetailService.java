package com.lxzl.erp.core.service.bank;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:59 2018/3/20
 * @Modified By:
 */
public interface BankSlipDetailService {

    ServiceResult<String,Page<BankSlipDetail>> pageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam);
}
