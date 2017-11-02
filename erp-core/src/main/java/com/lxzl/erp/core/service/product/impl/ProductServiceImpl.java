package com.lxzl.erp.core.service.product.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ProductEquipmentStatus;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.product.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    public ServiceResult<String, List<ProductCategory>> queryAllProductCategory() {
        ServiceResult<String, List<ProductCategory>> result = new ServiceResult<>();

        Map<String, Object> maps = new HashMap<>();
        List<ProductCategoryDO> productCategoryDOList = productCategoryMapper.findAllCategory(maps);
        List<ProductCategory> nodeList = ConvertProductCategory.convertProductCategoryTree(ConvertProductCategory.convertProductCategoryDOList(productCategoryDOList));
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(nodeList);
        return result;
    }

    @Override
    public ServiceResult<String, List<ProductCategoryProperty>> queryProductCategoryPropertyListByCategoryId(Integer categoryId) {
        ServiceResult<String, List<ProductCategoryProperty>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("categoryId", categoryId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByCategoryId(maps);
        result.setResult(ConvertProductCategoryProperty.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<ProductCategoryProperty>> queryPropertiesByProductId(Integer productId) {
        ServiceResult<String, List<ProductCategoryProperty>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("productId", productId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByProductId(maps);
        result.setResult(ConvertProductCategoryProperty.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

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
                if (loginUser != null) {
                    productImgDO.setCreateUser(loginUser.getUserId().toString());
                    productImgDO.setUpdateUser(loginUser.getUserId().toString());
                }

                productImgDO.setCreateTime(new Date());
                productImgDO.setUpdateTime(new Date());
                productImgMapper.save(productImgDO);

                imgList.add(ConvertProductImage.convertProductImgDO(productImgDO));
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
        if (loginUser != null) {
            productImgDO.setUpdateUser(loginUser.getUserId().toString());
        }
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
        ProductDO productDO = ConvertProduct.convertProduct(product);
        productDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        if (loginUser != null) {
            productDO.setCreateUser(loginUser.getUserId().toString());
            productDO.setUpdateUser(loginUser.getUserId().toString());
        }
        productDO.setCreateTime(currentTime);
        productDO.setUpdateTime(currentTime);
        productMapper.save(productDO);
        Integer productId = productDO.getId();
        saveProductImage(product.getProductImgList(), 1, productId, loginUser, currentTime);
        saveProductImage(product.getProductDescImgList(), 2, productId, loginUser, currentTime);
        saveSkuAndProperties(product.getProductSkuList(), productId, loginUser, currentTime);
        saveProductProperties(product.getProductPropertyList(), productId, loginUser, currentTime);

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
        ProductDO productDO = ConvertProduct.convertProduct(product);
        productDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        if (loginUser != null) {
            productDO.setUpdateUser(loginUser.getUserId().toString());
        }
        productDO.setUpdateTime(currentTime);
        productMapper.update(productDO);
        Integer productId = productDO.getId();
        saveProductImage(product.getProductImgList(), 1, productId, loginUser, currentTime);
        saveProductImage(product.getProductDescImgList(), 2, productId, loginUser, currentTime);
        saveSkuAndProperties(product.getProductSkuList(), productId, loginUser, currentTime);
        saveProductProperties(product.getProductPropertyList(), productId, loginUser, currentTime);

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
        Product product = ConvertProduct.convertProductDO(productDO);
        Map<String, Object> maps = new HashMap<>();
        maps.put("productId", productId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByProductId(maps);
        product.setProductCategoryPropertyList(ConvertProductCategoryProperty.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));

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
        List<Product> productList = ConvertProduct.convertProductDOList(productDOList);
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
        saveProductEquipment(productInStorage, loginUser, currentTime);
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
        List<ProductEquipment> productEquipmentList = ConvertProductEquipment.convertProductEquipmentDOList(productEquipmentDOList);
        Page<ProductEquipment> page = new Page<>(productEquipmentList, totalCount, productEquipmentQueryParam.getPageNo(), productEquipmentQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void saveProductEquipment(ProductInStorage productInStorage, User loginUser, Date currentTime) {
        ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
        param.setCreateStartTime(DateUtil.getBeginOfDay(currentTime));
        param.setCreateEndTime(DateUtil.getEndOfDay(currentTime));
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 1);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("productEquipmentQueryParam", param);
        Integer oldCount = productEquipmentMapper.findProductEquipmentCountByParams(maps);
        oldCount = oldCount == null ? 0 : oldCount;

        for (int i = 0; i < productInStorage.getProductCount(); i++) {
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            productEquipmentDO.setEquipmentNo(generateEquipmentNo(currentTime, productInStorage.getWarehouseId(), (oldCount + i + 1)));
            productEquipmentDO.setProductId(productInStorage.getProductId());
            productEquipmentDO.setSkuId(productInStorage.getProductSkuId());
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            productEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setCreateTime(currentTime);
            productEquipmentMapper.save(productEquipmentDO);
        }
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
        List<ProductSku> productSkuList = ConvertProduct.convertProductSkuDOList(productSkuDOList);
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
                return ErrorCode.PRODUCT_SKU_PRICE_EROR;
            }

            if (productSku.getOriginalPrice() == null || -1 != (zero.compareTo(productSku.getOriginalPrice()))) {
                return ErrorCode.PRODUCT_SKU_PRICE_EROR;
            }

            if (productSku.getSalePrice() == null || -1 != (zero.compareTo(productSku.getSalePrice()))) {
                return ErrorCode.PRODUCT_SKU_PRICE_EROR;
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
        return verifyAddProduct(product);
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
                ProductImgDO productImgDO = ConvertProductImage.convertProductImg(productImg);
                productImgDO.setProductId(productId);
                productImgDO.setImgType(type);
                productImgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                if (loginUser != null) {
                    productImgDO.setUpdateUser(loginUser.getUserId().toString());
                }
                productImgDO.setUpdateTime(currentTime);
                productImgMapper.update(productImgDO);
            }
        }
        if (!dbProductImgRecordMap.isEmpty()) {
            for (Map.Entry<Integer, ProductImgDO> entry : dbProductImgRecordMap.entrySet()) {
                ProductImgDO productImgDO = entry.getValue();
                productImgDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                if (loginUser != null) {
                    productImgDO.setUpdateUser(loginUser.getUserId().toString());
                }
                productImgDO.setUpdateTime(currentTime);
                productImgMapper.update(productImgDO);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void saveSkuAndProperties(List<ProductSku> productSkuList, Integer productId, User loginUser, Date currentTime) {
        // 根据商品查询SKU，然后与传入的SKU集合对比，交集修改，原多余部分删除，传入多余部分增加
        if (productSkuList == null || productSkuList.isEmpty()) {
            return;
        }

        List<ProductSku> saveProductSkuList = new ArrayList<>();
        List<ProductSku> updateProductSkuList = new ArrayList<>();
        List<ProductSkuDO> dbSkuRecord = productSkuMapper.findByProductId(productId);
        Map<Integer, ProductSkuDO> dbSkuRecordMap = ListUtil.listToMap(dbSkuRecord, "id");
        for (ProductSku productSku : productSkuList) {
            List<Integer> propertyValueIdList = new ArrayList<>();
            if (productSku.getProductSkuPropertyList() == null || productSku.getProductSkuPropertyList().isEmpty()) {
                continue;
            }
            for (ProductSkuProperty productSkuProperty : productSku.getProductSkuPropertyList()) {
                propertyValueIdList.add(productSkuProperty.getPropertyValueId());
            }

            Map<String, Object> maps = new HashMap<>();
            maps.put("productId", productId);
            maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
            maps.put("propertyValueIdList", propertyValueIdList);
            maps.put("propertyValueIdCount", propertyValueIdList.size());
            Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
            if (skuId != null) {
                productSku.setSkuId(skuId);
                updateProductSkuList.add(productSku);
                dbSkuRecordMap.remove(skuId);
            } else {
                saveProductSkuList.add(productSku);
            }
        }


        if (!saveProductSkuList.isEmpty()) {
            for (ProductSku productSku : saveProductSkuList) {
                ProductSkuDO productSkuDO = ConvertProduct.convertProductSku(productSku);
                productSkuDO.setProductId(productId);
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                if (loginUser != null) {
                    productSkuDO.setCreateUser(loginUser.getUserId().toString());
                    productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                }
                productSkuDO.setCreateTime(currentTime);
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.save(productSkuDO);
                Integer skuId = productSkuDO.getId();
                saveSkuProperties(productSku.getProductSkuPropertyList(), productId, skuId, CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD, loginUser, currentTime);
            }
        }
        if (!updateProductSkuList.isEmpty()) {
            for (ProductSku productSku : updateProductSkuList) {
                ProductSkuDO productSkuDO = ConvertProduct.convertProductSku(productSku);
                productSkuDO.setProductId(productId);
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                if (loginUser != null) {
                    productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                }
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.update(productSkuDO);
                Integer skuId = productSkuDO.getId();
                saveSkuProperties(productSku.getProductSkuPropertyList(), productId, skuId, CommonConstant.COMMON_DATA_OPERATION_TYPE_UPDATE, loginUser, currentTime);
            }
        }
        if (!dbSkuRecordMap.isEmpty()) {
            for (Map.Entry<Integer, ProductSkuDO> entry : dbSkuRecordMap.entrySet()) {
                ProductSkuDO productSkuDO = entry.getValue();
                productSkuDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                if (loginUser != null) {
                    productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                }
                productSkuDO.setUpdateTime(currentTime);
                productSkuMapper.update(productSkuDO);
                saveSkuProperties(null, productId, productSkuDO.getId(), CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE, loginUser, currentTime);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void saveProductProperties(List<ProductSkuProperty> productSkuPropertyList, Integer productId, User loginUser, Date currentTime) {

        if (productSkuPropertyList == null || productSkuPropertyList.isEmpty()) {
            return;
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

            ProductSkuPropertyDO productSkuPropertyDO = ConvertProduct.convertProductSkuProperty(productSkuProperty);
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productSkuPropertyDO.getPropertyValueId());
            if (productCategoryPropertyValueDO == null) {
                continue;
            }
            productSkuPropertyDO.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
            productSkuPropertyDO.setProductId(productId);
            productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_NO);
            productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            if (loginUser != null) {
                productSkuPropertyDO.setCreateUser(loginUser.getUserId().toString());
                productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
            }
            productSkuPropertyDO.setCreateTime(currentTime);
            productSkuPropertyDO.setUpdateTime(currentTime);
            productSkuPropertyMapper.save(productSkuPropertyDO);
        }
        for (ProductSkuProperty productSkuProperty : updateProductSkuPropertyList) {
            ProductSkuPropertyDO productSkuPropertyDO = ConvertProduct.convertProductSkuProperty(productSkuProperty);
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productSkuPropertyDO.getPropertyValueId());
            if (productCategoryPropertyValueDO == null) {
                continue;
            }
            productSkuPropertyDO.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
            productSkuPropertyDO.setProductId(productId);
            productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_NO);
            productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            if (loginUser != null) {
                productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
            }
            productSkuPropertyDO.setUpdateTime(currentTime);
            productSkuPropertyMapper.update(productSkuPropertyDO);
        }

        for (Map.Entry<Integer, ProductSkuPropertyDO> entry : dbProductSkuPropertyRecordMap.entrySet()) {
            ProductSkuPropertyDO productSkuPropertyDO = entry.getValue();
            deleteProductSkuPropertyDO(productSkuPropertyDO, loginUser, currentTime);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void saveSkuProperties(List<ProductSkuProperty> productSkuPropertyList, Integer productId, Integer skuId, Integer operationType, User loginUser, Date currentTime) {
        if (productSkuPropertyList != null && !productSkuPropertyList.isEmpty()) {
            for (ProductSkuProperty productSkuProperty : productSkuPropertyList) {
                ProductSkuPropertyDO productSkuPropertyDO = ConvertProduct.convertProductSkuProperty(productSkuProperty);
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
                    if (loginUser != null) {
                        productSkuPropertyDO.setCreateUser(loginUser.getUserId().toString());
                        productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
                    }
                    productSkuPropertyDO.setCreateTime(currentTime);
                    productSkuPropertyDO.setUpdateTime(currentTime);
                    productSkuPropertyMapper.save(productSkuPropertyDO);
                } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(operationType)) {
                    productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_YES);
                    productSkuPropertyDO.setSkuId(skuId);
                    productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    if (loginUser != null) {
                        productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
                    }
                    productSkuPropertyDO.setUpdateTime(currentTime);
                    productSkuPropertyMapper.update(productSkuPropertyDO);
                }
            }
        } else {
            List<ProductSkuPropertyDO> dbPropertiesRecord = productSkuPropertyMapper.findSkuProperties(skuId);
            // 删除SKU下的所有属性
            for (ProductSkuPropertyDO productSkuPropertyDO : dbPropertiesRecord) {
                deleteProductSkuPropertyDO(productSkuPropertyDO, loginUser, currentTime);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void deleteProductSkuPropertyDO(ProductSkuPropertyDO productSkuPropertyDO, User loginUser, Date currentTime) {
        productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        if (loginUser != null) {
            productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
        }
        productSkuPropertyDO.setUpdateTime(currentTime);
        productSkuPropertyMapper.update(productSkuPropertyDO);
    }

    private String generateEquipmentNo(Date currentTime, Integer warehouseId, int no) {
        return "LX-52RENTAL-VIEWPAKER-" + warehouseId + "-" + new SimpleDateFormat("yyyyMMdd").format(currentTime) + (10000 + no);
    }

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

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
}
