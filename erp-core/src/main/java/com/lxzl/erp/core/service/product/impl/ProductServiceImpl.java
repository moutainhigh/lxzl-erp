package com.lxzl.erp.core.service.product.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/10/6.
 * Time: 14:48.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ServiceResult<String, List<ProductImg>> uploadImage(MultipartFile[] files) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, List<ProductImg>> result = new ServiceResult<>();
        List<ProductImg> imgList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String filePath = FileUtil.uploadFile(file);
                Map<String, String> extraInfo = new HashMap<>();
                InputStream inputStream = file.getInputStream();
                byte[] fileBytes = file.getBytes();
                long size = file.getSize();
                String fileId = fileService.uploadFile(filePath, fileBytes, size, CommonConstant.UPLOAD_USER, extraInfo, inputStream);
                ProductImgDO productImgDO = new ProductImgDO();
                productImgDO.setOriginalName(file.getOriginalFilename());
                productImgDO.setImgUrl(fileId);
                productImgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productImgDO.setCreateUser(loginUser.getUserId().toString());
                productImgDO.setUpdateUser(loginUser.getUserId().toString());
                productImgDO.setCreateTime(new Date());
                productImgDO.setUpdateTime(new Date());
                productImgMapper.save(productImgDO);

                imgList.add(ProductImageConverter.convertProductImgDO(productImgDO));
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            logger.error("upload product image file error", e);
            result.setErrorCode(ErrorCode.PRODUCT_IMAGE_UPLOAD_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(imgList);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> deleteImage(Integer imgId) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        ProductImgDO dbRecord = productImgMapper.findById(imgId);
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        ProductImgDO productImgDO = new ProductImgDO();
        productImgDO.setId(imgId);
        productImgDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        productImgDO.setUpdateUser(loginUser.getUserId().toString());
        productImgDO.setUpdateTime(new Date());
        Integer returnCode = productImgMapper.update(productImgDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(returnCode);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addProduct(Product product) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String verifyAddCode = verifyAddProduct(product);
        if (!ErrorCode.SUCCESS.equals(verifyAddCode)) {
            result.setErrorCode(verifyAddCode);
            return result;
        }
        ProductDO productDO = ProductConverter.convertProduct(product);
        productDO.setProductNo(GenerateNoUtil.generateProductNo(currentTime));
        productDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productDO.setCreateUser(loginUser.getUserId().toString());
        productDO.setUpdateUser(loginUser.getUserId().toString());
        productDO.setCreateTime(currentTime);
        productDO.setUpdateTime(currentTime);
        productMapper.save(productDO);
        Integer productId = productDO.getId();
        saveProductImage(product.getProductImgList(), 1, productId, loginUser, currentTime);
        saveProductImage(product.getProductDescImgList(), 2, productId, loginUser, currentTime);
        String errorCode = saveSkuAndProperties(product.getProductSkuList(), productId, loginUser, currentTime);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // SKU 未保存成功回滚
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }
        errorCode = saveProductProperties(product.getProductPropertyList(), productId, loginUser, currentTime);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 商品属性未保存成功回滚
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(productId);
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateProduct(Product product) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String verifyAddCode = verifyUpdateProduct(product);
        if (!ErrorCode.SUCCESS.equals(verifyAddCode)) {
            result.setErrorCode(verifyAddCode);
            return result;
        }
        ProductDO productDO = ProductConverter.convertProduct(product);
        productDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productDO.setUpdateUser(loginUser.getUserId().toString());
        productDO.setUpdateTime(currentTime);
        productMapper.update(productDO);
        Integer productId = productDO.getId();
        saveProductImage(product.getProductImgList(), 1, productId, loginUser, currentTime);
        saveProductImage(product.getProductDescImgList(), 2, productId, loginUser, currentTime);
        String errorCode = saveSkuAndProperties(product.getProductSkuList(), productId, loginUser, currentTime);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // SKU 未保存成功回滚
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }
        errorCode = saveProductProperties(product.getProductPropertyList(), productId, loginUser, currentTime);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 商品属性未保存成功回滚
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(product.getProductId());
        return result;
    }

    @Override
    public ServiceResult<String, Product> queryProductById(Integer productId) {
        ServiceResult<String, Product> result = new ServiceResult<>();
        if (productId == null) {
            result.setErrorCode(ErrorCode.PRODUCT_ID_NOT_NULL);
            return result;
        }

        ProductDO productDO = productMapper.findByProductId(productId);

        List<ProductSkuDO> productSkuDOList = productSkuMapper.findByProductId(productId);
        productDO.setProductSkuDOList(productSkuDOList);
        Product product = ProductConverter.convertProductDO(productDO);
        Map<String, Object> maps = new HashMap<>();
        maps.put("productId", productId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByProductId(maps);
        product.setProductCategoryPropertyList(ProductCategoryPropertyConverter.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(product);
        return result;
    }

    @Override
    public ServiceResult<String, Product> queryProductDetailById(Integer productId) {
        ServiceResult<String, Product> result = new ServiceResult<>();
        if (productId == null) {
            result.setErrorCode(ErrorCode.PRODUCT_ID_NOT_NULL);
            return result;
        }

        ProductDO productDO = productMapper.findByProductId(productId);

        List<ProductSkuDO> productSkuDOList = productSkuMapper.findDetailByProductId(productId);
        productDO.setProductSkuDOList(productSkuDOList);
        Product product = ProductConverter.convertProductDO(productDO);
        Map<String, Object> maps = new HashMap<>();
        maps.put("productId", productId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByProductId(maps);
        product.setProductCategoryPropertyList(ProductCategoryPropertyConverter.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(product);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Product>> queryAllProduct(ProductQueryParam productQueryParam) {
        ServiceResult<String, Page<Product>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(productQueryParam.getPageNo(), productQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("productQueryParam", productQueryParam);

        Integer totalCount = productMapper.findProductCountByParams(maps);
        List<ProductDO> productDOList = productMapper.findProductByParams(maps);
        List<Product> productList = ProductConverter.convertProductDOList(productDOList);
        Page<Product> page = new Page<>(productList, totalCount, productQueryParam.getPageNo(), productQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> productInStorage(ProductInStorage productInStorage) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        if (productInStorage == null || productInStorage.getProductId() == null || productInStorage.getProductSkuId() == null
                || productInStorage.getProductCount() <= 0) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        ProductDO productDO = productMapper.findByProductId(productInStorage.getProductId());
        ProductSkuDO productSkuDO = productSkuMapper.findById(productInStorage.getProductSkuId());
        if (productDO == null || productSkuDO == null || !productDO.getId().equals(productSkuDO.getProductId())) {
            result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
            return result;
        }

        productSkuDO.setStock(productSkuDO.getStock() + productInStorage.getProductCount());
        productSkuDO.setUpdateUser(loginUser.getUserId().toString());
        productSkuDO.setUpdateTime(currentTime);
        productSkuMapper.update(productSkuDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ProductEquipment>> queryAllProductEquipment(ProductEquipmentQueryParam productEquipmentQueryParam) {
        ServiceResult<String, Page<ProductEquipment>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(productEquipmentQueryParam.getPageNo(), productEquipmentQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("productEquipmentQueryParam", productEquipmentQueryParam);

        Integer totalCount = productEquipmentMapper.findProductEquipmentCountByParams(maps);
        List<ProductEquipmentDO> productEquipmentDOList = productEquipmentMapper.findProductEquipmentByParams(maps);
        List<ProductEquipment> productEquipmentList = ProductEquipmentConverter.convertProductEquipmentDOList(productEquipmentDOList);
        Page<ProductEquipment> page = new Page<>(productEquipmentList, totalCount, productEquipmentQueryParam.getPageNo(), productEquipmentQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, ProductEquipment> queryProductEquipmentDetail(String equipmentNo) {
        ServiceResult<String, ProductEquipment> result = new ServiceResult<>();
        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
        if (productEquipmentDO == null) {
            result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ProductEquipmentConverter.convertProductEquipmentDO(productEquipmentDO));
        return result;
    }

    @Override
    public ServiceResult<String, Page<ProductSku>> queryProductSkuList(ProductSkuQueryParam productSkuQueryParam) {
        ServiceResult<String, Page<ProductSku>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(productSkuQueryParam.getPageNo(), productSkuQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("productSkuQueryParam", productSkuQueryParam);

        Integer totalCount = productSkuMapper.findProductSkuCountByParams(maps);
        List<ProductSkuDO> productSkuDOList = productSkuMapper.findProductSkuByParams(maps);
        List<ProductSku> productSkuList = ProductConverter.convertProductSkuDOList(productSkuDOList);
        Page<ProductSku> page = new Page<>(productSkuList, totalCount, productSkuQueryParam.getPageNo(), productSkuQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private String verifyAddProduct(Product product) {
        if (StringUtil.isBlank(product.getProductName())) {
            return ErrorCode.PRODUCT_NAME_NOT_NULL;
        }
        if (product.getProductPropertyList() == null || product.getProductPropertyList().isEmpty()) {
            return ErrorCode.PRODUCT_PROPERTY_NOT_NULL;
        }
        for (ProductSkuProperty productSkuProperty : product.getProductPropertyList()) {
            if (productSkuProperty.getPropertyValueId() == null) {
                return ErrorCode.PRODUCT_PROPERTY_VALUE_NOT_NULL;
            }
        }

        if (product.getProductSkuList() == null || product.getProductSkuList().isEmpty()) {
            return ErrorCode.PRODUCT_SKU_NOT_NULL;
        }
        BigDecimal zero = new BigDecimal(0);
        for (ProductSku productSku : product.getProductSkuList()) {

            if (productSku.getSkuPrice() == null || -1 != (zero.compareTo(productSku.getSkuPrice()))) {
                return ErrorCode.PRODUCT_SKU_PRICE_ERROR;
            }

            if (productSku.getOriginalPrice() == null || -1 != (zero.compareTo(productSku.getOriginalPrice()))) {
                return ErrorCode.PRODUCT_SKU_PRICE_ERROR;
            }

            if (productSku.getRentPrice() == null || -1 != (zero.compareTo(productSku.getRentPrice()))) {
                return ErrorCode.PRODUCT_SKU_PRICE_ERROR;
            }

            if (productSku.getProductSkuPropertyList() == null || productSku.getProductSkuPropertyList().isEmpty()) {
                return ErrorCode.PRODUCT_SKU_PROPERTY_NOT_NULL;
            }
            for (ProductSkuProperty productSkuProperty : productSku.getProductSkuPropertyList()) {
                if (productSkuProperty.getPropertyValueId() == null) {
                    return ErrorCode.PRODUCT_SKU_PROPERTY_VALUE_NOT_NULL;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    private String verifyUpdateProduct(Product product) {
        if (product.getProductId() == null) {
            return ErrorCode.PRODUCT_ID_NOT_NULL;
        }

        ProductDO dbProductDO = productMapper.findByProductId(product.getProductId());
        if (dbProductDO == null) {
            return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
        }
        return verifyAddProduct(product);
    }

    void saveProductImage(List<ProductImg> productImgList, Integer type, Integer productId, User loginUser, Date currentTime) {

        List<ProductImg> updateProductImgList = new ArrayList<>();
        List<ProductImgDO> dbProductImgRecord = productImgMapper.findByProductId(productId, type);
        Map<Integer, ProductImgDO> dbProductImgRecordMap = ListUtil.listToMap(dbProductImgRecord, "id");

        if (productImgList != null) {
            for (ProductImg productImg : productImgList) {
                if (productImg != null && productImg.getImgId() != null) {
                    updateProductImgList.add(productImg);
                    if (dbProductImgRecordMap.get(productImg.getImgId()) != null) {
                        dbProductImgRecordMap.remove(productImg.getImgId());
                    }
                }
            }
        }


        if (!updateProductImgList.isEmpty()) {
            for (ProductImg productImg : updateProductImgList) {
                ProductImgDO productImgDO = ProductImageConverter.convertProductImg(productImg);
                productImgDO.setProductId(productId);
                productImgDO.setImgType(type);
                productImgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productImgDO.setUpdateUser(loginUser.getUserId().toString());
                productImgDO.setUpdateTime(currentTime);
                productImgMapper.update(productImgDO);
            }
        }
        if (!dbProductImgRecordMap.isEmpty()) {
            for (Map.Entry<Integer, ProductImgDO> entry : dbProductImgRecordMap.entrySet()) {
                ProductImgDO productImgDO = entry.getValue();
                productImgDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                productImgDO.setUpdateUser(loginUser.getUserId().toString());
                productImgDO.setUpdateTime(currentTime);
                productImgMapper.update(productImgDO);
            }
        }
    }

    String saveSkuAndProperties(List<ProductSku> productSkuList, Integer productId, User loginUser, Date currentTime) {
        // 根据商品查询SKU，然后与传入的SKU集合对比，交集修改，原多余部分删除，传入多余部分增加
        if (productSkuList == null || productSkuList.isEmpty()) {
            return ErrorCode.PRODUCT_SKU_NOT_NULL;
        }
        List<ProductSku> saveProductSkuList = new ArrayList<>();
        List<ProductSku> updateProductSkuList = new ArrayList<>();
        List<ProductSkuDO> dbSkuRecord = productSkuMapper.findByProductId(productId);
        Map<Integer, ProductSkuDO> dbSkuRecordMap = ListUtil.listToMap(dbSkuRecord, "id");
        for (ProductSku productSku : productSkuList) {
            if (productSku.getProductSkuPropertyList() == null || productSku.getProductSkuPropertyList().isEmpty()) {
                return ErrorCode.PRODUCT_SKU_PROPERTY_NOT_NULL;
            }
            if (productSku.getSkuId() != null) {
                updateProductSkuList.add(productSku);
                dbSkuRecordMap.remove(productSku.getSkuId());
            } else {
                saveProductSkuList.add(productSku);
            }
        }
        Integer skuId = null;

        if (!saveProductSkuList.isEmpty()) {
            for (ProductSku productSku : saveProductSkuList) {
                ProductSkuDO productSkuDO = ProductConverter.convertProductSku(productSku);
                productSkuDO.setProductId(productId);
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productSkuDO.setCreateUser(loginUser.getUserId().toString());
                productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                productSkuDO.setCreateTime(currentTime);
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.save(productSkuDO);
                skuId = productSkuDO.getId();
                saveSkuProperties(productSku.getProductSkuPropertyList(), productId, skuId, CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD, loginUser, currentTime);
            }
        }
        if (!updateProductSkuList.isEmpty()) {
            for (ProductSku productSku : updateProductSkuList) {
                List<ProductSkuPropertyDO> productSkuPropertyDOList = productSkuPropertyMapper.findSkuProperties(productSku.getSkuId());
                Map<Integer, ProductSkuPropertyDO> dbSkuPropertyMap = ListUtil.listToMap(productSkuPropertyDOList, "propertyValueId");
                List<ProductSkuProperty> productSkuPropertyList = productSku.getProductSkuPropertyList();
                List<ProductSkuProperty> addProductSkuPropertyList = new ArrayList<>();

                // 判断sku属性的增加与删除
                for (ProductSkuProperty productSkuProperty : productSkuPropertyList) {
                    if (dbSkuPropertyMap.get(productSkuProperty.getPropertyValueId()) != null) {
                        dbSkuPropertyMap.remove(productSkuProperty.getPropertyValueId());
                    } else {
                        addProductSkuPropertyList.add(productSkuProperty);
                    }
                }
                productSku.setProductSkuPropertyList(addProductSkuPropertyList);

                ProductSkuDO productSkuDO = ProductConverter.convertProductSku(productSku);
                productSkuDO.setProductId(productId);
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.update(productSkuDO);
                skuId = productSkuDO.getId();
                saveSkuProperties(productSku.getProductSkuPropertyList(), productId, skuId, CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD, loginUser, currentTime);
                List<ProductSkuPropertyDO> deleteProductSkuPropertyList = ListUtil.mapToList(dbSkuPropertyMap);
                saveSkuProperties(ProductConverter.convertProductSkuPropertyDOList(deleteProductSkuPropertyList), productId, skuId, CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE, loginUser, currentTime);
            }
        }
        if (!dbSkuRecordMap.isEmpty()) {
            for (Map.Entry<Integer, ProductSkuDO> entry : dbSkuRecordMap.entrySet()) {
                ProductSkuDO productSkuDO = entry.getValue();
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.update(productSkuDO);
                List<ProductSkuPropertyDO> dbPropertiesRecord = productSkuPropertyMapper.findSkuProperties(skuId);
                saveSkuProperties(ProductConverter.convertProductSkuPropertyDOList(dbPropertiesRecord), productId, productSkuDO.getId(), CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE, loginUser, currentTime);
            }
        }

        List<ProductSkuPropertyDO> productSkuPropertyDOList = productSkuPropertyMapper.findSkuProperties(skuId);
        StringBuilder skuName = new StringBuilder();
        for (int i = 0; i < productSkuPropertyDOList.size(); i++) {
            ProductSkuPropertyDO productSkuPropertyDO = productSkuPropertyDOList.get(i);
            skuName.append(productSkuPropertyDO.getPropertyName());
            skuName.append(":");
            skuName.append(productSkuPropertyDO.getPropertyValueName());
            if (i != (productSkuPropertyDOList.size() - 1)) {
                skuName.append("/");
            }
        }

        ProductSkuDO updateProductSkuName = new ProductSkuDO();
        updateProductSkuName.setId(skuId);
        updateProductSkuName.setSkuName(skuName.toString());
        productSkuMapper.update(updateProductSkuName);

        return ErrorCode.SUCCESS;
    }

    String saveProductProperties(List<ProductSkuProperty> productSkuPropertyList, Integer productId, User loginUser, Date currentTime) {

        if (productSkuPropertyList == null || productSkuPropertyList.isEmpty()) {
            return ErrorCode.PRODUCT_PROPERTY_NOT_NULL;
        }

        List<ProductSkuProperty> saveProductSkuPropertyList = new ArrayList<>();
        List<ProductSkuProperty> updateProductSkuPropertyList = new ArrayList<>();
        List<ProductSkuPropertyDO> dbProductSkuPropertyRecord = productSkuPropertyMapper.findProductProperties(productId);

        Map<Integer, ProductSkuPropertyDO> dbProductSkuPropertyRecordMap = ListUtil.listToMap(dbProductSkuPropertyRecord, "id");
        for (ProductSkuProperty productSkuProperty : productSkuPropertyList) {
            ProductSkuPropertyDO productSkuPropertyDO = productSkuPropertyMapper.findByProductIdAndPropertyValue(productId, productSkuProperty.getPropertyValueId());
            if (productSkuPropertyDO != null) {
                productSkuProperty.setSkuPropertyId(productSkuPropertyDO.getId());
                updateProductSkuPropertyList.add(productSkuProperty);
                dbProductSkuPropertyRecordMap.remove(productSkuPropertyDO.getId());
            } else {
                saveProductSkuPropertyList.add(productSkuProperty);
            }
        }
        for (ProductSkuProperty productSkuProperty : saveProductSkuPropertyList) {

            ProductSkuPropertyDO productSkuPropertyDO = ProductConverter.convertProductSkuProperty(productSkuProperty);
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productSkuPropertyDO.getPropertyValueId());
            if (productCategoryPropertyValueDO == null) {
                continue;
            }
            productSkuPropertyDO.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
            productSkuPropertyDO.setProductId(productId);
            productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_NO);
            productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productSkuPropertyDO.setCreateUser(loginUser.getUserId().toString());
            productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
            productSkuPropertyDO.setCreateTime(currentTime);
            productSkuPropertyDO.setUpdateTime(currentTime);
            productSkuPropertyMapper.save(productSkuPropertyDO);
        }
        for (ProductSkuProperty productSkuProperty : updateProductSkuPropertyList) {
            ProductSkuPropertyDO productSkuPropertyDO = ProductConverter.convertProductSkuProperty(productSkuProperty);
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productSkuPropertyDO.getPropertyValueId());
            if (productCategoryPropertyValueDO == null) {
                continue;
            }
            productSkuPropertyDO.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
            productSkuPropertyDO.setProductId(productId);
            productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_NO);
            productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
            productSkuPropertyDO.setUpdateTime(currentTime);
            productSkuPropertyMapper.update(productSkuPropertyDO);
        }

        for (Map.Entry<Integer, ProductSkuPropertyDO> entry : dbProductSkuPropertyRecordMap.entrySet()) {
            ProductSkuPropertyDO productSkuPropertyDO = entry.getValue();
            deleteProductSkuPropertyDO(productSkuPropertyDO, loginUser, currentTime);
        }
        return ErrorCode.SUCCESS;
    }

    void saveSkuProperties(List<ProductSkuProperty> productSkuPropertyList, Integer productId, Integer skuId, Integer operationType, User loginUser, Date currentTime) {
        if (productSkuPropertyList != null && !productSkuPropertyList.isEmpty()) {
            for (ProductSkuProperty productSkuProperty : productSkuPropertyList) {
                ProductSkuPropertyDO productSkuPropertyDO = ProductConverter.convertProductSkuProperty(productSkuProperty);
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productSkuPropertyDO.getPropertyValueId());
                if (productCategoryPropertyValueDO == null) {
                    continue;
                }
                productSkuPropertyDO.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
                productSkuPropertyDO.setProductId(productId);
                if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(operationType)) {
                    productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_YES);
                    productSkuPropertyDO.setSkuId(skuId);
                    productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    productSkuPropertyDO.setCreateUser(loginUser.getUserId().toString());
                    productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
                    productSkuPropertyDO.setCreateTime(currentTime);
                    productSkuPropertyDO.setUpdateTime(currentTime);
                    productSkuPropertyMapper.save(productSkuPropertyDO);
                    saveProductMaterial(productId, skuId, productSkuPropertyDO.getPropertyId(), productSkuPropertyDO.getPropertyValueId(), loginUser, currentTime);
                } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_UPDATE.equals(operationType)) {
                    productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_YES);
                    productSkuPropertyDO.setSkuId(skuId);
                    productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
                    productSkuPropertyDO.setUpdateTime(currentTime);
                    productSkuPropertyMapper.update(productSkuPropertyDO);
                } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(operationType)) {
                    deleteProductSkuPropertyDO(productSkuPropertyDO, loginUser, currentTime);
                }
            }
        }
    }

    void saveProductMaterial(Integer productId, Integer skuId, Integer propertyId, Integer propertyValueId, User loginUser, Date currentTime) {
        MaterialDO materialDO = materialMapper.findByPropertyAndValueId(propertyId, propertyValueId);
        if (materialDO != null) {
            ProductMaterialDO dbProductMaterialDO = productMaterialMapper.findBySkuAndMaterial(skuId, materialDO.getId());
            if (dbProductMaterialDO == null) {
                ProductMaterialDO productMaterialDO = new ProductMaterialDO();
                productMaterialDO.setProductId(productId);
                productMaterialDO.setProductSkuId(skuId);
                productMaterialDO.setMaterialId(materialDO.getId());
                productMaterialDO.setMaterialCount(1);
                productMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productMaterialDO.setCreateUser(loginUser.getUserId().toString());
                productMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                productMaterialDO.setCreateTime(currentTime);
                productMaterialDO.setUpdateTime(currentTime);
                productMaterialMapper.save(productMaterialDO);
            }
        }
    }

    void deleteProductSkuPropertyDO(ProductSkuPropertyDO productSkuPropertyDO, User loginUser, Date currentTime) {
        productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
        productSkuPropertyDO.setUpdateTime(currentTime);
        productSkuPropertyMapper.update(productSkuPropertyDO);
    }

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductSkuPropertyMapper productSkuPropertyMapper;

    @Autowired
    private ProductImgMapper productImgMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private ProductMaterialMapper productMaterialMapper;
}
