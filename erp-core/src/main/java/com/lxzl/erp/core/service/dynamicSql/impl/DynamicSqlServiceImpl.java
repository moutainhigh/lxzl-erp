package com.lxzl.erp.core.service.dynamicSql.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.DynamicSqlTpye;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DMLResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSqlHolder;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql.impl.JdbcDynamicSqlDao;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlDao;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlHolderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.DynamicSqlMapper;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlDO;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

@Service("dynamicSqlService")
public class DynamicSqlServiceImpl implements DynamicSqlService {

    private static final Integer totalReturnCount = 100;

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
    public ServiceResult<String, List<List<Object>>> executeBySql(DynamicSqlParam dynamicSqlParam, Set<DynamicSqlTpye> allowMethod) {
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        String sql = dynamicSqlParam.getSql().trim();
        List<DynamicSqlItem> dynamicSqlItems = analysisAndRebuildDynamicSql(sql);
        DynamicSqlTpye dynamicSqlTpye = checkDynamicSqlParam(dynamicSqlParam, dynamicSqlItems);
        if(!allowMethod.contains(dynamicSqlTpye))
            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ILLEGAL_OPERATION);

        switch (dynamicSqlTpye) {
            case DELETE:
                throw new BusinessException(ErrorCode.DELETE_PROTECTION);
            case INSERT:
                serviceResult.setResult(new ArrayList<List<Object>>() {{
                    add(new ArrayList<>());
                }});
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                if (dynamicSqlHolderMapper.save(buildDynamicSqlHolderDO(sql, dynamicSqlTpye, dynamicSqlParam.getRemark())) >= 1)
                    serviceResult.getResult().get(0).add("INSERT 操作需要被审核才可执行");
                else
                    serviceResult.getResult().get(0).add("申请 INSERT 操作失败");
                break;
            case UPDATE:
                serviceResult.setResult(new ArrayList<List<Object>>() {{
                    add(new ArrayList<>());
                }});
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                if (dynamicSqlHolderMapper.save(buildDynamicSqlHolderDO(sql, dynamicSqlTpye, dynamicSqlParam.getRemark())) >= 1)
                    serviceResult.getResult().get(0).add("UPDATE 操作需要被审核才可执行");
                else
                    serviceResult.getResult().get(0).add("申请 UPDATE 操作失败");
                break;
            case SELECT:
                DynamicSqlItem dynamicSqlItem = dynamicSqlItems.get(0);
                if (!dynamicSqlItem.hasLimit)
                    dynamicSqlItem.setSql(dynamicSqlItem.getSql() + " LIMIT " + dynamicSqlParam.getLimit());
                dynamicSqlParam.setSql(dynamicSqlItem.getSql());
                serviceResult = selectBySql(dynamicSqlParam.getSql());
                break;
            case DEFAULT:
                throw new BusinessException(ErrorCode.DYNAMIC_SQL_ILLEGAL_OPERATION);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<List<Object>>> selectBySql(String sql) {
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        List<List<Object>> results = new ArrayList<>();
        try {
            results = jdbcDynamicSqlDao.selectBySql(sql);
        } catch (SQLException | NonTransientDataAccessException e) {
            List<Object> list = new ArrayList<Object>() {{
                add(e.getMessage());
            }};
            results.add(list);
//            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
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
    public ServiceResult<String, DMLResult> updateBySql(String sql) {
        ServiceResult<String, DMLResult> serviceResult = new ServiceResult<>();
        DMLResult dmlResult = new DMLResult();
        try {
            int mark = dynamicSqlDao.updateBySql(sql);
            dmlResult.setSuccess(true);
            dmlResult.setAffectedRows(mark);
            dmlResult.setMsg("Affected rows:" + mark);
        } catch (SQLException | NonTransientDataAccessException e) {
            dmlResult.setMsg(e.getMessage());
//            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }
        serviceResult.setResult(dmlResult);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, DMLResult> insertBySql(String sql) {
        ServiceResult<String, DMLResult> serviceResult = new ServiceResult<>();
        DMLResult dmlResult = new DMLResult();
        try {
            int mark = dynamicSqlDao.insertBySql(sql);
            dmlResult.setSuccess(true);
            dmlResult.setAffectedRows(mark);
            dmlResult.setMsg("Affected rows:" + mark);
        } catch (SQLException | NonTransientDataAccessException e) {
            dmlResult.setMsg(e.getMessage());
//            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ERROR);
        }
        serviceResult.setResult(dmlResult);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<DynamicSqlHolder>> pageDynamicSqlHolder(PageQuery param) {
        final PageQuery finalPageQuery = new PageQuery(param.getPageNo() == 0 ? 1 : param.getPageNo(),
                param.getPageSize() == 0 ? 10 : param.getPageSize());
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("start", finalPageQuery.getStart());
            put("pageSize", finalPageQuery.getPageSize() == 0 ? 10 : finalPageQuery.getPageSize());
        }};

        if (!userSupport.isSuperUser()) {
            map.put("createUser", userSupport.getCurrentUserId().toString());
        }

        ServiceResult<String, Page<DynamicSqlHolder>> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        List<DynamicSqlHolder> dynamicSqlHolders = ConverterUtil.convertList(dynamicSqlHolderMapper.listPage(map), DynamicSqlHolder.class);
        int totalCount = dynamicSqlHolderMapper.listCount(map);

        Page<DynamicSqlHolder> page = new Page<>(dynamicSqlHolders, totalCount, finalPageQuery.getPageNo(), finalPageQuery.getPageSize());
        serviceResult.setResult(page);
        return serviceResult;
    }

    private static final int changeLimit = 50;

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
        DynamicSqlTpye dynamicSqlTpye = DynamicSqlTpye.valueOf(dynamicSqlHolderDO.getSqlTpye());
        final String sql = dynamicSqlHolderDO.getSqlContent();
        List<DynamicSqlItem> dynamicSqlItems = analysisAndRebuildDynamicSql(sql);
        StringBuilder sqlResultBuilder = new StringBuilder();
        int affectedRows = 0;
        switch (dynamicSqlTpye) {
            case UPDATE:
                for (DynamicSqlItem dynamicSqlItem : dynamicSqlItems) {
                    DMLResult dmlResult = updateBySql(dynamicSqlItem.getSql()).getResult();
                    affectedRows += dmlResult.getAffectedRows();
                    sqlResultBuilder.append(dmlResult.getMsg()).append(";").append("\n");
                }
                break;
            case INSERT:
                for (DynamicSqlItem dynamicSqlItem : dynamicSqlItems) {
                    DMLResult dmlResult = insertBySql(dynamicSqlItem.getSql()).getResult();
                    affectedRows += dmlResult.getAffectedRows();
                    sqlResultBuilder.append(dmlResult.getMsg()).append(";").append("\n");
                }
                break;
        }
        if (affectedRows > changeLimit)
            throw new BusinessException("改变条数不能超过:" + changeLimit + "(affectedRows:" + affectedRows + ")");
        DynamicSqlHolderDO updateDynamicSqlHolderDO = new DynamicSqlHolderDO();
        updateDynamicSqlHolderDO.setId(dynamicSqlHolderDO.getId());
        updateDynamicSqlHolderDO.setStatus(DynamicSqlHolderDO.Status.CHECKED.value);
        updateDynamicSqlHolderDO.setResults(sqlResultBuilder.toString());
        updateDynamicSqlHolderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        updateDynamicSqlHolderDO.setUpdateTime(new Date());
        dynamicSqlHolderMapper.update(updateDynamicSqlHolderDO);
        serviceResult.setResult(updateDynamicSqlHolderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    @Override
    public ServiceResult<String, DynamicSqlHolderDO> rejectDynamicSqlHolder(Integer dynamicSqlHolderId, String rejectResult) {
        ServiceResult<String, DynamicSqlHolderDO> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return serviceResult;
        }

        if (dynamicSqlHolderId == null)
            throw new BusinessException(ErrorCode.DYNAMICSQLHOLDERID_NOT_NULL);

        if (rejectResult == null)
            rejectResult = "该动态sql的执行被拒绝";

        DynamicSqlHolderDO updateDynamicSqlHolderDO = new DynamicSqlHolderDO();
        updateDynamicSqlHolderDO.setId(dynamicSqlHolderId);
        updateDynamicSqlHolderDO.setStatus(DynamicSqlHolderDO.Status.REJECT.value);
        updateDynamicSqlHolderDO.setResults(rejectResult);
        updateDynamicSqlHolderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlHolderMapper.update(updateDynamicSqlHolderDO);
        updateDynamicSqlHolderDO.setUpdateTime(new Date());
        serviceResult.setResult(updateDynamicSqlHolderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
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



    private DynamicSqlTpye checkDynamicSqlParam(DynamicSqlParam dynamicSqlParam, List<DynamicSqlItem> dynamicSqlItems) {
        if (dynamicSqlParam.getLimit() == null || dynamicSqlParam.getLimit() <= 0) {
            dynamicSqlParam.setLimit(totalReturnCount);
        }
        if (dynamicSqlParam.getSql() == null)
            throw new BusinessException(ErrorCode.SQL_CONTENT_NOT_NULL);

        if (!isSimilarDynamicSqlTpye(dynamicSqlItems))
            throw new BusinessException(ErrorCode.DYNAMIC_SQL_ILLEGAL_OPERATION);

        return findHighestDynamicSqlTpye(dynamicSqlItems);
    }

    private List<DynamicSqlItem> analysisAndRebuildDynamicSql(String sql) {
        StringBuilder word = new StringBuilder();
        List<DynamicSqlItem> dynamicSqlItems = new LinkedList<>();
        String upperCaseSql = sql.toUpperCase();

        boolean initialMark = true;
        boolean checkKeyWork = false;

        DynamicSqlItem dynamicSqlItem = new DynamicSqlItem();
        StringBuilder sentenceBuilder = new StringBuilder();
        StringParameterStateItem stringParameterStateItem = new StringParameterStateItem();
        for (int i = 0; i < upperCaseSql.length(); i++) {
            boolean endOfSentence = false;
            char ch = upperCaseSql.charAt(i);

            if (ch != ';')
                sentenceBuilder.append(sql.charAt(i));

            switch (ch) {
                case ';':
                    if (stringParameterStateItem.isNotStringParameter()) {
                        checkKeyWork = true;
                        endOfSentence = true;
                    }
                    break;

                case ')':
                    if (stringParameterStateItem.isNotStringParameter())
                        if (dynamicSqlItem.hasLimit)
                            dynamicSqlItem.hasLimit = false;
                    break;

                case ' ':
                case '\t':
                    if (stringParameterStateItem.isNotStringParameter())
                        checkKeyWork = true;

                    if (initialMark) {
                        initialMark = false;
                        for (DynamicSqlTpye dynamicSqlTpye1 : DynamicSqlTpye.values()) {
                            if (dynamicSqlTpye1.getSqlTpyeName().equals(word.toString().trim()))
                                dynamicSqlItem.setDynamicSqlTpye(dynamicSqlTpye1);
                        }
                    }
                    break;

                case '\'':
                    stringParameterStateItem.transform(ch);
                    break;

                case '\"':
                    stringParameterStateItem.transform(ch);
                    break;
                default:
                    word.append(ch);
                    break;

            }

            if (checkKeyWork) {
                switch (word.toString().trim()) {
                    case "SELECT":
                        dynamicSqlItem.hasSelect = true;
                        //do something
                        break;

                    case "UPDATE":
                        dynamicSqlItem.hasUpdate = true;
                        //do something
                        break;

                    case "DELETE":
                        dynamicSqlItem.hasDelete = true;
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
                            dynamicSqlItem.hasInsertInto = true;
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
                        dynamicSqlItem.hasLimit = true;
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

            if (endOfSentence) {
                dynamicSqlItem.setSql(sentenceBuilder.toString());
                dynamicSqlItems.add(rebuildDynamicSqlItem(dynamicSqlItem));
                sentenceBuilder = new StringBuilder();
                dynamicSqlItem = new DynamicSqlItem();
                initialMark = true;
                while ((i + 1 < upperCaseSql.length()) && (upperCaseSql.charAt(i + 1) <= ' ')) {
                    i++;
                }
            }
        }
        if (sentenceBuilder.length() > 0) {
            dynamicSqlItem.setSql(sentenceBuilder.toString());
            dynamicSqlItems.add(rebuildDynamicSqlItem(dynamicSqlItem));
        }
        return dynamicSqlItems;
    }

    private boolean isSimilarDynamicSqlTpye(List<DynamicSqlItem> dynamicSqlItems) {
        DynamicSqlTpye target = DynamicSqlTpye.DEFAULT;
        for (DynamicSqlItem dynamicSqlItem : dynamicSqlItems) {
            if (dynamicSqlItem.getDynamicSqlTpye() == DynamicSqlTpye.DEFAULT)
                return false;

            if (target == DynamicSqlTpye.DEFAULT)
                target = dynamicSqlItem.getDynamicSqlTpye();
            else if (target != dynamicSqlItem.getDynamicSqlTpye())
                return false;

        }
        return true;

    }

    private DynamicSqlTpye findHighestDynamicSqlTpye(List<DynamicSqlItem> dynamicSqlItems) {
        int markingLevel = -1;
        DynamicSqlTpye target = DynamicSqlTpye.DEFAULT;
        for (DynamicSqlItem dynamicSqlItem : dynamicSqlItems) {
            if (dynamicSqlItem.getDynamicSqlTpye().getLevel() > markingLevel) {
                markingLevel = dynamicSqlItem.getDynamicSqlTpye().getLevel();
                target = dynamicSqlItem.getDynamicSqlTpye();
            }
            if (markingLevel >= DynamicSqlTpye.getHighest())
                break;
        }
        return target;
    }

    private DynamicSqlHolderDO buildDynamicSqlHolderDO(String sql, DynamicSqlTpye dynamicSqlTpye) {
        return buildDynamicSqlHolderDO(sql, dynamicSqlTpye, null);
    }

    private DynamicSqlHolderDO buildDynamicSqlHolderDO(String sql, DynamicSqlTpye dynamicSqlTpye, String remark) {
        DynamicSqlHolderDO dynamicSqlHolderDO = new DynamicSqlHolderDO();
        dynamicSqlHolderDO.setSqlContent(sql);
        dynamicSqlHolderDO.setSqlTpye(dynamicSqlTpye.getSqlTpyeName());
        dynamicSqlHolderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlHolderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dynamicSqlHolderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        dynamicSqlHolderDO.setRemark(remark);
        Date date = new Date();
        dynamicSqlHolderDO.setCreateTime(date);
        dynamicSqlHolderDO.setUpdateTime(date);
        return dynamicSqlHolderDO;
    }

    private DynamicSqlItem rebuildDynamicSqlItem(DynamicSqlItem dynamicSqlItem) {
        if (dynamicSqlItem.getDynamicSqlTpye() != DynamicSqlTpye.DEFAULT) {
            if (dynamicSqlItem.hasDelete) {
                dynamicSqlItem.setDynamicSqlTpye(DynamicSqlTpye.DELETE);
            } else if (dynamicSqlItem.hasInsertInto) {
                dynamicSqlItem.setDynamicSqlTpye(DynamicSqlTpye.INSERT);
            } else if (dynamicSqlItem.hasUpdate) {
                dynamicSqlItem.setDynamicSqlTpye(DynamicSqlTpye.UPDATE);
            } else if (dynamicSqlItem.hasSelect) {
                dynamicSqlItem.setDynamicSqlTpye(DynamicSqlTpye.SELECT);
            }
        }
        return dynamicSqlItem;
    }

    private class DynamicSqlItem {
        String sql;
        DynamicSqlTpye dynamicSqlTpye;
        boolean hasSelect;
        boolean hasUpdate;
        boolean hasDelete;
        boolean hasInsertInto;
        boolean hasLimit;


        DynamicSqlItem() {
            dynamicSqlTpye = DynamicSqlTpye.DEFAULT;
            hasSelect = false;
            hasUpdate = false;
            hasDelete = false;
            hasInsertInto = false;
            hasLimit = false;
        }

        String getSql() {
            return sql;
        }

        void setSql(String sql) {
            this.sql = sql;
        }

        DynamicSqlTpye getDynamicSqlTpye() {
            return dynamicSqlTpye;
        }

        void setDynamicSqlTpye(DynamicSqlTpye dynamicSqlTpye) {
            this.dynamicSqlTpye = dynamicSqlTpye;
        }
    }

    private class StringParameterStateItem {

        boolean isSingleQuotationMarkString;
        boolean isDoubleQuotationMarkString;

        StringParameterStateItem() {
            isSingleQuotationMarkString = false;
            isDoubleQuotationMarkString = false;
        }

        void transform(char translateCharacter) {
            switch (translateCharacter) {
                case '\'':
                    if (isNotStringParameter() || isSingleQuotationMarkString)
                        isSingleQuotationMarkString = !isSingleQuotationMarkString;
                    break;

                case '\"':
                    if (isNotStringParameter() || isDoubleQuotationMarkString)
                        isDoubleQuotationMarkString = !isDoubleQuotationMarkString;
                    break;
                default:
                    break;
            }
        }

        boolean isNotStringParameter() {
            return !isSingleQuotationMarkString && !isDoubleQuotationMarkString;
        }

    }
/*
    private static final String SENSITIVE_INFO_VIEW = "***";

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
/*
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
    */
}