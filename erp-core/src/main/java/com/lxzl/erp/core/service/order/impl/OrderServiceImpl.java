package com.lxzl.erp.core.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryByCustomerNoParam;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryParam;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointMaterial;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProductProduct;
import com.lxzl.erp.common.domain.k3.pojo.OrderMessage;
import com.lxzl.erp.common.domain.k3.pojo.OrderStatementDateSplit;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.ERPServiceLocator;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.IERPService;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.coupon.impl.support.CouponSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.material.impl.support.MaterialSupport;
import com.lxzl.erp.core.service.messagethirdchannel.MessageThirdChannelService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.OrderStatementDateSplitMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.orderOperationLog.OrderOperationLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductProductDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.k3.OrderStatementDateSplitDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.orderOperationLog.OrderOperationLogDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.hadoop.mapred.IFile;
import org.apache.velocity.runtime.directive.Foreach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);

        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);


        SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
        if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }
        orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());

        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(orderDO.getOrderSubCompanyId());
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(generateNoSupport.generateOrderNo(currentTime, orderSubCompanyDO != null ? orderSubCompanyDO.getSubCompanyCode() : null));
        orderDO.setOrderSellerId(customerDO.getOwner());

        //添加客户的结算时间（天）
//        Date rentStartTime = order.getRentStartTime();

        //获取
        orderDO.setStatementDate(customerDO.getStatementDate());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setCreateUser(loginUser.getUserId().toString());
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setCreateTime(currentTime);
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.save(orderDO);

        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        //为了不影响之前的订单逻辑，这里暂时使用修改的方式
        setOrderProductSummary(orderDO);
        orderMapper.update(orderDO);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(), OperationType.CREATE_ORDER);
        // TODO: 2018\4\26 0026 使用优惠券
        if (CollectionUtil.isEmpty(order.getCouponList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(orderDO.getOrderNo());
            return result;
        }
        String rs = couponSupport.useCoupon(order);
        if (!ErrorCode.SUCCESS.equals(rs)) {
            result.setErrorCode(rs);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createOrderNew(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        /***** 增加的组合商品逻辑 start*******/
        // 预处理订单中的组合商品项
        preValidateOrderJointProduct(order);
        /***** 增加的组合商品逻辑 end*******/

        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);

        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);


        SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
        if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }
        orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());

        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(orderDO.getOrderSubCompanyId());
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(generateNoSupport.generateOrderNo(currentTime, orderSubCompanyDO != null ? orderSubCompanyDO.getSubCompanyCode() : null));
        orderDO.setOrderSellerId(customerDO.getOwner());

        //添加客户的结算时间（天）
