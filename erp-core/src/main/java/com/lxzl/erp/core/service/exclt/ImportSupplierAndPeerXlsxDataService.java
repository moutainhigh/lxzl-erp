package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/25
 * @Time : Created in 9:48
 */
public interface ImportSupplierAndPeerXlsxDataService {
    /**
    * 导入供应商数据
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/25 9:59
    * @param : str
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>
    */
    ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
