package com.lxzl.erp.core.service.excel;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-13 1:14
 */
public interface ImportProductService {
    String importAssemblyProduct(String filePath) throws Exception;
    String importNodeProduct(String filePath) throws Exception;
    String importAIOProduct(String filePath) throws Exception;
    String importTVProduct(String filePath) throws Exception;
    String importMonitorProduct(String filePath) throws Exception;
    String importPhoneProduct(String filePath) throws Exception;
    String importPrinterProduct(String filePath) throws Exception;
    String importProjectorProduct(String filePath) throws Exception;
    String importPadProduct(String filePath) throws Exception;
    String importCopierProduct(String filePath) throws Exception;
    String importSwitchboardProduct(String filePath) throws Exception;
    String importRouterProduct(String filePath) throws Exception;
    String importServerProduct(String filePath) throws Exception;
}
