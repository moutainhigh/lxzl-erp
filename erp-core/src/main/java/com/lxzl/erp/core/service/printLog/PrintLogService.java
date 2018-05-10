package com.lxzl.erp.core.service.printLog;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.PrintLogPageParam;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;

import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/4
 * @Time : Created in 11:38
 */
public interface PrintLogService {
    /**
    * 保存
    * @Author : XiaoLuYu
    * @Date : Created in 2018/5/4 14:03
    * @param : printLog
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
    */
    ServiceResult<String,Integer> savePrintLog(PrintLog printLog);
    /**
    * 条件查询
    * @Author : XiaoLuYu
    * @Date : Created in 2018/5/4 14:04
    * @param : printLog
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.printLog.pojo.PrintLog>
    */
    ServiceResult<String,Page<PrintLog>> pagePrintLog(PrintLogPageParam printLogPageParam);

    /**
     * 条件查询
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/4 14:04
     * @param : printLog
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.printLog.pojo.PrintLog>
     */
    ServiceResult<String,Map<String,Integer >> queryPrintLogCount(PrintLogPageParam printLogPageParam);
}
