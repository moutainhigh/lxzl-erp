package com.lxzl.erp.core.service.k3;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.K3Config;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FromSEOrderConfirmlSelStock;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingSubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingSubCompanyDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
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
        cityMap.put("1000","00");
        cityMap.put("0755","01");
        cityMap.put("010","02");
        cityMap.put("021","03");
        cityMap.put("020","04");
        cityMap.put("027","05");
        cityMap.put("025","06");
        cityMap.put("028","07");
        cityMap.put("0592","08");
        cityMap.put("2000","10");
        cityMap.put("2001","20");
    }


    public String getK3CityCode(String erpCode){
        return cityMap.get(erpCode);
    }


    public String getK3CustomerNo(String erpCode){
        return k3MappingCustomerMapper.findByErpCode(erpCode).getK3CustomerCode();
    }
    public String getK3UserCode(Integer erpUserId){
        Integer subCompanyId = userSupport.getCompanyIdByUser(erpUserId);
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
        return k3MappingSubCompanyDO.getK3SubCompanyCode()+"."+erpUserId;
    }

    public ServiceResult<String,String> queryK3Stock(FromSEOrderConfirmlSelStock fromSEOrderConfirmlSelStock){
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Map responseMap = new HashMap();
        String response = null;

        String requestJson  = JSONObject.toJSONString(fromSEOrderConfirmlSelStock);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String k3confirmOrderUrl = K3Config.k3Server + "/OrderConfirml/SelStock";  //k3确认收货url
//            String k3confirmOrderUrl = "http://103.239.207.170:9090/OrderConfirml/SelStock";  //k3确认收货url
            response = HttpClientUtil.post(k3confirmOrderUrl, requestJson, headerBuilder, "UTF-8");
            responseMap = JSONObject.parseObject(response, HashMap.class);
            if ("true".equals(responseMap.get("IsSuccess").toString())){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(responseMap.get("Data").toString());
                return serviceResult;
            }
            else{
                serviceResult.setErrorCode(ErrorCode.K3_SEL_STOCK_QUERY_FAILED);
                serviceResult.setResult(responseMap.get("Message").toString());
                return serviceResult;
            }
        }catch (Exception e){
            serviceResult.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            serviceResult.setResult(responseMap.get("Message").toString());
            return serviceResult;
        }
    }


    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;
}
