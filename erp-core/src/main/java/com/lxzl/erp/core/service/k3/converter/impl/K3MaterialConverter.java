package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormICItem;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingBrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingMaterialTypeMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingMaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @Description: 转化k3商品工具类
 * @date 2018/1/23 16:09
 */

@Service
public class K3MaterialConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType , Object data) {
        Material material = (Material) data;
        K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(material.getMaterialType().toString());
        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(material.getBrandId().toString());
        FormICItem formICItem = new FormICItem();
        formICItem.setModel(material.getMaterialModel());//型号名称
        formICItem.setName(material.getMaterialName());//商品名称
        String number = "";
        if(StringUtil.isNotEmpty(material.getK3MaterialNo())){
            number = material.getK3MaterialNo();
            String[] ss = number.split("\\.");
            formICItem.setNumber(number);//编号
            formICItem.setNumbers(new ItemNumber[]{new ItemNumber(false, "配件", ss[0], "物料"),//配件
                    new ItemNumber(false, k3MappingMaterialTypeDO.getMaterialTypeName(), ss[0]+"."+ss[1], "物料"),//分类
                    new ItemNumber(false, k3MappingBrandDO.getBrandName(), ss[0]+"."+ss[1]+"."+ss[2], "物料"),//品牌
                    new ItemNumber(true, material.getMaterialModel(), number, "物料")});//型号
        }else{
            number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + material.getMaterialModel();
            formICItem.setNumber(number);//编号
            formICItem.setNumbers(new ItemNumber[]{new ItemNumber(false, "配件", "20", "物料"),//配件
                    new ItemNumber(false, k3MappingMaterialTypeDO.getMaterialTypeName(), "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode(), "物料"),//分类
                    new ItemNumber(false, k3MappingBrandDO.getBrandName(), "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode(), "物料"),//品牌
                    new ItemNumber(true, material.getMaterialModel(), number, "物料")});//型号
        }

        return formICItem;
    }

    @Override
    public void successNotify(K3SendRecordDO k3SendRecordDO) {

    }

    @Override
    public void failNotify(K3SendRecordDO k3SendRecordDO) {

    }


    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;
    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;
}
