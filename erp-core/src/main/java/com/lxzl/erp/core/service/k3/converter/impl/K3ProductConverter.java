package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormICItem;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingBrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCategoryMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCategoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @Description: 转化k3商品工具类
 * @date 2018/1/23 16:09
 */

@Service
public class K3ProductConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Object data) {
        Product product = (Product) data;
        K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
        K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());

        FormICItem formICItem = new FormICItem();
        formICItem.setModel(product.getProductModel());//型号名称
        formICItem.setName(product.getProductName());//商品名称
        String number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
        formICItem.setNumber(number);//编号
        formICItem.setNumbers(new ItemNumber[]{new ItemNumber(false, "整机", "10", "物料"),//整机
                new ItemNumber(false, k3MappingCategoryDO.getCategoryName(), "10." + k3MappingCategoryDO.getK3CategoryCode(), "物料"),//分类
                new ItemNumber(false, k3MappingBrandDO.getBrandName(), "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode(), "物料"),//品牌
                new ItemNumber(true, product.getProductModel(), number, "物料")});//型号
        return formICItem;
    }

    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;
    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;
}
