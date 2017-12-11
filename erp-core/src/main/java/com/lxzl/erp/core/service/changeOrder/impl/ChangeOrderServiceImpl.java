package com.lxzl.erp.core.service.changeOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.changeOrder.ChangeMaterialPairs;
import com.lxzl.erp.common.domain.changeOrder.ChangeProductSkuPairs;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderDO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChangeOrderServiceImpl implements ChangeOrderService{

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
    @Override
    public ServiceResult<String, String> add(AddChangeOrderParam addChangeOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addChangeOrderParam.getCustomerNo());
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        List<ChangeProductSkuPairs> changeOrderParamProductSkuList = addChangeOrderParam.getChangeProductSkuPairsList();
        List<ChangeMaterialPairs> changeOrderParamMaterialList = addChangeOrderParam.getChangeMaterialPairsList();
        //用户在租sku统计
        Map<String,Object> findRentMap = customerOrderSupport.getCustomerAllMap(customerDO.getId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findRentMap);
        Map<Integer,ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldProductSkuDOList)){
            for(ProductSkuDO productSkuDO  : oldProductSkuDOList){
                oldSkuCountMap.put(productSkuDO.getId(),productSkuDO);
            }
        }
        //用户在租物料统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String,MaterialDO> oldMaterialCountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldMaterialDOList)){
            for(MaterialDO materialDO  : oldMaterialDOList){
                oldMaterialCountMap.put(materialDO.getMaterialNo(),materialDO);
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
        if(CollectionUtil.isNotEmpty(changeOrderParamProductSkuList)){
            for(ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList){
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(changeProductSkuPairs.getProductSkuIdSrc());
                if(changeProductSkuPairs.getChangeCount()>oldSkuRent.getCanReturnCount()){//更换的sku数量大于可换数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                if(oldSkuRent==null){//如果要更换的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                totalChangeProductCount+=changeProductSkuPairs.getChangeCount();
                ServiceResult<String , Product> productSkuSrcResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdSrc());
                if(!ErrorCode.SUCCESS.equals(productSkuSrcResult.getErrorCode())||productSkuSrcResult.getResult()==null){
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                ServiceResult<String , Product> productSkuDestResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdDest());
                if(!ErrorCode.SUCCESS.equals(productSkuDestResult.getErrorCode())||productSkuDestResult.getResult()==null){
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
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
        if(CollectionUtil.isNotEmpty(changeOrderParamMaterialList)){
            for(ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList){
                MaterialDO srcMaterial = oldMaterialCountMap.get(changeMaterialPairs.getMaterialNoSrc());
                if(changeMaterialPairs.getChangeCount()>srcMaterial.getCanReturnCount()){//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                if(srcMaterial==null){//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                totalChangeMaterialCount+=changeMaterialPairs.getChangeCount();
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
        changeOrderDO.setChangeMode(ChangeMode.CHANGE_MODE_TO_DOOR);
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING);
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
        if(CollectionUtil.isNotEmpty(changeOrderProductDOList)){
            changeOrderProductMapper.batchSave(changeOrderDO.getId(),changeOrderDO.getChangeOrderNo(),changeOrderProductDOList);
        }
        //保存退还物料项
        if(CollectionUtil.isNotEmpty(changeOrderMaterialDOList)){
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(),changeOrderDO.getChangeOrderNo(),changeOrderMaterialDOList);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
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
}
