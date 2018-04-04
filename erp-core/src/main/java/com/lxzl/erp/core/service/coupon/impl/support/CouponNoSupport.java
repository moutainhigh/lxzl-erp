package com.lxzl.erp.core.service.coupon.impl.support;

import java.util.*;

/**
 * @Author: Sunzhipeng
 * @Description:生成优惠券编号:规则LX+8位大写字母数字组合(不要O和0)
 * @Date: Created in 2018\4\4 0004 11:15
 */
public class CouponNoSupport {
    public static String couponNoSupport(){
        String[] beforeShuffle = new String[] { "1" ,"2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
        }
        String afterShuffle = stringBuilder.toString();
        int x = new Random().nextInt(26)+1;
        String result = afterShuffle.substring(x, x+8);
        return "LX"+result;
    }


}
