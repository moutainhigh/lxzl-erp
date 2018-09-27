package com.lxzl.erp.core.service.export.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.ReturnReasonType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.payment.CustomerAccountLogParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementMapContainer;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementSummaryDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.rent.CheckStatementDetailRentProductDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent.CheckStatementDetailReplaceProductDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent.CheckStatementDetailUnReplaceProductDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.unrent.*;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.export.*;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3OrderStatementConfigMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3OrderStatementConfigDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/6/19
 * @Time : Created in 14:46
 */
@Service
public class ExportExcelCustomFormatServiceImpl implements ExportExcelCustomFormatService {

    @Autowired
    private StatementService statementService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private ReletOrderMaterialMapper reletOrderMaterialMapper;

    @Autowired
    private ReletOrderProductMapper reletOrderProductMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private K3OrderStatementConfigMapper k3OrderStatementConfigMapper;

    @Autowired
    private ProductSupport productSupport;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;

    @Autowired
    private ReplaceOrderProductMapper replaceOrderProductMapper;

    @Autowired
    private ReplaceOrderMaterialMapper replaceOrderMaterialMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ServiceResult<String, String> queryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam, HttpServletResponse response) throws Exception {
        return queryStatementOrderCheckParamNew(statementOrderMonthQueryParam, response);
    }

    /**
     * 导出对账单数据
     */
    private ServiceResult<String, String> queryStatementOrderCheckParamNew(StatementOrderMonthQueryParam queryParam, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult();
        // 1：根据客户编号获取该客户指定时间段内的结算单详情列表信息(结算单起始时间和结束时间都需要考虑)
        CustomerDO customerDO = customerMapper.findByNo(queryParam.getStatementOrderCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        queryParam.setCustomerId(customerDO.getId());
        // 2：构建结算单
        ServiceResult<String, List<BaseCheckStatementDetailDTO>> serviceResultOfCheckStatementDetail = getCheckStatementDetailDatas(queryParam);
        if (!Objects.equals(serviceResultOfCheckStatementDetail.getErrorCode(), ErrorCode.SUCCESS)) {
            serviceResult.setErrorCode(serviceResultOfCheckStatementDetail.getErrorCode(), serviceResultOfCheckStatementDetail.getFormatArgs());
            return serviceResult;
        }
        // 3：获取对账单详情列表数据
        List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS = serviceResultOfCheckStatementDetail.getResult();
        // 结算单映射容器
        CheckStatementMapContainer mapContainer = new CheckStatementMapContainer();
        // 获取单价为0的租赁商品和租赁订单信息
        buildPriceForZeroRentProductAndOrder(queryParam, mapContainer);
        // 4：构建对账单详情列表数据
        buildCheckStatementDetailDatas(checkStatementDetailDTOS, mapContainer);
        logger.info("构建对象单详情后的数据为：" + JSONObject.toJSONString(checkStatementDetailDTOS));
        // 5：获取月份结算单统计对象的映射map
        Map<Date, CheckStatementStatisticsDTO> monthStatisticsMap = getMonthStatisticsMap(queryParam);
        // 6：根据月份对对账单详情列表进行分组
        groupCheckStatementDetailsByMonth(monthStatisticsMap, checkStatementDetailDTOS, customerDO, mapContainer);
        logger.info("分组后的数据为：" + JSONObject.toJSONString(monthStatisticsMap));
        // 过滤对账单统计数据
//        filterStatementStatisticsData(monthStatisticsMap, mapContainer, queryParam);
        // 7：构建统计数据
        buildStatisticsData(monthStatisticsMap, queryParam);
        // 8：导出统计数据
        logger.info("导出的数据为：" + JSONObject.toJSONString(monthStatisticsMap));
        importCheckStatementStatisticsData(monthStatisticsMap, response);
        serviceResult.setResult(JSONObject.toJSONString(monthStatisticsMap));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /** 构建单价为0的租赁商品和租赁订单信息  */
    private void buildPriceForZeroRentProductAndOrder(StatementOrderMonthQueryParam queryParam,  CheckStatementMapContainer mapContainer) {
        List<Order> orders = ConverterUtil.convertList(orderMapper.listByMonthQuery(queryParam), Order.class);
        if (CollectionUtil.isNotEmpty(orders)) {
            Set<Integer> orderIds = new LinkedHashSet<>();
            for (Order order : orders) {
                orderIds.add(order.getOrderId());
            }
            Map<String, Object> productQueryMap = new HashMap<>();
            productQueryMap.put("orderIds", orderIds);
            productQueryMap.put("productUnitAmount", BigDecimal.ZERO);
            List<OrderProduct> orderProducts = ConverterUtil.convertList(orderProductMapper.listByOrderIds(productQueryMap), OrderProduct.class);
            mapContainer.addIdOrderMap(orders);
            mapContainer.addIdOrderProductMap(orderProducts);
        }
    }

    /**
     * 获取对账单详情数据列表信息
     */
    private ServiceResult<String, List<BaseCheckStatementDetailDTO>> getCheckStatementDetailDatas(StatementOrderMonthQueryParam statementOrderMonthQueryParam) {
        ServiceResult<String, List<BaseCheckStatementDetailDTO>> serviceResult = new ServiceResult<>();
        // 查询该用户退还时间在指定时间段内退货单列表数据
        List<K3ReturnOrderDO> k3ReturnOrderDOS = k3ReturnOrderMapper.listByMonthQuery(statementOrderMonthQueryParam);
        Set<Integer> returnOrderIds = new LinkedHashSet<>();
        for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOS) {
            returnOrderIds.add(k3ReturnOrderDO.getId());
        }
        // 查询该用户确认换货时间在指定时间段内换货单列表数据
        List<ReplaceOrderDO> replaceOrderDOS = replaceOrderMapper.listByMonthQuery(statementOrderMonthQueryParam);
        Set<Integer> replaceOrderIds = new LinkedHashSet<>();
        for (ReplaceOrderDO replaceOrderDO : replaceOrderDOS) {
            replaceOrderIds.add(replaceOrderDO.getId());
        }
        //orderIds IS 退货单id
        statementOrderMonthQueryParam.setOrderIds(returnOrderIds);
        //replaceOrderIds is 换货单id
        statementOrderMonthQueryParam.setReplaceOrderIds(replaceOrderIds);
        // 根据查询条件获取结算单列表数据
        ServiceResult<String, List<BaseCheckStatementDetailDTO>> serviceResultOfCheckStatementDetail = statementService.listCheckStatementDetailDTOByQuery(statementOrderMonthQueryParam);
        if (!Objects.equals(serviceResultOfCheckStatementDetail.getErrorCode(), ErrorCode.SUCCESS)) {
            serviceResult.setErrorCode(serviceResultOfCheckStatementDetail.getErrorCode(), serviceResultOfCheckStatementDetail.getFormatArgs());
            return serviceResult;
        }
        return serviceResultOfCheckStatementDetail;
    }

    /**
     * 构建对账单详情数据列表
     */
    private void buildCheckStatementDetailDatas(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS, CheckStatementMapContainer mapContainer) {

        // 构建退货单数据和退货详情单数据
        buildK3ReturnOrder(checkStatementDetailDTOS, mapContainer);
        Map<Integer, Set<Integer>> orderIdsUseOrderTypeGroup = getOrderIdsUseOrderTypeGroupNew(checkStatementDetailDTOS);
        // 构建租赁订单数据
        buildRentOrder(orderIdsUseOrderTypeGroup, mapContainer);
        // 获取符合条件的订单列表数据
        Set<Integer> orderIds = orderIdsUseOrderTypeGroup.get(OrderType.ORDER_TYPE_ORDER);
        // 构建订单项数据
        buildRentOrderItem(orderIds, mapContainer);
        // 构建续租订单相关数据
        buildReletOrder(orderIds, mapContainer);
        // todo 构建换货单相关数据
        buildReplaceOrder(orderIds, mapContainer,checkStatementDetailDTOS);

        for (BaseCheckStatementDetailDTO checkStatementDetailDTO : checkStatementDetailDTOS) {
            checkStatementDetailDTO.mapContainer(mapContainer).build();
        }
    }

    /**
     * 构建退货单和退货详情单数据
     */
    private void buildK3ReturnOrder(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS, CheckStatementMapContainer checkStatementMapContainer) {
        Set<Integer> returnOrderIds = new LinkedHashSet<>();
        for (BaseCheckStatementDetailDTO detailRentDTO : checkStatementDetailDTOS) {
            if (CheckStatementUtil.isReturnOrder(detailRentDTO)) {
                returnOrderIds.add(detailRentDTO.getOrderId());
            }
        }
        List<K3ReturnOrder> k3ReturnOrders = new ArrayList<>();
        List<K3ReturnOrderDetail> k3ReturnOrderDetails = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(returnOrderIds)) {
            k3ReturnOrders = ConverterUtil.convertList(k3ReturnOrderMapper.listByIds(returnOrderIds), K3ReturnOrder.class);
            // 退货单详情列表数据
            k3ReturnOrderDetails = ConverterUtil.convertList(k3ReturnOrderDetailMapper.listByReturnOrderIds(returnOrderIds), K3ReturnOrderDetail.class);
        }
        checkStatementMapContainer
                .idK3ReturnOrderMap(k3ReturnOrders)
                .idK3ReturnOrderDetailMap(k3ReturnOrderDetails);
    }

    /**
     * 构建租赁订单数据
     */
    private void buildRentOrder(Map<Integer, Set<Integer>> orderIdsUseOrderTypeGroup, CheckStatementMapContainer mapContainer) {
        Set<String> orderNos = new LinkedHashSet<>();
        for (K3ReturnOrderDetail k3ReturnOrderDetail : mapContainer.getIdK3ReturnOrderDetailMap().values()) {
            orderNos.add(k3ReturnOrderDetail.getOrderNo());
        }
        // 2 构建引用订单id列表信息（按照类型分为租赁订单（erp_order）、退货订单（erp_k3_return_order）、续租订单（erp_relet_order））
        List<Order> orders = new ArrayList<>();
        // 获取符合条件的订单列表数据

        Set<Integer> orderIds = orderIdsUseOrderTypeGroup.get(OrderType.ORDER_TYPE_ORDER);
        if (CollectionUtil.isNotEmpty(orderIds)) {
            List<Order> ordersByIds = ConverterUtil.convertList(orderMapper.listByIds(orderIds), Order.class);
            orders.addAll(ordersByIds);
        }
        if (CollectionUtil.isNotEmpty(orderNos)) {
            List<Order> ordersByNos = ConverterUtil.convertList(orderMapper.listByOrderNOs(orderNos), Order.class);
            orders.addAll(ordersByNos);
        }
        mapContainer.idOrderMap(orders)
                .noOrderMap(orders);
    }

    /**
     * 构建租赁订单项数据
     */
    private void buildRentOrderItem(Set<Integer> orderIds, CheckStatementMapContainer mapContainer) {
        Map<String, Object> productQueryMap = new HashMap<>();
        productQueryMap.put("orderIds", orderIds);
        List<OrderProduct> orderProducts = ConverterUtil.convertList(orderProductMapper.listByOrderIds(productQueryMap), OrderProduct.class);
        List<OrderMaterial> orderMaterials = ConverterUtil.convertList(orderMaterialMapper.listByOrderIds(orderIds), OrderMaterial.class);
        mapContainer.idOrderProductMap(orderProducts)
                .idOrderMaterialMap(orderMaterials);
    }

    /**
     * 构建续租订单数据
     */
    private void buildReletOrder(Set<Integer> orderIds, CheckStatementMapContainer mapContainer) {
        List<ReletOrder> reletOrders = ConverterUtil.convertList(reletOrderMapper.listByOrderIds(orderIds), ReletOrder.class);
        List<ReletOrderProduct> reletOrderProducts = ConverterUtil.convertList(reletOrderProductMapper.listByOrderIds(orderIds), ReletOrderProduct.class);
        List<ReletOrderMaterial> reletOrderMaterials = ConverterUtil.convertList(reletOrderMaterialMapper.listByOrderIds(orderIds), ReletOrderMaterial.class);
        mapContainer
                .idReletOrderMap(reletOrders)
                .idReletOrderProductMap(reletOrderProducts)
                .idReletOrderMaterialMap(reletOrderMaterials);
    }

    /**
     * 构建换货订单数据
     */
    private void buildReplaceOrder(Set<Integer> orderIds, CheckStatementMapContainer mapContainer,List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS) {
        List<ReplaceOrder> replaceOrders = ConverterUtil.convertList(replaceOrderMapper.listByOrderIds(orderIds), ReplaceOrder.class);

        Set<Integer> replaceIds = new HashSet<>();
        if(CollectionUtil.isNotEmpty(replaceOrders)){
            for(ReplaceOrder replaceOrder : replaceOrders){
                replaceIds.add(replaceOrder.getReplaceOrderId());
            }

            List<ReplaceOrderProduct> replaceOrderProducts = ConverterUtil.convertList(replaceOrderProductMapper.listByOrderIds(replaceIds), ReplaceOrderProduct.class);
            List<ReplaceOrderMaterial> replaceOrderMaterials = ConverterUtil.convertList(replaceOrderMaterialMapper.listByOrderIds(replaceIds), ReplaceOrderMaterial.class);

            for(ReplaceOrderProduct replaceOrderProduct : replaceOrderProducts){
                for(BaseCheckStatementDetailDTO baseCheckStatementDetailDTO : checkStatementDetailDTOS){
                    if(OrderType.ORDER_TYPE_REPLACE.equals(baseCheckStatementDetailDTO.getOrderType())){
                        if(OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(baseCheckStatementDetailDTO.getOrderItemType())){
                            if(replaceOrderProduct.getOldOrderProductId().equals(baseCheckStatementDetailDTO.getOrderItemReferId())){
                                baseCheckStatementDetailDTO.setReplaceItemId(replaceOrderProduct.getReplaceOrderProductId());
//                            baseCheckStatementDetailDTO.setItemId(replaceOrderProduct.getReplaceOrderProductId());
                            }
                        }

                        if(OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(baseCheckStatementDetailDTO.getOrderItemType())){
                            if(replaceOrderProduct.getNewOrderProductId().equals(baseCheckStatementDetailDTO.getOrderItemReferId())){
                                baseCheckStatementDetailDTO.setReplaceItemId(replaceOrderProduct.getReplaceOrderProductId());
//                            baseCheckStatementDetailDTO.setItemId(replaceOrderProduct.getReplaceOrderProductId());
                            }
                        }
                    }
                }
            }

            for(ReplaceOrderMaterial replaceOrderMaterial : replaceOrderMaterials){
                for(BaseCheckStatementDetailDTO baseCheckStatementDetailDTO : checkStatementDetailDTOS){
                    if(OrderType.ORDER_TYPE_REPLACE.equals(baseCheckStatementDetailDTO.getOrderType())){
                        if(OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(baseCheckStatementDetailDTO.getOrderItemType())){
                            if(replaceOrderMaterial.getOldOrderMaterialId().equals(baseCheckStatementDetailDTO.getOrderItemReferId())){
                                baseCheckStatementDetailDTO.setReplaceItemId(replaceOrderMaterial.getReplaceOrderMaterialId());
                            }
                        }

                        if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(baseCheckStatementDetailDTO.getOrderItemType())){
                            if(replaceOrderMaterial.getNewOrderMaterialId().equals(baseCheckStatementDetailDTO.getOrderItemReferId())){
                                baseCheckStatementDetailDTO.setReplaceItemId(replaceOrderMaterial.getReplaceOrderMaterialId());
                            }
                        }
                    }
                }
            }

            mapContainer
                    .idReplaceOrderMap(replaceOrders)
                    .idReplaceOrderProductMap(replaceOrderProducts)
                    .idReplaceOrderMaterialMap(replaceOrderMaterials);
        }
    }

    /**
     * 获取根据订单类型分组的订单id列表或者退货单id列表
     */
    private Map<Integer, Set<Integer>> getOrderIdsUseOrderTypeGroupNew(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS) {
        Map<Integer, Set<Integer>> orderIdsUserOrderTypeGroup = Maps.newHashMap();
        orderIdsUserOrderTypeGroup.put(OrderType.ORDER_TYPE_ORDER, new HashSet<Integer>());
        orderIdsUserOrderTypeGroup.put(OrderType.ORDER_TYPE_RETURN, new HashSet<Integer>());
        for (BaseCheckStatementDetailDTO checkStatementDetailDTO : checkStatementDetailDTOS) {
            Integer orderType = checkStatementDetailDTO.getOrderType();
            if (orderIdsUserOrderTypeGroup.containsKey(orderType)) {
                orderIdsUserOrderTypeGroup.get(orderType).add(checkStatementDetailDTO.getOrderId());
            }
        }
        return orderIdsUserOrderTypeGroup;
    }

    /**
     * 获取月份和结算单统计的map对象
     */
    private Map<Date, CheckStatementStatisticsDTO> getMonthStatisticsMap(StatementOrderMonthQueryParam queryParam) {
        // 按月分组
        Map<Date, CheckStatementStatisticsDTO> monthStatisticsMap = Maps.newLinkedHashMap();
        Date startTime = queryParam.getStatementOrderStartTime();
        Date endTime = queryParam.getStatementOrderEndTime();
        List<Date> monthsBetween = DateUtil.getMonthBetween(startTime, endTime);
        for (Date month : monthsBetween) {
            monthStatisticsMap.put(month, new CheckStatementStatisticsDTO(month));
        }
        return monthStatisticsMap;
    }

    /**
     * 根据月份对对账单详情列表进行分组
     */
    private void groupCheckStatementDetailsByMonth(Map<Date, CheckStatementStatisticsDTO> monthStatisticsMap, List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS, CustomerDO customerDO, CheckStatementMapContainer mapContainer) {
        // 添加没有结算单的退租商品结算单数据
        createAndAddUnitAmountIsZeroUnRentProductOrders(checkStatementDetailDTOS, mapContainer);
        // 添加没有结算单的租赁商品结算单数据
        createAndAddUnitAmountIsZeroRentProductOrders(checkStatementDetailDTOS, mapContainer);
        // 分组并统计月数据
        for (Date month : monthStatisticsMap.keySet()) {
            CheckStatementStatisticsDTO statementStatisticsDTO = monthStatisticsMap.get(month);
            statementStatisticsDTO.setCustomerName(customerDO.getCustomerName());
            // 根据退货时间创建并新增退货订单数据
            createAndAddUnRentOrdersByReturnTime(mapContainer, statementStatisticsDTO, checkStatementDetailDTOS);
            // TODO 根据换货时间创建并新增换货数据
            createReplaceOrdersByReturnTime(mapContainer, statementStatisticsDTO, checkStatementDetailDTOS);
            // 处理租赁、续租结算单
            for (BaseCheckStatementDetailDTO checkStatementDetailDTO : checkStatementDetailDTOS) {
                if (checkStatementDetailDTO.isAddTheMonth(statementStatisticsDTO)) {
                    statementStatisticsDTO.addCheckStatementDetailDTO(checkStatementDetailDTO.clone());
                }
            }
        }
    }

    private void addReturnOrderData(CheckStatementStatisticsDTO statementStatisticsDTO, Map<String, List<BaseCheckStatementDetailDTO>> orderNoDetailsMap) {
        // 退货结算单处理
        for (List<BaseCheckStatementDetailDTO> detailDTOS : orderNoDetailsMap.values()) {
            // 按照期数升序排列
            Collections.sort(detailDTOS, new Comparator<BaseCheckStatementDetailDTO>() {
                @Override
                public int compare(BaseCheckStatementDetailDTO o1, BaseCheckStatementDetailDTO o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    return (int) ((o1.getStatementStartTime().getTime() - o2.getStatementStartTime().getTime()) / 1000);
                }
            });
            String statementStartTimeMonthDay = null;
            String statementUnrentStartTime = null;
            for (BaseCheckStatementDetailDTO detailDTO : detailDTOS) {
                if (detailDTO.isAddTheMonth(statementStatisticsDTO)) {
                    // 其他退货单直接添加
                    if (detailDTO instanceof CheckStatementDetailUnRentOtherDTO) {
                        statementStatisticsDTO.addCheckStatementDetailDTO(detailDTO);
                    } else if (detailDTO instanceof CheckStatementDetailUnRentDTO) {
                        // 租赁退货
                        if (statementStartTimeMonthDay == null) {
                            statementStartTimeMonthDay = DateFormatUtils.format(detailDTO.getStatementStartTime(), "yyyyMMdd");
                        }
                        if (statementStartTimeMonthDay.equals(DateFormatUtils.format(detailDTO.getStatementStartTime(), "yyyyMMdd"))) {
                            statementStatisticsDTO.addCheckStatementDetailDTO(detailDTO);
                        }
                    } else if (detailDTO instanceof CheckStatementDetailUnRentReletDTO) {
                        // 续租退货
                        if (statementUnrentStartTime == null) {
                            statementUnrentStartTime = DateFormatUtils.format(detailDTO.getStatementStartTime(), "yyyyMMdd");
                        }
                        if (statementUnrentStartTime.equals(DateFormatUtils.format(detailDTO.getStatementStartTime(), "yyyyMMdd"))) {
                            statementStatisticsDTO.addCheckStatementDetailDTO(detailDTO);
                        }
                    }
                }

            }
        }
    }


    private void createAndAddUnRentOrdersByReturnTime(CheckStatementMapContainer mapContainer, CheckStatementStatisticsDTO statisticsDTO, List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS) {
        Map<Integer, K3ReturnOrder> k3ReturnOrderMap = mapContainer.getIdK3ReturnOrderMap();
        Map<Integer, OrderProduct> orderProductMap = mapContainer.getIdOrderProductMap();
        Map<Integer, OrderMaterial> orderMaterialMap = mapContainer.getIdOrderMaterialMap();
        Map<String, Order> orderMap = mapContainer.getNoOrderMap();
        for (K3ReturnOrderDetail k3ReturnOrderDetail : mapContainer.getIdK3ReturnOrderDetailMap().values()) {
            Order order = orderMap.get(k3ReturnOrderDetail.getOrderNo());
            if (order == null) {
                continue;
            }
            String returnTimeMonthStr = DateFormatUtils.format(k3ReturnOrderMap.get(k3ReturnOrderDetail.getReturnOrderId()).getReturnTime(), "yyyy-MM");
            if (!returnTimeMonthStr.equals(statisticsDTO.getMonth())) {
                continue;
            }
            if (productSupport.isProduct(k3ReturnOrderDetail.getProductNo())) {
                OrderProduct orderProduct = orderProductMap.get(Integer.valueOf(k3ReturnOrderDetail.getOrderItemId()));
                BigDecimal unitAmount = orderProduct.getProductUnitAmount();
                if (unitAmount == null || BigDecimal.ZERO.compareTo(unitAmount) == 0) {
                    continue;
                }
                boolean isReletReturnOrder = false;
                BaseCheckStatementDetailDTO baseCheckStatementDetailDTO = null;
                for (BaseCheckStatementDetailDTO statementDetailDTO : checkStatementDetailDTOS) {
                    boolean isReletReturnOrderTemp = k3ReturnOrderDetail.getK3ReturnOrderDetailId().equals(statementDetailDTO.getOrderItemReferId());
                    baseCheckStatementDetailDTO = statementDetailDTO;
                    if (statementDetailDTO instanceof CheckStatementDetailUnRentReletProductDTO && isReletReturnOrderTemp) {
                        isReletReturnOrder = true;
                        break;
                    }
                }
                if (isReletReturnOrder) {
                    continue;
                }
                statisticsDTO.addCheckStatementDetailDTO(createReturnProductStatementDTO(k3ReturnOrderDetail, orderProduct, order, mapContainer,baseCheckStatementDetailDTO));
            } else if (productSupport.isMaterial(k3ReturnOrderDetail.getProductNo())) {
                OrderMaterial orderMaterial = orderMaterialMap.get(Integer.valueOf(k3ReturnOrderDetail.getOrderItemId()));
                BigDecimal unitAmount = orderMaterial.getMaterialUnitAmount();
                if (unitAmount == null || BigDecimal.ZERO.compareTo(unitAmount) == 0) {
                    continue;
                }
                boolean isReletReturnOrder = false;
                BaseCheckStatementDetailDTO baseCheckStatementDetailDTO = null;
                for (BaseCheckStatementDetailDTO statementDetailDTO : checkStatementDetailDTOS) {
                    boolean isReletReturnOrderTemp = k3ReturnOrderDetail.getK3ReturnOrderDetailId().equals(statementDetailDTO.getOrderItemReferId());
                    baseCheckStatementDetailDTO = statementDetailDTO;
                    if (statementDetailDTO instanceof CheckStatementDetailUnRentReletMaterialDTO && isReletReturnOrderTemp) {
                        isReletReturnOrder = true;
                        break;
                    }
                }
                if (isReletReturnOrder) {
                    continue;
                }
                statisticsDTO.addCheckStatementDetailDTO(createReturnMaterialStatementData(k3ReturnOrderDetail, orderMaterial, order, mapContainer,baseCheckStatementDetailDTO));
            }

        }
    }

    private void createReplaceOrdersByReturnTime(CheckStatementMapContainer mapContainer, CheckStatementStatisticsDTO statisticsDTO, List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS){
        Map<Integer, ReplaceOrder> replaceOrderMap = mapContainer.getIdReplaceOrderMap();
        Map<Integer, ReplaceOrderMaterial> replaceOrderMaterialMap = mapContainer.getIdReplaceOrderMaterialMap();
        Map<Integer, OrderProduct> orderProductMap = mapContainer.getIdOrderProductMap();
        Map<Integer, OrderMaterial> orderMaterialMap = mapContainer.getIdOrderMaterialMap();
        Map<String, Order> orderMap = mapContainer.getNoOrderMap();
        for (ReplaceOrderProduct replaceOrderProduct : mapContainer.getIdReplaceOrderProductMap().values()) {
            ReplaceOrder replaceOrder = replaceOrderMap.get(replaceOrderProduct.getReplaceOrderId());
            if (replaceOrder == null) {
                continue;
            }

            String returnTimeMonthStr = DateFormatUtils.format(replaceOrderMap.get(replaceOrderProduct.getReplaceOrderId()).getRealReplaceTime(), "yyyy-MM");
            if (!returnTimeMonthStr.equals(statisticsDTO.getMonth())) {
                continue;
            }

            Order order = orderMap.get(replaceOrder.getOrderNo());
            if (order == null) {
                continue;
            }

            BigDecimal unitAmount = replaceOrderProduct.getProductUnitAmount();
            if (unitAmount == null || BigDecimal.ZERO.compareTo(unitAmount) == 0) {
                continue;
            }
            boolean isReplaceOrder = false;
            BaseCheckStatementDetailDTO baseCheckStatementDetailDTO = null;
            OrderProduct orderProduct = null;
            for (BaseCheckStatementDetailDTO statementDetailDTO : checkStatementDetailDTOS) {
                boolean isReplaceOrderTemp = replaceOrderProduct.getReplaceOrderProductId().equals(statementDetailDTO.getReplaceItemId());
                baseCheckStatementDetailDTO = statementDetailDTO;
                if ((statementDetailDTO instanceof CheckStatementDetailUnReplaceProductDTO) && isReplaceOrderTemp) {
                    isReplaceOrder =true;
                    orderProduct = orderProductMap.get(replaceOrderProduct.getOldOrderProductId());
                    break;
                }
            }
            if(isReplaceOrder){
                statisticsDTO.addCheckStatementDetailDTO(createReplaceOrderProductStatementDTO(replaceOrderProduct, orderProduct, order, mapContainer,baseCheckStatementDetailDTO));
            }
        }
    }

    /**
     * 创建并新增单价是0的退货产品订单列表信息
     */
    private void createAndAddUnitAmountIsZeroUnRentProductOrders(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS, CheckStatementMapContainer mapContainer) {
        Map<String, BaseCheckStatementDetailDTO> itemIdAndItemType = new HashMap<>();
        for (K3ReturnOrderDetail k3ReturnOrderDetail : mapContainer.getIdK3ReturnOrderDetailMap().values()) {
            Order order = mapContainer.getNoOrderMap().get(k3ReturnOrderDetail.getOrderNo());
            if (order != null) {
                for (OrderProduct orderProduct : mapContainer.getIdOrderProductMap().values()) {
                    if (BigDecimal.ZERO.compareTo(orderProduct.getProductUnitAmount()) != 0) {
                        continue;
                    }
                    String itemIdAndItemTypeKey = orderProduct.getProductId() + "_" + OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT;
                    if (itemIdAndItemType.containsKey(itemIdAndItemTypeKey)) {
                        continue;
                    }
                    if (order.getOrderId().equals(orderProduct.getOrderId())) {
                        BaseCheckStatementDetailDTO checkStatementDetailDTO = createReturnProductStatementDTO(k3ReturnOrderDetail, orderProduct, order, mapContainer,null);
                        itemIdAndItemType.put(itemIdAndItemTypeKey, checkStatementDetailDTO);
                        checkStatementDetailDTOS.add(checkStatementDetailDTO);
                    }
                }
            }
        }
    }

    /**
     * 创建并新增单价是0的租赁产品订单列表信息
     */
    private void createAndAddUnitAmountIsZeroRentProductOrders(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS, CheckStatementMapContainer mapContainer) {
        Map<String, BaseCheckStatementDetailDTO> itemIdAndItemTypeMap = new HashMap<>();
        for (Order order : mapContainer.getNoOrderMap().values()) {
            if (order != null) {
                for (OrderProduct orderProduct : mapContainer.getIdOrderProductMap().values()) {
                    if (BigDecimal.ZERO.compareTo(orderProduct.getProductUnitAmount()) != 0) {
                        continue;
                    }
                    String itemIdAndItemTypeKey = orderProduct.getProductId() + "_" + OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
                    if (itemIdAndItemTypeMap.containsKey(itemIdAndItemTypeKey)) {
                        continue;
                    }
                    if (order.getOrderId().equals(orderProduct.getOrderId())) {
                        BaseCheckStatementDetailDTO checkStatementDetailDTO = new CheckStatementDetailRentProductDTO();

                        setStatementOrderProductData(checkStatementDetailDTO, orderProduct);
                        setStatementOrderData(checkStatementDetailDTO, order, true);
                        checkStatementDetailDTO.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_PRODUCT);
                        checkStatementDetailDTO.setOrderType(OrderType.ORDER_TYPE_ORDER);
                        checkStatementDetailDTO.setOrderItemReferId(orderProduct.getOrderProductId());
                        checkStatementDetailDTO.setOrderId(order.getOrderId());

                        checkStatementDetailDTO.mapContainer(mapContainer).loadAmountData().buildImportData();
                        itemIdAndItemTypeMap.put(itemIdAndItemTypeKey, checkStatementDetailDTO);
                        checkStatementDetailDTOS.add(checkStatementDetailDTO);
                    }
                }
            }
        }
    }

    /**
     * 创建换货商品对账单数据传输对象
     */
    private BaseCheckStatementDetailDTO createReplaceOrderProductStatementDTO(ReplaceOrderProduct replaceOrderProduct, OrderProduct orderProduct, Order order, CheckStatementMapContainer mapContainer,BaseCheckStatementDetailDTO statementDetailDTO) {
        BaseCheckStatementDetailDTO checkStatementDetailDTO = new CheckStatementDetailUnReplaceProductDTO();
        setStatementOrderProductData(checkStatementDetailDTO, orderProduct);
        checkStatementDetailDTO.setItemCount(CommonConstant.COMMON_ZERO - replaceOrderProduct.getRealReplaceProductCount());
        checkStatementDetailDTO.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT);
        checkStatementDetailDTO.setOrderType(OrderType.ORDER_TYPE_REPLACE);
