package com.lxzl.erp.core.service.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSql;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.IdCardCheckUtil;
import com.lxzl.erp.core.service.DynamicSqlService;
import com.lxzl.erp.dataaccess.dao.mysql.DynamicSqlMapper;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    private static final Integer totalReturnCount = 100;
    private static final Integer PHONE_TYPE = 0;
    private static final Integer IDCARD_TYPE = 1;
    private static final String SENSITIVE_INFO_VIEW = "***";

    @Autowired
    private DynamicSqlMapper dynamicSqlMapper;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public ServiceResult<String, Page<Map<String, Object>>> selectBySql(DynamicSql dynamicSql) {
        ServiceResult<String, Page<Map<String, Object>>> serviceResult = new ServiceResult<>();
        List<Map<String, Object>> mapList = dynamicSqlMapper.selectBySql(formatSql(dynamicSql));
        filterSensitiveInfo(mapList);
        Page<Map<String, Object>> page = new Page<>(mapList, mapList.size(), dynamicSql.getPageNo(), dynamicSql.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    // 1. 如果sql本身有limit，则不考虑pageSize, pageNo
    // 2. 如果sql本身没有limit, 且pageSize和pageNo不为空时
    private String formatSql(DynamicSql dynamicSql) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ("); // 外层查询限制总条数

        // 添加查询主sql, 并去掉末尾分号
        String sql = dynamicSql.getSql();
        if (sql.endsWith(";")) {
            sb.append(sql.substring(0,sql.length() - 1));
        } else {
            sb.append(sql);
        }

        // 添加limit限制
        String sqlUpper = dynamicSql.getSql().toUpperCase();
        if (!sqlUpper.contains("LIMIT")) {
            PageQuery pageQuery = new PageQuery(dynamicSql.getPageNo(), dynamicSql.getPageSize());
            sb.append(" limit ");
            sb.append(pageQuery.getStart());
            sb.append(",");
            sb.append(pageQuery.getPageSize());
        }

        sb.append(") tmp limit ");
        sb.append(totalReturnCount.toString());

        return sb.toString();
    }

    private void filterSensitiveInfo(List<Map<String, Object>> mapList) {
        if (CollectionUtil.isNotEmpty(mapList)) {
            // 循环处理mapList中的敏感信息
            for (Map<String, Object> map : mapList) {
                Map<String, Object> mapTmp = new HashMap<>(); // 保存需要处理的敏感字段
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object valueObj = entry.getValue();
                    if (valueObj != null) {
                        String value = valueObj.toString();
                        if (StringUtils.isEmpty(IdCardCheckUtil.IDCardValidate(value.toString()))) {
                            String top4 = value.substring(0,4);
                            String last4 = value.substring(value.length() - 4, value.length());
                            String result = top4 + SENSITIVE_INFO_VIEW + last4;
                            mapTmp.put(entry.getKey(), result);
                        }
                        if (IdCardCheckUtil.checkMobile(value.toString())) {
                            String top3 = value.substring(0,3);
                            String last3 = value.substring(value.length() - 3, value.length());
                            String result = top3 + SENSITIVE_INFO_VIEW + last3;
                            map.put(entry.getKey(), result);
                        }
                    }
                }

                for (Map.Entry<String, Object> entryTmp : mapTmp.entrySet()) {
                    map.put(entryTmp.getKey(), entryTmp.getValue());
                }
            }
        }
    }
}
