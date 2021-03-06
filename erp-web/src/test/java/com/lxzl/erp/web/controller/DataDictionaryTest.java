package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.system.pojo.DataDictionary;
import org.junit.Test;
/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-10 9:38
 */
public class DataDictionaryTest extends ERPUnTransactionalTest {


    @Test
    public void getWarehousePage() throws Exception {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setLabel("1");
        TestResult result = getJsonTestResult("/data/addDictionary", dataDictionary);
    }
}
