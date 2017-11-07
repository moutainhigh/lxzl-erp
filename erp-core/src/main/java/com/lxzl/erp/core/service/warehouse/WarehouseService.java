package com.lxzl.erp.core.service.warehouse;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
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

    ServiceResult<String, Integer> productInStock(List<ProductInStorage> productInStorageList, Integer srcWarehouseId, Integer targetWarehouseId, Integer causeType, String referNo);

    ServiceResult<String, Integer> productOutStock(List<ProductInStorage> productInStorageList);
}
