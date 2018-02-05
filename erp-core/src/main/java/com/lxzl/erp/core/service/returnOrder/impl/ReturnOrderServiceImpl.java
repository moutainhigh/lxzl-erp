package com.lxzl.erp.core.service.returnOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.*;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.returnOrder.ReturnOrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.*;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
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
public class ReturnOrderServiceImpl implements ReturnOrderService {
    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(AddReturnOrderParam addReturnOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addReturnOrderParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        UserDO owner = userMapper.findByUserId(addReturnOrderParam.getOwner());
        if(owner==null){
            serviceResult.setErrorCode(ErrorCode.SALES_MAN_NOT_NULL);
            return serviceResult;
        }
        //校验退还商品项sku不能重复
        List<ReturnOrderProduct> returnOrderProductList = addReturnOrderParam.getReturnOrderProductList();
        Set<Integer> skuIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(returnOrderProductList)) {
            for (ReturnOrderProduct returnOrderProduct : returnOrderProductList) {
                skuIdSet.add(returnOrderProduct.getReturnProductSkuId());
            }
            if (skuIdSet.size() < returnOrderProductList.size()) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //校验退还物料项sku不能重复
        List<ReturnOrderMaterial> returnOrderMaterialList = addReturnOrderParam.getReturnOrderMaterialList();
        Set<String> materialIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(returnOrderMaterialList)) {
            for (ReturnOrderMaterial returnOrderMaterial : returnOrderMaterialList) {
                materialIdSet.add(returnOrderMaterial.getReturnMaterialNo());
            }
            if (materialIdSet.size() < returnOrderMaterialList.size()) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //用户在租sku统计
        Map<String, Object> findSkuRentMap = customerOrderSupport.getCustomerAllMap(customerDO.getId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findSkuRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        findSkuRentMap = customerOrderSupport.getCustomerCanReturnAllMap(customerDO.getId());
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findSkuRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计退还sku总数
        Integer totalReturnProductCount = 0;
        //累计退还物料总数
        Integer totalReturnMaterialCount = 0;
        Date now = new Date();
        //构造待保存退换单商品项
        List<ReturnOrderProductDO> returnOrderProductDOList = new ArrayList<>();
        //如果要退还的sku不在在租列表，或者要退还的sku数量大于可退数量，返回相应错误

        if (CollectionUtil.isNotEmpty(returnOrderProductList)) {
            for (ReturnOrderProduct returnOrderProduct : returnOrderProductList) {
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(returnOrderProduct.getReturnProductSkuId());
                if (oldSkuRent == null) {//如果要退还的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                } else if (returnOrderProduct.getReturnProductSkuCount() > oldSkuRent.getCanProcessCount()) {//退还的sku数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                totalReturnProductCount += returnOrderProduct.getReturnProductSkuCount();
                ServiceResult<String, Product> productSkuResult = productService.queryProductBySkuId(returnOrderProduct.getReturnProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productSkuResult.getErrorCode()) || productSkuResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                ReturnOrderProductDO returnOrderProductDO = new ReturnOrderProductDO();
                returnOrderProductDO.setReturnProductSkuId(returnOrderProduct.getReturnProductSkuId());
                returnOrderProductDO.setReturnProductSkuCount(returnOrderProduct.getReturnProductSkuCount());
                returnOrderProductDO.setReturnProductSkuSnapshot(JSON.toJSONString(productSkuResult.getResult()));
                returnOrderProductDO.setRealReturnProductSkuCount(0);
                returnOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                returnOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                returnOrderProductDO.setCreateTime(now);
                returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                returnOrderProductDO.setUpdateTime(now);
                returnOrderProductDOList.add(returnOrderProductDO);
            }
        }
        //构造待保存退换单物料项
        List<ReturnOrderMaterialDO> returnOrderMaterialDOList = new ArrayList<>();
        //如果要退还的物料不在在租列表，或者要退还的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(returnOrderMaterialList)) {
            for (ReturnOrderMaterial returnOrderMaterial : returnOrderMaterialList) {
                MaterialDO oldMaterialRent = oldMaterialCountMap.get(returnOrderMaterial.getReturnMaterialNo());
                if (oldMaterialRent == null) {//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                } else if (returnOrderMaterial.getReturnMaterialCount() > oldMaterialRent.getCanProcessCount()) {//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                totalReturnMaterialCount += returnOrderMaterial.getReturnMaterialCount();
                ReturnOrderMaterialDO returnOrderMaterialDO = new ReturnOrderMaterialDO();
                returnOrderMaterialDO.setReturnMaterialId(oldMaterialRent.getId());
                returnOrderMaterialDO.setReturnMaterialCount(returnOrderMaterial.getReturnMaterialCount());
                returnOrderMaterialDO.setReturnMaterialSnapshot(JSON.toJSONString(oldMaterialRent));
                returnOrderMaterialDO.setRealReturnMaterialCount(0);
                returnOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                returnOrderMaterialDO.setCreateTime(now);
                returnOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                returnOrderMaterialDO.setUpdateTime(now);
                returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                returnOrderMaterialDOList.add(returnOrderMaterialDO);
            }
        }
        //创建租赁退换单
        ReturnOrderDO returnOrderDO = new ReturnOrderDO();
        returnOrderDO.setReturnOrderNo(generateNoSupport.generateReturnOrderNo(now, customerDO.getId()));
        returnOrderDO.setCustomerId(customerDO.getId());
        returnOrderDO.setCustomerNo(customerDO.getCustomerNo());
        returnOrderDO.setReturnMode(addReturnOrderParam.getReturnMode());
        returnOrderDO.setReturnTime(addReturnOrderParam.getReturnTime());
        returnOrderDO.setIsCharging(addReturnOrderParam.getIsCharging());
        returnOrderDO.setTotalReturnProductCount(totalReturnProductCount);
        returnOrderDO.setTotalReturnMaterialCount(totalReturnMaterialCount);
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        returnOrderDO.setReturnReasonType(addReturnOrderParam.getReturnReasonType());
        returnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderDO.setOwner(owner.getId());
        returnOrderDO.setRemark(addReturnOrderParam.getRemark());
        returnOrderDO.setCreateTime(now);
        returnOrderDO.setUpdateTime(now);
        returnOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderMapper.save(returnOrderDO);

        //保存取货地址信息
        ReturnOrderConsignInfoDO returnOrderConsignInfoDO = new ReturnOrderConsignInfoDO();
        returnOrderConsignInfoDO.setReturnOrderId(returnOrderDO.getId());
        returnOrderConsignInfoDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
        returnOrderConsignInfoDO.setConsigneeName(addReturnOrderParam.getReturnOrderConsignInfo().getConsigneeName());
        returnOrderConsignInfoDO.setConsigneePhone(addReturnOrderParam.getReturnOrderConsignInfo().getConsigneePhone());
        returnOrderConsignInfoDO.setProvince(addReturnOrderParam.getReturnOrderConsignInfo().getProvince());
        returnOrderConsignInfoDO.setCity(addReturnOrderParam.getReturnOrderConsignInfo().getCity());
        returnOrderConsignInfoDO.setDistrict(addReturnOrderParam.getReturnOrderConsignInfo().getDistrict());
        returnOrderConsignInfoDO.setAddress(addReturnOrderParam.getReturnOrderConsignInfo().getAddress());
        returnOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderConsignInfoDO.setRemark(addReturnOrderParam.getReturnOrderConsignInfo().getRemark());
        returnOrderConsignInfoDO.setCreateTime(now);
        returnOrderConsignInfoDO.setUpdateTime(now);
        returnOrderConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
        returnOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderConsignInfoMapper.save(returnOrderConsignInfoDO);

        //保存退还商品项
        if (CollectionUtil.isNotEmpty(returnOrderProductDOList)) {
            returnOrderProductMapper.batchSave(returnOrderDO.getId(), returnOrderDO.getReturnOrderNo(), returnOrderProductDOList);
        }
        //保存退还物料项
        if (CollectionUtil.isNotEmpty(returnOrderMaterialDOList)) {
            returnOrderMaterialMapper.batchSave(returnOrderDO.getId(), returnOrderDO.getReturnOrderNo(), returnOrderMaterialDOList);
        }
        serviceResult.setResult(returnOrderDO.getReturnOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, ProductEquipment> doReturnEquipment(DoReturnEquipmentParam doReturnEquipmentParam) {
        ServiceResult<String, ProductEquipment> serviceResult = new ServiceResult<>();
        //校验退还单是否存在
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(doReturnEquipmentParam.getReturnOrderNo());
        if (returnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_TAKEN.equals(returnOrderDO.getReturnOrderStatus()) &&
                !ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(returnOrderDO.getReturnOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_RETURN);
            return serviceResult;
        }

        //查询客户的所有在租设备，并保存到map以便后续使用
        Map<String, ProductEquipmentDO> rentMap = new HashMap<>();
        List<ProductEquipmentDO> productEquipmentDOList = productEquipmentMapper.findRentByCustomerId(returnOrderDO.getCustomerId());
        for (ProductEquipmentDO productEquipmentDO : productEquipmentDOList) {
            rentMap.put(productEquipmentDO.getEquipmentNo(), productEquipmentDO);
        }
        //校验该设备是否可退
        ProductEquipmentDO productEquipmentDO = rentMap.get(doReturnEquipmentParam.getEquipmentNo());
        if (productEquipmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_RENT);
            return serviceResult;
        }

        //取得订单，并且把商品项存入map方便查找
        OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
        //取得订单的所有在租设备数量
        Integer rentEquipmentCount = productEquipmentMapper.getRentEquipmentCountByOrderNo(orderDO.getOrderNo());
        //取得订单的所有在租散料数量
        Integer rentBulkMaterialCount = bulkMaterialMapper.getRentBulkCountByOrderNo(orderDO.getOrderNo());
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        //订单商品项Map(key=订单商品项ID，value=订单商品项对象)
        Map<Integer, OrderProductDO> orderProductDOMap = new HashMap<>();
        for (OrderProductDO orderProductDO : orderProductDOList) {
            orderProductDOMap.put(orderProductDO.getId(), orderProductDO);
        }

        //修改订单商品设备-实际归还时间，实际租金
        Date now = new Date();
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findRentByCustomerIdAndEquipmentId(returnOrderDO.getCustomerId(), productEquipmentDO.getId());
        if(orderProductEquipmentDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_OR_ORDER_NOT_CONFIRM);
            return serviceResult;
        }
        //计算该设备的租金
        ServiceResult<String, String> returnResult = orderService.returnEquipment(orderDO.getOrderNo(), productEquipmentDO.getEquipmentNo(), null, now);
        if (!ErrorCode.SUCCESS.equals(returnResult.getErrorCode())) {
            serviceResult.setErrorCode(returnResult.getErrorCode());
            return serviceResult;
        }
        //修改设备状态为闲置,次新
        productEquipmentDO.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        productEquipmentDO.setOrderNo("");
        productEquipmentMapper.update(productEquipmentDO);
        //修改设备散料状态，次新
        bulkMaterialMapper.returnEquipment(productEquipmentDO.getEquipmentNo());
        //由于按天租赁无需授信额度，需交押金，所以当订单按月或按天租赁时，修改客户已用授信额度
        if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDOMap.get(orderProductEquipmentDO.getOrderProductId()).getRentType())) {
            CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(returnOrderDO.getCustomerId());
            BigDecimal amount = BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmountUsed(), productEquipmentDO.getEquipmentPrice());
            if(BigDecimalUtil.compare(amount,BigDecimal.ZERO)<0){
                amount = BigDecimal.ZERO;
            }
            customerRiskManagementDO.setCreditAmountUsed(amount);
            customerRiskManagementMapper.update(customerRiskManagementDO);
        }
        //修改订单,全部归还状态，最后一件归还时间
        if (rentEquipmentCount == 1 && rentBulkMaterialCount == 0) {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK);
            orderDO.setActualReturnTime(now);
            orderMapper.update(orderDO);
        }
        //修改退还单，归还订单状态,租赁期间产生总费用,实际退还商品总数,修改时间，修改人
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
//        returnOrderDO.setTotalRentCost(BigDecimalUtil.add(returnOrderDO.getTotalRentCost(), rentCost));
        returnOrderDO.setRealTotalReturnProductCount(returnOrderDO.getRealTotalReturnProductCount() + 1);
        returnOrderDO.setUpdateTime(now);
        returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        returnOrderMapper.update(returnOrderDO);
        //修改退还商品项表,实际退还商品数量,修改时间，修改人
        ReturnOrderProductDO returnOrderProductDO = returnOrderProductMapper.findBySkuIdAndReturnOrderId(productEquipmentDO.getSkuId(), returnOrderDO.getId());
        //如果退还单商品项不存在，意味着当初建退还单的时候，没有要退该种类的sku商品，但是货拿回来了
        //这种情况有两种处理方式：1.在本退还单中自动创建该sku的退还商品项 2.不允许在该退还单中退，要新建退还单
        //目前采用第一种处理方式，系统自动创建该sku的退还商品项
        if (returnOrderProductDO == null) {
            ServiceResult<String, Product> productSkuResult = productService.queryProductBySkuId(productEquipmentDO.getSkuId());
            returnOrderProductDO = new ReturnOrderProductDO();
            returnOrderProductDO.setReturnOrderId(returnOrderDO.getId());
            returnOrderProductDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
            returnOrderProductDO.setReturnProductSkuId(productEquipmentDO.getSkuId());
            returnOrderProductDO.setReturnProductSkuCount(0);
            returnOrderProductDO.setReturnProductSkuSnapshot(JSON.toJSONString(productSkuResult.getResult()));
            returnOrderProductDO.setRealReturnProductSkuCount(1);
            returnOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            returnOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
            returnOrderProductDO.setCreateTime(now);
            returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderProductDO.setUpdateTime(now);
            returnOrderProductMapper.save(returnOrderProductDO);
        } else {
            returnOrderProductDO.setRealReturnProductSkuCount(returnOrderProductDO.getRealReturnProductSkuCount() + 1);
            returnOrderProductDO.setUpdateTime(now);
            returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderProductMapper.update(returnOrderProductDO);
        }
        //添加退还商品设备表记录
        ReturnOrderProductEquipmentDO returnOrderProductEquipmentDO = new ReturnOrderProductEquipmentDO();
        returnOrderProductEquipmentDO.setReturnOrderProductId(returnOrderProductDO.getId());
        returnOrderProductEquipmentDO.setReturnOrderId(returnOrderDO.getId());
        returnOrderProductEquipmentDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
        returnOrderProductEquipmentDO.setOrderNo(orderDO.getOrderNo());
        returnOrderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
        returnOrderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
        returnOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderProductEquipmentDO.setCreateTime(now);
        returnOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
        returnOrderProductEquipmentDO.setUpdateTime(now);
        returnOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderProductEquipmentMapper.save(returnOrderProductEquipmentDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(productEquipmentDO, ProductEquipment.class));
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Material> doReturnMaterial(DoReturnMaterialParam doReturnMaterialParam) {
        ServiceResult<String, Material> serviceResult = new ServiceResult<>();
        //校验退还单是否存在
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(doReturnMaterialParam.getReturnOrderNo());
        if (returnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_TAKEN.equals(returnOrderDO.getReturnOrderStatus()) &&
                !ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(returnOrderDO.getReturnOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_RETURN);
            return serviceResult;
        }

        //查询客户的所有在租散料，并保存到map以便后续使用
        Map<String, List<BulkMaterialDO>> rentMap = new HashMap<>();
        //将在租物料按照订单划分，如果不需要退还的散料，不加入此map
        Map<String, Map<Integer, BulkMaterialDO>> orderBulkMaterialNeedReturnMap = new HashMap<>();
        List<BulkMaterialDO> rentBulkMaterialDOList = bulkMaterialMapper.findRentByCustomerId(returnOrderDO.getCustomerId());
        for (BulkMaterialDO bulkMaterialDO : rentBulkMaterialDOList) {
            if (!rentMap.containsKey(bulkMaterialDO.getMaterialNo())) {
                rentMap.put(bulkMaterialDO.getMaterialNo(), new ArrayList<BulkMaterialDO>());
            }
            rentMap.get(bulkMaterialDO.getMaterialNo()).add(bulkMaterialDO);
            MaterialDO materialDO = materialMapper.findByNo(bulkMaterialDO.getMaterialNo());
            //如果散料所属物料需要还
            if (materialDO.getIsConsumable()==null||CommonConstant.COMMON_CONSTANT_NO.equals(materialDO.getIsConsumable())) {
                if (!orderBulkMaterialNeedReturnMap.containsKey(bulkMaterialDO.getOrderNo())) {
                    orderBulkMaterialNeedReturnMap.put(bulkMaterialDO.getOrderNo(), new HashMap<Integer, BulkMaterialDO>());
                }
                orderBulkMaterialNeedReturnMap.get(bulkMaterialDO.getOrderNo()).put(bulkMaterialDO.getId(), bulkMaterialDO);
            }

        }
        if (!rentMap.containsKey(doReturnMaterialParam.getMaterialNo())) {
            serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_RENT);
            return serviceResult;
        }
        if (rentMap.get(doReturnMaterialParam.getMaterialNo()).size() < doReturnMaterialParam.getReturnCount()) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RENT_THIS_BULK_MATERIAL_NO_MORE,rentMap.get(doReturnMaterialParam.getMaterialNo()).size());
            return serviceResult;
        }
        Date now = new Date();
        //获取用户所有在租订单，并存入map待用
        List<OrderDO> orderDOList = orderMapper.findByCustomerId(returnOrderDO.getCustomerId());
        Map<String, OrderDO> orderDOMap = new HashMap<>();
        for (OrderDO orderDO : orderDOList) {
            orderDOMap.put(orderDO.getOrderNo(), orderDO);
        }
        //取得每个订单的本次退还的散料总数，存入此map
        Map<String, Integer> rentBulkMaterialCountNowMap = new HashMap<>();
        //取得每个订单的在租设备总数，存入此map
        Map<String, Integer> rentEquipmentCountMap = new HashMap<>();

        //待更新的散料列表
        List<BulkMaterialDO> bulkMaterialDOListForUpdate = new ArrayList<>();
        //待更新的客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(returnOrderDO.getCustomerId());
        //待更新的退还物料项列表
        Map<Integer, ReturnOrderMaterialDO> returnOrderMaterialDOMapForUpdate = new HashMap<>();
        //待保存的退还物料项列表
        Map<Integer, ReturnOrderMaterialDO> returnOrderMaterialDOMapForSave = new HashMap<>();
        //待保存的退还散料列表
        List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDOListForSave = new ArrayList<>();

        List<BulkMaterialDO> bulkMaterialDOList = rentMap.get(doReturnMaterialParam.getMaterialNo());

        for (int i = 0; i < doReturnMaterialParam.getReturnCount(); i++) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);
            //取得订单，并且把商品项存入map方便查找
            OrderDO orderDO = orderDOMap.get(bulkMaterialDO.getOrderNo());
            //这里统计订单的本次退还散料数+1
            if (!rentBulkMaterialCountNowMap.containsKey(orderDO.getOrderNo())) {
                rentBulkMaterialCountNowMap.put(orderDO.getOrderNo(), 1);
                //这里查询一遍该订单的在租设备总数，保存到map
                rentEquipmentCountMap.put(orderDO.getOrderNo(), productEquipmentMapper.getRentEquipmentCountByOrderNo(orderDO.getOrderNo()));
            } else {
                rentBulkMaterialCountNowMap.put(orderDO.getOrderNo(), rentBulkMaterialCountNowMap.get(orderDO.getOrderNo()) + 1);
            }
            //需还map删除此设备
            Map<Integer,BulkMaterialDO> orderBulkMaterialDOMap = orderBulkMaterialNeedReturnMap.get(orderDO.getOrderNo());
            if(orderBulkMaterialDOMap!=null){
                orderBulkMaterialDOMap.remove(bulkMaterialDO.getId());
            }

            List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
            Map<Integer, OrderMaterialDO> orderMaterialDOMap = new HashMap<>();
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                orderMaterialDOMap.put(orderMaterialDO.getId(), orderMaterialDO);
            }
            //修改设备状态为闲置
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
            bulkMaterialDO.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
            bulkMaterialDO.setOrderNo("");
            bulkMaterialDOListForUpdate.add(bulkMaterialDO);
            //修改订单散料-实际归还时间，实际租金
            OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findRentByCustomerIdAndBulkMaterialId(returnOrderDO.getCustomerId(), bulkMaterialDO.getId());
            ServiceResult<String, String> returnResult = orderService.returnBulkMaterial(orderDO.getOrderNo(), bulkMaterialDO.getBulkMaterialNo(), null, now);
            if (!ErrorCode.SUCCESS.equals(returnResult.getErrorCode())) {
                serviceResult.setErrorCode(returnResult.getErrorCode());
                return serviceResult;
            }

            //由于按天租赁无需授信额度，需交押金，所以当订单按月或按天租赁时，修改客户已用授信额度
            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDOMap.get(orderMaterialBulkDO.getOrderMaterialId()).getRentType())) {
                customerRiskManagementDO.setCreditAmountUsed(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmountUsed(), bulkMaterialDO.getBulkMaterialPrice()));
            }
            ReturnOrderMaterialDO returnOrderMaterialDO = null;
            //修改退还物料项表,实际退还物料数量,修改时间，修改人
            //如果待保存的退还物料项列表里有这个物料了，说明这个物料在新建退还单中没有，而且目前已经入过至少退一个了，那么就累加实际退还字段即可
            if (returnOrderMaterialDOMapForSave.containsKey(bulkMaterialDO.getMaterialId())) {
                returnOrderMaterialDO = returnOrderMaterialDOMapForSave.get(bulkMaterialDO.getMaterialId());
                returnOrderMaterialDO.setRealReturnMaterialCount(returnOrderMaterialDO.getRealReturnMaterialCount() + 1);
            } else if (returnOrderMaterialDOMapForUpdate.containsKey(bulkMaterialDO.getMaterialId())) {
                returnOrderMaterialDO = returnOrderMaterialDOMapForUpdate.get(bulkMaterialDO.getMaterialId());
                returnOrderMaterialDO.setRealReturnMaterialCount(returnOrderMaterialDO.getRealReturnMaterialCount() + 1);
            } else {//否则去查询退还单物料项
                returnOrderMaterialDO = returnOrderMaterialMapper.findByMaterialIdAndReturnOrderId(bulkMaterialDO.getMaterialId(), returnOrderDO.getId());
                //如果退还单物料项不存在，意味着当初建退还单的时候，没有要退该种类的物料，但是货拿回来了
                //这种情况有两种处理方式：1.在本退还单中自动创建该退还物料项 2.不允许在该退还单中退，要新建退还单
                //目前采用第一种处理方式，系统自动创建该退还物料项
                if (returnOrderMaterialDO == null) {
                    MaterialDO materialDO = materialMapper.findById(bulkMaterialDO.getMaterialId());
                    returnOrderMaterialDO = new ReturnOrderMaterialDO();
                    returnOrderMaterialDO.setReturnOrderId(returnOrderDO.getId());
                    returnOrderMaterialDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
                    returnOrderMaterialDO.setReturnMaterialId(bulkMaterialDO.getMaterialId());
                    returnOrderMaterialDO.setReturnMaterialCount(0);
                    returnOrderMaterialDO.setReturnMaterialSnapshot(JSON.toJSONString(ConverterUtil.convert(materialDO, Material.class)));
                    returnOrderMaterialDO.setRealReturnMaterialCount(1);
                    returnOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    returnOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDO.setCreateTime(now);
                    returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDO.setUpdateTime(now);
                    returnOrderMaterialDOMapForSave.put(bulkMaterialDO.getMaterialId(), returnOrderMaterialDO);
                } else {
                    returnOrderMaterialDO.setRealReturnMaterialCount(returnOrderMaterialDO.getRealReturnMaterialCount() + 1);
                    returnOrderMaterialDO.setUpdateTime(now);
                    returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDOMapForUpdate.put(returnOrderMaterialDO.getReturnMaterialId(), returnOrderMaterialDO);
                }
            }
            //添加退还散料记录
            ReturnOrderMaterialBulkDO returnOrderMaterialBulkDO = new ReturnOrderMaterialBulkDO();
            returnOrderMaterialBulkDO.setReturnOrderMaterialId(returnOrderMaterialDO.getId());
            returnOrderMaterialBulkDO.setReturnOrderId(returnOrderDO.getId());
            returnOrderMaterialBulkDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
            returnOrderMaterialBulkDO.setOrderNo(orderDO.getOrderNo());
            returnOrderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
            returnOrderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
            returnOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            returnOrderMaterialBulkDO.setCreateTime(now);
            returnOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
            returnOrderMaterialBulkDO.setUpdateTime(now);
            returnOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderMaterialBulkDOListForSave.add(returnOrderMaterialBulkDO);
        }

        //待更新的订单列表
        List<OrderDO> orderDOListForUpdate = new ArrayList<>();
        for (String orderNo : rentBulkMaterialCountNowMap.keySet()) {
            //修改订单,全部归还状态，最后一件归还时间
            if (rentEquipmentCountMap.get(orderNo) == 0 && orderBulkMaterialNeedReturnMap.get(orderNo).size() == 0) {
                OrderDO orderDO = orderDOMap.get(orderNo);
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK);
                orderDO.setActualReturnTime(now);
                orderDOListForUpdate.add(orderDO);
            }
        }
        //批量更新散料列表
        bulkMaterialMapper.updateList(bulkMaterialDOListForUpdate);
        //批量更新退还物料项列表
        if (returnOrderMaterialDOMapForUpdate.size() > 0) {
            List<ReturnOrderMaterialDO> returnOrderMaterialDOListForUpdate = new ArrayList<>();
            for (Integer key : returnOrderMaterialDOMapForUpdate.keySet()) {
                returnOrderMaterialDOListForUpdate.add(returnOrderMaterialDOMapForUpdate.get(key));
            }
            returnOrderMaterialMapper.updateListForReturn(returnOrderMaterialDOListForUpdate);
        }

        //批量保存退还物料项列表
        if (returnOrderMaterialDOMapForSave.size() > 0) {
            List<ReturnOrderMaterialDO> returnOrderMaterialDOListForSave = new ArrayList<>();
            for (Integer key : returnOrderMaterialDOMapForSave.keySet()) {
                returnOrderMaterialDOListForSave.add(returnOrderMaterialDOMapForSave.get(key));
            }
            returnOrderMaterialMapper.saveList(returnOrderMaterialDOListForSave);
        }
        //批量保存退还散料列表
        returnOrderMaterialBulkMapper.saveList(returnOrderMaterialBulkDOListForSave);
        //批量更新订单列表
        if (orderDOListForUpdate.size() > 0) {
            orderMapper.updateListForReturn(orderDOListForUpdate);
        }
        //修改退还单，归还订单状态,租赁期间产生总费用,实际退还商品总数,修改时间，修改人
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
//        returnOrderDO.setTotalRentCost(BigDecimalUtil.add(returnOrderDO.getTotalRentCost(), rentCostTotal));
        returnOrderDO.setRealTotalReturnMaterialCount(returnOrderDO.getRealTotalReturnMaterialCount() + doReturnMaterialParam.getReturnCount());
        returnOrderDO.setUpdateTime(now);
        returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderMapper.update(returnOrderDO);
        //更新客户风控信息
        customerRiskManagementMapper.update(customerRiskManagementDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, ReturnOrder> detail(ReturnOrder returnOrder) {
        ServiceResult<String, ReturnOrder> serviceResult = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrder.getReturnOrderNo());

        returnOrder = ConverterUtil.convert(returnOrderDO, ReturnOrder.class);
        serviceResult.setResult(returnOrder);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<ReturnOrder>> page(ReturnOrderPageParam returnOrderPageParam) {
        ServiceResult<String, Page<ReturnOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(returnOrderPageParam.getPageNo(), returnOrderPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("returnOrderPageParam", returnOrderPageParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = returnOrderMapper.findReturnOrderCountByParams(maps);
        List<ReturnOrderDO> purchaseOrderDOList = returnOrderMapper.findReturnOrderByParams(maps);
        List<ReturnOrder> purchaseOrderList = ConverterUtil.convertList(purchaseOrderDOList, ReturnOrder.class);
        Page<ReturnOrder> page = new Page<>(purchaseOrderList, totalCount, returnOrderPageParam.getPageNo(), returnOrderPageParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    /**
     * 只有处理中的订单可以结束
     *
     * @param returnOrder
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> end(ReturnOrder returnOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (returnOrder.getServiceCost().compareTo(BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_SERVICE_COST_ERROR);
            return serviceResult;
        }
        if (returnOrder.getDamageCost().compareTo(BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_DAMAGE_COST_ERROR);
            return serviceResult;
        }
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrder.getReturnOrderNo());
        if (returnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(returnOrderDO.getReturnOrderStatus())) {
            returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_END);
            returnOrderDO.setUpdateTime(new Date());
            returnOrderDO.setReturnTime(returnOrder.getReturnTime());
            returnOrderDO.setServiceCost(returnOrder.getServiceCost());
            returnOrderDO.setDamageCost(returnOrder.getDamageCost());
            returnOrderDO.setIsDamage(returnOrder.getIsDamage());
            returnOrderDO.setRemark(returnOrder.getRemark());
            returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderMapper.update(returnOrderDO);
        } else {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_END);
            return serviceResult;
        }
        //调用结算单接口
        ServiceResult<String, BigDecimal> statementResult = statementService.createReturnOrderStatement(returnOrderDO.getReturnOrderNo());
        if (!ErrorCode.SUCCESS.equals(statementResult.getErrorCode())) {
            serviceResult.setErrorCode(statementResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnOrderDO.getReturnOrderNo());
        return serviceResult;
    }

    /**
     * 只有待提交订单可以取消
     *
     * @param returnOrder
     * @return
     */
    @Override
    public ServiceResult<String, String> cancel(ReturnOrder returnOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrder.getReturnOrderNo());
        if (returnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(returnOrderDO.getReturnOrderStatus())) {
            returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_CANCEL);
            returnOrderDO.setUpdateTime(new Date());
            returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderMapper.update(returnOrderDO);
        } else {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_CANCEL);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnOrderDO.getReturnOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<ReturnOrderProductEquipment>> pageReturnEquipment(ReturnEquipmentPageParam returnEquipmentPageParam) {
        ServiceResult<String, Page<ReturnOrderProductEquipment>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(returnEquipmentPageParam.getPageNo(), returnEquipmentPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("returnEquipmentPageParam", returnEquipmentPageParam);

        Integer totalCount = returnOrderProductEquipmentMapper.listCount(maps);
        List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDOList = returnOrderProductEquipmentMapper.listPage(maps);
        List<ReturnOrderProductEquipment> returnOrderProductEquipmentList = ConverterUtil.convertList(returnOrderProductEquipmentDOList, ReturnOrderProductEquipment.class);
        for (ReturnOrderProductEquipment returnOrderProductEquipment : returnOrderProductEquipmentList) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(returnOrderProductEquipment.getEquipmentNo());
            returnOrderProductEquipment.setProductEquipment(ConverterUtil.convert(productEquipmentDO, ProductEquipment.class));
        }
        Page<ReturnOrderProductEquipment> page = new Page<>(returnOrderProductEquipmentList, totalCount, returnEquipmentPageParam.getPageNo(), returnEquipmentPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ReturnOrderMaterialBulk>> pageReturnBulk(ReturnBulkPageParam returnBulkPageParam) {
        ServiceResult<String, Page<ReturnOrderMaterialBulk>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(returnBulkPageParam.getPageNo(), returnBulkPageParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("returnBulkPageParam", returnBulkPageParam);

        Integer totalCount = returnOrderMaterialBulkMapper.listCount(maps);
        List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDOList = returnOrderMaterialBulkMapper.listPage(maps);
        List<ReturnOrderMaterialBulk> returnOrderMaterialBulkList = ConverterUtil.convertList(returnOrderMaterialBulkDOList, ReturnOrderMaterialBulk.class);
        for (ReturnOrderMaterialBulk returnOrderMaterialBulk : returnOrderMaterialBulkList) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(returnOrderMaterialBulk.getBulkMaterialNo());
            returnOrderMaterialBulk.setBulkMaterial(ConverterUtil.convert(bulkMaterialDO, BulkMaterial.class));
        }
        Page<ReturnOrderMaterialBulk> page = new Page<>(returnOrderMaterialBulkList, totalCount, returnBulkPageParam.getPageNo(), returnBulkPageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(UpdateReturnOrderParam updateReturnOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(updateReturnOrderParam.getReturnOrderNo());
        if (returnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(returnOrderDO.getReturnOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_CAN_NOT_UPDATE);
            return serviceResult;
        }
        UserDO owner = userMapper.findByUserId(updateReturnOrderParam.getOwner());
        if(owner==null){
            serviceResult.setErrorCode(ErrorCode.SALES_MAN_NOT_NULL);
            return serviceResult;
        }
        //校验退还商品项sku不能重复
        List<ReturnOrderProduct> returnOrderProductList = updateReturnOrderParam.getReturnOrderProductList();
        Set<Integer> skuIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(returnOrderProductList)) {
            for (ReturnOrderProduct returnOrderProduct : returnOrderProductList) {
                skuIdSet.add(returnOrderProduct.getReturnProductSkuId());
            }
            if (skuIdSet.size() < returnOrderProductList.size()) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //校验退还物料项sku不能重复
        List<ReturnOrderMaterial> returnOrderMaterialList = updateReturnOrderParam.getReturnOrderMaterialList();
        Set<String> materialIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(returnOrderMaterialList)) {
            for (ReturnOrderMaterial returnOrderMaterial : returnOrderMaterialList) {
                materialIdSet.add(returnOrderMaterial.getReturnMaterialNo());
            }
            if (materialIdSet.size() < returnOrderMaterialList.size()) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //用户在租sku统计
        Map<String, Object> findSkuRentMap = customerOrderSupport.getCustomerAllMap(returnOrderDO.getCustomerId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findSkuRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        findSkuRentMap = customerOrderSupport.getCustomerCanReturnAllMap(returnOrderDO.getCustomerId());
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findSkuRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计退还sku总数
        Integer totalReturnProductCount = 0;
        //累计退还物料总数
        Integer totalReturnMaterialCount = 0;
        Date now = new Date();
        //构造待保存退换单商品项
        List<ReturnOrderProductDO> returnOrderProductDOListForSave = new ArrayList<>();
        //定义要删除的列表
        Map<Integer, ReturnOrderProductDO> returnOrderProductDOMapForDelete = new HashMap<>();

        //如果要退还的sku不在在租列表，或者要退还的sku数量大于可退数量，返回相应错误

        if (CollectionUtil.isNotEmpty(returnOrderProductList)) {
            //查找原退货单商品项列表
            List<ReturnOrderProductDO> oldReturnOrderProductDOList = returnOrderProductMapper.findByReturnOrderId(returnOrderDO.getId());
            Map<Integer, ReturnOrderProductDO> oldReturnOrderProductDOMap = new HashMap<>();
            for (ReturnOrderProductDO returnOrderProductDO : oldReturnOrderProductDOList) {
                oldReturnOrderProductDOMap.put(returnOrderProductDO.getReturnProductSkuId(), returnOrderProductDO);
                returnOrderProductDOMapForDelete.put(returnOrderProductDO.getId(), returnOrderProductDO);
            }
            for (ReturnOrderProduct returnOrderProduct : returnOrderProductList) {
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(returnOrderProduct.getReturnProductSkuId());
                //本退还单此sku占的退还数量
                ReturnOrderProductDO returnOrderProductDO = oldReturnOrderProductDOMap.get(returnOrderProduct.getReturnProductSkuId());
                Integer thisReturnOrderSkuCount = returnOrderProductDO == null ? 0 : returnOrderProductDO.getReturnProductSkuCount();
                if (oldSkuRent == null) {//如果要退还的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                } else if (returnOrderProduct.getReturnProductSkuCount() > oldSkuRent.getCanProcessCount() + thisReturnOrderSkuCount) {//退还的sku数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                totalReturnProductCount += returnOrderProduct.getReturnProductSkuCount();
                ServiceResult<String, Product> productSkuResult = productService.queryProductBySkuId(returnOrderProduct.getReturnProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productSkuResult.getErrorCode()) || productSkuResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                //如果没有此skuId项
                if (returnOrderProductDO == null) {
                    returnOrderProductDO = new ReturnOrderProductDO();
                    returnOrderProductDO.setReturnProductSkuId(returnOrderProduct.getReturnProductSkuId());
                    returnOrderProductDO.setReturnProductSkuCount(returnOrderProduct.getRealReturnProductSkuCount());
                    returnOrderProductDO.setReturnProductSkuSnapshot(JSON.toJSONString(productSkuResult.getResult()));
                    returnOrderProductDO.setRealReturnProductSkuCount(0);
                    returnOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    returnOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    returnOrderProductDO.setCreateTime(now);
                    returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderProductDO.setUpdateTime(now);
                    returnOrderProductDOListForSave.add(returnOrderProductDO);
                } else {
                    //修改删除时，总数量相应改变
                    totalReturnProductCount = totalReturnProductCount - returnOrderProductDO.getReturnProductSkuCount() + returnOrderProduct.getReturnProductSkuCount();
                    //有skuId项，则修改，并将待删除的列表此项删除
                    returnOrderProductDO.setReturnProductSkuCount(returnOrderProduct.getReturnProductSkuCount());
                    returnOrderProductDO.setReturnProductSkuSnapshot(JSON.toJSONString(productSkuResult.getResult()));
                    returnOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderProductDO.setUpdateTime(now);
                    //这里暂时没有做批量修改
                    returnOrderProductMapper.update(returnOrderProductDO);
                    returnOrderProductDOMapForDelete.remove(returnOrderProductDO.getId());
                }
            }
        }
        //构造待保存退换单物料项
        List<ReturnOrderMaterialDO> returnOrderMaterialDOListForSave = new ArrayList<>();
        //定义要删除的物料项列表
        Map<Integer, ReturnOrderMaterialDO> returnOrderMaterialDOMapForDelete = new HashMap<>();

        //如果要退还的物料不在在租列表，或者要退还的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(returnOrderMaterialList)) {
            //查找原退货单商品项列表
            List<ReturnOrderMaterialDO> oldReturnOrderMaterialDOList = returnOrderMaterialMapper.findByReturnOrderId(returnOrderDO.getId());
            Map<Integer, ReturnOrderMaterialDO> oldReturnOrderMaterialDOMap = new HashMap<>();
            for (ReturnOrderMaterialDO returnOrderMaterialDO : oldReturnOrderMaterialDOList) {
                oldReturnOrderMaterialDOMap.put(returnOrderMaterialDO.getReturnMaterialId(), returnOrderMaterialDO);
                returnOrderMaterialDOMapForDelete.put(returnOrderMaterialDO.getId(), returnOrderMaterialDO);
            }

            for (ReturnOrderMaterial returnOrderMaterial : returnOrderMaterialList) {
                MaterialDO oldMaterialRent = oldMaterialCountMap.get(returnOrderMaterial.getReturnMaterialNo());
                //本退还单此物料占的退还数量
                Integer thisReturnOrderMaterialCount = 0;
                if (oldMaterialRent != null) {
                    thisReturnOrderMaterialCount = oldReturnOrderMaterialDOMap.get(oldMaterialRent.getId()) == null ? 0 : oldReturnOrderMaterialDOMap.get(oldMaterialRent.getId()).getReturnMaterialCount();
                }
                if (oldMaterialRent == null) {//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                } else if (returnOrderMaterial.getReturnMaterialCount() > oldMaterialRent.getCanProcessCount() + thisReturnOrderMaterialCount) {
                    //退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                //如果原列表没有此物料项
                if (oldReturnOrderMaterialDOMap.get(oldMaterialRent.getId()) == null) {
                    totalReturnMaterialCount += returnOrderMaterial.getReturnMaterialCount();
                    ReturnOrderMaterialDO returnOrderMaterialDO = new ReturnOrderMaterialDO();
                    returnOrderMaterialDO.setReturnMaterialId(oldMaterialRent.getId());
                    returnOrderMaterialDO.setReturnMaterialCount(returnOrderMaterial.getReturnMaterialCount());
                    returnOrderMaterialDO.setReturnMaterialSnapshot(JSON.toJSONString(oldMaterialRent));
                    returnOrderMaterialDO.setRealReturnMaterialCount(0);
                    returnOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    returnOrderMaterialDO.setCreateTime(now);
                    returnOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDO.setUpdateTime(now);
                    returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDOListForSave.add(returnOrderMaterialDO);
                } else {
                    //有此物料项，则修改，并将待删除的列表此项删除
                    ReturnOrderMaterialDO returnOrderMaterialDO = oldReturnOrderMaterialDOMap.get(oldMaterialRent.getId());
                    //修改删除时，总数量相应改变
                    totalReturnMaterialCount = totalReturnMaterialCount - returnOrderMaterialDO.getReturnMaterialCount() + returnOrderMaterial.getReturnMaterialCount();
                    returnOrderMaterialDO.setReturnMaterialCount(returnOrderMaterial.getReturnMaterialCount());
                    returnOrderMaterialDO.setReturnMaterialSnapshot(JSON.toJSONString(oldMaterialRent));
                    returnOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    returnOrderMaterialDO.setUpdateTime(now);
                    //这里暂时没有做批量修改
                    returnOrderMaterialMapper.update(returnOrderMaterialDO);
                    returnOrderMaterialDOMapForDelete.remove(returnOrderMaterialDO.getId());
                }
            }
        }
        //删除新列表中没有的商品项
        for (Integer id : returnOrderProductDOMapForDelete.keySet()) {
            ReturnOrderProductDO returnOrderProductDO = returnOrderProductDOMapForDelete.get(id);
            totalReturnProductCount = totalReturnProductCount - returnOrderProductDO.getReturnProductSkuCount();
            returnOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            returnOrderProductDO.setUpdateTime(now);
            returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderProductMapper.update(returnOrderProductDO);
        }
        //删除新列表中没有的物料项
        for (Integer id : returnOrderMaterialDOMapForDelete.keySet()) {
            ReturnOrderMaterialDO returnOrderMaterialDO = returnOrderMaterialDOMapForDelete.get(id);
            totalReturnMaterialCount = totalReturnMaterialCount - returnOrderMaterialDO.getReturnMaterialCount();
            returnOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            returnOrderMaterialDO.setUpdateTime(now);
            returnOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderMaterialMapper.update(returnOrderMaterialDO);
        }
        //修改租赁退还单
        returnOrderDO.setReturnMode(updateReturnOrderParam.getReturnMode());
        returnOrderDO.setIsCharging(updateReturnOrderParam.getIsCharging());
        returnOrderDO.setReturnTime(updateReturnOrderParam.getReturnTime());
        returnOrderDO.setTotalReturnProductCount(totalReturnProductCount);
        returnOrderDO.setTotalReturnMaterialCount(totalReturnMaterialCount);
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        returnOrderDO.setReturnReasonType(updateReturnOrderParam.getReturnReasonType());
        returnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderDO.setOwner(owner.getId());
        returnOrderDO.setRemark(updateReturnOrderParam.getRemark());
        returnOrderDO.setUpdateTime(now);
        returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderMapper.update(returnOrderDO);

        //保存取货地址信息
        ReturnOrderConsignInfoDO returnOrderConsignInfoDO = returnOrderDO.getReturnOrderConsignInfoDO();
        returnOrderConsignInfoDO.setConsigneeName(updateReturnOrderParam.getReturnOrderConsignInfo().getConsigneeName());
        returnOrderConsignInfoDO.setConsigneePhone(updateReturnOrderParam.getReturnOrderConsignInfo().getConsigneePhone());
        returnOrderConsignInfoDO.setProvince(updateReturnOrderParam.getReturnOrderConsignInfo().getProvince());
        returnOrderConsignInfoDO.setCity(updateReturnOrderParam.getReturnOrderConsignInfo().getCity());
        returnOrderConsignInfoDO.setDistrict(updateReturnOrderParam.getReturnOrderConsignInfo().getDistrict());
        returnOrderConsignInfoDO.setAddress(updateReturnOrderParam.getReturnOrderConsignInfo().getAddress());
        returnOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderConsignInfoDO.setRemark(updateReturnOrderParam.getReturnOrderConsignInfo().getRemark());
        returnOrderConsignInfoDO.setUpdateTime(now);
        returnOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderConsignInfoMapper.update(returnOrderConsignInfoDO);

        //保存退还商品项
        if (CollectionUtil.isNotEmpty(returnOrderProductDOListForSave)) {
            returnOrderProductMapper.batchSave(returnOrderDO.getId(), returnOrderDO.getReturnOrderNo(), returnOrderProductDOListForSave);
        }
        //保存退还物料项
        if (CollectionUtil.isNotEmpty(returnOrderMaterialDOListForSave)) {
            returnOrderMaterialMapper.batchSave(returnOrderDO.getId(), returnOrderDO.getReturnOrderNo(), returnOrderMaterialDOListForSave);
        }

        serviceResult.setResult(returnOrderDO.getReturnOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> commit(ReturnOrderCommitParam returnOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        //校验采购单是否存在
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderCommitParam.getReturnOrderNo());
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        } else if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(returnOrderDO.getReturnOrderStatus())) {
            //只有待提交状态的采购单可以提交
            result.setErrorCode(ErrorCode.RETURN_ORDER_CAN_NOT_COMMIT);
            return result;
        }
        if (!returnOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建采购单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_RETURN);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (returnOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_RETURN, returnOrderDO.getReturnOrderNo(), returnOrderCommitParam.getVerifyUserId(),null, returnOrderCommitParam.getRemark());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
                returnOrderDO.setUpdateTime(now);
                returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                returnOrderMapper.update(returnOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_TAKEN);
            returnOrderDO.setUpdateTime(now);
            returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderMapper.update(returnOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(businessNo);
            if (returnOrderDO == null) {
                return false;
            }
            //不是审核中状态的退还单，拒绝处理
            if (!ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(returnOrderDO.getReturnOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_TAKEN);
            } else {
                returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
            }
            returnOrderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
            returnOrderDO.setUpdateTime(new Date());
            returnOrderMapper.update(returnOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("【退还单审核业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        } catch (Throwable t) {
            logger.error("【退还单审核业务处理异常】", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        }
    }

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReturnOrderProductMapper returnOrderProductMapper;
    @Autowired
    private ReturnOrderMaterialMapper returnOrderMaterialMapper;
    @Autowired
    private ReturnOrderConsignInfoMapper returnOrderConsignInfoMapper;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
    @Autowired
    private ReturnOrderProductEquipmentMapper returnOrderProductEquipmentMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private ReturnOrderMaterialBulkMapper returnOrderMaterialBulkMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StatementService statementService;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PermissionSupport permissionSupport;
    @Autowired
    private UserMapper userMapper;


}
