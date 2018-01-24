package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderReturnDetailMapper;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderReturnDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-24 9:30
 */
@Component
public class StatementReturnSupport {
    @Autowired
    private StatementOrderReturnDetailMapper statementOrderReturnDetailMapper;

    public void saveStatementReturnRecord(Integer statementOrderId, Integer customerId, Integer orderId, Integer orderItemReferId, Integer returnType, BigDecimal returnAmount, Date returnTime, Integer loginUserId, Date currentTime) {
        StatementOrderReturnDetailDO statementOrderReturnDetailDO = new StatementOrderReturnDetailDO();
        statementOrderReturnDetailDO.setStatementOrderId(statementOrderId);
        statementOrderReturnDetailDO.setCustomerId(customerId);
        statementOrderReturnDetailDO.setOrderId(orderId);
        statementOrderReturnDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderReturnDetailDO.setReturnType(returnType);
        statementOrderReturnDetailDO.setReturnAmount(returnAmount);
        statementOrderReturnDetailDO.setReturnTime(returnTime);
        statementOrderReturnDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        statementOrderReturnDetailDO.setCreateUser(loginUserId.toString());
        statementOrderReturnDetailDO.setCreateTime(currentTime);
        statementOrderReturnDetailDO.setUpdateUser(loginUserId.toString());
        statementOrderReturnDetailDO.setUpdateTime(currentTime);
        statementOrderReturnDetailMapper.save(statementOrderReturnDetailDO);
    }
}
