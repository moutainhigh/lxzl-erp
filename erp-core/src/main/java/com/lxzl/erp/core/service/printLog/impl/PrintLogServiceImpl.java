package com.lxzl.erp.core.service.printLog.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.PrintLogPageParam;
import com.lxzl.erp.common.domain.printLog.PrintLogResponseValue;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.printLog.PrintLogService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.printLog.PrintLogMapper;
import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ServiceResult<String, Integer> savePrintLog(PrintLog printLog) {

        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        PrintLogDO printLogDO = new PrintLogDO();
        printLogDO.setReferNo(printLog.getReferNo());
        printLogDO.setReferType(printLog.getReferType());
        printLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        printLogDO.setRemark(printLog.getRemark());
        printLogDO.setCreateTime(now);
        printLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        printLogMapper.save(printLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(printLogDO.getId());
        return serviceResult;
    }

    @Override
    public ServiceResult<String,PrintLogResponseValue> queryPrintLogCount(PrintLogPageParam printLogPageParam) {

        ServiceResult<String, PrintLogResponseValue> serviceResult = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("printLogPageParam", printLogPageParam);
        Integer count = printLogMapper.findBankSlipCountByParams(maps);
        PrintLogResponseValue printLogResponseValue = new PrintLogResponseValue();
        printLogResponseValue.setPrintLogCount(count);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(printLogResponseValue);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PrintLog>> pagePrintLog(PrintLogPageParam printLogPageParam) {



        ServiceResult<String, Page<PrintLog>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(printLogPageParam.getPageNo(), printLogPageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("printLogPageParam", printLogPageParam);

        Integer count = printLogMapper.findBankSlipCountByParams(maps);
        List<PrintLogDO> printLogDOList = printLogMapper.findBankSlipByParams(maps);

        List<PrintLog> printLogList = ConverterUtil.convertList(printLogDOList, PrintLog.class);
        Page<PrintLog> page = new Page<>(printLogList, count, printLogPageParam.getPageNo(), printLogPageParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

}
