package com.lxzl.erp.core.service.exclt;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;

import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/19
 * @Time : Created in 15:50
 */
public interface ImportBankSlipService {

    /**
    * 保存对公流水
    * @Author : XiaoLuYu
    * @Date : Created in 2018/3/19 16:12
    * @param : multipartFile
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String,String> saveBankSlip(BankSlip bankSlip) throws Exception;
}
