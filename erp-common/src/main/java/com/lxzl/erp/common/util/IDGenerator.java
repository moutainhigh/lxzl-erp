package com.lxzl.erp.common.util;

import com.lxzl.se.common.util.date.DateUtil;

import java.util.Date;

/**
 * User : LiuKe
 * Date : 2016/11/12
 * Time : 17:02
 */
public class IDGenerator {
    public static String contractCodeGenerator(String contractId,Date createTime){
        contractId = contractId==null?"1":String.valueOf(Integer.parseInt(contractId)+1);
        String time = DateUtil.formatDate(createTime,"yyyyMMdd");
        return time+addZero(contractId,6);
    }
    public static String addZero(String arg , int size){
        int oldSize = arg.length();
        if(size<oldSize){
            return arg.substring(size);
        }else if(size==oldSize){
            return arg;
        }else{
            StringBuffer sb = new StringBuffer();
            int length0 = size-oldSize;
            for(int i = 0 ; i < length0 ; i++){
                sb.append("0");
            }
            sb.append(arg);
            return sb.toString();
        }
    }

}
