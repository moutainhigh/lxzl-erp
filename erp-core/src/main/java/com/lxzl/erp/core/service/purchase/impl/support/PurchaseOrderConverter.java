package com.lxzl.erp.core.service.purchase.impl.support;

import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.workflow.impl.support.WorkflowConverter;
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
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
        if(purchaseOrderDO!=null&& CollectionUtil.isNotEmpty(purchaseOrderProductDOList)){
            List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();
            for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
                PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
                BeanUtils.copyProperties(purchaseOrderProductDO,purchaseOrderProduct);
                purchaseOrderProduct.setPurchaseOrderProductId(purchaseOrderProductDO.getId());
                purchaseOrderProductList.add(purchaseOrderProduct);
            }
            purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        }
        List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = purchaseOrderDO.getPurchaseOrderMaterialDOList();
        if(purchaseOrderDO!=null&& CollectionUtil.isNotEmpty(purchaseOrderMaterialDOList)){
            List<PurchaseOrderMaterial> purchaseOrderMaterialList = new ArrayList<>();
            for(PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderMaterialDOList){
                PurchaseOrderMaterial purchaseOrderMaterial = new PurchaseOrderMaterial();
                BeanUtils.copyProperties(purchaseOrderMaterialDO,purchaseOrderMaterial);
                purchaseOrderMaterial.setPurchaseOrderMaterialId(purchaseOrderMaterialDO.getId());
                purchaseOrderMaterialList.add(purchaseOrderMaterial);
            }
            purchaseOrder.setPurchaseOrderMaterialList(purchaseOrderMaterialList);
        }
        if(purchaseOrderDO!=null&&purchaseOrderDO.getWorkflowLinkDO()!=null){
            purchaseOrder.setWorkflowLink(WorkflowConverter.convertWorkflowLinkDO(purchaseOrderDO.getWorkflowLinkDO()));
        }
        return purchaseOrder;
    }
    public static PurchaseDeliveryOrder convertPurchaseDeliveryOrderDO(PurchaseDeliveryOrderDO purchaseDeliveryOrderDO){
        PurchaseDeliveryOrder purchaseDeliveryOrder = new PurchaseDeliveryOrder();
        BeanUtils.copyProperties(purchaseDeliveryOrderDO,purchaseDeliveryOrder);
        purchaseDeliveryOrder.setPurchaseOrderId(purchaseDeliveryOrderDO.getId());
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList();
        if(purchaseDeliveryOrderDO!=null&&CollectionUtil.isNotEmpty(purchaseDeliveryOrderProductDOList)){
            List<PurchaseDeliveryOrderProduct> purchaseDeliveryOrderProductList = new ArrayList<>();
            for(PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO : purchaseDeliveryOrderProductDOList){
                PurchaseDeliveryOrderProduct purchaseDeliveryOrderProduct = new PurchaseDeliveryOrderProduct();
                BeanUtils.copyProperties(purchaseDeliveryOrderProductDO,purchaseDeliveryOrderProduct);
                purchaseDeliveryOrderProduct.setPurchaseDeliveryOrderProductId(purchaseDeliveryOrderProductDO.getId());
                purchaseDeliveryOrderProductList.add(purchaseDeliveryOrderProduct);
            }
            purchaseDeliveryOrder.setPurchaseDeliveryOrderProductList(purchaseDeliveryOrderProductList);
        }
        List<PurchaseDeliveryOrderMaterialDO> purchaseDeliveryOrderMaterialDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderMaterialDOList();
        if(purchaseDeliveryOrderDO!=null&&CollectionUtil.isNotEmpty(purchaseDeliveryOrderMaterialDOList)){
            List<PurchaseDeliveryOrderMaterial> purchaseDeliveryOrderMaterialList = new ArrayList<>();
            for(PurchaseDeliveryOrderMaterialDO purchaseDeliveryOrderMaterialDO : purchaseDeliveryOrderMaterialDOList){
                PurchaseDeliveryOrderMaterial purchaseDeliveryOrderMaterial = new PurchaseDeliveryOrderMaterial();
                BeanUtils.copyProperties(purchaseDeliveryOrderMaterialDO,purchaseDeliveryOrderMaterial);
                purchaseDeliveryOrderMaterial.setPurchaseDeliveryOrderId(purchaseDeliveryOrderMaterialDO.getId());
                purchaseDeliveryOrderMaterialList.add(purchaseDeliveryOrderMaterial);
            }
            purchaseDeliveryOrder.setPurchaseDeliveryOrderMaterialList(purchaseDeliveryOrderMaterialList);
        }
        return purchaseDeliveryOrder;
    }

    public static PurchaseReceiveOrder convertPurchaseReceiveOrderDO(PurchaseReceiveOrderDO purchaseReceiveOrderDO){
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        BeanUtils.copyProperties(purchaseReceiveOrderDO,purchaseReceiveOrder);
        purchaseReceiveOrder.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
        List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
        if(purchaseReceiveOrderDO!=null&&CollectionUtil.isNotEmpty(purchaseReceiveOrderProductDOList)){
            List<PurchaseReceiveOrderProduct> purchaseDeliveryOrderProductList = new ArrayList<>();
            for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList){
                PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
                BeanUtils.copyProperties(purchaseReceiveOrderProductDO,purchaseReceiveOrderProduct);
                purchaseReceiveOrderProduct.setPurchaseReceiveOrderProductId(purchaseReceiveOrderProductDO.getId());
                purchaseDeliveryOrderProductList.add(purchaseReceiveOrderProduct);
            }
            purchaseReceiveOrder.setPurchaseReceiveOrderProductList(purchaseDeliveryOrderProductList);
        }
        List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
        if(purchaseReceiveOrderDO!=null&&CollectionUtil.isNotEmpty(purchaseReceiveOrderMaterialDOList)){
            List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList = new ArrayList<>();
            for(PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : purchaseReceiveOrderMaterialDOList){
                PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial = new PurchaseReceiveOrderMaterial();
                BeanUtils.copyProperties(purchaseReceiveOrderMaterialDO,purchaseReceiveOrderMaterial);
                purchaseReceiveOrderMaterial.setPurchaseReceiveOrderMaterialId(purchaseReceiveOrderMaterialDO.getId());
                purchaseReceiveOrderMaterialList.add(purchaseReceiveOrderMaterial);
            }
            purchaseReceiveOrder.setPurchaseReceiveOrderMaterialList(purchaseReceiveOrderMaterialList);
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
