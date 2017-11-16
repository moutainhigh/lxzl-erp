package com.lxzl.erp.core.service.product.impl;

import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductCategory;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryProperty;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.product.ProductCategoryService;
import com.lxzl.erp.core.service.product.impl.support.ProductCategoryConverter;
import com.lxzl.erp.core.service.product.impl.support.ProductCategoryPropertyConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyValueMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
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
    public ServiceResult<String, List<ProductCategory>> queryAllProductCategory() {
        ServiceResult<String, List<ProductCategory>> result = new ServiceResult<>();

        Map<String, Object> maps = new HashMap<>();
        List<ProductCategoryDO> productCategoryDOList = productCategoryMapper.findAllCategory(maps);
        List<ProductCategory> nodeList = ProductCategoryConverter.convertProductCategoryTree(ProductCategoryConverter.convertProductCategoryDOList(productCategoryDOList));
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
        result.setResult(ProductCategoryPropertyConverter.convertProductCategoryPropertyDOList(productCategoryPropertyDOList));
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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
        productCategoryPropertyValueDO.setCategoryId(productCategoryPropertyDO.getCategoryId());
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productCategoryPropertyValueDO.setCreateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setUpdateUser(loginUser.getUserId().toString());
        productCategoryPropertyValueDO.setCreateTime(currentTime);
        productCategoryPropertyValueDO.setUpdateTime(currentTime);
        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);

        result.setResult(productCategoryPropertyValueDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
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

}
