package com.lxzl.erp.core.service.warehouse.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterialOutStorage;
import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipmentOutStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.*;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.warehouse.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:48
 */
@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private WarehousePositionMapper warehousePositionMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private StockOrderEquipmentMapper stockOrderEquipmentMapper;

    @Autowired
    private StockOrderBulkMaterialMapper stockOrderBulkMaterialMapper;

    @Autowired
    private ProductEquipmentMaterialMapper productEquipmentMaterialMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductEquipmentBulkMaterialMapper productEquipmentBulkMaterialMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> addWarehouse(Warehouse warehouse) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        String verifyCode = verifyAddWarehouse(warehouse);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        WarehouseDO dbRecord = warehouseMapper.finByCompanyAndType(warehouse.getSubCompanyId(), warehouse.getWarehouseType());
        if (dbRecord != null) {
            result.setErrorCode(ErrorCode.RECORD_ALREADY_EXISTS);
            return result;
        }

        WarehouseDO warehouseDO = WarehouseConverter.convertWarehouse(warehouse);
        warehouseDO.setWarehouseNo(GenerateNoUtil.generateWarehouseNo(currentTime));
        warehouseDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        warehouseDO.setUpdateUser(loginUser.getUserId().toString());
        warehouseDO.setCreateUser(loginUser.getUserId().toString());
        warehouseDO.setUpdateTime(currentTime);
        warehouseDO.setCreateTime(currentTime);
        warehouseMapper.save(warehouseDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(warehouseDO.getWarehouseNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateWarehouse(Warehouse warehouse) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        String verifyCode = verifyAddWarehouse(warehouse);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        WarehouseDO dbRecord = warehouseMapper.finByCompanyAndType(warehouse.getSubCompanyId(), warehouse.getWarehouseType());
        if (dbRecord == null || !dbRecord.getWarehouseNo().equals(warehouse.getWarehouseNo())) {
            result.setErrorCode(ErrorCode.RECORD_ALREADY_EXISTS);
            return result;
        }

        WarehouseDO warehouseDO = WarehouseConverter.convertWarehouse(warehouse);
        warehouseDO.setWarehouseNo(GenerateNoUtil.generateWarehouseNo(currentTime));
        warehouseDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        warehouseDO.setUpdateUser(loginUser.getUserId().toString());
        warehouseDO.setCreateUser(loginUser.getUserId().toString());
        warehouseDO.setUpdateTime(currentTime);
        warehouseDO.setCreateTime(currentTime);
        warehouseMapper.update(warehouseDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(warehouseDO.getWarehouseNo());
        return result;
    }

    private String verifyAddWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (warehouse.getSubCompanyId() == null
                || warehouse.getSubCompanyType() == null
                || StringUtil.isBlank(warehouse.getWarehouseName())) {
            return ErrorCode.PARAM_IS_NOT_ENOUGH;
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Page<Warehouse>> getWarehousePage(WarehouseQueryParam param) {
        ServiceResult<String, Page<Warehouse>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("warehouseQueryParam", param);
        Integer dataCount = warehouseMapper.listCount(paramMap);
        List<WarehouseDO> dataList = warehouseMapper.listPage(paramMap);
        Page<Warehouse> page = new Page<>(WarehouseConverter.convertWarehouseDOList(dataList), dataCount, param.getPageNo(), param.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<Warehouse>> getWarehouseByCompany(Integer subCompanyId) {
        ServiceResult<String, List<Warehouse>> result = new ServiceResult<>();
        WarehouseQueryParam param = new WarehouseQueryParam();
        param.setSubCompanyId(subCompanyId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("warehouseQueryParam", param);
        List<WarehouseDO> dataList = warehouseMapper.listPage(paramMap);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(WarehouseConverter.convertWarehouseDOList(dataList));
        return result;
    }

    @Override
    public ServiceResult<String, List<Warehouse>> getWarehouseByCurrentCompany() {

        ServiceResult<String, List<Warehouse>> result = new ServiceResult<>();
        WarehouseQueryParam param = new WarehouseQueryParam();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        List<Integer> subCompanyIdList = new ArrayList<>();
        for (Role role : loginUser.getRoleList()) {
            if (SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(role.getSubCompanyType())) {
                subCompanyIdList.clear();
                break;
            }
            subCompanyIdList.add(role.getSubCompanyId());
        }
        param.setSubCompanyIdList(subCompanyIdList);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("warehouseQueryParam", param);
        List<WarehouseDO> dataList = warehouseMapper.listPage(paramMap);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(WarehouseConverter.convertWarehouseDOList(dataList));
        return result;
    }

    @Override
    public ServiceResult<String, Warehouse> getWarehouseById(Integer warehouseId) {
        ServiceResult<String, Warehouse> result = new ServiceResult<>();
        WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(WarehouseConverter.convertWarehouseDO(warehouseDO));
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> productInStock(ProductInStockParam productInStockParam) {
        List<ProductInStorage> productInStorageList = productInStockParam.getProductInStorageList();
        List<MaterialInStorage> materialInStorageList = productInStockParam.getMaterialInStorageList();
        Integer srcWarehouseId = productInStockParam.getSrcWarehouseId();
        Integer targetWarehouseId = productInStockParam.getTargetWarehouseId();
        Integer causeType = productInStockParam.getCauseType();
        String referNo = productInStockParam.getReferNo();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        String errorCode = verifyProductAndMaterialInfo(productInStorageList, materialInStorageList);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            result.setErrorCode(errorCode);
            return result;
        }
        if (targetWarehouseId == null || causeType == null || referNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        Integer targetWarehousePositionId = 0, srcWarehousePositionId = 0;
        if (srcWarehouseId != null) {
            WarehouseDO srcWarehouseDO = warehouseMapper.findById(srcWarehouseId);
            if (srcWarehouseDO == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return result;
            }
            if (CollectionUtil.isNotEmpty(srcWarehouseDO.getWarehousePositionDOList())) {
                srcWarehousePositionId = srcWarehouseDO.getWarehousePositionDOList().get(0).getId();
            }
        }

        WarehouseDO targetWarehouseDO = warehouseMapper.findById(targetWarehouseId);
        if (targetWarehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isNotEmpty(targetWarehouseDO.getWarehousePositionDOList())) {
            targetWarehousePositionId = targetWarehouseDO.getWarehousePositionDOList().get(0).getId();
        }

        StockOrderDO dbOrder = stockOrderMapper.findOrderByTypeAndRefer(causeType, referNo);
        if (dbOrder != null) {
            result.setErrorCode(ErrorCode.STOCK_ORDER_ALREADY_EXISTS);
            return result;
        }

        StockOrderDO stockOrderDO = new StockOrderDO();
        stockOrderDO.setOperationType(StockOperationType.STORCK_OPERATION_TYPE_IN);
        stockOrderDO.setStockOrderNo(GenerateNoUtil.generateStockOrderNo(currentTime));
        stockOrderDO.setCauseType(causeType);
        stockOrderDO.setReferNo(referNo);
        stockOrderDO.setSrcWarehouseId(srcWarehouseId);
        stockOrderDO.setSrcWarehousePositionId(srcWarehousePositionId);
        stockOrderDO.setTargetWarehouseId(targetWarehouseId);
        stockOrderDO.setTargetWarehousePositionId(targetWarehousePositionId);
        stockOrderDO.setOrderStatus(StockOrderStatus.STOCK_ORDER_STATUS_OVER);
        stockOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        stockOrderDO.setUpdateUser(loginUser.getUserId().toString());
        stockOrderDO.setCreateUser(loginUser.getUserId().toString());
        stockOrderDO.setUpdateTime(currentTime);
        stockOrderDO.setCreateTime(currentTime);
        stockOrderMapper.save(stockOrderDO);

        if (StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE.equals(causeType)) {
            if (CollectionUtil.isNotEmpty(productInStorageList)) {
                for (ProductInStorage productInStorage : productInStorageList) {
                    saveProductEquipment(stockOrderDO.getStockOrderNo(), targetWarehouseId, targetWarehousePositionId, productInStorage, currentTime);
                    ProductSkuDO productSkuDO = productSkuMapper.findById(productInStorage.getProductSkuId());
                    productSkuDO.setStock(productSkuDO.getStock() + productInStorage.getProductCount());
                    productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                    productSkuDO.setUpdateTime(currentTime);
                    productSkuMapper.update(productSkuDO);
                }
            }
            if (CollectionUtil.isNotEmpty(materialInStorageList)) {
                for (MaterialInStorage materialInStorage : materialInStorageList) {
                    saveBulkMaterial(stockOrderDO.getStockOrderNo(), targetWarehouseId, targetWarehousePositionId, materialInStorage, currentTime);
                    MaterialDO materialDO = materialMapper.findById(materialInStorage.getMaterialId());
                    materialDO.setStock(materialDO.getStock() + materialInStorage.getMaterialCount());
                    materialDO.setUpdateUser(loginUser.getUserId().toString());
                    materialDO.setUpdateTime(currentTime);
                    materialMapper.update(materialDO);
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(stockOrderDO.getId());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> productOutStock(ProductOutStockParam productOutStockParam) {
        List<ProductEquipmentOutStorage> productEquipmentOutStorageList = productOutStockParam.getProductEquipmentOutStorageList();
        List<BulkMaterialOutStorage> bulkMaterialOutStorageList = productOutStockParam.getBulkMaterialOutStorageList();
        Integer srcWarehouseId = productOutStockParam.getSrcWarehouseId();
        Integer targetWarehouseId = productOutStockParam.getTargetWarehouseId();
        Integer causeType = productOutStockParam.getCauseType();
        String referNo = productOutStockParam.getReferNo();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        String errorCode = verifyProductEquipmentAndBulkMaterialInfo(productEquipmentOutStorageList, bulkMaterialOutStorageList);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            result.setErrorCode(errorCode);
            return result;
        }
        if (srcWarehouseId == null || causeType == null || referNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        Integer targetWarehousePositionId = 0, srcWarehousePositionId = 0;
        WarehouseDO srcWarehouseDO = warehouseMapper.findById(srcWarehouseId);
        if (srcWarehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isNotEmpty(srcWarehouseDO.getWarehousePositionDOList())) {
            srcWarehousePositionId = srcWarehouseDO.getWarehousePositionDOList().get(0).getId();
        }

        if (targetWarehouseId != null) {
            WarehouseDO targetWarehouseDO = warehouseMapper.findById(targetWarehouseId);
            if (targetWarehouseDO == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return result;
            }
            if (CollectionUtil.isNotEmpty(targetWarehouseDO.getWarehousePositionDOList())) {
                targetWarehousePositionId = targetWarehouseDO.getWarehousePositionDOList().get(0).getId();
            }
        }


        StockOrderDO dbOrder = stockOrderMapper.findOrderByTypeAndRefer(causeType, referNo);
        if (dbOrder != null) {
            result.setErrorCode(ErrorCode.STOCK_ORDER_ALREADY_EXISTS);
            return result;
        }

        StockOrderDO stockOrderDO = new StockOrderDO();
        stockOrderDO.setOperationType(StockOperationType.STORCK_OPERATION_TYPE_OUT);
        stockOrderDO.setStockOrderNo(GenerateNoUtil.generateStockOrderNo(currentTime));
        stockOrderDO.setCauseType(causeType);
        stockOrderDO.setReferNo(referNo);
        stockOrderDO.setSrcWarehouseId(srcWarehouseId);
        stockOrderDO.setSrcWarehousePositionId(srcWarehousePositionId);
        stockOrderDO.setTargetWarehouseId(targetWarehouseId);
        stockOrderDO.setTargetWarehousePositionId(targetWarehousePositionId);
        stockOrderDO.setOrderStatus(StockOrderStatus.STOCK_ORDER_STATUS_OVER);
        stockOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        stockOrderDO.setUpdateUser(loginUser.getUserId().toString());
        stockOrderDO.setCreateUser(loginUser.getUserId().toString());
        stockOrderDO.setUpdateTime(currentTime);
        stockOrderDO.setCreateTime(currentTime);
        stockOrderMapper.save(stockOrderDO);

        if (StockCauseType.STOCK_CAUSE_TYPE_ALLOCATION.equals(causeType)) {
            if (CollectionUtil.isNotEmpty(productEquipmentOutStorageList)) {
                for (ProductEquipmentOutStorage productEquipmentOutStorage : productEquipmentOutStorageList) {
                    ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findById(productEquipmentOutStorage.getProductEquipmentId());
                    updateProductEquipment(stockOrderDO.getStockOrderNo(), srcWarehouseId, srcWarehousePositionId, targetWarehouseId, targetWarehousePositionId, productEquipmentOutStorage, currentTime);
                    ProductSkuDO productSkuDO = productSkuMapper.findById(productEquipmentDO.getSkuId());
                    productSkuDO.setStock(productSkuDO.getStock() - productEquipmentOutStorage.getProductEquipmentCount());
                    productSkuDO.setUpdateUser(loginUser.getUserId().toString());
                    productSkuDO.setUpdateTime(currentTime);
                    productSkuMapper.update(productSkuDO);
                }
            }
            if (CollectionUtil.isNotEmpty(bulkMaterialOutStorageList)) {
                for (BulkMaterialOutStorage bulkMaterialOutStorage : bulkMaterialOutStorageList) {
                    BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(bulkMaterialOutStorage.getBulkMaterialId());
                    updateBulkMaterial(stockOrderDO.getStockOrderNo(), srcWarehouseId, srcWarehousePositionId, targetWarehouseId, targetWarehousePositionId, bulkMaterialOutStorage, currentTime);
                    MaterialDO materialDO = materialMapper.findById(bulkMaterialDO.getMaterialId());
                    materialDO.setStock(materialDO.getStock() - bulkMaterialOutStorage.getBulkMaterialCount());
                    materialDO.setUpdateUser(loginUser.getUserId().toString());
                    materialDO.setUpdateTime(currentTime);
                    materialMapper.update(materialDO);
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(stockOrderDO.getId());
        return result;
    }

    public String verifyProductAndMaterialInfo(List<ProductInStorage> productInStorageList, List<MaterialInStorage> materialInStorageList) {

        if ((productInStorageList == null || productInStorageList.isEmpty())
                && (materialInStorageList == null || materialInStorageList.isEmpty())) {
            return ErrorCode.WAREHOUSE_IN_STORAGE_LIST_NOT_NULL;
        }
        if (CollectionUtil.isNotEmpty(productInStorageList)) {
            for (ProductInStorage productInStorage : productInStorageList) {
                ProductSkuDO productSkuDO = productSkuMapper.findById(productInStorage.getProductSkuId());
                if (productSkuDO == null || !productInStorage.getProductId().equals(productSkuDO.getProductId())) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(materialInStorageList)) {
            for (MaterialInStorage materialInStorage : materialInStorageList) {
                MaterialDO materialDO = materialMapper.findById(materialInStorage.getMaterialId());
                if (materialDO == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
            }
        }

        return ErrorCode.SUCCESS;
    }


    public String verifyProductEquipmentAndBulkMaterialInfo(List<ProductEquipmentOutStorage> productEquipmentOutStorageList, List<BulkMaterialOutStorage> bulkMaterialOutStorageList) {

        if ((productEquipmentOutStorageList == null || productEquipmentOutStorageList.isEmpty())
                && (bulkMaterialOutStorageList == null || bulkMaterialOutStorageList.isEmpty())) {
            return ErrorCode.WAREHOUSE_IN_STORAGE_LIST_NOT_NULL;
        }
        if (CollectionUtil.isNotEmpty(productEquipmentOutStorageList)) {
            for (ProductEquipmentOutStorage productEquipmentOutStorage : productEquipmentOutStorageList) {
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findById(productEquipmentOutStorage.getProductEquipmentId());
                if (productEquipmentDO == null) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(bulkMaterialOutStorageList)) {
            for (BulkMaterialOutStorage bulkMaterialOutStorage : bulkMaterialOutStorageList) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(bulkMaterialOutStorage.getBulkMaterialId());
                if (bulkMaterialDO == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    private void saveBulkMaterial(String stockOrderNo, Integer warehouseId, Integer warehousePositionId, MaterialInStorage materialInStorage, Date currentTime) {
        // 入库散料（由物料产生散料）
        List<BulkMaterialDO> allBulkMaterialDOList = new ArrayList<>();
        // 入库物料记录
        List<StockOrderBulkMaterialDO> allStockOrderBulkMaterialDOList = new ArrayList<>();

        BulkMaterialQueryParam param = new BulkMaterialQueryParam();
        param.setCreateStartTime(DateUtil.getBeginOfDay(currentTime));
        param.setCreateEndTime(DateUtil.getEndOfDay(currentTime));
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 1);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("bulkMaterialQueryParam", param);
        Integer oldCount = bulkMaterialMapper.listCount(maps);
        oldCount = oldCount == null ? 0 : oldCount;
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        for (int i = 0; i < materialInStorage.getMaterialCount(); i++) {
            BulkMaterialDO bulkMaterialDO = new BulkMaterialDO();
            bulkMaterialDO.setBulkMaterialNo(GenerateNoUtil.generateBulkMaterialNo(currentTime, (oldCount + i + 1)));
            bulkMaterialDO.setMaterialId(materialInStorage.getMaterialId());
            bulkMaterialDO.setCurrentWarehouseId(warehouseId);
            bulkMaterialDO.setCurrentWarehousePositionId(warehousePositionId);
            bulkMaterialDO.setOwnerWarehouseId(warehouseId);
            bulkMaterialDO.setOwnerWarehousePositionId(warehousePositionId);
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
            bulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
            bulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
            bulkMaterialDO.setUpdateTime(currentTime);
            bulkMaterialDO.setCreateTime(currentTime);
            allBulkMaterialDOList.add(bulkMaterialDO);

            StockOrderBulkMaterialDO stockOrderBulkMaterialDO = new StockOrderBulkMaterialDO();
            stockOrderBulkMaterialDO.setStockOrderNo(stockOrderNo);
            stockOrderBulkMaterialDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
            stockOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            stockOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
            stockOrderBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
            stockOrderBulkMaterialDO.setUpdateTime(currentTime);
            stockOrderBulkMaterialDO.setCreateTime(currentTime);
            allStockOrderBulkMaterialDOList.add(stockOrderBulkMaterialDO);
        }

        if (!allBulkMaterialDOList.isEmpty()) {
            bulkMaterialMapper.saveList(allBulkMaterialDOList);
        }
        if (!allStockOrderBulkMaterialDOList.isEmpty()) {
            stockOrderBulkMaterialMapper.saveList(allStockOrderBulkMaterialDOList);
        }
    }


    private void saveProductEquipment(String stockOrderNo, Integer warehouseId, Integer warehousePositionId, ProductInStorage productInStorage, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
        param.setCreateStartTime(DateUtil.getBeginOfDay(currentTime));
        param.setCreateEndTime(DateUtil.getEndOfDay(currentTime));
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 1);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("productEquipmentQueryParam", param);
        Integer productEquipmentOldCount = productEquipmentMapper.findProductEquipmentCountByParams(maps);
        productEquipmentOldCount = productEquipmentOldCount == null ? 0 : productEquipmentOldCount;


        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setCreateStartTime(DateUtil.getBeginOfDay(currentTime));
        bulkMaterialQueryParam.setCreateEndTime(DateUtil.getEndOfDay(currentTime));
        Map<String, Object> paramMaps = new HashMap<>();
        paramMaps.put("start", 1);
        paramMaps.put("pageSize", Integer.MAX_VALUE);
        paramMaps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        Integer bulkMaterialOldCount = bulkMaterialMapper.listCount(paramMaps);
        bulkMaterialOldCount = bulkMaterialOldCount == null ? 0 : bulkMaterialOldCount;

        // 入库设备
        List<ProductEquipmentDO> allProductEquipmentDOList = new ArrayList<>();
        // 入库设备记录
        List<StockOrderEquipmentDO> allStockOrderEquipmentDOList = new ArrayList<>();
        // 入库物料记录
        List<StockOrderBulkMaterialDO> allStockOrderBulkMaterialDOList = new ArrayList<>();
        // 入库设备物料
        List<ProductEquipmentMaterialDO> allProductEquipmentMaterialDOList = new ArrayList<>();
        // 入库散料（由物料产生散料）
        List<BulkMaterialDO> allBulkMaterialDOList = new ArrayList<>();
        // 设备散料
        List<ProductEquipmentBulkMaterialDO> allProductEquipmentBulkMaterialDOList = new ArrayList<>();


        for (int i = 0; i < productInStorage.getProductCount(); i++) {
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            productEquipmentDO.setEquipmentNo(GenerateNoUtil.generateEquipmentNo(currentTime, warehouseId, (productEquipmentOldCount + i + 1)));
            productEquipmentDO.setProductId(productInStorage.getProductId());
            productEquipmentDO.setSkuId(productInStorage.getProductSkuId());
            productEquipmentDO.setCurrentWarehouseId(warehouseId);
            productEquipmentDO.setCurrentWarehousePositionId(warehousePositionId);
            productEquipmentDO.setOwnerWarehouseId(warehouseId);
            productEquipmentDO.setOwnerWarehousePositionId(warehousePositionId);
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            productEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setCreateTime(currentTime);
            allProductEquipmentDOList.add(productEquipmentDO);

            StockOrderEquipmentDO stockOrderEquipmentDO = new StockOrderEquipmentDO();
            stockOrderEquipmentDO.setStockOrderNo(stockOrderNo);
            stockOrderEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
            stockOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            stockOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            stockOrderEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            stockOrderEquipmentDO.setUpdateTime(currentTime);
            stockOrderEquipmentDO.setCreateTime(currentTime);
            allStockOrderEquipmentDOList.add(stockOrderEquipmentDO);

            if (CollectionUtil.isNotEmpty(productInStorage.getProductMaterialList())) {
                for (ProductMaterial productMaterial : productInStorage.getProductMaterialList()) {
                    ProductEquipmentMaterialDO productEquipmentMaterialDO = new ProductEquipmentMaterialDO();
                    productEquipmentMaterialDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                    productEquipmentMaterialDO.setMaterialId(productMaterial.getMaterialId());
                    productEquipmentMaterialDO.setMaterialCount(productMaterial.getMaterialCount());
                    productEquipmentMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    productEquipmentMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                    productEquipmentMaterialDO.setCreateUser(loginUser.getUserId().toString());
                    productEquipmentMaterialDO.setUpdateTime(currentTime);
                    productEquipmentMaterialDO.setCreateTime(currentTime);
                    allProductEquipmentMaterialDOList.add(productEquipmentMaterialDO);
                    for (int j = 0; j < productMaterial.getMaterialCount(); j++) {
                        BulkMaterialDO bulkMaterialDO = new BulkMaterialDO();
                        bulkMaterialDO.setBulkMaterialNo(GenerateNoUtil.generateBulkMaterialNo(currentTime,bulkMaterialOldCount));
                        bulkMaterialDO.setMaterialId(productMaterial.getMaterialId());
                        bulkMaterialDO.setCurrentWarehouseId(warehouseId);
                        bulkMaterialDO.setCurrentWarehousePositionId(warehousePositionId);
                        bulkMaterialDO.setOwnerWarehouseId(warehouseId);
                        bulkMaterialDO.setOwnerWarehousePositionId(warehousePositionId);
                        bulkMaterialDO.setCurrentEquipmentNo(productEquipmentDO.getEquipmentNo());
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                        bulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        bulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                        bulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setCreateTime(currentTime);
                        allBulkMaterialDOList.add(bulkMaterialDO);
                        bulkMaterialOldCount ++ ;

                        StockOrderBulkMaterialDO stockOrderBulkMaterialDO = new StockOrderBulkMaterialDO();
                        stockOrderBulkMaterialDO.setStockOrderNo(stockOrderNo);
                        stockOrderBulkMaterialDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                        stockOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        stockOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                        stockOrderBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                        stockOrderBulkMaterialDO.setUpdateTime(currentTime);
                        stockOrderBulkMaterialDO.setCreateTime(currentTime);
                        allStockOrderBulkMaterialDOList.add(stockOrderBulkMaterialDO);

                        ProductEquipmentBulkMaterialDO productEquipmentBulkMaterialDO = new ProductEquipmentBulkMaterialDO();
                        productEquipmentBulkMaterialDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                        productEquipmentBulkMaterialDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                        productEquipmentBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        productEquipmentBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                        productEquipmentBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                        productEquipmentBulkMaterialDO.setUpdateTime(currentTime);
                        productEquipmentBulkMaterialDO.setCreateTime(currentTime);
                        allProductEquipmentBulkMaterialDOList.add(productEquipmentBulkMaterialDO);
                    }
                }
            }
        }

        if (!allProductEquipmentDOList.isEmpty()) {
            productEquipmentMapper.saveList(allProductEquipmentDOList);
        }
        if (!allStockOrderEquipmentDOList.isEmpty()) {
            stockOrderEquipmentMapper.saveList(allStockOrderEquipmentDOList);
        }
        if (!allProductEquipmentMaterialDOList.isEmpty()) {
            productEquipmentMaterialMapper.saveList(allProductEquipmentMaterialDOList);
        }
        if (!allBulkMaterialDOList.isEmpty()) {
            bulkMaterialMapper.saveList(allBulkMaterialDOList);
        }
        if (!allProductEquipmentBulkMaterialDOList.isEmpty()) {
            productEquipmentBulkMaterialMapper.saveList(allProductEquipmentBulkMaterialDOList);
        }
        if (!allStockOrderBulkMaterialDOList.isEmpty()) {
            stockOrderBulkMaterialMapper.saveList(allStockOrderBulkMaterialDOList);
        }
    }


    private void updateBulkMaterial(String stockOrderNo, Integer srcWarehouseId, Integer srcWarehousePositionId, Integer targetWarehouseId, Integer targetWarehousePositionId, BulkMaterialOutStorage bulkMaterialOutStorage, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        // 变更散料（由物料产生散料）
        List<BulkMaterialDO> allBulkMaterialDOList = new ArrayList<>();
        // 变更物料记录
        List<StockOrderBulkMaterialDO> allStockOrderBulkMaterialDOList = new ArrayList<>();

        for (int i = 0; i < bulkMaterialOutStorage.getBulkMaterialCount(); i++) {
            BulkMaterialDO bulkMaterialDO = new BulkMaterialDO();
            bulkMaterialDO.setId(bulkMaterialOutStorage.getBulkMaterialId());
            bulkMaterialDO.setCurrentWarehouseId(targetWarehouseId);
            bulkMaterialDO.setCurrentWarehousePositionId(targetWarehousePositionId);
            bulkMaterialDO.setOwnerWarehouseId(srcWarehouseId);
            bulkMaterialDO.setOwnerWarehousePositionId(srcWarehousePositionId);
            bulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
            bulkMaterialDO.setUpdateTime(currentTime);
            allBulkMaterialDOList.add(bulkMaterialDO);

            StockOrderBulkMaterialDO stockOrderBulkMaterialDO = new StockOrderBulkMaterialDO();
            stockOrderBulkMaterialDO.setStockOrderNo(stockOrderNo);
            stockOrderBulkMaterialDO.setBulkMaterialId(bulkMaterialDO.getId());
            stockOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            stockOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
            stockOrderBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
            stockOrderBulkMaterialDO.setUpdateTime(currentTime);
            stockOrderBulkMaterialDO.setCreateTime(currentTime);
            allStockOrderBulkMaterialDOList.add(stockOrderBulkMaterialDO);
        }

        if (!allBulkMaterialDOList.isEmpty()) {
            bulkMaterialMapper.updateList(allBulkMaterialDOList);
        }
        if (!allStockOrderBulkMaterialDOList.isEmpty()) {
            stockOrderBulkMaterialMapper.saveList(allStockOrderBulkMaterialDOList);
        }

    }

    private void updateProductEquipment(String stockOrderNo, Integer srcWarehouseId,Integer srcWarehousePositionId, Integer targetWarehouseId, Integer targetWarehousePositionId, ProductEquipmentOutStorage productEquipmentOutStorage, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        // 变更设备
        List<ProductEquipmentDO> allProductEquipmentDOList = new ArrayList<>();
        // 变更设备记录
        List<StockOrderEquipmentDO> allStockOrderEquipmentDOList = new ArrayList<>();
        // 变更物料记录
        List<StockOrderBulkMaterialDO> allStockOrderBulkMaterialDOList = new ArrayList<>();
        // 入库散料（由物料产生散料）
        List<BulkMaterialDO> allBulkMaterialDOList = new ArrayList<>();


        for (int i = 0; i < productEquipmentOutStorage.getProductEquipmentCount(); i++) {
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            productEquipmentDO.setId(productEquipmentOutStorage.getProductEquipmentId());
            productEquipmentDO.setCurrentWarehouseId(targetWarehouseId);
            productEquipmentDO.setCurrentWarehousePositionId(targetWarehousePositionId);
            productEquipmentDO.setOwnerWarehouseId(srcWarehouseId);
            productEquipmentDO.setOwnerWarehousePositionId(srcWarehousePositionId);
            productEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            productEquipmentDO.setUpdateTime(currentTime);
            allProductEquipmentDOList.add(productEquipmentDO);

            StockOrderEquipmentDO stockOrderEquipmentDO = new StockOrderEquipmentDO();
            stockOrderEquipmentDO.setStockOrderNo(stockOrderNo);
            stockOrderEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
            stockOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            stockOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            stockOrderEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            stockOrderEquipmentDO.setUpdateTime(currentTime);
            stockOrderEquipmentDO.setCreateTime(currentTime);
            allStockOrderEquipmentDOList.add(stockOrderEquipmentDO);

        }

        if (!allProductEquipmentDOList.isEmpty()) {
            productEquipmentMapper.saveList(allProductEquipmentDOList);
        }
        if (!allStockOrderEquipmentDOList.isEmpty()) {
            stockOrderEquipmentMapper.saveList(allStockOrderEquipmentDOList);
        }
        if (!allBulkMaterialDOList.isEmpty()) {
            bulkMaterialMapper.saveList(allBulkMaterialDOList);
        }
        if (!allStockOrderBulkMaterialDOList.isEmpty()) {
            stockOrderBulkMaterialMapper.saveList(allStockOrderBulkMaterialDOList);
        }

    }
}
