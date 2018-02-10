package com.lxzl.erp.core.service.exclt;

import com.lxzl.erp.common.domain.ServiceResult;

import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/2
 * @Time : Created in 10:18
 */
public interface ImplK3MappingCustomerService {
    /**
     * 导入用户数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/23 16:08
     * @param : str
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>
     */
    ServiceResult<String, Map<String, String>> importData(String str) throws Exception;

}
