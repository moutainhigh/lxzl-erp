package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.ReturnOrderRollbackLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderReturnDetailMapper;
import com.lxzl.erp.dataaccess.domain.k3.ReturnOrderRollbackLogDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
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

    @Autowired
    private ReturnOrderRollbackLogMapper returnOrderRollbackLogMapper;

    public void saveStatementReturnRecord(Integer statementOrderId, Integer customerId, Integer orderId, Integer orderItemReferId, Integer returnType, BigDecimal returnAmount, Date returnTime, Integer loginUserId, Date currentTime) {
        saveStatementReturnRecord( statementOrderId,  customerId, orderId,  orderItemReferId,  returnType,  returnAmount, returnTime, loginUserId,currentTime,null,null);
    }

    public void saveStatementReturnRecord(Integer statementOrderId, Integer customerId, Integer orderId, Integer orderItemReferId, Integer returnType, BigDecimal returnAmount, Date returnTime, Integer loginUserId, Date currentTime,Integer returnOrderId,Integer returnOrderDetailId) {
        //为零不保存
        if(BigDecimalUtil.compare(returnAmount,BigDecimal.ZERO)==0)return;
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
        statementOrderReturnDetailDO.setReturnOrderId(returnOrderId);
        statementOrderReturnDetailDO.setReturnOrderDetailId(returnOrderDetailId);
        statementOrderReturnDetailMapper.save(statementOrderReturnDetailDO);
    }

    public void saveReturnOrderRollLogToDB(K3ReturnOrderDO k3ReturnOrderDO,String userId) {
        ReturnOrderRollbackLogDO logDO=new ReturnOrderRollbackLogDO();
        logDO.setReturnOrderId(k3ReturnOrderDO.getId());
        logDO.setReturnOrderNo(k3ReturnOrderDO.getReturnOrderNo());
        logDO.setCreateTime(new Date());
        logDO.setCreateUser(userId);
        returnOrderRollbackLogMapper.save(logDO);
    }


}
