package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.returnOrder.RentMaterialCanProcessPageParam;
import com.lxzl.erp.common.domain.returnOrder.RentProductSkuPageParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.order.CustomerOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/customerOrder")
@Controller
@ControllerLog
public class CustomerOrderController {
    @Autowired
    private CustomerOrderService customerOrderService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 在租商品列表-可退换
     *
     * @param rentProductSkuPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageRentProduct", method = RequestMethod.POST)
    public Result pageRentProductSku(@RequestBody @Validated RentProductSkuPageParam rentProductSkuPageParam, BindingResult validResult) {
        ServiceResult<String, Page<Product>> serviceResult = customerOrderService.pageRentProductSku(rentProductSkuPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 在租物料列表--可退
     *
     * @param rentMaterialCanProcessPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageRentMaterialCanReturn", method = RequestMethod.POST)
    public Result pageRentMaterialCanReturn(@RequestBody @Validated RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam, BindingResult validResult) {
        ServiceResult<String, Page<Material>> serviceResult = customerOrderService.pageRentMaterialCanReturn(rentMaterialCanProcessPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 在租物料列表--可换
     *
     * @param rentMaterialCanProcessPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageRentMaterialCanChange", method = RequestMethod.POST)
    public Result pageRentMaterialCanChange(@RequestBody @Validated RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam, BindingResult validResult) {
        ServiceResult<String, Page<Material>> serviceResult = customerOrderService.pageRentMaterialCanChange(rentMaterialCanProcessPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
