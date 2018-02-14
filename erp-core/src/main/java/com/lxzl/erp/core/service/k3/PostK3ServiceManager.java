package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.k3.converter.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostK3ServiceManager {

    public ConvertK3DataService getService(Integer postType){
        if(PostK3Type.POST_K3_TYPE_CUSTOMER.equals(postType)){
            return k3CustomerConverter;
        }
        if(PostK3Type.POST_K3_TYPE_PRODUCT.equals(postType)){
            return k3ProductConverter;
        }

        if(PostK3Type.POST_K3_TYPE_MATERIAL.equals(postType)){
            return k3MaterialConverter;
        }
        if(PostK3Type.POST_K3_TYPE_SUPPLIER.equals(postType)){
            return k3SupplierConverter;
        }
        if(PostK3Type.POST_K3_TYPE_ORDER.equals(postType)){
            return k3OrderConverter;
        }
        if(PostK3Type.POST_K3_TYPE_USER.equals(postType)){
            return k3UserConverter;
        }
        if(PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER.equals(postType)){
            return k3ReturnOrderConverter;
        }
        return null;
    }

    @Autowired
    private K3CustomerConverter k3CustomerConverter;
    @Autowired
    private K3ProductConverter k3ProductConverter;
    @Autowired
    private K3MaterialConverter k3MaterialConverter;
    @Autowired
    private K3SupplierConverter k3SupplierConverter;
    @Autowired
    private K3OrderConverter k3OrderConverter;
    @Autowired
    private K3UserConverter k3UserConverter;
    @Autowired
    private K3ReturnOrderConverter k3ReturnOrderConverter;
}
