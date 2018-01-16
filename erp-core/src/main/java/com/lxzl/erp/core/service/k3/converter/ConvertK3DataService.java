package com.lxzl.erp.core.service.k3.converter;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ConvertK3DataService {
    String getK3PostJson(Object data);
    List<NameValuePair> getK3PostForm(Object data);
}
