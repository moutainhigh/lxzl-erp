package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormOrganization;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
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
    public Object getK3PostWebServiceData(Object data) {
        Customer customer = (Customer)data;

        Integer subCompanyId = userSupport.getCompanyIdByUser(customer.getOwner());
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        String cityCode = cityMap.get(subCompanyDO.getSubCompanyCode());
        String customerNumber = cityCode + "."+customer.getCustomerNo();
        UserDO ownerUser = userMapper.findByUserId(customer.getOwner());
        String empNumber = cityCode + "."+ownerUser.getId();
        FormOrganization formOrganization = new FormOrganization();
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
        AreaCityDO areaCityDO = areaCityMapper.findById(subCompanyDO.getCity());
        String name = null;
        if(subCompanyDO.getCity()==null){
            name = subCompanyDO.getSubCompanyName();
        }else{
            name = areaCityDO.getAbbCn();
        }

        formOrganization.setNumber(customerNumber);
        formOrganization.setName(customer.getCustomerName());
        formOrganization.setEmpNumber(empNumber);
        formOrganization.setEmpName(ownerUser.getRealName());
        formOrganization.setPhone(phone);
        formOrganization.setContact(contact);
        formOrganization.setAddress(address);
        formOrganization.setNumbers(new ItemNumber[]{new ItemNumber(false, name, cityCode, "客户"),
                new ItemNumber(true, customer.getCustomerName(), customerNumber, "客户")});


        FormOrganization cust=new FormOrganization();
        cust.setNumber("01.0002300000");
        cust.setName("aaaa什么");
        cust.setEmpNumber("00.0002");
        cust.setEmpName("张华");
        cust.setPhone("138000138000");
        cust.setContact("张三");
        cust.setAddress("");
        cust.setNumbers(new ItemNumber[] {
                new ItemNumber(false,"深圳","01","客户"),
                new ItemNumber(false,"aaaa什么","01.0002300000","客户")
        });
        return formOrganization;
    }
}
