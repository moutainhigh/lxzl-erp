package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/2/1 11:04
 */

@Component
public class K3Support {
    //key<erp系统编码> ， value <k3系统编码>
    private static Map<String,String> cityMap = new HashMap<>();
    static {
        cityMap.put("1000","01");
        cityMap.put("0755","01");
        cityMap.put("010","02");
        cityMap.put("021","03");
        cityMap.put("020","04");
        cityMap.put("027","05");
        cityMap.put("025","06");
        cityMap.put("028","07");
        cityMap.put("0592","08");
        cityMap.put("2000","10");
    }


    public String getK3CityCode(String erpCode){
        return cityMap.get(erpCode);
    }


    public String getK3CustomerNo(String erpCode){
        return k3MappingCustomerMapper.findByErpCode(erpCode).getK3CustomerCode();
    }

    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
}