package com.lxzl.erp.core.service.system.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.DataDictionary;
import com.lxzl.erp.common.domain.system.DataDictionaryQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.system.DataDictionaryService;
import com.lxzl.erp.core.service.system.impl.support.DataDictionaryConverter;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/2.
 * Time: 11:17.
 */
@Service("dataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Autowired
    private DataDictionaryMapper dataDictionaryMysqlMapper;

    @Autowired(required = false)
    private HttpSession session;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addDictionary(DataDictionary dataDictionary) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        DataDictionaryDO dataDictionaryDO = DataDictionaryConverter.convertDataDictionary(dataDictionary);
        dataDictionaryDO.setUpdateUser(loginUser.getUserId().toString());
        dataDictionaryDO.setCreateUser(loginUser.getUserId().toString());
        dataDictionaryDO.setCreateTime(new Date());
        dataDictionaryDO.setUpdateTime(new Date());
        dataDictionaryMysqlMapper.save(dataDictionaryDO);
        result.setResult(dataDictionaryDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateDictionary(DataDictionary dataDictionary) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String verifyCode = verifyData(dataDictionary);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }
        DataDictionaryDO dbRecord = dataDictionaryMysqlMapper.findByDictionaryId(dataDictionary.getValue());
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        DataDictionaryDO dataDictionaryDO = DataDictionaryConverter.convertDataDictionary(dataDictionary);

        if (loginUser != null) {
            dataDictionaryDO.setUpdateUser(loginUser.getUserId().toString());
        }
        dataDictionaryDO.setUpdateTime(new Date());
        dataDictionaryMysqlMapper.update(dataDictionaryDO);
        result.setResult(dataDictionaryDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    private String verifyData(DataDictionary dataDictionary) {
        if (dataDictionary.getValue() == null || dataDictionary.getValue() == 0) {
            return ErrorCode.ID_NOT_NULL;
        }

        return ErrorCode.SUCCESS;

    }

    @Override
    public ServiceResult<String, Integer> deleteDictionary(Integer dictionaryId) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        DataDictionaryDO dbRecord = dataDictionaryMysqlMapper.findByDictionaryId(dictionaryId);
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        dbRecord.setUpdateUser(loginUser.getUserId().toString());
        dbRecord.setUpdateTime(new Date());
        dbRecord.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        dataDictionaryMysqlMapper.update(dbRecord);

        result.setResult(dictionaryId);
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    @Override
    public ServiceResult<String, Page<DataDictionary>> findAllData(DataDictionaryQueryParam param) {
        ServiceResult<String, Page<DataDictionary>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> params = new HashMap<>();
        if (param.getDataId() != null) {
            params.put("dictionaryId", param.getDataId());
        }
        if (StringUtil.isNotBlank(param.getDataName())) {
            params.put("dataName", param.getDataName());
        }
        if (param.getDataStatus() != null) {
            params.put("status", param.getDataStatus());
        }
        if (param.getDataType() != null) {
            params.put("dataType", param.getDataType());
        }

        params.put("start", pageQuery.getStart());
        params.put("pageSize", pageQuery.getPageSize());

        List<DataDictionaryDO> dataList = dataDictionaryMysqlMapper.findAllData(params);
        Integer totalCount = dataDictionaryMysqlMapper.findAllDataCount(params);

        List<DataDictionaryDO> nodeList = new ArrayList<>();
        if (param.getDataId() != null) {
            for (DataDictionaryDO node1 : dataList) {
                if (node1.getId().equals(param.getDataId())) {
                    nodeList.add(node1);
                }
                for (DataDictionaryDO t : dataList) {
                    if (t.getParentDictionaryId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<DataDictionaryDO> myChildren = new ArrayList<>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }
                    }
                }
            }
        } else if (StringUtil.isNotBlank(param.getDataName())) {
            nodeList = dataList;
        } else {
            for (DataDictionaryDO node1 : dataList) {
                if (node1.getParentDictionaryId().equals(CommonConstant.SUPER_DATA_DICTIONARY_ID)) {
                    nodeList.add(node1);
                }
                for (DataDictionaryDO t : dataList) {
                    if (t.getParentDictionaryId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<DataDictionaryDO> myChildren = new ArrayList<>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }


                    }
                }
            }
        }

        Page<DataDictionary> page = new Page<>(DataDictionaryConverter.convertDataDictionaryDOList(nodeList), totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, List<DataDictionary>> findDataByType(Integer dataType) {
        ServiceResult<String, List<DataDictionary>> result = new ServiceResult<>();
        List<DataDictionaryDO> dataList = dataDictionaryMysqlMapper.findDataByType(dataType);
        List<DataDictionaryDO> nodeList = new ArrayList<>();
        for (DataDictionaryDO node1 : dataList) {
            if (node1.getParentDictionaryId().equals(CommonConstant.SUPER_DATA_DICTIONARY_ID)) {
                nodeList.add(node1);
            }
            for (DataDictionaryDO t : dataList) {
                if (t.getParentDictionaryId().equals(node1.getId())) {
                    if (node1.getChildren() == null) {
                        List<DataDictionaryDO> myChildren = new ArrayList<>();
                        myChildren.add(t);
                        node1.setChildren(myChildren);
                    } else {
                        node1.getChildren().add(t);
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(DataDictionaryConverter.convertDataDictionaryDOList(nodeList));

        return result;
    }
}
