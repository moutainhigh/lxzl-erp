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
import com.lxzl.erp.core.service.purchase.PurchaseOrderReceiver;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderConverter;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseDeliveryOrderDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderProductDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
public class PurchaseOrderReceiverImpl implements PurchaseOrderReceiver {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderReceiverImpl.class);

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
            purchaseOrderProductDO.setProductSnapshot(JSON.toJSONString(productResult));
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

        //保存采购单
        PurchaseOrderDO purchaseOrderDO = PurchaseOrderConverter.convertPurchaseOrder(purchaseOrder);
        purchaseOrderDO.setPurchaseNo(GenerateNoUtil.generatePurchaseOrderNo(now, loginUser.getUserId()));
        if(purchaseOrderDO.getInvoiceSupplierId()==null){
            purchaseOrderDO.setInvoiceSupplierId(purchaseOrderDO.getProductSupplierId());
        }
        purchaseOrderDO.setPurchaseOrderAmountTotal(totalAmount);
        //创建采购单时，采购单实收金额和采购单结算金额为空
        purchaseOrderDO.setPurchaseOrderAmountReal(null);
        purchaseOrderDO.setPurchaseOrderAmountStatement(null);
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

    @Override
    public ServiceResult<String, Integer> commit(PurchaseOrder purchaseOrder) {
        //todo 校验采购单是否存在
        //todo 调用审核服务
        return null;
    }

    /**
     * 接收审核结果通知，审核通过生成发货单
     * @param verifyResult
     * @param businessId
     */
    @Override
    public boolean receiveVerifyResult(boolean verifyResult, Integer businessId) {
        try{
            if(verifyResult){
                PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findById(businessId);
                if(purchaseOrderDO==null){
                    throw new Exception();
                }
                createPurchaseDeliveryOrderDO(purchaseOrderDO);
            }else{

            }
        }catch (Exception e){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",e);
            return false;
        }catch (Throwable t){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",t);
            return false;
        }
        return false;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void createPurchaseDeliveryOrderDO(PurchaseOrderDO purchaseOrderDO){

        Date now = new Date();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = new PurchaseDeliveryOrderDO();
        purchaseDeliveryOrderDO.setPurchaseOrderId(purchaseOrderDO.getId());
        purchaseDeliveryOrderDO.setPurchaseDeliveryNo(GenerateNoUtil.generatePurchaseDeliveryOrderNo(now, loginUser.getUserId()));
        purchaseDeliveryOrderDO.setWarehouseId(purchaseOrderDO.getWarehouseId());
//        purchaseDeliveryOrderDO.setWarehouseSnapshot();
//        private Integer warehouseSnapshot;   //收货方仓库快照，JSON格式
//        private Integer isInvoice;   //是否有发票，0否1是
//        private Integer isNew;   //是否全新机
//        private BigDecimal purchaseOrderAmountTotal;   //采购发货单总价
//        private Integer purchaseDeliveryOrderStatus;   //采购发货单状态，0待发货，1已发货
//        private Date deliveryTime;   //发货时间
//        private Integer dataStatus;   //状态：0不可用；1可用；2删除
//        private Integer ownerSupplierId;   //数据归属人
//        private String remark;   //备注
//        private Date createTime;   //添加时间
//        private String createUser;   //添加人
//        private Date updateTime;   //添加时间
//        private String updateUser;   //修改人
    }
}
