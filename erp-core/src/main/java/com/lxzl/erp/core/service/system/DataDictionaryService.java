package com.lxzl.erp.core.service.system;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.DataDictionary;
import com.lxzl.erp.common.domain.system.DataDictionaryQueryParam;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/2.
 * Time: 11:16.
 */
public interface DataDictionaryService extends BaseService {

    ServiceResult<String, Integer> addDictionary(DataDictionary dataDictionary);

    ServiceResult<String, Integer> updateDictionary(DataDictionary dataDictionary);

    ServiceResult<String, Integer> deleteDictionary(Integer dictionaryId);

    ServiceResult<String, Page<DataDictionary>> findAllData(DataDictionaryQueryParam param);

    ServiceResult<String, List<DataDictionary>> findDataByType(Integer dataType);
}
