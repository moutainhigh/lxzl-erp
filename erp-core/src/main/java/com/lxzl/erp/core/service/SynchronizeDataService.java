package com.lxzl.erp.core.service;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/6/9
 */
public interface SynchronizeDataService {

    /**
     * 获取订单号，然后调用批量重算接口
     */
    void synchronizeOrderList2BatchReCreateOrderStatement(long millis);
}
