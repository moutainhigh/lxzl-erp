package com.lxzl.erp.core.service.statement;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.se.core.service.BaseService;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-06 14:57
 */
public interface StatementService extends BaseService {
    /**
     * 生成结算单
     *
     * @param startDate 结算开始时间
     * @param endDate   结算结束时间
     * @return 结算单号
     */
    ServiceResult<String, String> createStatement(Date startDate, Date endDate);
}
