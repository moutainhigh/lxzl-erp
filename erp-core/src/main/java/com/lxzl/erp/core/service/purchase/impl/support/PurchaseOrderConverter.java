package com.lxzl.erp.core.service.purchase.impl.support;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.purchase.*;
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
        if(purchaseOrderDO!=null&&purchaseOrderDO.getPurchaseOrderProductDOList()!=null&&purchaseOrderDO.getPurchaseOrderProductDOList().size()>0){
            List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();
            List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
            for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
                PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
                BeanUtils.copyProperties(purchaseOrderProductDO,purchaseOrderProduct);
                purchaseOrderProduct.setPurchaseOrderProductId(purchaseOrderProductDO.getId());
                purchaseOrderProductList.add(purchaseOrderProduct);
            }
            purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        }
        return purchaseOrder;
    }
    public static PurchaseDeliveryOrder convertPurchaseDeliveryOrderDO(PurchaseDeliveryOrderDO purchaseDeliveryOrderDO){
        PurchaseDeliveryOrder purchaseDeliveryOrder = new PurchaseDeliveryOrder();
        BeanUtils.copyProperties(purchaseDeliveryOrderDO,purchaseDeliveryOrder);
        purchaseDeliveryOrder.setPurchaseOrderId(purchaseDeliveryOrderDO.getId());
        if(purchaseDeliveryOrderDO!=null&&purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList()!=null&&purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList().size()>0){
            List<PurchaseDeliveryOrderProduct> purchaseDeliveryOrderProductList = new ArrayList<>();
            List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList();
            for(PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO : purchaseDeliveryOrderProductDOList){
                PurchaseDeliveryOrderProduct purchaseDeliveryOrderProduct = new PurchaseDeliveryOrderProduct();
                BeanUtils.copyProperties(purchaseDeliveryOrderProductDO,purchaseDeliveryOrderProduct);
                purchaseDeliveryOrderProduct.setPurchaseDeliveryOrderProductId(purchaseDeliveryOrderProductDO.getId());
                purchaseDeliveryOrderProductList.add(purchaseDeliveryOrderProduct);
            }
            purchaseDeliveryOrder.setPurchaseDeliveryOrderProductList(purchaseDeliveryOrderProductList);
        }
        return purchaseDeliveryOrder;
    }

    public static PurchaseReceiveOrder convertPurchaseReceiveOrderDO(PurchaseReceiveOrderDO purchaseReceiveOrderDO){
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        BeanUtils.copyProperties(purchaseReceiveOrderDO,purchaseReceiveOrder);
        purchaseReceiveOrder.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
        if(purchaseReceiveOrderDO!=null&&purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList()!=null&&purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList().size()>0){
            List<PurchaseReceiveOrderProduct> purchaseDeliveryOrderProductList = new ArrayList<>();
            List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
            for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList){
                PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
                BeanUtils.copyProperties(purchaseReceiveOrderProductDO,purchaseReceiveOrderProduct);
                purchaseReceiveOrderProduct.setPurchaseReceiveOrderProductId(purchaseReceiveOrderProductDO.getId());
                purchaseDeliveryOrderProductList.add(purchaseReceiveOrderProduct);
            }
            purchaseReceiveOrder.setPurchaseReceiveOrderProductList(purchaseDeliveryOrderProductList);
        }
        return purchaseReceiveOrder;
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
    public static List<PurchaseDeliveryOrder> convertPurchaseDeliveryOrderDOList(List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList){
        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = new ArrayList<>();
        if (purchaseDeliveryOrderDOList != null && purchaseDeliveryOrderDOList.size() > 0) {
            for (PurchaseDeliveryOrderDO purchaseDeliveryOrderDO : purchaseDeliveryOrderDOList) {
                purchaseDeliveryOrderList.add(convertPurchaseDeliveryOrderDO(purchaseDeliveryOrderDO));
            }
        }
        return purchaseDeliveryOrderList;
    }
    public static List<PurchaseReceiveOrder> convertPurchaseReceiveOrderDOList(List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList){
        List<PurchaseReceiveOrder> purchaseReceiveOrderList = new ArrayList<>();
        if (purchaseReceiveOrderDOList != null && purchaseReceiveOrderDOList.size() > 0) {
            for (PurchaseReceiveOrderDO purchaseReceiveOrderDO : purchaseReceiveOrderDOList) {
                purchaseReceiveOrderList.add(convertPurchaseReceiveOrderDO(purchaseReceiveOrderDO));
            }
        }
        return purchaseReceiveOrderList;
    }
}
