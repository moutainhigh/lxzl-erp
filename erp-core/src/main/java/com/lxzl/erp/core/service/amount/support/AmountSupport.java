package com.lxzl.erp.core.service.amount.support;

import com.lxzl.erp.common.constant.OrderRentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class AmountSupport {
    /**
     *
     * @param value 字面量，例如设备月租金 90元 ，value=90;设备次租金10元，value=10
     * @param start 起租时间
     * @param end   归还时间
     * @param rentType  租赁类型
     * @return
     */
    public BigDecimal calculateRentCost(BigDecimal value , Date start , Date end , Integer rentType){
        BigDecimal cost = new BigDecimal(0);
        if(OrderRentType.RENT_TYPE_DAY.equals(rentType)){
            cost=calculateDay(value,start,end);
        }else if(OrderRentType.RENT_TYPE_MONTH.equals(rentType)){
            cost=calculateMonth(value,start,end);
        }
        return cost;
    }

    /**
     * todo 计算按次租的租金
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateTime(BigDecimal value , Date start , Date end){
        BigDecimal cost = new BigDecimal(0);
        return cost;
    }
    /**
     * todo 计算按天租的租金
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateDay(BigDecimal value , Date start , Date end){
        BigDecimal cost = new BigDecimal(0);
        cost = new BigDecimal(50);
        return cost;
    }
    /**
     * todo 计算按月租的租金
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateMonth(BigDecimal value , Date start , Date end){
        BigDecimal cost = new BigDecimal(0);
        cost = new BigDecimal(100);
        return cost;
    }
}
