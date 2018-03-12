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
}
