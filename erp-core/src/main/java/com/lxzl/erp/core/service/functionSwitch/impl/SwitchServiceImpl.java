package com.lxzl.erp.core.service.functionSwitch.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.functionSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.functionSwitch.pojo.Switch;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.functionSwitch.SwitchService;
import com.lxzl.erp.core.service.functionSwitch.impl.support.SwitchSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.functionSwitch.SwitchMapper;
import com.lxzl.erp.dataaccess.domain.functionSwitch.SwitchDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ServiceResult<String, String> add(Switch functionSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //接口地址format
        String interfaceUrl = functionSwitch.getInterfaceUrl();
        interfaceUrl = switchSupport.verifyInterfaceUrl(interfaceUrl);

        SwitchDO switchDO = switchMapper.findByInterfaceUrl(interfaceUrl);
        Date now = new Date();
        if(switchDO!=null){
            switchDO.setInterfaceUrl(interfaceUrl);
            switchDO.setIsOpen(CommonConstant.COMMON_CONSTANT_NO);
            switchDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            switchDO.setUpdateTime(now);
            switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            switchMapper.update(switchDO);
        }else{
            switchDO = new SwitchDO();
            switchDO.setCreateTime(now);
            switchDO.setCreateUser(userSupport.getCurrentUserId().toString());
            switchDO.setInterfaceUrl(interfaceUrl);
            switchDO.setIsOpen(CommonConstant.COMMON_CONSTANT_NO);
            switchDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            switchDO.setUpdateTime(now);
            switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            switchMapper.save(switchDO);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> update(Switch functionSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        Date now = new Date();
        SwitchDO dbSwitchDO = switchMapper.findById(functionSwitch.getSwitchId());
        if(dbSwitchDO == null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_NOT_EXISTS);
            return serviceResult;
        }
        String interfaceUrl = switchSupport.verifyInterfaceUrl(functionSwitch.getInterfaceUrl());

        SwitchDO switchDO = switchMapper.findByInterfaceUrl(functionSwitch.getInterfaceUrl());
        if(switchDO!=null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_INTERFACE_URL_EXISTS);
            return serviceResult;
        }
        dbSwitchDO.setInterfaceUrl(interfaceUrl);
        dbSwitchDO.setIsOpen(functionSwitch.getIsOpen());
        dbSwitchDO.setUpdateTime(now);
        dbSwitchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        switchMapper.update(dbSwitchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Switch>> page(SwitchQueryParam switchQueryParam) {

        ServiceResult<String, Page<Switch>> serviceResult = new ServiceResult<>();
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
    public ServiceResult<String, String> delete(Switch functionSwitch) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        Date now = new Date();
        SwitchDO switchDO = switchMapper.findById(functionSwitch.getSwitchId());
        if(switchDO == null){
            serviceResult.setErrorCode(ErrorCode.SWITCH_NOT_EXISTS);
            return serviceResult;
        }
        switchDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        switchDO.setUpdateTime(now);
        switchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        switchMapper.update(switchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

}
