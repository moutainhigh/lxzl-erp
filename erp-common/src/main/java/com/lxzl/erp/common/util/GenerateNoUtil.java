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

    public static String generateAutoAllotStatusOrderNo(Date currentTime, Integer purchaseOrderId) {
        Random random = new Random();
        return "AO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + purchaseOrderId + (1000 + random.nextInt(900));
    }

    public static String generateStockOrderNo(Date currentTime) {
        Random random = new Random();
        return "SO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generateBulkMaterialNo(Date currentTime, int no) {
        return "BM" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) +  + (10000 + no);
    }

    public static String generateMaterialNo(Date currentTime) {
        Random random = new Random();
        return "M" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }
    public static String generateEquipmentNo(Date currentTime, Integer warehouseId, int no) {
        return "LX-E-" + warehouseId + "-" + new SimpleDateFormat("yyyyMMdd").format(currentTime) + (10000 + no);
    }
    public static String generateStatementNo(Date currentTime) {
        Random random = new Random();
        return "SN" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

}
