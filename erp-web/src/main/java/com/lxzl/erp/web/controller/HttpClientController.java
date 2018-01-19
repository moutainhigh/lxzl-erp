package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.area.AreaService;
import com.lxzl.erp.core.service.exclt.EXCLService;
import com.lxzl.erp.core.service.getIpAndMac.IpAndMacService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    @Autowired
    EXCLService exclService;

    @Autowired
    IpAndMacService ipAndMacService;

    /**
     * 获取邮政编号
     */
    @RequestMapping("test")
    public Result TestHttpClient(String str) {
        ServiceResult<String, List<String>> stringListServiceResult = areaService.selectPostCodeAndSavaData2postCode();
        return resultGenerator.generate(stringListServiceResult.getErrorCode(), stringListServiceResult.getResult());
    }

    /**
     * 中英文简写
     */
    @RequestMapping("change")
    public Result TestChineseCharToEn(String str) {
        ServiceResult<String, Object> stringListServiceResult = areaService.conversionType();
        return resultGenerator.generate(stringListServiceResult.getErrorCode());
    }

    /**
     * 导入数据
     *
     * @param : EXCTName
     * @param : shilt
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/11 10:32
     * @Return : com.lxzl.se.common.domain.Result
     */
    @RequestMapping("exct")
    public Result EXCTest(String str) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, IOException {
        ServiceResult<String, Map<String, String>> result = exclService.importData(str);
        return resultGenerator.generate(result.getErrorCode(), result.getResult());
    }

    /**
    * 获取客户端的ip和mac地址
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/19 19:16
    * @param : request
    * @Return : com.lxzl.se.common.domain.Result
    */

    @RequestMapping(value = "test11", method = RequestMethod.GET)
    public Result test11(HttpServletRequest request) throws IOException {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        IpAndMac ipAndMacService = this.ipAndMacService.getIpAndMacService(request);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult("IP:"+ipAndMacService.getIp()+",MAC:"+ipAndMacService.getMac());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}