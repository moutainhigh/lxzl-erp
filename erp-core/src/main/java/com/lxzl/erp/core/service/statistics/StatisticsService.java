package com.lxzl.erp.core.service.statistics;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIndexInfo;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-24 16:25
 */
public interface StatisticsService extends BaseService {

    ServiceResult<String,StatisticsIndexInfo> queryIndexInfo();
}
