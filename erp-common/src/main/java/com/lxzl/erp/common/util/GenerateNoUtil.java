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

    public static String generateProductNo(Date currentTime) {
        Random random = new Random();
        return "P" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generateOrderNo(Date currentTime) {
        Random random = new Random();
        return "O" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generatePurchaseOrderNo(Date currentTime, Integer userId) {
        Random random = new Random();
        return "PO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + userId + (1000 + random.nextInt(900));
    }

    public static String generatePurchaseDeliveryOrderNo(Date currentTime, Integer purchaseOrderId) {
        Random random = new Random();
        return "PD" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + purchaseOrderId + (1000 + random.nextInt(900));
    }
    public static String generatePurchaseReceiveOrderNo(Date currentTime, Integer purchaseOrderId) {
        Random random = new Random();
        return "PR" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + purchaseOrderId + (1000 + random.nextInt(900));
    }
    public static String generateAutoAllotStatusOrderNo(Date currentTime, Integer purchaseOrderId) {
        Random random = new Random();
        return "AO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + purchaseOrderId + (1000 + random.nextInt(900));
    }
    public static String generateStockOrderNo(Date currentTime) {
        Random random = new Random();
        return "SO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }
    public static String generateBulkMaterialNo(Date currentTime) {
        Random random = new Random();
        return "BM" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }
    public static String generateMaterialNo(Date currentTime) {
        Random random = new Random();
        return "M" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }
    public static String generateCustomerNo(Date currentTime) {
        Random random = new Random();
        return "C" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }
    public static String generateEquipmentNo(Date currentTime, Integer warehouseId, int no) {
        return "LX-EQUIPMENT-" + warehouseId + "-" + new SimpleDateFormat("yyyyMMdd").format(currentTime) + (10000 + no);
    }
}
