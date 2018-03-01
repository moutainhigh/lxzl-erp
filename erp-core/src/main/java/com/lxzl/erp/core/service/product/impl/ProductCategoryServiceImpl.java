package com.lxzl.erp.core.service.product.impl;

import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PermissionType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.product.ProductCategoryPageParam;
import com.lxzl.erp.common.domain.product.ProductCategoryQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductCategory;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryProperty;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.product.ProductCategoryService;
import com.lxzl.erp.core.service.product.impl.support.ProductCategoryConverter;
import com.lxzl.erp.core.service.product.impl.support.ProductCategoryPropertyConverter;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyValueMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.hadoop.mapred.IFile;
import org.hibernate.validator.internal.xml.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-09 19:06
 */
@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private static Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    @Override
    public ServiceResult<String, List<ProductCategory>> queryAllProductCategory(ProductCategoryQueryParam productCategoryQueryParam) {
        ServiceResult<String, List<ProductCategory>> result = new ServiceResult<>();

        Map<String, Object> maps = new HashMap<>();
        maps.put("productCategoryQueryParam", productCategoryQueryParam);
        List<ProductCategoryDO> productCategoryDOList = productCategoryMapper.findAllCategory(maps);
        List<ProductCategory> nodeList = ProductCategoryConverter.convertProductCategoryTree(ProductCategoryConverter.convertProductCategoryDOList(productCategoryDOList));
        // 过滤没有下级且本身不是
        filterSurplusProductCategoryNode(nodeList);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(nodeList);
        return result;
    }

    private void filterSurplusProductCategoryNode(List<ProductCategory> nodeList) {
        if (CollectionUtil.isEmpty(nodeList)) {
            return;
        }
        for (ProductCategory productCategory : nodeList) {
            if (CategoryType.CATEGORY_TYPE_FOLDER.equals(productCategory.getCategoryType()) && CollectionUtil.isEmpty(productCategory.getChildren())) {
                nodeList.remove(productCategory);
            } else if (CollectionUtil.isNotEmpty(productCategory.getChildren())) {
                filterSurplusProductCategoryNode(productCategory.getChildren());
            }
        }
    }

    @Override
    public ServiceResult<String, List<ProductCategoryProperty>> queryProductCategoryPropertyListByCategoryId(Integer categoryId) {
        ServiceResult<String, List<ProductCategoryProperty>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("categoryId", categoryId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByCategoryId(maps);
        result.setResult(ConverterUtil.convertList(productCategoryPropertyDOList, ProductCategoryProperty.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<ProductCategoryProperty>> queryPropertiesByProductId(Integer productId) {
        ServiceResult<String, List<ProductCategoryProperty>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("productId", productId);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findProductCategoryPropertyListByProductId(maps);
        result.setResult(ProductCategoryPropertyConverter.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> addProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = ProductCategoryPropertyConverter.convertProductCategoryPropertyValue(productCategoryPropertyValue);
        ProductCategoryPropertyDO productCategoryPropertyDO = productCategoryPropertyMapper.findById(productCategoryPropertyValueDO.getPropertyId());
        if (productCategoryPropertyDO == null) {
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS);
            return result;
        }

        if (productCategoryPropertyDO.getMaterialType() != null) {
            MaterialTypeDO materialTypeDO = materialTypeMapper.findById(productCategoryPropertyDO.getMaterialType());
            if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                    && productCategoryPropertyValueDO.getPropertyCapacityValue() == null) {
                result.setErrorCode(ErrorCode.MATERIAL_CAPACITY_VALUE_NOT_NULL);
                return result;
            } else if (!CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                    && productCategoryPropertyValueDO.getMaterialModelId() == null) {
                result.setErrorCode(ErrorCode.MATERIAL_MODEL_NOT_NULL);
                return result;
            }

            if (productCategoryPropertyValueDO.getMaterialModelId() != null) {
                MaterialModelDO materialModelDO = materialModelMapper.findById(productCategoryPropertyValueDO.getMaterialModelId());
                if (materialModelDO == null
                        || !materialModelDO.getMaterialType().equals(productCategoryPropertyDO.getMaterialType())) {
                    result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                    return result;
                }
            }
        }

        productCategoryPropertyValueDO.setCategoryId(productCategoryPropertyDO.getCategoryId());
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productCategoryPropertyValueDO.setCreateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setUpdateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setCreateTime(currentTime);
        productCategoryPropertyValueDO.setUpdateTime(currentTime);
        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
        // 自动添加物料，处于下架状态
        // saveMaterial(productCategoryPropertyDO, productCategoryPropertyValueDO, loginUser, currentTime);

        result.setResult(productCategoryPropertyValueDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void saveMaterial(ProductCategoryPropertyDO productCategoryPropertyDO, ProductCategoryPropertyValueDO productCategoryPropertyValueDO, User loginUser, Date currentTime) {
        if (productCategoryPropertyDO.getMaterialType() == null) {
            return;
        }
        MaterialDO dbMaterialDO = null;
        MaterialTypeDO materialTypeDO = materialTypeMapper.findById(productCategoryPropertyDO.getMaterialType());
        if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())) {
            dbMaterialDO = materialMapper.findByMaterialTypeAndCapacity(productCategoryPropertyDO.getMaterialType(), productCategoryPropertyValueDO.getPropertyCapacityValue());
        } else {
            dbMaterialDO = materialMapper.findByMaterialTypeAndModelId(productCategoryPropertyDO.getMaterialType(), productCategoryPropertyValueDO.getMaterialModelId());
        }
        if (dbMaterialDO != null) {
            return;
        }

        MaterialDO materialDO = new MaterialDO();
        materialDO.setMaterialType(productCategoryPropertyDO.getMaterialType());
        materialDO.setMaterialModelId(productCategoryPropertyValueDO.getMaterialModelId());
        materialDO.setMaterialCapacityValue(productCategoryPropertyValueDO.getPropertyCapacityValue());
        if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
            materialDO.setIsMainMaterial(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            materialDO.setIsMainMaterial(CommonConstant.COMMON_CONSTANT_NO);
        }
        materialDO.setMaterialName(productCategoryPropertyDO.getPropertyName() + "/" + productCategoryPropertyValueDO.getPropertyValueName());
        materialDO.setMaterialNo(GenerateNoUtil.generateMaterialNo(currentTime));
        materialDO.setIsRent(CommonConstant.COMMON_CONSTANT_NO);
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialDO.setCreateUser(loginUser.getUserId().toString());
        materialDO.setUpdateTime(currentTime);
        materialDO.setCreateTime(currentTime);
        materialMapper.save(materialDO);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        ProductCategoryPropertyValueDO dbProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productCategoryPropertyValue.getCategoryPropertyValueId());
        if (dbProductCategoryPropertyValueDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = ProductCategoryPropertyConverter.convertProductCategoryPropertyValue(productCategoryPropertyValue);
        ProductCategoryPropertyDO productCategoryPropertyDO = productCategoryPropertyMapper.findById(productCategoryPropertyValueDO.getPropertyId());
        if (productCategoryPropertyDO == null) {
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS);
            return result;
        }
        productCategoryPropertyValueDO.setCategoryId(productCategoryPropertyDO.getCategoryId());
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productCategoryPropertyValueDO.setCreateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setUpdateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setCreateTime(currentTime);
        productCategoryPropertyValueDO.setUpdateTime(currentTime);
        productCategoryPropertyValueMapper.update(productCategoryPropertyValueDO);

        result.setResult(productCategoryPropertyValueDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> addProductCategoryProperty(ProductCategoryProperty productCategoryProperty) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUser = userSupport.getCurrentUser().getUserId().toString();
        Date now = new Date();

        //首先判断传入的商品分类属性
        if (productCategoryProperty.getMaterialType() != null){
            MaterialTypeDO materialTypeDO = materialTypeMapper.findById(productCategoryProperty.getMaterialType());
            if (materialTypeDO == null){
                result.setErrorCode(ErrorCode.MATERIAL_TYPE_NOT_NULL);
                return result;
            }
            if(!materialTypeDO.getMaterialTypeName().equals(productCategoryProperty.getPropertyName())){
                result.setErrorCode(ErrorCode.PROPERTY_NAME_NOT_MATCH_MATERIAL_TYPE_NAME);
                return result;
            }
        }else{
            //在商品分类的配件类型为空时，防止随意填写的商品属性名称
            MaterialTypeDO materialTypeDO = materialTypeMapper.findByMaterialTypeName(productCategoryProperty.getPropertyName());
            if (materialTypeDO != null){
                result.setErrorCode(ErrorCode.PROPERTY_NAME_NOT_EXISTS);
                return result;
            }
        }

        //验证商品分类的类型
        if (productCategoryProperty.getPropertyType() != 1 && productCategoryProperty.getPropertyType() != 2){
            result.setErrorCode(ErrorCode.PROPERTY_TYPE_MUST_BE_ONE_OR_TWO);
            return result;
        }

        //判断商品分类是否存在
        if (productCategoryMapper.findById(productCategoryProperty.getCategoryId()) == null){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_NOT_EXISTS);
            return result;
        }

        ProductCategoryPropertyDO productCategoryPropertyDO = productCategoryPropertyMapper.findByProductNameAndCategoryId(productCategoryProperty.getPropertyName(), productCategoryProperty.getCategoryId());
        //添加的是新的商品分类属性
        if (productCategoryPropertyDO == null){
            productCategoryPropertyDO = ConverterUtil.convert(productCategoryProperty,ProductCategoryPropertyDO.class);
            productCategoryPropertyDO.setIsInput(CommonConstant.COMMON_CONSTANT_NO);
            productCategoryPropertyDO.setIsCheckbox(CommonConstant.COMMON_CONSTANT_NO);
            productCategoryPropertyDO.setIsRequired(CommonConstant.COMMON_CONSTANT_NO);
            productCategoryPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productCategoryPropertyDO.setCreateTime(now);
            productCategoryPropertyDO.setCreateUser(currentUser);
            productCategoryPropertyDO.setUpdateTime(now);
            productCategoryPropertyDO.setUpdateUser(currentUser);
            productCategoryPropertyMapper.save(productCategoryPropertyDO);

            productCategoryPropertyDO = productCategoryPropertyMapper.findByProductNameAndCategoryId(productCategoryPropertyDO.getPropertyName(), productCategoryPropertyDO.getCategoryId());
        }

        //处理商品分类属性值
        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = productCategoryProperty.getProductCategoryPropertyValueList();
        for (ProductCategoryPropertyValue productCategoryPropertyValue : productCategoryPropertyValueList){
            //判断传入的商品分类属性值的categoryId是否与上一级相同
            if (!productCategoryPropertyValue.getCategoryId().equals(productCategoryPropertyDO.getCategoryId())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_VALUE_CATEGORY_ID_IS_ERROR);
                return result;
            }

            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(productCategoryPropertyValue.getPropertyValueName(), productCategoryPropertyValue.getCategoryId());
            //添加的是新的商品分类属性值,如果新建的数据有或者重复，则不进行操作
            if (productCategoryPropertyValueDO == null){
                productCategoryPropertyValueDO = ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class);
                //该商品分类有物料类型时，先判断属性容量值和物料型号的值
                if (productCategoryPropertyDO.getMaterialType() != null){
                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(productCategoryPropertyDO.getMaterialType());
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                            && productCategoryPropertyValueDO.getPropertyCapacityValue() == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        result.setErrorCode(ErrorCode.MATERIAL_CAPACITY_VALUE_NOT_NULL);
                        return result;
                    } else if (!CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                            && productCategoryPropertyValueDO.getMaterialModelId() == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        result.setErrorCode(ErrorCode.MATERIAL_MODEL_NOT_NULL);
                        return result;
                    }

                    //是否为带字面量配件来区分硬盘、内存之类
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())){
                        productCategoryPropertyValueDO.setMaterialModelId(null);
                    }else if(!CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())){
                        productCategoryPropertyValueDO.setPropertyCapacityValue(null);
                    }
                }else{
                        //该商品分类没有物料类型的属性
                        productCategoryPropertyValueDO.setPropertyCapacityValue(null);
                        productCategoryPropertyValueDO.setMaterialModelId(null);
                    }
                if (productCategoryPropertyValueDO.getMaterialModelId() != null) {
                    MaterialModelDO materialModelDO = materialModelMapper.findById(productCategoryPropertyValueDO.getMaterialModelId());
                    if (materialModelDO == null
                            || !materialModelDO.getMaterialType().equals(productCategoryPropertyDO.getMaterialType())) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                        return result;
                    }
                }

                productCategoryPropertyValueDO.setPropertyId(productCategoryPropertyDO.getId());
                productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productCategoryPropertyValueDO.setCreateTime(now);
                productCategoryPropertyValueDO.setCreateUser(currentUser);
                productCategoryPropertyValueDO.setUpdateTime(now);
                productCategoryPropertyValueDO.setUpdateUser(currentUser);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(productCategoryPropertyDO.getId());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> deleteProductCategoryPropertyValue(ProductCategoryProperty productCategoryProperty) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUser = userSupport.getCurrentUser().getUserId().toString();
        Date now = new Date();

        ProductCategoryPropertyDO productCategoryPropertyDO = productCategoryPropertyMapper.findById(productCategoryProperty.getCategoryPropertyId());
        if (productCategoryPropertyDO == null){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS);
            return result;
        }

        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = productCategoryProperty.getProductCategoryPropertyValueList();
        for (ProductCategoryPropertyValue productCategoryPropertyValue : productCategoryPropertyValueList){
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productCategoryPropertyValue.getCategoryPropertyValueId());
            if (productCategoryPropertyValueDO == null){
                result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_VALUE_NOT_EXISTS);
                return result;
            }

            productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            productCategoryPropertyValueDO.setUpdateTime(now);
            productCategoryPropertyValueDO.setUpdateUser(currentUser);
            productCategoryPropertyValueMapper.update(productCategoryPropertyValueDO);
        }

        //如果该商品分类属性所有值都没有了，就删除该商品分类属性
        if (productCategoryPropertyDO.getProductCategoryPropertyValueDOList().size() == 0){
            productCategoryPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            productCategoryPropertyDO.setUpdateTime(now);
            productCategoryPropertyDO.setUpdateUser(currentUser);
            productCategoryPropertyMapper.update(productCategoryPropertyDO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(productCategoryPropertyDO.getId());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> updateCategoryPropertyValue(ProductCategoryProperty productCategoryProperty) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUser = userSupport.getCurrentUser().getUserId().toString();
        Date now = new Date();

        ProductCategoryPropertyDO productCategoryPropertyDO = productCategoryPropertyMapper.findById(productCategoryProperty.getCategoryPropertyId());
        if (productCategoryPropertyDO == null){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS);
            return result;
        }

        List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList = productCategoryPropertyValueMapper.findListByPropertyIdAndCategoryId(productCategoryPropertyDO.getId(),productCategoryPropertyDO.getCategoryId());
        Set<Integer> productCategoryPropertyValueIdSet = new HashSet<>();
        Set<String> productCategoryPropertyValueNameSet = new HashSet<>();

        for (ProductCategoryPropertyValueDO productCategoryPropertyValueDO : productCategoryPropertyValueDOList){
            productCategoryPropertyValueIdSet.add(productCategoryPropertyValueDO.getId());
            productCategoryPropertyValueNameSet.add(productCategoryPropertyValueDO.getPropertyValueName());
        }

        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = productCategoryProperty.getProductCategoryPropertyValueList();
        for (ProductCategoryPropertyValue productCategoryPropertyValue : productCategoryPropertyValueList){
            productCategoryPropertyValueIdSet.add(productCategoryPropertyValue.getCategoryPropertyValueId());
            productCategoryPropertyValueNameSet.add(productCategoryPropertyValue.getPropertyValueName());
        }

        //如果set中id的数量大于数据库中查询出的List的数量，就说明更改的属性值，有不属于该商品分类属性的
        if (productCategoryPropertyValueIdSet.size() > productCategoryPropertyValueDOList.size()){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_ID_IS_ERROR);
            return result;
        }

        //判断更改的商品分类属性值名称是否有相同的
        if (productCategoryPropertyValueNameSet.size() != (productCategoryPropertyValueDOList.size() + productCategoryPropertyValueList.size())){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_NAME_NOT_SAME);
            return result;
        }

        for (ProductCategoryPropertyValue productCategoryPropertyValue : productCategoryPropertyValueList) {
            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(productCategoryPropertyValue.getCategoryPropertyValueId());
            if (productCategoryPropertyValueDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_PROPERTY_VALUE_NOT_EXISTS);
                return result;
            }

            //判断更改过后的物料型号是否与数据中物料型号对应
            if (productCategoryPropertyValue.getMaterialModelId() != null) {
                MaterialModelDO materialModelDO = materialModelMapper.findById(productCategoryPropertyValue.getMaterialModelId());
                if (materialModelDO == null
                        || !materialModelDO.getMaterialType().equals(productCategoryPropertyDO.getMaterialType())) {
                    result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                    return result;
                }
            }

            productCategoryPropertyValueDO.setPropertyValueName(productCategoryPropertyValue.getPropertyValueName());
            //先判断更改后属性容量值是否需要值
            if (productCategoryPropertyDO.getMaterialType() != null) {
                MaterialTypeDO materialTypeDO = materialTypeMapper.findById(productCategoryPropertyDO.getMaterialType());
                if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                        && productCategoryPropertyValue.getPropertyCapacityValue() == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_CAPACITY_VALUE_NOT_NULL);
                    return result;
                } else if (!CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())
                        && productCategoryPropertyValue.getMaterialModelId() == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_MODEL_NOT_NULL);
                    return result;
                }

                //是否为带字面量配件来区分硬盘、内存之类
                if (CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())) {
                    productCategoryPropertyValueDO.setPropertyCapacityValue(productCategoryPropertyValue.getPropertyCapacityValue());
                    productCategoryPropertyValueDO.setMaterialModelId(null);
                } else if (!CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsCapacityMaterial())) {
                    productCategoryPropertyValueDO.setPropertyCapacityValue(null);
                    productCategoryPropertyValueDO.setMaterialModelId(productCategoryPropertyValue.getMaterialModelId());
                }
            }

            productCategoryPropertyValueDO.setUpdateTime(now);
            productCategoryPropertyValueDO.setUpdateUser(currentUser);
            productCategoryPropertyValueMapper.update(productCategoryPropertyValueDO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(productCategoryPropertyDO.getId());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Page<ProductCategory>> pageProductCategory(ProductCategoryPageParam productCategoryPageParam) {
        ServiceResult<String, Page<ProductCategory>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(productCategoryPageParam.getPageNo(), productCategoryPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("productCategoryPageParam", productCategoryPageParam);

        Integer totalCount = productCategoryMapper.findProductCategoryCountByParams(maps);
        List<ProductCategoryDO> productCategoryDOList = productCategoryMapper.findProductCategoryByParams(maps);

        List<ProductCategory> productCategoryList = ConverterUtil.convertList(productCategoryDOList, ProductCategory.class);
        Page<ProductCategory> page = new Page<>(productCategoryList, totalCount, productCategoryPageParam.getPageNo(), productCategoryPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, ProductCategory> detailProductCategory(ProductCategory productCategory) {
        ServiceResult<String, ProductCategory> result = new ServiceResult<>();
        String currentUser = userSupport.getCurrentUser().getUserId().toString();
        Date now = new Date();

        ProductCategoryDO dbProductCategoryDO = productCategoryMapper.findById(productCategory.getCategoryId());
        if (dbProductCategoryDO == null){
            result.setErrorCode(ErrorCode.PRODUCT_CATEGORY_NOT_EXISTS);
            return result;
        }

        //返回给前端展示的商品类
        ProductCategory productCategoryResult = new ProductCategory();

        List<ProductCategoryDO> productCategoryDOList = productCategoryMapper.findByParentCategoryId(dbProductCategoryDO.getId());

        if (productCategoryDOList.size() < 1){
            //没有子类
            List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findListByCategoryId(dbProductCategoryDO.getId());
            for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList){
                List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList = productCategoryPropertyValueMapper.findListByPropertyIdAndCategoryId(productCategoryPropertyDO.getId(),productCategoryPropertyDO.getCategoryId());
                productCategoryPropertyDO.setProductCategoryPropertyValueDOList(productCategoryPropertyValueDOList);
            }
            dbProductCategoryDO.setProductCategoryPropertyDOList(productCategoryPropertyDOList);
            productCategoryResult = ConverterUtil.convert(dbProductCategoryDO , ProductCategory.class);

        }else{
            //有子类
            for (ProductCategoryDO productCategoryDO : productCategoryDOList){
                //子类节点还有子类
                List<ProductCategoryDO> productCategoryDOList2 = productCategoryMapper.findByParentCategoryId(productCategoryDO.getId());
                if (productCategoryDOList2.size() > 0 ){
                    for (ProductCategoryDO productCategoryDO2 : productCategoryDOList2){
                        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findListByCategoryId(productCategoryDO2.getId());
                        for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList){
                            List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList = productCategoryPropertyValueMapper.findListByPropertyIdAndCategoryId(productCategoryPropertyDO.getId(),productCategoryPropertyDO.getCategoryId());
                            productCategoryPropertyDO.setProductCategoryPropertyValueDOList(productCategoryPropertyValueDOList);
                        }
                        productCategoryDO2.setProductCategoryPropertyDOList(productCategoryPropertyDOList);
                        productCategoryDO.setChildren(productCategoryDOList2);

                    }
                }

                //只有一个子类
                List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findListByCategoryId(productCategoryDO.getId());
                for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList){
                    List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList = productCategoryPropertyValueMapper.findListByPropertyIdAndCategoryId(productCategoryPropertyDO.getId(),productCategoryPropertyDO.getCategoryId());
                    productCategoryPropertyDO.setProductCategoryPropertyValueDOList(productCategoryPropertyValueDOList);
                }
                productCategoryDO.setProductCategoryPropertyDOList(productCategoryPropertyDOList);
                dbProductCategoryDO.setChildren(productCategoryDOList);
                productCategoryResult = ConverterUtil.convert(dbProductCategoryDO , ProductCategory.class);

            }
        }

        productCategoryResult.setCategoryId(dbProductCategoryDO.getId());
        if (productCategoryResult.getChildren().size() > 0 ) {
            for (int i = 0; i < productCategoryDOList.size(); i++) {
                productCategoryResult.getChildren().get(i).setCategoryId(productCategoryDOList.get(i).getId());
                //子类节点还有子类
                if (productCategoryResult.getChildren().get(i).getChildren().size() > 0) {
                    List<ProductCategoryDO> productCategoryDOListByParent = productCategoryMapper.findByParentCategoryId(productCategoryResult.getChildren().get(i).getCategoryId());
                    for (int x = 0; i < productCategoryDOListByParent.size(); i++) {
                        productCategoryResult.getChildren().get(i).getChildren().get(x).setCategoryId(productCategoryDOListByParent.get(x).getId());
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(productCategoryResult);
        return result;
    }

//    private void queryProductCategoryDO(List<ProductCategoryDO> productCategoryDOList){
//        for (ProductCategoryDO productCategoryDO : productCategoryDOList){
//            List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.findListByCategoryId(productCategoryDO.getId());
//            for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList){
//                List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList = productCategoryPropertyValueMapper.findListByCategoryIdAndPropertyId(productCategoryPropertyDO.getId(),productCategoryPropertyDO.getCategoryId());
//                productCategoryPropertyDO.setProductCategoryPropertyValueDOList(productCategoryPropertyValueDOList);
//            }
//        }


    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialModelMapper materialModelMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private UserSupport userSupport;
}
