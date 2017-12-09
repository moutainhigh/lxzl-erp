package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuPropertyDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductConverter {
    public static ProductDO convertProduct(Product product) {
        ProductDO productDO = new ProductDO();
        if (product.getProductId() != null) {
            productDO.setId(product.getProductId());
        }
        BeanUtils.copyProperties(product, productDO);
        return productDO;
    }

    public static ProductSkuDO convertProductSku(ProductSku productSku) {
        ProductSkuDO productSkuDO = new ProductSkuDO();
        if (productSku.getSkuId() != null) {
            productSkuDO.setId(productSku.getSkuId());
        }
        if (productSku.getSkuName() != null) {
            productSkuDO.setSkuName(productSku.getSkuName());
        }
        if (productSku.getProductId() != null) {
            productSkuDO.setProductId(productSku.getProductId());
        }
        // 修改商品的时候，不允许修改库存
        /*if (productSku.getStock() != null) {
            productSkuDO.setStock(productSku.getStock());
        }*/
        if (productSku.getSkuPrice() != null) {
            productSkuDO.setSkuPrice(productSku.getSkuPrice());
        }
        if (productSku.getDayRentPrice() != null) {
            productSkuDO.setDayRentPrice(productSku.getDayRentPrice());
        }
        if (productSku.getMonthRentPrice() != null) {
            productSkuDO.setMonthRentPrice(productSku.getMonthRentPrice());
        }
        if (productSku.getCustomCode() != null) {
            productSkuDO.setCustomCode(productSku.getCustomCode());
        }
        if (productSku.getBarCode() != null) {
            productSkuDO.setBarCode(productSku.getBarCode());
        }
        if (productSku.getProperties() != null) {
            productSkuDO.setProperties(productSku.getProperties());
        }
        if (productSku.getRemark() != null) {
            productSkuDO.setRemark(productSku.getRemark());
        }
        if (productSku.getDataStatus() != null) {
            productSkuDO.setDataStatus(productSku.getDataStatus());
        }
        return productSkuDO;
    }


    public static ProductSku convertProductSkuDO(ProductSkuDO productSkuDO) {
        ProductSku productSku = new ProductSku();
        if (productSkuDO.getId() != null) {
            productSku.setSkuId(productSkuDO.getId());
        }
        if (productSkuDO.getSkuName() != null) {
            productSku.setSkuName(productSkuDO.getSkuName());
        }
        if (productSkuDO.getProductId() != null) {
            productSku.setProductId(productSkuDO.getProductId());
        }
        if (productSkuDO.getStock() != null) {
            productSku.setStock(productSkuDO.getStock());
        }
        if (productSkuDO.getSkuPrice() != null) {
            productSku.setSkuPrice(productSkuDO.getSkuPrice());
        }
        if (productSkuDO.getDayRentPrice() != null) {
            productSku.setDayRentPrice(productSkuDO.getDayRentPrice());
        }
        if (productSkuDO.getMonthRentPrice() != null) {
            productSku.setMonthRentPrice(productSkuDO.getMonthRentPrice());
        }
        if (productSkuDO.getCustomCode() != null) {
            productSku.setCustomCode(productSkuDO.getCustomCode());
        }
        if (productSkuDO.getBarCode() != null) {
            productSku.setBarCode(productSkuDO.getBarCode());
        }
        if (productSkuDO.getProperties() != null) {
            productSku.setProperties(productSkuDO.getProperties());
        }
        if (productSkuDO.getRemark() != null) {
            productSku.setRemark(productSkuDO.getRemark());
        }
        if (productSkuDO.getDataStatus() != null) {
            productSku.setDataStatus(productSkuDO.getDataStatus());
        }
        if (productSkuDO.getProductSkuPropertyDOList() != null && !productSkuDO.getProductSkuPropertyDOList().isEmpty()) {
            productSku.setProductSkuPropertyList(convertProductSkuPropertyDOList(productSkuDO.getProductSkuPropertyDOList()));
        }
        if (productSkuDO.getProductMaterialDOList() != null && !productSkuDO.getProductMaterialDOList().isEmpty()) {
            productSku.setProductMaterialList(convertProductMaterialDOList(productSkuDO.getProductMaterialDOList()));
        }

        if (productSkuDO.getProductName() != null) {
            productSku.setProductName(productSkuDO.getProductName());
        }
        return productSku;
    }

    public static List<ProductSkuDO> convertProductSkuList(List<ProductSku> productSkuList) {
        List<ProductSkuDO> productSkuDOList = new ArrayList<>();
        if (productSkuList != null && productSkuList.size() > 0) {
            for (ProductSku productSku : productSkuList) {
                productSkuDOList.add(convertProductSku(productSku));
            }
        }
        return productSkuDOList;
    }

    public static List<ProductSku> convertProductSkuDOList(List<ProductSkuDO> productSkuDOList) {
        List<ProductSku> productSkuList = new ArrayList<>();
        if (productSkuDOList != null && productSkuDOList.size() > 0) {
            for (ProductSkuDO productSkuDO : productSkuDOList) {
                productSkuList.add(convertProductSkuDO(productSkuDO));
            }
        }
        return productSkuList;
    }

    public static ProductSkuPropertyDO convertProductSkuProperty(ProductSkuProperty productSkuProperty) {
        ProductSkuPropertyDO productSkuPropertyDO = new ProductSkuPropertyDO();
        if (productSkuProperty.getSkuPropertyId() != null) {
            productSkuPropertyDO.setId(productSkuProperty.getSkuPropertyId());
        }
        if (productSkuProperty.getProductId() != null) {
            productSkuPropertyDO.setProductId(productSkuProperty.getProductId());
        }
        if (productSkuProperty.getPropertyId() != null) {
            productSkuPropertyDO.setPropertyId(productSkuProperty.getPropertyId());
        }
        if (productSkuProperty.getPropertyValueId() != null) {
            productSkuPropertyDO.setPropertyValueId(productSkuProperty.getPropertyValueId());
        }
        if (productSkuProperty.getIsSku() != null) {
            productSkuPropertyDO.setIsSku(productSkuProperty.getIsSku());
        }
        if (productSkuProperty.getSkuId() != null) {
            productSkuPropertyDO.setSkuId(productSkuProperty.getSkuId());
        }
        if (productSkuProperty.getRemark() != null) {
            productSkuPropertyDO.setRemark(productSkuProperty.getRemark());
        }
        if (productSkuProperty.getDataStatus() != null) {
            productSkuPropertyDO.setDataStatus(productSkuProperty.getDataStatus());
        }
        return productSkuPropertyDO;
    }

    public static ProductSkuProperty convertProductSkuPropertyDO(ProductSkuPropertyDO productSkuPropertyDO) {
        ProductSkuProperty productSkuProperty = new ProductSkuProperty();
        if (productSkuPropertyDO.getId() != null) {
            productSkuProperty.setSkuPropertyId(productSkuPropertyDO.getId());
        }
        if (productSkuPropertyDO.getProductId() != null) {
            productSkuProperty.setProductId(productSkuPropertyDO.getProductId());
        }
        if (productSkuPropertyDO.getPropertyId() != null) {
            productSkuProperty.setPropertyId(productSkuPropertyDO.getPropertyId());
        }
        if (productSkuPropertyDO.getPropertyName() != null) {
            productSkuProperty.setPropertyName(productSkuPropertyDO.getPropertyName());
        }
        if (productSkuPropertyDO.getPropertyValueId() != null) {
            productSkuProperty.setPropertyValueId(productSkuPropertyDO.getPropertyValueId());
        }
        if (productSkuPropertyDO.getPropertyValueName() != null) {
            productSkuProperty.setPropertyValueName(productSkuPropertyDO.getPropertyValueName());
        }
        if (productSkuPropertyDO.getIsSku() != null) {
            productSkuProperty.setIsSku(productSkuPropertyDO.getIsSku());
        }
        if (productSkuPropertyDO.getSkuId() != null) {
            productSkuProperty.setSkuId(productSkuPropertyDO.getSkuId());
        }
        if (productSkuPropertyDO.getRemark() != null) {
            productSkuProperty.setRemark(productSkuPropertyDO.getRemark());
        }
        if (productSkuPropertyDO.getDataStatus() != null) {
            productSkuProperty.setDataStatus(productSkuPropertyDO.getDataStatus());
        }
        return productSkuProperty;
    }

    public static List<ProductMaterial> convertProductMaterialDOList(List<ProductMaterialDO> productMaterialDOList) {
        List<ProductMaterial> productMaterialList = new ArrayList<>();
        if (productMaterialDOList != null && !productMaterialDOList.isEmpty()) {
            for (ProductMaterialDO productMaterialDO : productMaterialDOList) {
                productMaterialList.add(convertProductMaterialDO(productMaterialDO));
            }
        }
        return productMaterialList;
    }

    public static ProductMaterial convertProductMaterialDO(ProductMaterialDO productMaterialDO) {
        ProductMaterial productMaterial = new ProductMaterial();
        if (productMaterialDO.getId() != null) {
            productMaterial.setProductMaterialId(productMaterialDO.getId());
        }
        BeanUtils.copyProperties(productMaterialDO, productMaterial);

        return productMaterial;
    }

    public static List<ProductSkuProperty> convertProductSkuPropertyDOList(List<ProductSkuPropertyDO> productSkuPropertyDOList) {
        List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
        if (productSkuPropertyDOList != null && !productSkuPropertyDOList.isEmpty()) {
            for (ProductSkuPropertyDO productSkuPropertyDO : productSkuPropertyDOList) {
                productSkuPropertyList.add(convertProductSkuPropertyDO(productSkuPropertyDO));
            }
        }

        return productSkuPropertyList;
    }

    public static Product convertProductDO(ProductDO productDO) {
        Product product = new Product();
        if (productDO.getId() != null) {
            product.setProductId(productDO.getId());
        }
        BeanUtils.copyProperties(productDO, product);
        if (productDO.getProductImgDOList() != null && !productDO.getProductImgDOList().isEmpty()) {
            product.setProductImgList(ProductImageConverter.convertProductImgDOList(productDO.getProductImgDOList()));
        }
        if (productDO.getProductDescImgDOList() != null && !productDO.getProductDescImgDOList().isEmpty()) {
            product.setProductDescImgList(ProductImageConverter.convertProductImgDOList(productDO.getProductDescImgDOList()));
        }
        if (productDO.getProductSkuDOList() != null && !productDO.getProductSkuDOList().isEmpty()) {
            product.setProductSkuList(convertProductSkuDOList(productDO.getProductSkuDOList()));
        }
        if (productDO.getProductPropertyDOList() != null && !productDO.getProductPropertyDOList().isEmpty()) {
            product.setProductPropertyList(convertProductSkuPropertyDOList(productDO.getProductPropertyDOList()));
        }
        return product;
    }


    public static List<Product> convertProductDOList(List<ProductDO> productDOList) {
        List<Product> productList = new ArrayList<>();
        if (productDOList != null && !productDOList.isEmpty()) {
            for (ProductDO productDO : productDOList) {
                productList.add(convertProductDO(productDO));
            }
        }
        return productList;
    }
}
