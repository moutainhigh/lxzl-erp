package com.lxzl.erp.core.service.dynamicSql;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSqlHolder;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public interface DynamicSqlService {


    ServiceResult<String, List<List<Object>>> executeBySql(DynamicSqlParam dynamicSqlParam);

    ServiceResult<String, List<List<Object>>> selectBySql(String sql);

    ServiceResult<String, String> updateBySql(String sql);

    ServiceResult<String, String> insertBySql(String sql);

    ServiceResult<String, Page<DynamicSqlHolder>> pageDynamicSqlHolder(PageQuery pageQuery);

    ServiceResult<String, DynamicSqlHolderDO> adoptDynamicSqlHolder(Integer dynamicSqlHolderId);

    ServiceResult<String, DynamicSqlHolderDO> rejectDynamicSqlHolder(Integer dynamicSqlHolderId, String rejectResult);

    ServiceResult<String, String> saveDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, String> deleteDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, DynamicSql> detailDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, Page<DynamicSql>> pageDynamicSql(DynamicSqlQueryParam dynamicSqlQueryParam);
}