//        Date rentStartTime = order.getRentStartTime();

        //获取
        orderDO.setStatementDate(customerDO.getStatementDate());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setCreateUser(loginUser.getUserId().toString());
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setCreateTime(currentTime);
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.save(orderDO);

        /***** 增加的组合商品逻辑 start*******/
        saveOrderJointProductInfo(orderDO.getOrderJointProductDOList(), orderDO, loginUser, currentTime);
        /***** 增加的组合商品逻辑 end*******/
        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        //为了不影响之前的订单逻辑，这里暂时使用修改的方式
        setOrderProductSummary(orderDO);
        orderMapper.update(orderDO);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.CREATE_ORDER);
        // TODO: 2018\4\26 0026 使用优惠券
        if (CollectionUtil.isEmpty(order.getCouponList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(orderDO.getOrderNo());
            return result;
        }
        String rs = couponSupport.useCoupon(order);
        if (!ErrorCode.SUCCESS.equals(rs)) {
            result.setErrorCode(rs);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    // 预处理订单中的组合商品项
    // 1. 校验订单组合商品列表是否和组合商品列表商品和配件id一致
    // 2. 重新设置组合商品中的商品数和配件数（商品数 = 订单组合商品数 * 组合商品定义的商品数）（配件数 = 订单组合商品配件数）
    // 3. 将每个组合商品中的商品配件增加唯一序号（不会存储），以便在保存完每一个组合商品后，根据组合商品中的商品和配件列表的序号找到订单商品配件中对应的商品，并设置组合商品项id
    // 4. 将组合商品中的商品和配件放入到订单商品和配件中
    private void preValidateOrderJointProduct(Order order) {
        verifyOrderJointProduct(order.getOrderJointProductList());
        setCountForOrderJointProduct(order.getOrderJointProductList());
        buildOrderJointProduct(order);
    }

    // 3. 将每个组合商品中的商品配件增加唯一序号（不会存储），以便在保存完每一个组合商品后，根据组合商品中的商品和配件列表的序号找到订单商品配件中对应的商品，并设置组合商品项id
    // 4. 将组合商品中的商品和配件放入到订单商品和配件中
    private void buildOrderJointProduct(Order order) {
        List<OrderProduct> orderProductList = order.getOrderProductList();
        if (CollectionUtil.isEmpty(orderProductList)) {
            orderProductList = new ArrayList<>();
            order.setOrderProductList(orderProductList);
        }
        List<OrderMaterial> orderMaterialList = order.getOrderMaterialList();
        if (CollectionUtil.isEmpty(orderMaterialList)) {
            orderMaterialList = new ArrayList<>();
            order.setOrderMaterialList(orderMaterialList);
        }

        List<OrderJointProduct> orderJointProductList = order.getOrderJointProductList();

        // 将组合商品列表中的商品和配件放到订单的订单商品项和订单配件项中，并为‘新增的’组合商品中的配件和商品添加IdentityNo序号
        Integer identityNo = 1;
        if (CollectionUtil.isNotEmpty(orderJointProductList)) {
            for (OrderJointProduct orderJointProduct : orderJointProductList) {
                List<OrderProduct> orderProductListTmp = orderJointProduct.getOrderProductList();
                if (CollectionUtil.isNotEmpty(orderProductListTmp)) {
                    for (OrderProduct orderProduct : orderProductListTmp) {
                        orderProductList.add(orderProduct); // 添加到订单项中
                        orderProduct.setIdentityNo(identityNo++);
                    }
                }
                List<OrderMaterial> orderMaterialListTmp = orderJointProduct.getOrderMaterialList();
                if (CollectionUtil.isNotEmpty(orderMaterialListTmp)) {
                    for (OrderMaterial orderMaterial : orderMaterialListTmp) {
                        orderMaterialList.add(orderMaterial); // 添加到订单项中
                        orderMaterial.setIdentityNo(identityNo++);
                    }
                }
            }
        }
    }

    // 订单组合商品参数验证
    private String verifyOrderJointProduct(List<OrderJointProduct> orderJointProductList) {
        if (CollectionUtil.isNotEmpty(orderJointProductList)) {
            for (OrderJointProduct orderJointProduct : orderJointProductList) {
                Integer jointProductId = orderJointProduct.getJointProductId();
                JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProductId);
                if (jointProductDO == null) {
                    throw new BusinessException(ErrorCode.JOINT_PRODUCT_ID_IS_NULL);
                }

                // 校验订单组合商品中商品项和配件项是否跟组合商品一致
                List<OrderProduct> orderProductList = orderJointProduct.getOrderProductList();
                List<JointProductProductDO> jointProductProductDOList = jointProductDO.getJointProductProductDOList();
                List<Integer> productIdList = new ArrayList<>();
                List<Integer> dbProductIdList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(orderProductList)) {
                    for (OrderProduct orderProduct : orderProductList) {
                        productIdList.add(orderProduct.getProductId());
                    }
                }
                if (CollectionUtil.isNotEmpty(jointProductProductDOList)) {
                    for (JointProductProductDO jointProductProductDO : jointProductProductDOList) {
                        dbProductIdList.add(jointProductProductDO.getProductId());
                    }
                }
                if (!ListUtil.equalIntegerList(productIdList, dbProductIdList)) {
                    throw new BusinessException(ErrorCode.ORDER_JOINT_PRODUCT_PRODUCT_ERROR);
                }

                // 订单组合商品配件必须是组合商品配件的子集
                List<OrderMaterial> orderMaterialList = orderJointProduct.getOrderMaterialList();
                List<JointMaterialDO> jointMaterialDOList = jointProductDO.getJointMaterialDOList();
                List<Integer> materialIdList = new ArrayList<>();
                List<Integer> dbMaterailIdList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(orderMaterialList)) {
                    for (OrderMaterial orderMaterial : orderMaterialList) {
                        materialIdList.add(orderMaterial.getMaterialId());
                    }
                }
                if (CollectionUtil.isNotEmpty(jointMaterialDOList)) {
                    for (JointMaterialDO jointMaterialDO : jointMaterialDOList) {
                        dbMaterailIdList.add(jointMaterialDO.getMaterialId());
                    }
                }
                if (!dbMaterailIdList.containsAll(materialIdList)) {
                    throw new BusinessException(ErrorCode.ORDER_JOINT_PRODUCT_MATERIAL_ERROR);
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 订单组合商品数量设置
    private String setCountForOrderJointProduct(List<OrderJointProduct> orderJointProductList) {
        if (CollectionUtil.isNotEmpty(orderJointProductList)) {
            for (OrderJointProduct orderJointProduct : orderJointProductList) {
                Integer jointProductId = orderJointProduct.getJointProductId();
                JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProductId);
                if (jointProductDO == null) {
                    throw new BusinessException(ErrorCode.JOINT_PRODUCT_ID_IS_NULL);
                }

                // 校验订单组合商品中商品项和配件项是否跟组合商品一致
                List<OrderProduct> orderProductList = orderJointProduct.getOrderProductList();
                List<JointProductProductDO> jointProductProductDOList = jointProductDO.getJointProductProductDOList();

                // 重新设置组合商品中商品数量
                // 商品数量 = 订单组合商品数 * 组合商品定义时商品数量
                Integer jointCount = orderJointProduct.getJointProductCount();
                if (CollectionUtil.isNotEmpty(orderProductList) && CollectionUtil.isNotEmpty(jointProductProductDOList)) {
                    Map<Integer, JointProductProductDO> jointProductProductDOMap = ListUtil.listToMap(jointProductProductDOList, "productId");
                    for (OrderProduct orderProduct : orderProductList) {
                        JointProductProductDO jointProductProductDO = jointProductProductDOMap.get(orderProduct.getProductId());
                        orderProduct.setProductCount(jointProductProductDO.getProductCount() * jointCount);
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    private void setOrderProductSummary(OrderDO orderDO) {
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            orderDO.setProductSummary(orderProductDOList.get(0).getProductName());
        } else if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            orderDO.setProductSummary(orderMaterialDOList.get(0).getMaterialName());
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO dbOrderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbOrderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(dbOrderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!loginUser.getUserId().toString().equals(dbOrderDO.getCreateUser())) {
            result.setErrorCode(ErrorCode.DATA_NOT_BELONG_TO_YOU);
            return result;
        }

        List<OrderProductDO> orderProductDOList = ConverterUtil.convertList(order.getOrderProductList(), OrderProductDO.class);
        List<OrderMaterialDO> orderMaterialDOList = ConverterUtil.convertList(order.getOrderMaterialList(), OrderMaterialDO.class);
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
        orderDO.setOrderProductDOList(orderProductDOList);
        orderDO.setOrderMaterialDOList(orderMaterialDOList);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
        if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }
        orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setId(dbOrderDO.getId());
        orderDO.setOrderNo(dbOrderDO.getOrderNo());
        orderDO.setOrderSellerId(customerDO.getOwner());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        //添加客户的结算时间（天）
//        Date rentStartTime = order.getRentStartTime();
        orderDO.setStatementDate(customerDO.getStatementDate());

        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.update(orderDO);

        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        //为了不影响之前的订单逻辑，这里暂时使用修改的方式
        setOrderProductSummary(orderDO);
        orderMapper.update(orderDO);

        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.UPDATE_ORDER);

        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);
        // TODO: 2018\4\26 0026  清除之前订单锁定的优惠券
        String revertresult = couponSupport.revertCoupon(order.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(revertresult)) {
            result.setErrorCode(revertresult);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        // TODO: 2018\4\26 0026  重新使用优惠券
        if (CollectionUtil.isEmpty(order.getCouponList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(orderDO.getOrderNo());
            return result;
        }
        String rs = couponSupport.useCoupon(order);
        if (!ErrorCode.SUCCESS.equals(rs)) {
            result.setErrorCode(rs);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateOrderNew(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        /***** 增加的组合商品逻辑 start*******/
        // 预处理订单中的组合商品项
        preValidateOrderJointProduct(order);
        /***** 增加的组合商品逻辑 end*******/

        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO dbOrderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbOrderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(dbOrderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!loginUser.getUserId().toString().equals(dbOrderDO.getCreateUser())) {
            result.setErrorCode(ErrorCode.DATA_NOT_BELONG_TO_YOU);
            return result;
        }

        List<OrderProductDO> orderProductDOList = ConverterUtil.convertList(order.getOrderProductList(), OrderProductDO.class);
        List<OrderMaterialDO> orderMaterialDOList = ConverterUtil.convertList(order.getOrderMaterialList(), OrderMaterialDO.class);
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
        orderDO.setOrderProductDOList(orderProductDOList);
        orderDO.setOrderMaterialDOList(orderMaterialDOList);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
        if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }
        orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setId(dbOrderDO.getId());
        orderDO.setOrderNo(dbOrderDO.getOrderNo());
        orderDO.setOrderSellerId(customerDO.getOwner());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        //添加客户的结算时间（天）
//        Date rentStartTime = order.getRentStartTime();
        orderDO.setStatementDate(customerDO.getStatementDate());

        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.update(orderDO);

        /***** 增加的组合商品逻辑 start*******/
        saveOrderJointProductInfo(orderDO.getOrderJointProductDOList(), orderDO, loginUser, currentTime);
        /***** 增加的组合商品逻辑 end*******/
        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        //为了不影响之前的订单逻辑，这里暂时使用修改的方式
        setOrderProductSummary(orderDO);
        orderMapper.update(orderDO);
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.UPDATE_ORDER);

        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);
        // TODO: 2018\4\26 0026  清除之前订单锁定的优惠券
        String revertresult = couponSupport.revertCoupon(order.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(revertresult)) {
            result.setErrorCode(revertresult);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        // TODO: 2018\4\26 0026  重新使用优惠券
        if (CollectionUtil.isEmpty(order.getCouponList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(orderDO.getOrderNo());
            return result;
        }
        String rs = couponSupport.useCoupon(order);
        if (!ErrorCode.SUCCESS.equals(rs)) {
            result.setErrorCode(rs);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitOrder(OrderCommitParam orderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        String orderNo = orderCommitParam.getOrderNo();
        Integer verifyUser = orderCommitParam.getVerifyUser();
        String commitRemark = orderCommitParam.getCommitRemark();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (CollectionUtil.isEmpty(orderDO.getOrderProductDOList())
                && CollectionUtil.isEmpty(orderDO.getOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }

        //只有创建订单本人可以提交
        if (!orderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(orderDO.getBuyerCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, orderDO);
        if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
            result.setErrorCode(verifyOrderShortRentReceivableResult);
            return result;
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            int oldProductCount = 0, newProductCount = 0;
            Map<Integer, Integer> productNewStockMap = new HashMap<>();
            Map<Integer, Integer> productOldStockMap = new HashMap<>();
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Product product = FastJsonUtil.toBean(orderProductDO.getProductSkuSnapshot(), Product.class);
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NOT_RENT);
                    return result;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                if (productNewStockMap.get(product.getProductId()) == null) {
                    productNewStockMap.put(product.getProductId(), product.getNewProductCount());
                }
                if (productOldStockMap.get(product.getProductId()) == null) {
                    productOldStockMap.put(product.getProductId(), product.getOldProductCount());
                }
                oldProductCount = productOldStockMap.get(product.getProductId());
                newProductCount = productNewStockMap.get(product.getProductId());

                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                    if ((newProductCount - orderProductDO.getProductCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_PRODUCT_STOCK_NEW_INSUFFICIENT);
                        return result;
                    } else {
                        newProductCount = newProductCount - orderProductDO.getProductCount();
                        productNewStockMap.put(product.getProductId(), newProductCount);
                    }
                } else {
                    if ((oldProductCount - orderProductDO.getProductCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_PRODUCT_STOCK_OLD_INSUFFICIENT);
                        return result;
                    } else {
                        oldProductCount = oldProductCount - orderProductDO.getProductCount();
                        productOldStockMap.put(product.getProductId(), oldProductCount);
                    }
                }*/
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                Material material = FastJsonUtil.toBean(orderMaterialDO.getMaterialSnapshot(), Material.class);
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                    if (material == null || material.getNewMaterialCount() == null || material.getNewMaterialCount() <= 0 || (material.getNewMaterialCount() - orderMaterialDO.getMaterialCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_MATERIAL_STOCK_NEW_INSUFFICIENT);
                        return result;
                    }
                } else {
                    if (material == null || material.getOldMaterialCount() == null || material.getOldMaterialCount() <= 0 || (material.getOldMaterialCount() - orderMaterialDO.getMaterialCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_MATERIAL_STOCK_OLD_INSUFFICIENT);
                        return result;
                    }
                }*/
            }
        }

        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        if (customerRiskManagementDO == null && BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return result;
        }
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return result;
        }

        ServiceResult<String, Boolean> isNeedVerifyResult = isNeedSecondVerify(orderNo);
        if (!ErrorCode.SUCCESS.equals(isNeedVerifyResult.getErrorCode())) {
            result.setErrorCode(isNeedVerifyResult.getErrorCode(), isNeedVerifyResult.getFormatArgs());
            return result;
        }
        // 是否需要二次审批
        boolean isNeedSecondVerify = isNeedVerifyResult.getResult();

        String orderRemark = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            orderRemark = "租赁类型：天租";
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
            orderRemark = "租赁类型：月租";
        }
        String verifyMatters = null;
        if (isNeedSecondVerify) {
            //如果要二次审核，判断审核注意事项
            ServiceResult<String, String> verifyMattersResult = getVerifyMatters(orderDO);
            if (!ErrorCode.SUCCESS.equals(verifyMattersResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(verifyMattersResult.getErrorCode());
                return result;
            }
            verifyMatters = verifyMattersResult.getResult();
        } else {
            verifyMatters = "例行审核";
        }
        ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo(), verifyUser, verifyMatters, commitRemark, orderCommitParam.getImgIdList(), orderRemark);
        if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(workflowCommitResult.getErrorCode());
            return result;
        }

        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.COMMIT_ORDER);

        // 扣除信用额度
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
            customerSupport.addCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
        }
//        if (!isNeedSecondVerify) {
//            String code = receiveVerifyResult(true, orderDO.getOrderNo());
//            if (!ErrorCode.SUCCESS.equals(code)) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                result.setErrorCode(ErrorCode.SYSTEM_EXCEPTION);
//                return result;
//            }
//        }
        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> payOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(String orderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedSecondVerify(String orderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())&&
                !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }

        Boolean isNeedVerify = false;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    result.setErrorCode(productServiceResult.getErrorCode());
                    return result;
                }
                Product product = productServiceResult.getResult();
                if (product == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }

                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? thisProductSku.getNewDayRentPrice() : thisProductSku.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? thisProductSku.getNewMonthRentPrice() : thisProductSku.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    result.setErrorCode(materialServiceResult.getErrorCode());
                    return result;
                }
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }

                BigDecimal materialUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewDayRentPrice() : material.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMonthRentPrice() : material.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), materialUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        // 检查是否需要审批流程
        if (isNeedVerify) {
            ServiceResult<String, Boolean> isMeedVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_ORDER_INFO);
            if (!ErrorCode.SUCCESS.equals(isMeedVerifyResult.getErrorCode())) {
                result.setErrorCode(isMeedVerifyResult.getErrorCode());
                return result;
            }
            if (!isMeedVerifyResult.getResult()) {
                result.setErrorCode(ErrorCode.WORKFLOW_HAVE_NO_CONFIG);
                return result;
            }
        }

        result.setResult(isNeedVerify);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 确认收货时发生退货修改订单并推送K3保存更改记录
     * @Author : sunzhipeng
     * @param orderConfirmChangeParam
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> confirmChangeOrder(OrderConfirmChangeParam orderConfirmChangeParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        // TODO: 2018\5\22 0022   1.保存订单确认收货变更记录详情
        OrderDO dborderDO = orderMapper.findByOrderNo(orderConfirmChangeParam.getOrderNo());
        if (dborderDO == null) {
            logger.info("未查询到交易订单{}相关信息", dborderDO);
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        // 判断订单状态，如果状态不是已发货状态不能进行确认收货功能
        Integer orderState = dborderDO.getOrderStatus();
        if (orderState == null || !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderState)) {
            logger.error("交易订单{}状态为{}，不能确认收货", dborderDO.getOrderNo(), orderState);
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        //调用公共方法进行出处理
        ServiceResult<String, String> serviceResult = changeOrderMethod(orderConfirmChangeParam, result, dborderDO);

        return serviceResult;
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String,Integer> updateOrderPrice(Order order){
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        if (StringUtil.isEmpty(order.getOrderNo())) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (orderDO == null){
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        String strOperationBefore;
        String strOperationAfter;
        BigDecimal totalUnitAmount = BigDecimal.ZERO;
        List<OrderOperationLogDO> orderOperationLogDOList = new ArrayList<>();
        List<OrderProductDO> orderProductDOList = new ArrayList<>();
        List<OrderMaterialDO> orderMaterialDOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(order.getOrderProductList())){
            for (OrderProduct orderProduct: order.getOrderProductList()){
                if (orderProduct.getOrderProductId() == null || orderProduct.getProductUnitAmount() == null){
                    continue;
                }
                OrderProductDO orderProductDO = getOrderProductDOById(orderDO, orderProduct.getOrderProductId());
                totalUnitAmount = BigDecimalUtil.add(orderProduct.getProductUnitAmount(), totalUnitAmount);//记录商品单价
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), orderProduct.getProductUnitAmount()) == 0){
                    continue;
                }
                strOperationBefore = "商品项id:" + orderProduct.getOrderProductId() + "，商品单价：" + orderProductDO.getProductUnitAmount()+ "。";
                strOperationAfter = "商品项id:" + orderProduct.getOrderProductId() + "，商品单价：" + orderProduct.getProductUnitAmount()+ "。";
                OrderOperationLogDO orderOperationLogDO = new OrderOperationLogDO();
                orderOperationLogDO.setOrderNo(orderDO.getOrderNo());
                orderOperationLogDO.setOrderStatusBefore(orderDO.getOrderStatus());
                orderOperationLogDO.setOrderStatusAfter(orderDO.getOrderStatus());
                orderOperationLogDO.setBusinessType(OrderBusinessType.ORDER_OPERATION_UPD_GOODS_PRICE);
                orderOperationLogDO.setCreateTime(currentTime);
                orderOperationLogDO.setCreateUser(loginUser.getUserId().toString());
                orderOperationLogDO.setOperationBefore(strOperationBefore);
                orderOperationLogDO.setOperationAfter(strOperationAfter);
                orderOperationLogDOList.add(orderOperationLogDO);
                //保存
                orderProductDO.setProductUnitAmount(orderProduct.getProductUnitAmount());
                BigDecimal productAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getRentTimeLength()), 2), new BigDecimal(orderProductDO.getProductCount()));
                orderProductDO.setProductAmount(productAmount);
                BigDecimal rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount()), 2), new BigDecimal(orderProductDO.getDepositCycle()));
                orderProductDO.setRentDepositAmount(rentDepositAmount);
                orderProductDOList.add(orderProductDO);
            }
            if (orderProductDOList.size() > 0){
                //批量更新商品项单价和商品总价
                orderMapper.updateOrderProductPriceList(orderProductDOList);
                orderMapper.updateOrderTotalProductAmount(orderDO.getOrderNo());
            }
        }
        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())){
            for (OrderMaterial orderMaterial: order.getOrderMaterialList()){
                if (orderMaterial.getOrderMaterialId() == null || orderMaterial.getMaterialUnitAmount() == null){
                    continue;
                }
                OrderMaterialDO orderMaterialDO = getOrderMaterialDOById(orderDO, orderMaterial.getOrderMaterialId());
                totalUnitAmount = BigDecimalUtil.add(orderMaterial.getMaterialUnitAmount(), totalUnitAmount);//记录配件单价
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), orderMaterial.getMaterialUnitAmount()) == 0){
                    continue;
                }
                strOperationBefore = "配件项id:" + orderMaterial.getOrderMaterialId() + "，配件单价：" + orderMaterialDO.getMaterialUnitAmount()+ "。";
                strOperationAfter = "配件项id:" + orderMaterial.getOrderMaterialId() + "，配件单价：" + orderMaterial.getMaterialUnitAmount()+ "。";
                OrderOperationLogDO orderOperationLogDO = new OrderOperationLogDO();
                orderOperationLogDO.setOrderNo(orderDO.getOrderNo());
                orderOperationLogDO.setOrderStatusBefore(orderDO.getOrderStatus());
                orderOperationLogDO.setOrderStatusAfter(orderDO.getOrderStatus());
                orderOperationLogDO.setBusinessType(OrderBusinessType.ORDER_OPERATION_UPD_GOODS_PRICE);
                orderOperationLogDO.setCreateTime(currentTime);
                orderOperationLogDO.setCreateUser(loginUser.getUserId().toString());
                orderOperationLogDO.setOperationBefore(strOperationBefore);
                orderOperationLogDO.setOperationAfter(strOperationAfter);
                orderOperationLogDOList.add(orderOperationLogDO);
                //保存
                orderMaterialDO.setMaterialUnitAmount(orderMaterial.getMaterialUnitAmount());
                BigDecimal materialAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getRentTimeLength()), 2), new BigDecimal(orderMaterialDO.getMaterialCount()));
                orderMaterialDO.setMaterialAmount(materialAmount);
                BigDecimal rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2), new BigDecimal(orderMaterialDO.getDepositCycle()));
                orderMaterialDO.setRentDepositAmount(rentDepositAmount);
                orderMaterialDOList.add(orderMaterialDO);
            }
            if (orderMaterialDOList.size() > 0){
                //批量更新配件项单价和配件总价
                orderMapper.updateOrderMaterialPriceList(orderMaterialDOList);
                orderMapper.updateOrderTotalMaterialAmount(orderDO.getOrderNo());
            }
        }
        if (orderOperationLogDOList.size() > 0){
            //更新订单总价
            orderMapper.updateOrderTotalOrderAmount(orderDO.getOrderNo());
            //若订单配件和商品单价之和为零，更新订单首付总额为零
            if (BigDecimalUtil.compare(totalUnitAmount, BigDecimal.ZERO) == 0){
                orderMapper.updateOrderFirstNeedPayAmount(orderDO.getOrderNo());
            }
            //重算订单
            ServiceResult<String, BigDecimal> serviceResult = statementService.reCreateOrderStatement(orderDO.getOrderNo(), orderDO.getStatementDate());
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                result.setErrorCode(serviceResult.getErrorCode());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return result;
            }
            //日志
            orderOperationLogMapper.saveList(orderOperationLogDOList);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderOperationLogDOList.size());
        return result;
    }

    /**
     * 通过商品项id查找 订单DO中的商品项信息
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     */
    private OrderProductDO getOrderProductDOById(OrderDO order, Integer id) {

        if (CollectionUtil.isNotEmpty(order.getOrderProductDOList())) {

            for (OrderProductDO orderProduct : order.getOrderProductDOList()) {
                if (orderProduct.getId().equals(id)) {
                    return orderProduct;
                }
            }
        }
        return null;
    }

    /**
     * 通过配件项id查找 订单中的配件项信息
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     */
    private OrderMaterialDO getOrderMaterialDOById(OrderDO order, Integer id) {

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialDOList())) {

            for (OrderMaterialDO orderMaterial : order.getOrderMaterialDOList()) {
                if (orderMaterial.getId().equals(id)) {
                    return orderMaterial;
                }
            }
        }
        return null;
    }

    /**
     * 超级管理员修改订单
     * @Author : sunzhipeng
     * @param orderConfirmChangeParam
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> supperUserChangeOrder(OrderConfirmChangeParam orderConfirmChangeParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        // TODO: 2018\5\22 0022   1.保存订单确认收货变更记录详情
        OrderDO dborderDO = orderMapper.findByOrderNo(orderConfirmChangeParam.getOrderNo());
        if (dborderDO == null) {
            logger.info("未查询到交易订单{}相关信息", dborderDO);
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        // 拥有权限的人员进行修改进行判断，状态为不是已发货或者确认收货的不能修改
        Integer orderState = dborderDO.getOrderStatus();
        if (orderState == null || (!OrderStatus.ORDER_STATUS_DELIVERED.equals(orderState) && !OrderStatus.ORDER_STATUS_CONFIRM.equals(orderState))) {
            logger.error("交易订单{}状态为{}，不能进行修改", dborderDO.getOrderNo(), orderState);
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        //调用公共方法进行出处理
        ServiceResult<String, String> serviceResult = changeOrderMethod(orderConfirmChangeParam, result, dborderDO);

        return serviceResult;
    }

    /**
     * 修改订单的公共方法
     * @Author : sunzhipeng
     * @param orderConfirmChangeParam
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> changeOrderMethod(OrderConfirmChangeParam orderConfirmChangeParam,ServiceResult<String, String> result,OrderDO orderDO){
        Date date = new Date();
        List<OrderItemParam> orderItemParamList = orderConfirmChangeParam.getOrderItemParamList();

        if (CollectionUtil.isEmpty(orderItemParamList)) {
            result.setErrorCode(ErrorCode.ORDER_ITEM_PARAM_LIST_NOT_NULL);
            return result;
        }
        Integer oldTotalProductCount = orderDO.getTotalProductCount();
        Integer oldTotalMaterialCount = orderDO.getTotalMaterialCount();
        OrderConfirmChangeToK3Param orderConfirmChangeToK3Param = new OrderConfirmChangeToK3Param();
        List<ChangeOrderItemParam> changeOrderItemParamList = new ArrayList<>();
        orderConfirmChangeToK3Param.setOrderId(orderDO.getId());
        orderConfirmChangeToK3Param.setOrderNo(orderDO.getOrderNo());
        // TODO: 2018\5\23 0023 按天计算的单子押金退还需要单独一个逻辑来进行退还
        StringBuffer sb = new StringBuffer();
        sb.append("您的客户[").append(orderDO.getBuyerCustomerName()).append("]所下租赁订单（订单号：").append(orderDO.getOrderNo()).append("）已经部分确认收货，内容如下：\n");
        StringBuffer confirmsb = new StringBuffer();
        StringBuffer returnsb = new StringBuffer();
        Integer count = 0;
        for (OrderItemParam orderItemParam:orderItemParamList) {
            count+=orderItemParam.getItemCount();
            //商品变化保存订单确认收货变更记录详情信息
            if (orderItemParam.getItemType()==1) {
                OrderConfirmChangeLogDetailDO orderConfirmChangeLogDetailDO = new OrderConfirmChangeLogDetailDO();
                for (OrderProductDO orderProductDO:orderDO.getOrderProductDOList()) {
                    if (orderProductDO.getId().equals(orderItemParam.getItemId())) {
                        if (orderItemParam.getItemCount()>orderProductDO.getStableProductCount()) {
                            result.setErrorCode(ErrorCode.ITEM_COUNT_MORE_THAN_STABLE_PRODUCT_COUNT);
                            return result;
                        }
                        //判断收货的商品是否跟原订单商品数不一致，不一致的进行记录并保存收货的数量
                        if (!orderProductDO.getProductCount().equals(orderItemParam.getItemCount())) {
                            if (orderProductDO.getProductCount() == 0) {
                                result.setErrorCode(ErrorCode.ORDER_PRODUCT_COUNT_IS_ZERO_NOT_CONFIRM);
                                return result;
                            }
                            //设置传递K3参数
                            ChangeOrderItemParam changeOrderItemParam = new ChangeOrderItemParam();
                            changeOrderItemParam.setItemId(orderItemParam.getItemId());
                            changeOrderItemParam.setItemType(orderItemParam.getItemType());
                            changeOrderItemParam.setReturnCount(orderProductDO.getStableProductCount()-orderItemParam.getItemCount());
                            changeOrderItemParamList.add(changeOrderItemParam);

                            orderConfirmChangeLogDetailDO.setOrderId(orderDO.getId());
                            orderConfirmChangeLogDetailDO.setOrderNo(orderDO.getOrderNo());
                            orderConfirmChangeLogDetailDO.setItemType(OrderItemType.ORDER_ITEM_TYPE_PRODUCT);
                            orderConfirmChangeLogDetailDO.setItemId(orderItemParam.getItemId());
                            orderConfirmChangeLogDetailDO.setOrderItemCount(orderProductDO.getStableProductCount());//订单初始商品/配件数
                            orderConfirmChangeLogDetailDO.setOldItemCount(orderProductDO.getProductCount());//原商品/配件数
                            orderConfirmChangeLogDetailDO.setNewItemCount(orderItemParam.getItemCount());//新商品/配件数
                            orderConfirmChangeLogDetailDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                            orderConfirmChangeLogDetailDO.setRemark(null);
                            orderConfirmChangeLogDetailDO.setCreateTime(date);
                            orderConfirmChangeLogDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
                            orderConfirmChangeLogDetailMapper.save(orderConfirmChangeLogDetailDO);
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                                confirmsb.append(orderProductDO.getProductName()).append("(").append(orderItemParam.getItemCount()).append("台)(全新)\n");
                                returnsb.append(orderProductDO.getProductName()).append("(").append(orderProductDO.getProductCount()-orderItemParam.getItemCount()).append("台)(全新)\n");
                            }else {
                                confirmsb.append(orderProductDO.getProductName()).append("(").append(orderItemParam.getItemCount()).append("台)(次新)\n");
                                returnsb.append(orderProductDO.getProductName()).append("(").append(orderProductDO.getProductCount()-orderItemParam.getItemCount()).append("台)(次新)\n");

                            }
                            //按天租的设置押金
                            if (orderDO.getRentType() == 1) {
                                BigDecimal one = BigDecimalUtil.div(orderProductDO.getDepositAmount(),new BigDecimal(orderProductDO.getProductCount()),3);
                                orderProductDO.setDepositAmount(BigDecimalUtil.mul(one,new BigDecimal(orderItemParam.getItemCount())));
                            }
                            //将订单商品项中的商品总数、商品在租数进行更新
                            orderProductDO.setProductCount(orderItemParam.getItemCount());
                            orderProductDO.setRentingProductCount(orderItemParam.getItemCount());
                        }else {
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                                confirmsb.append(orderProductDO.getProductName()).append("(").append(orderProductDO.getProductCount()).append("台)(全新)\n");
                            }else {
                                confirmsb.append(orderProductDO.getProductName()).append("(").append(orderProductDO.getProductCount()).append("台)(次新)\n");
                            }
                        }
                    }
                }

            }
            //配件变化保存订单确认收货变更记录详情信息
            if (orderItemParam.getItemType()==2) {
                OrderConfirmChangeLogDetailDO orderConfirmChangeLogDetailDO = new OrderConfirmChangeLogDetailDO();
                for (OrderMaterialDO orderMaterialDO:orderDO.getOrderMaterialDOList()) {
                    if (orderMaterialDO.getId().equals( orderItemParam.getItemId())) {
                        if (orderItemParam.getItemCount()>orderMaterialDO.getStableMaterialCount()) {
                            result.setErrorCode(ErrorCode.ITEM_COUNT_MORE_THAN_STABLE_MATERIAL_COUNT);
                            return result;
                        }
                        if (!orderMaterialDO.getMaterialCount().equals(orderItemParam.getItemCount())) {
                            if (orderMaterialDO.getMaterialCount() == 0) {
                                result.setErrorCode(ErrorCode.ORDER_MATERIAL_COUNT_IS_ZERO_NOT_CONFIRM);
                                return result;
                            }
                            //设置传递K3参数
                            ChangeOrderItemParam changeOrderItemParam = new ChangeOrderItemParam();
                            changeOrderItemParam.setItemId(orderItemParam.getItemId());
                            changeOrderItemParam.setItemType(orderItemParam.getItemType());
                            changeOrderItemParam.setReturnCount(orderMaterialDO.getStableMaterialCount()-orderItemParam.getItemCount());
                            changeOrderItemParamList.add(changeOrderItemParam);

                            orderConfirmChangeLogDetailDO.setOrderId(orderDO.getId());
                            orderConfirmChangeLogDetailDO.setOrderNo(orderDO.getOrderNo());
                            orderConfirmChangeLogDetailDO.setItemType(OrderItemType.ORDER_ITEM_TYPE_PRODUCT);
                            orderConfirmChangeLogDetailDO.setItemId(orderMaterialDO.getId());
                            orderConfirmChangeLogDetailDO.setOrderItemCount(orderMaterialDO.getStableMaterialCount());//订单初始商品/配件数
                            orderConfirmChangeLogDetailDO.setOldItemCount(orderMaterialDO.getMaterialCount());//原商品/配件数
                            orderConfirmChangeLogDetailDO.setNewItemCount(orderItemParam.getItemCount());//新商品/配件数
                            orderConfirmChangeLogDetailDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                            orderConfirmChangeLogDetailDO.setRemark(null);
                            orderConfirmChangeLogDetailDO.setCreateTime(date);
                            orderConfirmChangeLogDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
                            orderConfirmChangeLogDetailMapper.save(orderConfirmChangeLogDetailDO);
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                                confirmsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderItemParam.getItemCount()).append("台)(全新)\n");
                                returnsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderMaterialDO.getMaterialCount()-orderItemParam.getItemCount()).append("台)(全新)\n");
                            }else {
                                confirmsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderItemParam.getItemCount()).append("台)(次新)\n");
                                returnsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderMaterialDO.getMaterialCount()-orderItemParam.getItemCount()).append("台)(次新)\n");
                            }
                            //按天租的设置押金
                            if (orderDO.getRentType() == 1) {
                                BigDecimal one = BigDecimalUtil.div(orderMaterialDO.getDepositAmount(),new BigDecimal(orderMaterialDO.getMaterialCount()),3);
                                orderMaterialDO.setDepositAmount(BigDecimalUtil.mul(one,new BigDecimal(orderItemParam.getItemCount())));
                            }
                            //将订单商品项中的商品总数、商品在租数进行更新
                            orderMaterialDO.setMaterialCount(orderItemParam.getItemCount());
                            orderMaterialDO.setRentingMaterialCount(orderItemParam.getItemCount());
                        }else {
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                                confirmsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderMaterialDO.getMaterialCount()).append("台)(全新)\n");
                            }else {
                                confirmsb.append(orderMaterialDO.getMaterialName()).append("(").append(orderMaterialDO.getMaterialCount()).append("台)(次新)\n");
                            }
                        }
                    }
                }

            }
        }
        //设置传送到K3的参数
        orderConfirmChangeToK3Param.setChangeOrderItemParamList(changeOrderItemParamList);

        //保存图片
        if (orderConfirmChangeParam.getDeliveryNoteCustomerSignImg()!= null) {
            ImageDO deliveryNoteCustomerSignImgDO = imgMysqlMapper.findById(orderConfirmChangeParam.getDeliveryNoteCustomerSignImg().getImgId());
            if (deliveryNoteCustomerSignImgDO == null) {
                result.setErrorCode(ErrorCode.DELIVERY_NOTE_CUSTOMER_SIGN_IMAGE_NOT_EXISTS);
                return result;
            }
            deliveryNoteCustomerSignImgDO.setImgType(ImgType.DELIVERY_NOTE_CUSTOMER_SIGN);
            deliveryNoteCustomerSignImgDO.setRefId(orderDO.getId().toString());
            deliveryNoteCustomerSignImgDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            deliveryNoteCustomerSignImgDO.setUpdateTime(date);
            imgMysqlMapper.update(deliveryNoteCustomerSignImgDO);
        }
        //没有变化走原来确认收货逻辑
        if (count==(oldTotalProductCount+oldTotalMaterialCount)) {
            orderDO.setConfirmDeliveryTime(date);
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CONFIRM);
            orderDO.setUpdateTime(date);
            orderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            orderMapper.update(orderDO);

            // 记录订单时间轴
            orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, date, userSupport.getCurrentUserId(),OperationType.COMFIRM_ORDER);
            sb = new StringBuffer();
            sb.append("您的客户[").append(orderDO.getBuyerCustomerName()).append("]所下租赁订单（订单号：").append(orderDO.getOrderNo()).append("）已经正常确认收货。收货内容如下：\n");
            sb.append(confirmsb.toString());
            //给「业务员」发送消息
            if (orderDO.getOrderSellerId()!= null) {
                MessageThirdChannel messageThirdChannel = new MessageThirdChannel();
                messageThirdChannel.setMessageContent(sb.toString());
                messageThirdChannel.setReceiverUserId(orderDO.getOrderSellerId());
                messageThirdChannelService.sendMessage(messageThirdChannel);
            }
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(orderDO.getOrderNo());
            return result;
        }
//        // 更新订单信息，并完成确认收货操作
        orderConfirmChangeParam.setOrderId(orderDO.getId());
        BigDecimal oldTotalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        //为计算前初始化参数
        orderDO.setTotalRentDepositAmount(BigDecimal.ZERO);
        orderDO.setTotalCreditDepositAmount(BigDecimal.ZERO);
        orderDO.setTotalInsuranceAmount(BigDecimal.ZERO);
        orderDO.setTotalDepositAmount(BigDecimal.ZERO);
        orderDO.setTotalMustDepositAmount(BigDecimal.ZERO);

        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        // 更改订单，商品项，配件项的数量及金额
        calculateOrderProductInfo(orderProductDOList, orderDO);
        calculateOrderMaterialInfo(orderMaterialDOList, orderDO);

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));

        orderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        orderDO.setUpdateTime(date);
        orderDO.setConfirmDeliveryTime(date);
        //恢复信用额度（现成方法，有日志的记录）
        if (BigDecimalUtil.compare(oldTotalCreditDepositAmount,orderDO.getTotalCreditDepositAmount())>0) {
            BigDecimal value = BigDecimalUtil.sub(oldTotalCreditDepositAmount,orderDO.getTotalCreditDepositAmount());
            if (BigDecimalUtil.compare(value, BigDecimal.ZERO) != 0) {
                customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), value);
                logger.info("恢复信用额度{}", value);
            }
        }
        if (BigDecimalUtil.compare(oldTotalCreditDepositAmount,orderDO.getTotalCreditDepositAmount())<0) {
            BigDecimal value = BigDecimalUtil.sub(orderDO.getTotalCreditDepositAmount(),oldTotalCreditDepositAmount);
            if (BigDecimalUtil.compare(value, BigDecimal.ZERO) != 0) {
                customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), value);
                logger.info("恢复信用额度{}", value);
            }
        }

        //存储订单项
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                orderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                orderProductDO.setUpdateTime(date);
                orderProductMapper.update(orderProductDO);
            }
        }
        //存储配件项
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                orderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                orderMaterialDO.setUpdateTime(date);
                orderMaterialMapper.update(orderMaterialDO);
            }
        }
        //为了不影响之前的订单逻辑，这里暂时使用修改的方式
        setOrderProductSummary(orderDO);

        // 重算结算单
        ServiceResult<String, BigDecimal> serviceResult = statementService.reCreateOrderStatementAllowConfirmCustommer(orderDO);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            result.setErrorCode(serviceResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        if (count==0) {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_COLSE);
        } else {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CONFIRM);
        }
        orderMapper.update(orderDO);

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, date, userSupport.getCurrentUserId(),OperationType.COMFIRM_ORDER);


        Integer newTotalProductCount = orderDO.getTotalProductCount();
        Integer newTotalMaterialCount = orderDO.getTotalMaterialCount();
        //是否有退货标记，true为有退货，false为全部收货
        Boolean flag = !newTotalProductCount.equals(oldTotalProductCount) || !newTotalMaterialCount.equals(oldTotalMaterialCount);
        if (flag) {
            //保存订单确认收货变更记录
            OrderConfirmChangeLogDO orderConfirmChangeLogDO = new OrderConfirmChangeLogDO();
            orderConfirmChangeLogDO.setOrderId(orderDO.getId());
            orderConfirmChangeLogDO.setOrderNo(orderDO.getOrderNo());
            if (orderConfirmChangeParam.getChangeReasonType()==null) {
                result.setErrorCode(ErrorCode.CONFIRM_CHANGE_REASON_TYPE_NOT_NULL);
                return result;
            }
            if (orderConfirmChangeParam.getChangeReasonType()==1) {
                orderConfirmChangeLogDO.setChangeReasonType(ConfirmChangeReasonType.CONFIRM_CHANGE_REASON_TYPE__EQUIPMENT_FAILURE);
                orderConfirmChangeLogDO.setChangeReason("设备故障");
            }else if (orderConfirmChangeParam.getChangeReasonType()==2) {
                orderConfirmChangeLogDO.setChangeReasonType(ConfirmChangeReasonType.CONFIRM_CHANGE_REASON_TYPE_MORE);
                orderConfirmChangeLogDO.setChangeReason("商品数量超过实际需求");
            } else if (orderConfirmChangeParam.getChangeReasonType() == 3) {
                orderConfirmChangeLogDO.setChangeReasonType(ConfirmChangeReasonType.CONFIRM_CHANGE_REASON_TYPE_OTHER);
                orderConfirmChangeLogDO.setChangeReason(orderConfirmChangeParam.getChangeReason());
            } else {
                result.setErrorCode(ErrorCode.CONFIRM_CHANGE_REASON_TYPE_ERROR);
                return result;
            }
            orderConfirmChangeLogDO.setIsRestatementSuccess(CommonConstant.COMMON_CONSTANT_YES);
            orderConfirmChangeLogDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
            orderConfirmChangeLogDO.setCreateTime(date);
            orderConfirmChangeLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            orderConfirmChangeLogMapper.save(orderConfirmChangeLogDO);
        }
        // TODO: 2018\5\22 0022  7.传参数给K3
        ServiceResult<String, String> k3ServiceResult = k3Service.confirmOrder(orderConfirmChangeToK3Param);
        if (!ErrorCode.SUCCESS.equals(k3ServiceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            result.setErrorCode(k3ServiceResult.getErrorCode(),k3ServiceResult.getFormatArgs());
            return result;
        }
        // 推送钉钉
        if (flag) {//有退货
            if (orderDO.getOrderStatus()==OrderStatus.ORDER_STATUS_COLSE) {//订单状态为关闭，全部退货
                sb = new StringBuffer();
                sb.append("您的客户[").append(orderDO.getBuyerCustomerName()).append("]所下租赁订单（订单号：").append(orderDO.getOrderNo()).append("）已全部退货,退回内容如下：\n");
                sb.append(returnsb.toString());
            }else {
                sb.append(confirmsb.toString()).append("\n").append("下列商品、配件退回：\n").append(returnsb.toString());
            }
        }
        //给「业务员」发送消息
        if (orderDO.getOrderSellerId()!= null) {
            MessageThirdChannel messageThirdChannel = new MessageThirdChannel();
            messageThirdChannel.setMessageContent(sb.toString());
            messageThirdChannel.setReceiverUserId(orderDO.getOrderSellerId());
            messageThirdChannelService.sendMessage(messageThirdChannel);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, LastRentPriceResponse> queryLastPrice(LastRentPriceRequest request) {
        ServiceResult<String, LastRentPriceResponse> result = new ServiceResult<>();

        if (request.getCustomerNo() == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NO_NOT_NULL);
            return result;
        }
        if (request.getProductSkuId() == null && request.getMaterialId() == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(request.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }


        LastRentPriceResponse response = new LastRentPriceResponse();
        response.setCustomerNo(request.getCustomerNo());
        BigDecimal productLastDayAmount = null, productLastMonthAmount = null, monthLastDayAmount = null, monthLastMonthAmount = null;
        if (request.getProductSkuId() != null) {
            ServiceResult<String, Product> queryProductResult = productService.queryProductBySkuId(request.getProductSkuId());
            if (!ErrorCode.SUCCESS.equals(queryProductResult.getErrorCode())) {
                result.setErrorCode(queryProductResult.getErrorCode());
                return result;
            }
            Product product = queryProductResult.getResult();
            List<Map<String, Object>> productSkuLastPriceMap = orderProductMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId(), request.getIsNewProduct());
            for (Map<String, Object> map : productSkuLastPriceMap) {
                if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_DAY.equals(map.get("rent_type"))) {
                    productLastDayAmount = (BigDecimal) (map.get("product_unit_amount"));
                } else if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_MONTH.equals(map.get("rent_type"))) {
                    productLastMonthAmount = (BigDecimal) (map.get("product_unit_amount"));
                }
            }
            response.setProductSkuId(request.getProductSkuId());
            response.setProductSkuLastDayPrice(productLastDayAmount);
            response.setProductSkuLastMonthPrice(productLastMonthAmount);
            response.setProductSkuDayPrice(request.getIsNewProduct() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewProduct()) ? product.getProductSkuList().get(0).getDayRentPrice() : product.getProductSkuList().get(0).getNewDayRentPrice());
            response.setProductSkuMonthPrice(request.getIsNewProduct() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewProduct()) ? product.getProductSkuList().get(0).getMonthRentPrice() : product.getProductSkuList().get(0).getNewMonthRentPrice());
        }

        if (request.getMaterialId() != null) {
            ServiceResult<String, Material> queryMaterialResult = materialService.queryMaterialById(request.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(queryMaterialResult.getErrorCode())) {
                result.setErrorCode(queryMaterialResult.getErrorCode());
                return result;
            }
            Material material = queryMaterialResult.getResult();
            List<Map<String, Object>> materialSkuLastPriceMap = orderMaterialMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId(), request.getIsNewProduct());
            for (Map<String, Object> map : materialSkuLastPriceMap) {
                if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_DAY.equals(map.get("rent_type"))) {
                    monthLastDayAmount = (BigDecimal) (map.get("product_unit_amount"));
                } else if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_MONTH.equals(map.get("rent_type"))) {
                    monthLastMonthAmount = (BigDecimal) (map.get("product_unit_amount"));
                }
            }
            response.setMaterialId(request.getMaterialId());
            response.setMaterialLastDayPrice(monthLastDayAmount);
            response.setMaterialLastMonthPrice(monthLastMonthAmount);
            response.setMaterialDayPrice(request.getIsNewMaterial() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewMaterial()) ? material.getDayRentPrice() : material.getNewDayRentPrice());
            response.setMaterialMonthPrice(request.getIsNewMaterial() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewMaterial()) ? material.getMonthRentPrice() : material.getNewMonthRentPrice());
        }

        result.setResult(response);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> returnEquipment(String orderNo, String returnEquipmentNo, String changeEquipmentNo, Date returnDate) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null || returnEquipmentNo == null || returnDate == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), returnEquipmentNo);
        if (orderProductEquipmentDO == null) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
            return result;
        }
        if (orderProductEquipmentDO.getActualReturnTime() != null) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_ALREADY_RETURN);
            return result;
        }
        OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
        // 如果是换货，产生新的记录
        if (StringUtil.isNotBlank(changeEquipmentNo)) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeEquipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
                return result;
            }
            ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(productEquipmentDO.getSkuId());
            if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                result.setErrorCode(productServiceResult.getErrorCode());
                return result;
            }
            OrderProductEquipmentDO newOrderProductEquipmentDO = new OrderProductEquipmentDO();
            newOrderProductEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
            newOrderProductEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
            newOrderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
            newOrderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
            newOrderProductEquipmentDO.setRentStartTime(returnDate);
            newOrderProductEquipmentDO.setExpectReturnTime(orderProductEquipmentDO.getExpectReturnTime());

            // TODO 换货价格按照最新的价格来算还是按照原来的价格来算，现在采用的是按照之前价格来算
            BigDecimal productUnitAmount = orderProductDO.getProductUnitAmount();
            if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                productUnitAmount = productServiceResult.getResult().getProductSkuList().get(0).getDayRentPrice();
            } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                productUnitAmount = productServiceResult.getResult().getProductSkuList().get(0).getMonthRentPrice();
            }
            newOrderProductEquipmentDO.setProductEquipmentUnitAmount(orderProductEquipmentDO.getProductEquipmentUnitAmount());
            newOrderProductEquipmentDO.setExpectRentAmount(amountSupport.calculateRentAmount(returnDate, orderProductEquipmentDO.getExpectReturnTime(), orderProductEquipmentDO.getProductEquipmentUnitAmount()));
            newOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newOrderProductEquipmentDO.setCreateTime(currentTime);
            newOrderProductEquipmentDO.setUpdateTime(currentTime);
            newOrderProductEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            newOrderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            orderProductEquipmentMapper.save(newOrderProductEquipmentDO);
        }

        orderProductEquipmentDO.setActualRentAmount(amountSupport.calculateRentAmount(orderProductEquipmentDO.getRentStartTime(), returnDate, orderProductEquipmentDO.getProductEquipmentUnitAmount()));
        orderProductEquipmentDO.setActualReturnTime(returnDate);
        orderProductEquipmentDO.setUpdateTime(currentTime);
        orderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
        orderProductEquipmentMapper.update(orderProductEquipmentDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> returnBulkMaterial(String orderNo, String returnNBulkMaterialNo, String changeBulkMaterialNo, Date returnDate) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        if (returnNBulkMaterialNo == null || returnDate == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        BulkMaterialDO returnBulkMaterialDO = bulkMaterialMapper.findByNo(returnNBulkMaterialNo);
        if (returnBulkMaterialDO == null) {
            result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
            return result;
        }
        // 如果物料存在设备号，证明是由设备上拆下，设备升配
        if (StringUtil.isNotBlank(returnBulkMaterialDO.getCurrentEquipmentNo())) {
            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), returnBulkMaterialDO.getCurrentEquipmentNo());
            if (orderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                return result;
            }
            OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
            if (StringUtil.isNotBlank(changeBulkMaterialNo)) {
                BulkMaterialDO changeBulkMaterialDO = bulkMaterialMapper.findByNo(changeBulkMaterialNo);
                if (changeBulkMaterialDO == null) {
                    result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                    return result;
                }
                ServiceResult<String, Material> changeMaterialServiceResult = materialService.queryMaterialById(changeBulkMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(changeMaterialServiceResult.getErrorCode())) {
                    result.setErrorCode(changeMaterialServiceResult.getErrorCode());
                    return result;
                }
                BigDecimal changeMaterialBulkUnitAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    changeMaterialBulkUnitAmount = changeMaterialServiceResult.getResult().getDayRentPrice();
                } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                    changeMaterialBulkUnitAmount = changeMaterialServiceResult.getResult().getMonthRentPrice();
                }
                ServiceResult<String, Material> returnMaterialServiceResult = materialService.queryMaterialById(returnBulkMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(returnMaterialServiceResult.getErrorCode())) {
                    result.setErrorCode(returnMaterialServiceResult.getErrorCode());
                    return result;
                }
                BigDecimal returnMaterialBulkUnitAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    returnMaterialBulkUnitAmount = returnMaterialServiceResult.getResult().getDayRentPrice();
                } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                    returnMaterialBulkUnitAmount = returnMaterialServiceResult.getResult().getMonthRentPrice();
                }
                //  TODO 计算差价 并且把单价提升 暂时不提升
                /*if (BigDecimalUtil.compare(changeMaterialBulkUnitAmount, returnMaterialBulkUnitAmount) > 0) {
                    BigDecimal diffAmount = BigDecimalUtil.sub(changeMaterialBulkUnitAmount, returnMaterialBulkUnitAmount);
                    orderProductEquipmentDO.setProductEquipmentUnitAmount(BigDecimalUtil.add(orderProductEquipmentDO.getProductEquipmentUnitAmount(), diffAmount));
                    orderProductEquipmentDO.setUpdateTime(currentTime);
                    orderProductEquipmentMapper.update(orderProductEquipmentDO);
                }*/

            }
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }

        // 以下为换不在设备上的物料逻辑
        OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), returnNBulkMaterialNo);
        if (orderMaterialBulkDO == null) {
            result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_NOT_EXISTS);
            return result;
        }
        if (orderMaterialBulkDO.getActualReturnTime() != null) {
            result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_ALREADY_RETURN);
            return result;
        }
        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
        // 如果是换货，产生新的记录
        if (StringUtil.isNotBlank(changeBulkMaterialNo)) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(changeBulkMaterialNo);
            if (bulkMaterialDO == null) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                return result;
            }
            ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(bulkMaterialDO.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                result.setErrorCode(materialServiceResult.getErrorCode());
                return result;
            }
            OrderMaterialBulkDO newOrderMaterialBulkDO = new OrderMaterialBulkDO();
            newOrderMaterialBulkDO.setOrderId(orderMaterialBulkDO.getOrderId());
            newOrderMaterialBulkDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
            newOrderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
            newOrderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
            newOrderMaterialBulkDO.setRentStartTime(returnDate);
            newOrderMaterialBulkDO.setMaterialBulkUnitAmount(orderMaterialDO.getMaterialUnitAmount());
            newOrderMaterialBulkDO.setExpectReturnTime(orderMaterialBulkDO.getExpectReturnTime());
            BigDecimal materialBulkUnitAmount = orderMaterialDO.getMaterialUnitAmount();
            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                materialBulkUnitAmount = materialServiceResult.getResult().getDayRentPrice();
            } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType()))) {
                materialBulkUnitAmount = materialServiceResult.getResult().getMonthRentPrice();
            }
            newOrderMaterialBulkDO.setExpectRentAmount(amountSupport.calculateRentAmount(returnDate, orderMaterialBulkDO.getExpectReturnTime(), materialBulkUnitAmount));
            newOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newOrderMaterialBulkDO.setCreateTime(currentTime);
            newOrderMaterialBulkDO.setUpdateTime(currentTime);
            newOrderMaterialBulkDO.setCreateUser(loginUser.getUserId().toString());
            newOrderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
            orderMaterialBulkMapper.save(newOrderMaterialBulkDO);
        }

        orderMaterialBulkDO.setActualRentAmount(amountSupport.calculateRentAmount(orderMaterialBulkDO.getRentStartTime(), returnDate, orderMaterialBulkDO.getMaterialBulkUnitAmount()));
        orderMaterialBulkDO.setActualReturnTime(returnDate);
        orderMaterialBulkDO.setUpdateTime(currentTime);
        orderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
        orderMaterialBulkMapper.update(orderMaterialBulkDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            Date currentTime = new Date();
            User loginUser = userSupport.getCurrentUser();
            OrderDO orderDO = orderMapper.findByOrderNo(businessNo);
            if (orderDO == null || !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
                return ErrorCode.BUSINESS_EXCEPTION;
            }
            if (verifyResult) {
                CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
                // 审核通过时，对当前订单做短租应收额度校验
                String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, orderDO);
                if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return verifyOrderShortRentReceivableResult;
                }

                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
                // 只有审批通过的订单才生成结算单
                ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createOrderStatement(orderDO.getOrderNo());
                if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return createStatementOrderResult.getErrorCode();
                }
                orderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.VERIFY_ORDER_SUCCESS);
                orderDO.setUpdateTime(currentTime);
                orderDO.setUpdateUser(loginUser.getUserId().toString());
                orderMapper.update(orderDO);
                //获取订单详细信息，发送给k3
                Order order = queryOrderByNo(orderDO.getOrderNo()).getResult();
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD, PostK3Type.POST_K3_TYPE_ORDER, order, true);
            } else {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
                // 如果拒绝，则退还授信额度
                if (BigDecimalUtil.compare(orderDO.getTotalCreditDepositAmount(), BigDecimal.ZERO) != 0) {
                    customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), orderDO.getTotalCreditDepositAmount());
                }
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_REJECT, null, currentTime, loginUser.getUserId(),OperationType.VERIFY_ORDER_FAILED);
                orderDO.setUpdateTime(currentTime);
                orderDO.setUpdateUser(loginUser.getUserId().toString());
                orderMapper.update(orderDO);
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("审批订单通知失败： " + businessNo, e);
            return ErrorCode.BUSINESS_EXCEPTION;
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Order> queryOrderByNo(String orderNo) {
        ServiceResult<String, Order> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        orderDO.setCustomerRiskManagementDO(customerRiskManagementDO);

        List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisSupport.getOrderTimeAxis(orderDO.getId());
        orderDO.setOrderTimeAxisDOList(orderTimeAxisDOList);

//        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletOrderByOrderNo(orderDO.getOrderNo());
//        orderDO.setReletOrderDOList(reletOrderDOList);

        Order order = ConverterUtil.convert(orderDO, Order.class);

        if (orderDO.getOrderUnionSellerId() != null){
            UserDO unionUser = userMapper.findByUserId(orderDO.getOrderUnionSellerId());
            if(unionUser != null){
                order.setOrderUnionSellerName(unionUser.getRealName());
                order.setOrderUnionSellerPhone(unionUser.getPhone());
            }
        }

        ServiceResult<String, StatementOrder> statementOrderResult = statementService.queryStatementOrderDetailByOrderId(order.getOrderNo());
        if (ErrorCode.SUCCESS.equals(statementOrderResult.getErrorCode())) {
            order.setStatementOrder(statementOrderResult.getResult());
        }

        orderFirstNeedPayAmount(order, orderDO);

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            BigDecimal totalProductDeposit = BigDecimal.ZERO;
            BigDecimal totalProductRent = BigDecimal.ZERO;
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
                if (product != null && CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                    for (ProductSku productSku : product.getProductSkuList()) {
                        if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                            orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                            break;
                        }
                    }
                }
                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderProduct.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderProduct.getOrderProductId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderProduct.setFirstNeedPayAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderProduct.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                        orderProduct.setFirstNeedPayAmount(orderProduct.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayAmount());
                        orderProduct.setFirstNeedPayRentAmount(orderProduct.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayRentAmount());
                        orderProduct.setFirstNeedPayDepositAmount(BigDecimalUtil.add(orderProduct.getRentDepositAmount(), orderProduct.getDepositAmount()));
                    }
                }

                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProduct.getOrderProductId());
                orderProduct.setOrderProductEquipmentList(ConverterUtil.convertList(orderProductEquipmentDOList, OrderProductEquipment.class));
                totalProductDeposit = BigDecimalUtil.add(totalProductDeposit, orderProduct.getFirstNeedPayDepositAmount());
                totalProductRent = BigDecimalUtil.add(totalProductRent, orderProduct.getFirstNeedPayRentAmount());

            }
            order.setTotalProductFirstNeedPayAmount(BigDecimalUtil.add(totalProductDeposit, totalProductRent));
        }
        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            BigDecimal totalMaterialDeposit = BigDecimal.ZERO;
            BigDecimal totalMaterialRent = BigDecimal.ZERO;
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterial.getOrderMaterialId());
                orderMaterial.setOrderMaterialBulkList(ConverterUtil.convertList(orderMaterialBulkDOList, OrderMaterialBulk.class));

                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderMaterial.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderMaterial.getOrderMaterialId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderMaterial.setFirstNeedPayAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderMaterial.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                    }
                    orderMaterial.setFirstNeedPayAmount(orderMaterial.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayAmount());
                    orderMaterial.setFirstNeedPayRentAmount(orderMaterial.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayRentAmount());
                    orderMaterial.setFirstNeedPayDepositAmount(BigDecimalUtil.add(orderMaterial.getRentDepositAmount(), orderMaterial.getDepositAmount()));
                }
                totalMaterialDeposit = BigDecimalUtil.add(totalMaterialDeposit, orderMaterial.getFirstNeedPayDepositAmount());
                totalMaterialRent = BigDecimalUtil.add(totalMaterialRent, orderMaterial.getFirstNeedPayRentAmount());

            }
            order.setTotalMaterialFirstNeedPayAmount(BigDecimalUtil.add(totalMaterialDeposit, totalMaterialRent));
        }
        //获取订单退货单项列表
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(order.getOrderNo());
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = ConverterUtil.convertList(k3ReturnOrderDetailDOList, K3ReturnOrderDetail.class);
        order.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);
        //判断是否可续租
        Integer canReletOrder = orderSupport.isOrderCanRelet(order);
        order.setCanReletOrder(canReletOrder);
        Integer isReletOrder = order.getReletOrderId() != null ? CommonConstant.YES : CommonConstant.NO;
        order.setIsReletOrder(isReletOrder);

        //获取确认收货变更原因及交货单客户签字图片逻辑
        if (order.getOrderStatus()>OrderStatus.ORDER_STATUS_DELIVERED) {
            ImageDO byRefId = imgMysqlMapper.findLastByRefIdAndType(order.getOrderId().toString(),ImgType.DELIVERY_NOTE_CUSTOMER_SIGN);
            if (byRefId!= null) {
                Image image = ConverterUtil.convert(byRefId,Image.class);
                order.setDeliveryNoteCustomerSignImg(image);
            }
            OrderConfirmChangeLogDO orderConfirmChangeLogDO = orderConfirmChangeLogMapper.findLastByOrderId(order.getOrderId());
            if (orderConfirmChangeLogDO!=null) {
                order.setChangeReason(orderConfirmChangeLogDO.getChangeReason());
            }
        }
        OrderStatementDateSplitDO orderStatementDateSplitDO=orderStatementDateSplitMapper.findByOrderNo(orderNo);
        if(orderStatementDateSplitDO!=null)order.setOrderStatementDateSplit(ConverterUtil.convert(orderStatementDateSplitDO, OrderStatementDateSplit.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }


    @Override
    public ServiceResult<String, Order> queryOrderByNoNew(String orderNo) {
        ServiceResult<String, Order> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisSupport.getOrderTimeAxis(orderDO.getId());
        orderDO.setOrderTimeAxisDOList(orderTimeAxisDOList);

//        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletOrderByOrderNo(orderDO.getOrderNo());
//        orderDO.setReletOrderDOList(reletOrderDOList);

        Order order = ConverterUtil.convert(orderDO, Order.class);

        ServiceResult<String, StatementOrder> statementOrderResult = statementService.queryStatementOrderDetailByOrderId(order.getOrderNo());
        if (ErrorCode.SUCCESS.equals(statementOrderResult.getErrorCode())) {
            order.setStatementOrder(statementOrderResult.getResult());
        }

        orderFirstNeedPayAmount(order, orderDO);

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            BigDecimal totalProductDeposit = BigDecimal.ZERO;
            BigDecimal totalProductRent = BigDecimal.ZERO;
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
                if (product != null && CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                    for (ProductSku productSku : product.getProductSkuList()) {
                        if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                            orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                            break;
                        }
                    }
                }
                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderProduct.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderProduct.getOrderProductId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderProduct.setFirstNeedPayAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderProduct.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                        orderProduct.setFirstNeedPayAmount(orderProduct.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayAmount());
                        orderProduct.setFirstNeedPayRentAmount(orderProduct.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayRentAmount());
                        orderProduct.setFirstNeedPayDepositAmount(BigDecimalUtil.add(orderProduct.getRentDepositAmount(), orderProduct.getDepositAmount()));
                    }
                }

                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProduct.getOrderProductId());
                orderProduct.setOrderProductEquipmentList(ConverterUtil.convertList(orderProductEquipmentDOList, OrderProductEquipment.class));
                totalProductDeposit = BigDecimalUtil.add(totalProductDeposit, orderProduct.getFirstNeedPayDepositAmount());
                totalProductRent = BigDecimalUtil.add(totalProductRent, orderProduct.getFirstNeedPayRentAmount());

            }
            order.setTotalProductFirstNeedPayAmount(BigDecimalUtil.add(totalProductDeposit, totalProductRent));
        }
        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            BigDecimal totalMaterialDeposit = BigDecimal.ZERO;
            BigDecimal totalMaterialRent = BigDecimal.ZERO;
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterial.getOrderMaterialId());
                orderMaterial.setOrderMaterialBulkList(ConverterUtil.convertList(orderMaterialBulkDOList, OrderMaterialBulk.class));

                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderMaterial.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderMaterial.getOrderMaterialId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderMaterial.setFirstNeedPayAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderMaterial.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                    }
                    orderMaterial.setFirstNeedPayAmount(orderMaterial.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayAmount());
                    orderMaterial.setFirstNeedPayRentAmount(orderMaterial.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayRentAmount());
                    orderMaterial.setFirstNeedPayDepositAmount(BigDecimalUtil.add(orderMaterial.getRentDepositAmount(), orderMaterial.getDepositAmount()));
                }
                totalMaterialDeposit = BigDecimalUtil.add(totalMaterialDeposit, orderMaterial.getFirstNeedPayDepositAmount());
                totalMaterialRent = BigDecimalUtil.add(totalMaterialRent, orderMaterial.getFirstNeedPayRentAmount());

            }
            order.setTotalMaterialFirstNeedPayAmount(BigDecimalUtil.add(totalMaterialDeposit, totalMaterialRent));
        }
        //获取订单退货单项列表
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(order.getOrderNo());
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = ConverterUtil.convertList(k3ReturnOrderDetailDOList, K3ReturnOrderDetail.class);
        order.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);

        //判断是否可续租
        Integer canReletOrder = orderSupport.isOrderCanRelet(order);
        order.setCanReletOrder(canReletOrder);
        Integer isReletOrder = order.getReletOrderId() != null ? CommonConstant.YES : CommonConstant.NO;
        order.setIsReletOrder(isReletOrder);

        /*******组合商品逻辑 start********/
        // 将orderJointProductId不为空的订单商品和配件放入对应的OrderJointProduct中, 并将数量除以组合商品数
        buildOrderJointProductAfterQuery(order);
        /*******组合商品逻辑 end********/
        //获取确认收货变更原因及交货单客户签字图片逻辑
        if (order.getOrderStatus()>OrderStatus.ORDER_STATUS_DELIVERED) {
            ImageDO byRefId = imgMysqlMapper.findLastByRefIdAndType(order.getOrderId().toString(),ImgType.DELIVERY_NOTE_CUSTOMER_SIGN);
            if (byRefId!= null) {
                Image image = ConverterUtil.convert(byRefId,Image.class);
                order.setDeliveryNoteCustomerSignImg(image);
            }
            OrderConfirmChangeLogDO orderConfirmChangeLogDO = orderConfirmChangeLogMapper.findLastByOrderId(order.getOrderId());
            if (orderConfirmChangeLogDO!=null) {
                order.setChangeReason(orderConfirmChangeLogDO.getChangeReason());
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }

    // 根据订单商品项和配件项的组合商品项id来将其移动到对应的组合商品项中
    private void buildOrderJointProductAfterQuery(Order order) {
        List<OrderJointProduct> orderJointProductList = order.getOrderJointProductList();
        if (CollectionUtil.isNotEmpty(orderJointProductList)) {
            Map<Integer, OrderJointProduct> orderJointProductMap = ListUtil.listToMap(orderJointProductList, "orderJointProductId");
            List<OrderProduct> orderProductList = order.getOrderProductList();
            if (CollectionUtil.isNotEmpty(orderProductList)) {
                // 填入JointProductProduct实体
                Set<Integer> jointProductProductIds = new HashSet<>();
                for (OrderProduct orderProduct : orderProductList) {
                    if (orderProduct .getJointProductProductId() != null) {
                        jointProductProductIds.add(orderProduct .getJointProductProductId());
                    }
                }
                if (jointProductProductIds.size() > 0) {
                    List<JointProductProductDO> jointProductProductDOList = jointProductProductMapper.findByIds(jointProductProductIds);
                    List<JointProductProduct> jointProductProductList = ConverterUtil.convertList(jointProductProductDOList, JointProductProduct.class);
                    Map<Integer, JointProductProduct> jointProductProductMap = ListUtil.listToMap(jointProductProductList, "jointProductProductId");
                    for (OrderProduct orderProduct : orderProductList) {
                        if (orderProduct.getJointProductProductId() != null) {
                            JointProductProduct jointProductProduct = jointProductProductMap.get(orderProduct.getJointProductProductId());
                            if (jointProductProduct != null) {
                                orderProduct.setJointProductProduct(jointProductProduct);
                            }
                        }
                    }
                }


                List<OrderProduct> removeOrderProductList = new ArrayList<>();
                for (OrderProduct orderProduct : orderProductList) {
                    if (orderProduct.getOrderJointProductId() != null) {
                        OrderJointProduct orderJointProduct = orderJointProductMap.get(orderProduct.getOrderJointProductId());
                        if (orderJointProduct != null) {
                            if (orderJointProduct.getOrderProductList() == null) {
                                orderJointProduct.setOrderProductList(new ArrayList<OrderProduct>());
                            }
                            orderJointProduct.getOrderProductList().add(orderProduct);
                            removeOrderProductList.add(orderProduct);
                        }
                    }
                }
                orderProductList.removeAll(removeOrderProductList);
            }
            List<OrderMaterial> orderMaterialList = order.getOrderMaterialList();
            if (CollectionUtil.isNotEmpty(orderMaterialList)) {
                // 填入JointMaterial实体
                Set<Integer> jointMaterialIds = new HashSet<>();
                for (OrderMaterial orderMaterial : orderMaterialList) {
                    if (orderMaterial.getJointMaterialId() != null) {
                        jointMaterialIds.add(orderMaterial.getJointMaterialId());
                    }
                }
                if (jointMaterialIds.size() > 0) {
                    List<JointMaterialDO> jointMaterialDOList = jointMaterialMapper.findByIds(jointMaterialIds);
                    List<JointMaterial> jointMaterialList = ConverterUtil.convertList(jointMaterialDOList, JointMaterial.class);
                    Map<Integer, JointMaterial> jointMaterialMap = ListUtil.listToMap(jointMaterialList, "jointMaterialId");
                    for (OrderMaterial orderMaterial : orderMaterialList) {
                        if (orderMaterial.getJointMaterialId() != null) {
                            JointMaterial jointMaterial = jointMaterialMap.get(orderMaterial.getJointMaterialId());
                            if (jointMaterial != null) {
                                orderMaterial.setJointMaterial(jointMaterial);
                            }
                        }
                    }
                }

                List<OrderMaterial> removeOrderMaterialList = new ArrayList<>();
                for (OrderMaterial orderMaterial : orderMaterialList) {
                    if (orderMaterial.getOrderJointProductId() != null) {
                        OrderJointProduct orderJointProduct = orderJointProductMap.get(orderMaterial.getOrderJointProductId());
                        if (orderJointProduct != null) {
                            if (orderJointProduct.getOrderMaterialList() == null) {
                                orderJointProduct.setOrderMaterialList(new ArrayList<OrderMaterial>());
                            }
                            orderJointProduct.getOrderMaterialList().add(orderMaterial);
                            removeOrderMaterialList.add(orderMaterial); // 从返回的商品项列表移除组合商品中的
                        }
                    }
                }
                orderMaterialList.removeAll(removeOrderMaterialList);
            }

            for (OrderJointProduct orderJointProduct : orderJointProductList) {
                JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(orderJointProduct.getJointProductId());
                if (jointProductDO != null) {
                    JointProduct jointProduct = ConverterUtil.convert(jointProductDO, JointProduct.class);
                    orderJointProduct.setJointProductProductList(jointProduct.getJointProductProductList());
                    orderJointProduct.setJointMaterialList(jointProduct.getJointMaterialList());
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelOrder(String orderNo, Integer cancelOrderReasonType) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (cancelOrderReasonType == null) {
            result.setErrorCode(ErrorCode.CANCEL_ORDER_REASON_TYPE_NULL);
            return result;
        }
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (orderDO.getOrderStatus() == null || !OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!loginUser.getUserId().toString().equals(orderDO.getCreateUser())) {
            result.setErrorCode(ErrorCode.DATA_NOT_BELONG_TO_YOU);
            return result;
        }
        orderDO.setCancelOrderReasonType(cancelOrderReasonType);
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.CANCEL_ORDER);
        // TODO: 2018\4\26 0026  清除之前订单锁定的优惠券
        String revertresult = couponSupport.revertCoupon(orderDO.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(revertresult)) {
            result.setErrorCode(revertresult);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> forceCancelOrder(String orderNo, Integer cancelOrderReasonType) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        if (cancelOrderReasonType == null) {
            result.setErrorCode(ErrorCode.CANCEL_ORDER_REASON_TYPE_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        //非超级管理员，不能处理已支付的订单
        boolean paid = false;
        if(PayStatus.PAY_STATUS_PAID_PART.equals(orderDO.getPayStatus()) || PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())){
            paid = true;
        }
        if (!userSupport.isSuperUser()&&paid) {
            result.setErrorCode(ErrorCode.ORDER_ALREADY_PAID);
            return result;
        }
        if (userSupport.isSuperUser()) {
            if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus()) &&
                    !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus()) &&
                    !OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus()) &&
                    !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderDO.getOrderStatus())) {
                result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
                return result;
            }
        } else {
            if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus()) &&
                    !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus()) &&
                    !OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())) {
                result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
                return result;
            }
        }
        IERPService service = null;
        try {
            K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(orderDO.getId(), PostK3Type.POST_K3_TYPE_CANCEL_ORDER);
            if (k3SendRecordDO == null) {
                //创建推送记录，此时发送状态失败，接收状态失败
                k3SendRecordDO = new K3SendRecordDO();
                k3SendRecordDO.setRecordType(PostK3Type.POST_K3_TYPE_CANCEL_ORDER);
                k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setRecordJson(orderDO.getOrderNo());
                k3SendRecordDO.setSendTime(new Date());
                k3SendRecordDO.setRecordReferId(orderDO.getId());
                k3SendRecordMapper.save(k3SendRecordDO);
                logger.info("【推送消息】" + orderDO.getOrderNo());
            }
            service = new ERPServiceLocator().getBasicHttpBinding_IERPService();
            com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response = service.cancelOrder(orderDO.getOrderNo());
            //修改推送记录
            if (response == null) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                //失败要回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(ErrorCode.K3_SERVER_ERROR);
                return result;
            } else if (response.getStatus() != 0) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                //失败要回滚
                throw new BusinessException("k3取消订单失败:" + response.getResult());
            } else {
                logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： " + JSON.toJSONString(response));
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(JSON.toJSONString(response));
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【返回结果】" + response);

        } catch (ServiceException e) {
            throw new BusinessException("k3取消订单失败:", e.getMessage());
        } catch (RemoteException e) {
            throw new BusinessException("k3取消订单失败:", e.getMessage());
        }

        //审核中的订单，处理工作流
        if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo());
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                result.setErrorCode(cancelWorkFlowResult.getErrorCode());
                return result;
            }
        }
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER,orderDO.getId());
        Map<Integer, StatementOrderDO> statementOrderDOMap = statementOrderSupport.getStatementOrderByDetails(statementOrderDetailDOList);
        //审核中或者待发货订单，处理风控额度及结算单
        if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus()) ||
                OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus()) ||
                OrderStatus.ORDER_STATUS_DELIVERED.equals(orderDO.getOrderStatus())) {
            //恢复信用额度
            BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
            if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
                customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
            }
            statementOrderSupport.reStatement(currentTime,statementOrderDOMap,statementOrderDetailDOList);
        }
        orderDO.setCancelOrderReasonType(cancelOrderReasonType);
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.FORCE_CANCEL_ORDER);


        // TODO: 2018\4\26 0026 清除锁定优惠券
        String revertresult = couponSupport.revertCoupon(orderDO.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(revertresult)) {
            result.setErrorCode(revertresult);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }


        //超级管理员处理已支付的订单
        //已付设备押金
        BigDecimal depositPaidAmount = BigDecimal.ZERO;
        //已付其他费用
        BigDecimal otherPaidAmount = BigDecimal.ZERO;
        // 已付租金
        BigDecimal rentPaidAmount = BigDecimal.ZERO;
        //已付逾期费用
        BigDecimal overduePaidAmount = BigDecimal.ZERO;
        //已付违约金
        BigDecimal penaltyPaidAmount = BigDecimal.ZERO;
        //已付租金押金
        BigDecimal rentDepositPaidAmount = BigDecimal.ZERO;

        if (paid) {
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                //计算所有已支付金额,由于付款是在冲正后做的，所以此时无需考虑冲正金额
                depositPaidAmount = BigDecimalUtil.add(depositPaidAmount,statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                otherPaidAmount = BigDecimalUtil.add(otherPaidAmount,statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                rentPaidAmount = BigDecimalUtil.add(rentPaidAmount,statementOrderDetailDO.getStatementDetailRentPaidAmount());
                overduePaidAmount = BigDecimalUtil.add(overduePaidAmount,statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                penaltyPaidAmount = BigDecimalUtil.add(penaltyPaidAmount,statementOrderDetailDO.getStatementDetailPenaltyPaidAmount());
                rentDepositPaidAmount = BigDecimalUtil.add(rentDepositPaidAmount,statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
            }
            //处理结算单总状态及已支付金额
            statementOrderSupport.reStatementPaid(statementOrderDOMap,statementOrderDetailDOList);
            String returnCode = paymentService.returnDepositExpand(orderDO.getBuyerCustomerNo(),rentPaidAmount,BigDecimalUtil.addAll(otherPaidAmount,overduePaidAmount,penaltyPaidAmount)
                    ,rentDepositPaidAmount,depositPaidAmount,"超级管理员强制取消已支付订单，已支付金额退还到客户余额");
            if(!ErrorCode.SUCCESS.equals(returnCode)){
                result.setErrorCode(returnCode);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return result;
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> processStatementOrderByCancel(String orderNo) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (!OrderStatus.ORDER_STATUS_CANCEL.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!userSupport.isSuperUser()) {
            result.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return result;
        }

        //处理结算单
        //缓存查询到的结算单
        Map<Integer, StatementOrderDO> statementCache = new HashMap<>();
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                if (statementOrderDO == null) {
                    statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                    statementCache.put(statementOrderDO.getId(), statementOrderDO);
                }
                //处理结算单总金额
                statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE)));
                //处理结算租金押金金额
                statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
                //处理结算押金金额
                statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDetailDO.getStatementDetailDepositAmount()));
                //处理结算租金金额
                statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDetailDO.getStatementDetailRentAmount()));
                //处理结算单逾期金额
                statementOrderDO.setStatementOverdueAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDetailDO.getStatementDetailOverdueAmount()));
                //处理其他费用
                statementOrderDO.setStatementOtherAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDetailDO.getStatementDetailOtherAmount()));
                statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                statementOrderDetailMapper.update(statementOrderDetailDO);
            }
            for (Integer key : statementCache.keySet()) {
                StatementOrderDO statementOrderDO = statementCache.get(key);
                if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) == 0) {
                    statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                }
                statementOrderDO.setUpdateTime(currentTime);
                statementOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                statementOrderMapper.update(statementOrderDO);
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, String> addOrderMessage(Order order) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (order == null || StringUtil.isEmpty(order.getOrderNo())) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (OrderStatus.ORDER_STATUS_CANCEL.equals(orderDO.getOrderStatus()) || OrderStatus.ORDER_STATUS_OVER.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (StringUtil.isEmpty(order.getRemark())) {
            result.setErrorCode(ErrorCode.ORDER_MESSAGE_NULL);
            return result;
        }
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setCreateTime(currentTime);
        orderMessage.setUserId(loginUser.getUserId());
        orderMessage.setUserRealName(loginUser.getRealName());
        orderMessage.setContent(order.getRemark());
        List<OrderMessage> orderMessageList;
        if (StringUtil.isNotEmpty(orderDO.getOrderMessage())) {
            orderMessageList = JSON.parseArray(orderDO.getOrderMessage(), OrderMessage.class);
        } else {
            orderMessageList = new ArrayList<OrderMessage>();
        }
        orderMessageList.add(orderMessage);
        orderDO.setOrderMessage(JSON.toJSONString(orderMessageList));
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Order> createOrderFirstPayAmount(Order order) {

        ServiceResult<String, Order> result = new ServiceResult<>();
        Date currentTime = new Date();
        /*****组合商品 start*******/
        setCountForOrderJointProduct(order.getOrderJointProductList());
        buildOrderJointProduct(order);
        /*****组合商品 end*******/
        if ((order.getOrderProductList() == null || order.getOrderProductList().isEmpty()) && (order.getOrderMaterialList() == null || order.getOrderMaterialList().isEmpty())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        order.setBuyerCustomerId(customerDO.getId());

        if (order.getRentStartTime() == null) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_RENT_START_TIME);
            return result;
        }
        if (order.getRentType() == null) {
            result.setErrorCode(ErrorCode.ORDER_RENT_TYPE_IS_NULL);
            return result;
        }
        if (order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            result.setErrorCode(ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL);
            return result;
        }

        order.setRentLengthType(OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && order.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT);

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                orderProduct.setRentType(order.getRentType());
                orderProduct.setRentTimeLength(order.getRentTimeLength());
                orderProduct.setRentLengthType(order.getRentLengthType());
                if (orderProduct.getProductCount() == null || orderProduct.getProductCount() <= 0) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_COUNT_ERROR);
                    return result;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProduct.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode()) || productServiceResult.getResult() == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NOT_RENT);
                    return result;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                orderMaterial.setRentType(order.getRentType());
                orderMaterial.setRentTimeLength(order.getRentTimeLength());
                orderMaterial.setRentLengthType(order.getRentLengthType());
                if (orderMaterial.getMaterialCount() == null || orderMaterial.getMaterialCount() <= 0) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_COUNT_ERROR);
                    return result;
                }
                if (orderMaterial.getMaterialId() == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterial.getMaterialId());

                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode()) || materialServiceResult.getResult() == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                if (orderMaterial.getMaterialUnitAmount() == null || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_AMOUNT_ERROR);
                    return result;
                }
            }
        }

        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        SubCompanyDO subCompanyDO = subCompanyMapper.findById(orderDO.getOrderSubCompanyId());
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(generateNoSupport.generateOrderNo(currentTime, subCompanyDO != null ? subCompanyDO.getSubCompanyCode() : null));
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());
        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderDO.setStatementDate(customerDO.getStatementDate());
        order = ConverterUtil.convert(orderDO, Order.class);

        orderFirstNeedPayAmount(order, orderDO);

        /*****组合商品 start*******/
        rebuildOrderJointProductByIdentityNo(order);
        /*****组合商品 end*******/

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }

    // 根据订单商品项和配件项的IdentityNo来将其替换对应的组合商品项中商品和配件，并重置数量
    private void rebuildOrderJointProductByIdentityNo(Order order) {
        List<OrderJointProduct> orderJointProductList = order.getOrderJointProductList();
        List<OrderProduct> orderProductList = order.getOrderProductList();
        Map<Integer, OrderProduct> orderProductMap = ListUtil.listToMap(orderProductList, "identityNo");
        List<OrderMaterial> orderMaterialList = order.getOrderMaterialList();
        Map<Integer, OrderMaterial> orderMaterialMap = ListUtil.listToMap(orderMaterialList, "identityNo");
        if (CollectionUtil.isNotEmpty(orderJointProductList)) {
            List<OrderProduct> removeOrderProductList = new ArrayList<>();
            List<OrderMaterial> removeOrderMaterialList = new ArrayList<>();

            for (OrderJointProduct orderJointProduct :orderJointProductList) {
                List<OrderProduct> processedOrderProductList = new ArrayList<>(); // 处理后的
                List<OrderProduct> orderProductListForJoint = orderJointProduct.getOrderProductList();
                if (CollectionUtil.isNotEmpty(orderProductListForJoint)) {
                    for (OrderProduct orderProductForJoint : orderProductListForJoint) {
                        if (orderProductMap.get(orderProductForJoint.getIdentityNo()) != null) {
                            removeOrderProductList.add(orderProductMap.get(orderProductForJoint.getIdentityNo()));
                            processedOrderProductList.add(orderProductMap.get(orderProductForJoint.getIdentityNo()));
                        }
                    }
                }
                orderJointProduct.setOrderProductList(processedOrderProductList); // 放入处理后的商品

                List<OrderMaterial> processedOrderMaterialList = new ArrayList<>();
                List<OrderMaterial> orderMaterialListForJoint = orderJointProduct.getOrderMaterialList();
                if (CollectionUtil.isNotEmpty(orderMaterialListForJoint)) {
                    for (OrderMaterial orderMaterialForJoint : orderMaterialListForJoint) {
                        if (orderMaterialMap.get(orderMaterialForJoint.getIdentityNo()) != null) {
                            removeOrderMaterialList.add(orderMaterialMap.get(orderMaterialForJoint.getIdentityNo()));
                            processedOrderMaterialList.add(orderMaterialMap.get(orderMaterialForJoint.getIdentityNo()));
                        }
                    }
                }
                orderJointProduct.setOrderMaterialList(processedOrderMaterialList);
            }

            orderProductList.removeAll(removeOrderProductList);
            orderMaterialList.removeAll(removeOrderMaterialList);
        }
    }

    private String getErrorMessage(com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response, K3SendRecordDO k3SendRecordDO) {
        StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
        sb.append("向K3推送【取消订单-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }

    @Override
    public ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());

        //仅仅是为工作台查询可续租的订单服务
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderQueryParam.getIsCanReletOrder())){
            Map<String,Object> maps = new HashMap<>();
            maps.put("reletTimeOfDay",CommonConstant.RELET_TIME_OF_RENT_TYPE_DAY );
            maps.put("reletTimeOfMonth",CommonConstant.RELET_TIME_OF_RENT_TYPE_MONTH);
            maps.put("orderQueryParam",orderQueryParam);
            maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

            List<OrderDO> orderDOList = orderMapper.findCanReletOrderForWorkbench(maps);

            List<String> orderNoList = new ArrayList<>();
            Map<String, Order> orderDOMap = new HashMap<>();
            List<Order> canReletOrderList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(orderDOList)){
                for (OrderDO orderDO :orderDOList){
                    Order order = ConverterUtil.convert(orderDO, Order.class);
                    Integer canReletOrder = orderSupport.isOrderCanRelet(order);
                    order.setCanReletOrder(canReletOrder);
                    Integer isReletOrder = order.getReletOrderId() != null ? CommonConstant.YES : CommonConstant.NO;
                    order.setIsReletOrder(isReletOrder);
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(order.getCanReletOrder())){
                        canReletOrderList.add(order);
                    }
                }
            }

            List<Order> pageOrderList = new ArrayList<>();
            Integer startPage = orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - orderQueryParam.getPageSize() <= 0 ? 0:orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - orderQueryParam.getPageSize();
            if (CollectionUtil.isNotEmpty(canReletOrderList)){
                for (int i = startPage; i <= orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - 1; i++){
                    if (i <= canReletOrderList.size() - 1){
                        if(canReletOrderList.get(i) != null){
                            pageOrderList.add(canReletOrderList.get(i));
                            orderNoList.add(canReletOrderList.get(i).getOrderNo());
                            orderDOMap.put(canReletOrderList.get(i).getOrderNo(), canReletOrderList.get(i));
                        }
                    }
                }
            }

            List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.findByWorkflowTypeAndReferNoList(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderNoList);
            if (CollectionUtil.isNotEmpty(workflowLinkDOList)){
                for (WorkflowLinkDO workflowLinkDO : workflowLinkDOList) {
                    Order order = orderDOMap.get(workflowLinkDO.getWorkflowReferNo());
                    if (order != null) {
                        WorkflowLink workflowLink = ConverterUtil.convert(workflowLinkDO, WorkflowLink.class);
                        order.setWorkflowLink(workflowLink);
                    }
                }
            }

            Page<Order> page = new Page<>(pageOrderList, canReletOrderList.size(), orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("orderQueryParam", orderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        List<Order> orderList = new ArrayList<>();

        List<String> orderNoList = new ArrayList<>();
        Map<String, Order> orderDOMap = new HashMap<>();
        for (OrderDO orderDO : orderDOList) {
            orderNoList.add(orderDO.getOrderNo());
            Order order = ConverterUtil.convert(orderDO, Order.class);
            orderDOMap.put(orderDO.getOrderNo(), order);
            //判断是否可续租
            Integer canReletOrder = orderSupport.isOrderCanRelet(order);
            order.setCanReletOrder(canReletOrder);
            Integer isReletOrder = order.getReletOrderId() != null ? CommonConstant.YES : CommonConstant.NO;
            order.setIsReletOrder(isReletOrder);
            orderList.add(order);
        }
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.findByWorkflowTypeAndReferNoList(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderNoList);
        for (WorkflowLinkDO workflowLinkDO : workflowLinkDOList) {
            Order order = orderDOMap.get(workflowLinkDO.getWorkflowReferNo());
            if (order != null) {
                WorkflowLink workflowLink = ConverterUtil.convert(workflowLinkDO, WorkflowLink.class);
                order.setWorkflowLink(workflowLink);
            }
        }

        Page<Order> page = new Page<>(orderList, totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryOrderByUserId(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        orderQueryParam.setBuyerCustomerId(loginUser.getUserId());
        maps.put("orderQueryParam", orderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);

        List<Order> orderList = new ArrayList<>();
        for (OrderDO orderDO : orderDOList) {

            Order order = ConverterUtil.convert(orderDO, Order.class);
            //判断是否可续租
            Integer canReletOrder = orderSupport.isOrderCanRelet(order);
            order.setCanReletOrder(canReletOrder);
            Integer isReletOrder = order.getReletOrderId() != null ? CommonConstant.YES : CommonConstant.NO;
            order.setIsReletOrder(isReletOrder);
            orderList.add(order);
        }

        Page<Order> page = new Page<>(orderList, totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryOrderByUserIdInterface(InterfaceOrderQueryParam interfaceOrderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(interfaceOrderQueryParam.getPageNo(), interfaceOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        interfaceOrderQueryParam.setBuyerCustomerId(interfaceOrderQueryParam.getBuyerCustomerId());
        maps.put("orderQueryParam", interfaceOrderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        Page<Order> page = new Page<>(ConverterUtil.convertList(orderDOList, Order.class), totalCount, interfaceOrderQueryParam.getPageNo(), interfaceOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, List<Order>> queryAllOrderByCustomerNo(InterfaceOrderQueryByCustomerNoParam interfaceOrderQueryParam) {
        ServiceResult<String, List<Order>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("orderQueryParam", interfaceOrderQueryParam);
        List<OrderDO> orderDOList = orderMapper.findOrderByCustomerNo(maps);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ConverterUtil.convertList(orderDOList, Order.class));
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> processOrder(ProcessOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (param == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (StringUtil.isBlank(param.getEquipmentNo())
                && (param.getMaterialId() == null || param.getMaterialCount() == null)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        OrderDO orderDO = orderMapper.findByOrderNo(param.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_PROCESSING.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }

        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            ServiceResult<String, Object> addOrderItemResult = addOrderItem(orderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), param.getIsNewMaterial(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(addOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderItemResult.getErrorCode(), addOrderItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            ServiceResult<String, Object> removeOrderItemResult = removeOrderItem(orderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), param.getIsNewMaterial(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(removeOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(removeOrderItemResult.getErrorCode(), removeOrderItemResult.getFormatArgs());
                return result;
            }
        }

        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_PROCESSING);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);


        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.K3_DELIVER_CALLBACK);

        result.setResult(param.getOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> addOrderItem(OrderDO orderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer isNewMaterial, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        // 计算预计归还时间
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE, equipmentNo);
                return result;
            }
            WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(orderDO.getOrderSubCompanyId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return result;
            }
            currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                return result;
            }
            if (!productEquipmentDO.getCurrentWarehouseId().equals(currentWarehouse.getId())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE, equipmentNo, productEquipmentDO.getCurrentWarehouseId());
                return result;
            }

            boolean isMatching = false;
            Map<String, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "productSkuId", "rentType", "rentTimeLength", "isNewProduct");
            OrderProductDO matchingOrderProductDO = null;

            // 匹配SKU
            for (Map.Entry<String, OrderProductDO> entry : orderProductDOMap.entrySet()) {
                String key = entry.getKey();
                OrderProductDO orderProductDO = entry.getValue();
                // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
                if (key.startsWith(productEquipmentDO.getSkuId().toString()) && productEquipmentDO.getIsNew().equals(orderProductDO.getIsNewProduct())) {
                    matchingOrderProductDO = orderProductDO;
                    break;
                }
            }
            // 匹配商品
            if (matchingOrderProductDO == null) {
                for (Map.Entry<String, OrderProductDO> entry : orderProductDOMap.entrySet()) {
                    OrderProductDO orderProductDO = entry.getValue();
                    // 如果输入进来的设备productId,订单项中包含，就匹配 为当前订单项需要的，那么就匹配
                    if (orderProductDO.getProductId().equals(productEquipmentDO.getProductId()) && productEquipmentDO.getIsNew().equals(orderProductDO.getIsNewProduct())) {
                        matchingOrderProductDO = orderProductDO;
                        break;
                    }
                }
            }
            if (matchingOrderProductDO != null) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(matchingOrderProductDO.getId());
                // 如果这个订单项满了，那么就换下一个
                if (orderProductEquipmentDOList != null && orderProductEquipmentDOList.size() >= matchingOrderProductDO.getProductCount()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_MAX);
                    return result;
                }
                if (!productEquipmentDO.getIsNew().equals(matchingOrderProductDO.getIsNewProduct())) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NEW_NOT_MATCHING);
                    return result;
                }

                productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                productEquipmentDO.setOrderNo(orderDO.getOrderNo());
                productEquipmentDO.setUpdateTime(currentTime);
                productEquipmentDO.setUpdateUser(loginUserId.toString());
                productEquipmentMapper.update(productEquipmentDO);

                // 将该设备上的物料统一加上订单号
                bulkMaterialMapper.updateEquipmentOrderNo(productEquipmentDO.getEquipmentNo(), orderDO.getOrderNo());

                BigDecimal expectRentAmount = calculationOrderExpectRentAmount(matchingOrderProductDO.getProductUnitAmount(), matchingOrderProductDO.getRentType(), matchingOrderProductDO.getRentTimeLength());
                Date expectReturnTime = orderSupport.calculationOrderExpectReturnTime(orderDO.getRentStartTime(), matchingOrderProductDO.getRentType(), matchingOrderProductDO.getRentTimeLength());
                OrderProductEquipmentDO orderProductEquipmentDO = new OrderProductEquipmentDO();
                orderProductEquipmentDO.setOrderId(matchingOrderProductDO.getOrderId());
                orderProductEquipmentDO.setOrderProductId(matchingOrderProductDO.getId());
                orderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                orderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                orderProductEquipmentDO.setRentStartTime(orderDO.getRentStartTime());
                orderProductEquipmentDO.setProductEquipmentUnitAmount(matchingOrderProductDO.getProductUnitAmount());
                orderProductEquipmentDO.setExpectReturnTime(expectReturnTime);
                orderProductEquipmentDO.setExpectRentAmount(expectRentAmount);
                orderProductEquipmentDO.setActualRentAmount(BigDecimal.ZERO);
                orderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductEquipmentDO.setCreateTime(currentTime);
                orderProductEquipmentDO.setCreateUser(loginUserId.toString());
                orderProductEquipmentDO.setUpdateTime(currentTime);
                orderProductEquipmentDO.setUpdateUser(loginUserId.toString());
                orderProductEquipmentMapper.save(orderProductEquipmentDO);
                isMatching = true;
            }

            if (!isMatching) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }

            String operateSkuStockResult = productSupport.operateSkuStock(productEquipmentDO.getSkuId(), -1);
            if (!ErrorCode.SUCCESS.equals(operateSkuStockResult)) {
                result.setErrorCode(operateSkuStockResult);
                return result;
            }
        }

        if (materialId != null) {
            boolean isMatching = false;
            Map<String, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId", "rentType", "rentTimeLength", "isNewMaterial");
            for (Map.Entry<String, OrderMaterialDO> entry : orderMaterialDOMap.entrySet()) {
                String key = entry.getKey();
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(key);
                // 如果输入进来的散料ID 为当前订单项需要的，那么就匹配
                if (key.startsWith(materialId.toString()) && orderMaterialDO.getIsNewMaterial().equals(isNewMaterial)) {
                    //已经配好的
                    List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                    if (orderMaterialBulkDOList != null && orderMaterialBulkDOList.size() >= orderMaterialDO.getMaterialCount()) {
                        continue;
                    }

                    WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(orderDO.getOrderSubCompanyId());
                    if (currentWarehouse == null) {
                        result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                        return result;
                    }
                    currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
                    if (currentWarehouse == null) {
                        result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                        return result;
                    }

                    // 必须是当前库房闲置的物料
                    List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(currentWarehouse.getId(), materialId, materialCount, orderMaterialDO.getIsNewMaterial());
                    if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                        result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                        return result;
                    }

                    for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList) {
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                        bulkMaterialDO.setOrderNo(orderDO.getOrderNo());
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setUpdateUser(loginUserId.toString());
                        bulkMaterialMapper.update(bulkMaterialDO);

                        BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                        Date expectReturnTime = orderSupport.calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                        OrderMaterialBulkDO orderMaterialBulkDO = new OrderMaterialBulkDO();
                        orderMaterialBulkDO.setOrderId(orderMaterialDO.getOrderId());
                        orderMaterialBulkDO.setOrderMaterialId(orderMaterialDO.getId());
                        orderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
                        orderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                        orderMaterialBulkDO.setMaterialBulkUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                        orderMaterialBulkDO.setRentStartTime(orderDO.getRentStartTime());
                        orderMaterialBulkDO.setExpectReturnTime(expectReturnTime);
                        orderMaterialBulkDO.setExpectRentAmount(expectRentAmount);
                        orderMaterialBulkDO.setActualRentAmount(BigDecimal.ZERO);
                        orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        orderMaterialBulkDO.setCreateTime(currentTime);
                        orderMaterialBulkDO.setCreateUser(loginUserId.toString());
                        orderMaterialBulkDO.setUpdateTime(currentTime);
                        orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                        orderMaterialBulkMapper.save(orderMaterialBulkDO);
                    }
                    isMatching = true;
                    break;
                }
            }
            if (!isMatching) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, materialId);
                return result;
            }

            String operateMaterialStockResult = materialSupport.operateMaterialStock(materialId, (materialCount * -1));
            if (!ErrorCode.SUCCESS.equals(operateMaterialStockResult)) {
                result.setErrorCode(operateMaterialStockResult);
                return result;
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> removeOrderItem(OrderDO orderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer isNewMaterial, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_BUSY, equipmentNo);
                return result;
            }
            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), productEquipmentDO.getEquipmentNo());
            if (orderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }

            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setOrderNo("");
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setUpdateUser(loginUserId.toString());
            productEquipmentMapper.update(productEquipmentDO);

            bulkMaterialMapper.updateEquipmentOrderNo(productEquipmentDO.getEquipmentNo(), "");

            orderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            orderProductEquipmentDO.setUpdateTime(currentTime);
            orderProductEquipmentDO.setUpdateUser(loginUserId.toString());
            orderProductEquipmentMapper.update(orderProductEquipmentDO);

            String operateSkuStockResult = productSupport.operateSkuStock(productEquipmentDO.getSkuId(), 1);
            if (!ErrorCode.SUCCESS.equals(operateSkuStockResult)) {
                result.setErrorCode(operateSkuStockResult);
                return result;
            }
        }

        if (materialId != null) {
            Map<String, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId", "rentType", "rentTimeLength", "isNewMaterial");
            for (Map.Entry<String, OrderMaterialDO> entry : orderMaterialDOMap.entrySet()) {
                String key = entry.getKey();
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(key);
                // 如果输入进来的散料ID 为当前订单项需要的，那么就匹配
                if (key.startsWith(materialId.toString()) && orderMaterialDO.getIsNewMaterial().equals(isNewMaterial)) {
                    List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                    if (CollectionUtil.isEmpty(orderMaterialBulkDOList)) {
                        result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, materialId);
                        return result;
                    }

                    if (orderMaterialBulkDOList.size() < materialCount) {
                        result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                        return result;
                    }
                    for (int i = 0; i < materialCount; i++) {
                        OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkDOList.get(i);
                        BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(orderMaterialBulkDO.getBulkMaterialNo());
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                        bulkMaterialDO.setOrderNo("");
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setUpdateUser(loginUserId.toString());
                        bulkMaterialMapper.update(bulkMaterialDO);

                        orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                        orderMaterialBulkDO.setUpdateTime(currentTime);
                        orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                        orderMaterialBulkMapper.update(orderMaterialBulkDO);
                    }

                    String operateMaterialStockResult = materialSupport.operateMaterialStock(materialId, materialCount);
                    if (!ErrorCode.SUCCESS.equals(operateMaterialStockResult)) {
                        result.setErrorCode(operateMaterialStockResult);
                        return result;
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> deliveryOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        OrderDO dbRecordOrder = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbRecordOrder == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_PROCESSING.equals(dbRecordOrder.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_NOT_PROCESSED);
            return result;
        }
        Integer payType = null;
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderMaterialDOList()) && !OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType)) {
            for (OrderMaterialDO orderMaterialDO : dbRecordOrder.getOrderMaterialDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderMaterialDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType) &&
                !PayStatus.PAY_STATUS_PAID.equals(dbRecordOrder.getPayStatus())
                && !PayStatus.PAY_STATUS_PAID_PART.equals(dbRecordOrder.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
            return result;
        }
        if (BigDecimalUtil.compare(dbRecordOrder.getFirstNeedPayAmount(), BigDecimal.ZERO) > 0 &&
                !PayStatus.PAY_STATUS_PAID.equals(dbRecordOrder.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
            return result;
        }

        if (DateUtil.getBeginOfDay(currentTime).getTime() < DateUtil.dateInterval(DateUtil.getBeginOfDay(dbRecordOrder.getRentStartTime()), -2).getTime()) {
            result.setErrorCode(ErrorCode.ORDER_CAN_NOT_DELIVERY_TIME_REASON);
            return result;
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
                if (orderProductEquipmentDOList == null || orderProductDO.getProductCount() != orderProductEquipmentDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
                    return result;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : dbRecordOrder.getOrderMaterialDOList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                if (orderMaterialBulkDOList == null || orderMaterialDO.getMaterialCount() != orderMaterialBulkDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_BULK_MATERIAL_COUNT_ERROR);
                    return result;
                }
            }
        }

        dbRecordOrder.setDeliveryTime(currentTime);
        dbRecordOrder.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        dbRecordOrder.setUpdateUser(loginUser.getUserId().toString());
        dbRecordOrder.setUpdateTime(currentTime);
        orderMapper.update(dbRecordOrder);

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(dbRecordOrder.getId(), dbRecordOrder.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.K3_DELIVER_CALLBACK);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order.getOrderNo());
        return result;
    }



    private BigDecimal calculationOrderExpectRentAmount(BigDecimal unitAmount, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        }
        return null;
    }

    @Override
    public void verifyCustomerRiskInfo(OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        boolean isCheckRiskManagement = isCheckRiskManagement(orderDO);
        if (isCheckRiskManagement) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())
                        && orderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                    if (!productServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    Product product = productServiceResult.getResult();
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);

                    if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(skuPrice, customerRiskManagementDO.getSingleLimitPrice()) > 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }

                    Integer depositCycle, payCycle, payMode;
                    boolean productIsCheckRiskManagement = isCheckRiskManagement(orderProductDO, null);
                    if (!productIsCheckRiskManagement) {
                        if (orderProductDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        orderProductDO.setDepositCycle(0);
                        orderProductDO.setPaymentCycle(0);
                        continue;
                    }
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else {
                        // 查看客户风控信息是否齐全
                        /*if (!customerSupport.isFullRiskManagement(orderDO.getBuyerCustomerId())) {
                            throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_FULL);
                        }*/
                        if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) {
                            // 商品品牌为苹果品牌
                            depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                            payCycle = customerRiskManagementDO.getApplePaymentCycle();
                            payMode = customerRiskManagementDO.getApplePayMode();
                        } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                            depositCycle = customerRiskManagementDO.getNewDepositCycle();
                            payCycle = customerRiskManagementDO.getNewPaymentCycle();
                            payMode = customerRiskManagementDO.getNewPayMode();
                        } else {
                            depositCycle = customerRiskManagementDO.getDepositCycle();
                            payCycle = customerRiskManagementDO.getPaymentCycle();
                            payMode = customerRiskManagementDO.getPayMode();
                        }
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                        payCycle = payCycle > orderProductDO.getRentTimeLength() ? orderProductDO.getRentTimeLength() : payCycle;
                    }
                    orderProductDO.setDepositCycle(depositCycle);
                    orderProductDO.setPaymentCycle(payCycle);
                    orderProductDO.setPayMode(payMode);
                } else {
                    if (orderProductDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProductDO.getPayMode())) {
                        orderProductDO.setDepositCycle(0);
                        orderProductDO.setPaymentCycle(0);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())
                        && orderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                    Material material = materialResult.getResult();
                    if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(materialPrice, customerRiskManagementDO.getSingleLimitPrice()) >= 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean materialIsCheckRiskManagement = isCheckRiskManagement(null, orderMaterialDO);
                    if (!materialIsCheckRiskManagement) {
                        if (orderMaterialDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        orderMaterialDO.setDepositCycle(0);
                        orderMaterialDO.setPaymentCycle(0);
                        continue;
                    }
                    Integer depositCycle, payCycle, payMode;
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId())) {
                        // 商品品牌为苹果品牌
                        depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                        payCycle = customerRiskManagementDO.getApplePaymentCycle();
                        payMode = customerRiskManagementDO.getApplePayMode();
                    } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                        depositCycle = customerRiskManagementDO.getNewDepositCycle();
                        payCycle = customerRiskManagementDO.getNewPaymentCycle();
                        payMode = customerRiskManagementDO.getNewPayMode();
                    } else {
                        depositCycle = customerRiskManagementDO.getDepositCycle();
                        payCycle = customerRiskManagementDO.getPaymentCycle();
                        payMode = customerRiskManagementDO.getPayMode();
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                        depositCycle = depositCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : depositCycle;
                        payCycle = payCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : payCycle;
                    }
                    orderMaterialDO.setDepositCycle(depositCycle);
                    orderMaterialDO.setPaymentCycle(payCycle);
                    orderMaterialDO.setPayMode(payMode);
                } else {
                    if (orderMaterialDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterialDO.getPayMode())) {
                        orderMaterialDO.setDepositCycle(0);
                        orderMaterialDO.setPaymentCycle(0);
                    }

                }
            }
        }
    }

    private boolean isCheckRiskManagement(OrderProductDO orderProductDO, OrderMaterialDO orderMaterialDO) {
        if (orderProductDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())
                    && orderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                return true;
            }
        }
        if (orderMaterialDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())
                    && orderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCheckRiskManagement(OrderDO orderDO) {

        BigDecimal totalProductAmount = BigDecimal.ZERO;
        BigDecimal totalMaterialAmount = BigDecimal.ZERO;
        Integer totalProductCount = 0;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                boolean isCheckRiskManagement = isCheckRiskManagement(orderProductDO, null);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    totalProductAmount = BigDecimalUtil.add(totalProductAmount, BigDecimalUtil.mul(new BigDecimal(orderProductDO.getProductCount()), skuPrice));
                    totalProductCount += orderProductDO.getProductCount();
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {

                boolean isCheckRiskManagement = isCheckRiskManagement(null, orderMaterialDO);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Material> materialResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                if (ErrorCode.SUCCESS.equals(materialResult.getErrorCode())) {
                    Material material = materialResult.getResult();
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    totalMaterialAmount = BigDecimalUtil.add(totalMaterialAmount, BigDecimalUtil.mul(new BigDecimal(orderMaterialDO.getMaterialCount()), materialPrice));
                }
            }
        }
        BigDecimal totalAmount = BigDecimalUtil.add(totalProductAmount, totalMaterialAmount);
        if (totalProductCount >= CommonConstant.ORDER_NEED_VERIFY_PRODUCT_COUNT || BigDecimalUtil.compare(totalAmount, CommonConstant.ORDER_NEED_VERIFY_PRODUCT_AMOUNT) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public ServiceResult<String, String> confirmOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();

        Date currentTime = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            logger.info("未查询到交易订单{}相关信息", orderNo);
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        // 判断订单状态，如果状态已经终结，就不用再继续了
        Integer orderState = orderDO.getOrderStatus();
        if (orderState == null || !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderState)) {
            logger.error("交易订单{}状态为{}，不能确认收货", orderNo, orderState);
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }

        orderDO.setConfirmDeliveryTime(currentTime);
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CONFIRM);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(),OperationType.COMFIRM_ORDER);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Page<OrderProduct>> queryOrderProductInfo(OrderQueryProductParam queryProductParam) {
        ServiceResult<String, Page<OrderProduct>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(queryProductParam.getPageNo(), queryProductParam.getPageSize());
        //根据orderNo获取orderId
        if (StringUtil.isNotEmpty(queryProductParam.getOrderNo())) {
            OrderDO orderDO = orderMapper.findByOrderNo(queryProductParam.getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(new Page<OrderProduct>());
                return result;
            } else {
                queryProductParam.setOrderId(orderDO.getId());
            }
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryProductParam", queryProductParam);

        Integer totalCount = orderProductMapper.findOrderProductCountByParams(maps);
        List<OrderProductDO> orderProductDOList = orderProductMapper.findOrderProductByParams(maps);
        List<OrderProduct> productSkuList = ConverterUtil.convertList(orderProductDOList, OrderProduct.class);
        for (OrderProduct orderProduct : productSkuList) {
            Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
            for (ProductSku productSku : product.getProductSkuList()) {
                if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                    orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                    break;
                }
            }
        }
        Page<OrderProduct> page = new Page<>(productSkuList, totalCount, queryProductParam.getPageNo(), queryProductParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    // 保存订单组合商品项
    // 在保存新增的组合商品项时，会根据identityNo找到orderDO中这个组合商品对应的商品或配件，把组合商品项id设置进去，所以此方法应在保存订单商品配件之前
    private void saveOrderJointProductInfo(List<OrderJointProductDO> orderJointProductDOList, OrderDO orderDO, User loginUser, Date currentTime) {
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        Map<Integer, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderProductDOList, "identityNo");
        Map<Integer, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderMaterialDOList, "identityNo");

        List<OrderJointProductDO> saveOrderJointProductDOList = new ArrayList<>();
        List<OrderJointProductDO> updateOrderJointProductDOList = new ArrayList<>();
        List<OrderJointProductDO> dbOrderJointProductDOList = orderJointProductMapper.findByOrderId(orderDO.getId());
        Map<Integer, OrderJointProductDO> dbOrderJointProductDOMap = ListUtil.listToMap(dbOrderJointProductDOList, "id");
        if (CollectionUtil.isNotEmpty(orderJointProductDOList)) {
            for (OrderJointProductDO orderJointProductDO : orderJointProductDOList) {
                OrderJointProductDO dbOrderJointProductDO = dbOrderJointProductDOMap.get(orderJointProductDO.getId());
                if (dbOrderJointProductDO != null) {
                    updateOrderJointProductDOList.add(orderJointProductDO);
                    dbOrderJointProductDOMap.remove(orderJointProductDO.getId());
                } else {
                    saveOrderJointProductDOList.add(orderJointProductDO);
                }
            }
        }

        if (saveOrderJointProductDOList.size() > 0) {
            for (OrderJointProductDO orderJointProductDO : saveOrderJointProductDOList) {
                orderJointProductDO.setOrderId(orderDO.getId());
                orderJointProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderJointProductDO.setCreateUser(loginUser.getUserId().toString());
                orderJointProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderJointProductDO.setCreateTime(currentTime);
                orderJointProductDO.setUpdateTime(currentTime);
                orderJointProductMapper.save(orderJointProductDO);

                // 新增的保存后要根据IdentityNo找到orderDO中对应的组合商品商品和配件，并设置orderJointProductId
                List<OrderProductDO> orderProductDOListForJoint = orderJointProductDO.getOrderProductDOList();
                if (CollectionUtil.isNotEmpty(orderProductDOListForJoint)) {
                    for (OrderProductDO orderProductDOForJoint : orderProductDOListForJoint) {
                        OrderProductDO orderProductDO = orderProductDOMap.get(orderProductDOForJoint.getIdentityNo());
                        if (orderProductDO != null) {
                            orderProductDO.setOrderJointProductId(orderJointProductDO.getId());
                        }
                    }
                }

                List<OrderMaterialDO> orderMaterialDOListForJoint = orderJointProductDO.getOrderMaterialDOList();
                if (CollectionUtil.isNotEmpty(orderMaterialDOListForJoint)) {
                    for (OrderMaterialDO orderMaterialDOForJoint : orderMaterialDOListForJoint) {
                        OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(orderMaterialDOForJoint.getIdentityNo());
                        if (orderMaterialDO != null) {
                            orderMaterialDO.setOrderJointProductId(orderJointProductDO.getId());
                        }
                    }
                }
            }
        }

        if (updateOrderJointProductDOList.size() > 0) {
            for (OrderJointProductDO orderJointProductDO : updateOrderJointProductDOList) {
                orderJointProductDO.setOrderId(orderDO.getId());
                orderJointProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderJointProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderJointProductDO.setUpdateTime(currentTime);
                orderJointProductMapper.update(orderJointProductDO);
            }
        }

        if (dbOrderJointProductDOMap.size() > 0 ) {
            for (Map.Entry<Integer, OrderJointProductDO> entry : dbOrderJointProductDOMap.entrySet()) {
                OrderJointProductDO orderJointProductDO = entry.getValue();
                orderJointProductDO.setOrderId(orderDO.getId());
                orderJointProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderJointProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderJointProductDO.setUpdateTime(currentTime);
                orderJointProductMapper.update(orderJointProductDO);
            }
        }
    }

    @Override
    public void saveOrderProductInfo(List<OrderProductDO> orderProductDOList, Integer orderId, User loginUser, Date currentTime) {

        List<OrderProductDO> saveOrderProductDOList = new ArrayList<>();
        Map<Integer, OrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<OrderProductDO> dbOrderProductDOList = orderProductMapper.findByOrderId(orderId);
        Map<Integer, OrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "id");
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                OrderProductDO dbOrderProductDO = dbOrderProductDOMap.get(orderProductDO.getId());
                if (dbOrderProductDO != null) {
                    orderProductDO.setId(dbOrderProductDO.getId());
                    updateOrderProductDOMap.put(orderProductDO.getId(), orderProductDO);
                    dbOrderProductDOMap.remove(orderProductDO.getId());
                } else {
                    saveOrderProductDOList.add(orderProductDO);
                }
            }
        }

        if (saveOrderProductDOList.size() > 0) {
            for (OrderProductDO orderProductDO : saveOrderProductDOList) {
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setCreateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setCreateTime(currentTime);
                orderProductDO.setUpdateTime(currentTime);
                orderProductDO.setStableProductCount(orderProductDO.getProductCount());
                orderProductMapper.save(orderProductDO);
            }
        }

        if (updateOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderProductDO> entry : updateOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                orderProductDO.setStableProductCount(orderProductDO.getProductCount());
                orderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                orderProductDO.setStableProductCount(orderProductDO.getProductCount());
                orderProductMapper.update(orderProductDO);
            }
        }
    }

    @Override
    public void saveOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, Integer orderId, User loginUser, Date currentTime) {

        List<OrderMaterialDO> saveOrderMaterialDOList = new ArrayList<>();
        Map<Integer, OrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<OrderMaterialDO> dbOrderMaterialDOList = orderMaterialMapper.findByOrderId(orderId);
        Map<Integer, OrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                OrderMaterialDO dbOrderMaterialDO = dbOrderMaterialDOMap.get(orderMaterialDO.getId());
                if (dbOrderMaterialDO != null) {
                    orderMaterialDO.setId(dbOrderMaterialDO.getId());
                    updateOrderMaterialDOMap.put(orderMaterialDO.getId(), orderMaterialDO);
                    dbOrderMaterialDOMap.remove(orderMaterialDO.getId());
                } else {
                    saveOrderMaterialDOList.add(orderMaterialDO);
                }
            }
        }

        if (saveOrderMaterialDOList.size() > 0) {
            for (OrderMaterialDO orderMaterialDO : saveOrderMaterialDOList) {
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setCreateTime(currentTime);
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialDO.setStableMaterialCount(orderMaterialDO.getMaterialCount());
                orderMaterialMapper.save(orderMaterialDO);
            }
        }

        if (updateOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderMaterialDO> entry : updateOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialDO.setStableMaterialCount(orderMaterialDO.getMaterialCount());
                orderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialDO.setStableMaterialCount(orderMaterialDO.getMaterialCount());
                orderMaterialMapper.update(orderMaterialDO);
            }
        }
    }



    private void updateOrderConsignInfo(Integer userConsignId, Integer orderId, User loginUser, Date currentTime) {
        CustomerConsignInfoDO userConsignInfoDO = customerConsignInfoMapper.findById(userConsignId);
        OrderConsignInfoDO dbOrderConsignInfoDO = orderConsignInfoMapper.findByOrderId(orderId);
        OrderConsignInfoDO orderConsignInfoDO = new OrderConsignInfoDO();
        orderConsignInfoDO.setOrderId(orderId);
        orderConsignInfoDO.setCustomerConsignId(userConsignId);
        orderConsignInfoDO.setConsigneeName(userConsignInfoDO.getConsigneeName());
        orderConsignInfoDO.setConsigneePhone(userConsignInfoDO.getConsigneePhone());
        orderConsignInfoDO.setProvince(userConsignInfoDO.getProvince());
        orderConsignInfoDO.setCity(userConsignInfoDO.getCity());
        orderConsignInfoDO.setDistrict(userConsignInfoDO.getDistrict());
        orderConsignInfoDO.setAddress(userConsignInfoDO.getAddress());
        orderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);

        if (dbOrderConsignInfoDO == null) {
            orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            orderConsignInfoDO.setCreateTime(currentTime);
            orderConsignInfoDO.setUpdateTime(currentTime);
            orderConsignInfoMapper.save(orderConsignInfoDO);
        } else {
            if (!dbOrderConsignInfoDO.getCustomerConsignId().equals(userConsignId)) {
                dbOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                dbOrderConsignInfoDO.setId(dbOrderConsignInfoDO.getId());
                dbOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                dbOrderConsignInfoDO.setUpdateTime(currentTime);
                orderConsignInfoMapper.update(dbOrderConsignInfoDO);

                orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setCreateTime(currentTime);
                orderConsignInfoDO.setUpdateTime(currentTime);
                orderConsignInfoMapper.save(orderConsignInfoDO);
            }
        }
    }

    @Override
    public void calculateOrderProductInfo(List<OrderProductDO> orderProductDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);
            // 保险总额
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            // 设备该交押金总额
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = new BigDecimal(0.0);

            for (OrderProductDO orderProductDO : orderProductDOList) {

                String skuName = "";

                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;
                BigDecimal skuPrice = BigDecimal.ZERO;
                Product product = new Product();
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {

                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                        throw new BusinessException(productServiceResult.getErrorCode());
                    }
                    product = productServiceResult.getResult();
                    orderProductDO.setProductName(product.getProductName());
                    ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                    if (productSku == null) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    skuName = productSku.getSkuName();
                }


                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && orderProductDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (orderProductDO.getProductCount()>0) {
                        BigDecimal remainder = orderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderProductDO.getProductCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                        }
                    }
                    depositAmount = orderProductDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                        Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount()), 2), new BigDecimal(depositCycle));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    } else {
                        Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount()), 2), new BigDecimal(depositCycle));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    }
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }


                orderProductDO.setRentDepositAmount(rentDepositAmount);
                orderProductDO.setDepositAmount(depositAmount);
                orderProductDO.setCreditDepositAmount(creditDepositAmount);
                orderProductDO.setProductSkuName(skuName);
                orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getRentTimeLength()), 2), new BigDecimal(orderProductDO.getProductCount())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                BigDecimal thisOrderProductInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getInsuranceAmount(), new BigDecimal(orderProductDO.getRentTimeLength()), 2), new BigDecimal(orderProductDO.getProductCount()));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderProductInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalProductDepositAmount(totalDepositAmount);
            orderDO.setTotalProductCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalProductRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(productAmountTotal);
        }else {
            orderDO.setTotalProductDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductCreditDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductRentDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductCount(CommonConstant.COMMON_ZERO);
            orderDO.setTotalProductAmount(BigDecimal.ZERO);
        }
    }

    @Override
    public void calculateOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (orderMaterialDOList != null && !orderMaterialDOList.isEmpty()) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = BigDecimal.ZERO;
            // 保险总额
            BigDecimal totalInsuranceAmount = BigDecimal.ZERO;
            // 设备该交押金总额
            BigDecimal totalDepositAmount = BigDecimal.ZERO;
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = BigDecimal.ZERO;
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = BigDecimal.ZERO;
            BigDecimal materialPrice = BigDecimal.ZERO;
            BigDecimal depositAmount = BigDecimal.ZERO;
            BigDecimal creditDepositAmount = BigDecimal.ZERO;
            BigDecimal rentDepositAmount = BigDecimal.ZERO;
            String materialName = "";
            Material material = new Material();

            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                    ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                    if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                        throw new BusinessException(materialServiceResult.getErrorCode());
                    }
                    material = materialServiceResult.getResult();
                    if (material == null) {
                        throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                    }
                    orderMaterialDO.setMaterialName(material.getMaterialName());
                    materialName = material.getMaterialName();
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                }

                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && orderMaterialDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (orderMaterialDO.getMaterialCount()>0) {
                        BigDecimal remainder = orderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderMaterialDO.getMaterialCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                        }
                    }

                    depositAmount = orderMaterialDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    }

                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    }
                }

                orderMaterialDO.setRentDepositAmount(rentDepositAmount);
                orderMaterialDO.setDepositAmount(depositAmount);
                orderMaterialDO.setCreditDepositAmount(creditDepositAmount);
                orderMaterialDO.setMaterialName(StringUtil.isBlank(materialName) ? orderMaterialDO.getMaterialName() : materialName);
                orderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getRentTimeLength()), 2), new BigDecimal(orderMaterialDO.getMaterialCount())));
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                BigDecimal thisOrderMaterialInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getInsuranceAmount(), new BigDecimal(orderMaterialDO.getRentTimeLength()), 2), new BigDecimal(orderMaterialDO.getMaterialCount()));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderMaterialInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalMaterialDepositAmount(totalDepositAmount);
            orderDO.setTotalMaterialCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalMaterialRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalMaterialCount(materialCount);
            orderDO.setTotalMaterialAmount(materialAmountTotal);
        }else {
            orderDO.setTotalMaterialDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialCreditDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialRentDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialCount(CommonConstant.COMMON_ZERO);
            orderDO.setTotalMaterialAmount(BigDecimal.ZERO);
        }
    }

    /**
     * 当前用户待审核订单分页
     *
     * @param verifyOrderQueryParam
     * @return
     * @Author : sunzhipeng
     */
    @Override
    public ServiceResult<String, Page<Order>> queryVerifyOrder(VerifyOrderQueryParam verifyOrderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(verifyOrderQueryParam.getPageNo(), verifyOrderQueryParam.getPageSize());
        Integer currentVerifyUser = userSupport.getCurrentUserId();
        List<String> workflowReferNoList = workflowLinkMapper.findWorkflowReferNoList(currentVerifyUser);
        if (workflowReferNoList.size() == 0) {
            List<Order> orderList = new ArrayList<>();
            Page<Order> page = new Page<>(orderList, 0, verifyOrderQueryParam.getPageNo(), verifyOrderQueryParam.getPageSize());
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("verifyOrderQueryParam", verifyOrderQueryParam);
        maps.put("workflowReferNoList", workflowReferNoList);
        Integer totalCount = orderMapper.findVerifyOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findVerifyOrderByParams(maps);
        List<Order> orderList = ConverterUtil.convertList(orderDOList, Order.class);
        Page<Order> page = new Page<>(orderList, totalCount, verifyOrderQueryParam.getPageNo(), verifyOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, String> addReturnOrderToTimeAxis() {
        ServiceResult<String, String> result = new ServiceResult<>();
        //部分退货
        List<OrderDO> orderDOList = orderMapper.findByOrderStatus(OrderStatus.ORDER_STATUS_PART_RETURN);
        if (CollectionUtil.isEmpty(orderDOList)) orderDOList = new ArrayList<OrderDO>();
        //全部退货
        List<OrderDO> odList = orderMapper.findByOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK);
        if (CollectionUtil.isNotEmpty(odList)) orderDOList.addAll(odList);
        if (CollectionUtil.isNotEmpty(orderDOList)) {
            Date currentTime = new Date();
            Integer userId = userSupport.getCurrentUserId();
            for (OrderDO orderDO : orderDOList) {
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, userId,OperationType.K3_RETURN_CALLBACK);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    private String verifyOperateOrder(Order order) {
        if (order == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if ((order.getOrderProductList() == null || order.getOrderProductList().isEmpty())
                && (order.getOrderMaterialList() == null || order.getOrderMaterialList().isEmpty())) {
            return ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL;
        }
        if (order.getCustomerConsignId() == null) {
            return ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL;
        }
        if (order.getDeliveryMode() == null) {
            return ErrorCode.ORDER_DELIVERY_MODE_IS_NULL;
        }
        if (!DeliveryMode.inThisScope(order.getDeliveryMode())) {
            return ErrorCode.ORDER_DELIVERY_MODE_ERROR;
        }
        if (order.getIsPeer() == null) {
            return ErrorCode.ORDER_ISPEER_NOT_NULL;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }
        order.setBuyerCustomerId(customerDO.getId());

        // 判断逾期情况，如果客户存在未支付的逾期的结算单，不能产生新订单
        List<StatementOrderDO> overdueStatementOrderList = statementOrderSupport.getOverdueStatementOrderList(customerDO.getId());


        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(order.getCustomerConsignId());
        if (customerConsignInfoDO == null || !customerConsignInfoDO.getCustomerId().equals(customerDO.getId())) {
            return ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS;
        }
        if (order.getRentStartTime() == null) {
            return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
        }
        //测试放开起租时间限制
        try {
            if (order.getRentStartTime().getTime() < new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-03-01 00:00:00").getTime()) {
                return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
            }
        } catch (Exception e) {
            return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
        }
        if (order.getExpectDeliveryTime() == null) {
            return ErrorCode.ORDER_EXPECT_DELIVERY_TIME;
        }
        Integer deliveryBetweenDays = com.lxzl.erp.common.util.DateUtil.daysBetween(order.getExpectDeliveryTime(), order.getRentStartTime());
        if (deliveryBetweenDays < 0 || deliveryBetweenDays > 2) {
            return ErrorCode.ORDER_RENT_START_TIME_ERROR;
        }
        if (order.getRentType() == null) {
            return ErrorCode.ORDER_RENT_TYPE_IS_NULL;
        }
        if (order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            return ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL;
        }
        if (!OrderRentType.inThisScope(order.getRentType())) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        order.setRentLengthType(OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && order.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT);

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            Map<String, OrderProduct> orderProductMap = new HashMap<>();
            int oldProductCount = 0, newProductCount = 0;
            Map<Integer, Integer> productNewStockMap = new HashMap<>();
            Map<Integer, Integer> productOldStockMap = new HashMap<>();
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                orderProduct.setRentType(order.getRentType());
                orderProduct.setRentTimeLength(order.getRentTimeLength());
                orderProduct.setRentLengthType(order.getRentLengthType());

                if (orderProduct.getProductCount() == null || orderProduct.getProductCount() <= 0) {
                    return ErrorCode.ORDER_PRODUCT_COUNT_ERROR;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProduct.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode()) || productServiceResult.getResult() == null) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    return ErrorCode.PRODUCT_IS_NOT_RENT;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
                }
                if (productNewStockMap.get(product.getProductId()) == null) {
                    productNewStockMap.put(product.getProductId(), product.getNewProductCount());
                }
                if (productOldStockMap.get(product.getProductId()) == null) {
                    productOldStockMap.put(product.getProductId(), product.getOldProductCount());
                }
                oldProductCount = productOldStockMap.get(product.getProductId());
                newProductCount = productNewStockMap.get(product.getProductId());

                // 订单商品库存先去掉
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProduct.getIsNewProduct())) {
                    if ((newProductCount - orderProduct.getProductCount()) < 0) {
                        return ErrorCode.ORDER_PRODUCT_STOCK_NEW_INSUFFICIENT;
                    } else {
                        newProductCount = newProductCount - orderProduct.getProductCount();
                        productNewStockMap.put(product.getProductId(), newProductCount);
                    }
                } else {
                    if ((oldProductCount - orderProduct.getProductCount()) < 0) {
                        return ErrorCode.ORDER_PRODUCT_STOCK_OLD_INSUFFICIENT;
                    } else {
                        oldProductCount = oldProductCount - orderProduct.getProductCount();
                        productOldStockMap.put(product.getProductId(), oldProductCount);
                    }
                }*/

//                String key = orderProduct.getProductSkuId() + "-" + orderProduct.getRentType() + "-" + orderProduct.getRentTimeLength() + "-" + orderProduct.getIsNewProduct();
//                if (orderProductMap.get(key) != null) {
//                    return ErrorCode.ORDER_PRODUCT_LIST_REPEAT;
//                } else {
//                    orderProductMap.put(key, orderProduct);
//                }

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                Integer rentLengthType = OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType()) && orderProduct.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT;
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(rentLengthType)
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            Map<String, OrderMaterial> orderMaterialMap = new HashMap<>();
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                orderMaterial.setRentType(order.getRentType());
                orderMaterial.setRentTimeLength(order.getRentTimeLength());
                orderMaterial.setRentLengthType(order.getRentLengthType());
                if (orderMaterial.getMaterialCount() == null || orderMaterial.getMaterialCount() <= 0) {
                    return ErrorCode.ORDER_MATERIAL_COUNT_ERROR;
                }
                if (orderMaterial.getMaterialId() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterial.getMaterialId());

                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())
                        || materialServiceResult.getResult() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                if (orderMaterial.getMaterialUnitAmount() == null || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.ORDER_MATERIAL_AMOUNT_ERROR;
                }
                Material material = materialServiceResult.getResult();
                // 配件库存判断先去掉
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())) {
                    if (material == null || material.getNewMaterialCount() == null || material.getNewMaterialCount() <= 0 || (material.getNewMaterialCount() - orderMaterial.getMaterialCount()) < 0) {
                        return ErrorCode.ORDER_MATERIAL_STOCK_NEW_INSUFFICIENT;
                    }
                } else {
                    if (material == null || material.getOldMaterialCount() == null || material.getOldMaterialCount() <= 0 || (material.getOldMaterialCount() - orderMaterial.getMaterialCount()) < 0) {
                        return ErrorCode.ORDER_MATERIAL_STOCK_OLD_INSUFFICIENT;
                    }
                }*/
