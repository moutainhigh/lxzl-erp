package com.lxzl.erp.core.service.purchaseApply.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
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
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchase.impl.PurchaseOrderServiceImpl;
import com.lxzl.erp.core.service.purchaseApply.PurchaseApplyOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
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
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class PurchaseApplyOrderServiceImpl implements PurchaseApplyOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

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
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PermissionSupport permissionSupport;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(PurchaseApplyOrder purchaseApplyOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        User user = userSupport.getCurrentUser();
        //校验用户是否可选此部门
        DepartmentDO departmentDO = userSupport.getAvailableDepartment(purchaseApplyOrder.getDepartmentId());
        if (departmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
            return serviceResult;
        }
        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = purchaseApplyOrder.getPurchaseApplyOrderProductList();
        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = purchaseApplyOrder.getPurchaseApplyOrderMaterialList();
        if (CollectionUtil.isEmpty(purchaseApplyOrderProductList) && CollectionUtil.isEmpty(purchaseApplyOrderMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
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


        ServiceResult<String, List<PurchaseApplyOrderProductDO>> applyOrderProductResult = getPurchaseApplyOrderProductDOList(purchaseApplyOrderProductList, purchaseApplyOrderDO, now, user);
        if (!ErrorCode.SUCCESS.equals(applyOrderProductResult.getErrorCode())) {
            serviceResult.setErrorCode(applyOrderProductResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }
        ServiceResult<String, List<PurchaseApplyOrderMaterialDO>> applyOrderMaterialResult = getPurchaseApplyOrderMaterialDOList(purchaseApplyOrderMaterialList, purchaseApplyOrderDO, now, user);
        if (!ErrorCode.SUCCESS.equals(applyOrderMaterialResult.getErrorCode())) {
            serviceResult.setErrorCode(applyOrderProductResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOList = applyOrderProductResult.getResult();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList = applyOrderMaterialResult.getResult();
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductDOList)) {
            purchaseApplyOrderProductMapper.saveList(purchaseApplyOrderProductDOList);
        }
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialDOList)) {
            purchaseApplyOrderMaterialMapper.saveList(purchaseApplyOrderMaterialDOList);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        return serviceResult;
    }

    private ServiceResult<String, List<PurchaseApplyOrderProductDO>> getPurchaseApplyOrderProductDOList(List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList, PurchaseApplyOrderDO purchaseApplyOrderDO, Date now, User user) {
        ServiceResult<String, List<PurchaseApplyOrderProductDO>> serviceResult = new ServiceResult<>();
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOListForSave = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductList)) {
            for (PurchaseApplyOrderProduct purchaseApplyOrderProduct : purchaseApplyOrderProductList) {
                ServiceResult<String, PurchaseApplyOrderProductDO> createPurchaseApplyOrderProductDOResult = createPurchaseApplyOrderProductDO(purchaseApplyOrderDO, purchaseApplyOrderProduct, now, user);
                if (!ErrorCode.SUCCESS.equals(createPurchaseApplyOrderProductDOResult.getErrorCode())) {
                    serviceResult.setErrorCode(createPurchaseApplyOrderProductDOResult.getErrorCode());
                    return serviceResult;
                }
                purchaseApplyOrderProductDOListForSave.add(createPurchaseApplyOrderProductDOResult.getResult());
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderProductDOListForSave);
        return serviceResult;
    }

    private ServiceResult<String, PurchaseApplyOrderProductDO> createPurchaseApplyOrderProductDO(PurchaseApplyOrderDO purchaseApplyOrderDO, PurchaseApplyOrderProduct purchaseApplyOrderProduct, Date now, User user) {
        ServiceResult<String, PurchaseApplyOrderProductDO> serviceResult = new ServiceResult<>();
        PurchaseApplyOrderProductDO purchaseApplyOrderProductDO = new PurchaseApplyOrderProductDO();
        purchaseApplyOrderProductDO.setPurchaseApplyOrderId(purchaseApplyOrderDO.getId());
        purchaseApplyOrderProductDO.setPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(purchaseApplyOrderProduct.getProductSkuId());
        if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
            serviceResult.setErrorCode(productServiceResult.getErrorCode(), productServiceResult.getFormatArgs());
            return serviceResult;
        }
        Product product = productServiceResult.getResult();
        purchaseApplyOrderProductDO.setProductSkuId(purchaseApplyOrderProduct.getProductSkuId());
        purchaseApplyOrderProductDO.setProductSnapshot(JSON.toJSONString(product));
        purchaseApplyOrderProductDO.setApplyCount(purchaseApplyOrderProduct.getApplyCount());
        purchaseApplyOrderProductDO.setRealCount(0);
        purchaseApplyOrderProductDO.setPurchaseApplyOrderItemStatus(PurchaseApplyOrderItemStatus.PURCHASE_APPLY_ORDER_ITEM_STATUS_WAIT_PURCHASE);
        purchaseApplyOrderProductDO.setIsNew(purchaseApplyOrderProduct.getIsNew());
        if (purchaseApplyOrderDO.getAllUseTime() != null) {
            purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
        } else if(purchaseApplyOrderProduct.getUseTime() == null ){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_USE_TIME_NOT_NUll);
            return serviceResult;
        }else {
            purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderProduct.getUseTime());
        }
        purchaseApplyOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseApplyOrderProductDO.setRemark(purchaseApplyOrderProduct.getRemark());
        purchaseApplyOrderProductDO.setCreateTime(now);
        purchaseApplyOrderProductDO.setCreateUser(user.getUserId().toString());
        purchaseApplyOrderProductDO.setUpdateTime(now);
        purchaseApplyOrderProductDO.setUpdateUser(user.getUserId().toString());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderProductDO);
        return serviceResult;
    }

    private ServiceResult<String, List<PurchaseApplyOrderMaterialDO>> getPurchaseApplyOrderMaterialDOList(List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList, PurchaseApplyOrderDO purchaseApplyOrderDO, Date now, User user) {
        ServiceResult<String, List<PurchaseApplyOrderMaterialDO>> serviceResult = new ServiceResult<>();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOArrayListForSave = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialList)) {
            for (PurchaseApplyOrderMaterial purchaseApplyOrderMaterial : purchaseApplyOrderMaterialList) {
                ServiceResult<String, PurchaseApplyOrderMaterialDO> createPurchaseApplyOrderMaterialDOResult = createPurchaseApplyOrderMaterialDO(purchaseApplyOrderDO, purchaseApplyOrderMaterial, now, user);
                if (!ErrorCode.SUCCESS.equals(createPurchaseApplyOrderMaterialDOResult.getErrorCode())) {
                    serviceResult.setErrorCode(createPurchaseApplyOrderMaterialDOResult.getErrorCode());
                    return serviceResult;
                }
                purchaseApplyOrderMaterialDOArrayListForSave.add(createPurchaseApplyOrderMaterialDOResult.getResult());
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderMaterialDOArrayListForSave);
        return serviceResult;
    }

    private ServiceResult<String, PurchaseApplyOrderMaterialDO> createPurchaseApplyOrderMaterialDO(PurchaseApplyOrderDO purchaseApplyOrderDO, PurchaseApplyOrderMaterial purchaseApplyOrderMaterial, Date now, User user) {
        ServiceResult<String, PurchaseApplyOrderMaterialDO> serviceResult = new ServiceResult<>();
        PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO = new PurchaseApplyOrderMaterialDO();
        purchaseApplyOrderMaterialDO.setPurchaseApplyOrderId(purchaseApplyOrderDO.getId());
        purchaseApplyOrderMaterialDO.setPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        MaterialDO materialDO = materialMapper.findByNo(purchaseApplyOrderMaterial.getMaterialNo());
        if (materialDO == null) {
            serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
            return serviceResult;
        }
        purchaseApplyOrderMaterialDO.setMaterialId(materialDO.getId());
        purchaseApplyOrderMaterialDO.setMaterialNo(materialDO.getMaterialNo());
        purchaseApplyOrderMaterialDO.setApplyCount(purchaseApplyOrderMaterial.getApplyCount());
        purchaseApplyOrderMaterialDO.setRealCount(0);
        purchaseApplyOrderMaterialDO.setPurchaseApplyOrderItemStatus(PurchaseApplyOrderItemStatus.PURCHASE_APPLY_ORDER_ITEM_STATUS_WAIT_PURCHASE);
        purchaseApplyOrderMaterialDO.setIsNew(purchaseApplyOrderMaterial.getIsNew());
        if (purchaseApplyOrderDO.getAllUseTime() != null) {
            purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
        } else if(purchaseApplyOrderMaterial.getUseTime() == null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_USE_TIME_NOT_NUll);
            return serviceResult;
        } else {
            purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderMaterial.getUseTime());
        }
        purchaseApplyOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseApplyOrderMaterialDO.setRemark(purchaseApplyOrderMaterial.getRemark());
        purchaseApplyOrderMaterialDO.setCreateTime(now);
        purchaseApplyOrderMaterialDO.setCreateUser(user.getUserId().toString());
        purchaseApplyOrderMaterialDO.setUpdateTime(now);
        purchaseApplyOrderMaterialDO.setUpdateUser(user.getUserId().toString());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderMaterialDO);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(PurchaseApplyOrder purchaseApplyOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseApplyOrderDO purchaseApplyOrderDO = purchaseApplyOrderMapper.findByNo(purchaseApplyOrder.getPurchaseApplyOrderNo());
        if (purchaseApplyOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_COMMIT.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_CAN_NOT_UPDATE);
            return serviceResult;
        }
        //校验用户是否可选此部门
        DepartmentDO departmentDO = userSupport.getAvailableDepartment(purchaseApplyOrder.getDepartmentId());
        if (departmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
            return serviceResult;
        }
        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = purchaseApplyOrder.getPurchaseApplyOrderProductList();
        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = purchaseApplyOrder.getPurchaseApplyOrderMaterialList();
        if (CollectionUtil.isEmpty(purchaseApplyOrderProductList) && CollectionUtil.isEmpty(purchaseApplyOrderMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        User user = userSupport.getCurrentUser();
        Date now = new Date();

        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOList = purchaseApplyOrderProductMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOListForSave = new ArrayList<>();
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOListForUpdate = new ArrayList<>();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList = purchaseApplyOrderMaterialMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOListForSave = new ArrayList<>();
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOListForUpdate = new ArrayList<>();
        Map<Integer, PurchaseApplyOrderProductDO> deletePurchaseApplyOrderProductDOMap = ListUtil.listToMap(purchaseApplyOrderProductDOList, "id");
        Map<Integer, PurchaseApplyOrderMaterialDO> deletePurchaseApplyOrderMaterialDOMap = ListUtil.listToMap(purchaseApplyOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductList)) {
            for (PurchaseApplyOrderProduct purchaseApplyOrderProduct : purchaseApplyOrderProductList) {
                if (purchaseApplyOrderProduct.getPurchaseApplyOrderProductId() != null) {
                    //修改列表加入
                    PurchaseApplyOrderProductDO purchaseApplyOrderProductDO = purchaseApplyOrderProductMapper.findById(purchaseApplyOrderProduct.getPurchaseApplyOrderProductId());
                    if (purchaseApplyOrderProductDO == null) {
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        return serviceResult;
                    }
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(purchaseApplyOrderProduct.getProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                        serviceResult.setErrorCode(productServiceResult.getErrorCode(), productServiceResult.getFormatArgs());
                        return serviceResult;
                    }
                    Product product = productServiceResult.getResult();
                    purchaseApplyOrderProductDO.setProductSkuId(purchaseApplyOrderProduct.getProductSkuId());
                    purchaseApplyOrderProductDO.setProductSnapshot(JSON.toJSONString(product));
                    purchaseApplyOrderProductDO.setApplyCount(purchaseApplyOrderProduct.getApplyCount());
                    purchaseApplyOrderProductDO.setIsNew(purchaseApplyOrderProduct.getIsNew());
                    if (purchaseApplyOrderDO.getAllUseTime() != null) {
                        purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
                    } else if (purchaseApplyOrderProduct.getUseTime()==null){
                        serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_USE_TIME_NOT_NUll);
                        return serviceResult;
                    }else {
                        purchaseApplyOrderProductDO.setUseTime(purchaseApplyOrderProduct.getUseTime());
                    }
                    purchaseApplyOrderProductDO.setRemark(purchaseApplyOrderProduct.getRemark());
                    purchaseApplyOrderProductDO.setUpdateTime(now);
                    purchaseApplyOrderProductDO.setUpdateUser(user.getUserId().toString());
                    purchaseApplyOrderProductDOListForUpdate.add(purchaseApplyOrderProductDO);
                    //待删除列表删除
                    deletePurchaseApplyOrderProductDOMap.remove(purchaseApplyOrderProductDO.getId());
                } else {
                    //新增列表加入
                    ServiceResult<String, PurchaseApplyOrderProductDO> createPurchaseApplyOrderProductDOResult = createPurchaseApplyOrderProductDO(purchaseApplyOrderDO, purchaseApplyOrderProduct, now, user);
                    if (!ErrorCode.SUCCESS.equals(createPurchaseApplyOrderProductDOResult.getErrorCode())) {
                        serviceResult.setErrorCode(createPurchaseApplyOrderProductDOResult.getErrorCode());
                        return serviceResult;
                    }
                    purchaseApplyOrderProductDOListForSave.add(createPurchaseApplyOrderProductDOResult.getResult());
                }
            }
        }

        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialList)) {
            for (PurchaseApplyOrderMaterial purchaseApplyOrderMaterial : purchaseApplyOrderMaterialList) {
                if (purchaseApplyOrderMaterial.getPurchaseApplyOrderMaterialId() != null) {
                    //修改列表加入
                    PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO = purchaseApplyOrderMaterialMapper.findById(purchaseApplyOrderMaterial.getPurchaseApplyOrderMaterialId());
                    if (purchaseApplyOrderMaterialDO == null) {
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        return serviceResult;
                    }
                    MaterialDO materialDO = materialMapper.findByNo(purchaseApplyOrderMaterial.getMaterialNo());
                    if (materialDO == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        return serviceResult;
                    }
                    purchaseApplyOrderMaterialDO.setMaterialId(materialDO.getId());
                    purchaseApplyOrderMaterialDO.setMaterialNo(materialDO.getMaterialNo());
                    purchaseApplyOrderMaterialDO.setApplyCount(purchaseApplyOrderMaterial.getApplyCount());
                    purchaseApplyOrderMaterialDO.setIsNew(purchaseApplyOrderMaterial.getIsNew());
                    if (purchaseApplyOrderDO.getAllUseTime() != null) {
                        purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderDO.getAllUseTime());
                    } else if(purchaseApplyOrderMaterial.getUseTime() == null ){
                        serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_USE_TIME_NOT_NUll);
                        return serviceResult;
                    }else{
                        purchaseApplyOrderMaterialDO.setUseTime(purchaseApplyOrderMaterial.getUseTime());
                    }
                    purchaseApplyOrderMaterialDO.setRemark(purchaseApplyOrderMaterial.getRemark());
                    purchaseApplyOrderMaterialDO.setUpdateTime(now);
                    purchaseApplyOrderMaterialDO.setUpdateUser(user.getUserId().toString());
                    purchaseApplyOrderMaterialDOListForUpdate.add(purchaseApplyOrderMaterialDO);
                    //待删除列表删除
                    deletePurchaseApplyOrderMaterialDOMap.remove(purchaseApplyOrderMaterialDO.getId());
                } else {
                    //新增列表加入
                    ServiceResult<String, PurchaseApplyOrderMaterialDO> createPurchaseApplyOrderMaterialDOResult = createPurchaseApplyOrderMaterialDO(purchaseApplyOrderDO, purchaseApplyOrderMaterial, now, user);
                    if (!ErrorCode.SUCCESS.equals(createPurchaseApplyOrderMaterialDOResult.getErrorCode())) {
                        serviceResult.setErrorCode(createPurchaseApplyOrderMaterialDOResult.getErrorCode());
                        return serviceResult;
                    }
                    purchaseApplyOrderMaterialDOListForSave.add(createPurchaseApplyOrderMaterialDOResult.getResult());
                }
            }
        }

        purchaseApplyOrderDO.setAllUseTime(purchaseApplyOrder.getAllUseTime());
        purchaseApplyOrderDO.setRemark(purchaseApplyOrder.getRemark());
        purchaseApplyOrderDO.setUpdateTime(now);
        purchaseApplyOrderDO.setUpdateUser(user.getUserId().toString());
        purchaseApplyOrderMapper.update(purchaseApplyOrderDO);

        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductDOListForSave)) {
            purchaseApplyOrderProductMapper.saveList(purchaseApplyOrderProductDOListForSave);
        }
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialDOListForSave)) {
            purchaseApplyOrderMaterialMapper.saveList(purchaseApplyOrderMaterialDOListForSave);
        }
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductDOListForUpdate)) {
            for (PurchaseApplyOrderProductDO purchaseApplyOrderProductDO : purchaseApplyOrderProductDOListForUpdate) {
                purchaseApplyOrderProductMapper.update(purchaseApplyOrderProductDO);
            }
        }
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialDOListForUpdate)) {
            for (PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO : purchaseApplyOrderMaterialDOListForUpdate) {
                purchaseApplyOrderMaterialMapper.update(purchaseApplyOrderMaterialDO);
            }
        }
        if (deletePurchaseApplyOrderProductDOMap != null && deletePurchaseApplyOrderProductDOMap.size() > 0) {
            for (Integer id : deletePurchaseApplyOrderProductDOMap.keySet()) {
                PurchaseApplyOrderProductDO purchaseApplyOrderProductDO = deletePurchaseApplyOrderProductDOMap.get(id);
                purchaseApplyOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                purchaseApplyOrderProductDO.setUpdateTime(now);
                purchaseApplyOrderProductDO.setUpdateUser(user.getUserId().toString());
                purchaseApplyOrderProductMapper.update(purchaseApplyOrderProductDO);
            }
        }
        if (deletePurchaseApplyOrderMaterialDOMap != null && deletePurchaseApplyOrderMaterialDOMap.size() > 0) {
            for (Integer id : deletePurchaseApplyOrderMaterialDOMap.keySet()) {
                PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO = deletePurchaseApplyOrderMaterialDOMap.get(id);
                purchaseApplyOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                purchaseApplyOrderMaterialDO.setUpdateTime(now);
                purchaseApplyOrderMaterialDO.setUpdateUser(user.getUserId().toString());
                purchaseApplyOrderMaterialMapper.update(purchaseApplyOrderMaterialDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrderDO.getPurchaseApplyOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> commit(PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam) {

        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        //校验采购单是否存在
        PurchaseApplyOrderDO purchaseApplyOrderDO = purchaseApplyOrderMapper.findByNo(purchaseApplyOrderCommitParam.getPurchaseApplyOrderNo());
        if (purchaseApplyOrderDO == null) {
            result.setErrorCode(ErrorCode.PURCHASE_APPLY_ORDER_NOT_EXISTS);
            return result;
        } else if (!PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_COMMIT.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus())) {
            //只有待提交状态的可以提交
            result.setErrorCode(ErrorCode.COMMITTED_CAN_NOT_REPEAT);
            return result;
        }
        if (!purchaseApplyOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_PURCHASE_APPLY_ORDER);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (purchaseApplyOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_PURCHASE_APPLY_ORDER, purchaseApplyOrderCommitParam.getPurchaseApplyOrderNo(), purchaseApplyOrderCommitParam.getVerifyUserId(),null, purchaseApplyOrderCommitParam.getRemark(), purchaseApplyOrderCommitParam.getImgIdList());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_VERIFYING);
                purchaseApplyOrderDO.setUpdateTime(now);
                purchaseApplyOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseApplyOrderMapper.update(purchaseApplyOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_PURCHASE);
            purchaseApplyOrderDO.setUpdateTime(now);
            purchaseApplyOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseApplyOrderMapper.update(purchaseApplyOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public String cancel(String purchaseApplyOrderNo) {
        PurchaseApplyOrderDO purchaseApplyOrderDO = purchaseApplyOrderMapper.findByNo(purchaseApplyOrderNo);
        if (purchaseApplyOrderDO == null) {
            return ErrorCode.PURCHASE_APPLY_ORDER_NOT_EXISTS;
        }
        if (!PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_COMMIT.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus()) &&
                !PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_VERIFYING.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus()) &&
                !PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_PURCHASE.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus())) {
            return ErrorCode.PURCHASE_APPLY_CAN_NOT_CANCEL_BY_STATUS;
        }
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOList = purchaseApplyOrderProductMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderNo);
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList = purchaseApplyOrderMaterialMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderNo);
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderProductDOList)) {
            for (PurchaseApplyOrderProductDO purchaseApplyOrderProductDO : purchaseApplyOrderProductDOList) {
                if (purchaseApplyOrderProductDO.getRealCount() > 0) {
                    return ErrorCode.PURCHASE_APPLY_YET_RECEIVE_CAN_NOT_CANCEL;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(purchaseApplyOrderMaterialDOList)) {
            for (PurchaseApplyOrderMaterialDO purchaseApplyOrderMaterialDO : purchaseApplyOrderMaterialDOList) {
                if (purchaseApplyOrderMaterialDO.getRealCount() > 0) {
                    return ErrorCode.PURCHASE_APPLY_YET_RECEIVE_CAN_NOT_CANCEL;
                }
            }
        }
        //审核中的强制取消
        if (PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_VERIFYING.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_PURCHASE_APPLY_ORDER, purchaseApplyOrderNo);
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                return cancelWorkFlowResult.getErrorCode();
            }
        }
        //修改采购申请单状态为取消
        purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_CANCEL);
        purchaseApplyOrderDO.setUpdateTime(new Date());
        purchaseApplyOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseApplyOrderMapper.update(purchaseApplyOrderDO);
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, PurchaseApplyOrder> queryByNo(String purchaseApplyOrderNo) {
        ServiceResult<String, PurchaseApplyOrder> serviceResult = new ServiceResult<>();
        PurchaseApplyOrderDO purchaseApplyOrderDO = purchaseApplyOrderMapper.findByNo(purchaseApplyOrderNo);
        if (purchaseApplyOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_APPLY_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        List<PurchaseApplyOrderProductDO> purchaseApplyOrderProductDOList = purchaseApplyOrderProductMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderNo);
        List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList = purchaseApplyOrderMaterialMapper.findByPurchaseApplyOrderNo(purchaseApplyOrderNo);
        purchaseApplyOrderDO.setPurchaseApplyOrderProductDOList(purchaseApplyOrderProductDOList);
        purchaseApplyOrderDO.setPurchaseApplyOrderMaterialDOList(purchaseApplyOrderMaterialDOList);
        PurchaseApplyOrder purchaseApplyOrder = ConverterUtil.convert(purchaseApplyOrderDO, PurchaseApplyOrder.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseApplyOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseApplyOrder>> queryAll(PurchaseApplyOrderPageParam purchaseApplyOrderPageParam) {
        ServiceResult<String, Page<PurchaseApplyOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseApplyOrderPageParam.getPageNo(), purchaseApplyOrderPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("purchaseApplyOrderPageParam", purchaseApplyOrderPageParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = purchaseApplyOrderMapper.listCount(maps);
        List<PurchaseApplyOrderDO> purchaseApplyOrderDOList = purchaseApplyOrderMapper.listPage(maps);
        List<PurchaseApplyOrder> purchaseApplyOrderList = ConverterUtil.convertList(purchaseApplyOrderDOList, PurchaseApplyOrder.class);
        Page<PurchaseApplyOrder> page = new Page<>(purchaseApplyOrderList, totalCount, purchaseApplyOrderPageParam.getPageNo(), purchaseApplyOrderPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            PurchaseApplyOrderDO purchaseApplyOrderDO = purchaseApplyOrderMapper.findByNo(businessNo);
            if (purchaseApplyOrderDO == null) {
                return false;
            }
            //不是审核中状态的采购单，拒绝处理
            if (!PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_VERIFYING.equals(purchaseApplyOrderDO.getPurchaseApplyOrderStatus())) {
                return false;
            }

            if (verifyResult) {
                purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_PURCHASE);
            } else {
                purchaseApplyOrderDO.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_COMMIT);
            }
            purchaseApplyOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseApplyOrderDO.setUpdateTime(new Date());
            purchaseApplyOrderMapper.update(purchaseApplyOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("【采购申请单审核业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        } catch (Throwable t) {
            logger.error("【采购申请单审核业务处理异常】", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        }
    }
}
