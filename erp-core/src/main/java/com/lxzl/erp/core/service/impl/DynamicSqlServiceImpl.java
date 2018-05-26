package com.lxzl.erp.core.service.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSql;
import com.lxzl.erp.core.service.DynamicSqlService;
import com.lxzl.erp.dataaccess.dao.mysql.DynamicSqlMapper;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
@Service("dynamicSqlService")
public class DynamicSqlServiceImpl implements DynamicSqlService {

    @Autowired
    private DynamicSqlMapper dynamicSqlMapper;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public ServiceResult<String, Page<Map>> selectBySql(DynamicSql dynamicSql) {
        ServiceResult<String, Page<Map>> serviceResult = new ServiceResult<>();
        List<Map> mapList = dynamicSqlMapper.selectBySql(formatSql(dynamicSql));

        Page<Map> page = new Page<>(mapList, mapList.size(), dynamicSql.getPageNo(), dynamicSql.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    // 1. 如果sql本身有limit，则不考虑pageSize, pageNo
    // 2. 如果sql本身没有limit, 且pageSize和pageNo不为空时
    private String formatSql(DynamicSql dynamicSql) {
        String sqlUpper = dynamicSql.getSql().toUpperCase();
        if (sqlUpper.contains("LIMIT")) {
            return dynamicSql.getSql();
        } else {
            StringBuilder sb = new StringBuilder();
            String sql = dynamicSql.getSql();
            if (sql.endsWith(";")) {
                sb.append(sql.substring(0,sql.length() - 1));
            } else {
                sb.append(sql);
            }

            PageQuery pageQuery = new PageQuery(dynamicSql.getPageNo(), dynamicSql.getPageSize());
            sb.append(" limit ");
            sb.append(pageQuery.getStart());
            sb.append(",");
            sb.append(pageQuery.getPageSize());
            return sb.toString();
        }
    }
}
