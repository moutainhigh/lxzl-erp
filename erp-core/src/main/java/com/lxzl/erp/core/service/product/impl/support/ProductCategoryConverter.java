package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.product.pojo.ProductCategory;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryConverter {

    public static ProductCategory convertProductCategoryDO(ProductCategoryDO productCategoryDO) {
        ProductCategory productCategory = new ProductCategory();
        if (productCategoryDO.getId() != null) {
            productCategory.setCategoryId(productCategoryDO.getId());
        }
        if (productCategoryDO.getCategoryName() != null) {
            productCategory.setCategoryName(productCategoryDO.getCategoryName());
        }
        if (productCategoryDO.getParentCategoryId() != null) {
            productCategory.setParentCategoryId(productCategoryDO.getParentCategoryId());
        }
        if (productCategoryDO.getCategoryType() != null) {
            productCategory.setCategoryType(productCategoryDO.getCategoryType());
        }
        if (productCategoryDO.getDataOrder() != null) {
            productCategory.setDataOrder(productCategoryDO.getDataOrder());
        }
        if (productCategoryDO.getDataStatus() != null) {
            productCategory.setDataStatus(productCategoryDO.getDataStatus());
        }
        if (productCategoryDO.getRemark() != null) {
            productCategory.setRemark(productCategoryDO.getRemark());
        }
        return productCategory;
    }

    public static List<ProductCategory> convertProductCategoryDOList(List<ProductCategoryDO> productCategoryDOList) {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        if (productCategoryDOList != null && productCategoryDOList.size() > 0) {
            for (ProductCategoryDO productCategoryDO : productCategoryDOList) {
                productCategoryList.add(convertProductCategoryDO(productCategoryDO));
            }
        }
        return productCategoryList;
    }
    public static List<ProductCategory> convertProductCategoryTree(List<ProductCategory> productCategoryList) {
        List<ProductCategory> nodeList = new ArrayList<>();
        if (productCategoryList != null) {
            for (ProductCategory node1 : productCategoryList) {
                if (node1.getParentCategoryId().equals(CommonConstant.SUPER_PRODUCT_CATEGORY_ID)) {
                    nodeList.add(node1);
                }
                for (ProductCategory t : productCategoryList) {
                    if (t.getParentCategoryId().equals(node1.getCategoryId())) {
                        if (node1.getChildren() == null) {
                            List<ProductCategory> myChildren = new ArrayList<ProductCategory>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }
                    }
                }
            }
        }

        return nodeList;
    }


}
