package com.lxzl.erp.core.service.transferOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.transferOrder.*;
import com.lxzl.erp.common.domain.transferOrder.pojo.*;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.transferOrder.TransferOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.transferOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.transferOrder.*;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:23 2018/1/3
 * @Modified By:
 */
@Service
public class TransferOrderServiceImpl implements TransferOrderService {

    private static Logger logger = LoggerFactory.getLogger(TransferOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> createTransferOrderInto(TransferOrder transferOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(transferOrder.getWarehouseId());
        if (warehouseDO == null) {
            serviceResult.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return serviceResult;
        }

        //判断转移单商品和物料不能同时为空
        if (CollectionUtil.isEmpty(transferOrder.getTransferOrderProductList()) && CollectionUtil.isEmpty(transferOrder.getTransferOrderMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_PRODUCT_AND_MATERIAL_NOT_NULL);
            return serviceResult;
        }

        TransferOrderDO transferOrderDO = ConverterUtil.convert(transferOrder, TransferOrderDO.class);
        transferOrderDO.setTransferOrderNo(generateNoSupport.generateTransferOrderNo(now,warehouseDO.getId()));
        transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT);
        transferOrderDO.setTransferOrderMode(TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_INTO);
        transferOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        transferOrderDO.setCreateTime(now);
        transferOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        transferOrderDO.setUpdateTime(now);
        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderMapper.save(transferOrderDO);

        //判断转移单商品
        if (CollectionUtil.isNotEmpty(transferOrder.getTransferOrderProductList())) {
            for (TransferOrderProduct transferOrderProduct : transferOrder.getTransferOrderProductList()) {
                //通过productSku获取productId
                ProductSkuDO productSkuDO = productSkuMapper.findById(transferOrderProduct.getProductSkuId());
                if (productSkuDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }

                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(productSkuDO.getId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                Product product = productServiceResult.getResult();
                TransferOrderProductDO transferOrderProductDO = ConverterUtil.convert(transferOrderProduct, TransferOrderProductDO.class);
                transferOrderProductDO.setProductId(productSkuDO.getProductId());
                transferOrderProductDO.setTransferOrderId(transferOrderDO.getId());
                transferOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                transferOrderProductDO.setRemark(transferOrderProduct.getRemark());
                transferOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                transferOrderProductDO.setCreateTime(now);
                transferOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                transferOrderProductDO.setUpdateTime(now);
                transferOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderProductMapper.save(transferOrderProductDO);
            }
        }

        //判断转移单配件
        if (CollectionUtil.isNotEmpty(transferOrder.getTransferOrderMaterialList())) {
            for (TransferOrderMaterial transferOrderMaterial : transferOrder.getTransferOrderMaterialList()) {

                //通过materialNo获取materialId
                MaterialDO materialDO = materialMapper.findByNo(transferOrderMaterial.getMaterialNo());
                if (materialDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(materialDO.getId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(materialServiceResult.getErrorCode());
                    return serviceResult;
                }
                Material material = materialServiceResult.getResult();
                TransferOrderMaterialDO transferOrderMaterialDO = ConverterUtil.convert(transferOrderMaterial, TransferOrderMaterialDO.class);
                transferOrderMaterialDO.setMaterialId(materialDO.getId());
                transferOrderMaterialDO.setTransferOrderId(transferOrderDO.getId());
                transferOrderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                transferOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                transferOrderMaterialDO.setCreateTime(now);
                transferOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                transferOrderMaterialDO.setUpdateTime(now);
                transferOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderMaterialMapper.save(transferOrderMaterialDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> createTransferOrderOut(TransferOrder transferOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        WarehouseDO userWarehouseDO = warehouseSupport.getUserWarehouse(userSupport.getCurrentUserId());

        //生成转出转移单
        TransferOrderDO transferOrderDO = ConverterUtil.convert(transferOrder,TransferOrderDO.class);
        transferOrderDO.setTransferOrderNo(generateNoSupport.generateTransferOrderNo(now,userWarehouseDO.getId()));
        transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT);
        transferOrderDO.setTransferOrderMode(TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT);
        transferOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        transferOrderDO.setWarehouseId(userWarehouseDO.getId());
        transferOrderDO.setRemark(transferOrder.getRemark());
        transferOrderDO.setCreateTime(now);
        transferOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        transferOrderDO.setUpdateTime(now);
        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderMapper.save(transferOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateTransferOrderInto(TransferOrder transferOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //todo 需要判断是否转入单
        TransferOrderDO transferOrderDO = transferOrderMapper.findByNo(transferOrder.getTransferOrderNo());
        if (transferOrderDO == null){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (transferOrderDO.getTransferOrderStatus() == null || !TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(transferOrderDO.getWarehouseId());
        if (warehouseDO == null) {
            serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return serviceResult;
        }

        //判断转移单商品和物料不能同时为空
        if (CollectionUtil.isEmpty(transferOrder.getTransferOrderProductList()) && CollectionUtil.isEmpty(transferOrder.getTransferOrderMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_PRODUCT_AND_MATERIAL_NOT_NULL);
            return serviceResult;
        }

        serviceResult = updateTransferOrderProductInfo(transferOrder.getTransferOrderProductList(),transferOrderDO.getId(), userSupport.getCurrentUser(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        serviceResult = updateTransferOrderMaterialInfo(transferOrder.getTransferOrderMaterialList(), transferOrderDO.getId(), userSupport.getCurrentUser(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        transferOrderDO.setTransferOrderName(transferOrder.getTransferOrderName());
        transferOrderDO.setTransferOrderType(transferOrder.getTransferOrderType());
        transferOrderDO.setRemark(transferOrder.getRemark());
        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderDO.setUpdateTime(now);
        transferOrderMapper.update(transferOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> updateTransferOrderOut(TransferOrder transferOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //todo 判断是否是转出单
        TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrder.getTransferOrderNo());
        if (transferOrderDO == null){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (transferOrderDO.getTransferOrderStatus() == null || !TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        transferOrderDO.setTransferOrderType(transferOrder.getTransferOrderType());
        transferOrderDO.setTransferOrderName(transferOrder.getTransferOrderName());
        transferOrderDO.setRemark(transferOrder.getRemark());
        transferOrderDO.setUpdateTime(now);
        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderMapper.update(transferOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrder.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> transferOrderProductEquipmentOut(TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        TransferOrderDO transferOrderDO = transferOrderMapper.findByNo(transferOrderProductEquipmentOutParam.getTransferOrderNo());
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //首先判断该转移单的方式，以及状态
        if (!TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MODE_IS_NOT_OUT);
            return serviceResult;
        }

        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())
                && !TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(transferOrderProductEquipmentOutParam.getProductEquipmentNo());
        if (productEquipmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
            return serviceResult;
        }

        //判断设备的状态是否是空闲中
        if (!productEquipmentDO.getEquipmentStatus().equals(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE)) {
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE, productEquipmentDO.getEquipmentNo());
            return serviceResult;
        }

        //判断操作员与该转移单是否在一个仓库
        WarehouseDO userWarehouseDO = warehouseSupport.getUserWarehouse(userSupport.getCurrentUserId());
        //判断操作员是否与该转移单在同一个仓库
        if (!userWarehouseDO.getId().equals(transferOrderDO.getWarehouseId())) {
            serviceResult.setErrorCode(ErrorCode.USER_CAN_NOT_OPERATION_TRANSFER_ORDER_WAREHOUSE);
            return serviceResult;
        }

        //判断商品设备是否与操作员在同一个仓库
        if (!userWarehouseDO.getId().equals(productEquipmentDO.getCurrentWarehouseId())) {
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_AND_USER_NOT_SAME_WAREHOUSE, productEquipmentDO.getEquipmentNo());
            return serviceResult;
        }

        ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(productEquipmentDO.getSkuId());
        if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
            serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
            return serviceResult;
        }
        Product product = productServiceResult.getResult();

        //生成转移单商品表
        TransferOrderProductDO transferOrderProductDO = transferOrderProductMapper.findByTransferOrderIdAndSkuIdAndIsNew(transferOrderDO.getId(),productEquipmentDO.getSkuId(),productEquipmentDO.getIsNew());
        if (transferOrderProductDO == null){
            transferOrderProductDO = new TransferOrderProductDO();
            transferOrderProductDO.setTransferOrderId(transferOrderDO.getId());
            transferOrderProductDO.setProductId(productEquipmentDO.getProductId());
            transferOrderProductDO.setProductSkuId(productEquipmentDO.getSkuId());
            transferOrderProductDO.setProductCount(1);
            transferOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
            transferOrderProductDO.setIsNew(productEquipmentDO.getIsNew());
            transferOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            transferOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
            transferOrderProductDO.setCreateTime(now);
            transferOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderProductDO.setUpdateTime(now);
            transferOrderProductMapper.save(transferOrderProductDO);
        }else{
            transferOrderProductDO.setProductCount(transferOrderProductDO.getProductCount() + 1);
            transferOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderProductDO.setUpdateTime(now);
            transferOrderProductMapper.update(transferOrderProductDO);
        }

        //生成转移单商品设备表
        TransferOrderProductEquipmentDO transferOrderProductEquipmentDO = new TransferOrderProductEquipmentDO();
        transferOrderProductEquipmentDO.setTransferOrderId(transferOrderDO.getId());
        transferOrderProductEquipmentDO.setTransferOrderProductId(transferOrderProductDO.getId());
        transferOrderProductEquipmentDO.setProductEquipmentNo(productEquipmentDO.getEquipmentNo());
        transferOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        transferOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
        transferOrderProductEquipmentDO.setCreateTime(now);
        transferOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderProductEquipmentDO.setUpdateTime(now);
        transferOrderProductEquipmentMapper.save(transferOrderProductEquipmentDO);

        //将转移单的状态改为备货中
        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS);
            transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderDO.setUpdateTime(now);
            transferOrderMapper.update(transferOrderDO);
        }

        //锁定该商品设备的状态为转移中
        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_TRANSFER_OUTING);
        productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        productEquipmentDO.setUpdateTime(now);
        productEquipmentMapper.update(productEquipmentDO);

        //锁定该商品设备中的所有散料状态为转移中
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findByEquipmentNo(productEquipmentDO.getEquipmentNo());
        for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_TRANSFER_OUTING);
            bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bulkMaterialDO.setUpdateTime(now);
            bulkMaterialMapper.update(bulkMaterialDO);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> dumpTransferOrderProductEquipmentOut(TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        TransferOrderDO transferOrderDO = transferOrderMapper.findByNo(transferOrderProductEquipmentOutParam.getTransferOrderNo());
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //首先判断该转移单的方式，以及状态
        if (!TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MODE_IS_NOT_OUT);
            return serviceResult;
        }

        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())
                && !TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        //通过转移单ID和设备NO查找商品设备转移单
        TransferOrderProductEquipmentDO transferOrderProductEquipmentDO = transferOrderProductEquipmentMapper.findByTransferOrderIdAndEquipmentNo(transferOrderDO.getId(), transferOrderProductEquipmentOutParam.getProductEquipmentNo());
        if (transferOrderProductEquipmentDO == null){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IN_TRANSFER_ORDER_ID_NOT_EXISTS);
            return serviceResult;
        }

        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(transferOrderProductEquipmentOutParam.getProductEquipmentNo());

        //判断操作员与该转移单是否在一个仓库
        WarehouseDO userWarehouseDO = warehouseSupport.getUserWarehouse(userSupport.getCurrentUserId());
        if (!userWarehouseDO.getId().equals(transferOrderDO.getWarehouseId())){
            serviceResult.setErrorCode(ErrorCode.USER_CAN_NOT_OPERATION_TRANSFER_ORDER_WAREHOUSE);
            return serviceResult;
        }

        //处理转移单商品表
        TransferOrderProductDO transferOrderProductDO = transferOrderProductMapper.findByTransferOrderIdAndSkuIdAndIsNew(transferOrderDO.getId(),productEquipmentDO.getSkuId(),productEquipmentDO.getIsNew());
        if (transferOrderProductDO == null){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_PRODUCT_NOT_EXISTS_BY_EQUIPMENT_NO, productEquipmentDO.getEquipmentNo());
            return serviceResult;
        }

        transferOrderProductDO.setProductCount(transferOrderProductDO.getProductCount() - 1);
        //如果商品数量为零时，就该数据状态为删除
        if (transferOrderProductDO.getProductCount() == 0){
            transferOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        }
        transferOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderProductDO.setUpdateTime(now);
        transferOrderProductMapper.update(transferOrderProductDO);

        //处理转移单商品设备表
        transferOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        transferOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderProductEquipmentDO.setUpdateTime(now);
        transferOrderProductEquipmentMapper.update(transferOrderProductEquipmentDO);

        //修改该商品设备的状态为空闲中
        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        productEquipmentDO.setUpdateTime(now);
        productEquipmentMapper.update(productEquipmentDO);

        //修改该商品设备中的所有散料状态为空闲
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findByEquipmentNo(productEquipmentDO.getEquipmentNo());
        for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
            bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bulkMaterialDO.setUpdateTime(now);
            bulkMaterialMapper.update(bulkMaterialDO);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> transferOrderMaterialOut(TransferOrderMaterialOutParam transferOrderMaterialOutParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        TransferOrderDO transferOrderDO = transferOrderMapper.findByNo(transferOrderMaterialOutParam.getTransferOrderNo());
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //首先判断该转移单的方式，以及状态
        if (!TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MODE_IS_NOT_OUT);
            return serviceResult;
        }

        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())
                && !TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        MaterialDO materialDO = materialMapper.findByNo(transferOrderMaterialOutParam.getMaterialNo());
        if (materialDO == null){
            serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
            return serviceResult;
        }

        //判断操作员与该转移单是否在一个仓库
        WarehouseDO userWarehouseDO = warehouseSupport.getUserWarehouse(userSupport.getCurrentUserId());
        if (!userWarehouseDO.getId().equals(transferOrderDO.getWarehouseId())){
            serviceResult.setErrorCode(ErrorCode.USER_CAN_NOT_OPERATION_TRANSFER_ORDER_WAREHOUSE);
            return serviceResult;
        }

        //从同一个仓库中获取该物料下指定新旧的散料数量
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(transferOrderDO.getWarehouseId(), materialDO.getId(), transferOrderMaterialOutParam.getMaterialCount(), transferOrderMaterialOutParam.getIsNew());
        //判断库存是否充足
        if (bulkMaterialDOList.size() < transferOrderMaterialOutParam.getMaterialCount()){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MATERIAL_STOCK_NOT_ENOUGH);
            return serviceResult;
        }
        for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
            //判断散料是否在订单或者设备上
            if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())){
                serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_ORDER_EQUIPMENT,bulkMaterialDO.getBulkMaterialNo());
                return serviceResult;
            }
        }
        ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(materialDO.getId());
        if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(materialServiceResult.getErrorCode());
            return serviceResult;
        }
        Material material = materialServiceResult.getResult();

