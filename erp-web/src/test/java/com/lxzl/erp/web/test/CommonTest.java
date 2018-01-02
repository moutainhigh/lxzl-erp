package com.lxzl.erp.web.test;

import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class CommonTest {

    public static void main(String[] args) {
        BigDecimal remainder = new BigDecimal(1000.).divideAndRemainder(new BigDecimal(5))[1];
        System.out.println("====" + remainder);
        System.out.println(BigDecimalUtil.div(new BigDecimal(5000), new BigDecimal(30), 2));
        System.out.println(BigDecimalUtil.div(BigDecimalUtil.mul(new BigDecimal(5000), new BigDecimal(30)), new BigDecimal(30),2));
    }

    private static void add(int value) {
        //这个方法有一些复杂的逻辑，其中value+=1的逻辑只能写在这个方法中
        value ++;
    }
}
