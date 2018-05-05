package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/5/5 10:33
 */
public interface SelectProductService {
    ServiceResult<String, Map<String, String>> selectData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException ;
}
