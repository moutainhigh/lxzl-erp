package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/1
 * @Time : Created in 21:11
 */
public interface ImplK3MappingMaterialService {
    /**
    * 导入数据
    * @Author : XiaoLuYu
    * @Date : Created in 2018/2/1 21:13
    * @param : str
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>
    */
    ServiceResult<String, Map<String, String>> importData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InvalidFormatException;
}
