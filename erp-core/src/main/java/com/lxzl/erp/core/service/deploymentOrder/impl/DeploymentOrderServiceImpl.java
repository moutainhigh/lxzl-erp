package com.lxzl.erp.core.service.deploymentOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.ReturnDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderMaterial;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.deploymentOrder.DeploymentOrderService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.*;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 13:58
 */
@Service("deploymentOrderService")
public class DeploymentOrderServiceImpl implements DeploymentOrderService {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createDeploymentOrder(DeploymentOrder deploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        String verifyCode = verifyDeploymentOrderInfo(deploymentOrder);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(deploymentOrder.getSrcWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }


        DeploymentOrderDO deploymentOrderDO = ConverterUtil.convert(deploymentOrder, DeploymentOrderDO.class);
        deploymentOrderDO.setDeploymentOrderNo(generateNoSupport.generateDeploymentOrderNo(currentTime, deploymentOrderDO.getSrcWarehouseId(), deploymentOrderDO.getTargetWarehouseId()));
        deploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
        deploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderDO.setCreateTime(currentTime);
        deploymentOrderMapper.save(deploymentOrderDO);
        saveDeploymentOrderProduct(deploymentOrderDO.getDeploymentOrderNo(), ConverterUtil.convertList(deploymentOrder.getDeploymentOrderProductList(), DeploymentOrderProductDO.class), loginUser, currentTime);
        saveDeploymentOrderMaterial(deploymentOrderDO.getDeploymentOrderNo(), ConverterUtil.convertList(deploymentOrder.getDeploymentOrderMaterialList(), DeploymentOrderMaterialDO.class), loginUser, currentTime);

        DeploymentOrderDO newestDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderDO.getDeploymentOrderNo());
        for (DeploymentOrderProductDO deploymentOrderProductDO : newestDeploymentOrderDO.getDeploymentOrderProductDOList()) {
            deploymentOrderDO.setTotalProductCount(deploymentOrderDO.getTotalProductCount() == null ? deploymentOrderProductDO.getDeploymentProductSkuCount() : (deploymentOrderDO.getTotalProductCount() + deploymentOrderProductDO.getDeploymentProductSkuCount()));
            deploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderProductDO.getDeploymentProductAmount()));
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : newestDeploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            deploymentOrderDO.setTotalMaterialCount(deploymentOrderDO.getTotalMaterialCount() == null ? deploymentOrderMaterialDO.getDeploymentProductMaterialCount() : (deploymentOrderDO.getTotalMaterialCount() + deploymentOrderMaterialDO.getDeploymentProductMaterialCount()));
            deploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalMaterialAmount(), deploymentOrderMaterialDO.getDeploymentMaterialAmount()));
        }
        deploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderDO.getTotalMaterialAmount()));
        deploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(deploymentOrderDO.getTotalOrderAmount(), deploymentOrderDO.getTotalDiscountAmount()));
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setResult(deploymentOrderDO.getDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void saveDeploymentOrderProduct(String deploymentOrderNo, List<DeploymentOrderProductDO> deploymentOrderProductDOList, User loginUser, Date currentTime) {

        Map<String, DeploymentOrderProductDO> saveDeploymentOrderProductDOMap = new HashMap<>();
        Map<String, DeploymentOrderProductDO> updateDeploymentOrderProductDOMap = new HashMap<>();
        List<DeploymentOrderProductDO> dbDeploymentOrderProductDOList = deploymentOrderProductMapper.findByDeploymentOrderNo(deploymentOrderNo);
        Map<String, DeploymentOrderProductDO> dbDeploymentOrderProductDOMap = ListUtil.listToMap(dbDeploymentOrderProductDOList, "deploymentProductSkuId","isNew");
        if (CollectionUtil.isNotEmpty(deploymentOrderProductDOList)) {
            for (DeploymentOrderProductDO deploymentOrderProductDO : deploymentOrderProductDOList) {
                String productRecordKey = deploymentOrderProductDO.getDeploymentProductSkuId() + "" + deploymentOrderProductDO.getIsNew();
                if (dbDeploymentOrderProductDOMap.get(productRecordKey) != null) {
                    updateDeploymentOrderProductDOMap.put(productRecordKey, deploymentOrderProductDO);
                    dbDeploymentOrderProductDOMap.remove(productRecordKey);
                } else {
                    saveDeploymentOrderProductDOMap.put(productRecordKey, deploymentOrderProductDO);
                }
            }
        }

        if (saveDeploymentOrderProductDOMap.size() > 0) {
            List<DeploymentOrderProductDO> saveList = new ArrayList<>();
            for (Map.Entry<String, DeploymentOrderProductDO> entry : saveDeploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(deploymentOrderProductDO.getDeploymentProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                deploymentOrderProductDO.setDeploymentProductAmount(BigDecimalUtil.mul(deploymentOrderProductDO.getDeploymentProductUnitAmount(), new BigDecimal(deploymentOrderProductDO.getDeploymentProductSkuCount())));
                deploymentOrderProductDO.setDeploymentProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                deploymentOrderProductDO.setDeploymentOrderNo(deploymentOrderNo);
                deploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setCreateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductDO.setCreateTime(currentTime);
                saveList.add(deploymentOrderProductDO);
            }
            deploymentOrderProductMapper.saveList(saveList);
        }

        if (updateDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<String, DeploymentOrderProductDO> entry : updateDeploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                DeploymentOrderProductDO oldDeploymentOrderProductDO = deploymentOrderProductMapper.findByDeploymentOrderNoAndSkuId(deploymentOrderNo, deploymentOrderProductDO.getDeploymentProductSkuId());
                if (oldDeploymentOrderProductDO == null) {
                    throw new BusinessException(ErrorCode.RECORD_NOT_EXISTS);
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(deploymentOrderProductDO.getDeploymentProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                deploymentOrderProductDO.setId(oldDeploymentOrderProductDO.getId());
                deploymentOrderProductDO.setDeploymentProductAmount(BigDecimalUtil.mul(deploymentOrderProductDO.getDeploymentProductUnitAmount(), new BigDecimal(deploymentOrderProductDO.getDeploymentProductSkuCount())));
                deploymentOrderProductDO.setDeploymentProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductMapper.update(deploymentOrderProductDO);
            }
        }

        if (dbDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<String, DeploymentOrderProductDO> entry : dbDeploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                deploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductMapper.update(deploymentOrderProductDO);
            }
        }
    }

    private void saveDeploymentOrderMaterial(String deploymentOrderNo, List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList, User loginUser, Date currentTime) {

        Map<String, DeploymentOrderMaterialDO> saveDeploymentOrderMaterialDOMap = new HashMap<>();
        Map<String, DeploymentOrderMaterialDO> updateDeploymentOrderMaterialDOMap = new HashMap<>();
        List<DeploymentOrderMaterialDO> dbDeploymentOrderMaterialDOList = deploymentOrderMaterialMapper.findByDeploymentOrderNo(deploymentOrderNo);
        Map<String, DeploymentOrderMaterialDO> dbDeploymentOrderMaterialDOMap = ListUtil.listToMap(dbDeploymentOrderMaterialDOList, "deploymentMaterialId", "isNew" );
        if (CollectionUtil.isNotEmpty(deploymentOrderMaterialDOList)) {
            for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : deploymentOrderMaterialDOList) {
                String materialRecordKey = deploymentOrderMaterialDO.getDeploymentMaterialId() + "-" + deploymentOrderMaterialDO.getIsNew();
                if (dbDeploymentOrderMaterialDOMap.get(materialRecordKey) != null) {
                    updateDeploymentOrderMaterialDOMap.put(materialRecordKey, deploymentOrderMaterialDO);
                    dbDeploymentOrderMaterialDOMap.remove(materialRecordKey);
                } else {
                    saveDeploymentOrderMaterialDOMap.put(materialRecordKey, deploymentOrderMaterialDO);
                }
            }
        }

        if (saveDeploymentOrderMaterialDOMap.size() > 0) {
            List<DeploymentOrderMaterialDO> saveList = new ArrayList<>();
            for (Map.Entry<String, DeploymentOrderMaterialDO> entry : saveDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(deploymentOrderMaterialDO.getDeploymentMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                deploymentOrderMaterialDO.setDeploymentMaterialAmount(BigDecimalUtil.mul(deploymentOrderMaterialDO.getDeploymentMaterialUnitAmount(), new BigDecimal(deploymentOrderMaterialDO.getDeploymentProductMaterialCount())));
                deploymentOrderMaterialDO.setDeploymentProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                deploymentOrderMaterialDO.setDeploymentOrderNo(deploymentOrderNo);
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialDO.setCreateTime(currentTime);
                saveList.add(deploymentOrderMaterialDO);
            }
            deploymentOrderMaterialMapper.saveList(saveList);
        }

        if (updateDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<String, DeploymentOrderMaterialDO> entry : updateDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(deploymentOrderMaterialDO.getDeploymentMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                deploymentOrderMaterialDO.setDeploymentMaterialAmount(BigDecimalUtil.mul(deploymentOrderMaterialDO.getDeploymentMaterialUnitAmount(), new BigDecimal(deploymentOrderMaterialDO.getDeploymentProductMaterialCount())));
                deploymentOrderMaterialDO.setDeploymentProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialMapper.update(deploymentOrderMaterialDO);
            }
        }

        if (dbDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<String, DeploymentOrderMaterialDO> entry : dbDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialMapper.update(deploymentOrderMaterialDO);
            }
        }
    }

    private String verifyDeploymentOrderInfo(DeploymentOrder deploymentOrder) {
        if (deploymentOrder == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (!DeploymentType.inThisScope(deploymentOrder.getDeploymentType())) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        if (CollectionUtil.isEmpty(deploymentOrder.getDeploymentOrderProductList())
                && CollectionUtil.isEmpty(deploymentOrder.getDeploymentOrderMaterialList())) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        if (deploymentOrder.getSrcWarehouseId() == null
                || deploymentOrder.getTargetWarehouseId() == null) {
            return ErrorCode.WAREHOUSE_ID_NOT_NULL;
        }
        if (deploymentOrder.getSrcWarehouseId().equals(deploymentOrder.getTargetWarehouseId())) {
            return ErrorCode.DEPLOYMENT_ORDER_WAREHOUSE_NOT_SAME;
        }
        WarehouseDO srcWarehouseDO = warehouseMapper.findById(deploymentOrder.getSrcWarehouseId());
        WarehouseDO targetWarehouseDO = warehouseMapper.findById(deploymentOrder.getSrcWarehouseId());
        if (srcWarehouseDO == null
                || targetWarehouseDO == null) {
            return ErrorCode.WAREHOUSE_NOT_EXISTS;
        }

        if (CollectionUtil.isNotEmpty(deploymentOrder.getDeploymentOrderProductList())) {
            for (DeploymentOrderProduct deploymentOrderProduct : deploymentOrder.getDeploymentOrderProductList()) {
                if (deploymentOrderProduct.getDeploymentProductSkuCount() == null) {
                    return ErrorCode.PARAM_IS_ERROR;
                }
                if (deploymentOrderProduct.getDeploymentProductUnitAmount() == null
                        || BigDecimalUtil.compare(deploymentOrderProduct.getDeploymentProductUnitAmount(), BigDecimal.ZERO) <= 0) {
                    return ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO;
                }

                ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
                productEquipmentQueryParam.setSkuId(deploymentOrderProduct.getDeploymentProductSkuId());
                productEquipmentQueryParam.setCurrentWarehouseId(deploymentOrder.getSrcWarehouseId());

                Map<String, Object> maps = new HashMap<>();
                maps.put("start", 0);
                maps.put("pageSize", Integer.MAX_VALUE);
                maps.put("productEquipmentQueryParam", productEquipmentQueryParam);
                Integer productEquipmentCount = productEquipmentMapper.listCount(maps);
                // 库房商品库存不足
                if (productEquipmentCount == null || deploymentOrderProduct.getDeploymentProductSkuCount() > productEquipmentCount) {
                    return ErrorCode.DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_STOCK_NOT_ENOUGH;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(deploymentOrder.getDeploymentOrderMaterialList())) {
            for (DeploymentOrderMaterial deploymentOrderMaterial : deploymentOrder.getDeploymentOrderMaterialList()) {
                if (deploymentOrderMaterial.getDeploymentProductMaterialCount() == null) {
                    return ErrorCode.PARAM_IS_ERROR;
                }
                if (deploymentOrderMaterial.getDeploymentMaterialUnitAmount() == null
                        || BigDecimalUtil.compare(deploymentOrderMaterial.getDeploymentMaterialUnitAmount(), BigDecimal.ZERO) <= 0) {
                    return ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO;
                }
                BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
                bulkMaterialQueryParam.setMaterialId(deploymentOrderMaterial.getDeploymentMaterialId());
                bulkMaterialQueryParam.setCurrentWarehouseId(deploymentOrder.getSrcWarehouseId());

                Map<String, Object> maps = new HashMap<>();
                maps.put("start", 0);
                maps.put("pageSize", Integer.MAX_VALUE);
                maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
                Integer bulkMaterialCount = bulkMaterialMapper.listCount(maps);
                // 物料库存不足
                if (bulkMaterialCount == null || deploymentOrderMaterial.getDeploymentProductMaterialCount() > bulkMaterialCount) {
                    return ErrorCode.DEPLOYMENT_ORDER_BULK_MATERIAL_STOCK_NOT_ENOUGH;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateDeploymentOrder(DeploymentOrder deploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();

        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrder.getDeploymentOrderNo());
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        String verifyCode = verifyDeploymentOrderInfo(deploymentOrder);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(deploymentOrder.getSrcWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }

        DeploymentOrderDO deploymentOrderDO = ConverterUtil.convert(deploymentOrder, DeploymentOrderDO.class);
        deploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderDO.setCreateTime(currentTime);
        deploymentOrderMapper.update(deploymentOrderDO);
        saveDeploymentOrderProduct(deploymentOrderDO.getDeploymentOrderNo(), ConverterUtil.convertList(deploymentOrder.getDeploymentOrderProductList(), DeploymentOrderProductDO.class), loginUser, currentTime);
        saveDeploymentOrderMaterial(deploymentOrderDO.getDeploymentOrderNo(), ConverterUtil.convertList(deploymentOrder.getDeploymentOrderMaterialList(), DeploymentOrderMaterialDO.class), loginUser, currentTime);

        DeploymentOrderDO newestDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderDO.getDeploymentOrderNo());
        for (DeploymentOrderProductDO deploymentOrderProductDO : newestDeploymentOrderDO.getDeploymentOrderProductDOList()) {
            deploymentOrderDO.setTotalProductCount(deploymentOrderDO.getTotalProductCount() == null ? deploymentOrderProductDO.getDeploymentProductSkuCount() : (deploymentOrderDO.getTotalProductCount() + deploymentOrderProductDO.getDeploymentProductSkuCount()));
            deploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderProductDO.getDeploymentProductAmount()));
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : newestDeploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            deploymentOrderDO.setTotalMaterialCount(deploymentOrderDO.getTotalMaterialCount() == null ? deploymentOrderMaterialDO.getDeploymentProductMaterialCount() : (deploymentOrderDO.getTotalMaterialCount() + deploymentOrderMaterialDO.getDeploymentProductMaterialCount()));
            deploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalMaterialAmount(), deploymentOrderMaterialDO.getDeploymentMaterialAmount()));
        }
        deploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderDO.getTotalMaterialAmount()));
        deploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(deploymentOrderDO.getTotalOrderAmount(), deploymentOrderDO.getTotalDiscountAmount()));
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setResult(deploymentOrderDO.getDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> commitDeploymentOrder(String deploymentOrderNo, Integer verifyUser, String commitRemark) {

        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(dbDeploymentOrderDO.getSrcWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }

        ServiceResult<String, Boolean> isMeedVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO);
        if (!ErrorCode.SUCCESS.equals(isMeedVerifyResult.getErrorCode())) {
            result.setErrorCode(isMeedVerifyResult.getErrorCode());
            return result;
        }
        if (isMeedVerifyResult.getResult()) {
            ServiceResult<String, String> workFlowResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO, deploymentOrderNo, verifyUser, commitRemark);
            if (!ErrorCode.SUCCESS.equals(workFlowResult.getErrorCode())) {
                result.setErrorCode(workFlowResult.getErrorCode());
                return result;
            }
            dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_VERIFYING);
        } else {
            dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING);
        }
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbDeploymentOrderDO.getDeploymentOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> processDeploymentOrder(ProcessDeploymentOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        if (param == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(param.getDeploymentOrderNo());
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING.equals(deploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }

        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(deploymentOrderDO.getTargetWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }

        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            ServiceResult<String, Object> addDeploymentOrderItemResult = addDeploymentOrderItem(deploymentOrderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(addDeploymentOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addDeploymentOrderItemResult.getErrorCode(), addDeploymentOrderItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            ServiceResult<String, Object> removeDeploymentOrderItemResult = removeDeploymentOrderItem(deploymentOrderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(removeDeploymentOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(removeDeploymentOrderItemResult.getErrorCode(), removeDeploymentOrderItemResult.getFormatArgs());
                return result;
            }
        }

        result.setResult(param.getDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> cancelDeploymentOrder(String deploymentOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        if (deploymentOrderNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(deploymentOrderDO.getDeploymentOrderStatus())
                && !DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING.equals(deploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        for (DeploymentOrderProductDO deploymentOrderProductDO : deploymentOrderDO.getDeploymentOrderProductDOList()) {
            List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentMapper.findByDeploymentOrderProductId(deploymentOrderProductDO.getId());
            if (CollectionUtil.isNotEmpty(deploymentOrderProductEquipmentDOList)) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_LOCK_ITEM);
                return result;
            }
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : deploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
            if (CollectionUtil.isNotEmpty(deploymentOrderMaterialBulkDOList)) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_LOCK_ITEM);
                return result;
            }
        }

        deploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_CANCEL);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(deploymentOrderNo);
        return result;
    }

    @Override
    public ServiceResult<String, String> deliveryDeploymentOrder(String deploymentOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        if (deploymentOrderNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING.equals(deploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        for (DeploymentOrderProductDO deploymentOrderProductDO : deploymentOrderDO.getDeploymentOrderProductDOList()) {
            List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentMapper.findByDeploymentOrderProductId(deploymentOrderProductDO.getId());
            if (CollectionUtil.isEmpty(deploymentOrderProductEquipmentDOList) || deploymentOrderProductDO.getDeploymentProductSkuCount() != deploymentOrderProductEquipmentDOList.size()) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_COUNT_NOT_ENOUGH);
                return result;
            }
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : deploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
            if (CollectionUtil.isEmpty(deploymentOrderMaterialBulkDOList) || deploymentOrderMaterialDO.getDeploymentProductMaterialCount() != deploymentOrderMaterialBulkDOList.size()) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_MATERIAL_BULK_COUNT_NOT_ENOUGH);
                return result;
            }
        }

        deploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_DELIVERED);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(deploymentOrderNo);
        return result;
    }

    private ServiceResult<String, Object> addDeploymentOrderItem(DeploymentOrderDO deploymentOrderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        // 处理调拨设备业务
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            WarehouseDO currentWarehouse = warehouseSupport.getAvailableWarehouse(productEquipmentDO.getCurrentWarehouseId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE, equipmentNo);
                return result;
            }
            if (!deploymentOrderDO.getSrcWarehouseId().equals(productEquipmentDO.getCurrentWarehouseId())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE, equipmentNo, currentWarehouse.getWarehouseName());
                return result;
            }
            // 需要确定，货物调拨如果要新的，可否给旧的
            Map<Integer, DeploymentOrderProductDO> deploymentOrderProductDOMap = ListUtil.listToMap(deploymentOrderDO.getDeploymentOrderProductDOList(), "deploymentProductSkuId");
            DeploymentOrderProductDO deploymentOrderProductDO = deploymentOrderProductDOMap.get(productEquipmentDO.getSkuId());
            if (deploymentOrderProductDO == null) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }
            List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentMapper.findByDeploymentOrderProductId(deploymentOrderProductDO.getId());
            if (deploymentOrderProductEquipmentDOList != null && deploymentOrderProductEquipmentDOList.size() >= deploymentOrderProductDO.getDeploymentProductSkuCount()) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_COUNT_MAX, deploymentOrderProductDO.getDeploymentProductSkuCount());
                return result;
            }
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_DEPLOYING);
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setUpdateUser(loginUserId.toString());
            productEquipmentMapper.update(productEquipmentDO);

            DeploymentOrderProductEquipmentDO deploymentOrderProductEquipmentDO = new DeploymentOrderProductEquipmentDO();
            deploymentOrderProductEquipmentDO.setDeploymentOrderId(deploymentOrderProductDO.getDeploymentOrderId());
            deploymentOrderProductEquipmentDO.setDeploymentOrderNo(deploymentOrderProductDO.getDeploymentOrderNo());
            deploymentOrderProductEquipmentDO.setDeploymentOrderProductId(deploymentOrderProductDO.getId());
            deploymentOrderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
            deploymentOrderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
            deploymentOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            deploymentOrderProductEquipmentDO.setCreateUser(loginUserId.toString());
            deploymentOrderProductEquipmentDO.setUpdateUser(loginUserId.toString());
            deploymentOrderProductEquipmentDO.setCreateTime(currentTime);
            deploymentOrderProductEquipmentDO.setUpdateTime(currentTime);
            deploymentOrderProductEquipmentMapper.save(deploymentOrderProductEquipmentDO);
        }


        // 处理调拨物料业务
        if (materialId != null) {

            Map<Integer, DeploymentOrderMaterialDO> deploymentOrderMaterialDOMap = ListUtil.listToMap(deploymentOrderDO.getDeploymentOrderMaterialDOList(), "deploymentMaterialId");
            DeploymentOrderMaterialDO deploymentOrderMaterialDO = deploymentOrderMaterialDOMap.get(materialId);
            if (deploymentOrderMaterialDO == null) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }

            // 必须是当前库房闲置的物料
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(deploymentOrderDO.getSrcWarehouseId(),materialId, materialCount, deploymentOrderMaterialDO.getIsNew());
            if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return result;
            }
            List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
            if (deploymentOrderMaterialBulkDOList != null && (deploymentOrderMaterialBulkDOList.size() + materialCount) > deploymentOrderMaterialDO.getDeploymentProductMaterialCount()) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_MATERIAL_BULK_COUNT_MAX, deploymentOrderMaterialDO.getDeploymentProductMaterialCount());
                return result;
            }

            for (int i = 0; i < materialCount; i++) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);

                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_DEPLOYING);
                bulkMaterialDO.setUpdateTime(currentTime);
                bulkMaterialDO.setUpdateUser(loginUserId.toString());
                bulkMaterialMapper.update(bulkMaterialDO);

                DeploymentOrderMaterialBulkDO deploymentOrderMaterialBulkDO = new DeploymentOrderMaterialBulkDO();
                deploymentOrderMaterialBulkDO.setDeploymentOrderId(deploymentOrderMaterialDO.getDeploymentOrderId());
                deploymentOrderMaterialBulkDO.setDeploymentOrderNo(deploymentOrderMaterialDO.getDeploymentOrderNo());
                deploymentOrderMaterialBulkDO.setDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
                deploymentOrderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
                deploymentOrderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                deploymentOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderMaterialBulkDO.setCreateUser(loginUserId.toString());
                deploymentOrderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                deploymentOrderMaterialBulkDO.setCreateTime(currentTime);
                deploymentOrderMaterialBulkDO.setUpdateTime(currentTime);
                deploymentOrderMaterialBulkMapper.save(deploymentOrderMaterialBulkDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> removeDeploymentOrderItem(DeploymentOrderDO deploymentOrderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        // 处理调拨设备业务
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_DEPLOYING.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_BUSY, equipmentNo);
                return result;
            }
            DeploymentOrderProductEquipmentDO deploymentOrderProductEquipmentDO = deploymentOrderProductEquipmentMapper.findDeploymentOrderByEquipmentNo(deploymentOrderDO.getId(), equipmentNo);
            if (deploymentOrderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }
            deploymentOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            deploymentOrderProductEquipmentDO.setUpdateUser(loginUserId.toString());
            deploymentOrderProductEquipmentDO.setUpdateTime(currentTime);
            deploymentOrderProductEquipmentMapper.update(deploymentOrderProductEquipmentDO);

            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setUpdateUser(loginUserId.toString());
            productEquipmentMapper.update(productEquipmentDO);
        }
        DeploymentOrderMaterialDO deploymentOrderMaterialDO = deploymentOrderMaterialMapper.findByDeploymentOrderNoAndMaterialId(deploymentOrderDO.getDeploymentOrderNo(), materialId);
        if (deploymentOrderMaterialDO == null) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM);
            return result;
        }
        List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
        if (CollectionUtil.isEmpty(deploymentOrderMaterialBulkDOList) || deploymentOrderMaterialBulkDOList.size() < materialCount) {
            result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
            return result;
        }
        // 处理调拨物料业务
        if (materialId != null) {
            for (int i = 0; i < materialCount; i++) {
                DeploymentOrderMaterialBulkDO deploymentOrderMaterialBulkDO = deploymentOrderMaterialBulkDOList.get(i);
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(deploymentOrderMaterialBulkDO.getBulkMaterialNo());
                deploymentOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                deploymentOrderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                deploymentOrderMaterialBulkDO.setUpdateTime(currentTime);
                deploymentOrderMaterialBulkMapper.update(deploymentOrderMaterialBulkDO);

                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDO.setUpdateTime(currentTime);
                bulkMaterialDO.setUpdateUser(loginUserId.toString());
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> confirmDeploymentOrder(String deploymentOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_DELIVERED.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }

        if (CollectionUtil.isNotEmpty(dbDeploymentOrderDO.getDeploymentOrderProductDOList())) {
            for (DeploymentOrderProductDO deploymentOrderProductDO : dbDeploymentOrderDO.getDeploymentOrderProductDOList()) {
                List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentMapper.findByDeploymentOrderProductId(deploymentOrderProductDO.getId());
                if (CollectionUtil.isNotEmpty(deploymentOrderProductEquipmentDOList)) {
                    for (DeploymentOrderProductEquipmentDO deploymentOrderProductEquipmentDO : deploymentOrderProductEquipmentDOList) {
                        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(deploymentOrderProductEquipmentDO.getEquipmentNo());
                        if (productEquipmentDO != null
                                && ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_DEPLOYING.equals(productEquipmentDO.getEquipmentStatus())
                                && dbDeploymentOrderDO.getSrcWarehouseId().equals(productEquipmentDO.getCurrentWarehouseId())) {
                            if (DeploymentType.DEPLOYMENT_TYPE_BORROW.equals(dbDeploymentOrderDO.getDeploymentType())) {
                                productEquipmentDO.setCurrentWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                            } else if (DeploymentType.DEPLOYMENT_TYPE_SELL.equals(dbDeploymentOrderDO.getDeploymentType())) {
                                productEquipmentDO.setCurrentWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                                productEquipmentDO.setOwnerWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                            }
                            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
                            productEquipmentMapper.update(productEquipmentDO);
                        }
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(dbDeploymentOrderDO.getDeploymentOrderMaterialDOList())) {
            for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : dbDeploymentOrderDO.getDeploymentOrderMaterialDOList()) {
                List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
                if (CollectionUtil.isNotEmpty(deploymentOrderMaterialBulkDOList)) {
                    for (DeploymentOrderMaterialBulkDO deploymentOrderMaterialBulkDO : deploymentOrderMaterialBulkDOList) {
                        BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(deploymentOrderMaterialBulkDO.getBulkMaterialNo());
                        if (bulkMaterialDO != null
                                && BulkMaterialStatus.BULK_MATERIAL_STATUS_DEPLOYING.equals(bulkMaterialDO.getBulkMaterialStatus())
                                && dbDeploymentOrderDO.getSrcWarehouseId().equals(bulkMaterialDO.getCurrentWarehouseId())) {
                            if (DeploymentType.DEPLOYMENT_TYPE_BORROW.equals(dbDeploymentOrderDO.getDeploymentType())) {
                                bulkMaterialDO.setCurrentWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                            } else if (DeploymentType.DEPLOYMENT_TYPE_SELL.equals(dbDeploymentOrderDO.getDeploymentType())) {
                                bulkMaterialDO.setCurrentWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                                bulkMaterialDO.setOwnerWarehouseId(dbDeploymentOrderDO.getTargetWarehouseId());
                            }
                            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                            bulkMaterialMapper.update(bulkMaterialDO);
                        }
                    }
                }
            }
        }


        dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_CONFIRM);
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbDeploymentOrderDO.getDeploymentOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Page<DeploymentOrder>> queryDeploymentOrderPage(DeploymentOrderQueryParam param) {

        ServiceResult<String, Page<DeploymentOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("deploymentOrderQueryParam", param);
        Integer totalCount = deploymentOrderMapper.listCount(maps);
        List<DeploymentOrderDO> list = deploymentOrderMapper.listPage(maps);
        Page<DeploymentOrder> page = new Page<>(ConverterUtil.convertList(list, DeploymentOrder.class), totalCount, param.getPageNo(), param.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, DeploymentOrder> queryDeploymentOrderDetail(String deploymentOrderNo) {
        ServiceResult<String, DeploymentOrder> result = new ServiceResult<>();
        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        for (DeploymentOrderProductDO deploymentOrderProductDO : deploymentOrderDO.getDeploymentOrderProductDOList()) {
            List<DeploymentOrderProductEquipmentDO> deploymentOrderProductEquipmentDOList = deploymentOrderProductEquipmentMapper.findByDeploymentOrderProductId(deploymentOrderProductDO.getId());
            deploymentOrderProductDO.setDeploymentOrderProductEquipmentDOList(deploymentOrderProductEquipmentDOList);
        }

        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : deploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
            deploymentOrderMaterialDO.setDeploymentOrderMaterialBulkDOList(deploymentOrderMaterialBulkDOList);
        }

        result.setResult(ConverterUtil.convert(deploymentOrderDO, DeploymentOrder.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> returnDeploymentOrder(ReturnDeploymentOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (param == null || param.getDeploymentOrderNo() == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(param.getDeploymentOrderNo());
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_NOT_EXISTS);
            return result;
        }

        if (CollectionUtil.isEmpty(param.getEquipmentNoList()) && (param.getMaterialCount() == null || param.getMaterialCount() < 0)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(deploymentOrderDO.getTargetWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }

        List<DeploymentOrderProductEquipmentDO> returnDeploymentOrderProductEquipmentDOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(param.getEquipmentNoList())) {
            for (String equipmentNo : param.getEquipmentNoList()) {
                DeploymentOrderProductEquipmentDO deploymentOrderProductEquipmentDO = deploymentOrderProductEquipmentMapper.findDeploymentOrderByEquipmentNo(deploymentOrderDO.getId(), equipmentNo);
                if (deploymentOrderProductEquipmentDO == null) {
                    result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_PRODUCT_EQUIPMENT);
                    return result;
                } else {
                    if (deploymentOrderProductEquipmentDO.getReturnTime() == null) {
                        returnDeploymentOrderProductEquipmentDOList.add(deploymentOrderProductEquipmentDO);
                    } else {
                        result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_HAVE_RETURNED);
                        return result;
                    }
                }
            }
        }

        List<DeploymentOrderMaterialBulkDO> returnDeploymentOrderBulkMaterialDOList = new ArrayList<>();
        if (param.getMaterialId() != null && param.getMaterialCount() > 0) {
            DeploymentOrderMaterialDO deploymentOrderMaterialDO = deploymentOrderMaterialMapper.findByDeploymentOrderNoAndMaterialId(deploymentOrderDO.getDeploymentOrderNo(), param.getMaterialId());
            List<DeploymentOrderMaterialBulkDO> deploymentOrderMaterialBulkDOList = deploymentOrderMaterialBulkMapper.findByDeploymentOrderMaterialId(deploymentOrderMaterialDO.getId());
            if (deploymentOrderMaterialBulkDOList == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_BULK_MATERIAL_NOT_ENOUGH);
                return result;
            } else {
                for (DeploymentOrderMaterialBulkDO deploymentOrderMaterialBulkDO : deploymentOrderMaterialBulkDOList) {
                    if (deploymentOrderMaterialBulkDO.getReturnTime() == null) {
                        returnDeploymentOrderBulkMaterialDOList.add(deploymentOrderMaterialBulkDO);
                    }
                }
            }
            if (returnDeploymentOrderBulkMaterialDOList.size() < param.getMaterialCount()) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_BULK_MATERIAL_NOT_ENOUGH);
                return result;
            }
        }

        for (DeploymentOrderProductEquipmentDO deploymentOrderProductEquipmentDO : returnDeploymentOrderProductEquipmentDOList) {
            deploymentOrderProductEquipmentDO.setReturnTime(currentTime);
            deploymentOrderProductEquipmentMapper.update(deploymentOrderProductEquipmentDO);
        }
        for (DeploymentOrderMaterialBulkDO deploymentOrderMaterialBulkDO : returnDeploymentOrderBulkMaterialDOList) {
            deploymentOrderMaterialBulkDO.setReturnTime(currentTime);
            deploymentOrderMaterialBulkMapper.update(deploymentOrderMaterialBulkDO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(businessNo);
        if (dbDeploymentOrderDO == null || !DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_VERIFYING.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            return false;
        }
        if (verifyResult) {
            dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING);
        } else {
            dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
        }
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);
        return true;
    }

    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;

    @Autowired
    private DeploymentOrderProductMapper deploymentOrderProductMapper;

    @Autowired
    private DeploymentOrderMaterialMapper deploymentOrderMaterialMapper;

    @Autowired
    private DeploymentOrderProductEquipmentMapper deploymentOrderProductEquipmentMapper;

    @Autowired
    private DeploymentOrderMaterialBulkMapper deploymentOrderMaterialBulkMapper;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private WarehouseSupport warehouseSupport;

    @Autowired
    private UserSupport userSupport;
}
