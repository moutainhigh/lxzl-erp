package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderJointProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJointProductMapper extends BaseMysqlDAO<OrderJointProductDO> {

    List<OrderJointProductDO> findByOrderId(@Param("orderId") Integer orderId);
}
