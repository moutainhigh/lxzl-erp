package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSupply;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;



@Service
public class K3SupplierConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType , Object data) {
        Supplier supplier = (Supplier) data;
        FormSupply formSupply = new FormSupply();
        formSupply.setNumber(supplier.getSupplierNo());
        formSupply.setName(supplier.getSupplierName());
        String provinceName = getString(supplier.getProvinceName())+" ";
        String cityName = getString(supplier.getCityName())+" ";
        String districtName = getString(supplier.getDistrictName())+" ";
        String address = getString(supplier.getAddress());
        formSupply.setAddress(provinceName+cityName+districtName+address);
        formSupply.setBank(getString(supplier.getBeneficiaryBankName()));
        formSupply.setAccount(getString(supplier.getBeneficiaryAccount()));
        formSupply.setContact(getString(supplier.getContactName()));
        formSupply.setFax("");
        formSupply.setMobilePhone(getString(supplier.getContactPhone()));
        formSupply.setPhone("");
        formSupply.setNumbers(new ItemNumber[]{new ItemNumber(true, supplier.getSupplierName(), supplier.getSupplierNo(), "供应商")});
//        formSupply.setNumbers(new ItemNumber[]{new ItemNumber(true, "深圳一二三科技有限公司", "999232229999", "供应商")});formSupply.setNumber("999232229999");
//        formSupply.setName("深圳一二三科技有限公司");
//        formSupply.setAddress("地址1123123");
//        formSupply.setBank("88888812232");
//        formSupply.setAccount("平安银行深圳支行");
//        formSupply.setContact("张三");
//        formSupply.setFax("0755-12345678");
//        formSupply.setMobilePhone("13999992999");
//        formSupply.setPhone("0755-456789123");
//        formSupply.setNumbers(new ItemNumber[]{new ItemNumber(true, "深圳一二三科技有限公司", "999232229999", "供应商")});

        return formSupply;
    }

    @Override
    public void successNotify(K3SendRecordDO k3SendRecordDO) {

    }

    @Override
    public void failNotify(K3SendRecordDO k3SendRecordDO) {

    }

    private String getString(String s ){
        if(s==null){
            return "";
        }
        if(StringUtil.isBlank(s)){
            return "";
        }
        return s;
    }
}

