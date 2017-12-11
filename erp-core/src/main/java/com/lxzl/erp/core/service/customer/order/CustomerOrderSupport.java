package com.lxzl.erp.core.service.customer.order;


import com.lxzl.erp.common.domain.base.BasePageParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomerOrderSupport {
    public Map<String, Object> getCustomerCanReturnMap(BasePageParam basePageParam, Integer customerId) {
        Map<String, Object> map = getCustomerMap(basePageParam, customerId);
        map.put("onEquipment", 0);
        return map;
    }

    public Map<String, Object> getCustomerMap(BasePageParam basePageParam, Integer customerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("pageSize", basePageParam.getPageSize());
        map.put("start", basePageParam.getStart());
        return map;
    }
    public Map<String, Object> getCustomerCanReturnAllMap(Integer customerId) {
        Map<String, Object> map = getCustomerAllMap(customerId);
        map.put("onEquipment", 0);
        return map;
    }

    public Map<String, Object> getCustomerAllMap(Integer customerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("pageSize", Integer.MAX_VALUE);
        map.put("start", 0);
        return map;
    }
}
