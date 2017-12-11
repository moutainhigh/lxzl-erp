package com.lxzl.erp.core.service.customer.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.returnOrder.RentMaterialCanProcessPageParam;
import com.lxzl.erp.common.domain.returnOrder.RentProductSkuPageParam;

public interface CustomerOrderService {
    ServiceResult<String, Page<Product>> pageRentProductSku(RentProductSkuPageParam rentProductSkuPageParam);

    ServiceResult<String, Page<Material>> pageRentMaterialCanReturn(RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam);

    ServiceResult<String, Page<Material>> pageRentMaterialCanChange(RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam);
}