        //生成转移单配件表
        TransferOrderMaterialDO transferOrderMaterialDO = transferOrderMaterialMapper.findByTransferOrderIdAndMaterialNoAndIsNew(transferOrderDO.getId(), transferOrderMaterialOutParam.getMaterialNo(), transferOrderMaterialOutParam.getIsNew());
        if (transferOrderMaterialDO == null){
            transferOrderMaterialDO= ConverterUtil.convert(transferOrderMaterialOutParam,TransferOrderMaterialDO.class);
            transferOrderMaterialDO.setTransferOrderId(transferOrderDO.getId());
            transferOrderMaterialDO.setMaterialId(materialDO.getId());
            transferOrderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
            transferOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            transferOrderMaterialDO.setCreateTime(now);
            transferOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialDO.setUpdateTime(now);
            transferOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialMapper.save(transferOrderMaterialDO);
        }else{
            transferOrderMaterialDO.setMaterialCount(transferOrderMaterialDO.getMaterialCount() + transferOrderMaterialOutParam.getMaterialCount());
            transferOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialDO.setUpdateTime(now);
            transferOrderMaterialMapper.update(transferOrderMaterialDO);
        }

        //生成转移单配件散料表
        for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
            TransferOrderMaterialBulkDO transferOrderMaterialBulkDO = new TransferOrderMaterialBulkDO();
            transferOrderMaterialBulkDO.setTransferOrderId(transferOrderDO.getId());
            transferOrderMaterialBulkDO.setTransferOrderMaterialId(transferOrderMaterialDO.getId());
            transferOrderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
            transferOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            transferOrderMaterialBulkDO.setCreateTime(now);
            transferOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialBulkDO.setUpdateTime(now);
            transferOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialBulkMapper.save(transferOrderMaterialBulkDO);

