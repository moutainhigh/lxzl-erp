package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.*;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;


/**
 * 违约金计算
 *
 * @author kai
 * @date 2018-02-24 17:08
 */
@Component
public class PenaltySupport {

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ReturnOrderMaterialBulkMapper returnOrderMaterialBulkMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ReturnOrderProductEquipmentMapper returnOrderProductEquipmentMapper;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3Service k3Service;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    /**
     * erp违约金计算
     *
     * @param returnOrderNo
     * @return
     * @author kai
     * <p>
     * 目前根据退货单形式 看对方法 是否新增加字段
     */
    public ServiceResult<String, BigDecimal> orderPenalty(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();

        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        }
        BigDecimal materialPenalty = new BigDecimal(0);
        BigDecimal materialTotal = new BigDecimal(0);
        for (int i = 0; i < returnOrderDO.getReturnOrderMaterialDOList().size(); i++) {
            MaterialDO materialDO = materialMapper.findById(returnOrderDO.getReturnOrderMaterialDOList().get(i).getReturnMaterialId());
            if (materialDO == null) {
                result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return result;
            }
            //计算订单物料相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderMaterialBulkMapper.findByReturnOrderMaterialId  sql ......");
            List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDO = returnOrderMaterialBulkMapper.findByReturnOrderMaterialId(returnOrderDO.getReturnOrderMaterialDOList().get(i).getId());
            if (returnOrderMaterialBulkDO == null) {
                result.setErrorCode(ErrorCode.RETURN_ORDER_MATERIAL_BULK_NOT_NULL);
                return result;
            }

            List<OrderMaterialBulkDO> orderMaterialBulkDOList = new ArrayList<>();
            for (int a = 0; a < returnOrderMaterialBulkDO.size(); a++) {
                SqlLogInterceptor.setExecuteSql("skip print orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo  sql ......");
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.get(a).getOrderNo(), returnOrderMaterialBulkDO.get(a).getBulkMaterialNo());
                if (orderMaterialBulkDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_NOT_EXISTS);
                    return result;
                }
                orderMaterialBulkDOList.add(orderMaterialBulkDO);
            }

            Map countMaterialMap = new HashMap();
            for (OrderMaterialBulkDO temp : orderMaterialBulkDOList) {
                Integer count = (Integer) countMaterialMap.get(temp.getOrderMaterialId() + "," + temp.getMaterialBulkUnitAmount());
                countMaterialMap.put(temp.getOrderMaterialId() + "," + temp.getMaterialBulkUnitAmount(), (count == null) ? 1 : count + 1);
            }

            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderMaterialBulkDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            BigDecimal price;
            Integer total;
            Integer orderMaterialId;
            Iterator it = countMaterialMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String[] str = key.split(",");
                orderMaterialId = Integer.valueOf(str[0]);
                price = new BigDecimal(str[1]);
                total = (Integer) entry.getValue();

                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialId);
                if (orderMaterialDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_NOT_EXISTS);
                    return result;
                }

                //设备租金单价与数量相乘
                BigDecimal materialPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                //(1)非即租即还设备，甲方提前退还租赁设备，乙方将收取合同期内未到期的剩余租金的50%作为违约金，且乙方无须退还其他费用及押金；
                if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderMaterialDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialId);
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 2 && statementOrderDetailList.get(j).getItemRentType() == 2) {
                                if (orderMaterialDO.getMaterialName().equals(statementOrderDetailList.get(j).getItemName())) {
                                    if (total.equals(statementOrderDetailList.get(j).getItemCount())) {
                                        if (price.equals(statementOrderDetailList.get(j).getUnitAmount())) {
                                            if (statementOrderDetailList.get(j).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderMaterialDO.getMaterialCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(total));
                        materialTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (materialDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderMaterialDO.getRentType() == 2) {//判断月
                        materialTotal = materialAndProductTotal(materialPrice, orderDO.getRentStartTime(), orderDO.getRentTimeLength(), returnOrderDO.getReturnTime());
                    }
                }
                materialPenalty = BigDecimalUtil.add(materialPenalty, materialTotal);
            }
        }

        BigDecimal productPenalty = new BigDecimal(0);
        BigDecimal productTotal = new BigDecimal(0);
        for (int i = 0; i < returnOrderDO.getReturnOrderProductDOList().size(); i++) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(returnOrderDO.getReturnOrderProductDOList().get(i).getReturnProductSkuId());
            if (productSkuDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            ProductDO productDO = productMapper.findById(productSkuDO.getProductId());
            if (productDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            //计算订单设备相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderProductEquipmentMapper.findByReturnOrderProductId  sql ......");
            List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDO = returnOrderProductEquipmentMapper.findByReturnOrderProductId(returnOrderDO.getReturnOrderProductDOList().get(i).getId());
            if (returnOrderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.RETURN_ORDER_PRODUCT_EQUIPMENT_NOT_NULL);
                return result;
            }
            List<OrderProductEquipmentDO> orderProductEquipmentDOList = new ArrayList<>();
            for (int a = 0; a < returnOrderProductEquipmentDO.size(); a++) {
                SqlLogInterceptor.setExecuteSql("skip print orderProductEquipmentMapper.findByOrderNoAndEquipmentNo  sql ......");
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.get(a).getOrderNo(), returnOrderProductEquipmentDO.get(a).getEquipmentNo());
                if (orderProductEquipmentDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                    return result;
                }
                orderProductEquipmentDOList.add(orderProductEquipmentDO);
            }
            Map countProductMap = new HashMap();
            for (OrderProductEquipmentDO temp : orderProductEquipmentDOList) {
                Integer count = (Integer) countProductMap.get(temp.getOrderProductId() + "," + temp.getProductEquipmentUnitAmount());
                countProductMap.put(temp.getOrderProductId() + "," + temp.getProductEquipmentUnitAmount(), (count == null) ? 1 : count + 1);
            }
            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderProductEquipmentDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            BigDecimal price;
            Integer total;
            Integer orderProductId;
            Iterator it = countProductMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String[] str = key.split(",");
                orderProductId = Integer.valueOf(str[0]);
                price = new BigDecimal(str[1]);
                total = (Integer) entry.getValue();

                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductId);
                if (orderProductDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                    return result;
                }

                //设备单价与数量相乘
                BigDecimal productPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderProductDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductId);
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 1 && statementOrderDetailList.get(j).getItemRentType() == 2) {
                                if ((orderProductDO.getProductName() + orderProductDO.getProductSkuName()).equals(statementOrderDetailList.get(j).getItemName())) {
                                    if (total.equals(statementOrderDetailList.get(j).getItemCount())) {
                                        if (price.equals(statementOrderDetailList.get(j).getUnitAmount())) {
                                            if (statementOrderDetailList.get(j).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderProductDO.getProductCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(total));
                        productTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderProductDO.getRentType() == 2) {//判断月
                        productTotal = materialAndProductTotal(productPrice, orderDO.getRentStartTime(), orderDO.getRentTimeLength(), returnOrderDO.getReturnTime());
                    }
                }
                productPenalty = BigDecimalUtil.add(productPenalty, productTotal);
            }
        }
        BigDecimal totalPenalty = BigDecimalUtil.add(materialPenalty, productPenalty);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }

    public BigDecimal materialAndProductTotal(BigDecimal price, Date rentStartTime, Integer rentTimeLength, Date returnTime) {
        /*
         * ①若约定租期为6个月以上，租满6个月之后退还，凌雄租赁不收取额外违约金，只需由客户承担退货运费；
         * 若6个月内退还，实际租期≤3个月，将收取3个月租金作为违约金，并由客户自己承担退货运费；
         * 实际租期＞3个月，需补齐6个月租金作为违约金（如客户已租期4个月，需支付2个月租金作为违约金），并由客户自己承担退货运费。
         *
         * ②若约定租期为6个月或以内，实际租期≤3个月，将收取3个月租金作为违约金，并由客户自己承担退货运费；实际租期＞3个月，
         * 需补齐6个月租金作为违约金（如客户已租期4个月，需另支付2个月租金作为违约金）。
         *
         * ③若约定租期为≤3个月，需补齐约定租期所有租金作为违约金（如客户已租期2个月，需另支付1个月租金作为违约金），
         * 并由客户自己承担退货运费；设备正常归还、款项结清后合同终止。（ERP未设置）
         */
        Integer month = DateUtil.getMonthSpace(rentStartTime, returnTime) + 1;
        BigDecimal Total = new BigDecimal(0);
        if (rentTimeLength > 6) {
            if (month >= 6) {
                Total = new BigDecimal(0);
            } else if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else if (month > 3 && month <= 6) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (rentTimeLength <= 6 && rentTimeLength > 3) {
            if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (rentTimeLength <= 3) {
            Total = BigDecimalUtil.mul(price, new BigDecimal(3 - month));
        }
        return Total;
    }

    /**
     * k3违约金
     *
     * @param k3ReturnOrderNo
     * @return
     * @author kai
     */
    public ServiceResult<String, BigDecimal> k3OrderPenalty(String k3ReturnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }
        SqlLogInterceptor.setExecuteSql("skip print k3Service.queryOrder  sql ......");
        ServiceResult<String, Order> k3Order = k3Service.queryOrder(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(0).getOrderNo());
        if (!ErrorCode.SUCCESS.equals(k3Order.getErrorCode())) {
            result.setErrorCode(k3Order.getErrorCode());
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(k3Order.getResult().getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        BigDecimal totalPenalty = new BigDecimal(0);
        for (int i = 0; i < k3ReturnOrderDO.getK3ReturnOrderDetailDOList().size(); i++) {
            String productNo = k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductNo();
            if (productNo.substring(0, 2).equals("10")) {//商品
                //根据 erp_k3_return_order_detail的order_item_id 查询
                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.valueOf(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getOrderItemId()));
                if (orderProductDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                    return result;
                }

                BigDecimal productPenalty = new BigDecimal(0);
                BigDecimal productTotal = new BigDecimal(0);
                ProductDO productDO = productMapper.findById(orderProductDO.getProductId());
                if (productDO == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //设备单价与数量相乘
                BigDecimal productPrice = BigDecimalUtil.mul((orderProductDO.getProductUnitAmount()), new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));

                if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderProductDO.getRentType() == 2) {//判断月
                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getProductId());
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int z = 0; z < statementOrderDetailList.size(); z++) {
                            if (statementOrderDetailList.get(z).getOrderItemType() == 1 && statementOrderDetailList.get(z).getItemRentType() == 2) {
                                if ((orderProductDO.getProductName() + orderProductDO.getProductSkuName()).equals(statementOrderDetailList.get(z).getItemName())) {
                                    if (orderProductDO.getProductCount().equals(statementOrderDetailList.get(z).getItemCount())) {
                                        if (orderProductDO.getProductUnitAmount().equals(statementOrderDetailList.get(z).getUnitAmount())) {
                                            if (statementOrderDetailList.get(z).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderProductDO.getProductCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));
                        productTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderProductDO.getRentType() == 2) {//判断月
                        productTotal = materialAndProductTotal(productPrice, k3Order.getResult().getRentStartTime(), k3Order.getResult().getRentTimeLength(), k3ReturnOrderDO.getReturnTime());
                    }
                }
                productPenalty = BigDecimalUtil.add(productPenalty, productTotal);
                totalPenalty = BigDecimalUtil.add(totalPenalty, productPenalty);

            } else if (productNo.substring(0, 2).equals("20")) {//物料
                //根据 erp_k3_return_order_detail的order_item_id 查询
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.valueOf(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getOrderItemId()));
                if (orderMaterialDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_NOT_EXISTS);
                    return result;
                }
                BigDecimal materialPenalty = new BigDecimal(0);
                BigDecimal materialTotal = new BigDecimal(0);

                MaterialDO materialDO = materialMapper.findById(orderMaterialDO.getMaterialId());
                if (materialDO == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }

                //设备单价与数量相乘
                BigDecimal materialPrice = BigDecimalUtil.mul((orderMaterialDO.getMaterialUnitAmount()), new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));

                if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderMaterialDO.getRentType() == 2) {
                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getMaterialId());
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int z = 0; z < statementOrderDetailList.size(); z++) {
                            if (statementOrderDetailList.get(z).getOrderItemType() == 2 && statementOrderDetailList.get(z).getItemRentType() == 2) {
                                if (orderMaterialDO.getMaterialName().equals(statementOrderDetailList.get(z).getItemName())) {
                                    if (orderMaterialDO.getMaterialCount().equals(statementOrderDetailList.get(z).getItemCount())) {
                                        if (orderMaterialDO.getMaterialUnitAmount().equals(statementOrderDetailList.get(z).getUnitAmount())) {
                                            if (statementOrderDetailList.get(z).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderMaterialDO.getMaterialCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));
                        materialTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (materialDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderMaterialDO.getRentType() == 2) {//判断月
                        materialTotal = materialAndProductTotal(materialPrice, k3Order.getResult().getRentStartTime(), k3Order.getResult().getRentTimeLength(), k3ReturnOrderDO.getReturnTime());
                    }
                }
                materialPenalty = BigDecimalUtil.add(materialPenalty, materialTotal);
                totalPenalty = BigDecimalUtil.add(totalPenalty, materialPenalty);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }

    public ServiceResult<String, List<StatementOrderDetail>> queryStatementOrderDetailByOrderId(Integer orderId, Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, List<StatementOrderDetail>> result = new ServiceResult<>();

        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderIdAndOrderItemType(orderId, orderItemType, orderItemReferId);
        if (statementOrderDetailDOList == null) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_EXISTS);
            return result;
        }
        List<StatementOrderDetail> statementOrderDetailList = ConverterUtil.convertList(statementOrderDetailDOList, StatementOrderDetail.class);

        if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
            for (StatementOrderDetail statementOrderDetail : statementOrderDetailList) {
                if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                    SqlLogInterceptor.setExecuteSql("skip print orderMapper.findByOrderId  sql ......");
                    OrderDO orderDO = orderMapper.findByOrderId(statementOrderDetail.getOrderId());
                    if (orderDO != null) {
                        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                                if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                                    statementOrderDetail.setItemCount(orderProductDO.getProductCount());
                                    statementOrderDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                    statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                                    statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                                    break;
                                }
                            }
                        }
                        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
                            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                                if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderMaterialDO.getId())) {
                                    statementOrderDetail.setItemCount(orderMaterialDO.getMaterialCount());
                                    statementOrderDetail.setItemName(orderMaterialDO.getMaterialName());
                                    statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                    statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statementOrderDetailList);
        return result;
    }
}
