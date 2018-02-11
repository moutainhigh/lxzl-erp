package com.lxzl.erp.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    public static final int SCALE = 5;
    public static final int STANDARD_SCALE = 2;


    public static final BigDecimal MONEY_PRECISION = new BigDecimal(0.005);

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return value1.add(value2);
    }
    public static BigDecimal addAll(BigDecimal ... values) {
        BigDecimal result = BigDecimal.ZERO;
        for(BigDecimal value : values){
            value = value==null?BigDecimal.ZERO:value;
            result = result.add(value);
        }
        return result;
    }
    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    public static BigDecimal sub(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return value1.subtract(value2);
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return value1.multiply(value2);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param value1    被除数
     * @param value2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double value1, double value2, int scale) {
        if (scale < 0) {
            //如果精确范围小于0，抛出异常信息。
            throw new IllegalArgumentException("精确度不能小于0");
        } else if (value2 == 0) {
            //如果除数为0，抛出异常信息。
            throw new IllegalArgumentException("除数不能为0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal div(BigDecimal value1, BigDecimal value2, int scale) {
        if (scale < 0) {
            //如果精确范围小于0，抛出异常信息。
            throw new IllegalArgumentException("精确度不能小于0");
        } else if (value2.doubleValue() == 0) {
            //如果除数为0，抛出异常信息。
            throw new IllegalArgumentException("除数不能为0");
        }
        return value1.divide(value2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }
        BigDecimal one = new BigDecimal("1");
        return v.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确加法计算的add方法，确认精确度
     *
     * @param value1 被加数
     * @param value2 加数
     * @param scale  小数点后保留几位
     * @return 两个参数求和之后，按精度四舍五入的结果
     */
    public static double add(double value1, double value2, int scale) {
        return round(add(value1, value2), scale);
    }

    public static BigDecimal add(BigDecimal value1, BigDecimal value2, int scale) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return round(add(value1, value2), scale);
    }

    /**
     * 提供精确减法运算的sub方法，确认精确度
     *
     * @param value1 被减数
     * @param value2 减数
     * @param scale  小数点后保留几位
     * @return 两个参数的求差之后，按精度四舍五入的结果
     */
    public static double sub(double value1, double value2, int scale) {
        return round(sub(value1, value2), scale);
    }

    public static BigDecimal sub(BigDecimal value1, BigDecimal value2, int scale) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return round(sub(value1, value2), scale);
    }

    /**
     * 提供精确乘法运算的mul方法，确认精确度
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @param scale  小数点后保留几位
     * @return 两个参数的乘积之后，按精度四舍五入的结果
     */
    public static double mul(double value1, double value2, int scale) {
        return round(mul(value1, value2), scale);
    }

    public static BigDecimal mul(BigDecimal value1, BigDecimal value2, int scale) {
        if (value1 == null) {
            value1 = new BigDecimal(0);
        }
        if (value2 == null) {
            value2 = new BigDecimal(0);
        }
        return round(mul(value1, value2), scale);
    }

    public static int compare(BigDecimal value1, BigDecimal value2) {
        value1 = value1 == null ? BigDecimal.ZERO : value1.setScale(BigDecimalUtil.SCALE, BigDecimal.ROUND_HALF_UP);
        value2 = value2 == null ? BigDecimal.ZERO : value2.setScale(BigDecimalUtil.SCALE, BigDecimal.ROUND_HALF_UP);
        return value1.compareTo(value2);
    }

    public static void main(String[] args) {
        System.out.println(compare(new BigDecimal(18.01000001), new BigDecimal(18.01)));
        System.out.println(compare(new BigDecimal(18.02000001), new BigDecimal(18.01)));
        System.out.println(compare(new BigDecimal(18.00000001), new BigDecimal(18.01)));


        System.out.println(compare(new BigDecimal(18.01), new BigDecimal(18.01000001)));
        System.out.println(compare(new BigDecimal(18.01), new BigDecimal(18.02000001)));
        System.out.println(compare(new BigDecimal(18.01), new BigDecimal(18.00000001)));
    }
}
