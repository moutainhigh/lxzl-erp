package com.lxzl.erp.core.service.returnOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnMaterialParam;
import com.lxzl.erp.common.domain.returnOrder.ReturnOrderPageParam;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductEquipmentConverter;
import com.lxzl.erp.core.service.returnOrder.ReturnOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.*;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> create(AddReturnOrderParam addReturnOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addReturnOrderParam.getCustomerNo());
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //校验退还商品项sku不能重复
        List<ProductSku> productSkuList = addReturnOrderParam.getProductSkuList();
        Set<Integer> skuIdSet = new HashSet<>();
        if(CollectionUtil.isNotEmpty(productSkuList)){
            for(ProductSku productSku : productSkuList){
                skuIdSet.add(productSku.getSkuId());
            }
            if(skuIdSet.size()<productSkuList.size()){
                serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //校验退还商品项sku不能重复
        List<Material> materialList = addReturnOrderParam.getMaterialList();
        Set<Integer> materialIdSet = new HashSet<>();
        if(CollectionUtil.isNotEmpty(materialList)){
            for(Material material : materialList){
                materialIdSet.add(material.getMaterialId());
            }
            if(materialIdSet.size()<materialList.size()){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                return serviceResult;
            }
        }
        //用户在租sku统计
        List<ProductSkuDO> oldProductSkuDOList = productMapper.findSkuRentByCustomerId(customerDO.getId());

        Map<Integer,ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldProductSkuDOList)){
            for(ProductSkuDO productSkuDO  : oldProductSkuDOList){
                oldSkuCountMap.put(productSkuDO.getId(),productSkuDO);
            }
        }
        //用户在租物料统计
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRentByCustomerId(customerDO.getId());
        Map<String,MaterialDO> oldMaterialCountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldMaterialDOList)){
            for(MaterialDO materialDO  : oldMaterialDOList){
                oldMaterialCountMap.put(materialDO.getMaterialNo(),materialDO);
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

        if(CollectionUtil.isNotEmpty(productSkuList)){
            for(ProductSku productSku : productSkuList){
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(productSku.getSkuId());
               if(oldSkuRent==null){//如果要退还的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                   return serviceResult;
               }else if(productSku.getReturnCount()>oldSkuRent.getCanReturnCount()){//退还的sku数量大于可租数量
                   serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                   return serviceResult;
               }
                totalReturnProductCount+=productSku.getReturnCount();
                ServiceResult<String , Product> productSkuResult = productService.queryProductBySkuId(productSku.getSkuId());
                if(!ErrorCode.SUCCESS.equals(productSkuResult.getErrorCode())||productSkuResult.getResult()==null){
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                ReturnOrderProductDO returnOrderProductDO = new ReturnOrderProductDO();
                returnOrderProductDO.setReturnProductSkuId(productSku.getSkuId());
                returnOrderProductDO.setReturnProductSkuCount(productSku.getReturnCount());
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
        if(CollectionUtil.isNotEmpty(materialList)){
            for(Material material : materialList){
                MaterialDO oldMaterialRent = oldMaterialCountMap.get(material.getMaterialNo());
                if(oldMaterialRent==null){//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }else if(material.getReturnCount()>oldMaterialRent.getCanReturnCount()){//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                totalReturnMaterialCount+=material.getReturnCount();
                ReturnOrderMaterialDO returnOrderMaterialDO = new ReturnOrderMaterialDO();
                returnOrderMaterialDO.setReturnMaterialId(oldMaterialRent.getId());
                returnOrderMaterialDO.setReturnMaterialCount(material.getReturnCount());
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
        returnOrderDO.setReturnOrderNo(GenerateNoUtil.generateReturnOrderNo(now));
        returnOrderDO.setCustomerId(customerDO.getId());
        returnOrderDO.setCustomerNo(customerDO.getCustomerNo());
        returnOrderDO.setIsCharging(addReturnOrderParam.getIsCharging());
        returnOrderDO.setTotalReturnProductCount(totalReturnProductCount);
        returnOrderDO.setTotalReturnMaterialCount(totalReturnMaterialCount);
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAITING);
        returnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderDO.setOwner(userSupport.getCurrentUserId());
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
        if(CollectionUtil.isNotEmpty(returnOrderProductDOList)){
            returnOrderProductMapper.batchSave(returnOrderDO.getId(),returnOrderDO.getReturnOrderNo(),returnOrderProductDOList);
        }
        //保存退还物料项
        if(CollectionUtil.isNotEmpty(returnOrderMaterialDOList)){
            returnOrderMaterialMapper.batchSave(returnOrderDO.getId(),returnOrderDO.getReturnOrderNo(),returnOrderMaterialDOList);
        }
        serviceResult.setResult(returnOrderDO.getReturnOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, ProductEquipment> doReturnEquipment(DoReturnEquipmentParam doReturnEquipmentParam) {
        ServiceResult<String, ProductEquipment> serviceResult = new ServiceResult<>();
        //校验退还单是否存在
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(doReturnEquipmentParam.getReturnOrderNo());
        if(returnOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NO_EXISTS);
            return serviceResult;
        }
        //用设备的订单号方式查询客户的所有在租设备，并保存到map以便后续使用
        Map<String,ProductEquipmentDO> rentMap = new HashMap<>();
        List<ProductEquipmentDO> productEquipmentDOList = productEquipmentMapper.findByCustomerId(returnOrderDO.getCustomerId());
        for(ProductEquipmentDO productEquipmentDO : productEquipmentDOList){
            rentMap.put(productEquipmentDO.getEquipmentNo(),productEquipmentDO);
        }
        //校验该设备是否可退
        ProductEquipmentDO productEquipmentDO = rentMap.get(doReturnEquipmentParam.getEquipmentNo());
        if(productEquipmentDO==null){
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_RENT);
            return serviceResult;
        }


        //取得订单，并且把商品项存入map方便查找
        OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        Map<Integer,OrderProductDO> orderProductDOMap = new HashMap<>();
        for(OrderProductDO orderProductDO : orderProductDOList){
            orderProductDOMap.put(orderProductDO.getId(),orderProductDO);
        }

        //修改设备状态为闲置
        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        productEquipmentDO.setOrderNo("");
        productEquipmentMapper.update(productEquipmentDO);


        //修改订单商品设备-实际归还时间，实际租金
        Date now = new Date();
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findRentByCustomerIdAndEquipmentId(returnOrderDO.getCustomerId(),productEquipmentDO.getId());
        OrderProductDO orderProductDO = orderProductDOMap.get(orderProductEquipmentDO.getOrderProductId());
        //todo 计算该设备的租金
        BigDecimal rentCost = CommonConstant.COMMON_CONSTANT_NO.equals(returnOrderDO.getIsCharging())?
                new BigDecimal(0):amountSupport.calculateRentCost(orderProductDO.getProductUnitAmount(),orderDO.getRentStartTime(),now,orderDO.getRentType());
        orderProductEquipmentDO.setActualRentAmount(rentCost);
        orderProductEquipmentDO.setActualReturnTime(now);
        orderProductEquipmentMapper.update(orderProductEquipmentDO);

        //由于按天租赁无需授信额度，需交押金，所以当订单按月或按天租赁时，修改客户已用授信额度
        if(OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())||OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())){
            CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(returnOrderDO.getCustomerId());
            customerRiskManagementDO.setCreditAmountUsed(BigDecimalUtil.add(customerRiskManagementDO.getCreditAmountUsed(),productEquipmentDO.getEquipmentPrice()));
            customerRiskManagementMapper.update(customerRiskManagementDO);
        }
        //修改订单,全部归还状态，最后一件归还时间
        if(rentMap.size()==1){
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK);
            orderDO.setActualReturnTime(now);
            orderMapper.update(orderDO);
        }
        //修改退还单，归还订单状态，最后一件设备归还的时间,租赁期间产生总费用,实际退还商品总数,修改时间，修改人
        returnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
        returnOrderDO.setTotalRentCost(BigDecimalUtil.add(returnOrderDO.getTotalRentCost(),rentCost));
        returnOrderDO.setRealTotalReturnProductCount(returnOrderDO.getRealTotalReturnProductCount()+1);
        returnOrderDO.setUpdateTime(now);
        returnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        returnOrderMapper.update(returnOrderDO);
        //修改退还商品项表,实际退还商品数量,修改时间，修改人
        ReturnOrderProductDO returnOrderProductDO = returnOrderProductMapper.findBySkuIdAndReturnOrderId(productEquipmentDO.getSkuId(),returnOrderDO.getId());
        //如果退还单商品项不存在，意味着当初建退还单的时候，没有要退该种类的sku商品，但是货拿回来了
        //这种情况有两种处理方式：1.在本退还单中自动创建该sku的退还商品项 2.不允许在该退还单中退，要新建退还单
        //目前采用第一种处理方式，系统自动创建该sku的退还商品项
        if(returnOrderProductDO==null){
            ServiceResult<String , Product> productSkuResult = productService.queryProductBySkuId(productEquipmentDO.getSkuId());
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
        }else{
            returnOrderProductDO.setRealReturnProductSkuCount(returnOrderProductDO.getRealReturnProductSkuCount()+1);
            returnOrderProductDO.setUpdateTime(now);
            returnOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            returnOrderProductMapper.update(returnOrderProductDO);
        }
        //添加退还商品设备表记录
        ReturnOrderProductEquipmentDO returnOrderProductEquipmentDO = new ReturnOrderProductEquipmentDO();
        returnOrderProductEquipmentDO.setReturnOrderProductId(returnOrderProductDO.getId());
        returnOrderProductEquipmentDO.setReturnOrderId(returnOrderDO.getId());
        returnOrderProductEquipmentDO.setReturnOrderNo(returnOrderDO.getReturnOrderNo());
        returnOrderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
        returnOrderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
        returnOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnOrderProductEquipmentDO.setCreateTime(now);
        returnOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
        returnOrderProductEquipmentDO.setUpdateTime(now);
        returnOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnOrderProductEquipmentMapper.save(returnOrderProductEquipmentDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ProductEquipmentConverter.convertProductEquipmentDO(productEquipmentDO));
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Material> doReturnMaterial(DoReturnMaterialParam doReturnMaterialParam) {
        ServiceResult<String, Material> serviceResult = new ServiceResult<>();
        //校验退还单是否存在
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(doReturnMaterialParam.getReturnOrderNo());
        if(returnOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NO_EXISTS);
            return serviceResult;
        }
        List<String> materialNoList = doReturnMaterialParam.getMaterialNoList();

        //用散料的订单号方式查询客户的所有在租设备，并保存到map以便后续使用
        Map<String,MaterialDO> rentMap = new HashMap<>();
        List<MaterialDO> materialDOList = materialMapper.findMaterialRentByCustomerId(returnOrderDO.getCustomerId());
//        for(ProductEquipmentDO productEquipmentDO : productEquipmentDOList){
//            rentMap.put(productEquipmentDO.getEquipmentNo(),productEquipmentDO);
//        }
        return null;
    }

    @Override
    public ServiceResult<String, ReturnOrder> detail(ReturnOrder returnOrder) {
        ServiceResult<String, ReturnOrder> serviceResult = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrder.getReturnOrderNo());
        serviceResult.setResult(ConverterUtil.convert(returnOrderDO,ReturnOrder.class));
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
        maps.put("queryParam", returnOrderPageParam);

        Integer totalCount = returnOrderMapper.findReturnOrderCountByParams(maps);
        List<ReturnOrderDO> purchaseOrderDOList = returnOrderMapper.findReturnOrderByParams(maps);
        List<ReturnOrder> purchaseOrderList = ConverterUtil.convertList(purchaseOrderDOList,ReturnOrder.class);
        Page<ReturnOrder> page = new Page<>(purchaseOrderList, totalCount, returnOrderPageParam.getPageNo(), returnOrderPageParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private ProductMapper productMapper;
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
    private AmountSupport amountSupport;
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
    @Autowired
    private ReturnOrderProductEquipmentMapper returnOrderProductEquipmentMapper;
}
