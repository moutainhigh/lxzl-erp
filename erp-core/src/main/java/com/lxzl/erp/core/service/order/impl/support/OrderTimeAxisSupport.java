package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderTimeAxisMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderTimeAxisDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-29 9:15
 */
@Component
public class OrderTimeAxisSupport {


    public void addOrderTimeAxis(Integer orderId, Integer orderStatus, String orderSnapshot, Date currentTime, Integer loginUserId,Integer operationType) {
        String userId = loginUserId==null?null:loginUserId.toString();
        if(loginUserId!=null){
            addOrderTimeAxis( orderId,  orderStatus,  orderSnapshot,  currentTime, userId,operationType);
        }
    }
    public void addOrderTimeAxis(Integer orderId, Integer orderStatus, String orderSnapshot, Date currentTime, String loginUserId,Integer operationType) {

        List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisMapper.findByOrderId(orderId);
        if (CollectionUtil.isNotEmpty(orderTimeAxisDOList)) {
            OrderTimeAxisDO lastRecord = orderTimeAxisDOList.get(orderTimeAxisDOList.size() - 1);
            // 如果最后的状态和当前状态相同，就没有必要再存一条
            if (orderStatus.equals(lastRecord.getOrderStatus())) {
                lastRecord.setUpdateUser(loginUserId);
                lastRecord.setUpdateTime(currentTime);
                lastRecord.setOperationType(operationType);
                orderTimeAxisMapper.update(lastRecord);
                return;
            }
        }
        OrderTimeAxisDO orderTimeAxisDO = new OrderTimeAxisDO();
        orderTimeAxisDO.setOrderId(orderId);
        orderTimeAxisDO.setOrderStatus(orderStatus);
        orderTimeAxisDO.setGenerationTime(currentTime);
        orderTimeAxisDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderTimeAxisDO.setCreateUser(loginUserId);
        orderTimeAxisDO.setUpdateUser(loginUserId);
        orderTimeAxisDO.setCreateTime(currentTime);
        orderTimeAxisDO.setUpdateTime(currentTime);
        orderTimeAxisDO.setOperationType(operationType);
        orderTimeAxisMapper.save(orderTimeAxisDO);
    }
    public List<OrderTimeAxisDO> getOrderTimeAxis(Integer orderId) {
        return orderTimeAxisMapper.findByOrderId(orderId);
    }

    @Autowired
    private OrderTimeAxisMapper orderTimeAxisMapper;
}
