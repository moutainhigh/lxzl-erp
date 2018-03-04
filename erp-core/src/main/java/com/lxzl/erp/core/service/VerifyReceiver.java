package com.lxzl.erp.core.service;

import com.lxzl.se.core.service.BaseService;

/**
 * 接受审核结果接口
 * 有审核逻辑的业务需要实现该接口
 */
public interface VerifyReceiver extends BaseService {
    /**
     * @param verifyResult
     * @param businessNo
     * @return 业务处理结果，ErrorCode
     */
    String receiveVerifyResult(boolean verifyResult,String businessNo);
}
