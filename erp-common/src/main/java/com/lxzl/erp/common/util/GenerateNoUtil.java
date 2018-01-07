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
        currentTime = new Date();
        Random random = new Random();
        return "LXP" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generateAutoAllotStatusOrderNo(Date currentTime, Integer purchaseOrderId) {
        currentTime = new Date();
        Random random = new Random();
        return "LXAO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + purchaseOrderId + (1000 + random.nextInt(900));
    }

    public static String generateStockOrderNo(Date currentTime) {
        currentTime = new Date();
        Random random = new Random();
        return "LXSO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generateBulkMaterialNo(Date currentTime, int no) {
        currentTime = new Date();
        return "LXBM" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + +(10000 + no);
    }

    public static String generateMaterialNo(Date currentTime) {
        currentTime = new Date();
        Random random = new Random();
        return "LXM" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

    public static String generateEquipmentNo(Date currentTime, Integer warehouseId, int no) {
        currentTime = new Date();
        return "LX-E-" + warehouseId + "-" + new SimpleDateFormat("yyyyMMdd").format(currentTime) + (10000 + no);
    }

    public static String generateStatementNo(Date currentTime) {
        currentTime = new Date();
        Random random = new Random();
        return "LXSN" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentTime) + (1000 + random.nextInt(900));
    }

}
