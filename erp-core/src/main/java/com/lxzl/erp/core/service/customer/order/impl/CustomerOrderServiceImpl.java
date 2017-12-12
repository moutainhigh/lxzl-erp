package com.lxzl.erp.core.service.customer.order.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.RentMaterialCanProcessPageParam;
import com.lxzl.erp.common.domain.returnOrder.RentProductSkuPageParam;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.customer.order.CustomerOrderService;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {



    @Override
    public ServiceResult<String, Page<Product>> pageRentProductSku(RentProductSkuPageParam rentProductSkuPageParam) {
        ServiceResult<String, Page<Product>> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(rentProductSkuPageParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        Map<String, Object> map = customerOrderSupport.getCustomerMap(rentProductSkuPageParam, customerDO.getId());
        Integer totalCount = productSkuMapper.findSkuRentCount(map);
        List<ProductSkuDO> productSkuList = productSkuMapper.findSkuRent(map);
        Map<Integer, Product> productMap = new HashMap<>();
        for (ProductSkuDO productSkuDO : productSkuList) {
            Product product = productService.queryProductBySkuId(productSkuDO.getId()).getResult();
            ProductSku productSku = product.getProductSkuList().get(0);
            productSku.setReturnCount(productSkuDO.getRentCount());
            productSku.setCanProcessCount(productSkuDO.getCanProcessCount());
            Product p = productMap.get(product.getProductId());
            if (p == null) {
                productMap.put(product.getProductId(), product);
            } else {
                p.getProductSkuList().add(productSku);
            }
        }
        List<Product> productList = new ArrayList<>();
        for (Integer productId : productMap.keySet()) {
            productList.add(productMap.get(productId));
        }
        Page<Product> page = new Page<>(productList, totalCount, rentProductSkuPageParam.getPageNo(), rentProductSkuPageParam.getPageSize());
        serviceResult.setResult(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Material>> pageRentMaterialCanReturn(RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam) {
        ServiceResult<String, Page<Material>> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(rentMaterialCanProcessPageParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        Map<String, Object> map = customerOrderSupport.getCustomerCanReturnMap(rentMaterialCanProcessPageParam, customerDO.getId());
        Integer totalCount = materialMapper.findMaterialRentCount(map);
        List<MaterialDO> materialDOList = materialMapper.findMaterialRent(map);
        Page<Material> page = new Page<>(ConverterUtil.convertList(materialDOList, Material.class), totalCount, rentMaterialCanProcessPageParam.getPageNo(), rentMaterialCanProcessPageParam.getPageSize());
        serviceResult.setResult(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Material>> pageRentMaterialCanChange(RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam) {
        ServiceResult<String, Page<Material>> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(rentMaterialCanProcessPageParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        Map<String, Object> map = customerOrderSupport.getCustomerMap(rentMaterialCanProcessPageParam, customerDO.getId());
        Integer totalCount = materialMapper.findMaterialRentCount(map);
        List<MaterialDO> materialDOList = materialMapper.findMaterialRent(map);
        Page<Material> page = new Page<>(ConverterUtil.convertList(materialDOList, Material.class), totalCount, rentMaterialCanProcessPageParam.getPageNo(), rentMaterialCanProcessPageParam.getPageSize());
        serviceResult.setResult(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
}
