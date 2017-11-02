package com.lxzl.erp.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-26 8:53
 */
public class GenerateNoUtil {

    public static String generateOrderNo(Date currentTime, Integer userId) {
        Random random = new Random();
        return "O" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + userId + (1000 + random.nextInt(900));
    }

    public static String generatePayNo(Date currentTime, Integer orderId) {
        Random random = new Random();
        return "P" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + orderId + (1000 + random.nextInt(900));
    }

    public static String generateRefundNo(Date currentTime, Integer orderId) {
        Random random = new Random();
        return "R" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + orderId + (1000 + random.nextInt(900));
    }
    public static String generatePurchaseOrderNo(Date currentTime, Integer userId) {
        Random random = new Random();
        return "C" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + userId + (1000 + random.nextInt(900));
    }
}
