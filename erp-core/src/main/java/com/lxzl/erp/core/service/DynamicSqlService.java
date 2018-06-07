package com.lxzl.erp.core.service;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSql;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public interface DynamicSqlService {

    ServiceResult<String, List<List<Object>>> selectBySql(DynamicSql dynamicSql);
}
