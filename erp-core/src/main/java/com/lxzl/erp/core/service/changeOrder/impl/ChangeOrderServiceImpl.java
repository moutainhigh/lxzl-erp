package com.lxzl.erp.core.service.changeOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.*;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.annotations.Param;
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(AddChangeOrderParam addChangeOrderParam) {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addChangeOrderParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        List<ChangeOrderProduct> changeOrderProductList = addChangeOrderParam.getChangeOrderProductList();
        List<ChangeOrderMaterial> changeOrderMaterialList = addChangeOrderParam.getChangeOrderMaterialList();
        if (CollectionUtil.isEmpty(changeOrderProductList) && CollectionUtil.isEmpty(changeOrderMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        UserDO owner = userMapper.findByUserId(addChangeOrderParam.getOwner());
        if(owner==null){
            serviceResult.setErrorCode(ErrorCode.OWNER_NOT_NULL);
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
        //用户在租配件统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = 0;
        //累计换货配件总数
        Integer totalChangeMaterialCount = 0;
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOList = new ArrayList<>();
        //保存用到的skuMap,key<skuId>,value<Product>
        Map<Integer,Product> skuMap = new HashMap<>();

        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderProductList)) {
            //校验是否有重复
            Map<String,ChangeOrderProduct> changeOrderProductMap = ListUtil.listToMap(changeOrderProductList,"srcChangeProductSkuId","destChangeProductSkuId","isNew");
            if(changeOrderProductMap.size()<changeOrderProductList.size()){
                serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                return serviceResult;
            }
            //记录每种原sku累加后的总数,key为skuId
            Map<Integer,Integer> srcSkuCountMap = new HashMap<>();
            //记录每种目标sku累加后的总数,key为skuId_新旧属性
            Map<String,Integer> destSkuCountMap = new HashMap<>();
            for (ChangeOrderProduct changeOrderProduct : changeOrderProductList) {
                if(!skuMap.containsKey(changeOrderProduct.getSrcChangeProductSkuId())){
                    ServiceResult<String, Product> productResult = productService.queryProductBySkuId(changeOrderProduct.getSrcChangeProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode()) || productResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    skuMap.put(changeOrderProduct.getSrcChangeProductSkuId(),productResult.getResult());
                }
                if(!skuMap.containsKey(changeOrderProduct.getDestChangeProductSkuId())){
                    ServiceResult<String, Product> productResult = productService.queryProductBySkuId(changeOrderProduct.getDestChangeProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode()) || productResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    skuMap.put(changeOrderProduct.getDestChangeProductSkuId(),productResult.getResult());
                }

                //获取原商品，目标商品
                Product srcProduct = skuMap.get(changeOrderProduct.getSrcChangeProductSkuId());
                Product destProduct = skuMap.get(changeOrderProduct.getDestChangeProductSkuId());
                if(!srcProduct.getProductId().equals(destProduct.getProductId())){
                    //如果原设备和目标设备不是同一商品，则不允许更换
                    serviceResult.setErrorCode(ErrorCode.CHANGE_SRC_DEST_SHOULD_SAME_PRODUCT);
                    return serviceResult;
                }

                //累加每种原sku总数量，为后面校验用户是否有足够的在租sku做准备
                if(!srcSkuCountMap.containsKey(changeOrderProduct.getSrcChangeProductSkuId())){
                    srcSkuCountMap.put(changeOrderProduct.getSrcChangeProductSkuId(),changeOrderProduct.getChangeProductSkuCount());
                }else{
                    Integer count = srcSkuCountMap.get(changeOrderProduct.getSrcChangeProductSkuId());
                    srcSkuCountMap.put(changeOrderProduct.getSrcChangeProductSkuId(),count+changeOrderProduct.getChangeProductSkuCount());
                }
                //累加每种目标sku总数量，为后面校验用户是否有足够的sku库存做准备
                String destKey = changeOrderProduct.getDestChangeProductSkuId()+"_"+changeOrderProduct.getIsNew();
                if(!destSkuCountMap.containsKey(destKey)){
                    destSkuCountMap.put(destKey,changeOrderProduct.getChangeProductSkuCount());
                }else{
                    Integer count = destSkuCountMap.get(destKey);
                    destSkuCountMap.put(destKey,count+changeOrderProduct.getChangeProductSkuCount());
                }
                totalChangeProductCount += changeOrderProduct.getChangeProductSkuCount();
                ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                changeOrderProductDO.setSrcChangeProductSkuId(changeOrderProduct.getSrcChangeProductSkuId());
                changeOrderProductDO.setDestChangeProductSkuId(changeOrderProduct.getDestChangeProductSkuId());
                changeOrderProductDO.setChangeProductSkuCount(changeOrderProduct.getChangeProductSkuCount());
                changeOrderProductDO.setRealChangeProductSkuCount(0);
                changeOrderProductDO.setSrcChangeProductSkuSnapshot(JSON.toJSONString(srcProduct));
                changeOrderProductDO.setDestChangeProductSkuSnapshot(JSON.toJSONString(destProduct));
                changeOrderProductDO.setIsNew(changeOrderProduct.getIsNew());
                changeOrderProductDO.setOwner(addChangeOrderParam.getOwner());
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setCreateTime(now);
                changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setUpdateTime(now);
                changeOrderProductDOList.add(changeOrderProductDO);
            }
            for(Integer skuId : srcSkuCountMap.keySet()){
                ProductSkuDO productSkuDO = oldSkuCountMap.get(skuId);
                if (productSkuDO == null) {
                    //如果要更换的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                //如果原skuId数量大于在租数量
                if(srcSkuCountMap.get(skuId)>productSkuDO.getCanProcessCount()){
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
            }
            for(String skuIdAndIsNew : destSkuCountMap.keySet()){
                String[] keys = skuIdAndIsNew.split("_");
                Integer destSkuId = Integer.parseInt(keys[0]);
                Integer isNew = Integer.parseInt(keys[1]);
                Integer changeCount = destSkuCountMap.get(skuIdAndIsNew);
                ProductSku productSku = skuMap.get(destSkuId).getProductSkuList().get(0);
                if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)&&productSku.getOldProductSkuCount()<changeCount){
                    //该SKU次新设备不足
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
                if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&productSku.getNewProductSkuCount()<changeCount){
                    //该SKU全新新设备不足
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }
        //构造待保存换货单配件项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = new ArrayList<>();
        //保存用到的配件Map
        Map<String,Material> materialMap = new HashMap<>();
        //如果要换货的配件不在在租列表，或者要换货的配件数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderMaterialList)) {
            //校验是否有重复
            Map<String,ChangeOrderMaterial> changeOrderMaterialMap = ListUtil.listToMap(changeOrderMaterialList,"srcChangeMaterialNo","destChangeMaterialNo","isNew");
            if(changeOrderMaterialMap.size()<changeOrderMaterialList.size()){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                return serviceResult;
            }
            //记录每种原配件累加后的总数,key为配件编号
            Map<String,Integer> srcMaterialCountMap = new HashMap<>();
            //记录每种目标配件累加后的总数,key为配件编号_新旧属性
            Map<String,Integer> destMaterialCountMap = new HashMap<>();
            for (ChangeOrderMaterial changeOrderMaterial : changeOrderMaterialList) {
                if(!materialMap.containsKey(changeOrderMaterial.getSrcChangeMaterialNo())){
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialByNo(changeOrderMaterial.getSrcChangeMaterialNo());
                    if (!ErrorCode.SUCCESS.equals(materialResult.getErrorCode()) || materialResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        return serviceResult;
                    }
                    materialMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),materialResult.getResult());
                }
                if(!materialMap.containsKey(changeOrderMaterial.getDestChangeMaterialNo())){
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialByNo(changeOrderMaterial.getDestChangeMaterialNo());
                    if (!ErrorCode.SUCCESS.equals(materialResult.getErrorCode()) || materialResult.getResult() == null||CommonConstant.COMMON_CONSTANT_NO.equals(materialResult.getResult().getIsRent())) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        return serviceResult;
                    }
                    materialMap.put(changeOrderMaterial.getDestChangeMaterialNo(),materialResult.getResult());
                }
                //获取原配件，目标配件
                Material srcMaterial = materialMap.get(changeOrderMaterial.getSrcChangeMaterialNo());
                Material destMaterial = materialMap.get(changeOrderMaterial.getDestChangeMaterialNo());
                if(!srcMaterial.getMaterialType().equals(destMaterial.getMaterialType())){
                    //如果原配件和目标配件不是同一配件类型，则不允许更换
                    serviceResult.setErrorCode(ErrorCode.CHANGE_SRC_DEST_SHOULD_SAME_TYPE);
                    return serviceResult;
                }

                //累加每种原配件总数量，为后面校验用户是否有足够的在租配件做准备
                if(!srcMaterialCountMap.containsKey(changeOrderMaterial.getSrcChangeMaterialNo())){
                    srcMaterialCountMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),changeOrderMaterial.getChangeMaterialCount());
                }else{
                    Integer count = srcMaterialCountMap.get(changeOrderMaterial.getSrcChangeMaterialNo());
                    srcMaterialCountMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),count+changeOrderMaterial.getChangeMaterialCount());
                }
                //累加每种目标配件总数量，为后面校验用户是否有足够的配件库存做准备
                String destKey = changeOrderMaterial.getDestChangeMaterialNo()+"_"+changeOrderMaterial.getIsNew();
                if(!destMaterialCountMap.containsKey(destKey)){
                    destMaterialCountMap.put(destKey,changeOrderMaterial.getChangeMaterialCount());
                }else{
                    Integer count = destMaterialCountMap.get(destKey);
                    destMaterialCountMap.put(destKey,count+changeOrderMaterial.getChangeMaterialCount());
                }
                totalChangeMaterialCount += changeOrderMaterial.getChangeMaterialCount();
                ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                changeOrderMaterialDO.setSrcChangeMaterialId(srcMaterial.getMaterialId());
                changeOrderMaterialDO.setDestChangeMaterialId(destMaterial.getMaterialId());
                changeOrderMaterialDO.setSrcChangeMaterialSnapshot(JSON.toJSONString(srcMaterial));
                changeOrderMaterialDO.setDestChangeMaterialSnapshot(JSON.toJSONString(destMaterial));
                changeOrderMaterialDO.setChangeMaterialCount(changeOrderMaterial.getChangeMaterialCount());
                changeOrderMaterialDO.setRealChangeMaterialCount(0);
                changeOrderMaterialDO.setIsNew(changeOrderMaterial.getIsNew());
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialDO.setCreateTime(now);
                changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDO.setUpdateTime(now);
                changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDOList.add(changeOrderMaterialDO);
            }
            for(String materialNo : srcMaterialCountMap.keySet()){
                MaterialDO materialDO = oldMaterialCountMap.get(materialNo);
                if (materialDO == null) {
                    //如果要更换的配件不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                //如果原配件数量大于在租数量
                if(srcMaterialCountMap.get(materialNo)>materialDO.getCanProcessCount()){
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
            }
            for(String materialNoAndIsNew : destMaterialCountMap.keySet()){
                String[] keys = materialNoAndIsNew.split("_");
                String destMaterialNo = keys[0];
                Integer isNew = Integer.parseInt(keys[1]);
                Integer changeCount = destMaterialCountMap.get(materialNoAndIsNew);
                Material material = materialMap.get(destMaterialNo);
                if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)&&material.getOldMaterialCount()<changeCount){
                    //该配件次新设备不足
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    return serviceResult;
                }
                if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&material.getNewMaterialCount()<changeCount){
                    //该配件全新新设备不足
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }
        //创建换货单
        ChangeOrderDO changeOrderDO = new ChangeOrderDO();
        changeOrderDO.setChangeOrderNo(generateNoSupport.generateChangeOrderNo(now, customerDO.getId()));
        changeOrderDO.setRentStartTime(addChangeOrderParam.getRentStartTime());
        changeOrderDO.setCustomerId(customerDO.getId());
        changeOrderDO.setCustomerNo(customerDO.getCustomerNo());
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReasonType(addChangeOrderParam.getChangeReasonType());
        changeOrderDO.setChangeReason(addChangeOrderParam.getChangeReason());
        changeOrderDO.setChangeMode(addChangeOrderParam.getChangeMode());
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
        changeOrderDO.setOwner(addChangeOrderParam.getOwner());
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

        //保存换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOList)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOList);
        }
        //保存换货配件项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOList)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOList);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
        List<ChangeOrderProduct> changeOrderProductList = updateChangeOrderParam.getChangeOrderProductList();
        List<ChangeOrderMaterial> changeOrderMaterialList = updateChangeOrderParam.getChangeOrderMaterialList();
        if (CollectionUtil.isEmpty(changeOrderProductList) && CollectionUtil.isEmpty(changeOrderMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        List<ChangeOrderProductDO> oldChangeOrderProductDOList = changeOrderProductMapper.findByChangeOrderId(changeOrderDO.getId());
        List<ChangeOrderMaterialDO> oldChangeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
        if(CollectionUtil.isNotEmpty(oldChangeOrderProductDOList)){
            for(ChangeOrderProductDO changeOrderProductDO : oldChangeOrderProductDOList){
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderProductMapper.update(changeOrderProductDO);
            }
        }
        if(CollectionUtil.isNotEmpty(oldChangeOrderMaterialDOList)){
            for(ChangeOrderMaterialDO changeOrderMaterialDO : oldChangeOrderMaterialDOList){
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialMapper.update(changeOrderMaterialDO);
            }
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
        //用户在租配件统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = changeOrderDO.getTotalChangeProductCount();
        //累计换货配件总数
        Integer totalChangeMaterialCount = changeOrderDO.getTotalChangeMaterialCount();
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOList = new ArrayList<>();
        //保存用到的skuMap,key<skuId>,value<Product>
        Map<Integer,Product> skuMap = new HashMap<>();

        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderProductList)) {
            //校验是否有重复
            Map<String,ChangeOrderProduct> changeOrderProductMap = ListUtil.listToMap(changeOrderProductList,"srcChangeProductSkuId","destChangeProductSkuId","isNew");
            if(changeOrderProductMap.size()<changeOrderProductList.size()){
                serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                return serviceResult;
            }
            //记录每种原sku累加后的总数,key为skuId
            Map<Integer,Integer> srcSkuCountMap = new HashMap<>();
            //记录每种目标sku累加后的总数,key为skuId_新旧属性
            Map<String,Integer> destSkuCountMap = new HashMap<>();

            for (ChangeOrderProduct changeOrderProduct : changeOrderProductList) {
                if(!skuMap.containsKey(changeOrderProduct.getSrcChangeProductSkuId())){
                    ServiceResult<String, Product> productResult = productService.queryProductBySkuId(changeOrderProduct.getSrcChangeProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode()) || productResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    skuMap.put(changeOrderProduct.getSrcChangeProductSkuId(),productResult.getResult());
                }
                if(!skuMap.containsKey(changeOrderProduct.getDestChangeProductSkuId())){
                    ServiceResult<String, Product> productResult = productService.queryProductBySkuId(changeOrderProduct.getDestChangeProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode()) || productResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                        return serviceResult;
                    }
                    skuMap.put(changeOrderProduct.getDestChangeProductSkuId(),productResult.getResult());
                }

                //获取原商品，目标商品
                Product srcProduct = skuMap.get(changeOrderProduct.getSrcChangeProductSkuId());
                Product destProduct = skuMap.get(changeOrderProduct.getDestChangeProductSkuId());
                if(!srcProduct.getProductId().equals(destProduct.getProductId())){
                    //如果原设备和目标设备不是同一商品，则不允许更换
                    serviceResult.setErrorCode(ErrorCode.CHANGE_SRC_DEST_SHOULD_SAME_PRODUCT);
                    return serviceResult;
                }

                //累加每种原sku总数量，为后面校验用户是否有足够的在租sku做准备
                if(!srcSkuCountMap.containsKey(changeOrderProduct.getSrcChangeProductSkuId())){
                    srcSkuCountMap.put(changeOrderProduct.getSrcChangeProductSkuId(),changeOrderProduct.getChangeProductSkuCount());
                }else{
                    Integer count = srcSkuCountMap.get(changeOrderProduct.getSrcChangeProductSkuId());
                    srcSkuCountMap.put(changeOrderProduct.getSrcChangeProductSkuId(),count+changeOrderProduct.getChangeProductSkuCount());
                }
                //累加每种目标sku总数量，为后面校验用户是否有足够的sku库存做准备
                String destKey = changeOrderProduct.getDestChangeProductSkuId()+"_"+changeOrderProduct.getIsNew();
                if(!destSkuCountMap.containsKey(destKey)){
                    destSkuCountMap.put(destKey,changeOrderProduct.getChangeProductSkuCount());
                }else{
                    Integer count = destSkuCountMap.get(destKey);
                    destSkuCountMap.put(destKey,count+changeOrderProduct.getChangeProductSkuCount());
                }
                totalChangeProductCount += changeOrderProduct.getChangeProductSkuCount();
                ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                changeOrderProductDO.setSrcChangeProductSkuId(changeOrderProduct.getSrcChangeProductSkuId());
                changeOrderProductDO.setDestChangeProductSkuId(changeOrderProduct.getDestChangeProductSkuId());
                changeOrderProductDO.setChangeProductSkuCount(changeOrderProduct.getChangeProductSkuCount());
                changeOrderProductDO.setRealChangeProductSkuCount(0);
                changeOrderProductDO.setSrcChangeProductSkuSnapshot(JSON.toJSONString(srcProduct));
                changeOrderProductDO.setDestChangeProductSkuSnapshot(JSON.toJSONString(destProduct));
                changeOrderProductDO.setIsNew(changeOrderProduct.getIsNew());
                changeOrderProductDO.setOwner(updateChangeOrderParam.getOwner());
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setCreateTime(now);
                changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setUpdateTime(now);
                changeOrderProductDOList.add(changeOrderProductDO);
            }
            for(Integer skuId : srcSkuCountMap.keySet()){
                ProductSkuDO productSkuDO = oldSkuCountMap.get(skuId);
                if (productSkuDO == null) {
                    //如果要更换的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                //如果原skuId数量大于在租数量
                if(srcSkuCountMap.get(skuId)>productSkuDO.getCanProcessCount()){
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
            }
            for(String skuIdAndIsNew : destSkuCountMap.keySet()){
                String[] keys = skuIdAndIsNew.split("_");
                Integer destSkuId = Integer.parseInt(keys[0]);
                Integer isNew = Integer.parseInt(keys[1]);
                Integer changeCount = destSkuCountMap.get(skuIdAndIsNew);
                ProductSku productSku = skuMap.get(destSkuId).getProductSkuList().get(0);
                if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)&&productSku.getOldProductSkuCount()<changeCount){
                    //该SKU次新设备不足
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
                if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&productSku.getNewProductSkuCount()<changeCount){
                    //该SKU全新新设备不足
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }
        //构造待保存换货单配件项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = new ArrayList<>();
        //保存用到的配件Map
        Map<String,Material> materialMap = new HashMap<>();
        //如果要换货的配件不在在租列表，或者要换货的配件数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderMaterialList)) {
            //校验是否有重复
            Map<String,ChangeOrderMaterial> changeOrderMaterialMap = ListUtil.listToMap(changeOrderMaterialList,"srcChangeMaterialNo","destChangeMaterialNo","isNew");
            if(changeOrderMaterialMap.size()<changeOrderMaterialList.size()){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                return serviceResult;
            }
            //记录每种原配件累加后的总数,key为配件编号
            Map<String,Integer> srcMaterialCountMap = new HashMap<>();
            //记录每种目标配件累加后的总数,key为配件编号_新旧属性
            Map<String,Integer> destMaterialCountMap = new HashMap<>();
            for (ChangeOrderMaterial changeOrderMaterial : changeOrderMaterialList) {
                if(!materialMap.containsKey(changeOrderMaterial.getSrcChangeMaterialNo())){
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialByNo(changeOrderMaterial.getSrcChangeMaterialNo());
                    if (!ErrorCode.SUCCESS.equals(materialResult.getErrorCode()) || materialResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        return serviceResult;
                    }
                    materialMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),materialResult.getResult());
                }
                if(!materialMap.containsKey(changeOrderMaterial.getDestChangeMaterialNo())){
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialByNo(changeOrderMaterial.getDestChangeMaterialNo());
                    if (!ErrorCode.SUCCESS.equals(materialResult.getErrorCode()) || materialResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        return serviceResult;
                    }
                    materialMap.put(changeOrderMaterial.getDestChangeMaterialNo(),materialResult.getResult());
                }
                //获取原配件，目标配件
                Material srcMaterial = materialMap.get(changeOrderMaterial.getSrcChangeMaterialNo());
                Material destMaterial = materialMap.get(changeOrderMaterial.getDestChangeMaterialNo());
                if(!srcMaterial.getMaterialType().equals(destMaterial.getMaterialType())){
                    //如果原配件和目标配件不是同一配件类型，则不允许更换
                    serviceResult.setErrorCode(ErrorCode.CHANGE_SRC_DEST_SHOULD_SAME_TYPE);
                    return serviceResult;
                }

                //累加每种原配件总数量，为后面校验用户是否有足够的在租配件做准备
                if(!srcMaterialCountMap.containsKey(changeOrderMaterial.getSrcChangeMaterialNo())){
                    srcMaterialCountMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),changeOrderMaterial.getChangeMaterialCount());
                }else{
                    Integer count = srcMaterialCountMap.get(changeOrderMaterial.getSrcChangeMaterialNo());
                    srcMaterialCountMap.put(changeOrderMaterial.getSrcChangeMaterialNo(),count+changeOrderMaterial.getChangeMaterialCount());
                }
                //累加每种目标配件总数量，为后面校验用户是否有足够的配件库存做准备
                String destKey = changeOrderMaterial.getDestChangeMaterialNo()+"_"+changeOrderMaterial.getIsNew();
                if(!destMaterialCountMap.containsKey(destKey)){
                    destMaterialCountMap.put(destKey,changeOrderMaterial.getChangeMaterialCount());
                }else{
                    Integer count = destMaterialCountMap.get(destKey);
                    destMaterialCountMap.put(destKey,count+changeOrderMaterial.getChangeMaterialCount());
                }
                totalChangeMaterialCount += changeOrderMaterial.getChangeMaterialCount();
                ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                changeOrderMaterialDO.setSrcChangeMaterialId(srcMaterial.getMaterialId());
                changeOrderMaterialDO.setDestChangeMaterialId(destMaterial.getMaterialId());
                changeOrderMaterialDO.setSrcChangeMaterialSnapshot(JSON.toJSONString(srcMaterial));
                changeOrderMaterialDO.setDestChangeMaterialSnapshot(JSON.toJSONString(destMaterial));
                changeOrderMaterialDO.setChangeMaterialCount(changeOrderMaterial.getChangeMaterialCount());
                changeOrderMaterialDO.setRealChangeMaterialCount(0);
                changeOrderMaterialDO.setIsNew(changeOrderMaterial.getIsNew());
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialDO.setCreateTime(now);
                changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDO.setUpdateTime(now);
                changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDOList.add(changeOrderMaterialDO);
            }
            for(String materialNo : srcMaterialCountMap.keySet()){
                MaterialDO materialDO = oldMaterialCountMap.get(materialNo);
                if (materialDO == null) {
                    //如果要更换的配件不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                //如果原配件数量大于在租数量
                if(srcMaterialCountMap.get(materialNo)>materialDO.getCanProcessCount()){
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
            }
            for(String materialNoAndIsNew : destMaterialCountMap.keySet()){
                String[] keys = materialNoAndIsNew.split("_");
                String destMaterialNo = keys[0];
                Integer isNew = Integer.parseInt(keys[1]);
                Integer changeCount = destMaterialCountMap.get(materialNoAndIsNew);
                Material material = materialMap.get(destMaterialNo);
                if(CommonConstant.COMMON_CONSTANT_NO.equals(isNew)&&material.getOldMaterialCount()<changeCount){
                    //该配件次新设备不足
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    return serviceResult;
                }
                if(CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&material.getNewMaterialCount()<changeCount){
                    //该配件全新新设备不足
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }
        //修改换货单
        changeOrderDO.setRentStartTime(updateChangeOrderParam.getRentStartTime());
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReasonType(updateChangeOrderParam.getChangeReasonType());
        changeOrderDO.setChangeReason(updateChangeOrderParam.getChangeReason());
        changeOrderDO.setChangeMode(updateChangeOrderParam.getChangeMode());
        changeOrderDO.setOwner(updateChangeOrderParam.getOwner());
        changeOrderDO.setRemark(updateChangeOrderParam.getRemark());
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);

        //保存取货地址信息
        changeOrderConsignInfoDO.setConsigneeName(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneeName());
        changeOrderConsignInfoDO.setConsigneePhone(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneePhone());
        changeOrderConsignInfoDO.setProvince(updateChangeOrderParam.getChangeOrderConsignInfo().getProvince());
        changeOrderConsignInfoDO.setCity(updateChangeOrderParam.getChangeOrderConsignInfo().getCity());
        changeOrderConsignInfoDO.setDistrict(updateChangeOrderParam.getChangeOrderConsignInfo().getDistrict());
        changeOrderConsignInfoDO.setAddress(updateChangeOrderParam.getChangeOrderConsignInfo().getAddress());
        changeOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        changeOrderConsignInfoDO.setRemark(updateChangeOrderParam.getChangeOrderConsignInfo().getRemark());
        changeOrderConsignInfoDO.setUpdateTime(now);
        changeOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderConsignInfoMapper.update(changeOrderConsignInfoDO);

        //保存换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOList)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOList);
        }
        //保存换货配件项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOList)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOList);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_CHANGE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (changeOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CHANGE, changeOrderDO.getChangeOrderNo(), changeOrderCommitParam.getVerifyUserId(),null, changeOrderCommitParam.getRemark());
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
        if (StringUtil.isEmpty(param.getEquipmentNo()) && param.getChangeOrderMaterialId()==null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
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
            //添加更换后设备，添加更换后配件
            ServiceResult<String, Object> addOrderDestItemResult = addOrderDestItemResultDest(param, changeOrderDO);
            if (!ErrorCode.SUCCESS.equals(addOrderDestItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderDestItemResult.getErrorCode(), addOrderDestItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            //删除更换后设备，删除更换后配件
            ServiceResult<String, Object> removeOrderDestItemResult = removeOrderDestItemResultDest(param, changeOrderDO);
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
        if (CollectionUtil.isNotEmpty(changeOrderProductDOList)) {
            List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
            //按sku统计需备货数量
            Map<Integer, Integer> skuShouldStockMap = new HashMap<>();
            for (ChangeOrderProductDO changeOrderProductDO : changeOrderProductDOList) {
                if (!skuShouldStockMap.containsKey(changeOrderProductDO.getDestChangeProductSkuId())) {
                    skuShouldStockMap.put(changeOrderProductDO.getDestChangeProductSkuId(), 0);
                }
                skuShouldStockMap.put(changeOrderProductDO.getDestChangeProductSkuId(), skuShouldStockMap.get(changeOrderProductDO.getDestChangeProductSkuId()) + changeOrderProductDO.getChangeProductSkuCount());
            }

            //按sku统计已备货sku数量
            Map<Integer, Integer> stockYetMap = new HashMap<>();
            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                if (!stockYetMap.containsKey(changeOrderProductEquipmentDO.getDestSkuId())) {
                    stockYetMap.put(changeOrderProductEquipmentDO.getDestSkuId(), 0);
                }
                stockYetMap.put(changeOrderProductEquipmentDO.getDestSkuId(), stockYetMap.get(changeOrderProductEquipmentDO.getDestSkuId()) + 1);
            }

            for (Integer skuId : skuShouldStockMap.keySet()) {
                if (!stockYetMap.containsKey(skuId) || skuShouldStockMap.get(skuId) > stockYetMap.get(skuId)) {
                    serviceResult.setErrorCode(ErrorCode.STOCK_NOT_ENOUGH);
                    return serviceResult;
                }
            }
        }

        //校验散料是否已经全部备货
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOList)) {
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
            //按配件ID统计需备货数量
            Map<Integer, Integer> skuShouldStockMap = new HashMap<>();
            for (ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderMaterialDOList) {
                if (!skuShouldStockMap.containsKey(changeOrderMaterialDO.getDestChangeMaterialId())) {
                    skuShouldStockMap.put(changeOrderMaterialDO.getDestChangeMaterialId(), 0);
                }
                skuShouldStockMap.put(changeOrderMaterialDO.getDestChangeMaterialId(), skuShouldStockMap.get(changeOrderMaterialDO.getDestChangeMaterialId()) + changeOrderMaterialDO.getChangeMaterialCount());
            }

            //按配件ID统计已备货配件数量
            Map<Integer, Integer> stockYetMap = new HashMap<>();
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                if (!stockYetMap.containsKey(changeOrderMaterialBulkDO.getDestMaterialId())) {
                    stockYetMap.put(changeOrderMaterialBulkDO.getDestMaterialId(), 0);
                }
                stockYetMap.put(changeOrderMaterialBulkDO.getDestMaterialId(), stockYetMap.get(changeOrderMaterialBulkDO.getDestMaterialId()) + 1);
            }

            for (Integer materialId : skuShouldStockMap.keySet()) {
                if (!stockYetMap.containsKey(materialId) || skuShouldStockMap.get(materialId) > stockYetMap.get(materialId)) {
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
        if (changeOrderDO == null) {
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
        if (StringUtil.isEmpty(srcProductEquipmentDO.getOrderNo())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
            return serviceResult;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(srcProductEquipmentDO.getOrderNo());
        if (orderDO == null || !orderDO.getBuyerCustomerId().equals(changeOrderDO.getCustomerId())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
            return serviceResult;
        }
        //查找用户此换货单所有更换设备项
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderProductEquipment.getChangeOrderNo());
        //用户输入的src设备所属的sku应与dest设备的sku相同，先找出支持的dest设备有哪些sku，每个sku对应一个换货单设备列表
        //这里的map的value保存的并不是列表，因为只要有一个匹配的设备就可以了，这里相当于找到了sku下最后一个符合的换货单设备项
        Map<Integer, ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOMap = new HashMap<>();
        for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
            //找到没有换过的换货单设备项
            if (changeOrderProductEquipmentDO.getSrcEquipmentId() == null) {
                changeOrderProductEquipmentDOMap.put(changeOrderProductEquipmentDO.getDestSkuId(), changeOrderProductEquipmentDO);
            }
        }
        //如果换货单设备项中没有可换的该sku项，则不可换
        ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = changeOrderProductEquipmentDOMap.get(srcProductEquipmentDO.getSkuId());
        if (changeOrderProductEquipmentDO == null) {
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

        //计算差价
//        BigDecimal diff = getDiff(orderDO, srcProductEquipmentDO, destProductEquipmentDO);
//        changeOrderProductEquipmentDO.setPriceDiff(diff);
        changeOrderProductEquipmentDO.setUpdateTime(now);
        changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
        //更新换货单实际换货商品总数，总差价，状态
        changeOrderDO.setRealTotalChangeProductCount(changeOrderDO.getRealTotalChangeProductCount() + 1);
//        changeOrderDO.setTotalPriceDiff(BigDecimalUtil.add(changeOrderDO.getTotalPriceDiff(), diff));
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS);
        changeOrderMapper.update(changeOrderDO);
        //更新订单商品设备表，实际归还时间，实际租金；添加订单商品设备，预计归还时间
        ServiceResult<String, String> updateResult = orderService.returnEquipment(srcProductEquipmentDO.getOrderNo(), srcProductEquipmentDO.getEquipmentNo(), destProductEquipmentDO.getEquipmentNo(), now);
        if (!ErrorCode.SUCCESS.equals(updateResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return updateResult;
        }
        //更新设备散料锁定状态，次新
        bulkMaterialMapper.returnEquipment(srcProductEquipmentDO.getEquipmentNo());
        bulkMaterialMapper.updateEquipmentOrderNo(destProductEquipmentDO.getEquipmentNo(), srcProductEquipmentDO.getOrderNo());
        //更新设备锁定状态，次新
        destProductEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
        destProductEquipmentDO.setOrderNo(srcProductEquipmentDO.getOrderNo());
        productEquipmentMapper.update(destProductEquipmentDO);
        srcProductEquipmentDO.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        srcProductEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        srcProductEquipmentDO.setOrderNo("");
        productEquipmentMapper.update(srcProductEquipmentDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private BigDecimal getDiff(OrderDO orderDO, ProductEquipmentDO srcProductEquipmentDO, ProductEquipmentDO destProductEquipmentDO) {
        BigDecimal diff = BigDecimal.ZERO;
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), srcProductEquipmentDO.getEquipmentNo());
        OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
        ProductSkuDO srcProductSkuDO = productSkuMapper.findById(srcProductEquipmentDO.getSkuId());
        ProductSkuDO destProductSkuDO = productSkuMapper.findById(destProductEquipmentDO.getSkuId());
        if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
            diff = BigDecimalUtil.sub(destProductSkuDO.getDayRentPrice(), srcProductSkuDO.getDayRentPrice());
            diff = diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
            diff = BigDecimalUtil.sub(destProductSkuDO.getMonthRentPrice(), srcProductSkuDO.getMonthRentPrice());
            diff = diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
        }
        return diff;
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
        Map<String, Object> findRentMap = customerOrderSupport.getCustomerAllMap(changeOrderDO.getCustomerId());
        //用户在租配件统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //判断更换数量是否大于客户在租数量
//        List<BulkMaterialDO> oldBulkMaterialDOList = bulkMaterialMapper.findRentByCustomerIdAndMaterialId(changeOrderDO.getCustomerId(), srcMaterialDO.getId());
        MaterialDO materialDO = oldMaterialCountMap.get(srcMaterialDO.getMaterialNo());
        if(materialDO==null||changeOrderMaterial.getRealChangeMaterialCount()>materialDO.getCanProcessCount()){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
            return serviceResult;
        }

        //查找该单项可更换散料列表
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderMaterialId(changeOrderMaterialDO.getId());
        //目前尚未换的换货配件列表
        List<ChangeOrderMaterialBulkDO> noChangeOrderMaterialBulkDOList = new ArrayList<>();
        for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
            if(changeOrderMaterialBulkDO.getSrcBulkMaterialId()==null){
                noChangeOrderMaterialBulkDOList.add(changeOrderMaterialBulkDO);
            }
        }
        Integer moreCount = changeOrderMaterial.getRealChangeMaterialCount() - noChangeOrderMaterialBulkDOList.size();
        if(moreCount>0){
            UserDO owner = userMapper.findByUserId(changeOrderDO.getOwner());
            Integer subCompanyId = userSupport.getCompanyIdByUser(owner.getId());
            WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(subCompanyId);
            if (currentWarehouse == null) {
                serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return serviceResult;
            }
            currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
            if (currentWarehouse == null) {
                serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                return serviceResult;
            }
            // 必须是当前库房闲置的物料
            List<BulkMaterialDO> fitBulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(currentWarehouse.getId(), changeOrderMaterialDO.getDestChangeMaterialId(), moreCount, changeOrderMaterialDO.getIsNew());
            if (CollectionUtil.isEmpty(fitBulkMaterialDOList) || fitBulkMaterialDOList.size() < moreCount) {
                serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return serviceResult;
            }
            //如果实际换货的配件数大于预计更换的配件数（从备用库房拿了n个散料换了），则再新增n条租赁散料数据
            for (int i = 0; i < moreCount; i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = new ChangeOrderMaterialBulkDO();
                changeOrderMaterialBulkDO.setChangeOrderMaterialId(changeOrderMaterial.getChangeOrderMaterialId());
                changeOrderMaterialBulkDO.setChangeOrderId(changeOrderDO.getId());
                changeOrderMaterialBulkDO.setChangeOrderNo(changeOrderDO.getChangeOrderNo());
                //随机找一个符合的散料-超
                BulkMaterialDO bulkMaterialDO = fitBulkMaterialDOList.get(i);
                changeOrderMaterialBulkDO.setDestBulkMaterialId(bulkMaterialDO.getId());
                changeOrderMaterialBulkDO.setDestBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialBulkDO.setCreateTime(now);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.save(changeOrderMaterialBulkDO);
                //原换货单换货配件追加新增配件
                noChangeOrderMaterialBulkDOList.add(changeOrderMaterialBulkDO);
            }
        }
        //客户在租的所有散料
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findRentByCustomerIdAndMaterialId(changeOrderDO.getCustomerId(), srcMaterialDO.getId());
        //按是否在设备上排序，不在设备上的排在前面
        List<BulkMaterialDO> bulkMaterialDOListByOrder = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(bulkMaterialDOList)){
            for(BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
                if(bulkMaterialDO.getCurrentEquipmentId()==null){
                    bulkMaterialDOListByOrder.add(bulkMaterialDO);
                }
            }
            for(BulkMaterialDO bulkMaterialDO : bulkMaterialDOList){
                if(bulkMaterialDO.getCurrentEquipmentId()!=null){
                    bulkMaterialDOListByOrder.add(bulkMaterialDO);
                }
            }
        }

        //一对一更新租赁换货散料表，订单编号，原散料编号，原设备编号，原设备ID
        for (int i = 0; i < changeOrderMaterial.getRealChangeMaterialCount(); i++) {
            ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = noChangeOrderMaterialBulkDOList.get(i);
            //优先选取不在设备上的散料更换
            BulkMaterialDO srcBulkMaterialDO = bulkMaterialDOListByOrder.get(i);
            changeOrderMaterialBulkDO.setSrcBulkMaterialId(srcBulkMaterialDO.getId());
            changeOrderMaterialBulkDO.setSrcBulkMaterialNo(srcBulkMaterialDO.getBulkMaterialNo());
            changeOrderMaterialBulkDO.setSrcEquipmentId(srcBulkMaterialDO.getCurrentEquipmentId());
            changeOrderMaterialBulkDO.setSrcEquipmentNo(srcBulkMaterialDO.getCurrentEquipmentNo());

            changeOrderMaterialBulkDO.setOrderNo(srcBulkMaterialDO.getOrderNo());
            changeOrderMaterialBulkDO.setUpdateTime(now);
            changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderMaterialBulkMapper.update(changeOrderMaterialBulkDO);
            //更新订单配件散料表，实际归还时间，实际租金；添加订单配件散料，预计归还时间
            ServiceResult<String, String> returnBulkMaterialResult = orderService.returnBulkMaterial(srcBulkMaterialDO.getOrderNo(), srcBulkMaterialDO.getBulkMaterialNo(), changeOrderMaterialBulkDO.getDestBulkMaterialNo(), now);
            if (!ErrorCode.SUCCESS.equals(returnBulkMaterialResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return returnBulkMaterialResult;
            }
            //如果原散料在设备上调用拆卸安装接口  否则直接修改散料状态
            if (srcBulkMaterialDO.getCurrentEquipmentId() != null) {
                ServiceResult<String, Integer> dismantleAndInstallResult = bulkMaterialService.changeProductDismantleAndInstall(srcBulkMaterialDO.getId(), changeOrderMaterialBulkDO.getDestBulkMaterialId());
                if (!dismantleAndInstallResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                    serviceResult.setErrorCode(dismantleAndInstallResult.getErrorCode(), dismantleAndInstallResult.getFormatArgs());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return serviceResult;
                }
            } else {
                BulkMaterialDO destBulkMaterialDO = bulkMaterialMapper.findById(changeOrderMaterialBulkDO.getDestBulkMaterialId());
                destBulkMaterialDO.setOrderNo(srcBulkMaterialDO.getOrderNo());
                destBulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                destBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                destBulkMaterialDO.setUpdateTime(now);
                bulkMaterialMapper.update(destBulkMaterialDO);
                srcBulkMaterialDO.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
                srcBulkMaterialDO.setOrderNo("");
                srcBulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                srcBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                srcBulkMaterialDO.setUpdateTime(now);
                bulkMaterialMapper.update(srcBulkMaterialDO);
            }
        }
        //更新换货单实际换货配件总数，总差价，状态
        changeOrderDO.setRealTotalChangeMaterialCount(changeOrderDO.getRealTotalChangeMaterialCount() + changeOrderMaterial.getRealChangeMaterialCount());
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS);
        changeOrderMapper.update(changeOrderDO);
        changeOrderMaterialDO.setRealChangeMaterialCount(changeOrderMaterial.getRealChangeMaterialCount());
        changeOrderMaterialMapper.update(changeOrderMaterialDO);
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
        StockUpForChangeParam param = new StockUpForChangeParam();
        param.setChangeOrderNo(processNoChangeEquipmentParam.getChangeOrderNo());
        param.setEquipmentNo(processNoChangeEquipmentParam.getEquipmentNo());
        //删除更换后设备，删除更换后配件
        ServiceResult<String, Object> removeOrderDestItemResult = removeOrderDestItemResultDest(param, changeOrderDO);
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
    public ServiceResult<String, String> updateChangeEquipmentRemark(UpdateChangeEquipmentRemarkParam updateChangeEquipmentRemarkParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = changeOrderProductEquipmentMapper.findByChangeOrderNoAndSrcEquipmentNo(updateChangeEquipmentRemarkParam.getChangeOrderNo(),updateChangeEquipmentRemarkParam.getEquipmentNo());
        if(changeOrderProductEquipmentDO == null){
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderProductEquipmentDO.getChangeOrderNo());
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())&&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_CONFIRM.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_UPDATE_REMARK);
            return serviceResult;
        }
        changeOrderProductEquipmentDO.setRemark(updateChangeEquipmentRemarkParam.getRemark());
        changeOrderProductEquipmentDO.setUpdateTime(new Date());
        changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> updateChangeMaterialRemark(UpdateChangeMaterialRemarkParam updateChangeMaterialRemarkParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(updateChangeMaterialRemarkParam.getChangeOrderMaterialId());
        if(changeOrderMaterialDO==null){
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderMaterialDO.getChangeOrderNo());
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())&&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_CONFIRM.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_UPDATE_REMARK);
            return serviceResult;
        }
        changeOrderMaterialDO.setRemark(updateChangeMaterialRemarkParam.getRemark());
        changeOrderMaterialDO.setUpdateTime(new Date());
        changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMaterialMapper.update(changeOrderMaterialDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> confirmChangeOrder(String changeOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderNo);
        if(changeOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_CAN_NOT_CONFIRM);
            return serviceResult;
        }
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_CONFIRM);
        changeOrderDO.setUpdateTime(new Date());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);

        serviceResult.setResult(changeOrderNo);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
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
        for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
            if (changeOrderProductEquipmentDO.getSrcEquipmentId() == null) {
                serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_END);
                return serviceResult;
            }
        }
        Date now = new Date();
        //如果目标散料没有全部换，则还原散备货料为空闲状态，删除换货单散料项数据
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderDO.getChangeOrderNo());
        for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
            if (changeOrderMaterialBulkDO.getSrcBulkMaterialId() == null) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(changeOrderMaterialBulkDO.getDestBulkMaterialId());
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialMapper.update(bulkMaterialDO);
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.update(changeOrderMaterialBulkDO);
            }
        }
        //调用结算单接口
        ServiceResult<String, BigDecimal> statementResult = statementService.createChangeOrderStatement(changeOrderDO.getChangeOrderNo());
        if (!ErrorCode.SUCCESS.equals(statementResult.getErrorCode())) {
            serviceResult.setErrorCode(statementResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }
        changeOrderDO.setRentStartTime(changeOrder.getRentStartTime());
        changeOrderDO.setServiceCost(changeOrder.getServiceCost());
        changeOrderDO.setDamageCost(changeOrder.getDamageCost());
        changeOrderDO.setIsDamage(changeOrder.getIsDamage());
        changeOrderDO.setChangeMode(changeOrder.getChangeMode());
        changeOrderDO.setRemark(changeOrder.getRemark());

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
        changeOrder = ConverterUtil.convert(changeOrderDO, ChangeOrder.class);
        //填写退还商品项可换数量字段，用于修改接口提示
        List<ChangeOrderProduct> changeOrderProductList = changeOrder.getChangeOrderProductList();
        if (CollectionUtil.isNotEmpty(changeOrderProductList)) {
            for (ChangeOrderProduct changeOrderProduct : changeOrderProductList) {
//                changeOrderProduct.setCanProcessCount(oldSkuCountMap.get(changeOrderProduct.getSrcChangeProductSkuId()).getCanProcessCount() + changeOrderProduct.getChangeProductSkuCount());
                changeOrderProduct.setCanProcessCount(changeOrderProduct.getChangeProductSkuCount() - changeOrderProduct.getRealChangeProductSkuCount());
            }
        }
        //填写退还配件项可换数量字段，用于修改接口提示
        List<ChangeOrderMaterial> changeOrderMaterialList = changeOrder.getChangeOrderMaterialList();
        if (CollectionUtil.isNotEmpty(changeOrderMaterialList)) {
            for (ChangeOrderMaterial changeOrderMaterial : changeOrderMaterialList) {
//                changeOrderMaterial.setCanProcessCount(oldMaterialCountMap.get(changeOrderMaterial.getSrcChangeMaterialId()).getCanProcessCount() + changeOrderMaterial.getChangeMaterialCount());
                changeOrderMaterial.setCanProcessCount(changeOrderMaterial.getChangeMaterialCount() - changeOrderMaterial.getRealChangeMaterialCount());
            }
        }
        serviceResult.setResult(changeOrder);
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
        maps.put("changeOrderPageParam", changeOrderPageParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = changeOrderMapper.findChangeOrderCountByParams(maps);
        List<ChangeOrderDO> changeOrderDOList = changeOrderMapper.findChangeOrderByParams(maps);
        List<ChangeOrder> purchaseOrderList = ConverterUtil.convertList(changeOrderDOList, ChangeOrder.class);
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
        maps.put("changeEquipmentPageParam", changeEquipmentPageParam);

        Integer totalCount = changeOrderProductEquipmentMapper.listCount(maps);
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.listPage(maps);
        List<ChangeOrderProductEquipment> changeOrderProductEquipmentList = ConverterUtil.convertList(changeOrderProductEquipmentDOList, ChangeOrderProductEquipment.class);
        for (ChangeOrderProductEquipment changeOrderProductEquipment : changeOrderProductEquipmentList) {
            if (StringUtil.isNotEmpty(changeOrderProductEquipment.getSrcEquipmentNo())) {
                ProductEquipmentDO srcProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipment.getSrcEquipmentNo());
                changeOrderProductEquipment.setSrcProductEquipment(ConverterUtil.convert(srcProductEquipmentDO, ProductEquipment.class));
            }
            if (StringUtil.isNotEmpty(changeOrderProductEquipment.getDestEquipmentNo())) {
                ProductEquipmentDO destProductEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeOrderProductEquipment.getDestEquipmentNo());
                changeOrderProductEquipment.setDestProductEquipment(ConverterUtil.convert(destProductEquipmentDO, ProductEquipment.class));
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
        maps.put("changeBulkPageParam", changeBulkPageParam);

        Integer totalCount = changeOrderMaterialBulkMapper.listCount(maps);
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.listPage(maps);
        List<ChangeOrderMaterialBulk> changeOrderMaterialBulkList = ConverterUtil.convertList(changeOrderMaterialBulkDOList, ChangeOrderMaterialBulk.class);
        for (ChangeOrderMaterialBulk changeOrderMaterialBulk : changeOrderMaterialBulkList) {
            if (StringUtil.isNotEmpty(changeOrderMaterialBulk.getSrcBulkMaterialNo())) {
                BulkMaterialDO srcBulkMaterialDO = bulkMaterialMapper.findByNo(changeOrderMaterialBulk.getSrcBulkMaterialNo());
                changeOrderMaterialBulk.setSrcBulkMaterial(ConverterUtil.convert(srcBulkMaterialDO, BulkMaterial.class));
            }
            if (StringUtil.isNotEmpty(changeOrderMaterialBulk.getDestBulkMaterialNo())) {
                BulkMaterialDO destBulkMaterialDO = bulkMaterialMapper.findByNo(changeOrderMaterialBulk.getDestBulkMaterialNo());
                changeOrderMaterialBulk.setDestBulkMaterial(ConverterUtil.convert(destBulkMaterialDO, BulkMaterial.class));
            }
        }
        Page<ChangeOrderMaterialBulk> page = new Page<>(changeOrderMaterialBulkList, totalCount, changeBulkPageParam.getPageNo(), changeBulkPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    private ServiceResult<String, Object> addOrderDestItemResultDest(StockUpForChangeParam param, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        Date now = new Date();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getEquipmentNo())) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(param.getEquipmentNo());
            if (productEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                return serviceResult;
            }
            //不是空闲则不可以用
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE, productEquipmentDO.getEquipmentNo());
                return serviceResult;
            }
            //商品非在租则不可用
            ProductDO productDO = productMapper.findByProductId(productEquipmentDO.getProductId());
            if (productDO == null || CommonConstant.COMMON_CONSTANT_NO.equals(productDO.getIsRent())) {
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
            //按照destSku归集后的countMap key(skuId_新旧属性)
            Map<String, Integer> changeOrderProductCountMap = new HashMap<>();
            //按照destSku归集后的changeOrderProductDOList Mapkey(skuId_新旧属性)
            Map<String, List<ChangeOrderProductDO>> changeOrderProductDOListMap = new HashMap<>();
            for (ChangeOrderProductDO changeOrderProductDO : changeOrderProductDOList) {
                String  key = changeOrderProductDO.getDestChangeProductSkuId()+"_"+changeOrderProductDO.getIsNew();
                if (!changeOrderProductCountMap.containsKey(key)) {
                    changeOrderProductCountMap.put(key, changeOrderProductDO.getChangeProductSkuCount());
                }else{
                    changeOrderProductCountMap.put(key, changeOrderProductDO.getChangeProductSkuCount() + changeOrderProductCountMap.get(key));
                }
                if (!changeOrderProductDOListMap.containsKey(key)) {
                    changeOrderProductDOListMap.put(key, new ArrayList<ChangeOrderProductDO>());
                }
                changeOrderProductDOListMap.get(key).add(changeOrderProductDO);
            }
            //计算是否可备货，可备货总数-已备货总数大于等于1可备货
            String key = productEquipmentDO.getSkuId()+"_"+productEquipmentDO.getIsNew();
            Integer canStock = changeOrderProductCountMap.get(key) == null ? 0 : changeOrderProductCountMap.get(key);
            Integer stockYetCount = changeOrderProductEquipmentMap.get(key) == null ? 0 : changeOrderProductEquipmentMap.get(key);
            if (canStock - stockYetCount < 1) {
                serviceResult.setErrorCode(ErrorCode.STOCK_NOT_MATCH);
                return serviceResult;
            }

            //保存换货单设备项
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = new ChangeOrderProductEquipmentDO();
            ChangeOrderProductDO changeOrderProductDO = changeOrderProductDOListMap.get(key).get(0);
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
            bulkMaterialMapper.updateEquipmentBulkMaterialStatus(productEquipmentDO.getEquipmentNo(), BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
        }
        // 如果输入进来的配件ID 为当前订单项需要的，那么就匹配
        if (param.getChangeOrderMaterialId()!=null) {
            ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(param.getChangeOrderMaterialId());
            if(changeOrderMaterialDO==null||!param.getChangeOrderNo().equals(changeOrderMaterialDO.getChangeOrderNo())){
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return serviceResult;
            }
            //根据换货单号查找换货单所有目前备货散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(param.getChangeOrderNo());
            Integer stockYetCount = 0;
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                if(changeOrderMaterialBulkDO.getChangeOrderMaterialId().equals(changeOrderMaterialDO.getId())){
                    stockYetCount++;
                }
            }
            Integer needStockCount = changeOrderMaterialDO.getChangeMaterialCount() - stockYetCount;
            if(needStockCount<=0){
                serviceResult.setErrorCode(ErrorCode.STOCK_FINISH_THIS_ITEM);
                return serviceResult;
            }
            UserDO owner = userMapper.findByUserId(changeOrderDO.getOwner());
            Integer subCompanyId = userSupport.getCompanyIdByUser(owner.getId());
            WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(subCompanyId);
            if (currentWarehouse == null) {
                serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return serviceResult;
            }
            currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
            if (currentWarehouse == null) {
                serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                return serviceResult;
            }
            // 必须是当前库房闲置的物料
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(currentWarehouse.getId(), changeOrderMaterialDO.getDestChangeMaterialId(), changeOrderMaterialDO.getChangeMaterialCount(), changeOrderMaterialDO.getIsNew());
            if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < changeOrderMaterialDO.getChangeMaterialCount()) {
                serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return serviceResult;
            }
            //保存换货单散料项
            for (int i = 0; i < changeOrderMaterialDO.getChangeMaterialCount(); i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = new ChangeOrderMaterialBulkDO();
                changeOrderMaterialBulkDO.setChangeOrderMaterialId(changeOrderMaterialDO.getId());
                changeOrderMaterialBulkDO.setChangeOrderId(changeOrderMaterialDO.getChangeOrderId());
                changeOrderMaterialBulkDO.setChangeOrderNo(changeOrderMaterialDO.getChangeOrderNo());
                //随机找一个可用散料
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);
                changeOrderMaterialBulkDO.setDestBulkMaterialId(bulkMaterialDO.getId());
                changeOrderMaterialBulkDO.setDestBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialBulkDO.setCreateTime(now);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.save(changeOrderMaterialBulkDO);
            }
            Map<String,Object> maps = new HashMap<>();
            maps.put("bulkMaterialStatus",BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
            maps.put("updateTime",now);
            maps.put("updateUser",userSupport.getCurrentUserId().toString());
            maps.put("changeOrderMaterialId",param.getChangeOrderMaterialId());
            //批量更新散料状态
            bulkMaterialMapper.updateBatchDestStatusByChangeOrderMaterialId(maps);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String, Object> removeOrderDestItemResultDest(StockUpForChangeParam param, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        Date now = new Date();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getEquipmentNo())) {
            //根据换货单号和设备号查找备货设备
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = changeOrderProductEquipmentMapper.findByChangeOrderNoAndDestEquipmentNo(changeOrderDO.getChangeOrderNo(), param.getEquipmentNo());
            if (changeOrderProductEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return serviceResult;
            }
            changeOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            changeOrderProductEquipmentDO.setUpdateTime(now);
            changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
            //更新散料状态
            bulkMaterialMapper.updateEquipmentBulkMaterialStatus(changeOrderProductEquipmentDO.getDestEquipmentNo(), BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
        }
        // 如果输入进来的配件ID 为当前订单项需要的，那么就匹配
        if (param.getChangeOrderMaterialId()!=null) {
            ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(param.getChangeOrderMaterialId());
            if(changeOrderMaterialDO==null||!param.getChangeOrderNo().equals(changeOrderMaterialDO.getChangeOrderNo())){
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return serviceResult;
            }
            changeOrderMaterialBulkMapper.deleteByChangeOrderMaterialId(changeOrderMaterialDO.getId(),userSupport.getCurrentUserId().toString(),now);

            Map<String,Object> maps = new HashMap<>();
            maps.put("bulkMaterialStatus",BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
            maps.put("updateTime",now);
            maps.put("updateUser",userSupport.getCurrentUserId().toString());
            maps.put("changeOrderMaterialId",param.getChangeOrderMaterialId());
            //批量更新散料状态
            bulkMaterialMapper.updateBatchDestStatusByChangeOrderMaterialId(maps);
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
    private OrderMapper orderMapper;
    @Autowired
    private BulkMaterialService bulkMaterialService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private StatementService statementService;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PermissionSupport permissionSupport;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
}