//        if(statementDetailDTO.getReletOrderItemReferId() != null){
//            checkStatementDetailDTO.setOrderItemReferId(statementDetailDTO.getReletOrderItemReferId());
//        }else {
//            checkStatementDetailDTO.setOrderItemReferId(statementDetailDTO.getOrderItemReferId());
//        }
//        checkStatementDetailDTO.setOrderItemReferId(replaceOrderProduct.getReplaceOrderProductId());
        checkStatementDetailDTO.setOrderItemReferId(statementDetailDTO.getOrderItemReferId());
        checkStatementDetailDTO.setStatementOrderDetailId(replaceOrderProduct.getReplaceOrderProductId());
        setStatementReplaceOrderProductData(checkStatementDetailDTO, replaceOrderProduct, mapContainer);
        setStatementOrderData(checkStatementDetailDTO, order, false);
        checkStatementDetailDTO.mapContainer(mapContainer).loadAmountData().buildImportData();
        return checkStatementDetailDTO;
    }

    /**
     * 创建退货商品对账单数据传输对象
     */
    private BaseCheckStatementDetailDTO createReturnProductStatementDTO(K3ReturnOrderDetail k3ReturnOrderDetail, OrderProduct orderProduct, Order order, CheckStatementMapContainer mapContainer,BaseCheckStatementDetailDTO statementDetailDTO) {
        BaseCheckStatementDetailDTO checkStatementDetailDTO = new CheckStatementDetailUnRentProductDTO();
        setStatementOrderProductData(checkStatementDetailDTO, orderProduct);
        checkStatementDetailDTO.setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        checkStatementDetailDTO.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT);
        checkStatementDetailDTO.setOrderType(OrderType.ORDER_TYPE_RETURN);
        checkStatementDetailDTO.setOrderItemReferId(k3ReturnOrderDetail.getK3ReturnOrderDetailId());
        checkStatementDetailDTO.setStatementOrderDetailId(k3ReturnOrderDetail.getK3ReturnOrderDetailId());
        setStatementK3ReturnOrderData(checkStatementDetailDTO, k3ReturnOrderDetail, mapContainer);
        setStatementOrderData(checkStatementDetailDTO, order, false);
        checkStatementDetailDTO.mapContainer(mapContainer).loadAmountData().buildImportData();
        return checkStatementDetailDTO;
    }

    /**
     * 创建退货配件对账单数据传输对象
     */
    private BaseCheckStatementDetailDTO createReturnMaterialStatementData(K3ReturnOrderDetail k3ReturnOrderDetail, OrderMaterial orderMaterial, Order order, CheckStatementMapContainer mapContainer,BaseCheckStatementDetailDTO statementDetailDTO) {
        BaseCheckStatementDetailDTO checkStatementDetailDTO = new CheckStatementDetailUnRentMaterialDTO();
        setStatementOrderMaterialData(checkStatementDetailDTO, orderMaterial);
        checkStatementDetailDTO.setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        checkStatementDetailDTO.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL);
        checkStatementDetailDTO.setOrderType(OrderType.ORDER_TYPE_RETURN);
        checkStatementDetailDTO.setOrderItemReferId(k3ReturnOrderDetail.getK3ReturnOrderDetailId());
        checkStatementDetailDTO.setStatementOrderDetailId(k3ReturnOrderDetail.getK3ReturnOrderDetailId());
        setStatementK3ReturnOrderData(checkStatementDetailDTO, k3ReturnOrderDetail, mapContainer);
        setStatementOrderData(checkStatementDetailDTO, order, false);
        checkStatementDetailDTO.mapContainer(mapContainer).loadAmountData().buildImportData();
        return checkStatementDetailDTO;
    }

    private void setStatementK3ReturnOrderData(BaseCheckStatementDetailDTO checkStatementDetailDTO, K3ReturnOrderDetail k3ReturnOrderDetail, CheckStatementMapContainer mapContainer) {
        K3ReturnOrder k3ReturnOrder = mapContainer.getIdK3ReturnOrderMap().get(k3ReturnOrderDetail.getReturnOrderId());
        checkStatementDetailDTO.setStatementExpectPayTime(k3ReturnOrder.getReturnTime());
        checkStatementDetailDTO.setOrderId(k3ReturnOrder.getK3ReturnOrderId());
        checkStatementDetailDTO.setReturnTime(k3ReturnOrder.getReturnTime());
        checkStatementDetailDTO.setReturnReasonType(k3ReturnOrder.getReturnReasonType());
        checkStatementDetailDTO.setReturnOrderNo(k3ReturnOrder.getReturnOrderNo());
        checkStatementDetailDTO.setOrderItemReferId(k3ReturnOrderDetail.getK3ReturnOrderDetailId());
    }

    private void setStatementReplaceOrderProductData(BaseCheckStatementDetailDTO checkStatementDetailDTO, ReplaceOrderProduct replaceOrderProduct, CheckStatementMapContainer mapContainer) {
        ReplaceOrder replaceOrder = mapContainer.getIdReplaceOrderMap().get(replaceOrderProduct.getReplaceOrderId());
        checkStatementDetailDTO.setStatementExpectPayTime(replaceOrder.getRealReplaceTime());
        checkStatementDetailDTO.setOrderId(replaceOrder.getReplaceOrderId());
        checkStatementDetailDTO.setReturnTime(replaceOrder.getRealReplaceTime());
        checkStatementDetailDTO.setReturnOrderNo(replaceOrder.getReplaceOrderNo());
//        checkStatementDetailDTO.setOrderItemReferId(replaceOrderProduct.getReplaceOrderProductId());
    }


    private void setStatementOrderProductData(BaseCheckStatementDetailDTO checkStatementDetailDTO, OrderProduct orderProduct) {
        checkStatementDetailDTO.setItemCount(orderProduct.getProductCount());
        checkStatementDetailDTO.setItemName(orderProduct.getProductName());
        checkStatementDetailDTO.setItemSkuName(orderProduct.getProductSkuName());
        checkStatementDetailDTO.setUnitAmount(orderProduct.getProductUnitAmount());
        checkStatementDetailDTO.setIsNew(orderProduct.getIsNewProduct());
        checkStatementDetailDTO.setPayMode(orderProduct.getPayMode());
        checkStatementDetailDTO.setOrderItemActualId(orderProduct.getOrderProductId());
        checkStatementDetailDTO.setDepositCycle(orderProduct.getDepositCycle());
        checkStatementDetailDTO.setPaymentCycle(orderProduct.getPaymentCycle());
        checkStatementDetailDTO.setItemRentType(orderProduct.getRentType());
    }

    private void setStatementOrderMaterialData(BaseCheckStatementDetailDTO checkStatementDetailDTO, OrderMaterial orderMaterial) {
        checkStatementDetailDTO.setItemCount(orderMaterial.getMaterialCount());
        checkStatementDetailDTO.setItemName(orderMaterial.getMaterialName());
        checkStatementDetailDTO.setItemSkuName(orderMaterial.getMaterialName());
        checkStatementDetailDTO.setUnitAmount(orderMaterial.getMaterialUnitAmount());
        checkStatementDetailDTO.setIsNew(orderMaterial.getIsNewMaterial());
        checkStatementDetailDTO.setPayMode(orderMaterial.getPayMode());
        checkStatementDetailDTO.setOrderItemActualId(orderMaterial.getOrderMaterialId());
        checkStatementDetailDTO.setDepositCycle(orderMaterial.getDepositCycle());
        checkStatementDetailDTO.setPaymentCycle(orderMaterial.getPaymentCycle());
        checkStatementDetailDTO.setItemRentType(orderMaterial.getRentType());
    }

    private void setStatementOrderData(BaseCheckStatementDetailDTO checkStatementDetailDTO, Order order, boolean isInitStatementData) {
        checkStatementDetailDTO.setOrderNo(order.getOrderNo());
        checkStatementDetailDTO.setOrderOriginalId(order.getOrderId());
        checkStatementDetailDTO.setOrderRentStartTime(order.getRentStartTime());
        checkStatementDetailDTO.setOrderExpectReturnTime(order.getExpectReturnTime());
        if (isInitStatementData) {
            checkStatementDetailDTO.setStatementStartTime(order.getRentStartTime());
            checkStatementDetailDTO.setStatementEndTime(order.getExpectReturnTime());
            checkStatementDetailDTO.setStatementExpectPayTime(order.getExpectReturnTime());
        } else {
            checkStatementDetailDTO.setStatementExpectPayTime(null);
        }
    }

    /**
     * 构建统计数据
     */
    private void buildStatisticsData(Map<Date, CheckStatementStatisticsDTO> monthStatisticsMap, StatementOrderMonthQueryParam queryParam) {
        System.out.println("未合并数据之前的数据：" + JSONObject.toJSONString(monthStatisticsMap));
        BigDecimal accountAmount = getAccountAmountByCustomerNo(queryParam.getStatementOrderCustomerNo());

        for (Date month : monthStatisticsMap.keySet()) {
            CheckStatementStatisticsDTO statementStatisticsDTO = monthStatisticsMap.get(month);
            Map<String, BaseCheckStatementDetailDTO> orderIdAndOrderItemIdMap = Maps.newLinkedHashMap();
            // 合并租赁、续租订单
            mergeOrders(statementStatisticsDTO, orderIdAndOrderItemIdMap);
            // 合并换货单
            mergeReplaceOrders(statementStatisticsDTO, orderIdAndOrderItemIdMap);
            // 合并退货单
            mergeReturnOrders(statementStatisticsDTO, orderIdAndOrderItemIdMap);
            // 构建新的结算单详情列表数据
            List<BaseCheckStatementDetailDTO> checkStatementDetailDTONew = buildNewStatementDetailDTOs(statementStatisticsDTO, orderIdAndOrderItemIdMap);
            // 排序
            sortCheckStatementDetailDTOs(checkStatementDetailDTONew);
            statementStatisticsDTO.setCheckStatementDetailDTOS(checkStatementDetailDTONew);
            statementStatisticsDTO.getCheckStatementSummaryDTO().setAccountAmount(accountAmount);
        }
        // 构建统计汇总的数据
        buildSummaryData(monthStatisticsMap, queryParam);
    }

    private BigDecimal getAccountAmountByCustomerNo(String customerNo) {
        BigDecimal bigDecimalBigDecimal = BigDecimal.ZERO;
        if (StringUtils.isBlank(customerNo)) {
            return bigDecimalBigDecimal;
        }
        CustomerAccountLogParam accountLogParam = new CustomerAccountLogParam();
        accountLogParam.setBusinessCustomerNo(customerNo);
        accountLogParam.setPageNo(1);
        accountLogParam.setPageSize(1);
        ServiceResult<String, Map<String, Object>> accountAmountService;
        try {
            accountAmountService = paymentService.queryCustomerAccountLogPage(accountLogParam);
        } catch (Exception e) {
            accountAmountService = new ServiceResult<>();
            logger.error(e.getMessage(), e);
        }

        if (!ErrorCode.SUCCESS.equals(accountAmountService.getErrorCode())) {
            return bigDecimalBigDecimal;
        }
        Map<String, Object> accountAmountMap = accountAmountService.getResult();
        if (MapUtils.isEmpty(accountAmountMap)) {
            return bigDecimalBigDecimal;
        }
        Map<String, Object> customerAccountLogSummary = (Map<String, Object>) accountAmountMap.get("customerAccountLogSummary");
        if (customerAccountLogSummary == null) {
            return bigDecimalBigDecimal;
        }
        String balanceAmount = MapUtils.getString(customerAccountLogSummary, "newBalanceAmount");
        if (StringUtils.isBlank(balanceAmount)) {
            return bigDecimalBigDecimal;
        }
        return new BigDecimal(balanceAmount);
    }

    /**
     * 合并换货订单的数据
     */
    private void mergeReplaceOrders(CheckStatementStatisticsDTO statementStatisticsDTO, Map<String, BaseCheckStatementDetailDTO> orderIdAndOrderItemIdMap) {
        // 合并换货订单
        for (BaseCheckStatementDetailDTO detailDTO : statementStatisticsDTO.getCheckStatementDetailDTOS()) {
            if (!CheckStatementUtil.isReplaceOrder(detailDTO)) {
                continue;
            }

            boolean mergeFlag = detailDTO.isMergeReturnOrder(statementStatisticsDTO);
            String cloneKey = detailDTO.getCacheKey(statementStatisticsDTO);
            BaseCheckStatementDetailDTO ckStatementDetailDTOCache = null;
            if (mergeFlag) {
                boolean startFlag = false;
                if (orderIdAndOrderItemIdMap.containsKey(cloneKey)) {
                    startFlag = true;
                    ckStatementDetailDTOCache = orderIdAndOrderItemIdMap.get(cloneKey);
                } else {
                    for (String key : orderIdAndOrderItemIdMap.keySet()) {
                        if (key.startsWith(cloneKey)) {
                            startFlag = true;
                            ckStatementDetailDTOCache = orderIdAndOrderItemIdMap.get(key);
                            cloneKey = key;
                            break;
                        }
                    }
                }
                mergeFlag = mergeFlag && startFlag;
            }
            if (mergeFlag && ckStatementDetailDTOCache != null) {
                detailDTO.mergeToTarget(ckStatementDetailDTOCache, statementStatisticsDTO);
                orderIdAndOrderItemIdMap.put(cloneKey, ckStatementDetailDTOCache);
            } else {
                orderIdAndOrderItemIdMap.put(cloneKey, detailDTO);
            }

        }
    }

    /**
     * 合并租赁订单和续租订单的数据
     */
    private void mergeOrders(CheckStatementStatisticsDTO statementStatisticsDTO, Map<String, BaseCheckStatementDetailDTO> orderIdAndOrderItemIdMap) {
        // 合并租赁、续租订单
        for (BaseCheckStatementDetailDTO detailDTO : statementStatisticsDTO.getCheckStatementDetailDTOS()) {
            if (!CheckStatementUtil.isRentOrder(detailDTO)) {
                continue;
            }
            boolean mergeFlag = detailDTO.isMergeOrder(statementStatisticsDTO);
            String cloneKey = detailDTO.getCacheKey(statementStatisticsDTO);
            if (mergeFlag && orderIdAndOrderItemIdMap.containsKey(cloneKey)) {
                BaseCheckStatementDetailDTO ckStatementDetailDTOCache = orderIdAndOrderItemIdMap.get(cloneKey);
                detailDTO.mergeToTarget(ckStatementDetailDTOCache, statementStatisticsDTO);
                orderIdAndOrderItemIdMap.put(cloneKey, ckStatementDetailDTOCache);
            } else {
                orderIdAndOrderItemIdMap.put(cloneKey, detailDTO);
            }
        }
    }

    /**
     * 合并租赁退货订单和续租退货订单的数据
     */
    private void mergeReturnOrders(CheckStatementStatisticsDTO statementStatisticsDTO, Map<String, BaseCheckStatementDetailDTO> orderIdAndOrderItemIdMap) {
        // 合并退货租赁、续租订单
        for (BaseCheckStatementDetailDTO detailDTO : statementStatisticsDTO.getCheckStatementDetailDTOS()) {
            if (!CheckStatementUtil.isReturnOrder(detailDTO)) {
                continue;
            }
            boolean mergeFlag = detailDTO.isMergeReturnOrder(statementStatisticsDTO);
            String cloneKey = detailDTO.getCacheKey(statementStatisticsDTO);
            BaseCheckStatementDetailDTO ckStatementDetailDTOCache = null;
            if (mergeFlag) {
                boolean startFlag = false;
                if (orderIdAndOrderItemIdMap.containsKey(cloneKey)) {
                    startFlag = true;
                    ckStatementDetailDTOCache = orderIdAndOrderItemIdMap.get(cloneKey);
                } else {
                    for (String key : orderIdAndOrderItemIdMap.keySet()) {
                        if (key.startsWith(cloneKey)) {
                            startFlag = true;
                            ckStatementDetailDTOCache = orderIdAndOrderItemIdMap.get(key);
                            cloneKey = key;
                            break;
                        }
                    }
                }
                mergeFlag = mergeFlag && startFlag;
            }
            if (mergeFlag && ckStatementDetailDTOCache != null) {
                detailDTO.mergeToTarget(ckStatementDetailDTOCache, statementStatisticsDTO);
                orderIdAndOrderItemIdMap.put(cloneKey, ckStatementDetailDTOCache);
            } else {
                orderIdAndOrderItemIdMap.put(cloneKey, detailDTO);
            }
        }
    }

    /**
     * 构建新的结算单详情列表信息
     */
    private List<BaseCheckStatementDetailDTO> buildNewStatementDetailDTOs(CheckStatementStatisticsDTO statementStatisticsDTO, Map<String, BaseCheckStatementDetailDTO> orderIdAndOrderItemIdMap) {
        List<BaseCheckStatementDetailDTO> checkStatementDetailDTONew = Lists.newArrayList();
        //构造 退货单号相同两笔以上数据
        Map<Integer,List<BaseCheckStatementDetailDTO>> map = new HashMap<>();
        for (BaseCheckStatementDetailDTO detailDTO : orderIdAndOrderItemIdMap.values()) {
            if(detailDTO.getReturnOrderNo() != null){
                if(map.get(detailDTO.getOrderItemReferId()) == null){
                    List<BaseCheckStatementDetailDTO> list = new ArrayList<>();
                    if(detailDTO instanceof CheckStatementDetailUnRentReletDTO){

                    }
                    list.add(detailDTO);
                    map.put(detailDTO.getOrderItemReferId(),list);
                }else {
                    List<BaseCheckStatementDetailDTO> list = map.get(detailDTO.getOrderItemReferId());
                    list.add(detailDTO);
                    map.put(detailDTO.getOrderItemReferId(),list);
                }
            }
        }

        for (BaseCheckStatementDetailDTO detailDTO : orderIdAndOrderItemIdMap.values()) {
            if (!detailDTO.isShowTheMonth(statementStatisticsDTO)) {
                continue;
            }
            Date statementExpectPayTime = detailDTO.getStatementExpectPayTime();

            if(map.size() > CommonConstant.COMMON_ZERO){
                if(CollectionUtil.isNotEmpty(map.get(detailDTO.getOrderItemReferId()))){
                    if(map.get(detailDTO.getOrderItemReferId()).size() > 1 && BigDecimalUtil.compare(detailDTO.getStatementDetailAmount(), BigDecimal.ZERO) == 0){
                        continue;
                    }
                }
            }
            detailDTO.buildMonthAmount(statementExpectPayTime, statementStatisticsDTO).build();
            statementStatisticsDTO.getCheckStatementSummaryDTO().buildMonthAmount(detailDTO);
            checkStatementDetailDTONew.add(detailDTO);
        }
        return checkStatementDetailDTONew;
    }

    // 排序
    private void sortCheckStatementDetailDTOs(List<BaseCheckStatementDetailDTO> checkStatementStatisticsDTOS) {
        List<BaseCheckStatementDetailDTO> notOtherUnRentDetailDTO = new ArrayList<>();
        List<BaseCheckStatementDetailDTO> otherUnRentDetailDTO = new ArrayList<>();
        for (BaseCheckStatementDetailDTO detailDTO : checkStatementStatisticsDTOS) {
            if (detailDTO instanceof CheckStatementDetailUnRentOtherDTO) {
                otherUnRentDetailDTO.add(detailDTO);
            } else {
                notOtherUnRentDetailDTO.add(detailDTO);
            }
        }
        // 只排序除退货其他的对账单数据
        Collections.sort(notOtherUnRentDetailDTO, new Comparator<BaseCheckStatementDetailDTO>() {
            @Override
            public int compare(BaseCheckStatementDetailDTO o1, BaseCheckStatementDetailDTO o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                return (int) ((o1.getOrderRentStartTime().getTime() - o2.getOrderRentStartTime().getTime()) / 1000);
            }
        });
        Collections.sort(notOtherUnRentDetailDTO, new Comparator<BaseCheckStatementDetailDTO>() {
            @Override
            public int compare(BaseCheckStatementDetailDTO o1, BaseCheckStatementDetailDTO o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                // 按照原始订单id排序
                int i = o1.getOrderOriginalId() - o2.getOrderOriginalId();
                if (i == 0) {
                    // 按照原始订单项id排序
                    return o1.getSortItemType() - o2.getSortItemType();
//
                }
                return 0;
            }
        });
        notOtherUnRentDetailDTO.addAll(otherUnRentDetailDTO);
        checkStatementStatisticsDTOS.clear();
        checkStatementStatisticsDTOS.addAll(notOtherUnRentDetailDTO);
    }

    /**
     * 构建统计的汇总数据
     */
    private void buildSummaryData(Map<Date, CheckStatementStatisticsDTO> statementStatisticsDTOMap, StatementOrderMonthQueryParam queryParam) {
        // 构建期数未付金额数据
        queryParam.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        CheckStatementSummaryDTO statementSummaryDTOFromData = statementService.sumStatementDetailAmountByCustomerNo(queryParam);
        BigDecimal previousPeriodEndUnpaidAmount = statementSummaryDTOFromData.getPreviousPeriodEndUnpaidAmount();
        for (CheckStatementStatisticsDTO checkStatementStatisticsDTO : statementStatisticsDTOMap.values()) {
            CheckStatementSummaryDTO statementSummaryDTOFromCache = checkStatementStatisticsDTO.getCheckStatementSummaryDTO();
            statementSummaryDTOFromCache.buildPeriodsUnpaidAmount(previousPeriodEndUnpaidAmount);
            previousPeriodEndUnpaidAmount = statementSummaryDTOFromCache.getTotalUnpaidAmount();
        }
    }

    /**
     * 过滤对账单汇总数据
     */
    private void filterStatementStatisticsData(Map<Date, CheckStatementStatisticsDTO> monthStatisticsMap, CheckStatementMapContainer mapContainer, StatementOrderMonthQueryParam queryParam) {
        Set<Integer> orderIds = new HashSet<>();
        for (Order order : mapContainer.getNoOrderMap().values()) {
            orderIds.add(order.getOrderId());
        }
        List<K3OrderStatementConfigDO> k3OrderStatementConfigDOS = k3OrderStatementConfigMapper.listByOrderIds(orderIds);
        Map<Integer, K3OrderStatementConfigDO> k3OrderStatementConfigDOMap = ListUtil.listToMap(k3OrderStatementConfigDOS, "orderId");
        if (MapUtils.isNotEmpty(k3OrderStatementConfigDOMap)) {
            for (CheckStatementStatisticsDTO statementStatisticsDTO : monthStatisticsMap.values()) {
                List<BaseCheckStatementDetailDTO> newCheckStatementDetailDTOs = new ArrayList<>();
                List<BaseCheckStatementDetailDTO> monthCheckStatementDetails = statementStatisticsDTO.getCheckStatementDetailDTOS();
                for (BaseCheckStatementDetailDTO statementDetailDTO : monthCheckStatementDetails) {
                    K3OrderStatementConfigDO configDO = k3OrderStatementConfigDOMap.get(statementDetailDTO.getOrderOriginalId());
                    if (configDO == null) {
                        newCheckStatementDetailDTOs.add(statementDetailDTO);
                        continue;
                    }
                    if (configDO.getRentStartTime().getTime() <= statementStatisticsDTO.getMonthEndTime()) {
                        newCheckStatementDetailDTOs.add(statementDetailDTO);
                    }
                }
                statementStatisticsDTO.setCheckStatementDetailDTOS(newCheckStatementDetailDTOs);
            }
        }
    }

    /**
     * 导出对账单统计信息
     */
    private void importCheckStatementStatisticsData(Map<Date, CheckStatementStatisticsDTO> checkStatementStatisticsDTOMap, HttpServletResponse response) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
