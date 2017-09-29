package com.lxzl.erp.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/8.
 * Time: 14:29.
 */
public class GenerateUtil {
    public static String generateMoneyLogRunningNumber(Integer amountType) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return amountType + dateString + getRandom(2);
    }

    public static String getRandom(int i) {
        Random random = new Random();
        int n = 1;
        for (int m = 0; m < i; m++) {
            n = n * 10;
        }
        int rn = random.nextInt(n);

        int k = i - String.valueOf(rn).length();
        String v = "";
        for (; k > 0; k--) {
            v = v + "0";
        }
        return v + String.valueOf(rn);
    }

}