            //散料生成转移单配件散料表，改变该散料的状态为转移中
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_TRANSFER_OUTING);
            bulkMaterialDO.setUpdateTime(now);
            bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bulkMaterialMapper.update(bulkMaterialDO);
        }

        //将转移单的状态改为备货中
        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS);
            transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderDO.setUpdateTime(now);
            transferOrderMapper.update(transferOrderDO);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> dumpTransferOrderMaterialOut(TransferOrderMaterialOutParam transferOrderMaterialOutParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        TransferOrderDO transferOrderDO = transferOrderMapper.findByNo(transferOrderMaterialOutParam.getTransferOrderNo());
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //首先判断该转移单的方式，以及状态
        if (!TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MODE_IS_NOT_OUT);
            return serviceResult;
        }

        if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())
                && !TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
            return serviceResult;
        }

        MaterialDO materialDO = materialMapper.findByNo(transferOrderMaterialOutParam.getMaterialNo());
        if (materialDO == null){
            serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
            return serviceResult;
        }

        //判断操作员与该转移单是否在一个仓库
        WarehouseDO userWarehouseDO = warehouseSupport.getUserWarehouse(userSupport.getCurrentUserId());
        if (!userWarehouseDO.getId().equals(transferOrderDO.getWarehouseId())){
            serviceResult.setErrorCode(ErrorCode.USER_CAN_NOT_OPERATION_TRANSFER_ORDER_WAREHOUSE);
            return serviceResult;
        }

        TransferOrderMaterialDO transferOrderMaterialDO = transferOrderMaterialMapper.findByTransferOrderIdAndMaterialNoAndIsNew(transferOrderDO.getId(), transferOrderMaterialOutParam.getMaterialNo(), transferOrderMaterialOutParam.getIsNew());
        if (transferOrderMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MATERIAL_NOT_EXISTS);
            return serviceResult;
        }
        if (transferOrderMaterialOutParam.getMaterialCount() == 0 ){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MATERIAL_COUNT_NOT_ZERO);
            return serviceResult;
        }

        //如果清货的数量大于原转移单配件表中物料的数量
        if (transferOrderMaterialDO.getMaterialCount() < transferOrderMaterialOutParam.getMaterialCount()){
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_MATERIAL_COUNT_NOT_ENOUGH);
            return serviceResult;
        }else{
            //如果清货的数量小于原转移单配件表中物料的数量
            //改变转移单配件表的物料数量
            if (transferOrderMaterialOutParam.getMaterialCount() - transferOrderMaterialDO.getMaterialCount() == 0){
                transferOrderMaterialDO.setMaterialCount(0);
                transferOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            }else{
                transferOrderMaterialDO.setMaterialCount(transferOrderMaterialDO.getMaterialCount()- transferOrderMaterialOutParam.getMaterialCount());
            }
            transferOrderMaterialDO.setRemark(transferOrderMaterialOutParam.getRemark());
            transferOrderMaterialDO.setUpdateTime(now);
            transferOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMaterialMapper.update(transferOrderMaterialDO);

            //改变转移单配件散料表
            List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList = transferOrderMaterialBulkMapper.findByTransferOrderIdAndTransferOrderMaterialId(transferOrderMaterialDO.getTransferOrderId(),transferOrderMaterialDO.getId());
            for(int i = 0; i< transferOrderMaterialOutParam.getMaterialCount(); i++){
                TransferOrderMaterialBulkDO transferOrderMaterialBulkDO = transferOrderMaterialBulkDOList.get(i);
                transferOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                transferOrderMaterialBulkDO.setUpdateTime(now);
                transferOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderMaterialBulkMapper.update(transferOrderMaterialBulkDO);

                //更改对应的散料的状态为空闲
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(transferOrderMaterialBulkDO.getBulkMaterialNo());
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDO.setUpdateTime(now);
                bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> cancelTransferOrder(TransferOrder transferOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrder.getTransferOrderNo());
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //取消转入单
        if (TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_INTO.equals(transferOrderDO.getTransferOrderMode())){
            if (transferOrderDO.getTransferOrderStatus() == null || !TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())) {
                serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
                return serviceResult;
            }

            //强制取消
            if (TransferOrderStatus.TRANSFER_ORDER_STATUS_VERIFYING.equals(transferOrderDO.getTransferOrderStatus())){
                ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_TRANSFER_IN_ORDER, transferOrderDO.getTransferOrderNo());
                if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                    return cancelWorkFlowResult;
                }
            }
        }

        //取消转出单
        if (TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            if (transferOrderDO.getTransferOrderStatus() == null ||
                    (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())
                     && !TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus()))) {
                serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
                return serviceResult;
            }

            //如果是备货中取消
            if (TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())){
                if (CollectionUtil.isNotEmpty(transferOrderDO.getTransferOrderProductDOList())){
                    cancelProductEquipment(transferOrderDO.getId(),userSupport.getCurrentUser(),now);
                }
                if (CollectionUtil.isNotEmpty(transferOrderDO.getTransferOrderMaterialDOList())){
                    cancelBulkMaterial(transferOrderDO.getId(),userSupport.getCurrentUser(),now);
                }
            }

            //如果是审批中,强制取消
            if (TransferOrderStatus.TRANSFER_ORDER_STATUS_VERIFYING.equals(transferOrderDO.getTransferOrderStatus())){
                ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_TRANSFER_OUT_ORDER, transferOrderDO.getTransferOrderNo());
                if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                    return cancelWorkFlowResult;
                }
                if (CollectionUtil.isNotEmpty(transferOrderDO.getTransferOrderProductDOList())){
                    cancelProductEquipment(transferOrderDO.getId(),userSupport.getCurrentUser(),now);
                }
                if (CollectionUtil.isNotEmpty(transferOrderDO.getTransferOrderMaterialDOList())){
                    cancelBulkMaterial(transferOrderDO.getId(),userSupport.getCurrentUser(),now);
                }
            }
        }

        transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_CANCEL);
        transferOrderDO.setRemark(transferOrder.getRemark());
        transferOrderDO.setUpdateTime(now);
        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        transferOrderMapper.update(transferOrderDO);


        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
        return serviceResult;
    }

    private void cancelBulkMaterial(Integer transferOrderId, User currentUser, Date now) {
        List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList = transferOrderMaterialBulkMapper.findByTransferOrderId(transferOrderId);
        for (TransferOrderMaterialBulkDO transferOrderMaterialBulkDO : transferOrderMaterialBulkDOList){
            //更改对应的散料的状态为空闲
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(transferOrderMaterialBulkDO.getBulkMaterialNo());
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
            bulkMaterialDO.setUpdateTime(now);
            bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bulkMaterialMapper.update(bulkMaterialDO);
        }
    }

    private void cancelProductEquipment(Integer transferOrderId, User currentUser, Date now) {
        List<TransferOrderProductEquipmentDO> TransferOrderProductEquipmentDOList = transferOrderProductEquipmentMapper.findByTransferOrderId(transferOrderId);
        for (TransferOrderProductEquipmentDO transferOrderProductEquipmentDO : TransferOrderProductEquipmentDOList){
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(transferOrderProductEquipmentDO.getProductEquipmentNo());

            //修改该商品设备的状态为空闲中
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            productEquipmentDO.setUpdateTime(now);
            productEquipmentMapper.update(productEquipmentDO);

            //修改该商品设备中的所有散料状态为空闲
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findByEquipmentNo(productEquipmentDO.getEquipmentNo());
            for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bulkMaterialDO.setUpdateTime(now);
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> commitTransferOrder(String transferOrderNo, Integer verifyUser, String commitRemark) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrderNo);
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //只有创建维修单本人可以提交
        if (!transferOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            serviceResult.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return serviceResult;
        }

        ServiceResult<String, Boolean> needVerifyResult = new ServiceResult<>();

        if (TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_INTO.equals(transferOrderDO.getTransferOrderMode())){
            //转入只能状态为初始化
            if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT.equals(transferOrderDO.getTransferOrderStatus())) {
                serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_INTO_STATUS_NEED_INIT);
                return serviceResult;
            }
           //提交转入的转移单,判断是否需要审核
            needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_TRANSFER_IN_ORDER);
        }else{
            //转出单是备货中的才能进行提交审核操作
            if (!TransferOrderStatus.TRANSFER_ORDER_STATUS_CHOICE_GOODS.equals(transferOrderDO.getTransferOrderStatus())) {
                serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_OUT_STATUS_NEED_CHOICE_GOODS);
                return serviceResult;
            }
            if (CollectionUtil.isEmpty(transferOrderDO.getTransferOrderProductDOList()) && CollectionUtil.isEmpty(transferOrderDO.getTransferOrderMaterialDOList())){
                serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_OUT_NOT_NULL);
                return serviceResult;
            }
            //提交转出的转移单，判断是否需要审核
            needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_TRANSFER_OUT_ORDER);
        }

        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            serviceResult.setErrorCode(needVerifyResult.getErrorCode());
            return serviceResult;
        } else if (needVerifyResult.getResult()) {
            if (verifyUser == null) {
                serviceResult.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return serviceResult;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = new ServiceResult<>();
            //转入的转移单审核
            if(TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_INTO.equals(transferOrderDO.getTransferOrderMode())){
                verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_TRANSFER_IN_ORDER, transferOrderNo, verifyUser, commitRemark);
            }else{
                //转出的转移单审核
                verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_TRANSFER_OUT_ORDER, transferOrderNo, verifyUser, commitRemark);
            }
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_VERIFYING);
                transferOrderDO.setUpdateTime(now);
                transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderMapper.update(transferOrderDO);
                return verifyResult;
            } else {
                serviceResult.setErrorCode(verifyResult.getErrorCode());
                return serviceResult;
            }
        } else {
            transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_SUCCESS);
            transferOrderDO.setUpdateTime(now);
            transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMapper.update(transferOrderDO);
            serviceResult = updateTransferOrderRelevantStatus(transferOrderDO, now);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(serviceResult.getErrorCode());
                return serviceResult;
            }

            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(transferOrderDO.getTransferOrderNo());
            return serviceResult;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, String transferOrderNo) {
        try {
            ServiceResult<String,String> serviceResult = new ServiceResult<>();
            Date now = new Date();
            TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrderNo);
            if (transferOrderDO == null || !TransferOrderStatus.TRANSFER_ORDER_STATUS_VERIFYING.equals(transferOrderDO.getTransferOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                //审核通过
                transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_SUCCESS);
                serviceResult = updateTransferOrderRelevantStatus(transferOrderDO,now);
                if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                    return false;
                }
            } else {
                transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_INIT);
            }
            transferOrderDO.setUpdateTime(new Date());
            transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            transferOrderMapper.update(transferOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("审批设备转移单通知失败： {}", transferOrderNo);
            return false;
        }
    }

