package com.lxzl.erp.core.service.peerDeploymentOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderCommitParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peer.PeerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.peer.PeerDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderProductDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.*;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 15:39 2018/1/13
 * @Modified By:
 */
@Service
public class PeerDeploymentOrderServiceImpl implements PeerDeploymentOrderService {

    private static Logger logger = LoggerFactory.getLogger(PeerDeploymentOrderServiceImpl.class);

    /**
     * 创建同行调拨单
     * @param peerDeploymentOrder
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        PeerDO peerDO = peerMapper.findById(peerDeploymentOrder.getPeerId());
        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(peerDeploymentOrder.getWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }

        PeerDeploymentOrderDO peerDeploymentOrderDO = ConverterUtil.convert(peerDeploymentOrder,PeerDeploymentOrderDO.class);
        Date expectReturnTime = peerDeploymentOrderExpectReturnTime(peerDeploymentOrderDO.getRentStartTime(), peerDeploymentOrderDO.getRentType(), peerDeploymentOrderDO.getRentTimeLength());

        AreaCityDO areaCityDO = areaCityMapper.findById(peerDO.getCity());
        peerDeploymentOrderDO.setPeerDeploymentOrderNo(generateNoSupport.generatePeerDeploymentOrderNo(currentTime,areaCityDO.getCityCode()));
        peerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
        peerDeploymentOrderDO.setExpectReturnTime(expectReturnTime);
        peerDeploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        peerDeploymentOrderDO.setCreateTime(currentTime);
        peerDeploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        peerDeploymentOrderDO.setUpdateTime(currentTime);
        peerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        peerDeploymentOrderMapper.save(peerDeploymentOrderDO);

        //产品和配件和收货信息数据
        savePeerDeploymentOrderProductInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderProductList(),PeerDeploymentOrderProductDO.class),peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser,currentTime);
        savePeerDeploymentOrderMaterialInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderMaterialList(), PeerDeploymentOrderMaterialDO.class), peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser, currentTime);
        savePeerDeploymentOrderConsignInfo(ConverterUtil.convert(peerDeploymentOrder.getPeerDeploymentOrderConsignInfo(),PeerDeploymentOrderConsignInfoDO.class), peerDeploymentOrderDO.getId(), loginUser, currentTime);
        //判断产品和配件 总数与总额
        PeerDeploymentOrderDO newestPeerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderProductDOList()) {
            peerDeploymentOrderDO.setTotalProductCount(peerDeploymentOrderDO.getTotalProductCount() == null ? peerDeploymentOrderProductDO.getProductSkuCount() : (peerDeploymentOrderDO.getTotalProductCount() + peerDeploymentOrderProductDO.getProductSkuCount()));
            peerDeploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderProductDO.getProductAmount()));
        }
        for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderMaterialDOList()) {
            peerDeploymentOrderDO.setTotalMaterialCount(peerDeploymentOrderDO.getTotalMaterialCount() == null ? peerDeploymentOrderMaterialDO.getProductMaterialCount() : (peerDeploymentOrderDO.getTotalMaterialCount() + peerDeploymentOrderMaterialDO.getProductMaterialCount()));
            peerDeploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalMaterialAmount(), peerDeploymentOrderMaterialDO.getMaterialAmount()));
        }
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderDO.getTotalMaterialAmount()));
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(peerDeploymentOrderDO.getTotalOrderAmount(), peerDeploymentOrderDO.getTotalDiscountAmount()));
        peerDeploymentOrderMapper.update(peerDeploymentOrderDO);

        result.setResult(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 修改同行调拨单
     * @param peerDeploymentOrder
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updatePeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        PeerDeploymentOrderDO peerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrder.getPeerDeploymentOrderNo());
        if(peerDeploymentOrderDO == null){
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_NOT_EXISTS);
            return result;
        }
        //判断未提交状态才能修改
        if(!PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(peerDeploymentOrderDO.getPeerDeploymentOrderStatus())){
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_NOT_EXISTS);
            return result;
        }

        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(peerDeploymentOrder.getWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }

        peerDeploymentOrderDO = ConverterUtil.convert(peerDeploymentOrder,PeerDeploymentOrderDO.class);
        Date expectReturnTime = peerDeploymentOrderExpectReturnTime(peerDeploymentOrderDO.getRentStartTime(), peerDeploymentOrderDO.getRentType(), peerDeploymentOrderDO.getRentTimeLength());

        peerDeploymentOrderDO.setExpectReturnTime(expectReturnTime);
        peerDeploymentOrderDO.setUpdateTime(currentTime);
        peerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        peerDeploymentOrderMapper.update(peerDeploymentOrderDO);

        //产品和配件和收货信息数据
        savePeerDeploymentOrderProductInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderProductList(),PeerDeploymentOrderProductDO.class),peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser,currentTime);
        savePeerDeploymentOrderMaterialInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderMaterialList(), PeerDeploymentOrderMaterialDO.class), peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser, currentTime);
        savePeerDeploymentOrderConsignInfo(ConverterUtil.convert(peerDeploymentOrder.getPeerDeploymentOrderConsignInfo(),PeerDeploymentOrderConsignInfoDO.class), peerDeploymentOrderDO.getId(), loginUser, currentTime);

        //判断产品和配件 总数与总额
        PeerDeploymentOrderDO newestPeerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderProductDOList()) {
            peerDeploymentOrderDO.setTotalProductCount(peerDeploymentOrderDO.getTotalProductCount() == null ? peerDeploymentOrderProductDO.getProductSkuCount() : (peerDeploymentOrderDO.getTotalProductCount() + peerDeploymentOrderProductDO.getProductSkuCount()));
            peerDeploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderProductDO.getProductAmount()));
        }
        for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderMaterialDOList()) {
            peerDeploymentOrderDO.setTotalMaterialCount(peerDeploymentOrderDO.getTotalMaterialCount() == null ? peerDeploymentOrderMaterialDO.getProductMaterialCount() : (peerDeploymentOrderDO.getTotalMaterialCount() + peerDeploymentOrderMaterialDO.getProductMaterialCount()));
            peerDeploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalMaterialAmount(), peerDeploymentOrderMaterialDO.getMaterialAmount()));
        }
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderDO.getTotalMaterialAmount()));
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(peerDeploymentOrderDO.getTotalOrderAmount(), peerDeploymentOrderDO.getTotalDiscountAmount()));
        peerDeploymentOrderMapper.update(peerDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        return result;
    }

    /**
     * 提交同行调拨单
     * @param peerDeploymentOrderCommitParam
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitPeerDeploymentOrder(PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        PeerDeploymentOrderDO dbPeerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderCommitParam.getPeerDeploymentOrderNo());
        if(dbPeerDeploymentOrderDO == null){
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(dbPeerDeploymentOrderDO.getPeerDeploymentOrderStatus())){
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(dbPeerDeploymentOrderDO.getWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
            return result;
        }
        //只有创建同行调拨单本人可以提交
        if (!dbPeerDeploymentOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }

        if (CollectionUtil.isEmpty(dbPeerDeploymentOrderDO.getPeerDeploymentOrderProductDOList()) && CollectionUtil.isEmpty(dbPeerDeploymentOrderDO.getPeerDeploymentOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_PRODUCT_MATERIAL_LIST_NOT_NULL);
            return result;
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (peerDeploymentOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO, dbPeerDeploymentOrderDO.getPeerDeploymentOrderNo(), peerDeploymentOrderCommitParam.getVerifyUserId(), peerDeploymentOrderCommitParam.getRemark());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                dbPeerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_VERIFYING);
                dbPeerDeploymentOrderDO.setUpdateTime(currentTime);
                dbPeerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMapper.update(dbPeerDeploymentOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            dbPeerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_PROCESSING);
            dbPeerDeploymentOrderDO.setUpdateTime(currentTime);
            dbPeerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
            peerDeploymentOrderMapper.update(dbPeerDeploymentOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }

    }

    /**
     * 确认收货同行调拨单
     * @param peerDeploymentOrderNo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> confirmPeerDeploymentOrder(String peerDeploymentOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        PeerDeploymentOrderDO dbPeerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderNo);
        if (dbPeerDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_NOT_EXISTS);
            return result;
        }
        //判别同行调拨单是否在处理中
        if (!PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_PROCESSING.equals(dbPeerDeploymentOrderDO.getPeerDeploymentOrderStatus())){
            result.setErrorCode(ErrorCode.PEER_DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }
        //进行商品与配件入库操作
        ProductInStockParam productInStockParam = new ProductInStockParam();
        productInStockParam.setTargetWarehouseId(dbPeerDeploymentOrderDO.getWarehouseId());
        productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_TRANSFER_ORDER);
        productInStockParam.setReferNo(dbPeerDeploymentOrderDO.getPeerDeploymentOrderNo());
        List<ProductInStorage> productInStorageList = new ArrayList<>();
        List<MaterialInStorage> materialInStorageList = new ArrayList<>();
        List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList = dbPeerDeploymentOrderDO.getPeerDeploymentOrderProductDOList();
        List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList = dbPeerDeploymentOrderDO.getPeerDeploymentOrderMaterialDOList();

        //处理同行调拨单商品设备
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderProductDOList)) {
            for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : peerDeploymentOrderProductDOList) {
                ProductInStorage productInStorage = new ProductInStorage();
                ProductSkuDO productSkuDO = productSkuMapper.findById(peerDeploymentOrderProductDO.getProductSkuId());
                productInStorage.setProductId(productSkuDO.getProductId());
                productInStorage.setProductSkuId(peerDeploymentOrderProductDO.getProductSkuId());
                productInStorage.setProductCount(peerDeploymentOrderProductDO.getProductSkuCount());
                productInStorage.setIsNew(peerDeploymentOrderProductDO.getIsNew());
                productInStorage.setItemReferId(peerDeploymentOrderProductDO.getId());
                productInStorage.setItemReferType(StockItemReferType.PEER_DEPLOYMENT_PRODUCT);

                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(peerDeploymentOrderProductDO.getProductSkuId());
                List<ProductMaterial> productMaterialList = productServiceResult.getResult().getProductSkuList().get(0).getProductMaterialList();
                productInStorage.setProductMaterialList(productMaterialList);
                productInStorageList.add(productInStorage);
            }
        }
        //处理同行调拨单配件物料
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderMaterialDOList)) {
            for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : peerDeploymentOrderMaterialDOList) {
                MaterialInStorage materialInStorage = new MaterialInStorage();
                materialInStorage.setMaterialId(peerDeploymentOrderMaterialDO.getMaterialId());
                materialInStorage.setMaterialCount(peerDeploymentOrderMaterialDO.getProductMaterialCount());
                materialInStorage.setIsNew(peerDeploymentOrderMaterialDO.getIsNew());
                materialInStorage.setItemReferId(peerDeploymentOrderMaterialDO.getId());
                materialInStorage.setItemReferType(StockItemReferType.PEER_DEPLOYMENT_MATERIAL);
                materialInStorageList.add(materialInStorage);
            }
        }
        productInStockParam.setProductInStorageList(productInStorageList);
        productInStockParam.setMaterialInStorageList(materialInStorageList);

        //调用入库接口
        ServiceResult<String, Integer> inStockResult = warehouseService.productInStock(productInStockParam);
        if (!ErrorCode.SUCCESS.equals(inStockResult.getErrorCode())) {
            result.setErrorCode(inStockResult.getErrorCode());
            return result;
        }
        //获取返回值存库ID,通过ID查询库存单
        StockOrderDO stockOrderDO = stockOrderMapper.findById(inStockResult.getResult());

        //通过存库单的编号，查询设备和散料
        //设置同行调拨单商品设备单
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderProductDOList)) {
            List<StockOrderEquipmentDO> stockOrderEquipmentDOList = stockOrderEquipmentMapper.findByStockOrderNo(stockOrderDO.getStockOrderNo());
            for (StockOrderEquipmentDO stockOrderEquipmentDO : stockOrderEquipmentDOList) {
                PeerDeploymentOrderProductEquipmentDO peerDeploymentOrderProductEquipmentDO = new PeerDeploymentOrderProductEquipmentDO();
                peerDeploymentOrderProductEquipmentDO.setPeerDeploymentOrderProductId(stockOrderEquipmentDO.getItemReferId());
                peerDeploymentOrderProductEquipmentDO.setPeerDeploymentOrderId(dbPeerDeploymentOrderDO.getId());
                peerDeploymentOrderProductEquipmentDO.setPeerDeploymentOrderNo(dbPeerDeploymentOrderDO.getPeerDeploymentOrderNo());
                peerDeploymentOrderProductEquipmentDO.setEquipmentId(stockOrderEquipmentDO.getEquipmentId());
                peerDeploymentOrderProductEquipmentDO.setEquipmentNo(stockOrderEquipmentDO.getEquipmentNo());

                peerDeploymentOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderProductEquipmentDO.setRemark(stockOrderEquipmentDO.getRemark());
                peerDeploymentOrderProductEquipmentDO.setCreateTime(currentTime);
                peerDeploymentOrderProductEquipmentDO.setCreateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductEquipmentDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductEquipmentMapper.save(peerDeploymentOrderProductEquipmentDO);
            }
        }

        //设置同行调拨单配件散料单
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderMaterialDOList)) {
            List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList = stockOrderBulkMaterialMapper.findByStockOrderNo(stockOrderDO.getStockOrderNo());
            for (StockOrderBulkMaterialDO stockOrderBulkMaterialDO : stockOrderBulkMaterialDOList) {
                if (StockItemReferType.PEER_DEPLOYMENT_MATERIAL.equals(stockOrderBulkMaterialDO.getItemReferType())){
                    PeerDeploymentOrderMaterialBulkDO peerDeploymentOrderMaterialBulkDO = new PeerDeploymentOrderMaterialBulkDO();
                    peerDeploymentOrderMaterialBulkDO.setPeerDeploymentOrderMaterialId(stockOrderBulkMaterialDO.getItemReferId());
                    peerDeploymentOrderMaterialBulkDO.setPeerDeploymentOrderId(dbPeerDeploymentOrderDO.getId());
                    peerDeploymentOrderMaterialBulkDO.setPeerDeploymentOrderNo(dbPeerDeploymentOrderDO.getPeerDeploymentOrderNo());
                    peerDeploymentOrderMaterialBulkDO.setBulkMaterialId(stockOrderBulkMaterialDO.getBulkMaterialId());
                    peerDeploymentOrderMaterialBulkDO.setBulkMaterialNo(stockOrderBulkMaterialDO.getBulkMaterialNo());
                    peerDeploymentOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    peerDeploymentOrderMaterialBulkDO.setRemark(stockOrderBulkMaterialDO.getRemark());
                    peerDeploymentOrderMaterialBulkDO.setCreateTime(currentTime);
                    peerDeploymentOrderMaterialBulkDO.setCreateUser(loginUser.getUserId().toString());
                    peerDeploymentOrderMaterialBulkDO.setUpdateTime(currentTime);
                    peerDeploymentOrderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
                    peerDeploymentOrderMaterialBulkMapper.save(peerDeploymentOrderMaterialBulkDO);
                }
            }
        }

        dbPeerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_CONFIRM);
        dbPeerDeploymentOrderDO.setUpdateTime(currentTime);
        dbPeerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        peerDeploymentOrderMapper.update(dbPeerDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbPeerDeploymentOrderDO.getPeerDeploymentOrderNo());
        return result;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            PeerDeploymentOrderDO peerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(businessNo);
            if (peerDeploymentOrderDO == null) {
                return false;
            }
            //不是审核中状态的同行调拨单，拒绝处理
            if (!PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_VERIFYING.equals(peerDeploymentOrderDO.getPeerDeploymentOrderStatus())) {
                return false;
            }

            if (verifyResult) {
                peerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_PROCESSING);
            } else {
                peerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
            }
            peerDeploymentOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            peerDeploymentOrderDO.setUpdateTime(new Date());
            peerDeploymentOrderMapper.update(peerDeploymentOrderDO);

            return true;
        } catch (Exception e) {
            logger.error("【同行调拨单审核，业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        }
    }

    @Override
    public ServiceResult<String, Page<PeerDeploymentOrder>> page(PeerDeploymentOrderQueryParam peerDeploymentOrderQueryParam) {
        ServiceResult<String, Page<PeerDeploymentOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(peerDeploymentOrderQueryParam.getPageNo(), peerDeploymentOrderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("peerDeploymentOrderQueryParam", peerDeploymentOrderQueryParam);

        Integer totalCount = peerDeploymentOrderMapper.findPeerDeploymentOrderCountByParams(maps);
        List<PeerDeploymentOrderDO> peerDeploymentOrderDOList = peerDeploymentOrderMapper.findPeerDeploymentOrderByParams(maps);
        List<PeerDeploymentOrder> peerDeploymentOrderList = ConverterUtil.convertList(peerDeploymentOrderDOList, PeerDeploymentOrder.class);
        Page<PeerDeploymentOrder> page = new Page<>(peerDeploymentOrderList, totalCount, peerDeploymentOrderQueryParam.getPageNo(), peerDeploymentOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Override
    public ServiceResult<String, PeerDeploymentOrder> detailPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder) {
        ServiceResult<String, PeerDeploymentOrder> result = new ServiceResult<>();

        PeerDeploymentOrderDO peerDeploymentOrderDO = peerDeploymentOrderMapper.findDetailByNo(peerDeploymentOrder.getPeerDeploymentOrderNo());

        PeerDeploymentOrder dbPeerDeploymentOrder = ConverterUtil.convert(peerDeploymentOrderDO, PeerDeploymentOrder.class);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbPeerDeploymentOrder);
        return result;
    }

    /**
     * 保存同行调拨单商品转入
     * @param peerDeploymentOrderProductDOList
     * @param peerDeploymentOrderNo
     * @param loginUser
     * @param currentTime
     */
    private void savePeerDeploymentOrderProductInfo(List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList, String peerDeploymentOrderNo, User loginUser, Date currentTime) {
        Map<Integer, PeerDeploymentOrderProductDO> savePeerDeploymentOrderProductDOMap = new HashMap<>();
        Map<Integer, PeerDeploymentOrderProductDO> updatePeerDeploymentOrderProductDOMap = new HashMap<>();
        List<PeerDeploymentOrderProductDO> dbPeerDeploymentOrderProductDOList = peerDeploymentOrderProductMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderNo);
        PeerDeploymentOrderDO peerDeploymentOrderDO = peerDeploymentOrderMapper.findByNo(peerDeploymentOrderNo);
        Map<Integer, PeerDeploymentOrderProductDO> dbPeerDeploymentOrderProductDOMap = ListUtil.listToMap(dbPeerDeploymentOrderProductDOList, "id");

