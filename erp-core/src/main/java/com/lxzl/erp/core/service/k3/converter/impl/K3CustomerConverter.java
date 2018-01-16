package com.lxzl.erp.core.service.k3.converter.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.domain.k3.K3Customer;
import com.lxzl.erp.common.domain.k3.K3Number;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class K3CustomerConverter implements ConvertK3DataService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private AreaCityMapper areaCityMapper;
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

    @Override
    public String getK3PostJson(Object data) {
        Customer customer = (Customer)data;
        return JSON.toJSONString(getK3Customer(customer));
    }

    @Override
    public List<NameValuePair> getK3PostForm(Object data) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        K3Customer k3Customer = getK3Customer((Customer)data);
        if(CollectionUtil.isNotEmpty(k3Customer.getNumbers())){
            JSONArray jsonArray = new JSONArray();
            for(K3Number item : k3Customer.getNumbers()){
                jsonArray.add(item);
            }
            nameValuePairList.add(new BasicNameValuePair("numbers", JSON.toJSONString(jsonArray)));
        }

        nameValuePairList.add(new BasicNameValuePair("number", k3Customer.getNumber()));
        nameValuePairList.add(new BasicNameValuePair("name", k3Customer.getName()));
        nameValuePairList.add(new BasicNameValuePair("address", k3Customer.getAddress()));
        nameValuePairList.add(new BasicNameValuePair("empNumber", k3Customer.getEmpNumber()));
        nameValuePairList.add(new BasicNameValuePair("empName", k3Customer.getEmpName()));
        nameValuePairList.add(new BasicNameValuePair("phone", k3Customer.getPhone()));
        nameValuePairList.add(new BasicNameValuePair("valueAddRate", String.valueOf(k3Customer.getValueAddRate())));
        nameValuePairList.add(new BasicNameValuePair("contact", k3Customer.getContact()));
        return nameValuePairList;
    }

    private K3Customer getK3Customer(Customer customer){

        //封装请求参数
        Integer subCompanyId = userSupport.getCompanyIdByUser(customer.getOwner());
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        String cityCode = cityMap.get(subCompanyDO.getSubCompanyCode());
        String customerNumber = cityCode + "."+customer.getCustomerNo();
        UserDO ownerUser = userMapper.findByUserId(customer.getOwner());
        String empNumber = cityCode + "."+ownerUser.getId();

        K3Customer k3Customer = new K3Customer();
        k3Customer.setNumber(customerNumber);
        k3Customer.setName(customer.getCustomerName());

        k3Customer.setEmpNumber(empNumber);
        k3Customer.setEmpName(ownerUser.getRealName());
        String phone = null;
        String address = null;
        String contact = null;

        if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){
            CustomerCompany customerCompany = customer.getCustomerCompany();
            phone = customerCompany.getConnectPhone();
            address = customerCompany.getAddress();
            contact = customerCompany.getConnectRealName();
        }
        if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customer.getCustomerType())){
            CustomerPerson customerPerson = customer.getCustomerPerson();
            phone = customerPerson.getPhone();
            address = customerPerson.getAddress();
            contact = customerPerson.getConnectRealName();
        }
        k3Customer.setPhone(phone);
        k3Customer.setAddress(address);
        k3Customer.setValueAddRate(0);
        k3Customer.setContact(contact);

        List<K3Number> k3NumberList = new ArrayList<>();
        k3Customer.setNumbers(k3NumberList);
        K3Number k3Number1 = new K3Number();
        K3Number k3Number2 = new K3Number();


        k3Number1.setTypeName("客户");
        k3Number1.setNumber(cityCode);
        AreaCityDO areaCityDO = areaCityMapper.findById(subCompanyDO.getCity());
        if(subCompanyDO.getCity()==null){
            k3Number1.setName(subCompanyDO.getSubCompanyName());
        }else{
            k3Number1.setName(areaCityDO.getAbbCn());
        }
        k3Number1.setDetail(false);
        k3Number2.setTypeName("客户");
        k3Number2.setNumber(customerNumber);
        k3Number2.setName(customer.getCustomerName());
        k3Number2.setDetail(true);

        k3NumberList.add(k3Number1);
        k3NumberList.add(k3Number2);
        return k3Customer;
    }
}