//    @Override
//    public ServiceResult<String,String> endTransferOrder(TransferOrder transferOrder) {
//        ServiceResult<String,String> serviceResult = new ServiceResult<>();
//        Date now = new Date();
//
//        TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrder.getTransferOrderNo());
//        if (transferOrderDO == null){
//            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
//            return serviceResult;
//        }
//
//        if (transferOrderDO.getTransferOrderStatus() == null && !TransferOrderStatus.TRANSFER_ORDER_STATUS_END.equals(transferOrderDO.getTransferOrderStatus())){
//            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_STATUS_IS_ERROR);
//            return serviceResult;
//        }
//
//        transferOrderDO.setTransferOrderStatus(TransferOrderStatus.TRANSFER_ORDER_STATUS_END);
//        transferOrderDO.setRemark(transferOrder.getRemark());
//        transferOrderDO.setUpdateTime(new Date());
//        transferOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
//        transferOrderMapper.update(transferOrderDO);
//
//        serviceResult.setErrorCode(ErrorCode.SUCCESS);
//        serviceResult.setResult(transferOrderDO.getTransferOrderNo());
//        return serviceResult;
//    }

    @Override
    public ServiceResult<String, Page<TransferOrder>> pageTransferOrder(TransferOrderQueryParam transferOrderQueryParam) {
        ServiceResult<String, Page<TransferOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(transferOrderQueryParam.getPageNo(), transferOrderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("transferOrderQueryParam", transferOrderQueryParam);

        Integer totalCount = transferOrderMapper.findTransferOrderCountByParams(maps);
        List<TransferOrderDO> transferOrderDOList = transferOrderMapper.findTransferOrderByParams(maps);
        List<TransferOrder> transferOrderList = ConverterUtil.convertList(transferOrderDOList, TransferOrder.class);
        Page<TransferOrder> page = new Page<>(transferOrderList, totalCount, transferOrderQueryParam.getPageNo(), transferOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, TransferOrder> detailTransferOrderByNo(String transferOrderNo) {
        ServiceResult<String, TransferOrder> serviceResult = new ServiceResult<>();

        TransferOrderDO transferOrderDO = transferOrderMapper.findDetailByNo(transferOrderNo);
        if (transferOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.TRANSFER_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        TransferOrder transferOrder = ConverterUtil.convert(transferOrderDO, TransferOrder.class);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(transferOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,  Page<TransferOrderProductEquipment>> detailTransferOrderProductEquipmentById(TransferOrderProductEquipmentQueryParam transferOrderProductEquipmentQueryParam) {
        ServiceResult<String,   Page<TransferOrderProductEquipment>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(transferOrderProductEquipmentQueryParam.getPageNo(), transferOrderProductEquipmentQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("transferOrderProductEquipmentQueryParam", transferOrderProductEquipmentQueryParam);

        Integer totalCount = transferOrderProductEquipmentMapper.findTransferOrderProductEquipmentCountByParams(maps);
        List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList = transferOrderProductEquipmentMapper.findTransferOrderProductEquipmentByParams(maps);
        List<TransferOrderProductEquipment> transferOrderProductEquipmentList = ConverterUtil.convertList(transferOrderProductEquipmentDOList, TransferOrderProductEquipment.class);
        Page<TransferOrderProductEquipment> page = new Page<>(transferOrderProductEquipmentList, totalCount, transferOrderProductEquipmentQueryParam.getPageNo(), transferOrderProductEquipmentQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,Page<TransferOrderMaterialBulk>> detailTransferOrderMaterialBulkById(TransferOrderMaterialBulkQueryParam transferOrderMaterialBulkQueryParam) {
        ServiceResult<String,Page<TransferOrderMaterialBulk>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(transferOrderMaterialBulkQueryParam.getPageNo(), transferOrderMaterialBulkQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("transferOrderMaterialBulkQueryParam", transferOrderMaterialBulkQueryParam);

        Integer totalCount = transferOrderMaterialBulkMapper.findTransferOrderMaterialBulkCountByParams(maps);
        List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList = transferOrderMaterialBulkMapper.findTransferOrderMaterialBulkByParams(maps);
        List<TransferOrderMaterialBulk> transferOrderMaterialBulkList = ConverterUtil.convertList(transferOrderMaterialBulkDOList, TransferOrderMaterialBulk.class);
        Page<TransferOrderMaterialBulk> page = new Page<>(transferOrderMaterialBulkList, totalCount, transferOrderMaterialBulkQueryParam.getPageNo(), transferOrderMaterialBulkQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }


    private ServiceResult<String, String> updateTransferOrderProductInfo(List<TransferOrderProduct> transferOrderProductList, Integer transferOrderId, User loginUser, Date now) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Map<String, TransferOrderProduct> updateTransferOrderProductMap = new HashMap<>();
        List<TransferOrderProductDO> dbTransferOrderProductDOList = transferOrderProductMapper.findByTransferOrderId(transferOrderId);
        Map<String,TransferOrderProductDO> dbTransferOrderProductDOMap = ListUtil.listToMap(dbTransferOrderProductDOList,"productSkuId","isNew");
        if (CollectionUtil.isNotEmpty(transferOrderProductList)) {
            for (TransferOrderProduct transferOrderProduct : transferOrderProductList) {
                //如果原单中有更改单的ProductSkuId
                if (dbTransferOrderProductDOMap.get(transferOrderProduct.getProductSkuId() +"-"+ transferOrderProduct.getIsNew()) != null &&
                        dbTransferOrderProductDOMap.get(transferOrderProduct.getProductSkuId() +"-"+ transferOrderProduct.getIsNew()).getProductCount().equals(transferOrderProduct.getProductCount())) {
                    //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据删除
                    dbTransferOrderProductDOMap.remove(transferOrderProduct.getProductSkuId() +"-"+ transferOrderProduct.getIsNew());
                } else {
                    //将原单和现单中都存在的数据，存入此Map
                    updateTransferOrderProductMap.put(transferOrderProduct.getProductSkuId() +"-"+ transferOrderProduct.getIsNew(), transferOrderProduct);
                }
            }
        }

        if (updateTransferOrderProductMap.size() > 0) {
            for (String transferOrderProductKey : updateTransferOrderProductMap.keySet()) {
                TransferOrderProductDO transferOrderProductDO = ConverterUtil.convert(updateTransferOrderProductMap.get(transferOrderProductKey), TransferOrderProductDO.class);
                //通过productSku获取productId
                ProductSkuDO productSkuDO = productSkuMapper.findById(transferOrderProductDO.getProductSkuId());
                if (productSkuDO == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(productSkuDO.getId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                Product product = productServiceResult.getResult();
                transferOrderProductDO.setProductId(productSkuDO.getProductId());
                transferOrderProductDO.setTransferOrderId(transferOrderId);
                transferOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                transferOrderProductDO.setIsNew(updateTransferOrderProductMap.get(transferOrderProductKey).getIsNew());
                transferOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                transferOrderProductDO.setCreateTime(now);
                transferOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                transferOrderProductDO.setUpdateTime(now);
                transferOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderProductMapper.save(transferOrderProductDO);
            }
        }

        if (dbTransferOrderProductDOMap.size() > 0) {
            for (String  transferOrderProductKey : dbTransferOrderProductDOMap.keySet()) {
                TransferOrderProductDO dbTransferOrderProductDO = dbTransferOrderProductDOMap.get(transferOrderProductKey);
                dbTransferOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                dbTransferOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                dbTransferOrderProductDO.setUpdateTime(now);
                transferOrderProductMapper.update(dbTransferOrderProductDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String, String> updateTransferOrderMaterialInfo(List<TransferOrderMaterial> transferOrderMaterialList,Integer transferOrderId, User loginUser, Date now) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Map<String, TransferOrderMaterial> updateTransferOrderMaterialMap = new HashMap<>();
        List<TransferOrderMaterialDO> dbTransferOrderMaterialDOList = transferOrderMaterialMapper.findByTransferOrderId(transferOrderId);
        Map<String, TransferOrderMaterialDO> dbTransferOrderMaterialDOMap = ListUtil.listToMap(dbTransferOrderMaterialDOList, "materialNo","isNew");
        if (CollectionUtil.isNotEmpty(transferOrderMaterialList)) {
            for (TransferOrderMaterial transferOrderMaterial : transferOrderMaterialList) {
                //如果原单中有更改单中的数据
                if (dbTransferOrderMaterialDOMap.get(transferOrderMaterial.getMaterialNo() +"-"+ transferOrderMaterial.getIsNew()) != null &&
                        dbTransferOrderMaterialDOMap.get(transferOrderMaterial.getMaterialNo() +"-"+ transferOrderMaterial.getIsNew()).getMaterialCount().equals(transferOrderMaterial.getMaterialCount())) {
                    //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据删除
                    dbTransferOrderMaterialDOMap.remove(transferOrderMaterial.getMaterialNo() +"-"+ transferOrderMaterial.getIsNew());
                } else {
                    //将原单和现单中都存在的数据，存入此Map
                    updateTransferOrderMaterialMap.put(transferOrderMaterial.getMaterialNo() +"-"+ transferOrderMaterial.getIsNew(),transferOrderMaterial);
                }
            }
        }

        if (updateTransferOrderMaterialMap.size() > 0) {
            for (String transferOrderMaterialKy : updateTransferOrderMaterialMap.keySet()) {
                TransferOrderMaterialDO transferOrderMaterialDO = ConverterUtil.convert(updateTransferOrderMaterialMap.get(transferOrderMaterialKy), TransferOrderMaterialDO.class);

                //通过materialNo获取materialId
                MaterialDO materialDO = materialMapper.findByNo(transferOrderMaterialDO.getMaterialNo());
                if (materialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(materialDO.getId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(materialServiceResult.getErrorCode());
                    return serviceResult;
                }
                Material material = materialServiceResult.getResult();
                transferOrderMaterialDO.setMaterialId(materialDO.getId());
                transferOrderMaterialDO.setTransferOrderId(transferOrderId);
                transferOrderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                transferOrderMaterialDO.setIsNew(updateTransferOrderMaterialMap.get(transferOrderMaterialKy).getIsNew());
                transferOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                transferOrderMaterialDO.setCreateTime(now);
                transferOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                transferOrderMaterialDO.setUpdateTime(now);
                transferOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                transferOrderMaterialMapper.save(transferOrderMaterialDO);
            }
        }

        if (dbTransferOrderMaterialDOMap.size() > 0) {
            for (String materialNo : dbTransferOrderMaterialDOMap.keySet()) {
                TransferOrderMaterialDO dbTransferOrderMaterialDO = dbTransferOrderMaterialDOMap.get(materialNo);
                dbTransferOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                dbTransferOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                dbTransferOrderMaterialDO.setUpdateTime(now);
                transferOrderMaterialMapper.update(dbTransferOrderMaterialDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String,String> updateTransferOrderRelevantStatus(TransferOrderDO transferOrderDO,Date now) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();

        if (TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_OUT.equals(transferOrderDO.getTransferOrderMode())){
            //将该转移单中的商品设备和散料状态改为转移出
            //先获取商品单和配件单
            List<TransferOrderProductDO> dbTransferOrderProductDOList = transferOrderDO.getTransferOrderProductDOList();
            List<TransferOrderMaterialDO> dbTransferOrderMaterialDOList = transferOrderDO.getTransferOrderMaterialDOList();

            if (CollectionUtil.isNotEmpty(dbTransferOrderProductDOList)){
                List<BulkMaterialDO> bulkMaterialDOList = new ArrayList<>();
                //获取转移单商品设备单
                List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList = transferOrderProductEquipmentMapper.findByTransferOrderId(transferOrderDO.getId());
                for (TransferOrderProductEquipmentDO transferOrderProductEquipmentDO : transferOrderProductEquipmentDOList){
                    ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(transferOrderProductEquipmentDO.getProductEquipmentNo());
                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_TRANSFER_OUT_END);
                    productEquipmentDO.setUpdateTime(now);
                    productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    productEquipmentMapper.update(productEquipmentDO);

                    //改变该设备下散料的状态
                    List<BulkMaterialDO> dbBulkMaterialDOList = bulkMaterialMapper.findByEquipmentNo(productEquipmentDO.getEquipmentNo());
                    for (BulkMaterialDO bulkMaterialDO : dbBulkMaterialDOList){
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_TRANSFER_OUT_END);
                        bulkMaterialDO.setUpdateTime(now);
                        bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                        bulkMaterialDOList.add(bulkMaterialDO);
                    }
                }
                bulkMaterialMapper.updateList(bulkMaterialDOList);
            }

            if (CollectionUtil.isNotEmpty(dbTransferOrderMaterialDOList)){
                List<BulkMaterialDO> bulkMaterialDOList = new ArrayList<>();
                //获取转移单配件散料单
                List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList = transferOrderMaterialBulkMapper.findByTransferOrderId(transferOrderDO.getId());
                for (TransferOrderMaterialBulkDO transferOrderMaterialBulkDO : transferOrderMaterialBulkDOList){
                    BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(transferOrderMaterialBulkDO.getBulkMaterialNo());
                    bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_TRANSFER_OUT_END);
                    bulkMaterialDO.setUpdateTime(now);
                    bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bulkMaterialDOList.add(bulkMaterialDO);
                }
                bulkMaterialMapper.updateList(bulkMaterialDOList);
            }
        }

        //如果转移单方式市转入，就要调用货物入库的方法
        if (TransferOrderMode.TRANSFER_ORDER_MODE_TRUN_INTO.equals(transferOrderDO.getTransferOrderMode())){
            // 进行商品入库操作
            //组装入库接口数据
            ProductInStockParam productInStockParam = new ProductInStockParam();
            productInStockParam.setTargetWarehouseId(transferOrderDO.getWarehouseId());
            productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_TRANSFER_ORDER);
            productInStockParam.setReferNo(transferOrderDO.getTransferOrderNo());
            List<ProductInStorage> productInStorageList = new ArrayList<>();
            List<MaterialInStorage> materialInStorageList = new ArrayList<>();
            List<TransferOrderProductDO> transferOrderProductDOList = transferOrderDO.getTransferOrderProductDOList();
            List<TransferOrderMaterialDO> transferOrderMaterialDOList = transferOrderDO.getTransferOrderMaterialDOList();
            //处理转移单设备
            if (CollectionUtil.isNotEmpty(transferOrderProductDOList)) {
                for (TransferOrderProductDO transferOrderProductDO : transferOrderProductDOList) {
                    ProductInStorage productInStorage = new ProductInStorage();
                    productInStorage.setProductId(transferOrderProductDO.getProductId());
                    productInStorage.setProductSkuId(transferOrderProductDO.getProductSkuId());
                    productInStorage.setProductCount(transferOrderProductDO.getProductCount());
                    productInStorage.setIsNew(transferOrderProductDO.getIsNew());
                    productInStorage.setItemReferId(transferOrderProductDO.getId());
                    productInStorage.setItemReferType(StockItemReferType.TRANSFER_ORDER_PRODUCT);

                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(transferOrderProductDO.getProductSkuId());
                    List<ProductMaterial> productMaterialList = productServiceResult.getResult().getProductSkuList().get(0).getProductMaterialList();
                    productInStorage.setProductMaterialList(productMaterialList);
                    productInStorageList.add(productInStorage);
                }
            }

            //处理转移单物料
            if (CollectionUtil.isNotEmpty(transferOrderMaterialDOList)) {
                for (TransferOrderMaterialDO transferOrderMaterialDO : transferOrderMaterialDOList) {
                    MaterialInStorage materialInStorage = new MaterialInStorage();
                    materialInStorage.setMaterialId(transferOrderMaterialDO.getMaterialId());
                    materialInStorage.setMaterialCount(transferOrderMaterialDO.getMaterialCount());
                    materialInStorage.setIsNew(transferOrderMaterialDO.getIsNew());
                    materialInStorage.setItemReferId(transferOrderMaterialDO.getId());
                    materialInStorage.setItemReferType(StockItemReferType.TRANSFER_ORDER_MATERIAL);
                    materialInStorageList.add(materialInStorage);
                }
            }

            productInStockParam.setProductInStorageList(productInStorageList);
            productInStockParam.setMaterialInStorageList(materialInStorageList);

            //调用入库接口
            ServiceResult<String, Integer> inStockResult = warehouseService.productInStock(productInStockParam);
            if (!ErrorCode.SUCCESS.equals(inStockResult.getErrorCode())) {
                serviceResult.setErrorCode(inStockResult.getErrorCode());
                return serviceResult;
            }

            //获取返回值存库ID,通过ID查询库存单
            StockOrderDO stockOrderDO = stockOrderMapper.findById(inStockResult.getResult());

            //通过存库单的编号，查询设备和散料
            //设置转移单商品设备单
            if (CollectionUtil.isNotEmpty(transferOrderProductDOList)) {
                List<StockOrderEquipmentDO> stockOrderEquipmentDOList = stockOrderEquipmentMapper.findByStockOrderNo(stockOrderDO.getStockOrderNo());
                List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList = new ArrayList<>();
                for (StockOrderEquipmentDO stockOrderEquipmentDO : stockOrderEquipmentDOList) {
                    TransferOrderProductEquipmentDO transferOrderProductEquipmentDO = new TransferOrderProductEquipmentDO();
                    transferOrderProductEquipmentDO.setTransferOrderId(transferOrderDO.getId());
                    transferOrderProductEquipmentDO.setTransferOrderProductId(stockOrderEquipmentDO.getItemReferId());
                    transferOrderProductEquipmentDO.setProductEquipmentNo(stockOrderEquipmentDO.getEquipmentNo());
                    transferOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    transferOrderProductEquipmentDO.setRemark(stockOrderEquipmentDO.getRemark());
                    transferOrderProductEquipmentDO.setCreateTime(now);
                    transferOrderProductEquipmentDO.setCreateUser(transferOrderDO.getCreateUser());
                    transferOrderProductEquipmentDO.setUpdateTime(now);
                    transferOrderProductEquipmentDO.setUpdateUser(transferOrderDO.getUpdateUser());
                    transferOrderProductEquipmentDOList.add(transferOrderProductEquipmentDO);
                }
                transferOrderProductEquipmentMapper.saveList(transferOrderProductEquipmentDOList);
            }
            //设置转移单配件散料单
            if (CollectionUtil.isNotEmpty(transferOrderMaterialDOList)) {
                List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList = stockOrderBulkMaterialMapper.findByStockOrderNo(stockOrderDO.getStockOrderNo());
                for (StockOrderBulkMaterialDO stockOrderBulkMaterialDO : stockOrderBulkMaterialDOList) {
                    if (StockItemReferType.TRANSFER_ORDER_MATERIAL.equals(stockOrderBulkMaterialDO.getItemReferType())){
                        TransferOrderMaterialBulkDO transferOrderMaterialBulkDO = new TransferOrderMaterialBulkDO();
                        transferOrderMaterialBulkDO.setTransferOrderId(transferOrderDO.getId());
                        transferOrderMaterialBulkDO.setTransferOrderMaterialId(stockOrderBulkMaterialDO.getItemReferId());
                        transferOrderMaterialBulkDO.setBulkMaterialNo(stockOrderBulkMaterialDO.getBulkMaterialNo());
                        transferOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        transferOrderMaterialBulkDO.setRemark(stockOrderBulkMaterialDO.getRemark());
                        transferOrderMaterialBulkDO.setCreateTime(now);
                        transferOrderMaterialBulkDO.setCreateUser(transferOrderDO.getCreateUser());
                        transferOrderMaterialBulkDO.setUpdateTime(now);
                        transferOrderMaterialBulkDO.setUpdateUser(transferOrderDO.getUpdateUser());
                        transferOrderMaterialBulkMapper.save(transferOrderMaterialBulkDO);
                    }
                }
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    @Autowired
    private TransferOrderMapper transferOrderMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private TransferOrderProductMapper transferOrderProductMapper;

    @Autowired
    private TransferOrderMaterialMapper transferOrderMaterialMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private StockOrderEquipmentMapper stockOrderEquipmentMapper;

    @Autowired
    private StockOrderBulkMaterialMapper stockOrderBulkMaterialMapper;

    @Autowired
    private TransferOrderProductEquipmentMapper transferOrderProductEquipmentMapper;

    @Autowired
    private TransferOrderMaterialBulkMapper transferOrderMaterialBulkMapper;

    @Autowired
    private WarehouseSupport warehouseSupport;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private MaterialService materialService;



}

