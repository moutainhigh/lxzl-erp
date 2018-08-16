package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.product.ProductCategoryService;
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
    public Result add(@RequestBody @Validated(AddGroup.class) Product product, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.addProduct(product);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated({UpdateGroup.class}) Product product, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.updateProduct(product);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addProductMaterial", method = RequestMethod.POST)
    public Result addProductMaterial(@RequestBody ProductSku productSku, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.addProductMaterial(productSku);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateProductMaterial", method = RequestMethod.POST)
    public Result updateProductMaterial(@RequestBody ProductSku productSku, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.updateProductMaterial(productSku);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "removeProductMaterial", method = RequestMethod.POST)
    public Result removeProductMaterial(@RequestBody ProductSku productSku, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productService.removeProductMaterial(productSku);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryProductById", method = RequestMethod.POST)
    public Result queryProductById(@RequestBody Product product, BindingResult validResult) {
        ServiceResult<String, Product> serviceResult = productService.queryProductById(product.getProductId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryProductByNo", method = RequestMethod.POST)
    public Result queryProductByNo(@RequestBody Product product, BindingResult validResult) {
        ServiceResult<String, Product> serviceResult = productService.queryProductByNo(product.getProductNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProduct", method = RequestMethod.POST)
    public Result queryAllProduct(@RequestBody ProductQueryParam productQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Product>> serviceResult = productService.queryAllProduct(productQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProductEquipment", method = RequestMethod.POST)
    public Result queryAllProductEquipment(@RequestBody ProductEquipmentQueryParam productEquipmentQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<ProductEquipment>> serviceResult = productService.queryAllProductEquipment(productEquipmentQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryProductEquipmentDetail", method = RequestMethod.POST)
    public Result queryProductEquipmentDetail(@RequestBody ProductEquipmentQueryParam productEquipmentQueryParam, BindingResult validResult) {
        ServiceResult<String, ProductEquipment> serviceResult = productService.queryProductEquipmentDetail(productEquipmentQueryParam.getEquipmentNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryPropertiesByCategoryId", method = RequestMethod.POST)
    public Result queryPropertiesByCategoryId(@RequestBody ProductCategory productCategory, BindingResult validResult) {
        ServiceResult<String, List<ProductCategoryProperty>> serviceResult = productCategoryService.queryProductCategoryPropertyListByCategoryId(productCategory.getCategoryId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllProductCategory", method = RequestMethod.POST)
    public Result queryAllProductCategory(@RequestBody ProductCategoryQueryParam productCategoryQueryParam, BindingResult validResult) {
        ServiceResult<String, List<ProductCategory>> serviceResult = productCategoryService.queryAllProductCategory(productCategoryQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryProductSkuList", method = RequestMethod.POST)
    public Result queryProductSkuList(@RequestBody ProductSkuQueryParam productSkuQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<ProductSku>> serviceResult = productService.queryProductSkuList(productSkuQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @RequestMapping(value = "pageProductCategory", method = RequestMethod.POST)
    public Result pageProductCategory(@RequestBody ProductCategoryPageParam productCategoryPageParam, BindingResult validResult) {
        ServiceResult<String, Page<ProductCategory>> serviceResult = productCategoryService.pageProductCategory(productCategoryPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailProductCategory", method = RequestMethod.POST)
    public Result detailProductCategory(@RequestBody @Validated(IdGroup.class) ProductCategory productCategory, BindingResult validResult) {
        ServiceResult<String, ProductCategory> serviceResult = productCategoryService.detailProductCategory(productCategory);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addProductCategoryPropertyValue", method = RequestMethod.POST)
    public Result addProductCategoryPropertyValue(@RequestBody ProductCategoryPropertyValue productCategoryPropertyValue, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productCategoryService.addProductCategoryPropertyValue(productCategoryPropertyValue);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

//    @RequestMapping(value = "addProductCategoryProperty", method = RequestMethod.POST)
//    public Result addProductCategoryProperty(@RequestBody @Validated(AddGroup.class) ProductCategoryProperty productCategoryProperty, BindingResult validResult) {
//        ServiceResult<String, Integer> serviceResult = productCategoryService.addProductCategoryProperty(productCategoryProperty);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }

//    @RequestMapping(value = "deleteProductCategoryPropertyValue", method = RequestMethod.POST)
//    public Result deleteProductCategoryPropertyValue(@RequestBody @Validated(CancelGroup.class) ProductCategoryProperty productCategoryProperty, BindingResult validResult) {
//        ServiceResult<String, Integer> serviceResult = productCategoryService.deleteProductCategoryPropertyValue(productCategoryProperty);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }

    @RequestMapping(value = "updateCategoryPropertyValue", method = RequestMethod.POST)
    public Result updateCategoryPropertyValue(@RequestBody @Validated(UpdateGroup.class) ProductCategoryPropertyValue productCategoryPropertyValue, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = productCategoryService.updateCategoryPropertyValue(productCategoryPropertyValue);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageProductCategoryProperty", method = RequestMethod.POST)
    public Result pageProductCategoryProperty(@RequestBody ProductCategoryPropertyPageParam productCategoryPropertyPageParam) {
        ServiceResult<String, Page<ProductCategoryProperty>> serviceResult = productCategoryService.pageProductCategoryProperty(productCategoryPropertyPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailProductCategoryProperty", method = RequestMethod.POST)
    public Result detailProductCategoryProperty(@RequestBody @Validated(IdGroup.class)ProductCategoryProperty productCategoryProperty) {
        ServiceResult<String, ProductCategoryProperty> serviceResult = productCategoryService.detailProductCategoryProperty(productCategoryProperty.getCategoryPropertyId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageProductCategoryPropertyValue", method = RequestMethod.POST)
    public Result pageProductCategoryPropertyValue(@RequestBody ProductCategoryPropertyValuePageParam productCategoryPropertyValuePageParam) {
        ServiceResult<String, Page<ProductCategoryPropertyValue>> serviceResult = productCategoryService.pageProductCategoryPropertyValue(productCategoryPropertyValuePageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailProductCategoryPropertyValue", method = RequestMethod.POST)
    public Result detailProductCategoryPropertyValue(@RequestBody @Validated(IdGroup.class) ProductCategoryPropertyValue productCategoryPropertyValue, BindingResult validResult) {
        ServiceResult<String, ProductCategoryPropertyValue> serviceResult = productCategoryService.detailProductCategoryPropertyValue(productCategoryPropertyValue.getCategoryPropertyValueId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }



    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ResultGenerator resultGenerator;
}
