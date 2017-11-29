package com.lxzl.erp.core.service.purchase.impl.support;

import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseDeliveryOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseDeliveryOrderProductDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderMaterialDO;
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
    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;
    @Autowired
    private PurchaseDeliveryOrderProductMapper purchaseDeliveryOrderProductMapper;

    public List<PurchaseOrderProductDO> getAllPurchaseOrderProductDO(Integer purchaseOrderId){
        Map<String,Object> map  = new HashMap<>();
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        map.put("purchaseOrderId",purchaseOrderId);
        return  purchaseOrderProductMapper.listPage(map);
    }
    public List<PurchaseOrderMaterialDO> getAllPurchaseOrderMaterialDO(Integer purchaseOrderId){
        Map<String,Object> map  = new HashMap<>();
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        map.put("purchaseOrderId",purchaseOrderId);
        return  purchaseOrderMaterialMapper.listPage(map);
    }

    public List<PurchaseDeliveryOrderProductDO> getAllPurchaseDeliveryOrderProductDO(Integer purchaseDeliveryOrderId){
        Map<String,Object> map  = new HashMap<>();
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        map.put("purchaseDeliveryOrderId",purchaseDeliveryOrderId);
        return  purchaseDeliveryOrderProductMapper.listPage(map);
    }
}
