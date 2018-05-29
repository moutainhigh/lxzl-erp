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

    // 比较两个整数数组是否元素相同(不考虑顺序，可重复放相同值)
    public static boolean equalIntegerList(List<Integer> list1, List<Integer> list2) {
        if (list1 == list2) {
            return true;
        } else if (list1 != null && list2 != null && list1.size() == list2.size()) {
            List<Integer> tmpList2 = new ArrayList<>();
            for (Integer i : list2) {
                tmpList2.add(i);
            }

            for (Integer i : list1) {
                if (!tmpList2.remove(i)) {
                    return false;
                }
            }

            if (tmpList2.size() != 0) {
                return false;
            }

            return true;
        } else  {
            return false;
        }
    }

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(2);
        list2.add(1);
//        list2.add(2);
        System.out.println(equalIntegerList(list1, list2));
    }

}
