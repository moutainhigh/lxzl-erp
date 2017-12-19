package com.lxzl.erp.core.service.changeOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterial;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterialBulk;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterialBulk;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
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

@Service
public class ChangeOrderServiceImpl implements ChangeOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ChangeOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(AddChangeOrderParam addChangeOrderParam) {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addChangeOrderParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        List<ChangeProductSkuPairs> changeOrderParamProductSkuList = addChangeOrderParam.getChangeProductSkuPairsList();
        List<ChangeMaterialPairs> changeOrderParamMaterialList = addChangeOrderParam.getChangeMaterialPairsList();
        if (CollectionUtil.isEmpty(changeOrderParamProductSkuList) && CollectionUtil.isEmpty(changeOrderParamMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        //用户在租sku统计
        Map<String, Object> findRentMap = customerOrderSupport.getCustomerAllMap(customerDO.getId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = 0;
        //累计换货物料总数
        Integer totalChangeMaterialCount = 0;
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOList = new ArrayList<>();
        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamProductSkuList)) {
            for (ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList) {
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(changeProductSkuPairs.getProductSkuIdSrc());
                if (!changeProductSkuPairs.getProductSkuIdDest().equals(changeProductSkuPairs.getProductSkuIdSrc())) {//目前只支持相同sku更换
                    serviceResult.setErrorCode(ErrorCode.CHANGE_ONLY_SAME_SKU);
                    return serviceResult;
                }
                if (oldSkuRent == null) {//如果要更换的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                if (changeProductSkuPairs.getChangeCount() > oldSkuRent.getCanProcessCount()) {//更换的sku数量大于可换数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }

                totalChangeProductCount += changeProductSkuPairs.getChangeCount();
                ServiceResult<String, Product> productSkuSrcResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdSrc());
                if (!ErrorCode.SUCCESS.equals(productSkuSrcResult.getErrorCode()) || productSkuSrcResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                ServiceResult<String, Product> productSkuDestResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdDest());
                if (!ErrorCode.SUCCESS.equals(productSkuDestResult.getErrorCode()) || productSkuDestResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                changeOrderProductDO.setChangeProductSkuIdSrc(changeProductSkuPairs.getProductSkuIdSrc());
                changeOrderProductDO.setChangeProductSkuIdDest(changeProductSkuPairs.getProductSkuIdDest());
                changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                changeOrderProductDO.setRealChangeProductSkuCount(0);
                changeOrderProductDO.setChangeProductSkuSnapshotSrc(JSON.toJSONString(productSkuSrcResult.getResult()));
                changeOrderProductDO.setChangeProductSkuSnapshotDest(JSON.toJSONString(productSkuDestResult.getResult()));
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setCreateTime(now);
                changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setUpdateTime(now);
                changeOrderProductDOList.add(changeOrderProductDO);
            }
        }
        //构造待保存换货单物料项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = new ArrayList<>();
        //如果要换货的物料不在在租列表，或者要换货的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamMaterialList)) {
            for (ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList) {
                MaterialDO srcMaterial = oldMaterialCountMap.get(changeMaterialPairs.getMaterialNoSrc());
                if (changeMaterialPairs.getChangeCount() > srcMaterial.getCanProcessCount()) {//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                if (srcMaterial == null) {//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                totalChangeMaterialCount += changeMaterialPairs.getChangeCount();
                MaterialDO destMaterial = materialMapper.findByNo(changeMaterialPairs.getMaterialNoDest());

                ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                changeOrderMaterialDO.setChangeMaterialIdSrc(srcMaterial.getId());
                changeOrderMaterialDO.setChangeMaterialIdDest(destMaterial.getId());
                changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(srcMaterial));
                changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(destMaterial));
                changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                changeOrderMaterialDO.setRealChangeMaterialCount(0);
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialDO.setCreateTime(now);
                changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDO.setUpdateTime(now);
                changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDOList.add(changeOrderMaterialDO);
            }
        }
        //创建租赁换货单
        ChangeOrderDO changeOrderDO = new ChangeOrderDO();
        changeOrderDO.setChangeOrderNo(GenerateNoUtil.generateChangeOrderNo(now));
        changeOrderDO.setCustomerId(customerDO.getId());
        changeOrderDO.setCustomerNo(customerDO.getCustomerNo());
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReasonType(ChangeReasonType.CHANGE_REASON_TYPE_GO_UP);
        changeOrderDO.setChangeReason(addChangeOrderParam.getChangeReason());
        changeOrderDO.setChangeMode(addChangeOrderParam.getChangeMode());
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
        changeOrderDO.setOwner(userSupport.getCurrentUserId());
        changeOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        changeOrderDO.setRemark(addChangeOrderParam.getRemark());
        changeOrderDO.setCreateTime(now);
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.save(changeOrderDO);

        //保存取货地址信息
        ChangeOrderConsignInfoDO changeOrderConsignInfoDO = new ChangeOrderConsignInfoDO();
        changeOrderConsignInfoDO.setChangeOrderId(changeOrderDO.getId());
        changeOrderConsignInfoDO.setChangeOrderNo(changeOrderDO.getChangeOrderNo());
        changeOrderConsignInfoDO.setConsigneeName(addChangeOrderParam.getChangeOrderConsignInfo().getConsigneeName());
        changeOrderConsignInfoDO.setConsigneePhone(addChangeOrderParam.getChangeOrderConsignInfo().getConsigneePhone());
        changeOrderConsignInfoDO.setProvince(addChangeOrderParam.getChangeOrderConsignInfo().getProvince());
        changeOrderConsignInfoDO.setCity(addChangeOrderParam.getChangeOrderConsignInfo().getCity());
        changeOrderConsignInfoDO.setDistrict(addChangeOrderParam.getChangeOrderConsignInfo().getDistrict());
        changeOrderConsignInfoDO.setAddress(addChangeOrderParam.getChangeOrderConsignInfo().getAddress());
        changeOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        changeOrderConsignInfoDO.setRemark(addChangeOrderParam.getChangeOrderConsignInfo().getRemark());
        changeOrderConsignInfoDO.setCreateTime(now);
        changeOrderConsignInfoDO.setUpdateTime(now);
        changeOrderConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
        changeOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderConsignInfoMapper.save(changeOrderConsignInfoDO);

