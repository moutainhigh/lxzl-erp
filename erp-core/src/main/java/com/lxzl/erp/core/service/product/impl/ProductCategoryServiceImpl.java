package com.lxzl.erp.core.service.product.impl;

import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
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
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyValueMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
