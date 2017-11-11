package com.lxzl.erp.core.service.system.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.system.pojo.DataDictionary;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-10 9:25
 */
public class DataDictionaryConverter {

    public static DataDictionary convertDataDictionaryDO(DataDictionaryDO dataDictionaryDO) {
        DataDictionary dataDictionary = new DataDictionary();
        if (dataDictionaryDO.getId() != null) {
            dataDictionary.setValue(dataDictionaryDO.getId());
        }
        if (dataDictionaryDO.getDataName() != null) {
            dataDictionary.setLabel(dataDictionaryDO.getDataName());
        }
        if (dataDictionaryDO.getParentDictionaryId() != null) {
            dataDictionary.setParentDictionaryId(dataDictionaryDO.getParentDictionaryId());
        }
        if (dataDictionaryDO.getDataType() != null) {
            dataDictionary.setDataType(dataDictionaryDO.getDataType());
        }
        if (dataDictionaryDO.getDataOrder() != null) {
            dataDictionary.setDataOrder(dataDictionaryDO.getDataOrder());
        }
        if (dataDictionaryDO.getDataStatus() != null) {
            dataDictionary.setDataStatus(dataDictionaryDO.getDataStatus());
        }
        if (dataDictionaryDO.getRemark() != null) {
            dataDictionary.setRemark(dataDictionaryDO.getRemark());
        }
        if(dataDictionaryDO.getChildren() != null && !dataDictionaryDO.getChildren().isEmpty()){
            dataDictionary.setChildren(convertDataDictionaryDOList(dataDictionaryDO.getChildren()));
        }
        return dataDictionary;
    }

    public static DataDictionaryDO convertDataDictionary(DataDictionary dataDictionary) {
        DataDictionaryDO dataDictionaryDO = new DataDictionaryDO();
        if (dataDictionary.getValue() != null) {
            dataDictionaryDO.setId(dataDictionary.getValue());
        }
        if (dataDictionary.getLabel() != null) {
            dataDictionaryDO.setDataName(dataDictionary.getLabel());
        }
        if (dataDictionary.getParentDictionaryId() != null) {
            dataDictionaryDO.setParentDictionaryId(dataDictionary.getParentDictionaryId());
        }
        if (dataDictionary.getDataType() != null) {
            dataDictionaryDO.setDataType(dataDictionary.getDataType());
        }
        if (dataDictionary.getDataOrder() != null) {
            dataDictionaryDO.setDataOrder(dataDictionary.getDataOrder());
        }
        if (dataDictionary.getDataStatus() != null) {
            dataDictionaryDO.setDataStatus(dataDictionary.getDataStatus());
        } else {
            dataDictionaryDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        }
        if (dataDictionary.getRemark() != null) {
            dataDictionaryDO.setRemark(dataDictionary.getRemark());
        }
        return dataDictionaryDO;
    }


    public static List<DataDictionary> convertDataDictionaryDOList(List<DataDictionaryDO> dataDictionaryDOList) {
        List<DataDictionary> dataDictionaryList = new ArrayList<>();
        if(dataDictionaryDOList != null && dataDictionaryDOList.size() > 0 ){
            for(DataDictionaryDO dataDictionaryDO : dataDictionaryDOList){
                dataDictionaryList.add(convertDataDictionaryDO(dataDictionaryDO));
            }
        }
        return dataDictionaryList;
    }

}
