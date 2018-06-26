package com.lxzl.erp.core.service.dynamicSql.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.IdCardCheckUtil;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql.DynamicSqlDao;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlMapper;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
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
    private static final String SENSITIVE_INFO_VIEW = "***";

    @Autowired
    private DynamicSqlDao dynamicSqlDao;

    @Autowired
    private DynamicSqlMapper dynamicSqlMapper;

    @Autowired
    private UserSupport userSupport;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public ServiceResult<String, List<List<Object>>> selectBySql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        List<List<Object>> listList;

        if (dynamicSqlSelectParam.getLimit() == null || dynamicSqlSelectParam.getLimit() <= 0) {
            dynamicSqlSelectParam.setLimit(totalReturnCount);
        }

        try {
            listList = dynamicSqlDao.selectBySql(dynamicSqlSelectParam.getSql(), dynamicSqlSelectParam.getLimit());
        } catch (SQLException e) {
            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }

        if (CollectionUtil.isNotEmpty(listList)) {
            filterSensitiveInfo(listList); // 过滤敏感信息
            formatResult(listList);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(listList);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> saveDynamicSql(DynamicSql dynamicSql) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        DynamicSqlDO dynamicSqlDO = ConverterUtil.convert(dynamicSql,DynamicSqlDO.class);
        dynamicSqlDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        dynamicSqlDO.setCreateTime(now);
        dynamicSqlDO.setCreateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlDO.setUpdateTime(now);
        dynamicSqlDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlMapper.save(dynamicSqlDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dynamicSqlDO.getId().toString());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> deleteDynamicSql(DynamicSql dynamicSql) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        DynamicSqlDO dynamicSqlDO = dynamicSqlMapper.findById(dynamicSql.getDynamicSqlId());
        if (dynamicSqlDO == null){
            result.setErrorCode(ErrorCode.DYNAMIC_SQL_NOT_EXISTS);
            return result;
        }

        dynamicSqlDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        dynamicSqlDO.setUpdateTime(now);
        dynamicSqlDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlMapper.update(dynamicSqlDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dynamicSqlDO.getId().toString());
        return result;
    }

    @Override
    public ServiceResult<String, DynamicSql> detailDynamicSql(DynamicSql dynamicSql) {
        ServiceResult<String, DynamicSql> result = new ServiceResult<>();

        DynamicSqlDO dynamicSqlDO = dynamicSqlMapper.findById(dynamicSql.getDynamicSqlId());
        if (dynamicSqlDO == null){
            result.setErrorCode(ErrorCode.DYNAMIC_SQL_NOT_EXISTS);
            return result;
        }

        DynamicSql dynamicSql1 = ConverterUtil.convert(dynamicSqlDO,DynamicSql.class);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dynamicSql1);
        return result;
    }

    @Override
    public ServiceResult<String, Page<DynamicSql>> pageDynamicSql(DynamicSqlQueryParam dynamicSqlQueryParam) {
        ServiceResult<String, Page<DynamicSql>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(dynamicSqlQueryParam.getPageNo(),dynamicSqlQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start",pageQuery.getStart());
        maps.put("pageSize",pageQuery.getPageSize());
        maps.put("dynamicSqlQueryParam",dynamicSqlQueryParam);

        Integer totalCount = dynamicSqlMapper.findDynamicSqlCountByParams(maps);
        List<DynamicSqlDO> dynamicSqlDOList = dynamicSqlMapper.findDynamicSqlByParams(maps);

        List<DynamicSql> dynamicSqlList = ConverterUtil.convertList(dynamicSqlDOList,DynamicSql.class);
        Page<DynamicSql> page = new Page<>(dynamicSqlList,totalCount,dynamicSqlQueryParam.getPageNo(),dynamicSqlQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private void filterSensitiveInfo(List<List<Object>> listList) {
        // 循环处理mapList中的敏感信息
        for (List<Object> objList : listList) {
            Map<Integer, Object> mapTmp = new HashMap<>(); // 保存需要处理的敏感字段
            for (int i = 0; i < objList.size(); i ++) {
                Object object = objList.get(i);
                if (object != null) {
                    String value = object.toString();
                    if (StringUtils.isEmpty(IdCardCheckUtil.IDCardValidate(value.toString()))) {
                        String top4 = value.substring(0,4);
                        String last4 = value.substring(value.length() - 4, value.length());
                        String result = top4 + SENSITIVE_INFO_VIEW + last4;
                        mapTmp.put(i, result);
                    }
                    if (IdCardCheckUtil.checkMobile(value.toString())) {
                        String top3 = value.substring(0,3);
                        String last3 = value.substring(value.length() - 3, value.length());
                        String result = top3 + SENSITIVE_INFO_VIEW + last3;
                        mapTmp.put(i, result);
                    }
                }
            }

            for (Map.Entry<Integer, Object> entryTmp : mapTmp.entrySet()) {
                objList.set(entryTmp.getKey(), entryTmp.getValue());
            }
        }
    }

    /**
     * 格式化结果信息
     * 1. 时间转为字符串
     * 2. double float统一转为两位小数
     * @param listList
     */
    private void formatResult(List<List<Object>> listList) {
        for (List<Object> objList : listList) {
            for (int i = 0; i < objList.size(); i++) {
                Object obj = objList.get(i);
                if (obj instanceof Date) { // 格式化时间
                    String dateStr = DateUtil.formatDate((Date) obj, "yyyy-MM-dd HH:mm:ss");
                    objList.set(i, dateStr);
                }
                if (obj instanceof Double || obj instanceof Float || obj instanceof BigDecimal) { // 格式化数字（保留两位小数）
                    DecimalFormat df = new DecimalFormat("######0.00");
                    String numberStr = df.format(obj);
                    objList.set(i, numberStr);
                }
            }
        }
    }

}