//                String key = orderMaterial.getMaterialId() + "-" + orderMaterial.getRentType() + "-" + orderMaterial.getRentTimeLength() + "-" + orderMaterial.getIsNewMaterial();
//                if (orderMaterialMap.get(key) != null) {
//                    return ErrorCode.ORDER_MATERIAL_LIST_REPEAT;
//                } else {
//                    orderMaterialMap.put(key, orderMaterial);
//                }

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(order.getRentLengthType())
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }
        return verifyOrderShortRentReceivable(customerDO, ConverterUtil.convert(order, OrderDO.class));
    }

    String verifyOrderShortRentReceivable(CustomerDO customerDO, OrderDO orderDO) {

        Integer subCompanyId = orderDO.getOrderSubCompanyId();
        subCompanyId = subCompanyId == null ? userSupport.getCurrentUserCompanyId() : subCompanyId;

        BigDecimal customerTotalShortRentReceivable = statementOrderSupport.getShortRentReceivable(customerDO.getId());
        //分公司的应收短期上线
        BigDecimal subCompanyTotalShortRentReceivable = statementOrderSupport.getSubCompanyShortRentReceivable(subCompanyId);
        if (BigDecimalUtil.compare(subCompanyTotalShortRentReceivable, BigDecimal.ZERO) < 0) {
            return ErrorCode.SHORT_RECEIVABLE_CALCULATE_FAIL;
        }
        //得到分公司设置的短期上线
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        BigDecimal shortLimitReceivableAmount = customerDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : customerDO.getShortLimitReceivableAmount();
        BigDecimal subCompanyShortLimitReceivableAmount = subCompanyDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : subCompanyDO.getShortLimitReceivableAmount();

        BigDecimal otherAmount = orderDO.getLogisticsAmount();
        customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, otherAmount);
        subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, otherAmount);


        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(orderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderProductDO.getProductCount()), orderProductDO.getProductUnitAmount()), new BigDecimal(orderDO.getRentTimeLength()));
                    thisTotalAmount = BigDecimalUtil.add(thisTotalAmount, orderProductDO.getDepositAmount());
                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(orderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderMaterialDO.getMaterialCount()), orderMaterialDO.getMaterialUnitAmount()), new BigDecimal(orderDO.getRentTimeLength()));
                    thisTotalAmount = BigDecimalUtil.add(thisTotalAmount, orderMaterialDO.getDepositAmount());
                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * 审核注意事项
     *
     * @param orderDO
     * @return
     */
    private ServiceResult<String, String> getVerifyMatters(OrderDO orderDO) {
        ServiceResult<String, String> result = new ServiceResult<>();

        String verifyProduct = null;
        BigDecimal productPrice = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            Integer count = 1;
            OrderProductDO orderProductDO;
            for (int i = 0; i < orderDO.getOrderProductDOList().size(); i++) {
                orderProductDO = orderDO.getOrderProductDOList().get(i);
                ProductSkuDO productSkuDO = productSkuMapper.findById(orderProductDO.getProductSkuId());
                if (productSkuDO == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //得到
                if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSkuDO.getNewDayRentPrice() : productSkuDO.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSkuDO.getNewMonthRentPrice() : productSkuDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productPrice) < 0) {
                    if (verifyProduct == null) {
                        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：天租，次新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        } else {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：月租，全新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：月租，次新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        }
                    } else {
                        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：天租，次新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        } else {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    verifyProduct + count + "；租赁方式：月租，全新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    verifyProduct + count + "；租赁方式：月租，次新。商品名称：【" + orderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        }
                    }
                    count++;
                }
            }
        }

        String verifyMaterial = null;
        BigDecimal materialPrice = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            Integer count = 1;
            OrderMaterialDO orderMaterialDO;
            for (int i = 0; i < orderDO.getOrderMaterialDOList().size(); i++) {
                orderMaterialDO = orderDO.getOrderMaterialDOList().get(i);
                MaterialDO materialDO = materialMapper.findById(orderMaterialDO.getMaterialId());
                if (materialDO == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? materialDO.getNewDayRentPrice() : materialDO.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? materialDO.getNewMonthRentPrice() : materialDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), materialPrice) < 0) {
                    if (verifyMaterial == null) {
                        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：天租，全新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    "配件项：" + count + "；租赁方式：天租，次新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        } else {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：月租，全新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    "配件项：" + count + "；租赁方式：月租，次新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        }
                    } else {
                        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：天租，全新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    verifyMaterial + count + "；租赁方式：天租，次新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        } else {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：月租，全新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    verifyMaterial + count + "；租赁方式：月租，次新。【配件名称：" + orderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(orderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        }
                    }
                    count++;
                }
            }
        }
        String verifyMatters;
        if (verifyProduct == null) {
            verifyMatters = verifyMaterial;
        } else if (verifyMaterial == null) {
            verifyMatters = verifyProduct;
        } else {
            verifyMatters = verifyProduct + verifyMaterial;
        }

        result.setResult(verifyMatters);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private Order orderFirstNeedPayAmount(Order order, OrderDO orderDO) {
        if (orderDO.getFirstNeedPayAmount() == null || BigDecimalUtil.compare(orderDO.getFirstNeedPayAmount(), BigDecimal.ZERO) == 0) {
            ServiceResult<String, Map<String, BigDecimal>> firstNeedPayAmountResult = statementService.calculateOrderFirstNeedPayAmount(orderDO);
            Map<String, BigDecimal> map = firstNeedPayAmountResult.getResult();
            if (ErrorCode.SUCCESS.equals(firstNeedPayAmountResult.getErrorCode())) {
                order.setFirstNeedPayAmount(map.get("thisNeedPayAmount,ALL"));

                BigDecimal totalProductDeposit = BigDecimal.ZERO;
                BigDecimal totalProductRent = BigDecimal.ZERO;
                OrderProduct orderProduct;
                for (int i = 0; i < order.getOrderProductList().size(); i++) {
                    orderProduct = order.getOrderProductList().get(i);
                    String ItemName = orderProduct.getProductName() + orderProduct.getProductSkuName() + "-" + orderProduct.getIsNewProduct() + "-" + orderProduct.getOrderProductId() + "-" + orderProduct.getOrderId() + "-" + orderProduct.getSerialNumber();

                    orderProduct.setFirstNeedPayAmount(map.get(ItemName));
                    orderProduct.setFirstNeedPayRentAmount(map.get(ItemName));

                    BigDecimal firstNeedPayDepositAmount = BigDecimalUtil.add(orderProduct.getRentDepositAmount(), orderProduct.getDepositAmount());
                    orderProduct.setFirstNeedPayDepositAmount(firstNeedPayDepositAmount);
                    totalProductDeposit = BigDecimalUtil.add(totalProductDeposit, firstNeedPayDepositAmount);
                    totalProductRent = BigDecimalUtil.add(totalProductRent, orderProduct.getFirstNeedPayRentAmount());
                }
                order.setTotalProductFirstNeedPayAmount(BigDecimalUtil.add(totalProductDeposit, totalProductRent));

                BigDecimal totalMaterialDeposit = BigDecimal.ZERO;
                BigDecimal totalMaterialRent = BigDecimal.ZERO;
                OrderMaterial orderMaterial;
                for (int i = 0; i < order.getOrderMaterialList().size(); i++) {
                    orderMaterial = order.getOrderMaterialList().get(i);
                    String ItemName = orderMaterial.getMaterialName() + "-" + orderMaterial.getIsNewMaterial() + "-" + orderMaterial.getOrderMaterialId() + "-" + orderMaterial.getOrderId() + "-" + orderMaterial.getSerialNumber();

                    order.getOrderMaterialList().get(i).setFirstNeedPayAmount(map.get(ItemName));
                    order.getOrderMaterialList().get(i).setFirstNeedPayRentAmount(map.get(ItemName));

                    BigDecimal firstNeedPayDepositAmount = BigDecimalUtil.add(orderMaterial.getRentDepositAmount(), orderMaterial.getDepositAmount());
                    orderMaterial.setFirstNeedPayDepositAmount(firstNeedPayDepositAmount);
                    totalMaterialDeposit = BigDecimalUtil.add(totalMaterialDeposit, firstNeedPayDepositAmount);
                    totalMaterialRent = BigDecimalUtil.add(totalMaterialRent, orderMaterial.getFirstNeedPayRentAmount());
                }
                order.setTotalMaterialFirstNeedPayAmount(BigDecimalUtil.add(totalMaterialDeposit, totalMaterialRent));
            }
        }
        return order;
    }



    @Autowired
    private UserSupport userSupport;

    @Autowired
    private AmountSupport amountSupport;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderOperationLogMapper orderOperationLogMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private StatementService statementService;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;

    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CustomerSupport customerSupport;

    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;

    @Autowired
    private WarehouseSupport warehouseSupport;

    @Autowired
    private ProductSupport productSupport;

    @Autowired
    private MaterialSupport materialSupport;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private WebServiceHelper webServiceHelper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private DingDingSupport dingDingSupport;

    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private CouponSupport couponSupport;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderJointProductMapper orderJointProductMapper;

    @Autowired
    private JointProductMapper jointProductMapper;

    @Autowired
    private JointProductProductMapper jointProductProductMapper;

    @Autowired
    private JointMaterialMapper jointMaterialMapper;

    @Autowired
    private OrderConfirmChangeLogMapper orderConfirmChangeLogMapper;

    @Autowired
    private OrderConfirmChangeLogDetailMapper orderConfirmChangeLogDetailMapper;

    @Autowired
    private ImgMysqlMapper imgMysqlMapper;

    @Autowired
    private MessageThirdChannelService messageThirdChannelService;

    @Autowired
    private K3Service k3Service;

    @Autowired
    private OrderSupport orderSupport;

    @Autowired
    private OrderStatementDateSplitMapper  orderStatementDateSplitMapper;
}