//        HSSFWorkbook xssfWorkbook = new HSSFWorkbook();
        SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook, 1000);
        String customerName = null;
        ExcelExportConfig excelExportConfig = ExcelExportConfigGroup.statementOrderCheckConfigNew;
        Map<String, CellStyle> stringCellStyleMap = ExcelExportSupport.getImportCheckStatementCellStyleMap(xssfWorkbook, excelExportConfig);
        try {
            // 对LinkHashMap逆序导出
            ListIterator<Map.Entry<Date, CheckStatementStatisticsDTO>> i = new ArrayList<>(checkStatementStatisticsDTOMap.entrySet()).listIterator(checkStatementStatisticsDTOMap.size());
            while (i.hasPrevious()) {
                Map.Entry<Date, CheckStatementStatisticsDTO> entry = i.previous();
                CheckStatementStatisticsDTO checkStatementStatisticsDTO = entry.getValue();
                if (CollectionUtil.isEmpty(checkStatementStatisticsDTO.getCheckStatementDetailDTOS())) {
                    continue;
                }
                if (customerName == null) {
                    customerName = checkStatementStatisticsDTO.getCustomerName();
                }
                Sheet sheet = xssfWorkbook.getSheet(checkStatementStatisticsDTO.getMonth());
                if (sheet == null) {
                    sheet = xssfWorkbook.createSheet(checkStatementStatisticsDTO.getMonth());
                    buildSheetTitle(xssfWorkbook, sheet, checkStatementStatisticsDTO);
                }
                ExcelExportSupport.buildSXSSFWorkbook(xssfWorkbook, stringCellStyleMap, sheet, checkStatementStatisticsDTO, excelExportConfig, 1, 40, 63);
                buildCheckStatementSummaryData(xssfWorkbook, sheet, checkStatementStatisticsDTO);
            }
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + new String((customerName + "对账单").getBytes("GB2312"), "ISO_8859_1") + ".xlsx");
            response.setContentType("application/json;charset=utf-8");
            OutputStream stream = response.getOutputStream();
            workbook.write(stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildSheetTitle(Workbook workbook, Sheet sheet, CheckStatementStatisticsDTO checkStatementStatisticsDTO) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 27));
        Row rowNo = sheet.createRow(0);
        Cell cellNo = rowNo.createCell(0);
        cellNo.setCellValue(checkStatementStatisticsDTO.getCustomerName());
        rowNo.setHeightInPoints(30);
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("宋体");//字体格式
        font.setFontHeightInPoints((short) 9);//字体大小
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体显示
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
        cellNo.setCellStyle(cellStyle);
    }

    private void buildCheckStatementSummaryData(Workbook workbook, Sheet sheet, CheckStatementStatisticsDTO checkStatementStatisticsDTO) {
        Sheet sheetAt = sheet;
        int lastRowNum = sheetAt.getLastRowNum();

        Row row1 = sheetAt.createRow(lastRowNum + 2);
        Row row2 = sheetAt.createRow(lastRowNum + 3);
        Row row3 = sheetAt.createRow(lastRowNum + 4);
        Row row4 = sheetAt.createRow(lastRowNum + 5);
        Row row5 = sheetAt.createRow(lastRowNum + 6);
        Row row6 = sheetAt.createRow(lastRowNum + 7);
        Row row7 = sheetAt.createRow(lastRowNum + 8);
        row1.setHeightInPoints(30);
        row2.setHeightInPoints(30);
        row3.setHeightInPoints(30);
        row4.setHeightInPoints(30);
        row5.setHeightInPoints(30);
        row6.setHeightInPoints(30);
        row7.setHeightInPoints(30);

        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 2, lastRowNum + 2, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 3, lastRowNum + 3, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 4, lastRowNum + 4, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 5, lastRowNum + 5, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 6, lastRowNum + 6, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 7, lastRowNum + 7, 20, 24));
        sheetAt.addMergedRegion(new CellRangeAddress(lastRowNum + 8, lastRowNum + 8, 20, 24));

        Cell cell11 = row1.createCell(20);
        Cell cell12 = row2.createCell(20);
        Cell cell13 = row3.createCell(20);
        Cell cell14 = row4.createCell(20);
        Cell cell15 = row5.createCell(20);
        Cell cell16 = row6.createCell(20);
        Cell cell17 = row7.createCell(20);

        createCell(workbook, row1, 21, 4);
        createCell(workbook, row2, 21, 4);
        createCell(workbook, row3, 21, 4);
        createCell(workbook, row4, 21, 4);
        createCell(workbook, row5, 21, 4);
        createCell(workbook, row6, 21, 4);
        createCell(workbook, row7, 21, 4);

        cell11.setCellValue("本月应付");
        ExcelExportSupport.setCellStyle(workbook, cell11, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        cell12.setCellValue("本月已付");
        ExcelExportSupport.setCellStyle(workbook, cell12, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        cell13.setCellValue("本月未付");
        ExcelExportSupport.setCellStyle(workbook, cell13, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        cell14.setCellValue("截止上期未付");
        ExcelExportSupport.setCellStyle(workbook, cell14, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);
        cell15.setCellValue("累计未付");
        ExcelExportSupport.setCellStyle(workbook, cell15, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);
        cell16.setCellValue("账户余额");
        ExcelExportSupport.setCellStyle(workbook, cell16, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_TURQUOISE.index);
        cell17.setCellValue("尚需支付");
        ExcelExportSupport.setCellStyle(workbook, cell17, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_TURQUOISE.index);

        Cell cell21 = row1.createCell(25);
        Cell cell22 = row2.createCell(25);
        Cell cell23 = row3.createCell(25);
        Cell cell24 = row4.createCell(25);
        Cell cell25 = row5.createCell(25);
        Cell cell26 = row6.createCell(25);
        Cell cell27 = row7.createCell(25);
        CheckStatementSummaryDTO checkStatementSummaryDTO = checkStatementStatisticsDTO.getCheckStatementSummaryDTO();
        ExcelExportSupport.setCellStyle(workbook, cell21, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        ExcelExportSupport.setCellStyle(workbook, cell22, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        ExcelExportSupport.setCellStyle(workbook, cell23, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
        ExcelExportSupport.setCellStyle(workbook, cell24, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);
        ExcelExportSupport.setCellStyle(workbook, cell25, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);
        ExcelExportSupport.setCellStyle(workbook, cell26, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_TURQUOISE.index);
        ExcelExportSupport.setCellStyle(workbook, cell27, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_TURQUOISE.index);

        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getThisMonthPayableAmountStr(), cell21);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getThisMonthPaidAmountStr(), cell22);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getThisMonthUnpaidAmountStr(), cell23);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getPreviousPeriodEndUnpaidAmountStr(), cell24);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getTotalUnpaidAmountStr(), cell25);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getAccountAmountStr(), cell26);
        ExcelExportSupport.generateValue(checkStatementSummaryDTO.getNeedPayAmountStr(), cell27);
        logger.info("汇总数据为： " + JSONObject.toJSONString(checkStatementSummaryDTO));
    }

    private void createCell(Workbook workbook, Row row, Integer startColumn, Integer several) {
        for (int i = startColumn; i < startColumn + several; i++) {
            Cell cell = row.createCell(i);
            ExcelExportSupport.setCellStyle(workbook, cell, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
        }
    }

    String formatReturnReasonType(Integer returnReasonType) {

        if (returnReasonType.equals(ReturnReasonType.NOT_REFUNDABLE)) {
            return "客户方设备不愿或无法退还";
        } else if (returnReasonType.equals(ReturnReasonType.EXPIRATION_OF_NORMAL)) {
            return "期满正常收回";
        } else if (returnReasonType.equals(ReturnReasonType.RETIRING_IN_ADVANCE)) {
            return "提前退租";
        } else if (returnReasonType.equals(ReturnReasonType.NO_PAYMENT_ON_TIME)) {
            return "未按时付款或风险等原因上门收回";
        } else if (returnReasonType.equals(ReturnReasonType.EQUIPMENT_FAILURE)) {
            return "设备故障等我方原因导致退货";
        } else if (returnReasonType.equals(ReturnReasonType.SUBJECTIVE_FACTORS)) {
            return "主观因素等客户方原因导致退货";
        } else if (returnReasonType.equals(ReturnReasonType.REPLACEMENT_EQUIPMENT)) {
            return "更换设备";
        } else if (returnReasonType.equals(ReturnReasonType.COMPANY_CLOSURES)) {
            return "公司经营不善/倒闭";
        } else if (returnReasonType.equals(ReturnReasonType.IDLE_EQUIPMENT)) {
            return "项目结束闲置";
        } else if (returnReasonType.equals(ReturnReasonType.FOLLOW_THE_RENT)) {
            return "满三个月或六个月随租随还";
        } else if (returnReasonType.equals(ReturnReasonType.OTHER)) {
            return "其它',";
        }else if (returnReasonType.equals(ReturnReasonType.COMMODITY_FAILURE_REPLACEMENT)) {
            return "商品故障更换";
        }else if (returnReasonType.equals(ReturnReasonType.CONFIGURATION_UPGRADE_REPLACEMENT)) {
            return "配置升级更换";
        }else if (returnReasonType.equals(ReturnReasonType.ORDER_INVALIDATION )) {
            return "订单作废/取消";
        }else if (returnReasonType.equals(ReturnReasonType.PRICE_CAUSE_TO_PURCHASE)) {
            return "价格原因转购买";
        }else if (returnReasonType.equals(ReturnReasonType.PRICE_REASONS_FOR_SUPPLIERS)) {
            return "价格原因换供应商";
        }else if (returnReasonType.equals(ReturnReasonType.PURCHASE_OF_COMMODITY_QUALITY)) {
            return "商品质量问题转购买";
        }else if (returnReasonType.equals(ReturnReasonType.COMMODITY_QUALITY_PROBLEMS_FOR_SUPPLIERS)) {
            return "商品质量问题换供应商";
        }else if (returnReasonType.equals(ReturnReasonType.THE_SERVICE_IS_NOT_RETURNED_IN_TIME)) {
            return "服务不及时造成退货";
        }else if (returnReasonType.equals(ReturnReasonType.STAFF_LEAVING_OR_STUDENT_GRADUATION_IDLE)) {
            return "人员离职/学生毕业闲置";
        }
        return "";
    }


}
