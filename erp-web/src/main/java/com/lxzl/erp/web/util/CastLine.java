package com.lxzl.erp.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User : LiuKe
 * Date : 2016/11/5
 * Time : 14:08
 */
public class CastLine {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    /**下划线转驼峰*/
    public static String lineToHump(String str){
//        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "private Long productId;\n" +
                "    private String productName;\n" +
                "    private String model;\n" +
                "    private Integer category;\n" +
                "    private Double cost;\n" +
                "    private Double min_selling_price;\n" +
                "    private Double suggest_selling_price;\n" +
                "    private Integer is_customization;\n" +
                "    private Integer status;\n" +
                "    private String remark;\n" +
                "    private String description;\n" +
                "    private Date create_time;\n" +
                "    private String create_user;\n" +
                "    private Date update_time;\n" +
                "    private String update_user;";
        String result = lineToHump(s);
        System.out.println(result);
    }
}
