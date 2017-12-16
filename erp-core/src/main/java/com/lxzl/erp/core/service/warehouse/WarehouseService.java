package com.lxzl.erp.core.service.warehouse;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.StockOrderQueryParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.StockOrder;
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
    /**
     * 添加仓库
     *
     * @param warehouse 仓库基本信息
     * @return 仓库编号
     */
    ServiceResult<String, String> addWarehouse(Warehouse warehouse);

    /**
     * 修改仓库
     *
     * @param warehouse 仓库基本信息
     * @return 仓库编号
     */
    ServiceResult<String, String> updateWarehouse(Warehouse warehouse);

    /**
     * 获取仓库列表信息
     *
     * @param param 查询仓库参数
     * @return 仓库列表
     */
    ServiceResult<String, Page<Warehouse>> getWarehousePage(WarehouseQueryParam param);

    /**
     * 获取分公司下的仓库
     *
     * @param subCompanyId 公司ID
     * @return 仓库列表
     */
    ServiceResult<String, List<Warehouse>> getWarehouseByCompany(Integer subCompanyId);

    /**
     * 获取当前分公司下的仓库
     *
     * @return 仓库列表
     */
    ServiceResult<String, List<Warehouse>> getWarehouseByCurrentCompany();

    /**
     * 根据ID获取仓库详情
     *
     * @param warehouseId 仓库ID
     * @return 仓库详情
     */
    ServiceResult<String, Warehouse> getWarehouseById(Integer warehouseId);

    /**
     * 商品入库
     *
     * @param productInStockParam 商品入库基本信息
     * @return
     * @description 商品入库，只支持采购首次入库
     */
    ServiceResult<String, Integer> productInStock(ProductInStockParam productInStockParam);

    /**
     * 商品出库
     *
     * @param productOutStockParam 商品出库参数
     * @return 出库单ID
     */
    ServiceResult<String, Integer> productOutStock(ProductOutStockParam productOutStockParam);


    /**
     * 获取出入库单列表
     * @param stockOrderQueryParam 查询出入库单信息
     * @return 出入库列表
     */
    ServiceResult<String, Page<StockOrder>> getStockOrderPage(StockOrderQueryParam stockOrderQueryParam);


    /**
     * 获取可用的仓库
     * @return 可用仓库列表
     */
    ServiceResult<String, List<Warehouse>> getAvailableWarehouse();
}
