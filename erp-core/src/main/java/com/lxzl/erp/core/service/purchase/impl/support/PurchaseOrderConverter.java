package com.lxzl.erp.core.service.purchase.impl.support;

import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderConverter {
    public static PurchaseOrderDO convertPurchaseOrder(PurchaseOrder purchaseOrder){
        PurchaseOrderDO purchaseOrderDO = new PurchaseOrderDO();
        BeanUtils.copyProperties(purchaseOrder,purchaseOrderDO);
        purchaseOrderDO.setId(purchaseOrder.getPurchaseOrderId());
        return purchaseOrderDO;
    }
    public static PurchaseOrder convertPurchaseOrderDO(PurchaseOrderDO purchaseOrderDO){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        BeanUtils.copyProperties(purchaseOrderDO,purchaseOrder);
        purchaseOrder.setPurchaseOrderId(purchaseOrderDO.getId());
        return purchaseOrder;
    }
    public static List<PurchaseOrder> convertPurchaseOrderDOList(List<PurchaseOrderDO> purchaseOrderDOList){
        List<PurchaseOrder> purchaseOrderList = new ArrayList<>();
        if (purchaseOrderDOList != null && purchaseOrderDOList.size() > 0) {
            for (PurchaseOrderDO purchaseOrderDO : purchaseOrderDOList) {
                purchaseOrderList.add(convertPurchaseOrderDO(purchaseOrderDO));
            }
        }
        return purchaseOrderList;
    }
}
