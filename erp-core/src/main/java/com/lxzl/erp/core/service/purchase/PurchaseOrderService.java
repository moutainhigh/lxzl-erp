package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.purchase.*;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseDeliveryOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrder;
import com.lxzl.erp.core.service.VerifyReceiver;


public interface PurchaseOrderService extends VerifyReceiver {

    ServiceResult<String, String> add(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> update(PurchaseOrder purchaseOrder);

    ServiceResult<String, PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo);

    ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);

    ServiceResult<String, String> commit(PurchaseOrderCommitParam purchaseOrderCommitParam);

    String delete(PurchaseOrder purchaseOrder);

    ServiceResult<String, Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam);

    ServiceResult<String, PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder);

    ServiceResult<String, String> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, String> confirmPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, Page<PurchaseReceiveOrder>> pagePurchaseReceive(PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam);

    ServiceResult<String, PurchaseReceiveOrder> queryPurchaseReceiveOrderByNo(PurchaseReceiveOrder purchaseReceiveOrder);

    ServiceResult<String, String> endPurchaseOrder(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> continuePurchaseOrder(PurchaseOrder purchaseOrder);

    ServiceResult<String, String> updatePurchaseReceiveOrderPrice(UpdatePurchaseReceiveOrderPriceParam updatePurchaseReceiveOrderPriceParam);

    ServiceResult<String, Page<ProductEquipment>> pageReceiveOrderProductEquipment(PurchaseReceiveOrderProductEquipmentPageParam purchaseReceiveOrderProductEquipmentPageParam);

    ServiceResult<String, Page<BulkMaterial>> pageReceiveOrderMaterialBulk(PurchaseReceiveOrderMaterialBulkPageParam purchaseReceiveOrderMaterialBulkPageParam);

    ServiceResult<String,String> updateReceiveRemark(UpdatePurchaseReceiveOrderRemarkParam updatePurchaseReceiveOrderRemarkParam);

    ServiceResult<String,String> commitPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);
}
