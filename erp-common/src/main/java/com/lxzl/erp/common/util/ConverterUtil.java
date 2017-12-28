package com.lxzl.erp.common.util;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);
    public static final <T> List<T> convertList(List list, Class<T> clazz){
        if(list==null) return null;
        List<T> newList = new ArrayList<>();
        for(Object o : list){
            if(o!=null){
                newList.add(convert(o,clazz));
            }
        }
        return newList;
    }
    public static final <T> T convert(Object o, Class<T> clazz){
        if(o == null ) return null;
        try{
            if(clazz.newInstance() instanceof BaseDO){//po转do
                String name = o.getClass().getSimpleName();
                String idFiledName = firstLowName(name) +"Id";
                Field[] fields = o.getClass().getDeclaredFields();
                T t = JSON.parseObject(JSON.toJSONString(o),clazz);
                processPOSpecialField(o,t,clazz);
                for(Field field : fields){
                    try{
                        field.setAccessible(true);
                        if(idFiledName.equals(field.getName())){
                            Integer id = (Integer)field.get(o);
                            Field doId = t.getClass().getDeclaredField("id");
                            doId.setAccessible(true);
                            doId.set(t,id);
                        }else if(field.getName().endsWith("List")){
                            List list = (List)field.get(o);
                            Class poListGenericClazz = getGenericClazzForList(field);
                            String doListFieldName = null;
                            if(isJavaClass(poListGenericClazz)){
                                doListFieldName = field.getName();
                            }else{
                                doListFieldName = field.getName().replace("List","DOList");
                            }
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
                        }else{
                            String type = field.getType().getName();
                            Class cl = getWrapClass(type);
                            if(!isJavaClass(cl)){
                                String doFieldName = firstLowName(cl.getSimpleName()+"DO");
                                Field doField = clazz.getDeclaredField(doFieldName);
                                Class doClazz =  getWrapClass(doField.getType().getName());
                                Object value = field.get(o);
                                if(value!=null){
                                    doField.setAccessible(true);
                                    doField.set(t,convert(value,doClazz));
                                }
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        logger.error("======================="+o.getClass().getName()+"类，"+field.getName()+"字段转换异常========================");
                        continue;
                    }

                }
                return t;
            }else{//do 转 po
                T t = JSON.parseObject(JSON.toJSONString(o),clazz);
                String idFiledName = firstLowName(clazz.getSimpleName()) +"Id";
                Field[] fields = o.getClass().getDeclaredFields();
                Field[] poFields = clazz.getDeclaredFields();
                Map<String,Field> poFiledMap = new HashMap<>();
                for( Field field : poFields){
                    field.setAccessible(true);
                    poFiledMap.put(field.getName(),field);
                }
                processDOSpecialField(o,t,clazz);
                for(Field field : fields){
                    try{
                        field.setAccessible(true);
                        if("id".equals(field.getName())){
                            if(poFiledMap.get(idFiledName)!=null){
                                Field poField = poFiledMap.get(idFiledName);
                                poField.set(t,field.get(o));
                            }
                        }else if(field.getName().endsWith("List")){
                            List doList = (List)field.get(o);
                            Class doListGenericClazz = getGenericClazzForList(field);
                            String poListFieldName = null;
                            if(isJavaClass(doListGenericClazz)){
                                poListFieldName = field.getName();
                            }else{
                                poListFieldName = field.getName().replace("DOList","List");
//                                poListFieldName = firstLowName(doListGenericClazz.getSimpleName().substring(0,doListGenericClazz.getSimpleName().length()-2))+"List";
                            }
                            if(poFiledMap.get(poListFieldName)!=null){
                                Field poField = poFiledMap.get(poListFieldName);
//                                poField.set(t,field.get(o));
                                Class poListGenericClazz = getGenericClazzForList(poField);
                                List newList = new ArrayList();
                                if(CollectionUtil.isNotEmpty(doList)){
                                    for(Object oo : doList){
                                        newList.add(convert(oo,poListGenericClazz));
                                    }
                                }
                                poField.set(t,newList);
                            }
                        }else {
                            String type = field.getType().getName();
                            Class cl = getWrapClass(type);
                            if(!isJavaClass(cl)){
                                int endIndex = field.getName().length()-2;
                                String poFieldName = field.getName().substring(0,endIndex);
                                if(poFiledMap.get(poFieldName)!=null){
                                    Field poField = poFiledMap.get(poFieldName);
                                    Class poClazz =  getWrapClass(poField.getType().getName());
                                    Object value = field.get(o);
                                    if(value!=null){
                                        poField.set(t,convert(value,poClazz));
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error("====="+o.getClass().getName()+"类，"+field.getName()+"字段转换异常====");
                        continue;
                    }

                }
                try{
                    Class parentDOClazz =o.getClass().getSuperclass();
                    Class parentPOClazz =clazz.getSuperclass();
                    Field doField = parentDOClazz.getDeclaredField("createUser");
                    Field poField = parentPOClazz.getDeclaredField("createUserRealName");
                    doField.setAccessible(true);
                    Object createUserId = doField.get(o);
                    setName(createUserId,poField,t);
                }catch(Exception e){
                    e.printStackTrace();
                    logger.error("===="+o.getClass().getName()+"类，createUser字段转换异常====");
                }
                try{
                    Class parentDOClazz =o.getClass().getSuperclass();
                    Class parentPOClazz =clazz.getSuperclass();
                    Field doField = parentDOClazz.getDeclaredField("updateUser");
                    Field poField = parentPOClazz.getDeclaredField("updateUserRealName");
                    doField.setAccessible(true);
                    Object updateUserId = doField.get(o);
                    setName(updateUserId,poField,t);
                }catch(Exception e){
                    e.printStackTrace();
                    logger.error("======================="+o.getClass().getName()+"类，updateUser字段转换异常========================");
                }
                return t;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("error:",e);
            throw new BusinessException("数据转换错误");
        }
    }

    /**
     * 对本项目特殊转换需求进行特殊处理
     */
    private static void processDOSpecialField(Object o ,Object t , Class clazz) throws NoSuchFieldException, IllegalAccessException {
        if(o.getClass().getSimpleName().equals("ProductSkuDO")){
            Field doIdField = o.getClass().getDeclaredField("id");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("skuId".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ProductSkuPropertyDO")){
            Field doIdField = o.getClass().getDeclaredField("id");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("skuPropertyId".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ProductCategoryPropertyDO")){
            Field doIdField = o.getClass().getDeclaredField("id");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("categoryPropertyId".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ImageDO")){
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("imgDomain".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t, ConstantConfig.imageDomain);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ProductImgDO")){
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("imgDomain".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t, ConstantConfig.imageDomain);
                }
            }
        }else if(o.getClass().getSimpleName().equals("MaterialImgDO")){
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("imgDomain".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t, ConstantConfig.imageDomain);
                }
            }
        }
    }
    /**
     * 对本项目特殊转换需求进行特殊处理
     */
    private static void processPOSpecialField(Object o ,Object t , Class clazz) throws NoSuchFieldException, IllegalAccessException {
        if(o.getClass().getSimpleName().equals("ProductSku")){
            Field doIdField = o.getClass().getDeclaredField("skuId");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("id".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ProductSkuProperty")){
            Field doIdField = o.getClass().getDeclaredField("skuPropertyId");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("id".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }else if(o.getClass().getSimpleName().equals("ProductCategoryProperty")){
            Field doIdField = o.getClass().getDeclaredField("categoryPropertyId");
            doIdField.setAccessible(true);
            Object value = doIdField.get(o);
            Field[] poFields = clazz.getDeclaredFields();
            for(Field poIdField : poFields){
                if("id".equals(poIdField.getName())){
                    poIdField.setAccessible(true);
                    poIdField.set(t,value);
                }
            }
        }
    }
    private static void setName(Object userId,Field field,Object t ) throws IllegalAccessException {
        field.setAccessible(true);
        if(userId!=null&& StringUtil.isNotEmpty(String.valueOf(userId))){
            User user = CommonCache.userMap.get(Integer.parseInt(String.valueOf(userId)));
            if(user!=null){
                field.setAccessible(true);
                field.set(t,user.getRealName());
            }
        }
    }
    private static Class getWrapClass(String type) throws ClassNotFoundException {
        if("int".equals(type)){
            return Integer.class;
        }else if("double".equals(type)){
            return Double.class;
        }else if("float".equals(type)){
            return Float.class;
        }else if("long".equals(type)){
            return Long.class;
        }else if("short".equals(type)){
            return Short.class;
        }else if("boolean".equals(type)){
            return Boolean.class;
        }else if("byte".equals(type)){
            return Byte.class;
        }else if("char".equals(type)){
            return Character.class;
        }else{
            return Class.forName(type);
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
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
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
