package com.lxzl.erp.web.controller;

import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-29 15:03
 */
@Controller
@ControllerLog
@RequestMapping("test")
public class TestController extends BaseController {

    @RequestMapping("file")
    public String filePage(){
        return "/example/file";
    }
}
