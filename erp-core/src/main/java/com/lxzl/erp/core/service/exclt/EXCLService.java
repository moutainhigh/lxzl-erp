package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/10
 * @Time : Created in 21:04
 */
public interface EXCLService {
    //导入数据
    ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException;
}
