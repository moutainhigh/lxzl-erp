package com.lxzl.erp.core.service.k3.converter.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CustomerIndustry;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.domain.k3.pojo.K3MappingIndustry;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormOrganization;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ResultData;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult;
import com.lxzl.erp.core.service.k3.K3Support;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingIndustryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingSubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingIndustryDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingSubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class K3CustomerConverter implements ConvertK3DataService{
    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType,Object data) throws Exception{
        Customer customer = (Customer)data;

        Integer subCompanyId = userSupport.getCompanyIdByUser(customer.getOwner());
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        String cityCode = k3Support.getK3CityCode(subCompanyDO.getSubCompanyCode());
        K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(customer.getCustomerNo());
        if(k3MappingCustomerDO==null){
            k3MappingCustomerDO = new K3MappingCustomerDO();
            k3MappingCustomerDO.setCustomerName(customer.getCustomerName());
            k3MappingCustomerDO.setErpCustomerCode(customer.getCustomerNo());

            String customerNumber = cityCode + "."+customer.getCustomerNo();
            k3MappingCustomerDO.setK3CustomerCode(customerNumber);
            k3MappingCustomerMapper.save(k3MappingCustomerDO);
        }else if(StringUtil.isEmpty(k3MappingCustomerDO.getK3CustomerCode())){
            String customerNumber = cityCode + "."+customer.getCustomerNo();
            k3MappingCustomerDO.setK3CustomerCode(customerNumber);
            k3MappingCustomerMapper.update(k3MappingCustomerDO);
        }
        UserDO ownerUser = userMapper.findByUserId(customer.getOwner());
        String empNumber = cityCode + "."+ownerUser.getId();
        FormOrganization formOrganization = new FormOrganization();
        String phone = null;
        String address = null;
        String contact = null;
        String industryNumber = null;
        if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){
            CustomerCompany customerCompany = customer.getCustomerCompany();
            K3MappingIndustryDO k3MappingIndustryDO = null;
            if(StringUtil.isEmpty(customerCompany.getIndustry())){
                k3MappingIndustryDO = k3MappingIndustryMapper.findByErpCode(CustomerIndustry.CUSTOMER_INDUSTRY_OTHER.toString());
            }else{
                k3MappingIndustryDO = k3MappingIndustryMapper.findByErpCode(customerCompany.getIndustry());
            }
            industryNumber = k3MappingIndustryDO.getK3IndustryCode();
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
        industryNumber = industryNumber==null?"":industryNumber;
        formOrganization.setIndustryNumber(industryNumber);
//        AreaCityDO areaCityDO = areaCityMapper.findById(subCompanyDO.getCity());
//        String name = null;
//        if(subCompanyDO.getCity()==null){
//            name = subCompanyDO.getSubCompanyName();
//        }else{
//            name = areaCityDO.getAbbCn();
//        }

        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
        String name = k3MappingSubCompanyDO.getSubCompanyName();

        formOrganization.setNumber(k3MappingCustomerDO.getK3CustomerCode());
        formOrganization.setName(customer.getCustomerName());
        formOrganization.setEmpNumber(empNumber);
        formOrganization.setEmpName(ownerUser.getRealName());
        formOrganization.setPhone(phone==null?"":phone);
        formOrganization.setContact(contact==null?"":contact);
        formOrganization.setAddress(address==null?"":address);
        formOrganization.setNumbers(new ItemNumber[]{new ItemNumber(false, name, cityCode, "客户"),
                new ItemNumber(true, customer.getCustomerName(), k3MappingCustomerDO.getK3CustomerCode(), "客户")});
        return formOrganization;
    }

    @Override
    public void successNotify(K3SendRecordDO k3SendRecordDO) {
        String responseJson = k3SendRecordDO.getResponseJson();
        ServiceResult serviceResult = JSON.parseObject(responseJson,ServiceResult.class);
        Integer customerId = k3SendRecordDO.getRecordReferId();
        CustomerDO customerDO = customerMapper.findById(customerId);
        K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(customerDO.getCustomerNo());

        if(serviceResult.getData()!=null&&serviceResult.getData().length>0){
            Map<String,String> map = new HashMap<>();
            for(ResultData resultData : serviceResult.getData()){
                map.put(resultData.getKey(),resultData.getValue());
            }
            if(!k3MappingCustomerDO.getK3CustomerCode().equals(map.get("k3CustomerCode"))){
                k3MappingCustomerDO.setK3CustomerCode(map.get("k3CustomerCode"));
                k3MappingCustomerMapper.update(k3MappingCustomerDO);
            }
        }
    }


    @Override
    public void failNotify(K3SendRecordDO k3SendRecordDO) {

    }


    public static void main(String[] args) {
        String s = "{\"data\":[],\"result\":\"OK\",\"status\":0}";
        ServiceResult serviceResult = JSON.parseObject(s,ServiceResult.class);
    }

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private K3Support k3Support;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private K3MappingIndustryMapper k3MappingIndustryMapper;
    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;
}
