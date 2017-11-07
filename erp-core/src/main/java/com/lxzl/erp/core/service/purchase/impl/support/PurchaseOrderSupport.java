package com.lxzl.erp.core.service.purchase.impl.support;

import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PurchaseOrderSupport {

    @Autowired
    private PurchaseOrderProductMapper purchaseOrderProductMapper;
    public List<PurchaseOrderProductDO> getAllPurchaseOrderProductDO(Integer purchaseOrderId){
        Map<String,Object> map  = new HashMap<>();
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        map.put("purchase_order_id",purchaseOrderId);
        return  purchaseOrderProductMapper.listPage(map);
    }

}
