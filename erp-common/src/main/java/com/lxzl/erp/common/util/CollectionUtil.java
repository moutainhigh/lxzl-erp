package com.lxzl.erp.common.util;

import java.util.Collection;

public class CollectionUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }
}
