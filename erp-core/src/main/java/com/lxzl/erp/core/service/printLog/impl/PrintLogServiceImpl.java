package com.lxzl.erp.core.service.printLog.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import com.lxzl.erp.core.service.printLog.PrintLogService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.printLog.PrintLogMapper;
import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/4
 * @Time : Created in 11:40
 */
@Service
public class PrintLogServiceImpl implements PrintLogService {

    @Autowired
    private PrintLogMapper printLogMapper;

    @Autowired
    private UserSupport userSupport;

    @Override
    public ServiceResult<String, Integer> updatePrintLog(PrintLog printLog) {

        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        PrintLogDO printLogDO = printLogMapper.findByNoAndType(printLog.getReferNo(), printLog.getReferType());
        if(printLogDO != null){
            printLogDO.setPrintCount(printLogDO.getPrintCount()+1);
            printLogDO.setRemark(printLog.getRemark());
            printLogDO.setUpdateTime(now);
            printLogDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            printLogMapper.update(printLogDO);
        }else {
            printLogDO = new PrintLogDO();
            printLogDO.setReferNo(printLog.getReferNo());
            printLogDO.setReferType(printLog.getReferType());
            printLogDO.setPrintCount(CommonConstant.COMMON_ONE);
            printLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            printLogDO.setRemark(printLog.getRemark());
            printLogDO.setCreateTime(now);
            printLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            printLogDO.setUpdateTime(now);
            printLogDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            printLogMapper.save(printLogDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(printLogDO.getId());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, PrintLogDO> queryPrintLog(PrintLog printLog) {

        ServiceResult<String, PrintLogDO> serviceResult = new ServiceResult<>();
        PrintLogDO printLogDO = printLogMapper.findByNoAndType(printLog.getReferNo(), printLog.getReferType());
        if(printLogDO == null){
            serviceResult.setErrorCode(ErrorCode.PRINT_LOG_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(printLogDO);
        return serviceResult;
    }
}
