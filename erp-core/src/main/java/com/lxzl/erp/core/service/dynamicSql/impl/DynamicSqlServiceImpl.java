package com.lxzl.erp.core.service.dynamicSql.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.IdCardCheckUtil;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql.impl.JdbcDynamicSqlDao;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlDao;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlHolderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlMapper;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlDO;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

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

    @Autowired
    private DynamicSqlHolderMapper dynamicSqlHolderMapper;

    @Autowired
    private JdbcDynamicSqlDao jdbcDynamicSqlDao;


    @Override
    public ServiceResult<String, List<List<Object>>> executeBySql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        if (dynamicSqlSelectParam.getLimit() == null || dynamicSqlSelectParam.getLimit() <= 0) {
            dynamicSqlSelectParam.setLimit(totalReturnCount);
        }

        SqlTpye sqlTpye = analysisAndRebuildDynamicSql(dynamicSqlSelectParam);
        String sql = dynamicSqlSelectParam.getSql();

        switch (sqlTpye) {
            case DELETE:
                throw new BusinessException(ErrorCode.DELETE_PROTECTION);
            case INSERT:
                serviceResult.setResult(new ArrayList<List<Object>>() {{
                    add(new ArrayList<>());
                }});
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                if (dynamicSqlHolderMapper.save(initDynamicSqlHolderDO(sql, sqlTpye)) >= 1)
                    serviceResult.getResult().get(0).add("INSERT 操作需要被审核才可执行");
                else
                    serviceResult.getResult().get(0).add("申请 INSERT 操作失败");
                break;
            case UPDATE:
                serviceResult.setResult(new ArrayList<List<Object>>() {{
                    add(new ArrayList<>());
                }});
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                if (dynamicSqlHolderMapper.save(initDynamicSqlHolderDO(sql, sqlTpye)) >= 1)
                    serviceResult.getResult().get(0).add("UPDATE 操作需要被审核才可执行");
                else
                    serviceResult.getResult().get(0).add("申请 UPDATE 操作失败");
                break;
            case SELECT:
                dynamicSqlSelectParam.setSql(sql);
                serviceResult = selectBySql(dynamicSqlSelectParam);
                break;
            case DEFAULT:
                throw new BusinessException(ErrorCode.DYNAMIC_SQL_ILLEGAL_OPERATION);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<List<Object>>> selectBySql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        List<List<Object>> results = new ArrayList<>();
        try {
            results = jdbcDynamicSqlDao.selectBySql(dynamicSqlSelectParam.getSql());
        } catch (SQLException | BadSqlGrammarException e) {
//            List<Object> list = new ArrayList<Object>() {{
//                add("动态SQL查询语句不正确");
//            }};
//            results.add(list);
            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }

        if (results.size() == 0) {
            List<Object> list = new ArrayList<Object>() {{
                add("Data is not found");
            }};
            results.add(list);
        }

        serviceResult.setResult(results);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> updateBySql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        final int mark;
        try {
            mark = dynamicSqlDao.updateBySql(dynamicSqlSelectParam.getSql());
            serviceResult.setResult("Affected rows:" + mark);
        } catch (SQLException | BadSqlGrammarException e) {
            serviceResult.setResult(e.getMessage());
//            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> insertBySql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        final int mark;
        try {
            mark = dynamicSqlDao.insertBySql(dynamicSqlSelectParam.getSql());
            serviceResult.setResult("Affected rows:" + mark);
        } catch (SQLException | BadSqlGrammarException e) {
            serviceResult.setResult(e.getMessage());
//            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<DynamicSqlHolderDO>> pageDynamicSqlHolder(PageQuery pageQuery) {
        final PageQuery finalPageQuery = new PageQuery(pageQuery.getPageNo(), pageQuery.getPageSize());
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("start", finalPageQuery.getPageNo());
            put("pageSize", finalPageQuery.getPageSize() == 0 ? 10 : finalPageQuery.getPageSize());
        }};

        if (!userSupport.isSuperUser()) {
            map.put("createUser", userSupport.getCurrentUserId().toString());
        }

        ServiceResult<String, List<DynamicSqlHolderDO>> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(dynamicSqlHolderMapper.listPage(map));
        return serviceResult;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, DynamicSqlHolderDO> adoptDynamicSqlHolder(Integer dynamicSqlHolderId) {
        ServiceResult<String, DynamicSqlHolderDO> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return serviceResult;
        }
        if (dynamicSqlHolderId == null)
            throw new BusinessException(ErrorCode.DYNAMICSQLHOLDERID_NOT_NULL);
        DynamicSqlHolderDO dynamicSqlHolderDO = dynamicSqlHolderMapper.findById(dynamicSqlHolderId);
        SqlTpye sqlTpye = SqlTpye.valueOf(dynamicSqlHolderDO.getSqlTpye());
        ServiceResult<String, String> sqlResult = new ServiceResult<>();
        final String sql = dynamicSqlHolderDO.getSqlContent();
        switch (sqlTpye) {
            case UPDATE:
                sqlResult = updateBySql(new DynamicSqlSelectParam() {{
                    setSql(sql);
                }});
                break;
            case INSERT:
                sqlResult = insertBySql(new DynamicSqlSelectParam() {{
                    setSql(sql);
                }});
                break;
        }

        serviceResult.setErrorCode(sqlResult.getErrorCode());
        if (ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            DynamicSqlHolderDO updateDynamicSqlHolderDO = new DynamicSqlHolderDO();
            updateDynamicSqlHolderDO.setId(dynamicSqlHolderDO.getId());
            updateDynamicSqlHolderDO.setStatus(DynamicSqlHolderDO.Status.CHECKED.value);
            updateDynamicSqlHolderDO.setResults(sqlResult.getResult());
            updateDynamicSqlHolderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            dynamicSqlHolderMapper.update(updateDynamicSqlHolderDO);
            serviceResult.setResult(updateDynamicSqlHolderDO);
        }

        return serviceResult;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> saveDynamicSql(DynamicSql dynamicSql) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        DynamicSqlDO dynamicSqlDO = ConverterUtil.convert(dynamicSql, DynamicSqlDO.class);
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
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> deleteDynamicSql(DynamicSql dynamicSql) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        DynamicSqlDO dynamicSqlDO = dynamicSqlMapper.findById(dynamicSql.getDynamicSqlId());
        if (dynamicSqlDO == null) {
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
        if (dynamicSqlDO == null) {
            result.setErrorCode(ErrorCode.DYNAMIC_SQL_NOT_EXISTS);
            return result;
        }

        DynamicSql dynamicSql1 = ConverterUtil.convert(dynamicSqlDO, DynamicSql.class);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dynamicSql1);
        return result;
    }

    @Override
    public ServiceResult<String, Page<DynamicSql>> pageDynamicSql(DynamicSqlQueryParam dynamicSqlQueryParam) {
        ServiceResult<String, Page<DynamicSql>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(dynamicSqlQueryParam.getPageNo(), dynamicSqlQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("dynamicSqlQueryParam", dynamicSqlQueryParam);

        Integer totalCount = dynamicSqlMapper.findDynamicSqlCountByParams(maps);
        List<DynamicSqlDO> dynamicSqlDOList = dynamicSqlMapper.findDynamicSqlByParams(maps);

        List<DynamicSql> dynamicSqlList = ConverterUtil.convertList(dynamicSqlDOList, DynamicSql.class);
        Page<DynamicSql> page = new Page<>(dynamicSqlList, totalCount, dynamicSqlQueryParam.getPageNo(), dynamicSqlQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private void filterSensitiveInfo(List<List<Object>> listList) {
        // 循环处理mapList中的敏感信息
        for (List<Object> objList : listList) {
            Map<Integer, Object> mapTmp = new HashMap<>(); // 保存需要处理的敏感字段
            for (int i = 0; i < objList.size(); i++) {
                Object object = objList.get(i);
                if (object != null) {
                    String value = object.toString();
                    if (StringUtils.isEmpty(IdCardCheckUtil.IDCardValidate(value.toString()))) {
                        String top4 = value.substring(0, 4);
                        String last4 = value.substring(value.length() - 4, value.length());
                        String result = top4 + SENSITIVE_INFO_VIEW + last4;
                        mapTmp.put(i, result);
                    }
                    if (IdCardCheckUtil.checkMobile(value.toString())) {
                        String top3 = value.substring(0, 3);
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
     *
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

    private SqlTpye analysisAndRebuildDynamicSql(DynamicSqlSelectParam dynamicSqlSelectParam) {
        StringBuilder word = new StringBuilder();

        String sql = dynamicSqlSelectParam.getSql().trim();
        String upperCaseSql = sql.toUpperCase();
        SqlTpye sqlTpye = SqlTpye.DEFAULT;

        boolean initialMark = true;
        boolean checkKeyWork = false;
        boolean isString = false;
        boolean isString2 = false;
        boolean hasSelect = false;
        boolean hasUpdate = false;
        boolean hasDelete = false;
        boolean hasInsertInto = false;
        boolean hasLimit = false;

        for (int i = 0; i < upperCaseSql.length(); i++) {
            char ch = upperCaseSql.charAt(i);
            switch (ch) {
                case ')':
                    if (!isString && !isString2)
                        if (hasLimit)
                            hasLimit = false;
                    break;

                case ' ':
                    if (!isString && !isString2)
                        checkKeyWork = true;

                    if (initialMark) {
                        initialMark = false;
                        for (SqlTpye sqlTpye1 : SqlTpye.values()) {
                            if (sqlTpye1.sqlTpyeName.equals(word.toString()))
                                sqlTpye = sqlTpye1;
                        }
                    }

                    break;

                case '\'':
                    isString = !isString;
                    break;

                case '\"':
                    isString2 = !isString2;
                    break;
                default:
                    word.append(ch);
                    break;
            }


            if (checkKeyWork) {
                switch ((word.toString())) {
                    case "SELECT":
                        hasSelect = true;
                        //do something
                        break;

                    case "UPDATE":
                        hasUpdate = true;
                        //do something
                        break;

                    case "DELETE":
                        hasDelete = true;
                        //do something
                        break;

                    case "INSERT":
                        int m = i + 1;
                        for (; m < upperCaseSql.length(); m++)
                            if (upperCaseSql.charAt(m) != ' ')
                                break;

                        for (; m < upperCaseSql.length(); m++) {
                            char c = upperCaseSql.charAt(m);
                            if (c != ' ')
                                word.append(c);
                            else
                                break;
                        }
                        if (word.toString().equals("INSERTINTO")) {
                            hasInsertInto = true;
                            i = m;
                            //do something
                        }
                        break;

                    case "FROM":
                        //do something
                        break;

                    case "WHERE":
                        //do something
                        break;

                    case "LIMIT":
                        hasLimit = true;
                        //do something
                        break;

                    case "OFFSET":
                        //do something
                        break;
                    default:
                        break;
                }
                checkKeyWork = false;
                word = new StringBuilder();
            }
        }

        if (sqlTpye != SqlTpye.DEFAULT) {
            if (hasDelete) {
                sqlTpye = SqlTpye.DELETE;
            } else if (hasInsertInto) {
                sqlTpye = SqlTpye.INSERT;
            } else if (hasUpdate) {
                sqlTpye = SqlTpye.UPDATE;
            } else if (hasSelect) {
                if (!hasLimit)
                    dynamicSqlSelectParam.setSql(sql + " LIMIT " + dynamicSqlSelectParam.getLimit());
                sqlTpye = SqlTpye.SELECT;
            }
        }

        return sqlTpye;

    }

    private DynamicSqlHolderDO initDynamicSqlHolderDO(String sql, SqlTpye sqlTpye) {
        DynamicSqlHolderDO dynamicSqlHolderDO = new DynamicSqlHolderDO();
        dynamicSqlHolderDO.setSqlContent(sql);
        dynamicSqlHolderDO.setSqlTpye(sqlTpye.getSqlTpyeName());
        dynamicSqlHolderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlHolderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlHolderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        return dynamicSqlHolderDO;
    }

    private enum SqlTpye {
        SELECT("SELECT"), UPDATE("UPDATE"), DELETE("DELETE"), INSERT("INSERT"), DEFAULT("DEFAULT");
        String sqlTpyeName;

        SqlTpye(String sqlTpyeName) {
            this.sqlTpyeName = sqlTpyeName;
        }

        public String getSqlTpyeName() {
            return sqlTpyeName;
        }

        public void setSqlTpyeName(String sqlTpyeName) {
            this.sqlTpyeName = sqlTpyeName;
        }
    }

}