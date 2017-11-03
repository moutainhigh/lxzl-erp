package com.lxzl.erp.core.service.purchase.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PurchaseOrderStatus;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderConverter;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PurchaseOrderProductMapper purchaseOrderProductMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> add(PurchaseOrder purchaseOrder) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        String loginUserId = loginUser.getUserId().toString();
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        //声明待保存的采购单项列表
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = new ArrayList<>();
        //声明累加采购单总价
        BigDecimal totalAmount = new BigDecimal(0);
        // 验证采购单商品项是否合法
        for(PurchaseOrderProduct purchaseOrderProduct : purchaseOrderProductList){

            if(purchaseOrderProduct.getProductSkuId()==null){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_NOT_NULL);
                return result;
            }
            if(purchaseOrderProduct.getProductAmount()==null||  purchaseOrderProduct.getProductAmount().doubleValue()<=0){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_PRICE_ERROR);
                return result;
            }
            if(purchaseOrderProduct.getProductCount()==null||purchaseOrderProduct.getProductCount()<=0){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_COUNT_ERROR);
                return result;
            }
            ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseOrderProduct.getProductSkuId());
            if(productSkuDO==null){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            //累加采购单总价
            totalAmount = BigDecimalUtil.add(totalAmount,BigDecimalUtil.mul(purchaseOrderProduct.getProductAmount(),new BigDecimal(purchaseOrderProduct.getProductCount())));

            PurchaseOrderProductDO purchaseOrderProductDO = new PurchaseOrderProductDO();
            //保存采购订单商品项快照
            ServiceResult<String,Product> productResult = productService.queryProductById(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductModeSnapshot(JSON.toJSONString(productResult));
            purchaseOrderProductDO.setProductId(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductName(productSkuDO.getProductName());
            purchaseOrderProductDO.setProductSkuId(productSkuDO.getId());
            purchaseOrderProductDO.setProductCount(purchaseOrderProduct.getProductCount());
            purchaseOrderProductDO.setProductAmount(purchaseOrderProduct.getProductAmount());
            purchaseOrderProductDO.setRemark(purchaseOrderProduct.getRemark());
            purchaseOrderProductDO.setCreateUser(loginUserId);
            purchaseOrderProductDO.setUpdateUser(loginUserId);
            purchaseOrderProductDO.setCreateTime(now);
            purchaseOrderProductDO.setUpdateTime(now);
            purchaseOrderProductDOList.add(purchaseOrderProductDO);
        }
        PurchaseOrderDO purchaseOrderDO = PurchaseOrderConverter.convertPurchaseOrder(purchaseOrder);
        purchaseOrderDO.setPurchaseNo(GenerateNoUtil.generateOrderNo(now, loginUser.getUserId()));
        if(purchaseOrderDO.getInvoiceSupplierId()==null){
            purchaseOrderDO.setInvoiceSupplierId(purchaseOrderDO.getProductSupplierId());
        }
        purchaseOrderDO.setPurchaseOrderAmountTotal(totalAmount);
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PENDING);
        purchaseOrderDO.setDeliveryTime(null);
        purchaseOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseOrderDO.setOwner(loginUser.getUserId());
        purchaseOrderDO.setCreateUser(loginUserId);
        purchaseOrderDO.setUpdateUser(loginUserId);
        purchaseOrderDO.setCommitStatus((CommonConstant.COMMON_CONSTANT_NO));
        purchaseOrderDO.setCreateTime(now);
        purchaseOrderDO.setUpdateTime(now);
        purchaseOrderMapper.save(purchaseOrderDO);

        //保存采购订单项
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
            purchaseOrderProductDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderProductMapper.save(purchaseOrderProductDO);
        }
        result.setResult(purchaseOrderDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> update(PurchaseOrder purchaseOrder) {
//        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
//        Date currentTime = new Date();
//        ServiceResult<String, Integer> result = new ServiceResult<>();
//        if (!ErrorCode.SUCCESS.equals(verifyAddCode)) {
//            result.setErrorCode(verifyAddCode);
//            return result;
//        }
//        ProductDO productDO = ConvertProduct.convertProduct(product);
//        productDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//        if (loginUser != null) {
//            productDO.setUpdateUser(loginUser.getUserId().toString());
//        }
//        productDO.setUpdateTime(currentTime);
//        productMapper.update(productDO);
//        Integer productId = productDO.getId();
//        saveProductImage(product.getProductImgList(), 1, productId, loginUser, currentTime);
//        saveProductImage(product.getProductDescImgList(), 2, productId, loginUser, currentTime);
//        saveSkuAndProperties(product.getProductSkuList(), productId, loginUser, currentTime);
//        saveProductProperties(product.getProductPropertyList(), productId, loginUser, currentTime);
//
//        result.setErrorCode(ErrorCode.SUCCESS);
//        result.setResult(product.getProductId());
//        return result;
        return null;
    }

    @Override
    public ServiceResult<String, PurchaseOrder> getById(Integer purchaseId) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam) {
        return null;
    }
}
