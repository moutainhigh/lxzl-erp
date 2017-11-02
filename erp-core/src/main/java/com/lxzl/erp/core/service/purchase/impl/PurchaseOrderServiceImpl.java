package com.lxzl.erp.core.service.purchase.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.company.impl.support.CompanyConverter;
import com.lxzl.erp.core.service.product.impl.support.ConvertProduct;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderConverter;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;


    @Override
    public ServiceResult<String, Integer> add(PurchaseOrder purchaseOrder) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date now = new Date();
        PurchaseOrderDO purchaseOrderDO = PurchaseOrderConverter.convertPurchaseOrder(purchaseOrder);
        purchaseOrderDO.setPurchaseNo(GenerateNoUtil.generateOrderNo(now, loginUser.getUserId()));
        purchaseOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseOrderDO.setCreateUser(loginUser.getUserId().toString());
        purchaseOrderDO.setUpdateUser(loginUser.getUserId().toString());
        purchaseOrderDO.setCreateTime(new Date());
        purchaseOrderDO.setUpdateTime(new Date());
        purchaseOrderMapper.save(purchaseOrderDO);
//  `product_supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
//  `invoice_supplier_id` int(20) NOT NULL COMMENT '发票供应商ID',
//  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
//  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
//  `is_new` int(11) NOT NULL COMMENT '是否全新机',
//  `purchase_order_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '采购单总价',
//  `purchase_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购单状态，0待采购，1部分采购，2全部采购',
//  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
//  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态，0待提交，1已提交，2审批通过，3审批驳回，4取消',
//  `verify_user` varchar(20) NOT NULL DEFAULT '' COMMENT '审核人',
//  `verify_time` datetime DEFAULT NULL COMMENT '审核时间',
//  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
//  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
//  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
//  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
//  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
//  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
//  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
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
