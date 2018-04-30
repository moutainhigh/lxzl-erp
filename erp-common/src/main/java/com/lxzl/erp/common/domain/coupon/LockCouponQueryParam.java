package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.order.pojo.Order;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\26 0026 16:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LockCouponQueryParam  extends BasePageParam {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
