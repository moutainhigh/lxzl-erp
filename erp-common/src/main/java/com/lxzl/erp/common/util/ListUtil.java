package com.lxzl.erp.common.util;

import java.lang.reflect.Field;
import java.util.*;

public class ListUtil {
    //list转化为set
    public static <K, V> Set<K> listToSet(List<V> list, String kName) {
        Set<K> set = new HashSet<>();
        if (CollectionUtil.isEmpty(list)) {
            return set;
        }
        Field kField = getField(list.get(0).getClass(), kName);
        for (Object o : list) {
            try {
                if(kField.get(o)!=null){
                    set.add((K) kField.get(o));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    //list转化为map
    public static <K, V> Map<K, V> listToMap(List<V> vList, String kName) {
        Map<K, V> map = new HashMap<K, V>();
        if (vList == null || kName == null || vList.size() == 0) {
            return map;
        }
        Field kField = getField(vList.get(0).getClass(), kName);
        for (V v : vList) {
            try {
                if( kField.get(v)!=null){
                    map.put((K) kField.get(v), v);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //list转化为map,多个key值，用-拼接组成一个新的key
    public static <V> Map<String, V> listToMap(List<V> vList, String... kNames) {
        Map<String, V> map = new HashMap();
        if (vList == null || kNames == null || kNames.length == 0 || vList.size() == 0) {
            return map;
        }

        for (V v : vList) {
            String key = "";
            try {
                for (String kName : kNames) {
                    Field kField = getField(v.getClass(), kName);
                    key = key + kField.get(v) + "-";
                }
                key = key.substring(0, key.length() - 1);
                map.put(key, v);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private static <V> Field getField(Class<V> clazz, String kName) {
        if (clazz == null || kName == null) {
            return null;
        }
        for (Field fieldElem : clazz.getDeclaredFields()) {
            fieldElem.setAccessible(true);
            if (fieldElem.getName().equals(kName)) {
                return fieldElem;
            }
        }
        return null;
    }


    public static <K, V> List<V> mapToList(Map<K, V> kvMap) {
        List<V> list = new ArrayList<>();
        if (kvMap != null && kvMap.size() > 0) {
            for (Map.Entry<K, V> entry : kvMap.entrySet()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

}
