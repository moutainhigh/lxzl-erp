package com.lxzl.erp.common.util;

import com.alibaba.fastjson.JSON;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConverterUtil {

    public static final <T> T convert(Object o, Class<T> clazz){
        try{
            if(clazz.newInstance() instanceof BaseDO){//po转do
                String name = o.getClass().getSimpleName();
                String idFiledName = firstLowName(name) +"Id";
                Field[] fields = o.getClass().getDeclaredFields();
                T t = JSON.parseObject(JSON.toJSONString(o),clazz);
                for(Field field : fields){
                    field.setAccessible(true);
                    if(idFiledName.equals(field.getName())){
                        Integer id = (Integer)field.get(o);
                        Field doId = t.getClass().getDeclaredField("id");
                        doId.setAccessible(true);
                        doId.set(t,id);
                    }else if(field.getName().endsWith("List")){
                        List list = (List)field.get(o);
                        Class poListGenericClazz = getGenericClazzForList(field);
                        String doListFieldName = firstLowName(poListGenericClazz.getSimpleName())+"DOList";
                        Field doField = clazz.getDeclaredField(doListFieldName);
                        doField.setAccessible(true);
                        Class doListGenericClazz = getGenericClazzForList(doField);
                        List newList = new ArrayList();
                        if(CollectionUtil.isNotEmpty(list)){
                            for(Object oo : list){
                                newList.add(convert(oo,doListGenericClazz));
                            }
                        }
                        doField.setAccessible(true);
                        doField.set(t,newList);
                    }
                }
                return t;
            }else{//do 转 po
                T t = JSON.parseObject(JSON.toJSONString(o),clazz);
                String idFiledName = firstLowName(clazz.getSimpleName()) +"Id";
                Field[] fields = o.getClass().getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    if("id".equals(field.getName())){
                        Field poId = t.getClass().getDeclaredField(idFiledName);
                        poId.setAccessible(true);
                        poId.set(t,field.get(o));
                    }else if(field.getName().endsWith("List")){
                        List doList = (List)field.get(o);
                        Class doListGenericClazz = getGenericClazzForList(field);
                        String poListFieldName = firstLowName(doListGenericClazz.getSimpleName().substring(0,doListGenericClazz.getSimpleName().length()-2))+"List";
                        Field poField = clazz.getDeclaredField(poListFieldName);
                        poField.setAccessible(true);
                        Class poListGenericClazz = getGenericClazzForList(poField);
                        List newList = new ArrayList();
                        if(CollectionUtil.isNotEmpty(doList)){
                            for(Object oo : doList){
                                newList.add(convert(oo,poListGenericClazz));
                            }
                        }
                        poField.setAccessible(true);
                        poField.set(t,newList);
                    }
                }
                return t;
            }
        }catch (Exception e){
            throw new BusinessException("数据转换错误");
        }
    }
    private static String firstLowName (String name ){
        String first = name.substring(0,1);
        String lowName = first.toLowerCase()+name.substring(1,name.length());
        return lowName;
    }
    private static Class getGenericClazzForList(Field field ){
        Type genericType = field.getGenericType();
        ParameterizedType pt = (ParameterizedType) genericType;
        Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
        return genericClazz;
    }
    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fieldName){
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
//        ReturnOrder returnOrder = new ReturnOrder();
//        returnOrder.setReturnOrderId(1);
//        List<ReturnOrderProduct> returnOrderProductList = new ArrayList<>();
//        ReturnOrderProduct returnOrderProduct = new ReturnOrderProduct();
//        returnOrderProduct.setReturnOrderProductId(1);
//        returnOrderProduct.setCreateTime(new Date());
//        returnOrderProductList.add(returnOrderProduct);
//        returnOrder.setReturnOrderProductList(returnOrderProductList);
//        ReturnOrderDO returnOrderDO = parseObject(returnOrder,ReturnOrderDO.class);
//        System.out.println();

//        ReturnOrderDO returnOrderDO = new ReturnOrderDO();
//        returnOrderDO.setId(1);
//        List<ReturnOrderProductDO> returnOrderProductDOList = new ArrayList<>();
//        ReturnOrderProductDO returnOrderProductDO = new ReturnOrderProductDO();
//        returnOrderProductDO.setId(1);
//        returnOrderProductDO.setCreateTime(new Date());
//        returnOrderProductDOList.add(returnOrderProductDO);
//        returnOrderDO.setReturnOrderProductDOList(returnOrderProductDOList);
//        ReturnOrder returnOrder = parseObject(returnOrderDO,ReturnOrder.class);
//        System.out.println();

    }
}