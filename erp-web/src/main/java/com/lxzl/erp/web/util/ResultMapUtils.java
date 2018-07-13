package com.lxzl.erp.web.util;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultMapUtils {

    @Nullable
    public static List<List<Object>> toLists(List<Map> maps) {
        List<List<Object>> results = null;
        if (maps == null)
            return new LinkedList<>();
        for (Map map : maps) {
            Set set = map.keySet();
            if (results == null) {
                results = new LinkedList<>();
                LinkedList<Object> keyList = new LinkedList<>();
                keyList.addAll(set);
                results.add(keyList);
            }
            LinkedList<Object> linkedList = new LinkedList<>();
            for (Object key : set)
                linkedList.add(map.get(key));
            results.add(linkedList);
        }
        return results;
    }

}
