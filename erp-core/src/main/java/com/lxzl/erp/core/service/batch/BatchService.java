package com.lxzl.erp.core.service.batch;

import com.lxzl.erp.common.domain.ServiceResult;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/7/12
 */
public interface BatchService {


    String batchCreateK3ReturnOrderStatement(List<String> returnOrderNoList);

    /**
     * 批量退还应退的押金
     * @param orderNos
     * @return
     */
    ServiceResult<String, String> batchReturnDeposit(List<String> orderNos);

}
