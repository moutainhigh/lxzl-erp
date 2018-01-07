package com.lxzl.erp.core.service.purchaseApply.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PurchaseApplyOrderItemStatus;
import com.lxzl.erp.common.constant.PurchaseApplyOrderStatus;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderCommitParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrder;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrderMaterial;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchaseApply.PurchaseApplyOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchaseApply.PurchaseApplyOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchaseApply.PurchaseApplyOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchaseApply.PurchaseApplyOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyOrderDO;
import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyOrderProductDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseApplyOrderServiceImpl implements PurchaseApplyOrderService {

    @Autowired
    private PurchaseApplyOrderMapper purchaseApplyOrderMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private ProductService productService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private PurchaseApplyOrderMaterialMapper purchaseApplyOrderMaterialMapper;
    @Autowired
    private PurchaseApplyOrderProductMapper purchaseApplyOrderProductMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(PurchaseApplyOrder purchaseApplyOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User user = userSupport.getCurrentUser();
        //校验用户是否可选此部门
        DepartmentDO departmentDO = userSupport.getAvailableDepartment(purchaseApplyOrder.getDepartmentId());
        if(departmentDO==null){
            result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
            return result;
        }
        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = purchaseApplyOrder.getPurchaseApplyOrderProductList();
        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = purchaseApplyOrder.getPurchaseApplyOrderMaterialList();
        if(CollectionUtil.isEmpty(purchaseApplyOrderProductList)&&CollectionUtil.isEmpty(purchaseApplyOrderMaterialList)){
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        Date now = new Date();
        WarehouseDO warehouseDO = warehouseSupport.getUserWarehouse(user.getUserId());
        SubCompanyDO subCompanyDO = userSupport.getCurrentUserCompany();
        PurchaseApplyOrderDO purchaseApplyOrderDO = new PurchaseApplyOrderDO();
        purchaseApplyOrderDO.setPurchaseApplyOrderNo(generateNoSupport.generatePurchaseApplyOrderNo(subCompanyDO.getSubCompanyCode()));
        purchaseApplyOrderDO.setApplyUserId(user.getUserId());
        purchaseApplyOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseApplyOrderDO.setDepartmentId(departmentDO.getId());
        purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_COMMIT);
        purchaseApplyOrderDO.setAllUseTime(purchaseApplyOrder.getAllUseTime());
        purchaseApplyOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseApplyOrderDO.setRemark(purchaseApplyOrder.getRemark());
        purchaseApplyOrderDO.setCreateTime(now);
        purchaseApplyOrderDO.setCreateUser(user.getUserId().toString());
        purchaseApplyOrderDO.setUpdateTime(now);
        purchaseApplyOrderDO.setUpdateUser(user.getUserId().toString());

        purchaseApplyOrderMapper.save(purchaseApplyOrderDO);


        ServiceResult<String,List<PurchaseApplyOrderProductDO>> applyOrderProductResult = getPurchaseApplyOrderProductDOList(purchaseApplyOrderProductList,purchaseApplyOrderDO,now,user);
        if(!ErrorCode.SUCCESS.equals(applyOrderProductResult.getErrorCode())){
            result.setErrorCode(applyOrderProductResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        ServiceResult<String,List<PurchaseApplyOrderMaterialDO>> applyOrderMaterialResult = getPurchaseApplyOrderMaterialDOList(purchaseApplyOrderMaterialList,purchaseApplyOrderDO,now,user);
        if(!ErrorCode.SUCCESS.equals(applyOrderMaterialResult.getErrorCode())){
            result.setErrorCode(applyOrderProductResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOList = applyOrderProductResult.getResult();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList = applyOrderMaterialResult.getResult();
        if(CollectionUtil.isNotEmpty(purchaseApplyOrderProductDOList)){
            purchaseApplyOrderProductMapper.saveList(purchaseApplyOrderProductDOList);
        }
        if(CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialDOList)){
            purchaseApplyOrderMaterialMapper.saveList(purchaseApplyOrderMaterialDOList);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        return result;
    }

    private ServiceResult<String,List<PurchaseApplyOrderProductDO>> getPurchaseApplyOrderProductDOList(List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList , PurchaseApplyOrderDO purchaseApplyOrderDO , Date now , User user){
        ServiceResult<String,List<PurchaseApplyOrderProductDO>> serviceResult = new ServiceResult<>();
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOListForSave = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(purchaseApplyOrderProductList)){
            for(PurchaseApplyOrderProduct purchaseApplyOrderProduct : purchaseApplyOrderProductList){
                PurchaseApplyOrderProductDO purchaseApplyOrderProductDO = new PurchaseApplyOrderProductDO();
                purchaseApplyOrderProductDO.setPurchaseApplyOrderId(purchaseApplyOrderDO.getId());
                purchaseApplyOrderProductDO.setPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
                ServiceResult<String,Product> productServiceResult = productService.queryProductBySkuId(purchaseApplyOrderProduct.getProductSkuId());
                if(!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())){
                    serviceResult.setErrorCode(productServiceResult.getErrorCode(),productServiceResult.getFormatArgs());
                    return serviceResult;
                }
                Product product = productServiceResult.getResult();
                purchaseApplyOrderProductDO.setProductSkuId(purchaseApplyOrderProduct.getProductSkuId());
                purchaseApplyOrderProductDO.setProductSnapshot(JSON.toJSONString(product));
                purchaseApplyOrderProductDO.setApplyCount(purchaseApplyOrderProduct.getApplyCount());
                purchaseApplyOrderProductDO.setRealCount(0);
                purchaseApplyOrderProductDO.setPurchaseApplyOrderItemStatus(PurchaseApplyOrderItemStatus.PURCHASE_APPLY_ORDER_ITEM_STATUS_WAIT_PURCHASE);
                purchaseApplyOrderProductDO.setIsNew(purchaseApplyOrderProduct.getIsNew());
                if(purchaseApplyOrderDO.getAllUseTime()!=null){
                    purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
                }else{
                    purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderProduct.getUseTime());
                }
                purchaseApplyOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseApplyOrderProductDO.setRemark(purchaseApplyOrderProduct.getRemark());
                purchaseApplyOrderProductDO.setCreateTime(now);
                purchaseApplyOrderProductDO.setCreateUser(user.getUserId().toString());
                purchaseApplyOrderProductDO.setUpdateTime(now);
                purchaseApplyOrderProductDO.setUpdateUser(user.getUserId().toString());

                purchaseApplyOrderProductDOListForSave.add(purchaseApplyOrderProductDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderProductDOListForSave);
        return serviceResult;
    }

    private ServiceResult<String,List<PurchaseApplyOrderMaterialDO>> getPurchaseApplyOrderMaterialDOList(List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList , PurchaseApplyOrderDO purchaseApplyOrderDO , Date now , User user){
        ServiceResult<String,List<PurchaseApplyOrderMaterialDO>> serviceResult = new ServiceResult<>();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOArrayListForSave = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialList)){
            for(PurchaseApplyOrderMaterial purchaseApplyOrderMaterial : purchaseApplyOrderMaterialList){
                PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO = new PurchaseApplyOrderMaterialDO();
                purchaseApplyOrderMaterialDO.setPurchaseApplyOrderId(purchaseApplyOrderDO.getId());
                purchaseApplyOrderMaterialDO.setPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
                MaterialDO materialDO = materialMapper.findByNo(purchaseApplyOrderMaterial.getMaterialNo());
                if(materialDO==null){
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }
                purchaseApplyOrderMaterialDO.setMaterialId(materialDO.getId());
                purchaseApplyOrderMaterialDO.setMaterialNo(materialDO.getMaterialNo());
                purchaseApplyOrderMaterialDO.setApplyCount(purchaseApplyOrderMaterial.getApplyCount());
                purchaseApplyOrderMaterialDO.setRealCount(0);
                purchaseApplyOrderMaterialDO.setPurchaseApplyOrderItemStatus(PurchaseApplyOrderItemStatus.PURCHASE_APPLY_ORDER_ITEM_STATUS_WAIT_PURCHASE);
                purchaseApplyOrderMaterialDO.setIsNew(purchaseApplyOrderMaterial.getIsNew());
                if(purchaseApplyOrderDO.getAllUseTime()!=null){
                    purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
                }else{
                    purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderMaterial.getUseTime());
                }
                purchaseApplyOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseApplyOrderMaterialDO.setRemark(purchaseApplyOrderMaterial.getRemark());
                purchaseApplyOrderMaterialDO.setCreateTime(now);
                purchaseApplyOrderMaterialDO.setCreateUser(user.getUserId().toString());
                purchaseApplyOrderMaterialDO.setUpdateTime(now);
                purchaseApplyOrderMaterialDO.setUpdateUser(user.getUserId().toString());
                purchaseApplyOrderMaterialDOArrayListForSave.add(purchaseApplyOrderMaterialDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderMaterialDOArrayListForSave);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> update(PurchaseApplyOrder purchaseApplyOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, String> commit(PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam) {
        return null;
    }

    @Override
    public String cancel(String purchaseApplyOrderNo) {
        return null;
    }

    @Override
    public ServiceResult<String, PurchaseApplyOrder> queryByNo(String purchaseApplyOrderNo) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<PurchaseApplyOrder>> queryAll(PurchaseApplyOrderPageParam purchaseApplyOrderPageParam) {
        return null;
    }

    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        return false;
    }
}
