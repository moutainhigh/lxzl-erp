package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 18:16
 */
@RequestMapping("/warehouse")
@Controller
@ControllerLog
public class WarehouseController extends BaseController {


    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Warehouse warehouse) {
        ServiceResult<String, String> serviceResult = warehouseService.addWarehouse(warehouse);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody Warehouse warehouse) {
        ServiceResult<String, String> serviceResult = warehouseService.updateWarehouse(warehouse);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getWarehousePage", method = RequestMethod.POST)
    public Result getWarehousePage(@RequestBody WarehouseQueryParam warehouseQueryParam) {
        ServiceResult<String, Page<Warehouse>> serviceResult = warehouseService.getWarehousePage(warehouseQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getWarehouseByCompany", method = RequestMethod.POST)
    public Result getWarehouseByCompany(@RequestBody WarehouseQueryParam warehouseQueryParam) {
        ServiceResult<String, List<Warehouse>> serviceResult = warehouseService.getWarehouseByCompany(warehouseQueryParam.getSubCompanyId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getWarehouseByCurrentCompany", method = RequestMethod.POST)
    public Result getWarehouseByCurrentCompany(@RequestBody WarehouseQueryParam warehouseQueryParam) {
        ServiceResult<String, List<Warehouse>> serviceResult = warehouseService.getWarehouseByCurrentCompany();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getWarehouseById", method = RequestMethod.POST)
    public Result getWarehouseById(@RequestBody WarehouseQueryParam warehouseQueryParam) {
        ServiceResult<String, Warehouse> serviceResult = warehouseService.getWarehouseById(warehouseQueryParam.getWarehouseId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "productInStock", method = RequestMethod.POST)
    public Result productInStock(@RequestBody ProductInStockParam productInStockParam) {
        ServiceResult<String, Integer> serviceResult = warehouseService.productInStock(productInStockParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "productOutStock", method = RequestMethod.POST)
    public Result productOutStock(@RequestBody ProductOutStockParam productOutStockParam) {
        ServiceResult<String, Integer> serviceResult = warehouseService.productOutStock(productOutStockParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
