package com.lxzl.erp.core.service.replace.impl;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.core.service.replace.ReplaceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\5 0005 10:08
 */
@Service
public class ReplaceOrderServiceImpl implements ReplaceOrderService{
    private static final Logger logger = LoggerFactory.getLogger(ReplaceOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(ReplaceOrder replaceOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();



        return null;
    }

}
