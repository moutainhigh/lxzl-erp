package com.lxzl.erp.web.controller;

import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 15:39 2018/1/13
 * @Modified By:
 */

@RequestMapping("peerDeploymentOrder")
@Controller
@ControllerLog
public class PeerDeploymentOrderController extends BaseController {

    @Autowired
    private PeerDeploymentOrderService peerDeploymentOrderService;

    @Autowired
    private ResultGenerator resultGenerator;
}
