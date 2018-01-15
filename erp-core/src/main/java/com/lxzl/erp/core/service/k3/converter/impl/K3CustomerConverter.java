package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import org.springframework.stereotype.Service;


@Service
public class K3CustomerConverter implements ConvertK3DataService{

    @Override
    public String getK3PostJson(Object data) {
        Customer customer = (Customer)data;
        //todo 封装请求参数
        String postJson = "";
        return postJson;
    }
}
