package com.lxzl.erp.core.service.replace.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderStatus;
import com.lxzl.erp.common.constant.ReturnOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.replace.ReplaceOrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\5 0005 10:08
 */
@Service
public class ReplaceOrderServiceImpl implements ReplaceOrderService{
    private static final Logger logger = LoggerFactory.getLogger(ReplaceOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(ReplaceOrder replaceOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //校验客户编号
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(replaceOrder.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //校验换货项是否为空
        List<ReplaceOrderProduct> replaceOrderDetailList = replaceOrder.getReplaceOrderProductList();
        List<ReplaceOrderMaterial> replaceOrderMaterialList = replaceOrder.getReplaceOrderMaterialList();
        if (CollectionUtil.isEmpty(replaceOrderDetailList) && CollectionUtil.isEmpty(replaceOrderMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_DETAIL_LIST_NOT_NULL);
            return serviceResult;
        }
        //校验订单编号
        OrderDO orderDO = orderMapper.findByOrderNo(replaceOrder.getOrderNo());
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        //只有确认收货和部分归还状态的才可以换货
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //校验换货时间
        Date replaceTime = replaceOrder.getReplaceTime();
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String replaceTimeString = simpleDateFormat.format(replaceTime);
        String rentStartTimeString = simpleDateFormat.format(rentStartTime);
        String expectReturnTimeString = simpleDateFormat.format(expectReturnTime);
        try {
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date rentStartTimeDate = simpleDateFormat.parse(rentStartTimeString);
            Date expectReturnTimeDate = simpleDateFormat.parse(expectReturnTimeString);
            if (!(replaceTimeDate.compareTo(rentStartTimeDate)>0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_AFTER_RENT_START_TIME);
                return serviceResult;
            } else if (!(rentStartTimeDate.compareTo(expectReturnTimeDate)<0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_BEFORE_EXPECT_RETURN_TIME);
                return serviceResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验换货时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return serviceResult;
        }


        //校验换货数量

        //获取换货数量
        Map<Integer, Integer> replaceProductCountMap = new HashMap<>();
        Map<Integer, Integer> replaceMaterialCountMap = new HashMap<>();
        for (ReplaceOrderProduct replaceOrderProduct : replaceOrderDetailList) {
            replaceProductCountMap.put(replaceOrderProduct.getOldOrderProductId(), replaceOrderProduct.getProductCount());
        }
        for (ReplaceOrderMaterial replaceOrderMaterial : replaceOrderMaterialList) {
            replaceMaterialCountMap.put(replaceOrderMaterial.getOldOrderMaterialId(), replaceOrderMaterial.getMaterialCount());
        }
        //获取在租数量
        Map<Integer, Integer> rentingProductCountMap = new HashMap<>();
        Map<Integer, Integer> rentingMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                rentingProductCountMap.put(orderProductDO.getId(), orderProductDO.getRentingProductCount());
            }
        }
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                rentingMaterialCountMap.put(orderMaterialDO.getId(), orderMaterialDO.getRentingMaterialCount());
            }
        }
        //获取该用户未完成的退货
        Map<Integer, Integer> productCountMap = new HashMap<>();
        Map<Integer, Integer> materialCountMap = new HashMap<>();
        List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findByCustomerNo(orderDO.getBuyerCustomerNo());
        //获取该用户处于待提交、审核中、处理中三种状态的商品或者配件的数量
        getReturnCount(productCountMap, materialCountMap, k3ReturnOrderDOList);
        //获取该订单的换货单
//        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findByOrderNo(replaceOrder.getOrderNo());
        // TODO: 2018\9\6 0006 将该订单的待提交、审核中、处理中三种状态的换货单商品或配件数量保存
        //比较设备项
//        for (ReplaceOrderProduct replaceOrderProduct : replaceOrderDetailList) {
//            Integer rentingProductCount = rentingProductCountMap.get(orderProduct.getOrderProductId()) == null ? 0 : rentingProductCountMap.get(orderProduct.getOrderProductId());//在租数
//            Integer processProductCount = productCountMap.get(orderProduct.getOrderProductId()) == null ? 0 : productCountMap.get(orderProduct.getOrderProductId()); //待提交、处理中和审核中数量
//            Integer canReturnProductCount = rentingProductCount - processProductCount;
//            if (canReturnProductCount < 0) {
//                canReturnProductCount = 0;
//            }
//            orderProduct.setCanReturnProductCount(canReturnProductCount);
//        }
//        //比较物料项
//        for (ReplaceOrderMaterial replaceOrderMaterial : replaceOrderMaterialList) {
//            Integer rentingMaterialCount = rentingMaterialCountMap.get(orderMaterial.getOrderMaterialId()) == null ? 0 : rentingMaterialCountMap.get(orderMaterial.getOrderMaterialId());//在租数
//            Integer processMaterialCount = materialCountMap.get(orderMaterial.getOrderMaterialId()) == null ? 0 : materialCountMap.get(orderMaterial.getOrderMaterialId()); //待提交、处理中和审核中数量
//            Integer canReturnMaterialCount = rentingMaterialCount - processMaterialCount;
//            if (canReturnMaterialCount < 0) {
//                canReturnMaterialCount = 0;
//            }
//            orderMaterial.setCanReturnMaterialCount(canReturnMaterialCount);
//        }

        return null;
    }
    /**
     * 获取该用户处于待提交、审核中、处理中三种状态的商品或者配件的数量
     *
     * @param productCountMap
     * @param materialCountMap
     * @param k3ReturnOrderDOList
     */
    private void getReturnCount(Map<Integer, Integer> productCountMap, Map<Integer, Integer> materialCountMap, List<K3ReturnOrderDO> k3ReturnOrderDOList) {
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
            for (K3ReturnOrderDO dBK3ReturnOrderDO : k3ReturnOrderDOList) {
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())
                        || ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())
                        || ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(dBK3ReturnOrderDO.getReturnOrderStatus())) {
                    List<K3ReturnOrderDetailDO> dBK3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByReturnOrderId(dBK3ReturnOrderDO.getId());
                    //获取商品和配件的退货数量和存入集合中
                    getProductAndMaterialReturnCount(productCountMap, materialCountMap, dBK3ReturnOrderDetailDOList);
                }
            }
        }
    }
    /**
     * 获取商品和配件的退货数量和存入集合中
     *
     * @param productCountMap
     * @param materialCountMap
     * @param dBK3ReturnOrderDetailDOList
     */
    private void getProductAndMaterialReturnCount(Map<Integer, Integer> productCountMap, Map<Integer, Integer> materialCountMap, List<K3ReturnOrderDetailDO> dBK3ReturnOrderDetailDOList) {
        Integer materialId;
        Integer productId;
        for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : dBK3ReturnOrderDetailDOList) {
            String productNo = k3ReturnOrderDetailDO.getProductNo();
            if (productSupport.isMaterial(productNo)) {
                //物料
                materialId = productSupport.getMaterialId(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                if (materialCountMap.get(materialId) == null) {
                    materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount());
                } else {
                    materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount() + materialCountMap.get(materialId));
                }

            } else {
                //设备
                productId = productSupport.getProductId(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                if (productCountMap.get(productId) == null) {
                    productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount());
                } else {
                    productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount() + productCountMap.get(productId));
                }
            }
        }
    }
    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;
    @Autowired
    private ProductSupport productSupport;
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
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

}
