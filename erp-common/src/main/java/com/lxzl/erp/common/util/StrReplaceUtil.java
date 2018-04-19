package com.lxzl.erp.common.util;


import com.lxzl.se.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
/**
 * @创建人 liuzy
 * @创建日期 2018/3/23
 * @描述:提供对字符特殊处理类
 */
public class StrReplaceUtil {

    public final static Map<Character,Character> map = new HashMap<>();

    /**初始化定义存储全角和半角字符之间的对应关系*/
    static{
        map.put('(', '（');
        map.put(')', '）');
    }
    /**
     *@描述 替换方法
     *@参数  [line]
     *@返回值  java.lang.String
     *@创建人  liuzy
     *@创建时间  2018/3/23
     *@修改人和其它信息
     */
    public static String replaceAll(String line) {
        if(line==null){
            return null;
        }
        int length = line.length();
        for (int i = 0; i < length; i++) {
              Character c = line.charAt(i);
            if (map.containsKey(c)) {
                line = line.replace(c,map.get(c));
            }
        }
        return line;
    }

    /**
     *@描述 替换方法,将字符串中除汉字和英文字母大小写之外的符号全部剔除
     *@参数  [str]
     *@返回值  java.lang.String
     *@创建人  sunzp
     *@创建时间  2018/3/27
     *@修改人和其它信息
     */
    public static String nameToSimple(String str){
        String reg = "[^\u4e00-\u9fa5a-zA-Zａ-ｚＡ-Ｚ]";
        str = str.replaceAll(reg, "");
        str=half2Full(str);
        return str;
    }
    /**
     * 这是一个半角专全角的方法
     * @Title: half2Full
     * @param value input value
     * @return converted value
     */
    public static String half2Full(String value) {
        if (value.isEmpty()) {
            return "";
        }
        char[] cha = value.toCharArray();
        /**
         * full blank space is 12288, half blank space is 32
         * others :full is 65281-65374,and half is 33-126.
         */
        for (int i = 0; i < cha.length; i++) {
            if (cha[i] == 32) {
                cha[i] = (char) 12288;
            } else if (cha[i] < 127) {
                cha[i] = (char) (cha[i] + 65248);
            }
        }
        return new String(cha);
    }

    /**
     * 格式化接口地址
     * @param interfaceUrl
     * @return
     */
    public static String formatInterfaceUrl(String interfaceUrl) {

        if(StringUtil.isEmpty(interfaceUrl)){
            return "";
        }
        interfaceUrl = interfaceUrl.trim();
        if ('/' != interfaceUrl.charAt(0)) {
            interfaceUrl = "/" + interfaceUrl;
        }
        if ('/' == interfaceUrl.charAt(interfaceUrl.length() - 1)) {
            interfaceUrl = interfaceUrl.substring(0, interfaceUrl.length() - 1);
        }
        return interfaceUrl;
    }

}
