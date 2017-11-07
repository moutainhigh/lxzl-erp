package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/product")
@Controller
@ControllerLog
public class ProductController extends BaseController {


    @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
    public Result uploadImage(@RequestParam("file") MultipartFile[] file, HttpServletRequest request) {
        if (file.length == 0) {
            return new Result(ErrorCode.SYSTEM_ERROR, ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR), false);
        }
        ServiceResult<String, List<ProductImg>> serviceResult = productService.uploadImage(file);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteImage", method = RequestMethod.POST)
    public Result deleteImage(@RequestBody Image image, HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = productService.deleteImage(image.getImgId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Product product, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.addProduct(product);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated({UpdateGroup.class}) Product product, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.updateProduct(product);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryProductById", method = RequestMethod.POST)
    public Result queryProductById(@RequestBody Product product, BindingResult validResult) {
        ServiceResult<String, Product> serviceResult = productService.queryProductById(product.getProductId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProduct", method = RequestMethod.POST)
    public Result queryAllProduct(@RequestBody ProductQueryParam productQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Product>> serviceResult = productService.queryAllProduct(productQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "productInStorage", method = RequestMethod.POST)
    public Result productInStorage(@RequestBody ProductInStorage productInStorage, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.productInStorage(productInStorage);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProductEquipment", method = RequestMethod.POST)
    public Result queryAllProductEquipment(@RequestBody ProductEquipmentQueryParam productEquipmentQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<ProductEquipment>> serviceResult = productService.queryAllProductEquipment(productEquipmentQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryPropertiesByCategoryId", method = RequestMethod.POST)
    public Result queryPropertiesByCategoryId(@RequestBody ProductCategory productCategory, BindingResult validResult) {
        ServiceResult<String, List<ProductCategoryProperty>> serviceResult = productService.queryProductCategoryPropertyListByCategoryId(productCategory.getCategoryId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProductCategory", method = RequestMethod.POST)
    public Result queryAllProductCategory(@RequestBody ProductCategory productCategory, BindingResult validResult) {
        ServiceResult<String, List<ProductCategory>> serviceResult = productService.queryAllProductCategory();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "queryProductSkuList", method = RequestMethod.POST)
    public Result queryProductSkuList(@RequestBody ProductSkuQueryParam productSkuQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<ProductSku>> serviceResult = productService.queryProductSkuList(productSkuQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ProductService productService;

    @Autowired
    private ResultGenerator resultGenerator;
}
