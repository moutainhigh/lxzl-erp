package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormUser;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K3UserConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType , Object data) {
        User user = (User)data;
        FormUser formUser = new FormUser();
        Integer subCompanyId = userSupport.getCompanyIdByUser(user.getUserId());
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
        formUser.setCompanyName(k3MappingSubCompanyDO.getSubCompanyName());
        formUser.setUserID(k3MappingSubCompanyDO.getK3SubCompanyCode()+"."+user.getUserId());
        formUser.setUserName(user.getRealName());
        return formUser;
    }

    @Override
    public void successNotify(K3SendRecordDO k3SendRecordDO) {

    }

    @Override
    public void failNotify(K3SendRecordDO k3SendRecordDO) {

    }

    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
}
