package com.lxzl.erp.core.service.purchase.impl.support;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrderProduct;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderProductDO;
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

                if(purchaseOrderProductDO.getProductSnapshot()!=null){
                    Product product = JSON.parseObject(purchaseOrderProductDO.getProductSnapshot(),Product.class);
                    purchaseOrderProduct.setProduct(product);
                }
                purchaseOrderProductList.add(purchaseOrderProduct);
            }
            purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        }
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