        if (CollectionUtil.isNotEmpty(peerDeploymentOrderProductDOList)) {
            for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : peerDeploymentOrderProductDOList) {
                if (dbPeerDeploymentOrderProductDOMap.get(peerDeploymentOrderProductDO.getId()) != null) {
                    updatePeerDeploymentOrderProductDOMap.put(peerDeploymentOrderProductDO.getId(), peerDeploymentOrderProductDO);
                    dbPeerDeploymentOrderProductDOMap.remove(peerDeploymentOrderProductDO.getId());
                } else {
                    //判断新旧做区分
                    Integer key = peerDeploymentOrderProductDO.getProductSkuId() + peerDeploymentOrderProductDO.getIsNew() + (Math.round(1000)+1000);
                    savePeerDeploymentOrderProductDOMap.put(key, peerDeploymentOrderProductDO);

                }
            }
        }

        if (savePeerDeploymentOrderProductDOMap.size() > 0) {
            List<PeerDeploymentOrderProductDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : savePeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(peerDeploymentOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                peerDeploymentOrderProductDO.setProductAmount(BigDecimalUtil.mul(peerDeploymentOrderProductDO.getProductUnitAmount(), new BigDecimal(peerDeploymentOrderProductDO.getProductSkuCount())));
                peerDeploymentOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                peerDeploymentOrderProductDO.setPeerDeploymentOrderId(peerDeploymentOrderDO.getId());
                peerDeploymentOrderProductDO.setPeerDeploymentOrderNo(peerDeploymentOrderNo);
                peerDeploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setCreateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductDO.setCreateTime(currentTime);
                saveList.add(peerDeploymentOrderProductDO);
            }
            peerDeploymentOrderProductMapper.saveList(saveList);

        }

        if (updatePeerDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : updatePeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                PeerDeploymentOrderProductDO oldPeerDeploymentOrderProductDO = peerDeploymentOrderProductMapper.findByPeerDeploymentOrderNoAndSkuId(peerDeploymentOrderNo, peerDeploymentOrderProductDO.getProductSkuId());
                if (oldPeerDeploymentOrderProductDO == null) {
                    throw new BusinessException(ErrorCode.RECORD_NOT_EXISTS);
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(peerDeploymentOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                peerDeploymentOrderProductDO.setId(oldPeerDeploymentOrderProductDO.getId());
                peerDeploymentOrderProductDO.setProductAmount(BigDecimalUtil.mul(peerDeploymentOrderProductDO.getProductUnitAmount(), new BigDecimal(peerDeploymentOrderProductDO.getProductSkuCount())));
                peerDeploymentOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductMapper.update(peerDeploymentOrderProductDO);
            }
        }

        if (dbPeerDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : dbPeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                peerDeploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductMapper.update(peerDeploymentOrderProductDO);
            }
        }
    }

    /**
     * 保存同行调拨单配件转入
     * @param peerDeploymentOrderMaterialDOList
     * @param peerDeploymentOrderNo
     * @param loginUser
     * @param currentTime
     */
    private void savePeerDeploymentOrderMaterialInfo(List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList, String peerDeploymentOrderNo, User loginUser, Date currentTime) {
        Map<Integer, PeerDeploymentOrderMaterialDO> savePeerDeploymentOrderMaterialDOMap = new HashMap<>();
        Map<Integer, PeerDeploymentOrderMaterialDO> updatePeerDeploymentOrderMaterialDOMap = new HashMap<>();
        List<PeerDeploymentOrderMaterialDO> dbPeerDeploymentOrderMaterialDOList = peerDeploymentOrderMaterialMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderNo);
        PeerDeploymentOrderDO peerDeploymentOrderDO = peerDeploymentOrderMapper.findByNo(peerDeploymentOrderNo);
        Map<Integer, PeerDeploymentOrderMaterialDO> dbPeerDeploymentOrderMaterialDOMap = ListUtil.listToMap(dbPeerDeploymentOrderMaterialDOList, "id");

        if (CollectionUtil.isNotEmpty(peerDeploymentOrderMaterialDOList)) {
            for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : peerDeploymentOrderMaterialDOList) {
                if (dbPeerDeploymentOrderMaterialDOMap.get(peerDeploymentOrderMaterialDO.getId()) != null) {
                    updatePeerDeploymentOrderMaterialDOMap.put(peerDeploymentOrderMaterialDO.getId(), peerDeploymentOrderMaterialDO);
                    dbPeerDeploymentOrderMaterialDOMap.remove(peerDeploymentOrderMaterialDO.getId());
                } else {
                    //判断新旧做区分
                    Integer key = peerDeploymentOrderMaterialDO.getMaterialId() + peerDeploymentOrderMaterialDO.getIsNew() + (Math.round(1000)+1000);
                    savePeerDeploymentOrderMaterialDOMap.put(key, peerDeploymentOrderMaterialDO);
                }
            }
        }

        if (savePeerDeploymentOrderMaterialDOMap.size() > 0) {
            List<PeerDeploymentOrderMaterialDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : savePeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(peerDeploymentOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                peerDeploymentOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(peerDeploymentOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(peerDeploymentOrderMaterialDO.getProductMaterialCount())));
                peerDeploymentOrderMaterialDO.setProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                peerDeploymentOrderMaterialDO.setPeerDeploymentOrderId(peerDeploymentOrderDO.getId());
                peerDeploymentOrderMaterialDO.setPeerDeploymentOrderNo(peerDeploymentOrderNo);
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialDO.setCreateTime(currentTime);
                saveList.add(peerDeploymentOrderMaterialDO);

            }
            peerDeploymentOrderMaterialMapper.saveList(saveList);

        }

        if (updatePeerDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : updatePeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(peerDeploymentOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                peerDeploymentOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(peerDeploymentOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(peerDeploymentOrderMaterialDO.getProductMaterialCount())));
                peerDeploymentOrderMaterialDO.setProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialMapper.update(peerDeploymentOrderMaterialDO);
            }
        }

        if (dbPeerDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : dbPeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialMapper.update(peerDeploymentOrderMaterialDO);
            }
        }
    }

    /**
     * 保存同行调拨单收货信息
     * @param peerDeploymentOrderConsignInfoDO
     * @param peerDeploymentOrderId
     * @param loginUser
     * @param currentTime
     */
    private void savePeerDeploymentOrderConsignInfo(PeerDeploymentOrderConsignInfoDO peerDeploymentOrderConsignInfoDO, Integer peerDeploymentOrderId, User loginUser, Date currentTime) {

        PeerDeploymentOrderConsignInfoDO dbPeerDeploymentOrderConsignInfoDO = peerDeploymentOrderConsignInfoMapper.findByPeerDeploymentOrderConsignInfoId(peerDeploymentOrderId);


        if (dbPeerDeploymentOrderConsignInfoDO == null) {
            peerDeploymentOrderConsignInfoDO.setPeerDeploymentOrderId(peerDeploymentOrderId);
            peerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            peerDeploymentOrderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setCreateTime(currentTime);
            peerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.save(peerDeploymentOrderConsignInfoDO);
        } else {
            dbPeerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            dbPeerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            dbPeerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.update(dbPeerDeploymentOrderConsignInfoDO);

            peerDeploymentOrderConsignInfoDO.setPeerDeploymentOrderId(peerDeploymentOrderId);
            peerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            peerDeploymentOrderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setCreateTime(currentTime);
            peerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.save(peerDeploymentOrderConsignInfoDO);

        }
    }

    /**
     * 计算订单预计归还时间
     */
    private Date peerDeploymentOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (PeerDeploymentOrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (PeerDeploymentOrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private PeerDeploymentOrderMapper peerDeploymentOrderMapper;
    @Autowired
    private PeerMapper peerMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PeerDeploymentOrderProductMapper peerDeploymentOrderProductMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PeerDeploymentOrderMaterialMapper peerDeploymentOrderMaterialMapper;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private PeerDeploymentOrderConsignInfoMapper peerDeploymentOrderConsignInfoMapper;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private AreaCityMapper areaCityMapper;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private StockOrderMapper stockOrderMapper;
    @Autowired
    private StockOrderEquipmentMapper stockOrderEquipmentMapper;
    @Autowired
    private PeerDeploymentOrderProductEquipmentMapper peerDeploymentOrderProductEquipmentMapper;
    @Autowired
    private StockOrderBulkMaterialMapper stockOrderBulkMaterialMapper;
    @Autowired
    private PeerDeploymentOrderMaterialBulkMapper peerDeploymentOrderMaterialBulkMapper;

}
