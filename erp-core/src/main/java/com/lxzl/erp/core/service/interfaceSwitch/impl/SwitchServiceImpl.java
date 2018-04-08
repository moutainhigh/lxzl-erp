package com.lxzl.erp.core.service.interfaceSwitch.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.interfaceSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.interfaceSwitch.pojo.Switch;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.interfaceSwitch.SwitchService;
import com.lxzl.erp.core.service.interfaceSwitch.impl.support.SwitchSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.functionSwitch.SwitchMapper;
import com.lxzl.erp.dataaccess.domain.interfaceSwitch.SwitchDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 16:20
 */
@Service
public class SwitchServiceImpl implements SwitchService {

    @Autowired
    SwitchMapper switchMapper;

    @Autowired
    UserSupport userSupport;

    @Autowired
    SwitchSupport switchSupport;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> add(Switch interfaceSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            //不为超级管理员
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //接口地址format
        String interfaceUrl = interfaceSwitch.getInterfaceUrl();
        interfaceUrl = switchSupport.verifyInterfaceUrl(interfaceUrl);

        SwitchDO switchDO = switchMapper.findByInterfaceUrl(interfaceUrl);
        Date now = new Date();
        if(switchDO!=null){
            switchDO.setInterfaceUrl(interfaceUrl);
            switchDO.setIsOpen(CommonConstant.COMMON_CONSTANT_NO);
            switchDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            switchDO.setRemark(interfaceSwitch.getRemark());
            switchDO.setUpdateTime(now);
            switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            switchMapper.update(switchDO);
        }else{
            switchDO = new SwitchDO();
            switchDO.setInterfaceUrl(interfaceUrl);
            switchDO.setIsOpen(CommonConstant.COMMON_CONSTANT_NO);
            switchDO.setRemark(interfaceSwitch.getRemark());
            switchDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            switchDO.setCreateTime(now);
            switchDO.setCreateUser(userSupport.getCurrentUserId().toString());
            switchDO.setUpdateTime(now);
            switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            switchMapper.save(switchDO);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> update(Switch interfaceSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            //不为超级管理员
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        Date now = new Date();
        SwitchDO dbSwitchDO = switchMapper.findById(interfaceSwitch.getSwitchId());
        if(dbSwitchDO == null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_NOT_EXISTS);
            return serviceResult;
        }
        String interfaceUrl = switchSupport.verifyInterfaceUrl(interfaceSwitch.getInterfaceUrl());

        SwitchDO switchDO = switchMapper.findByInterfaceUrl(interfaceSwitch.getInterfaceUrl());
        if(switchDO!=null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_INTERFACE_URL_EXISTS);
            return serviceResult;
        }
        dbSwitchDO.setInterfaceUrl(interfaceUrl);
        dbSwitchDO.setIsOpen(interfaceSwitch.getIsOpen());
        dbSwitchDO.setRemark(interfaceSwitch.getRemark());
        dbSwitchDO.setUpdateTime(now);
        dbSwitchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        switchMapper.update(dbSwitchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Switch>> page(SwitchQueryParam switchQueryParam) {
        ServiceResult<String, Page<Switch>> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            //不为超级管理员
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        PageQuery pageQuery = new PageQuery(switchQueryParam.getPageNo(), switchQueryParam.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageQuery.getStart());
        map.put("pageSize", pageQuery.getPageSize());
        map.put("switchQueryParam", switchQueryParam);
        Integer switchCount = switchMapper.findSwitchCountByParam(map);
        List<SwitchDO> switchDOList = switchMapper.findSwitchByParam(map);

        List<Switch> switchList = ConverterUtil.convertList(switchDOList, Switch.class);

        Page<Switch> switchPage = new Page<>(switchList, switchCount, switchQueryParam.getPageNo(), switchQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(switchPage);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> delete(Switch interfaceSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            //不为超级管理员
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        Date now = new Date();
        SwitchDO switchDO = switchMapper.findById(interfaceSwitch.getSwitchId());
        if(switchDO == null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_NOT_EXISTS);
            return serviceResult;
        }
        switchDO.setRemark(interfaceSwitch.getRemark());
        switchDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        switchDO.setUpdateTime(now);
        switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        switchMapper.update(switchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

}
