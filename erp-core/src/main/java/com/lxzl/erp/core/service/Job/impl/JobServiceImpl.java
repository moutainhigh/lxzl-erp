package com.lxzl.erp.core.service.Job.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.Job.JobService;
import com.lxzl.erp.core.service.Job.impl.support.JobSupport;
import com.lxzl.erp.core.service.bank.BankSlipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/22
 * @Time : Created in 14:00
 */
@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private BankSlipService bankSlipService;

    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> automaticUnknownBankSlipDetail(HttpServletRequest request) {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ServiceResult<String, String> verifyServiceResult = JobSupport.verifyAppIdAndAppSecret(request);
        if(!ErrorCode.SUCCESS.equals(verifyServiceResult.getErrorCode())){
            serviceResult.setErrorCode(verifyServiceResult.getErrorCode());
            return serviceResult;
        }
        bankSlipService.automaticUnknownBankSlipDetail();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

}
