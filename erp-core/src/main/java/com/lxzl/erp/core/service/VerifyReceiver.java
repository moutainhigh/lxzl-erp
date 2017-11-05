package com.lxzl.erp.core.service;

/**
 * 接受审核结果接口
 * 有审核逻辑的业务需要实现该接口
 */
public interface VerifyReceiver {
    /**
     * @param verifyResult
     * @param businessId
     * @return 业务处理结果
     */
    boolean receiveVerifyResult(boolean verifyResult,Integer businessId);
}
