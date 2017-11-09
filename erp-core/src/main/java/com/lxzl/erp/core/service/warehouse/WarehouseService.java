package com.lxzl.erp.core.service.warehouse;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:43
 */
public interface WarehouseService extends BaseService {
    ServiceResult<String, Page<Warehouse>> getWarehousePage(WarehouseQueryParam param);

    ServiceResult<String, List<Warehouse>> getWarehouseByCompany(Integer subCompanyId);

    ServiceResult<String, List<Warehouse>> getWarehouseByCurrentCompany();

    ServiceResult<String, Warehouse> getWarehouseById(Integer warehouseId);

    /**
     * @description 商品入库，只支持采购首次入库
     * @param productInStockParam 商品入库基本信息
     * @return
     */
    ServiceResult<String, Integer> productInStock(ProductInStockParam productInStockParam);

    ServiceResult<String, Integer> productOutStock(List<ProductInStorage> productInStorageList);
}
