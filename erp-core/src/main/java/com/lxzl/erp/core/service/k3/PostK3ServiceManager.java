package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.k3.converter.impl.K3CustomerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostK3ServiceManager {

    public ConvertK3DataService getService(Integer postType){
        if(PostK3Type.POST_K3_TYPE_CUSTOMER.equals(postType)){
            return k3CustomerConverter;
        }
        return null;
    }

    @Autowired
    private K3CustomerConverter k3CustomerConverter;
}
