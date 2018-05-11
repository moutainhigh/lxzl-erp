package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderSplitDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 14:01
 * @Description:
 */
public interface OrderSplitDetailMapper extends BaseMysqlDAO<OrderSplitDetailDO> {

    Integer findOrderSplitDetailCountByParams(@Param("maps") Map<String, Object> paramMap);

    List<OrderSplitDetailDO> findOrderSplitDetailByParams(@Param("maps") Map<String, Object> paramMap);

    Integer deleteByIds(@Param("ids") List<Integer> ids);

    Integer deleteByItemTypeAndItemId(@Param("orderItemType") Integer orderItemType, @Param("orderItemReferId") Integer orderItemReferId);
}
