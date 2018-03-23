package com.lxzl.erp.common.util;


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

    public static void main(String[] args) {
       String str1="(孙）齐天大圣(贵州)有限公司";
       System.out.print(StrReplaceUtil.replaceAll(str1));
    }

}
