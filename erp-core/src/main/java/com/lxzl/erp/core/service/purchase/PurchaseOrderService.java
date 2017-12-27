package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.purchase.*;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.core.service.VerifyReceiver;

import java.util.List;


public interface PurchaseOrderService extends VerifyReceiver {

    ServiceResult<String, String> add(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> update(PurchaseOrder purchaseOrder);

    ServiceResult<String, PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo);

    ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);

    ServiceResult<String, String> commit(PurchaseOrderCommitParam purchaseOrderCommitParam);

    String cancel(PurchaseOrder purchaseOrder);

    ServiceResult<String, Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam);

    ServiceResult<String, PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder);

    ServiceResult<String, String> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, String> confirmPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, Page<PurchaseReceiveOrder>> pagePurchaseReceive(PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam);

    ServiceResult<String, PurchaseReceiveOrder> queryPurchaseReceiveOrderByNo(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, String> endPurchaseOrder(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> continuePurchaseOrder(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> updateReceiveEquipmentPrice(UpdatePurchaseReceiveEquipmentPriceParam updatePurchaseReceiveEquipmentPriceParam);

    ServiceResult<String, Page<ProductEquipment>> pageReceiveOrderProductEquipment(PurchaseReceiveOrderProductEquipmentPageParam purchaseReceiveOrderProductEquipmentPageParam);

    ServiceResult<String, Page<BulkMaterial>> pageReceiveOrderMaterialBulk(PurchaseReceiveOrderMaterialBulkPageParam purchaseReceiveOrderMaterialBulkPageParam);

    ServiceResult<String,String> updateReceiveEquipmentRemark(UpdateReceiveEquipmentRemarkParam updateReceiveEquipmentRemarkParam);

    ServiceResult<String,String> commitPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String,List<PurchaseReceiveOrderMaterialPrice>> getPurchaseReceiveMaterialPriceList(PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial);

    ServiceResult<String,Integer> updatePurchaseReceiveMaterialPrice(UpdatePurchaseReceiveMaterialPriceParam updatePurchaseReceiveMaterialPriceParam);

    ServiceResult<String,String> updatePurchaseReceiveMaterialRemark(UpdateReceiveMaterialRemarkParam updateReceiveMaterialRemarkParam);
}
