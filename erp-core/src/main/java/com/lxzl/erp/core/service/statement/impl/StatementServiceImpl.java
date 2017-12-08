package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 描述: 结算单服务类
 *
 * @author gaochao
 * @date 2017-12-06 15:34
 */
@Service("statementService")
public class StatementServiceImpl implements StatementService {

    @Override
    public ServiceResult<String, String> createStatement(Date startDate, Date endDate) {
        ServiceResult<String, String> result = new ServiceResult<>();
        // TODO 查询在这期间范围内，欠款的用户的需要支付的订单。

        // 根据用户

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
}
