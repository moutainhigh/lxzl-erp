package com.lxzl.erp.web.controller;

import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-19 19:20
 */
@Controller
@ControllerLog
@RequestMapping("QRCode")
public class QRCodeController extends BaseController {
    @RequestMapping("/scan")
    public String scan() {
        String idCode = getStringParameter("idCode");
        return "/support";
    }
}
