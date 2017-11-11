package com.lxzl.erp.core.service.warehouse.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehousePositionMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehousePositionDO;
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
    private ProductEquipmentMaterialMapper productEquipmentMaterialMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductEquipmentBulkMaterialMapper productEquipmentBulkMaterialMapper;

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
        Integer srcWarehouseId = productInStockParam.getSrcWarehouseId();
        Integer targetWarehouseId = productInStockParam.getTargetWarehouseId();
        Integer causeType = productInStockParam.getCauseType();
        String referNo = productInStockParam.getReferNo();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        String errorCode = verifyProductInfo(productInStorageList);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            result.setErrorCode(errorCode);
            return result;
        }
        if (targetWarehouseId == null || causeType == null || referNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (srcWarehouseId != null) {
            WarehouseDO srcWarehouseDO = warehouseMapper.findById(srcWarehouseId);
            if (srcWarehouseDO == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return result;
            }
        }

        WarehouseDO targetWarehouseDO = warehouseMapper.findById(targetWarehouseId);
        if (targetWarehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }

        Integer warehousePositionId = 0;
        List<WarehousePositionDO> warehousePositionDOList = warehousePositionMapper.findByWarehouseId(targetWarehouseId);
        if (warehousePositionDOList != null && !warehousePositionDOList.isEmpty()) {
            warehousePositionId = warehousePositionDOList.get(0).getId();
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
        stockOrderDO.setSrcWarehousePositionId(warehousePositionId);
        stockOrderDO.setTargetWarehouseId(targetWarehouseId);
        stockOrderDO.setTargetWarehousePositionId(warehousePositionId);
        stockOrderDO.setOrderStatus(StockOrderStatus.STOCK_ORDER_STATUS_OVER);
        stockOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        stockOrderDO.setUpdateUser(loginUser.getUserId().toString());
        stockOrderDO.setCreateUser(loginUser.getUserId().toString());
        stockOrderDO.setUpdateTime(currentTime);
        stockOrderDO.setCreateTime(currentTime);
        stockOrderMapper.save(stockOrderDO);

        for (ProductInStorage productInStorage : productInStorageList) {
            saveProductEquipment(stockOrderDO.getStockOrderNo(), targetWarehouseId, warehousePositionId, productInStorage, currentTime);
            ProductSkuDO productSkuDO = productSkuMapper.findById(productInStorage.getProductSkuId());
            productSkuDO.setStock(productSkuDO.getStock() + productInStorage.getProductCount());
            productSkuDO.setUpdateUser(loginUser.getUserId().toString());
            productSkuDO.setUpdateTime(currentTime);
            productSkuMapper.update(productSkuDO);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> productOutStock(List<ProductInStorage> productInStorageList) {
        ServiceResult<String, Integer> result = new ServiceResult<>();

        return result;
    }

    public String verifyProductInfo(List<ProductInStorage> productInStorageList) {

        if (productInStorageList == null || productInStorageList.isEmpty()) {
            return ErrorCode.WAREHOUSE_IN_STORAGE_LIST_NOT_NULL;
        }
        for (ProductInStorage productInStorage : productInStorageList) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(productInStorage.getProductSkuId());
            if (productSkuDO == null || !productInStorage.getProductId().equals(productSkuDO.getProductId())) {
                return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
            }
        }
        return ErrorCode.SUCCESS;
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
        Integer oldCount = productEquipmentMapper.findProductEquipmentCountByParams(maps);
        oldCount = oldCount == null ? 0 : oldCount;

        // 入库设备
        List<ProductEquipmentDO> allProductEquipmentDOList = new ArrayList<>();
        // 入库设备记录
        List<StockOrderEquipmentDO> allStockOrderEquipmentDOList = new ArrayList<>();
        // 入库设备物料
        List<ProductEquipmentMaterialDO> allProductEquipmentMaterialDOList = new ArrayList<>();
        // 入库散料（由物料产生散料）
        List<BulkMaterialDO> allBulkMaterialDOList = new ArrayList<>();
        // 设备散料
        List<ProductEquipmentBulkMaterialDO> allProductEquipmentBulkMaterialDOList = new ArrayList<>();

        for (int i = 0; i < productInStorage.getProductCount(); i++) {
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            productEquipmentDO.setEquipmentNo(GenerateNoUtil.generateEquipmentNo(currentTime, warehouseId, (oldCount + i + 1)));
            productEquipmentDO.setProductId(productInStorage.getProductId());
            productEquipmentDO.setSkuId(productInStorage.getProductSkuId());
            productEquipmentDO.setWarehouseId(warehouseId);
            productEquipmentDO.setWarehousePositionId(warehousePositionId);
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

            if (productInStorage.getProductMaterialList() != null && !productInStorage.getProductMaterialList().isEmpty()) {
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
                        bulkMaterialDO.setBulkMaterialNo(GenerateNoUtil.generateBulkMaterialNo(currentTime));
                        bulkMaterialDO.setMaterialId(productMaterial.getMaterialId());
                        bulkMaterialDO.setWarehouseId(warehouseId);
                        bulkMaterialDO.setWarehousePositionId(warehousePositionId);
                        bulkMaterialDO.setOwnerWarehouseId(warehouseId);
                        bulkMaterialDO.setOwnerWarehousePositionId(warehousePositionId);
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                        bulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        bulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                        bulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setCreateTime(currentTime);
                        allBulkMaterialDOList.add(bulkMaterialDO);

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
    }
}
