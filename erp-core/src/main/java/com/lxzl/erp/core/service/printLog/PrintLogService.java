package com.lxzl.erp.core.service.printLog;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;

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
    ServiceResult<String,Integer> updatePrintLog(PrintLog printLog);
    /**
    * 条件查询
    * @Author : XiaoLuYu
    * @Date : Created in 2018/5/4 14:04
    * @param : printLog
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.printLog.pojo.PrintLog>
    */
    ServiceResult<String,PrintLogDO> queryPrintLog(PrintLog printLog);
}
