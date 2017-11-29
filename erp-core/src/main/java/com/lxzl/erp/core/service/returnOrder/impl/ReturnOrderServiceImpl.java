package com.lxzl.erp.core.service.returnOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ProductEquipmentStatus;
import com.lxzl.erp.common.constant.ReturnOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.util.AmountUtil;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.returnOrder.ReturnOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        Map<Integer,MaterialDO> oldMaterialCountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(oldMaterialDOList)){
            for(MaterialDO materialDO  : oldMaterialDOList){
                oldMaterialCountMap.put(materialDO.getId(),materialDO);
            }
        }
        //累计退还sku总数
        Integer totalReturnProductCount = 0;
        //累计退还物料总数
        Integer totalReturnMaterialCount = 0;
        Date now = new Date();
        //构造待保存退换单商品项
        List<ReturnOrderProductDO> returnOrderProductDOList = new ArrayList<>();
        //如果要退还的sku不在在租列表，或者要退还的sku数量大于可租数量，返回相应错误
        List<ProductSku> productSkuList = addReturnOrderParam.getProductSkuList();
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
        //构造待保存退换单商品项
        List<ReturnOrderMaterialDO> returnOrderMaterialDOList = new ArrayList<>();
        //如果要退还的物料不在在租列表，或者要退还的物料数量大于在租数量，返回相应错误
        List<Material> materialList = addReturnOrderParam.getMaterialList();
        if(CollectionUtil.isNotEmpty(materialList)){
            for(Material material : materialList){
                MaterialDO oldMaterialRent = oldMaterialCountMap.get(material.getMaterialId());
                if(oldMaterialRent==null){//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }else if(material.getReturnCount()>oldMaterialRent.getCanReturnCount()){//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                totalReturnMaterialCount+=material.getReturnCount();
                ReturnOrderMaterialDO returnOrderMaterialDO = new ReturnOrderMaterialDO();
                returnOrderMaterialDO.setReturnMaterialId(material.getMaterialId());
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
        returnOrderDO.setIsCharging(CommonConstant.COMMON_CONSTANT_YES);
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
        //修改设备状态为空闲
        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
        productEquipmentDO.setOrderNo("");
        productEquipmentMapper.update(productEquipmentDO);

        //取得订单，并且把商品项存入map方便查找
        OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        Map<Integer,OrderProductDO> orderProductDOMap = new HashMap<>();
        for(OrderProductDO orderProductDO : orderProductDOList){
            orderProductDOMap.put(orderProductDO.getId(),orderProductDO);
        }
        //修改订单商品设备-实际归还时间，实际租金
        Date now = new Date();
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findRentByCustomerIdAndEquipmentId(returnOrderDO.getCustomerId(),productEquipmentDO.getId());
        OrderProductDO orderProductDO = orderProductDOMap.get(orderProductEquipmentDO.getOrderProductId());
        //todo 计算该设备的租金
        orderProductEquipmentDO.setActualRentAmount(amountSupport.calculateRentCost(orderProductDO.getProductUnitAmount(),orderDO.getRentStartTime(),now,orderDO.getRentType()));
        orderProductEquipmentDO.setActualReturnTime(now);
        orderProductEquipmentMapper.update(orderProductEquipmentDO);

        //修改客户授信额度
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(returnOrderDO.getCustomerId());
        customerRiskManagementDO.setCreditAmountUsed(BigDecimalUtil.add(customerRiskManagementDO.getCreditAmountUsed(),productEquipmentDO.getEquipmentPrice()));
        customerRiskManagementMapper.update(customerRiskManagementDO);

        //todo 修改相应订单
        //todo 修改退还单
        //todo 修改退还商品项表
        //todo 修改退还商品设备表

        return serviceResult;
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
    private OrderProductMapper orderProductMapper;
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
}
