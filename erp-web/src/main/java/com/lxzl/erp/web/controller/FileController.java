package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.system.ImageService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-25 13:36
 */
@Controller
@ControllerLog
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return new Result(ErrorCode.SYSTEM_ERROR, ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR), false);
        }
        ServiceResult<String, String> serviceResult = fileService.uploadFile(file);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
