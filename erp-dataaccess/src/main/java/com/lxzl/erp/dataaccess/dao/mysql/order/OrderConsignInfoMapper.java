package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderConsignInfoDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderConsignInfoMapper extends BaseMysqlDAO<OrderConsignInfoDO> {
    OrderConsignInfoDO findByOrderId(@Param("orderId") Integer orderId);
}
