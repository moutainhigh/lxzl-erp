package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;

import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/2
 * @Time : Created in 17:37
 */
public interface ImportMaterialService {

    ServiceResult<String, Map<String, String>> importData(String str) throws Exception;
}
