package com.lxzl.erp.core.service.Job;

import com.lxzl.erp.common.domain.ServiceResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/22
 * @Time : Created in 14:00
 */
public interface JobService {
    ServiceResult<String,String> automaticUnknownBankSlipDetail(HttpServletRequest request);
}
