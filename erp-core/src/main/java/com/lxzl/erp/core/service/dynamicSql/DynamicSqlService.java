package com.lxzl.erp.core.service.dynamicSql;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public interface DynamicSqlService {


    ServiceResult<String,  List<List<Object>>> executeBySql(DynamicSqlParam dynamicSqlParam);

    ServiceResult<String,  List<List<Object>>> selectBySql(DynamicSqlParam dynamicSqlParam);

    ServiceResult<String, List<Map>> updateBySql(DynamicSqlParam dynamicSqlParam);

    ServiceResult<String, List<Map>> insertBySql(DynamicSqlParam dynamicSqlParam);

    ServiceResult<String, List<DynamicSqlHolderDO>> pageDynamicSqlHolder(BasePageParam basePageParam);

    ServiceResult<String,DynamicSqlHolderDO> adoptDynamicSqlHolder(Integer dynamicSqlHolderId);

    ServiceResult<String, String> saveDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, String> deleteDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, DynamicSql> detailDynamicSql(DynamicSql dynamicSql);

    ServiceResult<String, Page<DynamicSql>> pageDynamicSql(DynamicSqlQueryParam dynamicSqlQueryParam);
}