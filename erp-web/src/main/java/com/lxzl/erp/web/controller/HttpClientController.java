package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.area.AreaService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@RequestMapping("http")
@Controller
public class HttpClientController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    AreaService areaService;

    /**
     * 获取邮政编号
     * */
    @RequestMapping("test")
    public Result TestHttpClient(String str){
       ServiceResult<String, List<String>> stringListServiceResult = areaService.selectPostCodeAndSavaData2postCode();
        return resultGenerator.generate(stringListServiceResult.getErrorCode(),stringListServiceResult.getResult());
    }

    /**
     * 中英文简写
     * */
    @RequestMapping("change")
    public Result TestChineseCharToEn(String str) {
        ServiceResult<String, Object> stringListServiceResult =  areaService.conversionType();
        return resultGenerator.generate(stringListServiceResult.getErrorCode());
    }

}
