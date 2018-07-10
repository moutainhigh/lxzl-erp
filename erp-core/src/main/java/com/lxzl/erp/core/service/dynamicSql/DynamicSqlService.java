package com.lxzl.erp.core.service.dynamicSql;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public interface DynamicSqlService {


    ServiceResult<String, List<Map>> executeBySql(DynamicSqlSelectParam dynamicSqlSelectParam);

    ServiceResult<String, List<Map>> selectBySql(DynamicSqlSelectParam dynamicSqlSelectParam);

    ServiceResult<String, List<Map>> updateBySql(DynamicSqlSelectParam dynamicSqlSelectParam);

    ServiceResult<String, List<Map>> insertBySql(DynamicSqlSelectParam dynamicSqlSelectParam);

    ServiceResult<String, String> saveDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, String> deleteDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, DynamicSql> detailDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, Page<DynamicSql>> pageDynamicSql(DynamicSqlQueryParam dynamicSqlQueryParam);
}