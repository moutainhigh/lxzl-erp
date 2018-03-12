package com.lxzl.erp.web.excel;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-12 23:21
 */
public class ImportTest extends ERPUnTransactionalTest {
    @Test
    public void importProduct() throws Exception {

//        TestResult testResult = getJsonTestResult("/excel/importAssemblyProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importNodeProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importAIOProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importTVProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importMonitorProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importPhoneProduct", null);
//        TestResult testResult = getJsonTestResult("/excel/importPrinterProduct", null);
        TestResult testResult = getJsonTestResult("/excel/importProjectorProduct", null);
    }
}
