package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.product.pojo.ProductCategory;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryConverter {

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
