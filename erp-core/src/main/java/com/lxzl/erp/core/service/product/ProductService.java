package com.lxzl.erp.core.service.product;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.se.core.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService extends BaseService {

    // 上传商品图片
    ServiceResult<String, List<ProductImg>> uploadImage(MultipartFile[] files);

    // 删除商品图片
    ServiceResult<String, Integer> deleteImage(Integer imgId);

    // 创建商品
    ServiceResult<String, Integer> addProduct(Product product);

    // 变更商品
    ServiceResult<String, Integer> updateProduct(Product product);

    // 根据商品ID查询商品
    ServiceResult<String, Product> queryProductById(Integer productId);

    /**
     * @param productId 商品ID
     * @return  商品详情（带物料信息）
     */
    ServiceResult<String, Product> queryProductDetailById(Integer productId);

    // 根据参数查询商品
    ServiceResult<String, Page<Product>> queryAllProduct(ProductQueryParam param);

    // 商品入库
    ServiceResult<String, Integer> productInStorage(ProductInStorage productInStorage);

    // 查询设备列表
    ServiceResult<String, Page<ProductEquipment>> queryAllProductEquipment(ProductEquipmentQueryParam productEquipmentQueryParam);

    // 查询商品sku列表
    ServiceResult<String, Page<ProductSku>> queryProductSkuList(ProductSkuQueryParam productSkuQueryParam);
}
