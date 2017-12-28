package com.lxzl.erp.common.cache;

import com.lxzl.erp.common.domain.user.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class CommonCache {
    public static Map<Integer, User> userMap = new HashMap<>();
}
