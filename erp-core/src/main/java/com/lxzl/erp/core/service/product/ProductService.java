package com.lxzl.erp.core.service.product;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.se.core.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2017/10/6.
 * Time: 14:48.
 * @author gaochao
 */
public interface ProductService extends BaseService {

    /**
     * 上传图片
     *
     * @param files 图片流
     * @return 图片ID
     */
    ServiceResult<String, List<ProductImg>> uploadImage(MultipartFile[] files);

    /**
     * 删除商品图片
     *
     * @param imgId 图片ID
     * @return 图片ID
     */
    ServiceResult<String, Integer> deleteImage(Integer imgId);

    /**
     * 创建商品
     *
     * @param product 商品信息
     * @return 商品ID
     */
    ServiceResult<String, Integer> addProduct(Product product);

    /**
     * 变更商品
     *
     * @param product 商品信息
     * @return 商品ID
     */
    ServiceResult<String, Integer> updateProduct(Product product);

    /**
     * 根据商品ID查询商品
     *
     * @param productId 商品ID
     * @return 单个商品信息
     */
    ServiceResult<String, Product> queryProductById(Integer productId);

    /**
     * 商品详情
     *
     * @param productId 商品ID
     * @return 商品详情（带物料信息）
     */
    ServiceResult<String, Product> queryProductDetailById(Integer productId);

    /**
     * 根据参数查询商品
     *
     * @param param 查询商品参数
     * @return 商品列表
     */
    ServiceResult<String, Page<Product>> queryAllProduct(ProductQueryParam param);

    /**
     * 查询设备列表
     *
     * @param productEquipmentQueryParam 查询设备列表参数
     * @return 设备列表
     */
    ServiceResult<String, Page<ProductEquipment>> queryAllProductEquipment(ProductEquipmentQueryParam productEquipmentQueryParam);


    /**
     * 设备明细
     *
     * @param equipmentNo 设备编号
     * @return 设备明细
     */
    ServiceResult<String, ProductEquipment> queryProductEquipmentDetail(String equipmentNo);


    /**
     * 查询商品sku列表
     *
     * @param productSkuQueryParam 查询商品列表参数
     * @return SKU列表
     */
    ServiceResult<String, Page<ProductSku>> queryProductSkuList(ProductSkuQueryParam productSkuQueryParam);
}
