package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


/**
 * 描述: ${DESCRIPTION}
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

    public ServiceResult<String, BigDecimal> orderPenalty(String returnOrderNo, String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();

        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        }
        BigDecimal materialPenalty = new BigDecimal(0);
        BigDecimal materialTotal = new BigDecimal(0);
        for (int i = 0; i < orderDO.getOrderMaterialDOList().size(); i++) {
            MaterialDO materialDO = materialMapper.findById(orderDO.getOrderMaterialDOList().get(i).getMaterialId());
            //设备租金单价与数量相乘
            BigDecimal materialPrice = BigDecimalUtil.mul(orderDO.getOrderMaterialDOList().get(i).getMaterialUnitAmount(), new BigDecimal(orderDO.getOrderMaterialDOList().get(i).getMaterialCount()));
            /*
             * (1)非即租即还设备，甲方提前退还租赁设备，乙方将收取合同期内未到期的剩余租金的50%作为违约金，且乙方无须退还其他费用及押金；
             */
            if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                if (orderDO.getRentType() == 2) {//判断月
                    Integer month = DateUtil.getMonthSpace(returnOrderDO.getReturnTime(), orderDO.getExpectReturnTime()) + 1;
                    materialTotal = BigDecimalUtil.div(BigDecimalUtil.mul(materialPrice, new BigDecimal(month)), new BigDecimal(2), 2);
                }
            } else if (materialDO.getIsReturnAnyTime() == CommonConstant.YES) {
                if (orderDO.getRentType() == 2) {//判断月
                    materialTotal = materialAndProductTotal(materialPrice, orderDO, returnOrderDO);
                }
            }
            materialPenalty = BigDecimalUtil.add(materialPenalty, materialTotal);
        }

        BigDecimal productPenalty = new BigDecimal(0);
        BigDecimal productTotal = new BigDecimal(0);
        for (int i = 0; i < orderDO.getOrderProductDOList().size(); i++) {
            ProductDO productDO = productMapper.findById(orderDO.getOrderProductDOList().get(i).getProductId());
            //设备单价与数量相乘
            BigDecimal productPrice = BigDecimalUtil.mul(orderDO.getOrderProductDOList().get(i).getProductUnitAmount(), new BigDecimal(orderDO.getOrderProductDOList().get(i).getProductCount()));

            if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                if (orderDO.getRentType() == 2) {//判断月
                    Integer month = DateUtil.getMonthSpace(returnOrderDO.getReturnTime(), orderDO.getExpectReturnTime()) + 1;
                    productTotal = BigDecimalUtil.div(BigDecimalUtil.mul(productPrice, new BigDecimal(month)), new BigDecimal(2), 2);
                }
            } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                if (orderDO.getRentType() == 2) {//判断月
                    productTotal = materialAndProductTotal(productPrice, orderDO, returnOrderDO);
                }
            }
            productPenalty = BigDecimalUtil.add(productPenalty, productTotal);
        }

        BigDecimal totalPenalty = BigDecimalUtil.add(materialPenalty, productPenalty);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }

    public BigDecimal materialAndProductTotal(BigDecimal price, OrderDO orderDO, ReturnOrderDO returnOrderDO) {
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
        Integer month = DateUtil.getMonthSpace(orderDO.getRentStartTime(), returnOrderDO.getReturnTime()) + 1;
        BigDecimal Total = new BigDecimal(0);
        if (orderDO.getRentTimeLength() > 6) {
            if (month >= 6) {
                Total = new BigDecimal(0);
            } else if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else if (month > 3 && month <= 6) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (orderDO.getRentTimeLength() <= 6 && orderDO.getRentTimeLength() > 3) {
            if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (orderDO.getRentTimeLength() <= 3) {
            Total = BigDecimalUtil.mul(price, new BigDecimal(3 - month));
        }
        return Total;
    }
}