        //保存退还商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOList)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOList);
        }
        //保存退还物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOList)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOList);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(UpdateChangeOrderParam updateChangeOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(updateChangeOrderParam.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_CAN_NOT_UPDATE);
            return serviceResult;
        }
        ChangeOrderConsignInfoDO changeOrderConsignInfoDO = changeOrderDO.getChangeOrderConsignInfoDO();
        List<ChangeProductSkuPairs> changeOrderParamProductSkuList = updateChangeOrderParam.getChangeProductSkuPairsList();
        List<ChangeMaterialPairs> changeOrderParamMaterialList = updateChangeOrderParam.getChangeMaterialPairsList();
        if (CollectionUtil.isEmpty(changeOrderParamProductSkuList) && CollectionUtil.isEmpty(changeOrderParamMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        //用户在租sku统计
        Map<String, Object> findRentMap = customerOrderSupport.getCustomerAllMap(changeOrderDO.getCustomerId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = changeOrderDO.getTotalChangeProductCount();
        //累计换货物料总数
        Integer totalChangeMaterialCount = changeOrderDO.getTotalChangeMaterialCount();
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOListForSave = new ArrayList<>();
        List<ChangeOrderProductDO> changeOrderProductDOListForUpdate = new ArrayList<>();
        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamProductSkuList)) {
            for (ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList) {
                if (changeProductSkuPairs.getChangeOrderProductId() == null) {
                    ProductSkuDO oldSkuRent = oldSkuCountMap.get(changeProductSkuPairs.getProductSkuIdSrc());
                    if (!changeProductSkuPairs.getProductSkuIdDest().equals(changeProductSkuPairs.getProductSkuIdSrc())) {//目前只支持相同sku更换
                        serviceResult.setErrorCode(ErrorCode.CHANGE_ONLY_SAME_SKU);
                        return serviceResult;
                    }
                    if (oldSkuRent == null) {//如果要更换的sku不在在租列表
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                        return serviceResult;
                    }
                    if (changeProductSkuPairs.getChangeCount() > oldSkuRent.getCanProcessCount()) {//更换的sku数量大于可换数量
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                        return serviceResult;
                    }
                    totalChangeProductCount += changeProductSkuPairs.getChangeCount();
                    ServiceResult<String, Product> productSkuSrcResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdSrc());
                    if (!ErrorCode.SUCCESS.equals(productSkuSrcResult.getErrorCode()) || productSkuSrcResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    ServiceResult<String, Product> productSkuDestResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdDest());
                    if (!ErrorCode.SUCCESS.equals(productSkuDestResult.getErrorCode()) || productSkuDestResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                    changeOrderProductDO.setChangeProductSkuIdSrc(changeProductSkuPairs.getProductSkuIdSrc());
                    changeOrderProductDO.setChangeProductSkuIdDest(changeProductSkuPairs.getProductSkuIdDest());
                    changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                    changeOrderProductDO.setRealChangeProductSkuCount(0);
                    changeOrderProductDO.setChangeProductSkuSnapshotSrc(JSON.toJSONString(productSkuSrcResult.getResult()));
                    changeOrderProductDO.setChangeProductSkuSnapshotDest(JSON.toJSONString(productSkuDestResult.getResult()));
                    changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setCreateTime(now);
                    changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setUpdateTime(now);
                    changeOrderProductDOListForSave.add(changeOrderProductDO);
                } else {
                    ChangeOrderProductDO changeOrderProductDO = changeOrderProductMapper.findById(changeProductSkuPairs.getChangeOrderProductId());
                    if(changeOrderProductDO==null||!changeOrderProductDO.getChangeOrderNo().equals(changeOrderDO.getChangeOrderNo())){
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        return serviceResult;
                    }
                    totalChangeProductCount = totalChangeProductCount + changeProductSkuPairs.getChangeCount() - changeOrderProductDO.getChangeProductSkuCount();
                    changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                    changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setUpdateTime(now);
                    changeOrderProductDOListForUpdate.add(changeOrderProductDO);
                }
            }
        }
        //构造待保存换货单物料项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOListForSave = new ArrayList<>();
        List<ChangeOrderMaterialDO> changeOrderMaterialDOListForUpdate = new ArrayList<>();
        //如果要换货的物料不在在租列表，或者要换货的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamMaterialList)) {
            for (ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList) {
                if (changeMaterialPairs.getChangeOrderMaterialId() == null) {
                    MaterialDO srcMaterial = oldMaterialCountMap.get(changeMaterialPairs.getMaterialNoSrc());
                    if (changeMaterialPairs.getChangeCount() > srcMaterial.getCanProcessCount()) {//退还的物料数量大于可租数量
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                        return serviceResult;
                    }
                    if (srcMaterial == null) {//如果要退还的物料不在在租列表
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                        return serviceResult;
                    }
                    totalChangeMaterialCount += changeMaterialPairs.getChangeCount();
                    MaterialDO destMaterial = materialMapper.findByNo(changeMaterialPairs.getMaterialNoDest());

                    ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                    changeOrderMaterialDO.setChangeMaterialIdSrc(srcMaterial.getId());
                    changeOrderMaterialDO.setChangeMaterialIdDest(destMaterial.getId());
                    changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(srcMaterial));
                    changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(destMaterial));
                    changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                    changeOrderMaterialDO.setRealChangeMaterialCount(0);
                    changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    changeOrderMaterialDO.setCreateTime(now);
                    changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDO.setUpdateTime(now);
                    changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDOListForSave.add(changeOrderMaterialDO);
                } else {
                    ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(changeMaterialPairs.getChangeOrderMaterialId());
                    if(changeOrderMaterialDO==null||!changeOrderMaterialDO.getChangeOrderNo().equals(changeOrderDO.getChangeOrderNo())){
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        return serviceResult;
                    }
                    totalChangeProductCount = totalChangeProductCount + changeMaterialPairs.getChangeCount() - changeOrderMaterialDO.getChangeMaterialCount();
                    changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                    changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDO.setUpdateTime(now);
                    changeOrderMaterialDOListForUpdate.add(changeOrderMaterialDO);
                }
            }
        }
        List<ChangeOrderProductDO> oldChangeOrderProductDOList = changeOrderProductMapper.findByChangeOrderId(changeOrderDO.getId());
        Map<Integer ,ChangeProductSkuPairs> newChangeOrderParamProductSkuMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldChangeOrderProductDOList)){
            for(ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList){
                if(changeProductSkuPairs.getChangeOrderProductId()!=null){
                    newChangeOrderParamProductSkuMap.put(changeProductSkuPairs.getChangeOrderProductId(),changeProductSkuPairs);
                }
            }
        }

        for(ChangeOrderProductDO changeOrderProductDO : oldChangeOrderProductDOList){
            //如果不在新列表中，则删除
            if(!newChangeOrderParamProductSkuMap.containsKey(changeOrderProductDO.getId())){
                //改变总数
                totalChangeProductCount = totalChangeProductCount-changeOrderProductDO.getChangeProductSkuCount();
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderProductMapper.update(changeOrderProductDO);
            }
        }
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
        Map<Integer ,ChangeMaterialPairs> changeOrderParamMaterialMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(changeOrderParamMaterialList)){
            for(ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList){
                if(changeMaterialPairs.getChangeOrderMaterialId()!=null){
                    changeOrderParamMaterialMap.put(changeMaterialPairs.getChangeOrderMaterialId(),changeMaterialPairs);
                }
            }
        }

        //如果旧列表中有的新列表中没有则删除
        for(ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderMaterialDOList){
            //如果不在新列表中，则删除
            if(!changeOrderParamMaterialMap.containsKey(changeOrderMaterialDO.getId())){
                //改变总数
                totalChangeMaterialCount = totalChangeMaterialCount-changeOrderMaterialDO.getChangeMaterialCount();
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialMapper.update(changeOrderMaterialDO);
            }
        }
        //更新租赁换货单
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReason(updateChangeOrderParam.getChangeReason());
        changeOrderDO.setRemark(updateChangeOrderParam.getRemark());
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);

        //更新取货地址信息
        if (changeOrderConsignInfoDO != null) {
            changeOrderConsignInfoDO.setConsigneeName(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneeName());
            changeOrderConsignInfoDO.setConsigneePhone(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneePhone());
            changeOrderConsignInfoDO.setProvince(updateChangeOrderParam.getChangeOrderConsignInfo().getProvince());
            changeOrderConsignInfoDO.setCity(updateChangeOrderParam.getChangeOrderConsignInfo().getCity());
            changeOrderConsignInfoDO.setDistrict(updateChangeOrderParam.getChangeOrderConsignInfo().getDistrict());
            changeOrderConsignInfoDO.setAddress(updateChangeOrderParam.getChangeOrderConsignInfo().getAddress());
            changeOrderConsignInfoDO.setRemark(updateChangeOrderParam.getChangeOrderConsignInfo().getRemark());
            changeOrderConsignInfoDO.setUpdateTime(now);
            changeOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderConsignInfoMapper.update(changeOrderConsignInfoDO);
        }

        //批量保存换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOListForSave)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOListForSave);
        }
        //批量更新换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOListForUpdate)) {
            changeOrderProductMapper.batchUpdate(changeOrderProductDOListForUpdate);
        }
        //批量保存换货物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOListForSave)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOListForSave);
        }
        //批量更新换货物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOListForUpdate)) {
            changeOrderMaterialMapper.batchUpdate(changeOrderMaterialDOListForUpdate);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> commit(ChangeOrderCommitParam changeOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        //校验换货单是否存在
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderCommitParam.getChangeOrderNo());
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        } else if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus())) {
            //只有待提交状态的换货单可以提交
            result.setErrorCode(ErrorCode.CHANGE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!changeOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建换货单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (changeOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CHANGE, changeOrderDO.getChangeOrderNo(), changeOrderCommitParam.getVerifyUserId(), changeOrderCommitParam.getRemark());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING);
                changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderDO.setUpdateTime(now);
                changeOrderMapper.update(changeOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK);
            changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderDO.setUpdateTime(now);
            changeOrderMapper.update(changeOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    /**
     * 接收审核结果通知
     *
     * @param verifyResult
     * @param businessNo
     */

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(businessNo);
            if (changeOrderDO == null) {
                return false;
            }
            //不是审核中状态的收货单，拒绝处理
            if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING.equals(changeOrderDO.getChangeOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK);
            } else {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
            }
            changeOrderMapper.update(changeOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("【换货单审核后，业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        } catch (Throwable t) {
            logger.error("【换货单审核后，业务处理异常】", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> stockUpForChange(StockUpForChangeParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(param.getChangeOrderNo());
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_STOCK_UP);
            return result;
        }
        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }

        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            //添加更换后设备，添加更换后物料
            ServiceResult<String, Object> addOrderDestItemResult = addOrderDestItemResultDest(param, changeOrderDO);
            if (!ErrorCode.SUCCESS.equals(addOrderDestItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderDestItemResult.getErrorCode(),addOrderDestItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            //删除更换后设备，删除更换后物料
            ServiceResult<String, Object> removeOrderDestItemResult = removeOrderDestItemResultDest(param.getEquipmentNo(),param.getMaterialNo(),param.getMaterialCount(), changeOrderDO);
            if (!ErrorCode.SUCCESS.equals(removeOrderDestItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(removeOrderDestItemResult.getErrorCode());
                return result;
            }
        }

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderDO.setUpdateTime(currentTime);
        changeOrderMapper.update(changeOrderDO);
        result.setResult(changeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> delivery(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //不是备货中的换货单不能发货
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_DELIVERY);
        }
        //校验设备是否已经全部备货
        List<ChangeOrderProductDO> changeOrderProductDOList = changeOrderProductMapper.findByChangeOrderId(changeOrderDO.getId());
        if(CollectionUtil.isNotEmpty(changeOrderProductDOList)){
            List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
            //按sku统计需备货数量
            Map<Integer,Integer> skuShouldStockMap = new HashMap<>();
            for(ChangeOrderProductDO changeOrderProductDO : changeOrderProductDOList){
                if(!skuShouldStockMap.containsKey(changeOrderProductDO.getChangeProductSkuIdDest())){
                    skuShouldStockMap.put(changeOrderProductDO.getChangeProductSkuIdDest(),0);
                }
                skuShouldStockMap.put(changeOrderProductDO.getChangeProductSkuIdDest(),skuShouldStockMap.get(changeOrderProductDO.getChangeProductSkuIdDest())+changeOrderProductDO.getChangeProductSkuCount());
            }

            //按sku统计已备货sku数量
            Map<Integer,Integer> stockYetMap = new HashMap<>();
            for(ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList){
                if(!stockYetMap.containsKey(changeOrderProductEquipmentDO.getDestSkuId())){
                    stockYetMap.put(changeOrderProductEquipmentDO.getDestSkuId(),0);
                }
                stockYetMap.put(changeOrderProductEquipmentDO.getDestSkuId(),stockYetMap.get(changeOrderProductEquipmentDO.getDestSkuId())+1);
            }

            for(Integer skuId : skuShouldStockMap.keySet()){
                if(!stockYetMap.containsKey(skuId)||skuShouldStockMap.get(skuId)>stockYetMap.get(skuId)){
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }

        //校验散料是否已经全部备货
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
        if(CollectionUtil.isNotEmpty(changeOrderMaterialDOList)){
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
            //按物料ID统计需备货数量
            Map<Integer,Integer> skuShouldStockMap = new HashMap<>();
            for(ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderMaterialDOList){
                if(!skuShouldStockMap.containsKey(changeOrderMaterialDO.getChangeMaterialIdDest())){
                    skuShouldStockMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(),0);
                }
                skuShouldStockMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(),skuShouldStockMap.get(changeOrderMaterialDO.getChangeMaterialIdDest())+changeOrderMaterialDO.getChangeMaterialCount());
            }

            //按物料ID统计已备货物料数量
            Map<Integer,Integer> stockYetMap = new HashMap<>();
            for(ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList){
                if(!stockYetMap.containsKey(changeOrderMaterialBulkDO.getDestMaterialId())){
                    stockYetMap.put(changeOrderMaterialBulkDO.getDestMaterialId(),0);
                }
                stockYetMap.put(changeOrderMaterialBulkDO.getDestMaterialId(),stockYetMap.get(changeOrderMaterialBulkDO.getDestMaterialId())+1);
            }

            for(Integer materialId : skuShouldStockMap.keySet()){
                if(!stockYetMap.containsKey(materialId)||skuShouldStockMap.get(materialId)>stockYetMap.get(materialId)){
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP);
        changeOrderDO.setUpdateTime(new Date());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> doChangeEquipment(ChangeOrderProductEquipment changeOrderProductEquipment) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderProductEquipment.getChangeOrderNo());
        if(changeOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //判断换货单状态是否允许更换操作
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_DO_CHANGE);
            return serviceResult;
        }
        ProductEquipmentDO srcProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipment.getSrcEquipmentNo());
        if (srcProductEquipmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
            return serviceResult;
        }
        //判断是否是客户的在租设备
        if(StringUtil.isEmpty(srcProductEquipmentDO.getOrderNo())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
            return serviceResult;
        }
        OrderDO orderDO  = orderMapper.findByOrderNo(srcProductEquipmentDO.getOrderNo());
        if(orderDO==null||!orderDO.getBuyerCustomerId().equals(changeOrderDO.getCustomerId())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
            return serviceResult;
        }
        //查找用户此换货单所有更换设备项
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderProductEquipment.getChangeOrderNo());
        //用户输入的src设备所属的sku应与dest设备的sku相同，先找出支持的dest设备有哪些sku，每个sku对应一个换货单设备列表
        //这里的map的value保存的并不是列表，因为只要有一个匹配的设备就可以了，这里相当于找到了sku下最后一个符合的换货单设备项
        Map<Integer,ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOMap = new HashMap<>();
        for(ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList){
            //找到没有换过的换货单设备项
            if(changeOrderProductEquipmentDO.getSrcEquipmentId()==null){
                changeOrderProductEquipmentDOMap.put(changeOrderProductEquipmentDO.getDestSkuId(),changeOrderProductEquipmentDO);
            }
        }
        //如果换货单设备项中没有可换的该sku项，则不可换
        ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = changeOrderProductEquipmentDOMap.get(srcProductEquipmentDO.getSkuId());
        if(changeOrderProductEquipmentDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_CAN_NOT_CHANGE);
            return serviceResult;
        }
        ProductEquipmentDO destProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipmentDO.getDestEquipmentNo());
        //更新租赁换货商品项表，实际换货数量
        ChangeOrderProductDO changeOrderProductDO = changeOrderProductMapper.findById(changeOrderProductEquipmentDO.getChangeOrderProductId());
        changeOrderProductDO.setRealChangeProductSkuCount(changeOrderProductDO.getRealChangeProductSkuCount() + 1);
        changeOrderProductMapper.update(changeOrderProductDO);
        //更新租赁换货设备表，订单编号，原设备编号，差价
        changeOrderProductEquipmentDO.setOrderNo(srcProductEquipmentDO.getOrderNo());
        changeOrderProductEquipmentDO.setSrcEquipmentId(srcProductEquipmentDO.getId());
        changeOrderProductEquipmentDO.setSrcEquipmentNo(srcProductEquipmentDO.getEquipmentNo());
        BigDecimal diff = BigDecimalUtil.sub(destProductEquipmentDO.getEquipmentPrice(), srcProductEquipmentDO.getEquipmentPrice());
        changeOrderProductEquipmentDO.setPriceDiff(diff);
        changeOrderProductEquipmentDO.setUpdateTime(now);
        changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
        //更新换货单实际换货商品总数，总差价，状态
        changeOrderDO.setRealTotalChangeProductCount(changeOrderDO.getRealTotalChangeProductCount() + 1);
        changeOrderDO.setTotalPriceDiff(BigDecimalUtil.add(changeOrderDO.getTotalPriceDiff(), diff));
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS);
        changeOrderMapper.update(changeOrderDO);
        //更新订单商品设备表，实际归还时间，实际租金；添加订单商品设备，预计归还时间
        ServiceResult<String, String> updateResult = orderService.returnEquipment(srcProductEquipmentDO.getOrderNo(), srcProductEquipmentDO.getEquipmentNo(), destProductEquipmentDO.getEquipmentNo(), now);
        if (!ErrorCode.SUCCESS.equals(updateResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return updateResult;
        }
        //更新设备散料锁定状态
        bulkMaterialMapper.updateEquipmentOrderNo(srcProductEquipmentDO.getEquipmentNo(), "");
        bulkMaterialMapper.updateEquipmentOrderNo(destProductEquipmentDO.getEquipmentNo(), srcProductEquipmentDO.getOrderNo());
        //更新设备锁定状态
        destProductEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
        destProductEquipmentDO.setOrderNo(srcProductEquipmentDO.getOrderNo());
        productEquipmentMapper.update(destProductEquipmentDO);
        srcProductEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        srcProductEquipmentDO.setOrderNo("");
        productEquipmentMapper.update(srcProductEquipmentDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> doChangeMaterial(ChangeOrderMaterial changeOrderMaterial) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(changeOrderMaterial.getChangeOrderMaterialId());
        if (changeOrderMaterialDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderMaterialDO.getChangeOrderNo());

        MaterialDO srcMaterialDO = materialMapper.findByNo(changeOrderMaterial.getSrcChangeMaterialNo());
        if (srcMaterialDO == null) {
            serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
            return serviceResult;
        }
        //判断换货单状态是否允许更换操作
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_DO_CHANGE);
            return serviceResult;
        }
        //判断更换数量是否大于客户在租数量
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findRentByCustomerIdAndMaterialId(changeOrderDO.getCustomerId(), srcMaterialDO.getId());
        if (changeOrderMaterial.getRealChangeMaterialCount() > bulkMaterialDOList.size()) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
            return serviceResult;
        }
        //更新租赁换货物料项表，实际换货数量
        if(changeOrderMaterialDO.getRealChangeMaterialCount()>0){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_MATERIAL_CAN_NOT_MODIFY);
            return serviceResult;
        }
        changeOrderMaterialDO.setRealChangeMaterialCount(changeOrderMaterial.getRealChangeMaterialCount());
        changeOrderMaterialMapper.update(changeOrderMaterialDO);
        //如果实际换货的物料数大于预计更换的物料数（从备用库房拿了n个散料换了），则再新增n条租赁散料数据
        Integer diffCount = changeOrderMaterial.getRealChangeMaterialCount() - changeOrderMaterialDO.getChangeMaterialCount();
        if (diffCount >= 0) {
            for (int i = 0; i < diffCount; i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = new ChangeOrderMaterialBulkDO();
                changeOrderMaterialBulkDO.setChangeOrderMaterialId(changeOrderMaterial.getChangeOrderMaterialId());
                changeOrderMaterialBulkDO.setChangeOrderId(changeOrderDO.getId());
                changeOrderMaterialBulkDO.setChangeOrderNo(changeOrderDO.getChangeOrderNo());
                //随机找一个符合的散料-超
                BulkMaterialDO bulkMaterialDO = bulkMaterialSupport.queryFitBulkMaterialDO(null,changeOrderMaterialDO.getChangeMaterialIdDest());
                if(bulkMaterialDO==null){
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return serviceResult;
                }
                changeOrderMaterialBulkDO.setDestBulkMaterialId(bulkMaterialDO.getId());
                changeOrderMaterialBulkDO.setDestBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialBulkDO.setCreateTime(now);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.save(changeOrderMaterialBulkDO);
            }
        }
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.listByChangeOrderPickUp(changeOrderDO.getId());
        //一对一更新租赁换货散料表，订单编号，原散料编号，原设备编号，原设备ID，差价
        BigDecimal totalDiffPrice = changeOrderDO.getTotalPriceDiff();
        for (int i = 0; i < changeOrderMaterial.getRealChangeMaterialCount(); i++) {
            ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = changeOrderMaterialBulkDOList.get(i);
            BulkMaterialDO srcBulkMaterialDO = bulkMaterialDOList.get(i);
            changeOrderMaterialBulkDO.setSrcBulkMaterialId(srcBulkMaterialDO.getId());
            changeOrderMaterialBulkDO.setSrcEquipmentId(srcBulkMaterialDO.getCurrentEquipmentId());
            changeOrderMaterialBulkDO.setSrcEquipmentNo(srcBulkMaterialDO.getCurrentEquipmentNo());
            //todo 这里暂时从数据库取，后面改成直接取对象
//            BulkMaterialDO destBulkMaterialDO = changeOrderMaterialBulkDO.getDestBulkMaterialDO();
            BulkMaterialDO destBulkMaterialDO = bulkMaterialMapper.findByNo(changeOrderMaterialBulkDO.getDestBulkMaterialNo());
            BigDecimal diffPrice = BigDecimalUtil.sub(destBulkMaterialDO.getBulkMaterialPrice(), srcBulkMaterialDO.getBulkMaterialPrice());
            totalDiffPrice = BigDecimalUtil.add(totalDiffPrice, diffPrice);
            changeOrderMaterialBulkDO.setPriceDiff(diffPrice);
            changeOrderMaterialBulkDO.setUpdateTime(now);
            changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderMaterialBulkMapper.update(changeOrderMaterialBulkDO);
            //更新订单物料散料表，实际归还时间，实际租金；添加订单物料散料，预计归还时间
            ServiceResult<String,String> returnBulkMaterialResult = orderService.returnBulkMaterial(srcBulkMaterialDO.getOrderNo(),srcBulkMaterialDO.getBulkMaterialNo(),destBulkMaterialDO.getBulkMaterialNo(),now);
            if(!ErrorCode.SUCCESS.equals(returnBulkMaterialResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return returnBulkMaterialResult;
            }
            //如果原散料在设备上调用拆卸安装接口  否则直接修改散料状态
            if(srcBulkMaterialDO.getCurrentEquipmentId()!=null){
                ServiceResult<String,Integer> dismantleAndInstallResult = bulkMaterialService.changeProductDismantleAndInstall(srcBulkMaterialDO.getId(),destBulkMaterialDO.getId());
                if(!dismantleAndInstallResult.getErrorCode().equals(ErrorCode.SUCCESS)){
                    serviceResult.setErrorCode(dismantleAndInstallResult.getErrorCode(),dismantleAndInstallResult.getFormatArgs());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return serviceResult;
                }
            }else{
                destBulkMaterialDO.setOrderNo(srcBulkMaterialDO.getOrderNo());
                destBulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                bulkMaterialMapper.update(destBulkMaterialDO);
                srcBulkMaterialDO.setOrderNo("");
                srcBulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialMapper.update(srcBulkMaterialDO);
            }
        }
        //更新换货单实际换货物料总数，总差价，状态
        changeOrderDO.setRealTotalChangeMaterialCount(changeOrderDO.getRealTotalChangeMaterialCount() + changeOrderMaterial.getRealChangeMaterialCount());
        changeOrderDO.setTotalPriceDiff(totalDiffPrice);
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS);
        changeOrderMapper.update(changeOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);

        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> processNoChangeEquipment(ProcessNoChangeEquipmentParam processNoChangeEquipmentParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(processNoChangeEquipmentParam.getChangeOrderNo());
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_CAN_NOT_UPDATE);
            return result;
        }

        //删除更换后设备，删除更换后物料
        ServiceResult<String, Object> removeOrderDestItemResult = removeOrderDestItemResultDest(processNoChangeEquipmentParam.getEquipmentNo(),null,null, changeOrderDO);
        if (!ErrorCode.SUCCESS.equals(removeOrderDestItemResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(removeOrderDestItemResult.getErrorCode());
            return result;
        }

        result.setResult(changeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> end(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //不是处理中的换货单不允许结束
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_END);
            return serviceResult;
        }
        //校验是否目标设备全部换了，有没换的不允许结束
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
        for(ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList){
            if(changeOrderProductEquipmentDO.getSrcEquipmentId()==null){
                serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_END);
                return serviceResult;
            }
        }
        Date now = new Date();
        //如果目标散料没有全部换，则还原散备货料为空闲状态，删除换货单散料项数据
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
        for(ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList){
            if(changeOrderMaterialBulkDO.getSrcBulkMaterialId()==null){
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(changeOrderMaterialBulkDO.getDestBulkMaterialId());
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialMapper.update(bulkMaterialDO);
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.update(changeOrderMaterialBulkDO);
            }
        }
        //todo 结算插入数据
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_END);
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> cancel(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        Date now = new Date();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //判断何时可以取消
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP.equals(changeOrderDO.getChangeOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_CANCEL);
            return serviceResult;
        }

        //已经备货或者等等取货状态的换货单，取消时需要还原设备及散料的锁定状态
        if (ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus()) ||
                ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP.equals(changeOrderDO.getChangeOrderStatus())) {
            //获取换货单所有设备
            List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());

            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                //还原设备所有散料锁定状态
                bulkMaterialMapper.updateEquipmentBulkMaterialStatus(changeOrderProductEquipmentDO.getDestEquipmentNo(), BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                //还原设备的锁定状态
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipmentDO.getDestEquipmentNo());
                productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
                productEquipmentDO.setOrderNo("");
                productEquipmentMapper.update(productEquipmentDO);
            }

            //获取换货单所有散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
            //还原所有散料的锁定状态
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                //还原单条
                BulkMaterialDO bulkMaterialDO = new BulkMaterialDO();
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDO.setId(changeOrderMaterialBulkDO.getDestBulkMaterialId());
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_CANCEL);
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, ChangeOrder> detail(ChangeOrder changeOrder) {
        ServiceResult<String, ChangeOrder> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        serviceResult.setResult(ConverterUtil.convert(changeOrderDO,ChangeOrder.class));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrder>> page(ChangeOrderPageParam changeOrderPageParam) {

        ServiceResult<String, Page<ChangeOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(changeOrderPageParam.getPageNo(), changeOrderPageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", changeOrderPageParam);

        Integer totalCount = changeOrderMapper.findChangeOrderCountByParams(maps);
        List<ChangeOrderDO> changeOrderDOList = changeOrderMapper.findChangeOrderByParams(maps);
        List<ChangeOrder> purchaseOrderList = ConverterUtil.convertList(changeOrderDOList,ChangeOrder.class);
        Page<ChangeOrder> page = new Page<>(purchaseOrderList, totalCount, changeOrderPageParam.getPageNo(), changeOrderPageParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrderProductEquipment>> pageChangeOrderProductEquipment(ChangeEquipmentPageParam changeEquipmentPageParam) {
        ServiceResult<String, Page<ChangeOrderProductEquipment>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(changeEquipmentPageParam.getPageNo(), changeEquipmentPageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", changeEquipmentPageParam);

        Integer totalCount = changeOrderProductEquipmentMapper.listCount(maps);
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.listPage(maps);
        List<ChangeOrderProductEquipment> changeOrderProductEquipmentList = ConverterUtil.convertList(changeOrderProductEquipmentDOList,ChangeOrderProductEquipment.class);
        for(ChangeOrderProductEquipment changeOrderProductEquipment : changeOrderProductEquipmentList){
            if(StringUtil.isNotEmpty(changeOrderProductEquipment.getSrcEquipmentNo())){
                ProductEquipmentDO srcProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipment.getSrcEquipmentNo());
                changeOrderProductEquipment.setSrcProductEquipment(ConverterUtil.convert(srcProductEquipmentDO,ProductEquipment.class));
            }
            if(StringUtil.isNotEmpty(changeOrderProductEquipment.getDestEquipmentNo())){
                ProductEquipmentDO destProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipment.getDestEquipmentNo());
                changeOrderProductEquipment.setDestProductEquipment(ConverterUtil.convert(destProductEquipmentDO,ProductEquipment.class));
            }
        }
        Page<ChangeOrderProductEquipment> page = new Page<>(changeOrderProductEquipmentList, totalCount, changeEquipmentPageParam.getPageNo(), changeEquipmentPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrderMaterialBulk>> pageChangeOrderMaterialBulk(ChangeBulkPageParam changeBulkPageParam) {
        ServiceResult<String, Page<ChangeOrderMaterialBulk>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(changeBulkPageParam.getPageNo(), changeBulkPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", changeBulkPageParam);

        Integer totalCount = changeOrderMaterialBulkMapper.listCount(maps);
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.listPage(maps);
        List<ChangeOrderMaterialBulk> changeOrderMaterialBulkList = ConverterUtil.convertList(changeOrderMaterialBulkDOList,ChangeOrderMaterialBulk.class);
        for(ChangeOrderMaterialBulk changeOrderMaterialBulk : changeOrderMaterialBulkList){
            if(StringUtil.isNotEmpty(changeOrderMaterialBulk.getSrcBulkMaterialNo())){
                BulkMaterialDO srcBulkMaterialDO = bulkMaterialMapper.findByNo(changeOrderMaterialBulk.getSrcBulkMaterialNo());
                changeOrderMaterialBulk.setSrcBulkMaterial(ConverterUtil.convert(srcBulkMaterialDO,BulkMaterial.class));
            }
            if(StringUtil.isNotEmpty(changeOrderMaterialBulk.getDestBulkMaterialNo())){
                BulkMaterialDO destBulkMaterialDO = bulkMaterialMapper.findByNo(changeOrderMaterialBulk.getDestBulkMaterialNo());
                changeOrderMaterialBulk.setDestBulkMaterial(ConverterUtil.convert(destBulkMaterialDO,BulkMaterial.class));
            }
        }
        Page<ChangeOrderMaterialBulk> page = new Page<>(changeOrderMaterialBulkList, totalCount, changeBulkPageParam.getPageNo(), changeBulkPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    private ServiceResult<String, Object> addOrderDestItemResultDest(StockUpForChangeParam param, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getEquipmentNo())) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(param.getEquipmentNo());
            if (productEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                return serviceResult;
            }
            //不是空闲则不可以用
            if(!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())){
                serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE,productEquipmentDO.getEquipmentNo());
                return serviceResult;
            }
            //商品非在租则不可用
            ProductDO productDO = productMapper.findByProductId(productEquipmentDO.getProductId());
            if(productDO==null||CommonConstant.COMMON_CONSTANT_NO.equals(productDO.getIsRent())){
                serviceResult.setErrorCode(ErrorCode.PRODUCT_IS_NOT_RENT);
                return serviceResult;
            }
            //根据换货单号查找换货单所有商品项
            List<ChangeOrderProductDO> changeOrderProductDOList = changeOrderProductMapper.findByChangeOrderId(changeOrderDO.getId());
            //根据换货单号查找换货单所有目前备货设备
            List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(param.getChangeOrderNo());
            Map<Integer, Integer> changeOrderProductEquipmentMap = new HashMap<>();
            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                Integer skuId = changeOrderProductEquipmentDO.getDestSkuId();
                if (!changeOrderProductEquipmentMap.containsKey(skuId)) {
                    changeOrderProductEquipmentMap.put(skuId, 0);
                }
                changeOrderProductEquipmentMap.put(skuId, changeOrderProductEquipmentMap.get(skuId) + 1);
            }
            //按照destSku归集后的countMap
            Map<Integer, Integer> changeOrderProductCountMap = new HashMap<>();
            //按照destSku归集后的changeOrderProductDOList Map
            Map<Integer, List<ChangeOrderProductDO>> changeOrderProductDOListMap = new HashMap<>();
            for (ChangeOrderProductDO changeOrderProductDO : changeOrderProductDOList) {
                if (!changeOrderProductCountMap.containsKey(changeOrderProductDO.getChangeProductSkuIdDest())) {
                    changeOrderProductCountMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), 0);
                }
                changeOrderProductCountMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), changeOrderProductDO.getChangeProductSkuCount() + changeOrderProductCountMap.get(changeOrderProductDO.getChangeProductSkuIdDest()));
                if (!changeOrderProductDOListMap.containsKey(changeOrderProductDO.getChangeProductSkuIdDest())) {
                    changeOrderProductDOListMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), new ArrayList<ChangeOrderProductDO>());
                }
                changeOrderProductDOListMap.get(changeOrderProductDO.getChangeProductSkuIdDest()).add(changeOrderProductDO);
            }
            //计算是否可备货，可备货总数-已备货总数大于等于1可备货
            Integer canStock = changeOrderProductCountMap.get(productEquipmentDO.getSkuId())==null?0:changeOrderProductCountMap.get(productEquipmentDO.getSkuId());
            Integer stockYetCount = changeOrderProductEquipmentMap.get(productEquipmentDO.getSkuId())==null?0:changeOrderProductEquipmentMap.get(productEquipmentDO.getSkuId());
            if ( canStock - stockYetCount < 1) {
                serviceResult.setErrorCode(ErrorCode.STOCK_NOT_MATCH);
                return serviceResult;
            }
            Date now = new Date();
            //保存换货单设备项
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = new ChangeOrderProductEquipmentDO();
            ChangeOrderProductDO changeOrderProductDO = changeOrderProductDOListMap.get(productEquipmentDO.getSkuId()).get(0);
            changeOrderProductEquipmentDO.setChangeOrderProductId(changeOrderProductDO.getId());
            changeOrderProductEquipmentDO.setChangeOrderId(changeOrderProductDO.getChangeOrderId());
            changeOrderProductEquipmentDO.setChangeOrderNo(changeOrderProductDO.getChangeOrderNo());
            changeOrderProductEquipmentDO.setDestEquipmentId(productEquipmentDO.getId());
            changeOrderProductEquipmentDO.setDestEquipmentNo(productEquipmentDO.getEquipmentNo());
            changeOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            changeOrderProductEquipmentDO.setCreateTime(now);
            changeOrderProductEquipmentDO.setUpdateTime(now);
            changeOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentMapper.save(changeOrderProductEquipmentDO);
            //更新设备状态
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
            productEquipmentMapper.update(productEquipmentDO);
            //更新散料状态
            bulkMaterialMapper.updateEquipmentBulkMaterialStatus(productEquipmentDO.getEquipmentNo(),BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
        }
        // 如果输入进来的物料ID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getMaterialNo())) {
            if(param.getMaterialCount()==null||param.getMaterialCount()<=0){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_COUNT_ERROR);
                return serviceResult;
            }
            MaterialDO materialDO = materialMapper.findByNo(param.getMaterialNo());
            if (materialDO == null) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return serviceResult;
            }
            //根据换货单号查找换货单所有物料项
            List<ChangeOrderMaterialDO> changeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
            //根据换货单号查找换货单所有目前备货散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(param.getChangeOrderNo());
            Map<Integer, Integer> changeOrderMaterialBulkMap = new HashMap<>();
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                Integer materialId = changeOrderMaterialBulkDO.getDestMaterialId();
                if (!changeOrderMaterialBulkMap.containsKey(materialId)) {
                    changeOrderMaterialBulkMap.put(materialId, 0);
                }
                changeOrderMaterialBulkMap.put(materialId, changeOrderMaterialBulkMap.get(materialId) + 1);
            }
            //按照destMaterial归集后的countMap
            Map<Integer, Integer> changeOrderMaterialCountMap = new HashMap<>();
            //按照destSku归集后的changeOrderMaterialDOListMap Map
            Map<Integer, List<ChangeOrderMaterialDO>> changeOrderMaterialDOListMap = new HashMap<>();
            for (ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderMaterialDOList) {
                if (!changeOrderMaterialCountMap.containsKey(changeOrderMaterialDO.getChangeMaterialIdDest())) {
                    changeOrderMaterialCountMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), 0);
                }
                changeOrderMaterialCountMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), changeOrderMaterialDO.getChangeMaterialCount() + changeOrderMaterialCountMap.get(changeOrderMaterialDO.getChangeMaterialIdDest()));
                if (!changeOrderMaterialDOListMap.containsKey(changeOrderMaterialDO.getChangeMaterialIdDest())) {
                    changeOrderMaterialDOListMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), new ArrayList<ChangeOrderMaterialDO>());
                }
                changeOrderMaterialDOListMap.get(changeOrderMaterialDO.getChangeMaterialIdDest()).add(changeOrderMaterialDO);
            }
            Integer canStock = changeOrderMaterialCountMap.get(materialDO.getId())==null?0:changeOrderMaterialCountMap.get(materialDO.getId());
            Integer stockYetCount = changeOrderMaterialBulkMap.get(materialDO.getId())==null?0:changeOrderMaterialBulkMap.get(materialDO.getId());
            //计算是否可备货，可备货总数-已备货总数-本次备货数 大于等于0可备货
            if (canStock - stockYetCount - param.getMaterialCount() < 0) {
                serviceResult.setErrorCode(ErrorCode.STOCK_NOT_MATCH);
                return serviceResult;
            }
            Date now = new Date();
            //保存换货单散料项
            for (int i = 0; i < param.getMaterialCount(); i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = new ChangeOrderMaterialBulkDO();
                ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialDOListMap.get(materialDO.getId()).get(0);
                changeOrderMaterialBulkDO.setChangeOrderMaterialId(changeOrderMaterialDO.getId());
                changeOrderMaterialBulkDO.setChangeOrderId(changeOrderMaterialDO.getChangeOrderId());
                changeOrderMaterialBulkDO.setChangeOrderNo(changeOrderMaterialDO.getChangeOrderNo());
                //随机找一个可用散料
                BulkMaterialDO bulkMaterialDO = bulkMaterialSupport.queryFitBulkMaterialDO(null,materialDO.getId());
                if(bulkMaterialDO==null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    return serviceResult;
                }
                changeOrderMaterialBulkDO.setDestBulkMaterialId(bulkMaterialDO.getId());
                changeOrderMaterialBulkDO.setDestBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialBulkDO.setCreateTime(now);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.save(changeOrderMaterialBulkDO);

                //更新散料状态
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String, Object> removeOrderDestItemResultDest(String equipmentDO,String materialNo,Integer materialCount, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        Date now = new Date();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(equipmentDO)) {
            //根据换货单号和设备号查找备货设备
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = changeOrderProductEquipmentMapper.findByChangeOrderNoAndEquipmentNo(changeOrderDO.getChangeOrderNo(),equipmentDO);
            if (changeOrderProductEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            }
            changeOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            changeOrderProductEquipmentDO.setUpdateTime(now);
            changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
            //更新散料状态
            bulkMaterialMapper.updateEquipmentBulkMaterialStatus(changeOrderProductEquipmentDO.getDestEquipmentNo(),BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
        }
        // 如果输入进来的物料ID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(materialNo)) {
            if(materialCount==null||materialCount<=0){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_COUNT_ERROR);
            }
            MaterialDO materialDO = materialMapper.findByNo(materialNo);
            if (materialDO == null) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return serviceResult;
            }
            //根据换货单号和物料ID查找目前备货散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNoAndMaterialNo(changeOrderDO.getChangeOrderNo(),materialNo);
            if (CollectionUtil.isEmpty(changeOrderMaterialBulkDOList) || changeOrderMaterialBulkDOList.size() < materialCount) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_MATERIAL_NOT_MATCHING);
            }
            //待更新的散料
            List<BulkMaterialDO> bulkMaterialDOListForUpdate = new ArrayList<>();
            for (int i = 0; i < materialCount; i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = changeOrderMaterialBulkDOList.get(i);
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                //更新的换货散料表数据
                changeOrderMaterialBulkMapper.update(changeOrderMaterialBulkDO);
                BulkMaterialDO bulkMaterialDO = changeOrderMaterialBulkDO.getDestBulkMaterialDO();
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDOListForUpdate.add(bulkMaterialDO);
            }
            //批量更新散料状态
            bulkMaterialMapper.updateList(bulkMaterialDOListForUpdate);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private ChangeOrderMapper changeOrderMapper;
    @Autowired
    private ChangeOrderConsignInfoMapper changeOrderConsignInfoMapper;
    @Autowired
    private ChangeOrderProductMapper changeOrderProductMapper;
    @Autowired
    private ChangeOrderMaterialMapper changeOrderMaterialMapper;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private ChangeOrderProductEquipmentMapper changeOrderProductEquipmentMapper;
    @Autowired
    private ChangeOrderMaterialBulkMapper changeOrderMaterialBulkMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BulkMaterialService bulkMaterialService;
    @Autowired
    private ProductMapper productMapper;
}
